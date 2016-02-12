package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.objectivefunctions;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.FluxValueMap;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.AbstractObjectiveFunction;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.InvalidObjectiveFunctionConfiguration;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.ObjectiveFunctionParameterType;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.RegulatoryNetworkSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.IntegratedSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.controlcenter.IntegratedSimulationControlCenter;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;
import pt.uminho.ceb.biosystems.mew.solvers.SolverType;
import pt.uminho.ceb.biosystems.mew.utilities.math.MathUtils;

public class YieldMinimumBiomassObjectiveFunctionRegulatory extends AbstractObjectiveFunction 
{
	
	private static final long serialVersionUID 		= 1L;
	public static final String	ID					= "YIELD";
	
	public static final String	YIELD_PARAM_BIOMASS					= "Biomass";
	public static final String	YIELD_PARAM_PRODUCT					= "Product";
	public static final String	YIELD_PARAM_MIN_BIOMASS_PERCENTAGE	= "MinBiomassPercentage";
	public static final String	YIELD_PARAM_SOLVER					= "Solver";

	String biomassId; 
	String desiredFluxId;
	protected final double worstFitness = Double.NEGATIVE_INFINITY;
	protected SolverType solver;

	protected double minimumBiomassPercentage = 0.1;
	protected double minimumBiomassValue = 0.0;
	protected boolean biomassComputed = false;
	
	@Override
	public Map<String, ObjectiveFunctionParameterType> loadParameters() {
		Map<String, ObjectiveFunctionParameterType> myparams = new LinkedHashMap<>();
		myparams.put(YIELD_PARAM_BIOMASS, ObjectiveFunctionParameterType.REACTION_BIOMASS);
		myparams.put(YIELD_PARAM_PRODUCT, ObjectiveFunctionParameterType.REACTION_PRODUCT);
		myparams.put(YIELD_PARAM_MIN_BIOMASS_PERCENTAGE, ObjectiveFunctionParameterType.DOUBLE);
		myparams.put(YIELD_PARAM_SOLVER, ObjectiveFunctionParameterType.SOLVER);
		return Collections.unmodifiableMap(myparams);
	}
	
	public YieldMinimumBiomassObjectiveFunctionRegulatory() {
		super();
	}
	
	public YieldMinimumBiomassObjectiveFunctionRegulatory(Map<String, Object> configuration) throws InvalidObjectiveFunctionConfiguration {
		super(configuration);
	}

	public YieldMinimumBiomassObjectiveFunctionRegulatory(String biomassId, String desiredFluxId, double minimumBiomass, SolverType solver) 
	{
		this.biomassId = biomassId;
		this.desiredFluxId = desiredFluxId;
		this.minimumBiomassPercentage = minimumBiomass;
		this.solver = solver;		
	}
	
	@Override
	protected void processParams(Object... params) {
		setParameterValue(YIELD_PARAM_BIOMASS, params[0]);
		setParameterValue(YIELD_PARAM_PRODUCT, params[1]);
		setParameterValue(YIELD_PARAM_MIN_BIOMASS_PERCENTAGE, params[2]);
		setParameterValue(YIELD_PARAM_SOLVER, params[3]);
	}
	
	
	public double evaluate(SteadyStateSimulationResult simResult)
	{
		
		if(!biomassComputed)
			computeReferenceBiomassValue(simResult);
		FluxValueMap fluxValues = simResult.getFluxValues();

		double biomassValue = fluxValues.getValue(biomassId);

		
		double desiredFlux = fluxValues.getValue(desiredFluxId);

		
		double fitness = 0.0;

		if (biomassValue>= (minimumBiomassPercentage*minimumBiomassValue)){
			
			fitness = desiredFlux;
			
		}
		
		

		return fitness;
	}
	
	protected void computeReferenceBiomassValue(SteadyStateSimulationResult simResult){
		IntegratedSimulationResult intResult = (IntegratedSimulationResult) simResult;
		ISteadyStateModel model = simResult.getModel();
		
		String IntsimulationMethod = "";
		String metabolimethodtype ="";
		String regulatorysimmethodtype = "";
		
	
		if(intResult.getMethod().equals(IntegratedSimulationMethods.SRFBA))
			IntsimulationMethod = IntegratedSimulationMethods.SRFBA;
		else{
			IntsimulationMethod = IntegratedSimulationMethods.INTEGRATED_BRN;
			metabolimethodtype = intResult.getMetSimulMethod();
			regulatorysimmethodtype = RegulatoryNetworkSimulationMethods.BRNV;
		}
		

		EnvironmentalConditions env = simResult.getEnvironmentalConditions();
		VariablesContainer varcont = intResult.getInitialUsedVariablesContainer(); 
		
		HashSet<String> falsenodes = null;
		falsenodes = intResult.getInitialusedfalsenodes();

		
		IntegratedSimulationControlCenter simulationControlCenter = new IntegratedSimulationControlCenter(env, null, model, IntsimulationMethod, metabolimethodtype, regulatorysimmethodtype, varcont, falsenodes, true, solver);
		

		
		//SteadyStateSimulationResult solution = null;	
		IntegratedSimulationResult solution = null;
		
		try {
			solution = (IntegratedSimulationResult) simulationControlCenter.simulate();
		} catch (Exception e) {
			System.err.println("YieldMinimumBiomassObjectiveFunction: could not compute reference biomass value");
			e.printStackTrace();
		}
		
		if(solution!=null){
			double biomass = solution.getFluxValues().getValue(biomassId);

			minimumBiomassValue = biomass * minimumBiomassPercentage;

		}
		

		biomassComputed = true;
	}


	@Override
	public double getWorstFitness() {
		return worstFitness;
	}


	@Override
	public boolean isMaximization() {
		return true;
	}


	/* (non-Javadoc)
	 * @see metabolic.optimization.objectivefunctions.interfaces.IObjectiveFunction#getUnnormalizedFitness(double)
	 */
	@Override
	public double getUnnormalizedFitness(double fit) {
		return fit;
	}


	/* (non-Javadoc)
	 * @see metabolic.optimization.objectivefunctions.interfaces.IObjectiveFunction#getShortString()
	 */
	@Override
	public String getShortString() {
		return toString();
	}


	public String getBiomassId() {
		return biomassId;
	}


	public String getDesiredFluxId() {
		return desiredFluxId;
	}


	public double getMinimumBiomassPercentage() {
		return minimumBiomassPercentage;
	}


	@Override
	public String getLatexString() {
		return "$" + getLatexFormula() + "$";
	}
	
	@Override
	public String getLatexFormula() {
		Double minBiomassPercentage = (Double) getParameterValue(YIELD_PARAM_MIN_BIOMASS_PERCENTAGE);
		double percentage = MathUtils.round(minBiomassPercentage * 100, 2);
		return "$YIELD = max (\\text{" + getParameterValue(YIELD_PARAM_PRODUCT) + "}); biomass \\ge " + percentage + "\\% wt$";
		
	}
	
	@Override
	public String getBuilderString() {
		return getID() + "(" + getParameterValue(YIELD_PARAM_BIOMASS) + "," + getParameterValue(YIELD_PARAM_PRODUCT) + "," + getParameterValue(YIELD_PARAM_MIN_BIOMASS_PERCENTAGE) + "," + getParameterValue(YIELD_PARAM_SOLVER) + ")";
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	public String toString() {
		Double minBiomassPercentage = (Double) getParameterValue(YIELD_PARAM_MIN_BIOMASS_PERCENTAGE);
		String productID = (String) getParameterValue(YIELD_PARAM_PRODUCT);
		double percentage = MathUtils.round(minBiomassPercentage * 100, 2);
		return "YIELD> percentage=" + percentage + ";target=" + productID;
	}
	
}
