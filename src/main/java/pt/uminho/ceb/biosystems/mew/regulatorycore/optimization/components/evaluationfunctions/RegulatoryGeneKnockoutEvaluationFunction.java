package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.evaluationfunctions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.jecoli.algorithm.components.evaluationfunction.AbstractMultiobjectiveEvaluationFunction;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.evaluationfunction.IEvaluationFunction;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.evaluationfunction.InvalidEvaluationFunctionInputDataException;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.representation.IRepresentation;
import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.evaluationfunction.IOptimizationEvaluationFunction;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.IObjectiveFunction;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.strainoptimizationalgorithms.jecoli.components.decoder.ISteadyStateDecoder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatorySimulationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.controlcenter.IntegratedSimulationControlCenter;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPSolutionType;

public class RegulatoryGeneKnockoutEvaluationFunction extends AbstractMultiobjectiveEvaluationFunction<IRepresentation> implements IOptimizationEvaluationFunction {

	/**
	 * 
	 */
	
	protected final boolean debug = false;

	protected List<IObjectiveFunction> objectiveFunctions; 
	
	protected IIntegratedStedystateModel model;
	
	protected ISteadyStateDecoder decoder;
	
	protected IntegratedSimulationControlCenter controlCenter;
	
	protected int numberOfObjectives = 1;
	
	protected String solver;
	
	protected String simulationMethod;
	
	protected String metabsimulationMethod ="";
	
	protected String regulatorysimulationMethod;
	
	protected VariablesContainer varscontainer;
	
	protected EnvironmentalConditions environmentalConditions;
	
	
	protected HashSet<String> initialFalseNodes;
	
	private static final long serialVersionUID = 1L;

	public RegulatoryGeneKnockoutEvaluationFunction(IIntegratedStedystateModel model, 
			ISteadyStateDecoder decoder, 
			List<IObjectiveFunction> objectiveFunctions, 
			EnvironmentalConditions envConds,
			VariablesContainer varscontainer,
			String simulationMethod,
			String metabolicsimulationmethod,
			String regulatorysimulationMet,
			HashSet<String> initialFalseNodes,
			String solver) {
		this.model = model;
		this.decoder = decoder;
		this.objectiveFunctions = objectiveFunctions;
		this.environmentalConditions = envConds;
		this.varscontainer = varscontainer;
		this.solver = solver;
		this.simulationMethod = simulationMethod;
		this.metabsimulationMethod = metabolicsimulationmethod;
		this.regulatorysimulationMethod = regulatorysimulationMet;
		
		this.initialFalseNodes = initialFalseNodes;
		this.controlCenter = new IntegratedSimulationControlCenter(envConds, null, model, simulationMethod, metabolicsimulationmethod,regulatorysimulationMet, varscontainer, initialFalseNodes, true,solver);
		this.numberOfObjectives = objectiveFunctions.size();
	}
	
	
	public ISteadyStateDecoder getDecoder (){
		return decoder;
	}
	
	public void setSolver(String solver) {
		controlCenter.setSolver(solver);
	}
	
	public void setMethodType(String methodType){
		controlCenter.setMethodType(methodType);
	}
	
	public String getMethodType()
	{
		return controlCenter.getMethodType();
	}
	
	public void setEnvironmentalConditions(EnvironmentalConditions environmentalConditions) {
			controlCenter.setEnvironmentalConditions(environmentalConditions);
	}

	@Override
	public int getNumberOfObjectives() {
		return numberOfObjectives;
	}

	public void setNumberOfObjectives(int numberOfObjectives) {
		this.numberOfObjectives = numberOfObjectives;
	}

	public Object getSimulationProperty(String propertyKey)
	{
		return controlCenter.getProperty(propertyKey);
	}
	
	public void setSimulationProperty(String key, Object value)
	{
		controlCenter.setSimulationProperty(key, value);
	}
	
	public IntegratedSimulationControlCenter getSimulationControlCenter(){
		return this.controlCenter;
	}
	
	
	public void setOverUnderReferenceDistribution(Map<String,Double> reference){
		this.setSimulationProperty(RegulatorySimulationProperties.OVERUNDER_REFERENCE_FLUXES, reference);
	}
	
	
	protected VariablesContainer setVariables(List<String> userVariablesTrue){
		
		VariablesContainer cont = (VariablesContainer) this.model.getVariablescontainer().clone();
		
		cont.setVariablesssToActive((ArrayList<String>) userVariablesTrue);
		
		return cont;	
	}
	
	
	
	@Override
	public void verifyInputData()
			throws InvalidEvaluationFunctionInputDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IEvaluationFunction<IRepresentation> deepCopy() throws Exception {
		// TODO Auto-generated method stub
		return new RegulatoryGeneKnockoutEvaluationFunction(this.model, this.decoder, this.objectiveFunctions, controlCenter.getEnvironmentalConditions(), this.varscontainer, this.simulationMethod, this.metabsimulationMethod,this.regulatorysimulationMethod, this.initialFalseNodes,this.solver);
	}
     
	/**
	 * @return the objectiveFunctions
	 */
	public List<IObjectiveFunction> getObjectiveFunctions() {
		return objectiveFunctions;
	}
	
	

	@Override
	public Double[] evaluateMO(IRepresentation solution)
			throws Exception {
		Double[] resultList =  new Double[objectiveFunctions.size()];
		try {
			RegulatoryGeneticConditions gc = null;
			gc = (RegulatoryGeneticConditions) decoder.decode(solution);
			
			controlCenter.setGeneticConditions(gc);	
			
	
			
			IntegratedSimulationResult result = (IntegratedSimulationResult) controlCenter.simulate();
			double biomassflux = result.getFluxValues().getValue(model.getBiomassFlux());

			double fitness = 0.0;
			if (result != null && (result.getSolutionType().equals(LPSolutionType.OPTIMAL)
					|| result.getSolutionType().equals(LPSolutionType.FEASIBLE)))
			{
				int size = objectiveFunctions.size();
				for(int i=0;i<size;i++)
				{
					IObjectiveFunction of = objectiveFunctions.get(i);
					fitness = of.evaluate(result);
					resultList[i] = fitness;
				}
				
			}else{
				int size = objectiveFunctions.size();
				for(int i=0;i<size;i++)
				{
					//NOTE: this may not be correct for clashing OFs, i.e., max vs min. Should be evaluated separately for each OF. (of.isMaximization ?)
					IObjectiveFunction of = objectiveFunctions.get(i);
					resultList[i] = of.getWorstFitness();
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return resultList;
	}

}
