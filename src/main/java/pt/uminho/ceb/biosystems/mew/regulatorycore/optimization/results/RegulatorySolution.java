package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.results;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneChangesList;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.optimizationresult.AbstractSolution;
import pt.uminho.ceb.biosystems.mew.utilities.java.StringUtils;

public class RegulatorySolution extends AbstractSolution {

	public RegulatorySolution(GeneticConditions solutionGeneticConditions) {
		super(solutionGeneticConditions, new HashMap<String, SteadyStateSimulationResult>());
	}
	
	public RegulatorySolution(GeneticConditions solutionGeneticConditions, Map<String, SteadyStateSimulationResult> simulationResultMap, List<Double> fitnesses) {
		super(solutionGeneticConditions, simulationResultMap, fitnesses);
	}

	@Override
	public void write(OutputStreamWriter outputStream) throws Exception {
		GeneChangesList geneChangesList = solutionGeneticConditions.getGeneList();
		List<String> geneKnockoutList = geneChangesList.getGeneKnockoutList();
		
		if (attributes != null) {
			String fitString = StringUtils.concat(INNER_DELIMITER, attributes);
			outputStream.write(fitString);
		}
		
		outputStream.write(INNER_DELIMITER);
		
		for (String geneKnockout : geneKnockoutList) {
			outputStream.write(INNER_DELIMITER + geneKnockout);
		}
		
	}

	@Override
	public String toStringHumanReadableGC(String delimiter) {
		GeneChangesList geneChangesList = solutionGeneticConditions.getGeneList();
		if(geneChangesList!=null && geneChangesList.size()>0){
			List<String> geneKnockoutList = geneChangesList.getGeneKnockoutList();
			
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<geneKnockoutList.size(); i++){
				sb.append(geneKnockoutList.get(i));
				if(i<geneKnockoutList.size()){
					sb.append(delimiter);
				}
			}
			return sb.toString();			
		}
		return "";
	}

}
