package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.resultcontainers;

import java.util.ArrayList;
import java.util.HashSet;

import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;

public class BRNSimulationResults extends AbstractRegulatoryNetworkSimulationResults{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BRNSimulationResults(IRegulatoryModel model,
			ArrayList<ArrayList<Boolean>> simulres,
			HashSet<String> initdeactgenes,
			ArrayList<String> geneknock,
			ArrayList<String> trueVars,
			ArrayList<String> falsetfvariables,
			ArrayList<String> genesalwaystrueattractor,
			ArrayList<String> genesalwaysfalseattractor,
			ArrayList<String> genesundefinedattractor) {
		super(model, simulres, initdeactgenes, geneknock, trueVars,falsetfvariables, genesalwaystrueattractor,genesalwaysfalseattractor,genesundefinedattractor);
	}


	

	
	

}
