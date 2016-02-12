package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.controlcenters;

import java.util.ArrayList;
import java.util.HashSet;

import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.IRegulatoryNetworkSimulationMethod;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.RegulatoryNetworkSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.RegulatoryNetworkSimulationMethodsFactory;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.RegulatoryNetworkSimulationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.IRegulatoryNetworkSimulationResult;

public abstract class AbstractRegulatoryNetworkSimulationControlCenter {
	
	
	
	protected IRegulatoryModel regmodel;
	
	protected String methodType = RegulatoryNetworkSimulationMethods.BRNV;
	
	protected IRegulatoryNetworkSimulationMethod lastmethod;
	
	protected abstract RegulatoryNetworkSimulationMethodsFactory getFactory();
	
     	
	
	public AbstractRegulatoryNetworkSimulationControlCenter(IRegulatoryModel regmodel, String regmethodType, ArrayList<String> genesKnockout, HashSet<String> InitfalseNetNodes, ArrayList<String> trueVariables, ArrayList<String> TFsFalse , boolean isintegratedsimulation){
		
		this.regmodel = regmodel;
		this.regmodel.setUseForIntegratedSimulation(isintegratedsimulation);
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.GENEKNOCKOUTS, genesKnockout);
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.INITDEACTIVATEDGENES, InitfalseNetNodes);
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.TRUEVARIABLES, trueVariables);
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.TFFALSEVALUES, TFsFalse);
		getFactory().setModel(this.regmodel);
		
		
	}
	
	
	public ArrayList<String> getGeneKnockouts(){
		return (ArrayList<String>)getFactory().getProperty(RegulatoryNetworkSimulationProperties.GENEKNOCKOUTS);
	}
	
	
	public void setGeneKnockouts(ArrayList<String> geneKnockouts){
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.GENEKNOCKOUTS, geneKnockouts);	
	}
	
	public HashSet<String> getInitialDeactivatedGenes(){
		return (HashSet<String>)getFactory().getProperty(RegulatoryNetworkSimulationProperties.INITDEACTIVATEDGENES);
	}
	
	public void setInitialDeactivatedGenes(HashSet<String> genesDeactivated){
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.INITDEACTIVATEDGENES, genesDeactivated);	
	}
	
	
	public ArrayList<String> getTrueVariables(){
		return (ArrayList<String>)getFactory().getProperty(RegulatoryNetworkSimulationProperties.TRUEVARIABLES);
	}

	
	public void setTrueVariables(ArrayList<String> truevars){
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.TRUEVARIABLES, truevars);	
	
	}
	
	public ArrayList<String> getTFsWithFalseValues(){
		return (ArrayList<String>)getFactory().getProperty(RegulatoryNetworkSimulationProperties.TFFALSEVALUES);
	}
	
	public void setTfsWithFalseValues(ArrayList<String> falsetfs){
		getFactory().addProperty(RegulatoryNetworkSimulationProperties.TFFALSEVALUES, falsetfs);
		
	}

	public Object getSimulationProperty (String key)
	{
		return getFactory().getProperty(key);
	}

	public void setSimulationProperty (String key, Object value)
	{
		getFactory().addProperty(key, value);
	}
		
		
	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	

	public IRegulatoryModel getModel() {
		return regmodel;
	}
	
	
	
	public IRegulatoryNetworkSimulationResult simulate() throws Exception {

		this.lastmethod = getFactory().getMethod(this.methodType);
		return lastmethod.simulate(); 
	}
	
	
	public IRegulatoryNetworkSimulationMethod getSimulatedMethod(){
		return lastmethod;
	}
		
   
	public void addProperty(String id, Object obj){
		getFactory().addProperty(id, obj);
	}
	
	
	
}
