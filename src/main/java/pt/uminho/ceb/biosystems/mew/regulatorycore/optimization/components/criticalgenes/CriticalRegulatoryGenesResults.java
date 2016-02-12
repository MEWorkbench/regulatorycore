package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.criticalgenes;

import java.io.Serializable;
import java.util.ArrayList;

import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;

   



public class CriticalRegulatoryGenesResults implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<String> criticalGenesId = null;
	protected IIntegratedStedystateModel model;
	protected String usedIntegratedSimulationMethod;
	protected String usedMetabolicMethod = "";
	protected String usedRegulatorymethod = "";
	protected boolean geneIDsFromFile = false;
	
	
	
	public CriticalRegulatoryGenesResults(ArrayList<String> listgenes, IIntegratedStedystateModel model) {
		// TODO Auto-generated constructor stub
		
		this.criticalGenesId = listgenes;
		this.model = model;
		
		
	}
	
	public CriticalRegulatoryGenesResults(ArrayList<String> listgenes) {
		// TODO Auto-generated constructor stub
		
		this.criticalGenesId = listgenes;
	}
	
	
	
	
	
	public ArrayList<String> getCriticalRegulatoryGeneList(){
		return criticalGenesId;
	}
	
	public IIntegratedStedystateModel getModel(){
		return model;
	}
	
	
	
	public void setModel(IIntegratedStedystateModel model) {
		this.model = model;
	}

	public void setCriticalGenesId(ArrayList<String> criticalGenesId) {
		this.criticalGenesId = criticalGenesId;
	}


	public String getUsedIntegratedSimulationMethod() {
		return usedIntegratedSimulationMethod;
	}


	public void setUsedIntegratedSimulationMethod(
			String usedIntegratedSimulationMethod) {
		this.usedIntegratedSimulationMethod = usedIntegratedSimulationMethod;
	}


	public String getUsedMetabolicMethod() {
		return usedMetabolicMethod;
	}


	public void setUsedMetabolicMethod(String usedMetabolicMethod) {
		this.usedMetabolicMethod = usedMetabolicMethod;
	}


	public String getUsedRegulatorymethod() {
		return usedRegulatorymethod;
	}


	public void setUsedRegulatorymethod(String usedRegulatorymethod) {
		this.usedRegulatorymethod = usedRegulatorymethod;
	}

	public boolean isGeneIDsFromFile() {
		return geneIDsFromFile;
	}

	public void setGeneIDsFromFile(boolean geneIDsFromFile) {
		this.geneIDsFromFile = geneIDsFromFile;
	}
	
	
	

}
