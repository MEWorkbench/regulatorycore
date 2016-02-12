package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model;

import java.util.ArrayList;

import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.gpr.ISteadyStateGeneReactionModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;

public interface IIntegratedStedystateModel extends ISteadyStateGeneReactionModel{
	
	
	Boolean isRegulatoryGene(String geneId);
	Boolean isMetabolicGene(String geneId);
	Boolean isMetabolicAndRegulatoryGene(String geneId);
	IRegulatoryModel getRegulatoryNet();
	void setRegulatoryNet(IRegulatoryModel regmodel);
	VariablesContainer getVariablescontainer();
	ArrayList<String> getGenesInRegulatoryModel();
	ArrayList<String> filterOnlyMetabolicGenes(ArrayList<String> geneslist);
	ArrayList<String> filterOnlyRegulatoryGenes(ArrayList<String> geneslist);
	
	
	
	
	

}
