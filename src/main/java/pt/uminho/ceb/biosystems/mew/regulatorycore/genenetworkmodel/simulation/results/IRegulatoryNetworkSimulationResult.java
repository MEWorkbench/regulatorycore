package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results;

import java.util.ArrayList;
import java.util.HashSet;

import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public interface IRegulatoryNetworkSimulationResult {
	
	
	
	public IRegulatoryModel getRegulatoryNetworkModel();
    
    public ArrayList<ArrayList<Boolean>> getSimulationSolution();
    
    public ArrayList<String> getGeneKnockouts();
    
    public HashSet<String> getInitialDeactivatedGenes();
    
    public ArrayList<String> getTrueVaribles();
    
    public IndexedHashMap<Gene, Boolean> getGeneStates();
    
    public  IndexedHashMap<Integer, IndexedHashMap<Gene, Boolean>> getMapofGenesinAllAtractors();
    
    public IndexedHashMap<String, Boolean> getVariableStates();
    
    public ArrayList<String> getGenesAlwaysTrueInAttractor();
    
    public ArrayList<String> getGenesAlwaysFalseInAttractor();
    

	
	
	

}
