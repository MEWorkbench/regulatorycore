package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.simplification;

import java.io.Serializable;
import java.util.List;

import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.IObjectiveFunction;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;

public class RegulatorySolutionSimplificationResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected IntegratedSimulationResult simulationResult;
	protected GeneticConditions simplifiedSolution;
	protected double[] fitnesses;
	protected List<IObjectiveFunction> objectiveFunctions;
	
	public RegulatorySolutionSimplificationResult(IntegratedSimulationResult simInitial, GeneticConditions newGC, 
			List<IObjectiveFunction> objectiveFunctions, double[] fitnesses) {
		this.simulationResult = simInitial;
		this.simplifiedSolution = newGC;
		this.objectiveFunctions = objectiveFunctions;
		this.fitnesses = fitnesses;
	}
	
	public RegulatorySolutionSimplificationResult(IntegratedSimulationResult simResult, double[] fitnesses){
		this(simResult, null, null, fitnesses);		
	}
	

	public IntegratedSimulationResult getSimulationResult() {
		return simulationResult;
	}
	
	public void setSimulationResult(IntegratedSimulationResult simulationResult) {
		this.simulationResult = simulationResult;
	}
	
	public GeneticConditions getSimplifiedSolution() {
		return simplifiedSolution;
	}
	
	public void setSimplifiedSolution(GeneticConditions simplifiedSolution) {
		this.simplifiedSolution = simplifiedSolution;
	}

	/**
	 * @return the fitnesses
	 */
	public double[] getFitnesses() {
		return fitnesses;
	}
	
	
	
}
