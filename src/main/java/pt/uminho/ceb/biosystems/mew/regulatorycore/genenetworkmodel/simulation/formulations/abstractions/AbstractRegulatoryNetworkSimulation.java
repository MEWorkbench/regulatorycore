package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.formulations.abstractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.ManagerExceptionUtils;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.MandatoryPropertyException;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.PropertyCastException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.mapper.regnetworkmodel.OverrideRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.mapper.regnetworkmodel.RegulatoryModelMapper;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.IRegulatoryNetworkSimulationMethod;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.RegulatoryNetworkSimulationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.IRegulatoryNetworkSimulationResult;

public abstract class AbstractRegulatoryNetworkSimulation implements IRegulatoryNetworkSimulationMethod{
	
    protected IRegulatoryModel model;
	protected Map<String, Object> properties;
	protected OverrideRegulatoryModel overrideModel;
	protected RegulatoryModelMapper modelMapper;
	
	
	protected ArrayList<String> genesKnockout;
	protected HashSet<String> InifalseNetNodes;
	protected ArrayList<String> trueVariables;
	
	protected Set<String> possibleProperties;
	protected Set<String> mandatoryProps;
	
	
	public AbstractRegulatoryNetworkSimulation(IRegulatoryModel model){
		
		this.model = model;
		properties = new HashMap<String, Object>();
		initPropsKeys();
	
		
	}
	
	
	private void initPropsKeys(){
		
		mandatoryProps = new HashSet<String>();

		
		possibleProperties = new HashSet<String>();
		possibleProperties.add(RegulatoryNetworkSimulationProperties.GENEKNOCKOUTS);
		possibleProperties.add(RegulatoryNetworkSimulationProperties.TRUEVARIABLES);
		possibleProperties.add(RegulatoryNetworkSimulationProperties.INITDEACTIVATEDGENES);
		possibleProperties.add(RegulatoryNetworkSimulationProperties.TFFALSEVALUES);
	}
	
	

	protected void createModelOverride() throws PropertyCastException, MandatoryPropertyException{
		
		overrideModel = new OverrideRegulatoryModel(model, getGeneKnockouts());
		modelMapper = new RegulatoryModelMapper(model);
		modelMapper.setOverrideModel(overrideModel);
		this.model = modelMapper;
		
	}
	
	
	public IRegulatoryModel getModel(){
		return model;
	}
	
	
	public void setProperty(String m, Object o){
		properties.put(m, o);
	}
	
	@SuppressWarnings("unchecked")
	public Object getProperty(String k) {
		return properties.get(k);
	}
	
	public Set<String> getPossibleProperties(){
		return possibleProperties;		
	}
	
	public Set<String> getMandatoryProperties(){
		return mandatoryProps;
	}
	
	public void putAllProperties(Map<String, Object> p){
		this.properties.putAll(p);		
	}
	
	public void setInitialDeactivatedGenes(HashSet<String> initdeactivatedgenes){
		
		setProperty(RegulatoryNetworkSimulationProperties.INITDEACTIVATEDGENES, initdeactivatedgenes);
	}
	
	public HashSet<String> getInitDeactivatedGenes() throws PropertyCastException, MandatoryPropertyException{
		return (HashSet<String>) ManagerExceptionUtils.testCast(properties, HashSet.class, RegulatoryNetworkSimulationProperties.INITDEACTIVATEDGENES, true);
	}

	public void setGeneKnockouts(ArrayList<String> genesknockout){
		
		setProperty(RegulatoryNetworkSimulationProperties.GENEKNOCKOUTS, genesknockout);
	}
	
	public ArrayList<String> getGeneKnockouts() throws PropertyCastException, MandatoryPropertyException {
		
		return (ArrayList<String>) ManagerExceptionUtils.testCast(properties, ArrayList.class,RegulatoryNetworkSimulationProperties.GENEKNOCKOUTS, true);
	}
	
	
	public void setTrueVariables(ArrayList<String> truevars){
		
		setProperty(RegulatoryNetworkSimulationProperties.TRUEVARIABLES, truevars);
	}
	
	
	public ArrayList<String> getTrueVariables() throws PropertyCastException, MandatoryPropertyException {
		return (ArrayList<String>) ManagerExceptionUtils.testCast(properties,ArrayList.class, RegulatoryNetworkSimulationProperties.TRUEVARIABLES, true);
	}
	
	
   public void setTFsWithFalseValues(ArrayList<String> falsetfs){
		
		setProperty(RegulatoryNetworkSimulationProperties.TFFALSEVALUES, falsetfs);
	}
	
	
	public ArrayList<String> getTFsWithFalseValues() throws PropertyCastException, MandatoryPropertyException {
		return (ArrayList<String>) ManagerExceptionUtils.testCast(properties,ArrayList.class, RegulatoryNetworkSimulationProperties.TFFALSEVALUES, true);
	}
	
	
	public abstract IRegulatoryNetworkSimulationResult simulate();
	

}
