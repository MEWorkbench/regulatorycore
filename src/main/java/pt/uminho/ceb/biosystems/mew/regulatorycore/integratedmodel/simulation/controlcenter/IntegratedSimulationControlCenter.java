package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.controlcenter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.AbstractSimulationSteadyStateControlCenter;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.IntegratedSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatorySimulationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.methods.SRFBA;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.methods.doublelayer.IntegratedBRN;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;

public class IntegratedSimulationControlCenter extends AbstractSimulationSteadyStateControlCenter implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The regulatory geneticconditions. */
	protected RegulatoryGeneticConditions regulatoryGeneticconditions;
    
    /** The IntegratedMethodsFactory factory. */
    protected static IntegratedMethodsFactory factory;
    
    /** The metabolic simulation method. */
    protected String metaboliSimulationMethod;
    
    protected String regulatoryNetworkSimulationMethod;
    
	
	static{
		LinkedHashMap<String, Class<?>> mapMethods = new LinkedHashMap<String,Class<?>>();
		mapMethods.put(IntegratedSimulationMethods.INTEGRATED_BRN, IntegratedBRN.class);
		mapMethods.put(IntegratedSimulationMethods.SRFBA, SRFBA.class);
		factory = new IntegratedMethodsFactory(mapMethods);
		
	}
	

	/**
	 * Instantiates a new integrated simulation control center.
	 *
	 * @param environmentalConditions the environmental conditions
	 * @param geneticConditions the genetic conditions
	 * @param model the model
	 * @param methodType the method type
	 * @param metabolimethodtype the metabolimethodtype
	 * @param variables the variables
	 * @param falsenodes the falsenodes
	 */
	public IntegratedSimulationControlCenter(
			EnvironmentalConditions environmentalConditions,
			RegulatoryGeneticConditions geneticConditions, 
			ISteadyStateModel model,
			String methodType,
			String metabolimethodtype,
			String regulatorysimmethodtype,
			VariablesContainer variables,
			HashSet<String> falsenodes,
			boolean isMaximization,
			String solver 
			) {
		super(environmentalConditions, geneticConditions, model, methodType);

		setGeneticConditions(geneticConditions);
		setvariables(variables);
		setMetabolicMethodType(metabolimethodtype);
		setRegulatoryNetworkSimulationMethodType(regulatorysimmethodtype);
		setFalseValuesInitStep(falsenodes);
		
		
		isOverUnderSimulation(false); // to be changed
		
		//setObjectiveFunction(obj_coef);
		setMaximization(isMaximization);
		setSolver(solver);
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.AbstractSimulationSteadyStateControlCenter#getFactory()
	 */
	@Override
	protected IntegratedMethodsFactory getFactory() {
		// TODO Auto-generated method stub
		return factory;
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.AbstractSimulationSteadyStateControlCenter#addUnderOverRef()
	 */
	@Override
	public void addUnderOverRef() throws Exception {
		// TODO Auto-generated method stub	
	}
	
	
	/**
	 * Sets the solver.
	 *
	 * @param solverType the new solver
	 */
	public void setSolver(String solverType){
		addProperty(RegulatorySimulationProperties.SOLVER, solverType);	
	}
	
	
	/**
	 * Checks if is maximization.
	 *
	 * @return the boolean
	 */
	public Boolean isMaximization() {
		return (Boolean) (getProperty(RegulatorySimulationProperties.IS_MAXIMIZATION));
	}

	/**
	 * Sets the maximization.
	 *
	 * @param isMaximization the new maximization
	 */
	public void setMaximization(boolean isMaximization) {
		addProperty(RegulatorySimulationProperties.IS_MAXIMIZATION, isMaximization);
	}

	
	/**
	 * Sets the variables.
	 *
	 * @param variables the new variables
	 */
	public void setvariables(VariablesContainer variables){
		addProperty(RegulatorySimulationProperties.VARIABLES_CONTAINER, variables);
	}
	
	/**
	 * Gets the variables container.
	 *
	 * @return the variables container
	 */
	public VariablesContainer getVariablesContainer(){
		return (VariablesContainer)getProperty(RegulatorySimulationProperties.VARIABLES_CONTAINER);
	}
	
	/**
	 * Sets the metabolic method type.
	 *
	 * @param method the new metabolic method type
	 */
	public void setMetabolicMethodType(String method){
		addProperty(RegulatorySimulationProperties.METABOLIC_SIMULATION_METHOD, method);
	}
	
	/**
	 * Gets the metabolic method type.
	 *
	 * @return the metabolic method type
	 */
	public String getMetabolicMethodType(){
		
		return (String)getProperty(RegulatorySimulationProperties.METABOLIC_SIMULATION_METHOD);
	}
	
	
	public void setRegulatoryNetworkSimulationMethodType(String method){
		addProperty(RegulatorySimulationProperties.REGULATORY_NETWORK_SIMULATION_METHOD, method);
	}
	
	
    public String getRegulatoryNetworkSimulationMethodType(){
		
		return (String)getProperty(RegulatorySimulationProperties.REGULATORY_NETWORK_SIMULATION_METHOD);
	}
	
	
	/**
	 * Sets the Initial false values.
	 *
	 * @param falsenodes the new false values init step
	 */
	public void setFalseValuesInitStep(HashSet<String> falsenodes){
		
		addProperty(RegulatorySimulationProperties.FALSE_VALUES_INITSTEP, falsenodes);
	}
	
	
	
	public void setObjectiveFunction(Map<String, Double> obj_fun){
		addProperty(RegulatorySimulationProperties.OBJECTIVE_FUNCTION, obj_fun);
	}


	
	/**
	 * Regist method.
	 *
	 * @param methodId the method id
	 * @param klass the klass
	 * @throws Exception the exception
	 */
	static public void registMethod(String methodId, Class<?> klass) throws Exception{
		factory.addSimulationMethod(methodId, klass);
	}
	
	/**
	 * Gets the registered methods.
	 *
	 * @return the registered methods
	 */
	public static Set<String> getRegisteredMethods(){
		return factory.getRegisteredMethods();
	}
	
	/**
	 * Register method.
	 *
	 * @param id the id
	 * @param method the method
	 */
	public static void registerMethod(String id, Class<?> method) {
		factory.registerMethod(id, method);
		
	}
	
	public void isOverUnderSimulation(boolean bol){
		addProperty(RegulatorySimulationProperties.IS_OVERUNDER_SIMULATION, bol);
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see metabolic.simulation.components.AbstractSimulationSteadyStateControlCenter#setGeneticConditions(metabolic.simulation.components.GeneticConditions)
	 */
	@Override
	public void setGeneticConditions(GeneticConditions geneticConditions) {

		addProperty(RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS, geneticConditions);
	}
	
	/* (non-Javadoc)
	 * @see metabolic.simulation.components.AbstractSimulationSteadyStateControlCenter#getGeneticConditions()
	 */
	@Override
	public GeneticConditions getGeneticConditions()
	{    
	
		return (RegulatoryGeneticConditions)getProperty(RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS);		
	}
	
	
    /* (non-Javadoc)
     * @see metabolic.simulation.components.AbstractSimulationSteadyStateControlCenter#simulate()
     */
    @Override
	public SteadyStateSimulationResult simulate() throws Exception {
		if(isUnderOverSimulation() && getUnderOverRef() == null){
			addUnderOverRef();
		}
		
		this.lastMethod = getFactory().getMethod(this.methodType, methodProperties, model);
		
		return (IntegratedSimulationResult)lastMethod.simulate();
		
	}
    

	

}
