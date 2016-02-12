package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.resultcontainers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.IRegulatoryNetworkSimulationResult;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public abstract class AbstractRegulatoryNetworkSimulationResults implements IRegulatoryNetworkSimulationResult, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected IRegulatoryModel regmodel;
	protected ArrayList<ArrayList<Boolean>> atractor;
	protected ArrayList<String> geneKnockouts;
	protected HashSet<String> initDeactivatedGenes;
	protected ArrayList<String> truevariables;
	protected ArrayList<String> falseTFvariables;
	protected IndexedHashMap<Gene, Boolean> mapgenestates;
	protected IndexedHashMap<String, Boolean> mapvariablestates;
	protected ArrayList<String> GenesAlwaysTrueInAttractor;
	protected ArrayList<String> GenesAlwaysFalseInAttractor;
	protected ArrayList<String> GenesUndefinedInAttractor;
	protected IndexedHashMap<Integer, IndexedHashMap<Gene, Boolean>> mappingGenesAllAtractors;
	
	protected boolean debug = false;
	
	public AbstractRegulatoryNetworkSimulationResults (IRegulatoryModel model, 
			ArrayList<ArrayList<Boolean>> simulres, 
			HashSet<String> initdeactgenes, 
			ArrayList<String> geneknock, 
			ArrayList<String> trueVars, 
			ArrayList<String> falsetfvariables,
			ArrayList<String> genesalwaystrueattractor,
			ArrayList<String> genesalwaysfalseattractor,
			ArrayList<String> genesundefinedattractor){
		
		regmodel = model;
		atractor = simulres;
		geneKnockouts= geneknock;
		initDeactivatedGenes = initdeactgenes;
		truevariables = trueVars;
		falseTFvariables = falsetfvariables;
		GenesAlwaysTrueInAttractor = genesalwaystrueattractor;
		GenesAlwaysFalseInAttractor = genesalwaysfalseattractor;
		GenesUndefinedInAttractor = genesundefinedattractor;
		
		mapLastGeneState();
		mapConditionVariableStates();
		mapAllGeneStates();
	
	
	}
	
    public IRegulatoryModel getRegulatoryNetworkModel(){
    	
    	return regmodel;
    }
   
    public IndexedHashMap<Integer, IndexedHashMap<Gene, Boolean>> getMapofGenesinAllAtractors() {
  		return mappingGenesAllAtractors;
  	}
    
    public ArrayList<ArrayList<Boolean>> getSimulationSolution(){
    	
    	return atractor;
    }
    
    public ArrayList<String> getGeneKnockouts(){
    	
    	return geneKnockouts;
    }
    
    public HashSet<String> getInitialDeactivatedGenes(){
    	
    	return initDeactivatedGenes;
    }
    
    public ArrayList<String> getTrueVaribles(){
    	
    	return truevariables;
    }
    
    public IndexedHashMap<Gene, Boolean> getGeneStates(){
    	return mapgenestates;
    }
    
    
    public IndexedHashMap<String, Boolean> getVariableStates(){
    	return mapvariablestates;
    }
    
	
	public void mapLastGeneState(){
		
		int numgenes = regmodel.getNumberOfGenes();
		mapgenestates = new IndexedHashMap<Gene, Boolean>();
		ArrayList<Boolean> genestate = atractor.get(0);
		
		for (int i = 0; i < numgenes; i++) {
			mapgenestates.put(regmodel.getGene(i), genestate.get(i));

		}
	
	}
	
	
	
	public void mapAllGeneStates(){
		mappingGenesAllAtractors = new IndexedHashMap<Integer, IndexedHashMap<Gene,Boolean>>();
		int numgenes = regmodel.getNumberOfGenes();
		int numatractores = atractor.size();
		
		for (int i = 0; i < numatractores; i++) {
			ArrayList<Boolean> genestate = atractor.get(i);
			IndexedHashMap<Gene, Boolean> genestatesmap = new IndexedHashMap<Gene, Boolean>();
			for (int j = 0; j < numgenes; j++) {
				genestatesmap.put(regmodel.getGene(j), genestate.get(j));
			}
			
			mappingGenesAllAtractors.put(i, genestatesmap);
			
		}
		
		
	}
	
	
	public void mapConditionVariableStates(){
		
		mapvariablestates = new IndexedHashMap<String, Boolean>();
		ArrayList<String> vars = regmodel.getVariablesInNetwork();
		
		for (int i = 0; i < vars.size(); i++) {
			
			if(regmodel.getVarType(vars.get(i)).equals(VariableCI.CONDITION)){
			      if (truevariables !=null && truevariables.contains(vars.get(i)))
				     mapvariablestates.put(vars.get(i), true);
			      else
				     mapvariablestates.put(vars.get(i), false);
			}
			else if(regmodel.getVarType(vars.get(i)).equals(VariableCI.TF)){
				if(falseTFvariables!=null && falseTFvariables.contains(vars.get(i)))
					mapvariablestates.put(vars.get(i), false);
				else
					mapvariablestates.put(vars.get(i), true);
				
			}
			
	    }

		
	 }
	

	
	public ArrayList<String> getGenesAlwaysTrueInAttractor() {
		return GenesAlwaysTrueInAttractor;
	}

	public ArrayList<String> getGenesAlwaysFalseInAttractor() {
		return GenesAlwaysFalseInAttractor;
	}

	public ArrayList<String> getGenesUndefinedInAttractor() {
		return GenesUndefinedInAttractor;
	}



}
