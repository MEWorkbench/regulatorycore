package pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.interfaces.IContainerBuilder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.RegulatoryContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryGeneCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryRuleCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;

public interface IContainerIntegratedMRBuilder extends IContainerBuilder {
	
	
	//NetWork Information
		Map <String, RegulatoryGeneCI > getRegulatoryGenes();
		Map <String, VariableCI > getRegulatoryVariables();
		Map <String, RegulatoryRuleCI> getRegulatoryGeneRules();
		Map <String,String> getMappingOfGeneIDtoASTgeneID();
        RegulatoryContainer getRegulatoryContainer();
        

}
