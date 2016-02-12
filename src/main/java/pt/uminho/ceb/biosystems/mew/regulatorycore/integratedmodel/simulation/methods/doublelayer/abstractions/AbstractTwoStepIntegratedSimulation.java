package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.methods.doublelayer.abstractions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.ISteadyStateSimulationMethod;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SimulationProperties;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.ManagerExceptionUtils;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.MandatoryPropertyException;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.PropertyCastException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatorySimulationProperties;
import pt.uminho.ceb.biosystems.mew.solvers.SolverType;

public abstract class AbstractTwoStepIntegratedSimulation implements ISteadyStateSimulationMethod, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The integratedmodel. */
	protected ISteadyStateModel integratedmodel;
	
	/** The possible properties. */
	protected Set<String> possibleProperties;
	
	/** The mandatory props. */
	protected Set<String> mandatoryProps;
	
	/** The propreties. */
	protected Map<String, Object> propreties;
	
	/** The metabolic simulationmethod. */
	protected String metabolicSimulationMethod;
	
	protected String regulatoryNetworkSimulationMethod;
	
	protected Map<String, Double> obj_funct=null;
	
 /**
  * Instantiates a new abstract two step integrated simulation.
  *
  * @param ISteadyStateModel  model
  */
 public AbstractTwoStepIntegratedSimulation(ISteadyStateModel model){
		this.integratedmodel = model;
		initPropsKeys();
	
	}
	
    /**
     * Inits the props keys.
     */
    protected void initPropsKeys(){
    
		mandatoryProps = new HashSet<String>();
		propreties = new HashMap<String, Object>();
		possibleProperties = new HashSet<String>();
		possibleProperties.add(RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS);
		possibleProperties.add(RegulatorySimulationProperties.SOLVER);
		
		mandatoryProps.add(RegulatorySimulationProperties.OBJECTIVE_FUNCTION);
		mandatoryProps.add(RegulatorySimulationProperties.VARIABLES_CONTAINER);
		mandatoryProps.add(RegulatorySimulationProperties.METABOLIC_SIMULATION_METHOD);
		mandatoryProps.add(RegulatorySimulationProperties.REGULATORY_NETWORK_SIMULATION_METHOD);
	
	}
	
 
  
 
    // fazer no método
	/* (non-Javadoc)
     * @see metabolic.simulation.components.ISteadyStateSimulationMethod#simulate()
     */
    @Override
	public SteadyStateSimulationResult simulate() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    
    
    
	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#getModel()
	 */
	@Override
	public ISteadyStateModel getModel() {
		return this.integratedmodel;
	}
	
	
	
	
	/**
	 * Gets the checks if is maximization.
	 *
	 * @return the checks if is maximization
	 * @throws PropertyCastException the property cast exception
	 * @throws MandatoryPropertyException the mandatory property exception
	 */
	public Boolean getIsMaximization()throws PropertyCastException, MandatoryPropertyException {
		return (Boolean)ManagerExceptionUtils.testCast(propreties, Boolean.class, RegulatorySimulationProperties.IS_MAXIMIZATION, true);
	}
	
	
	/**
	 * Gets the metabolic simulation method.
	 *
	 * @return the metabolic simulation method
	 * @throws PropertyCastException the property cast exception
	 * @throws MandatoryPropertyException the mandatory property exception
	 */
	public String getMetabolicSimulationMethod()throws PropertyCastException, MandatoryPropertyException {
		return (String)ManagerExceptionUtils.testCast(propreties, String.class, RegulatorySimulationProperties.METABOLIC_SIMULATION_METHOD, false);
	}
	
	
	public String getRegulatoryNetworkSimulationMethod()throws PropertyCastException, MandatoryPropertyException {
		return (String)ManagerExceptionUtils.testCast(propreties, String.class, RegulatorySimulationProperties.REGULATORY_NETWORK_SIMULATION_METHOD, false);
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Double> getObjectiveFunction() throws PropertyCastException, MandatoryPropertyException{
		
		Map<String, Double> objfunct = null;
		try {
			objfunct = ManagerExceptionUtils.testCast(propreties, Map.class, SimulationProperties.OBJECTIVE_FUNCTION, false);
		} catch (Exception e) {
			objfunct = new HashMap<String, Double>();
			
			objfunct.put(this.integratedmodel.getBiomassFlux(), 1.0);
		
		}
		return objfunct;
	}
	

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#getEnvironmentalConditions()
	 */
	@Override
	public EnvironmentalConditions getEnvironmentalConditions()throws PropertyCastException, MandatoryPropertyException {
		// TODO Auto-generated method stub
		return (EnvironmentalConditions)ManagerExceptionUtils.testCast(propreties, EnvironmentalConditions.class, RegulatorySimulationProperties.ENVIRONMENTAL_CONDITIONS, true);
	}

	
	
	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#setEnvironmentalConditions(metabolic.model.components.EnvironmentalConditions)
	 */
	@Override
	public void setEnvironmentalConditions(EnvironmentalConditions environmentalConditions) {
		setProperty(RegulatorySimulationProperties.ENVIRONMENTAL_CONDITIONS, environmentalConditions);
		
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#getGeneticConditions()
	 */
	@Override
	public GeneticConditions getGeneticConditions()throws PropertyCastException, MandatoryPropertyException {
	    
		return (RegulatoryGeneticConditions) ManagerExceptionUtils.testCast(propreties, RegulatoryGeneticConditions.class, RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS, true);
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#setGeneticConditions(metabolic.simulation.components.GeneticConditions)
	 */
	@Override
	public void setGeneticConditions(GeneticConditions geneticConditions) {
		setProperty(RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS, geneticConditions);	
	}


	
	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#getPossibleProperties()
	 */
	@Override
	public Set<String> getPossibleProperties() {
		return this.possibleProperties;
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#getMandatoryProperties()
	 */
	@Override
	public Set<String> getMandatoryProperties() {
		return this.mandatoryProps;
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#setProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setProperty(String m, Object o) {
		propreties.put(m, o);
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#putAllProperties(java.util.Map)
	 */
	public void putAllProperties(Map<String, Object> properties) {
		this.propreties.putAll(properties);
	}
	
    public SolverType getSolverType()throws PropertyCastException, MandatoryPropertyException {
	    
		return (SolverType) ManagerExceptionUtils.testCast(propreties, SolverType.class, RegulatorySimulationProperties.SOLVER, true);
	}
	

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String k) {
		return this.propreties.get(k);
	}

	/* (non-Javadoc)
	 * @see metabolic.simulation.components.ISteadyStateSimulationMethod#getFormulationClass()
	 */
	@Override
	public Class<?> getFormulationClass() {
		return null;
	}
	
	


}
