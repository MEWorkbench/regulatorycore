package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryRule;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryVariable;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;


public interface IRegulatoryModel {
	
	
	IRegulatoryModel clone()  ;
	
	void setUseForIntegratedSimulation(boolean isintegrated);
	
	// Regulatory rules
	Integer getNumberOfRegulatoryRules();
	RegulatoryRule getRegulatoryRule(int ruleIndex);
	Integer getComponentRuleIndex(String ruleId);
	String getRegulatoryRuleGeneLinkId(Integer ruleIndex);
	String getRuleLinkId(Integer ruleIndex);
	String getRuleLinkIDByGeneID(String geneid);
	Integer getRegulatoryGeneIndex(String geneId);
	IndexedHashMap<String, RegulatoryRule> getGeneRegRules();
	IndexedHashMap<String, String> getMapOfRegGenesToASTgeneID();
	void setRegulatoryRules(IndexedHashMap<String, RegulatoryRule> newrules);
	
	//Regulatory Variables
	IndexedHashMap<String, RegulatoryVariable> getVariablesRegNetwork();
	Integer getNumberOfVariables();
	Integer getVariableIndex(String variableName) throws Exception;
	RegulatoryVariable getVariableByIndex(int variableIDX);
	ArrayList<String> getVariablesInNetwork();
	String getVarType(String var);
	IndexedHashMap<String, String> getRegulatoryVariablesType();
	
	
	
	// Regulatory Genes
	Integer getNumberOfGenes();
	Set<String> getGenesId();
	Gene getGene(int geneIndex);
	Gene getGene(String geneId);
	Integer getGeneIndex(String geneId);
	IndexedHashMap<String, Gene> getRegulatoryGenes();
	String getGeneIDassociatedtoTFinAST(String tfname);
	ArrayList<String> getRegulatoryGeneIDs();
	ArrayList<String> getTFsNames();
	HashMap<String, String> getmapASTIDtoGeneName();
	

}
