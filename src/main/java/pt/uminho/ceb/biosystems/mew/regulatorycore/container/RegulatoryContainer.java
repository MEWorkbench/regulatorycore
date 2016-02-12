package pt.uminho.ceb.biosystems.mew.regulatorycore.container;

import java.io.Serializable;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryGeneCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryRuleCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces.IContainerRegulatoryBuilder;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class RegulatoryContainer implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean debug = false;
	
	protected String name;
	protected String organism;
	protected String notes;
	protected Integer version;
	
	protected IndexedHashMap<String, RegulatoryGeneCI> regulatorygenes;
	protected IndexedHashMap<String, VariableCI> variables;
	protected IndexedHashMap<String, RegulatoryRuleCI> regulatorygeneRules;
	protected IndexedHashMap<String, String> mapofGeneIDtoASTRuleID;
	
	
	
	
	
	
	public RegulatoryContainer (){
		
		regulatorygenes = new IndexedHashMap<String, RegulatoryGeneCI>();
		variables = new IndexedHashMap<String, VariableCI>();
	}
	
	
	public RegulatoryContainer(IContainerRegulatoryBuilder builder){
		
		this.name = builder.getModelName();
		this.organism = builder.getOrganismName();
		this.notes = builder.getNotes();
		this.version = builder.getVersion();
		
		this.regulatorygenes = builder.getRegulatoryGenes();
		this.variables = builder.getRegulatoryVariables();
		this.regulatorygeneRules = builder.getRegulatoryGeneRules();
		makeMappingGeneIDtoASTusedID();
	}
	
	
	public RegulatoryContainer(RegulatoryContainer container){
		
		this.name 				= container.name;
		this.organism 			= container.organism;
		this.notes 				= container.notes;
		this.version 			= container.version;
		
	    this.regulatorygenes = new IndexedHashMap<String, RegulatoryGeneCI>();
	    for (String id : container.getRegulatoryGenes().keySet()) {
			this.regulatorygenes.put(id, (RegulatoryGeneCI) container.getRegulatoryGene(id).clone());
		}
	    
	    this.variables = new IndexedHashMap<String, VariableCI>();
	    for (String id : container.getAllVariables().keySet()) {
			this.variables.put(id, container.getSingleVariable(id));
		}
	    
	    this.regulatorygeneRules = new IndexedHashMap<String, RegulatoryRuleCI>();
	    for (String id : container.getRegulatorygeneRules().keySet()) {
	    	this.regulatorygeneRules.put(id, container.getRegulatoryRulebyID(id));
		}
		
	    this.mapofGeneIDtoASTRuleID = new IndexedHashMap<String, String>();
	    for (String id : container.getMapofGeneIDtoASTRuleID().keySet()) {
	    	this.mapofGeneIDtoASTRuleID.put(id, container.getASTgeneidMappedtogneID(id));
		}
	    
	    
	    
	}
	
	
	private void makeMappingGeneIDtoASTusedID(){
		
		mapofGeneIDtoASTRuleID = new IndexedHashMap<String, String>();
	         
		for (int i = 0; i < regulatorygeneRules.size(); i++) {
			mapofGeneIDtoASTRuleID.putAt(i, regulatorygeneRules.getKeyAt(i), regulatorygeneRules.getValueAt(i).getRuleId());
		}
		
		
	}
	
	
	public String getModelName() {
		return name;
	}

	public void setModelName(String name) {
		this.name = name;
	}

	public String getOrganismName() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	public IndexedHashMap<String, RegulatoryGeneCI> getRegulatoryGenes(){
		return regulatorygenes;
	}
	
	
	public RegulatoryGeneCI getRegulatoryGene(String id){
		return regulatorygenes.get(id);
	}
	
	public IndexedHashMap<String, VariableCI> getAllVariables(){
		return this.variables;
	}
	
	public VariableCI getSingleVariable(String id){
		return variables.get(id);
	}
	
    public RegulatoryRuleCI getRegulatoryRulebyID(String id){
    	return regulatorygeneRules.get(id);
    }
	
	
	public IndexedHashMap<String, RegulatoryRuleCI> getRegulatorygeneRules() {
		return regulatorygeneRules;
	}
	
	


	public IndexedHashMap<String, String> getMapofGeneIDtoASTRuleID() {
		return mapofGeneIDtoASTRuleID;
	}
	
	public String getASTgeneidMappedtogneID(String id){
		return mapofGeneIDtoASTRuleID.get(id);
	}


	@Override
	public RegulatoryContainer clone() {
		return new RegulatoryContainer(this);
	}
	
	
	

}
