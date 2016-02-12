package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.mapper.regnetworkmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryRule;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryVariable;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;


public class RegulatoryModelMapper implements IRegulatoryModelMapper,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected IRegulatoryModel model;
	protected IRegulatoryOverrideModel overrideModel;
	protected IRegulatoryDecoder decoder;
	
	public RegulatoryModelMapper(IRegulatoryModel model, IRegulatoryOverrideModel overrideModel, IRegulatoryDecoder decoder){
		this.model = model;
		this.overrideModel = overrideModel;
		this.decoder = decoder;
	}
	
	public RegulatoryModelMapper(IRegulatoryModel model) {
		this.model = model;

	}
	

	public IRegulatoryDecoder getDecoder() {
		return decoder;
	}

	@Override
	public IRegulatoryModel getModel() {
		return model;
	}

	@Override
	public IRegulatoryOverrideModel getOverrideModel() {
		return overrideModel;
	}

	@Override
	public void setDecoder(IRegulatoryDecoder decoder) {
		this.decoder = decoder;
		
	}

	@Override
	public void setModel(IRegulatoryModel model) {
		this.model = model;
		
	}

	@Override
	public void setOverrideModel(IRegulatoryOverrideModel overrideModel) {
		this.overrideModel = overrideModel;
		
	}

	@Override
	public Gene getGene(int geneIndex) {
		return model.getGene(geneIndex);
	}

	@Override
	public Gene getGene(String geneId) {
		return model.getGene(geneId);
	}

	@Override
	public Set<String> getGenesId() {
		return model.getGenesId();
	}

	@Override
	public Integer getNumberOfGenes() {
		return model.getNumberOfGenes();
	}

	@Override
	public Integer getNumberOfRegulatoryRules() {
		return model.getNumberOfRegulatoryRules();
	}

	@Override
	public Integer getNumberOfVariables() {
		return model.getNumberOfVariables();
	}

	@Override
	public RegulatoryRule getRegulatoryRule(int ruleIndex) {
		
		RegulatoryRule result = null;
		
		if(overrideModel != null)
			result = overrideModel.getRegulatoryRule(ruleIndex);
		
		if ((result == null) && (decoder != null))
			result = decoder.getRegulatoryRule(ruleIndex);
		
		if(result == null)
			result = model.getRegulatoryRule(ruleIndex);
		
		return result;
	}



	@Override
	public Integer getVariableIndex(String variableName) throws Exception {
		return model.getVariableIndex(variableName);
	}

	
	@Override
	public IndexedHashMap<String, RegulatoryVariable> getVariablesRegNetwork() {
		// TODO Auto-generated method stub
		return model.getVariablesRegNetwork();
	}
	
	@Override
	public Integer getComponentRuleIndex(String ruleId) {
		return model.getComponentRuleIndex(ruleId);
	}

	@Override
	public String getRegulatoryRuleGeneLinkId(Integer ruleIndex) {
		return model.getRegulatoryRuleGeneLinkId(ruleIndex);
	}

	@Override
	public Integer getRegulatoryGeneIndex(String geneId) {
		return model.getRegulatoryGeneIndex(geneId);
	}

	@Override
	public RegulatoryVariable getVariableByIndex(int variableIDX) {
		// TODO Auto-generated method stub
		return model.getVariablesRegNetwork().getValueAt(variableIDX);
	}

	@Override
	public ArrayList<String> getVariablesInNetwork() {
		// TODO Auto-generated method stub
		ArrayList<String> vars = new ArrayList<String>();
		for (RegulatoryVariable var : model.getVariablesRegNetwork().values()) {
			vars.add(var.getId());
		}
		
		return vars;
	}

	@Override
	public IndexedHashMap<String, RegulatoryRule> getGeneRegRules() {
		// TODO Auto-generated method stub
		return model.getGeneRegRules();
	}

	@Override
	public IndexedHashMap<String, Gene> getRegulatoryGenes() {
		// TODO Auto-generated method stub
		return model.getRegulatoryGenes();
	}

	@Override
	public IndexedHashMap<String, String> getMapOfRegGenesToASTgeneID() {
		// TODO Auto-generated method stub
		return model.getMapOfRegGenesToASTgeneID();
	}

	@Override
	public Integer getGeneIndex(String geneId) {
		// TODO Auto-generated method stub
		return model.getGeneIndex(geneId);
	}

	@Override
	public String getVarType(String var) {
		return model.getVarType(var);
	}

	@Override
	public String getGeneIDassociatedtoTFinAST(String tfname) {
		// TODO Auto-generated method stub
		return model.getGeneIDassociatedtoTFinAST(tfname);
	}

	@Override
	public ArrayList<String> getRegulatoryGeneIDs() {
		// TODO Auto-generated method stub
		return model.getRegulatoryGeneIDs();
	}

	@Override
	public ArrayList<String> getTFsNames() {
		// TODO Auto-generated method stub
		return model.getTFsNames();
	}

    
	@Override
	public IRegulatoryModel clone(){
		return model.clone();
	}

	@Override
	public HashMap<String, String> getmapASTIDtoGeneName() {
		// TODO Auto-generated method stub
		return model.getmapASTIDtoGeneName();
	}

	@Override
	public void setRegulatoryRules(
			IndexedHashMap<String, RegulatoryRule> newrules) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IndexedHashMap<String, String> getRegulatoryVariablesType() {
		// TODO Auto-generated method stub
		return model.getRegulatoryVariablesType();
	}

	@Override
	public void setUseForIntegratedSimulation(boolean isintegrated) {
		model.setUseForIntegratedSimulation(isintegrated);
		
	}

	@Override
	public String getRuleLinkId(Integer ruleIndex) {
		// TODO Auto-generated method stub
		return model.getRuleLinkId(ruleIndex);
	}

	@Override
	public String getRuleLinkIDByGeneID(String geneid) {
		// TODO Auto-generated method stub
		return model.getRuleLinkIDByGeneID(geneid);
	}
	
		
	

	

}
