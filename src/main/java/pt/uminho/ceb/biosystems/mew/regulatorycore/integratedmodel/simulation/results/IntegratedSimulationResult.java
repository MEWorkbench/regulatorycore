package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.FluxValueMap;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPSolutionType;

public class IntegratedSimulationResult extends SteadyStateSimulationResult implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ISteadyStateModel model;
	protected ArrayList<ArrayList<Boolean>> regulatoryAtractor;
	protected String metSimulMethod = null;
	protected VariablesContainer initialUsedVariablesContainer;
	protected VariablesContainer finalvariablesContaineraftersimulation;
	 
	protected RegulatoryGeneticConditions oldRegulatoryConditions;
	protected RegulatoryGeneticConditions actualregulatoryConditions;
	protected ArrayList<String> reactionsKnockouts;
	protected HashSet<String> usedfalsenodes; 
	protected HashSet<String> initialusedfalsenodes=null;

	

	
	
	
	
	
	public IntegratedSimulationResult(ISteadyStateModel model,
			EnvironmentalConditions environmentalConditions,
			GeneticConditions geneticConditions, 
			String method,
			FluxValueMap fluxValues, 
			String solverOutput, 
			Double oFvalue,
			String oFString, 
			LPSolutionType solutionType,
			ArrayList<ArrayList<Boolean>> regulatoryAtractor,
			VariablesContainer variables,
			String metabolicSimulationMethod
			) {
		super(model, environmentalConditions, geneticConditions, method, fluxValues,
				solverOutput, oFvalue, oFString, solutionType);

		this.regulatoryAtractor = regulatoryAtractor;
		this.metSimulMethod = metabolicSimulationMethod;
		this.initialUsedVariablesContainer = variables;
		setSolutionType(solutionType);
		

		
	}
	
	public IntegratedSimulationResult(ISteadyStateModel model, String method, FluxValueMap fluxValues){
		super(model,method,fluxValues);
		
	}
   

	public ArrayList<String> getMetabolicGenesKnock() {
		return (ArrayList<String>) ((RegulatoryGeneticConditions)geneticConditions).getMetabolicKnockoutList();
	}

    public ArrayList<String> getRegulatoryGenesKnockout() {
		return (ArrayList<String>) ((RegulatoryGeneticConditions)geneticConditions).getRegulatoryKnockoutList();
	}
	
    
    public ArrayList<String> getALLGenesKnockout() {
		return (ArrayList<String>) ((RegulatoryGeneticConditions)geneticConditions).getALLGeneKnockoutList();
	}
	

	public ArrayList<ArrayList<Boolean>> getRegulatoryAtractor() {
		return regulatoryAtractor;
	}

	public void setRegulatoryAtractor(
			ArrayList<ArrayList<Boolean>> regulatoryAtractor) {
		this.regulatoryAtractor = regulatoryAtractor;
	}

	public void setOldRegulatoryGeneticConditions(RegulatoryGeneticConditions geneconds){
		this.oldRegulatoryConditions = geneconds;
	}
	
	public RegulatoryGeneticConditions getOldRegulatoryGeneticConditions(){
		return this.oldRegulatoryConditions;
	}
	
	
	public ArrayList<String> getReactionsKnockouts(){
		return this.reactionsKnockouts;
	}
	

	public void setReactionsKnockouts(ArrayList<String> reactionsKnockouts) {
		this.reactionsKnockouts = reactionsKnockouts;
	}
    

	
	public String getMetSimulMethod() {
		return metSimulMethod;
	}

	public void setMetSimulMethod(String metSimulMethod) {
		this.metSimulMethod = metSimulMethod;
	}


	
	
//	private double roundValue(double value){
//		
//    		Double v = (Double)value;
//    		String val ="";
//    		if (!Double.isNaN(v) && v != 0){
//	        	
//	        	DecimalFormat form = new DecimalFormat();
//	    		form.setMaximumFractionDigits((Integer)PropertiesManager.getPManager().getProperty(PMUtils.DOUBLEP));
//	    		
//	    		val = form.format(v);
//    		}
//		return Double.parseDouble(val); 
//	}

	public VariablesContainer getInitialUsedVariablesContainer() {
		return initialUsedVariablesContainer;
	}

	public void setInitialUsedVariablesContainer(VariablesContainer initialUsedVariablesContainer) {
		this.initialUsedVariablesContainer = initialUsedVariablesContainer;
	}
	

	public VariablesContainer getFinalvariablesContaineraftersimulation() {
		return finalvariablesContaineraftersimulation;
	}

	public void setFinalvariablesContaineraftersimulation(
			VariablesContainer finalvariablesContaineraftersimulation) {
		this.finalvariablesContaineraftersimulation = finalvariablesContaineraftersimulation;
	}

	public HashSet<String> getUsedfalsenodes() {
		return usedfalsenodes;
	}

	public void setUsedfalsenodes(HashSet<String> usedfalsenodes) {
		this.usedfalsenodes = usedfalsenodes;
	}

	public HashSet<String> getInitialusedfalsenodes() {
		return initialusedfalsenodes;
	}

	public void setInitialusedfalsenodes(HashSet<String> initialusedfalsenodes) {
		this.initialusedfalsenodes = initialusedfalsenodes;
	}
	

}
