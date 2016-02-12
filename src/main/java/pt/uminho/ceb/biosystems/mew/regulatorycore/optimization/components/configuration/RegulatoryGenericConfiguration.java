package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.configuration;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.terminationcriteria.InvalidTerminationCriteriaParameter;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.terminationcriteria.NumFunctionEvaluationsListenerHybridTerminationCriteria;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.components.ArchiveManager;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.configuration.GenericConfiguration;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.configuration.GenericOptimizationProperties;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.IObjectiveFunction;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.strainoptimizationalgorithms.jecoli.JecoliOptimizationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class RegulatoryGenericConfiguration extends GenericConfiguration {
	
	public static final String INTEGRATED_STEADY_STATE_MODEL = "regulatory.integratedsteadystatemodel";
	public static final String REGULATORY_SIMULATION_METHOD = "regulatory.simulationmethod";
	
	
	public RegulatoryGenericConfiguration() {
		super();
		loadMandatoryOptionalProperties();
	}
	
	public RegulatoryGenericConfiguration(Map<String,Object> propertyMapToCopy) {
		super(propertyMapToCopy);
		loadMandatoryOptionalProperties();
	}

	private void loadMandatoryOptionalProperties() {
		mandatoryPropertyMap.put(GenericOptimizationProperties.OPTIMIZATION_ALGORITHM, String.class);
		
		mandatoryPropertyMap.put(JecoliOptimizationProperties.IS_VARIABLE_SIZE_GENOME, Boolean.class);
		mandatoryPropertyMap.put(GenericOptimizationProperties.STEADY_STATE_MODEL, ISteadyStateModel.class);
		mandatoryPropertyMap.put(INTEGRATED_STEADY_STATE_MODEL, IIntegratedStedystateModel.class);
		
		mandatoryPropertyMap.put(GenericOptimizationProperties.SIMULATION_CONFIGURATION, Map.class);
		mandatoryPropertyMap.put(REGULATORY_SIMULATION_METHOD, String.class);
		mandatoryPropertyMap.put(GenericOptimizationProperties.MAP_OF2_SIM, IndexedHashMap.class);
		mandatoryPropertyMap.put(JecoliOptimizationProperties.TERMINATION_CRITERIA, ITerminationCriteria.class);
		
		optionalPropertyMap.put(GenericOptimizationProperties.NOT_ALLOWED_IDS, List.class);
		optionalPropertyMap.put(GenericOptimizationProperties.MAX_SET_SIZE, Integer.class);
	}
	
	public String getOptimizationAlgorithm(){
		return (String)getProperty(GenericOptimizationProperties.OPTIMIZATION_ALGORITHM);
	}
	
	public void setOptimizationAlgorithm(String optimizationAlgorithm){
		getPropertyMap().put(GenericOptimizationProperties.OPTIMIZATION_ALGORITHM, optimizationAlgorithm);
	}
	
	public int getNumberOfObjectives() {
		return getObjectiveFunctionsMap().size();
	}
	
	public int getMaxSetSize() {
		return getDefaultValue(GenericOptimizationProperties.MAX_SET_SIZE, 1);
	}
	
	public void setIsVariableSizeGenome(boolean isVariableSizeGenome) {
		propertyMap.put(JecoliOptimizationProperties.IS_VARIABLE_SIZE_GENOME, isVariableSizeGenome);
	}
	
	public void setRegulatorySimulationMethod(String regulatorySimulationMethod){
		propertyMap.put(REGULATORY_SIMULATION_METHOD, regulatorySimulationMethod);
	}
	
	public String getRegulatorySimulationMethod(){
		return (String) propertyMap.get(REGULATORY_SIMULATION_METHOD);
	}
	
	public boolean getIsVariableSizeGenome() {
		return getDefaultValue(JecoliOptimizationProperties.IS_VARIABLE_SIZE_GENOME, true);
	}
	
	public void setNotAllowedIds(List<String> notAllowedIds) {
		propertyMap.put(GenericOptimizationProperties.NOT_ALLOWED_IDS, notAllowedIds);
	}
	
	public List<String> getNonAllowedIds() {
		return (List<String>) propertyMap.get(GenericOptimizationProperties.NOT_ALLOWED_IDS);
	}
	
	public void setMaxSetSize(int maxSetSize) {
		propertyMap.put(GenericOptimizationProperties.MAX_SET_SIZE, maxSetSize);
	}
	
	public ITerminationCriteria getTerminationCriteria() throws InvalidTerminationCriteriaParameter {
		return getDefaultValue(JecoliOptimizationProperties.TERMINATION_CRITERIA, new NumFunctionEvaluationsListenerHybridTerminationCriteria(50000));
	}
	
	public void setTerminationCriteria(ITerminationCriteria terminationCriteria) throws InvalidTerminationCriteriaParameter {
		getPropertyMap().put(JecoliOptimizationProperties.TERMINATION_CRITERIA, terminationCriteria);
	}
	
	public IndexedHashMap<IObjectiveFunction, String> getObjectiveFunctionsMap() {
		return (IndexedHashMap<IObjectiveFunction, String>) propertyMap.get(GenericOptimizationProperties.MAP_OF2_SIM);
	}
	
	public void setObjectiveFunctionsMap(IndexedHashMap<IObjectiveFunction, String> objectiveFunctionMap) {
		propertyMap.put(GenericOptimizationProperties.MAP_OF2_SIM, objectiveFunctionMap);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Map<String,Object>> getSimulationConfiguration(){
		return (Map<String, Map<String, Object>>) propertyMap.get(GenericOptimizationProperties.SIMULATION_CONFIGURATION);
	}

	public void setSimulationConfiguration(Map<String, Map<String, Object>> simulationConfiguration) {
		propertyMap.put(GenericOptimizationProperties.SIMULATION_CONFIGURATION, simulationConfiguration);
	}
	
	public ISteadyStateModel getSteadyStateModel() {
		return (ISteadyStateModel) propertyMap.get(GenericOptimizationProperties.STEADY_STATE_MODEL);
	}
	
	public void setModel(ISteadyStateModel model) {
		propertyMap.put(GenericOptimizationProperties.STEADY_STATE_MODEL, model);
		if(IIntegratedStedystateModel.class.isAssignableFrom(model.getClass()))
			propertyMap.put(INTEGRATED_STEADY_STATE_MODEL, model);
	}
	
	public IIntegratedStedystateModel getIntegratedSteadyStateModel() {
		return (IIntegratedStedystateModel) propertyMap.get(INTEGRATED_STEADY_STATE_MODEL);
	}
	
	public ArchiveManager getArchiveManager(){
		return (ArchiveManager) propertyMap.get(JecoliOptimizationProperties.ARCHIVE_MANAGER);
	}
	
	public void setArchiveManager(ArchiveManager archiveManager){
		propertyMap.put(JecoliOptimizationProperties.ARCHIVE_MANAGER, archiveManager);
	}
	
}
