package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.controlcenter.subcomponents;

import java.io.Serializable;

import pt.uminho.ceb.biosystems.jecoli.algorithm.AlgorithmTypeEnum;

public class OptimizationDefinitionsContainer implements Serializable{


	private static final long serialVersionUID = 1L;
	
	
	protected int numberOfFunctionEvaluations;
	protected int numberOfKnockouts;
	protected boolean isVariableSizeGenome;
	protected AlgorithmTypeEnum algorithmType;
	protected String UsedIntegratedSimulationMethod=null;
	protected String UsedMetabolicSimulationMethod=null;
	

	public OptimizationDefinitionsContainer(int numberOfFunctionEvaluations,
			int numberOfKnockouts, boolean isVariableSizeGenome, AlgorithmTypeEnum algorithmType, String usedIntSimMet, String usedMetSimMet) {
		this.numberOfFunctionEvaluations = numberOfFunctionEvaluations;
		this.numberOfKnockouts = numberOfKnockouts;
		this.isVariableSizeGenome = isVariableSizeGenome;
		this.algorithmType = algorithmType;
		this.UsedIntegratedSimulationMethod = usedIntSimMet;
		this.UsedMetabolicSimulationMethod = usedMetSimMet;
	}
	
	
	public int getNumberOfFunctionEvaluations() {
		return numberOfFunctionEvaluations;
	}
	public void setNumberOfFunctionEvaluations(int numberOfFunctionEvaluations) {
		this.numberOfFunctionEvaluations = numberOfFunctionEvaluations;
	}
	public int getNumberOfKnockouts() {
		return numberOfKnockouts;
	}
	public void setNumberOfKnockouts(int numberOfKnockouts) {
		this.numberOfKnockouts = numberOfKnockouts;
	}
	public boolean isVariableSizeGenome() {
		return isVariableSizeGenome;
	}
	public void setVariableSizeGenome(boolean isVariableSizeGenome) {
		this.isVariableSizeGenome = isVariableSizeGenome;
	}


	public AlgorithmTypeEnum getAlgorithmType() {
		return algorithmType;
	}


	public void setAlgorithmType(AlgorithmTypeEnum algorithmType) {
		this.algorithmType = algorithmType;
	}


	public String getUsedIntegratedSimulationMethod() {
		return UsedIntegratedSimulationMethod;
	}


	public void setUsedIntegratedSimulationMethod(
			String usedIntegratedSimulationMethod) {
		UsedIntegratedSimulationMethod = usedIntegratedSimulationMethod;
	}


	public String getUsedMetabolicSimulationMethod() {
		return UsedMetabolicSimulationMethod;
	}


	public void setUsedMetabolicSimulationMethod(
			String usedMetabolicSimulationMethod) {
		UsedMetabolicSimulationMethod = usedMetabolicSimulationMethod;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	

}
