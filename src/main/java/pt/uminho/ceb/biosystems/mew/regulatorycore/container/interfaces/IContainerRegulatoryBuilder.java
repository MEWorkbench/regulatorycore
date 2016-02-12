package pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryGeneCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryRuleCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public interface IContainerRegulatoryBuilder{
	
	// model Information
	String getModelName();
	String getOrganismName();
	String getNotes();
	Integer getVersion();
	
	//NetWork Information
	IndexedHashMap<String, RegulatoryGeneCI > getRegulatoryGenes();
	IndexedHashMap<String, VariableCI > getRegulatoryVariables();
	IndexedHashMap<String, RegulatoryRuleCI> getRegulatoryGeneRules();
	
	
}
