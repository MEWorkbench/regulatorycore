package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.formulations.methods;

import java.util.ArrayList;
import java.util.HashSet;

import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.MandatoryPropertyException;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.PropertyCastException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryRule;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryVariable;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.formulations.NetworkMemory;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.formulations.abstractions.AbstractRegulatoryNetworkSimulation;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.IRegulatoryNetworkSimulationResult;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.resultcontainers.BRNSimulationResults;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.Environment;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.BooleanValue;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;

public class BRNV extends AbstractRegulatoryNetworkSimulation{

	
	protected Integer lenghtCycle = null;
	protected int iterations = 100;
	protected NetworkMemory memory = null;
	protected ArrayList<Boolean> firstStep;
	protected Boolean[] variablesValues;
	protected ArrayList<String> trueGenes;
	protected ArrayList<String> falseGenes;
	protected ArrayList<String> undefinedGenes;

	
	
	public BRNV(IRegulatoryModel model) {
		
		super(model);	
	}

	
	private void calcFirstStep(HashSet<String> initdeactivatedGenes) throws PropertyCastException, MandatoryPropertyException{
		
		firstStep = new ArrayList<Boolean>();
		
		
		if(initdeactivatedGenes != null){ 
		for (int i = 0; i < model.getNumberOfGenes(); i++) {

			if (initdeactivatedGenes.contains(model.getGene(i).getId())){
			    firstStep.add(false);
			}
			else
			    firstStep.add(true);	
		  }
		}
		
		else{
			if(getGeneKnockouts() != null){
				for (int i = 0; i < model.getNumberOfGenes(); i++) {
					if(getGeneKnockouts().contains(model.getGene(i).getId())){
					   firstStep.add(false);
					}
					else 
						 firstStep.add(true);		
				}
			}
			else{
				for (int i = 0; i < model.getNumberOfGenes(); i++) {
					firstStep.add(true);
				}
			}
		}
	
		
		
	}
	
	
	private void SetConditionVariablesValues(ArrayList<String> truevars){
		
		variablesValues = new Boolean [model.getNumberOfVariables()];
		IndexedHashMap<String, RegulatoryVariable> allvars = model.getVariablesRegNetwork();
		
		if(truevars!=null){
			
			for (int i = 0; i < allvars.size(); i++) {
				
				if(truevars.contains(allvars.getKeyAt(i)) && allvars.getValueAt(i).getType().equals(VariableCI.CONDITION) ){
					variablesValues[i]=true;	
				}
				else{
					if(allvars.getValueAt(i).getType().equals(VariableCI.TF))
						variablesValues[i]=true;
					else
						variablesValues[i]=false;
					
				}
				
			}
		}
		
		else{
			for (int i = 0; i < allvars.size(); i++) {
				
				if(allvars.getValueAt(i).getType().equals(VariableCI.TF))
					variablesValues[i]=true;
				else
					variablesValues[i]=false;	
				
				
			}
		}

	}

	
	private void SetTFVariablesValues(ArrayList<String> falseTFs) throws Exception{
		
		if(falseTFs != null && falseTFs.size()>0){
		for (int i = 0; i < falseTFs.size(); i++) {
			
			int pos = model.getVariableIndex(falseTFs.get(i));
			if(pos!=-1)
			variablesValues[pos]=false;
			
		   }
		
		}
	}
	
	
	
	public Environment<IValue> createEnvironment(ArrayList<Boolean> geneExpression) {

		Environment<IValue> environment = new Environment<IValue>();
		ArrayList<String> variablesNames = model.getVariablesInNetwork();
		
	
		
		for (int i = 0; i < variablesNames.size(); i++) {

			environment.associate(variablesNames.get(i), new BooleanValue(variablesValues[i]));
		}

		addExpressionInIEnvironment(environment, geneExpression);

		return environment;
	}
	
	
	private void addExpressionInIEnvironment(Environment<IValue> environment, ArrayList<Boolean> exp) {
      
		for (int i = 0; i < model.getNumberOfRegulatoryRules(); i++){

			environment.associate(model.getRegulatoryRule(i).getRuleId(), new BooleanValue(exp.get(i)));
		  
		}
	}
	
	public void setExpressionSet(ArrayList<Boolean> step) {
		if (step.size() == model.getNumberOfGenes())
			memory.push(step);

	}
	
	public void setGeneExpression(int idx, Boolean value){
		firstStep.set(idx, value);
	}
	
	public void setGeneExpression(String geneId, Boolean value){
		int idx = model.getRegulatoryGeneIndex(geneId);
		firstStep.set(idx, value);
	}
	
	
	private ArrayList<Boolean> oneIteration(Environment<IValue> environment) {
		ArrayList<Boolean> antIteration = memory.lookTop();
		ArrayList<Boolean> iteration = new ArrayList<Boolean>();

		
		for (int i = 0; i < model.getNumberOfRegulatoryRules(); i++) {
			RegulatoryRule rule = model.getRegulatoryRule(i);
			
			if (rule.getBooleanRule().getRootNode() != null) {
				boolean value = (boolean) rule.getBooleanRule().evaluate(environment).getValue();
				iteration.add(value);

			} else{
				iteration.add(antIteration.get(i));

				
			}
	

		}

		return iteration;
	}

	private void calcSolution(){
		trueGenes = new ArrayList<String>();
		falseGenes = new ArrayList<String>();
		undefinedGenes = new ArrayList<String>();
		
		
		ArrayList<ArrayList<Boolean>> cycle = memory.getCycle();
		
		for (int g = 0; g < model.getNumberOfGenes(); g++) {
			boolean geneAlwaysTrue = cycle.get(0).get(g);
			boolean geneAlwaysFalse = !cycle.get(0).get(g);
			
			for(int i = 1; i < cycle.size() && (geneAlwaysFalse || geneAlwaysTrue); i++){
				geneAlwaysTrue = geneAlwaysTrue && cycle.get(i).get(g);
				geneAlwaysFalse = geneAlwaysFalse && !cycle.get(i).get(g);
			}
			
			if(geneAlwaysFalse)
				falseGenes.add(model.getGene(g).getId());
			else if(geneAlwaysTrue)
				trueGenes.add(model.getGene(g).getId());
			else
				undefinedGenes.add(model.getGene(g).getId());
		}
	}
	
	
	private void run(){
		
		try {
			
			createModelOverride();
			calcFirstStep(getInitDeactivatedGenes());
			SetConditionVariablesValues(getTrueVariables());
			SetTFVariablesValues(getTFsWithFalseValues());
		} catch (PropertyCastException e) {
			e.printStackTrace();
		} catch (MandatoryPropertyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int checkCycle = -1;
		
		
		memory = new NetworkMemory(firstStep);
		ArrayList<Boolean> geneExp = memory.lookTop();
        

		
		
		Environment<IValue> environment = createEnvironment(geneExp);
        
		for (int i = 0; i < iterations && checkCycle == -1; i++) {
		
			addExpressionInIEnvironment(environment, geneExp);
			geneExp = oneIteration(environment);
			memory.push(geneExp);
			checkCycle = memory.checkRedundancy();
			
		}
		this.lenghtCycle = checkCycle;
		
	}
	
	
	
	public ArrayList<String> getGenesAlwaysTrueInAttractor(){
		if(trueGenes==null)
			calcSolution();
		return trueGenes;
	}
	
	public ArrayList<String> getGenesAlwaysFalseInAttractor(){
		if(falseGenes==null)
			calcSolution();
		
		return falseGenes;
	}
	
	public ArrayList<String> getGenesUndefinedInAttractor(){
		if(undefinedGenes==null)
			calcSolution();
		return undefinedGenes;
	}
	
    @Override
	public IRegulatoryNetworkSimulationResult simulate() {
		
    	run();
    	calcSolution();
   
    	BRNSimulationResults res = null;
    	try {
			res = new BRNSimulationResults(model, memory.getCycle(), getInitDeactivatedGenes(), getGeneKnockouts(),getTrueVariables(),getTFsWithFalseValues(),trueGenes,falseGenes,undefinedGenes);
		} catch (PropertyCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MandatoryPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return res;
	}
	








}
