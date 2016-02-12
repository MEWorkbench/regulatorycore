package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.results;

import java.util.HashMap;
import java.util.List;

import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.configuration.IGenericConfiguration;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.optimizationresult.AbstractStrainOptimizationResultSet;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.optimizationresult.IStrainOptimizationReader;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;

public class RegulatorySolutionSet<T extends IGenericConfiguration> extends AbstractStrainOptimizationResultSet<T, RegulatorySolution> {

	public RegulatorySolutionSet(T baseConfiguration) {
		super(baseConfiguration);
	}

	public RegulatorySolutionSet(T baseConfiguration, List<RegulatorySolution> resultList) {
        super(baseConfiguration, resultList);
    }
	
	@Override
	public RegulatorySolution createSolution(GeneticConditions gc) {
		return new RegulatorySolution(gc);
	}

	@Override
	public RegulatorySolution createSolution(GeneticConditions gc, List<Double> attributes) {
		return new RegulatorySolution(gc, new HashMap<String,SteadyStateSimulationResult>(), attributes);
	}

	@Override
	public IStrainOptimizationReader getSolutionReaderInstance() throws Exception {
		// TODO Reader to be implemented in the future
		return null;
	}

}
