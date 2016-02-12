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
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.parser.ParseException;

public class OverrideRegulatoryModel implements IRegulatoryOverrideModel, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<String> knockoutList;
	protected ArrayList<Integer> knockoutListIndex = null;
	protected IRegulatoryModel originalModel;
	
	public OverrideRegulatoryModel(IRegulatoryModel originalModel, ArrayList<String> knockoutList){
		this.originalModel = originalModel;
		this.knockoutList = knockoutList;
		if(knockoutList != null){
		knockoutListIndex = generateIndexedgeneOFF(knockoutList);
		}
	}
	
	
	
	public OverrideRegulatoryModel(ArrayList<String> knockoutListString, IRegulatoryModel originalModel){
		this.originalModel = originalModel;
		this.knockoutListIndex= null;
		
		if(knockoutListString!= null){
			knockoutListIndex = new ArrayList<Integer>();
			for(int i =0; i < knockoutListString.size(); i++){
				knockoutListIndex.add(originalModel.getComponentRuleIndex(knockoutListString.get(i)));
			}
		}
	}
	
	
	
	
		private ArrayList<Integer> generateIndexedgeneOFF(ArrayList<String> knockoutgenelist) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		  for(String id : knockoutgenelist){
			ret.add(originalModel.getRegulatoryGeneIndex(id));
		   }
		return ret;
	}
	
	
	
	
	
	@Override
	public Gene getGene(int geneIndex) {
		return null;
	}

	@Override
	public Gene getGene(String geneId) {
		return null;
	}

	@Override
	public Set<String> getGenesId() {
		return null;
	}

	@Override
	public Integer getNumberOfGenes() {
		return null;
	}

	@Override
	public Integer getNumberOfRegulatoryRules() {
		return null;
	}

	@Override
	public Integer getNumberOfVariables() {
		return null;
	}
	
	

	@Override
	public RegulatoryRule getRegulatoryRule(int ruleIndex){
		RegulatoryRule ret = null;
		
		if(knockoutListIndex!=null && knockoutListIndex.contains(ruleIndex)){
			try {
				ret = new RegulatoryRule(originalModel.getRegulatoryRule(ruleIndex).getRuleId(),"");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}



	@Override
	public Integer getVariableIndex(String variableName) {
		return null;
	}
	

	@Override
	public Integer getComponentRuleIndex(String ruleId) {
		return null;
	}

	@Override
	public String getRegulatoryRuleGeneLinkId(Integer ruleIndex) {
		return null;
	}

	@Override
	public Integer getRegulatoryGeneIndex(String geneId) {
		return null;
	}

	@Override
	public IndexedHashMap<String, RegulatoryVariable> getVariablesRegNetwork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegulatoryVariable getVariableByIndex(int variableIDX) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getVariablesInNetwork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexedHashMap<String, RegulatoryRule> getGeneRegRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexedHashMap<String, Gene> getRegulatoryGenes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexedHashMap<String, String> getMapOfRegGenesToASTgeneID() {
		// TODO Auto-generated method stub
		return originalModel.getMapOfRegGenesToASTgeneID();
	}



	@Override
	public Integer getGeneIndex(String geneId) {
		// TODO Auto-generated method stub
		return originalModel.getGeneIndex(geneId);
	}



	@Override
	public String getVarType(String var) {
		// TODO Auto-generated method stub
		return originalModel.getVarType(var);
	}



	@Override
	public String getGeneIDassociatedtoTFinAST(String tfname) {
		// TODO Auto-generated method stub
		return originalModel.getGeneIDassociatedtoTFinAST(tfname);
	}



	@Override
	public ArrayList<String> getRegulatoryGeneIDs() {
		// TODO Auto-generated method stub
		return originalModel.getRegulatoryGeneIDs();
	}



	@Override
	public ArrayList<String> getTFsNames() {
		// TODO Auto-generated method stub
		return originalModel.getTFsNames();
	}

    @Override
	public IRegulatoryModel clone(){
		return originalModel.clone();
	}



	@Override
	public HashMap<String, String> getmapASTIDtoGeneName() {
		// TODO Auto-generated method stub
		return originalModel.getmapASTIDtoGeneName();
	}



	@Override
	public void setRegulatoryRules(
			IndexedHashMap<String, RegulatoryRule> newrules) {
		this.originalModel.setRegulatoryRules(newrules);
		
	}



	@Override
	public IndexedHashMap<String, String> getRegulatoryVariablesType() {
		// TODO Auto-generated method stub
		return originalModel.getRegulatoryVariablesType();
	}



	@Override
	public void setUseForIntegratedSimulation(boolean isintegrated) {
		this.originalModel.setUseForIntegratedSimulation(isintegrated);
		
	}



	@Override
	public String getRuleLinkId(Integer ruleIndex) {
		// TODO Auto-generated method stub
		return originalModel.getRuleLinkId(ruleIndex);
	}



	@Override
	public String getRuleLinkIDByGeneID(String geneid) {
		// TODO Auto-generated method stub
		return originalModel.getRuleLinkIDByGeneID(geneid);
	}

	

}
