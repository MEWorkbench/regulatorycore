package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.model.components.Compartment;
import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.core.model.components.GeneReactionRule;
import pt.uminho.ceb.biosystems.mew.core.model.components.IStoichiometricMatrix;
import pt.uminho.ceb.biosystems.mew.core.model.components.Metabolite;
import pt.uminho.ceb.biosystems.mew.core.model.components.Pathway;
import pt.uminho.ceb.biosystems.mew.core.model.components.Protein;
import pt.uminho.ceb.biosystems.mew.core.model.components.ProteinReactionRule;
import pt.uminho.ceb.biosystems.mew.core.model.components.Reaction;
import pt.uminho.ceb.biosystems.mew.core.model.components.enums.ModelType;
import pt.uminho.ceb.biosystems.mew.core.model.exceptions.InvalidSteadyStateModelException;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.gpr.SteadyStateGeneReactionModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryVariable;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class IntegratedSteadyStateModel extends SteadyStateGeneReactionModel implements IIntegratedStedystateModel, Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected IRegulatoryModel regulatoryNet;
	protected VariablesContainer varscontainer;
	

	// Genes
	protected HashMap<String,Integer> mappingRegGenesandMetGenes;
	protected ArrayList<String> onlyRegulatoryGenes;


	
	
	public IntegratedSteadyStateModel(String modelId,
			IStoichiometricMatrix stoichiometricMatrix,
			IndexedHashMap<String, Reaction> reactions,
			IndexedHashMap<String, Metabolite> metabolites,
			Map<String, Compartment> compartments,
			IndexedHashMap<String, Pathway> pathways,
			IndexedHashMap<String, Gene> genes, // metabolic genes
			IndexedHashMap<String, Protein> proteins,
			IndexedHashMap<String, GeneReactionRule> geneReactionRules,
			IndexedHashMap<String, ProteinReactionRule> proteinReactionRules,
			IRegulatoryModel regulatoryNet )
	
			throws InvalidSteadyStateModelException {
		super(modelId, stoichiometricMatrix, reactions, metabolites, compartments,
				pathways, genes, proteins, geneReactionRules, proteinReactionRules);
		this.regulatoryNet = regulatoryNet;
		mapRegulatoryToMetabolicGenes(genes);
		mapvariables(reactions);
		
	
	}
	
	
	

    public VariablesContainer getVariablescontainer(){
		
		return (VariablesContainer) this.varscontainer;
	}
	

	public Boolean isMetabolicAndRegulatoryGene(String geneId){
		return mappingRegGenesandMetGenes.containsKey(geneId);
	}
	
	public IRegulatoryModel getRegulatoryNet(){
		return regulatoryNet;
	}
	
	public Integer getNumberOfGenesInNetAndModel(){
		return mappingRegGenesandMetGenes.size();
	}
	
	
	public Boolean isRegulatoryGene(String geneId){
		return regulatoryNet.getGene(geneId)!=null;
	}
	
	public Boolean isMetabolicGene(String geneId){
		return getGene(geneId)!=null;
	}
	
	

	public Set<String> getonlyMetabolicGenes(){
		HashSet<String> genes = new HashSet<String>();
		
		IndexedHashMap<String, Gene> metabolicGenes = getGenes();
		
		for(String geneId : metabolicGenes.keySet())
			if(!isRegulatoryGene(geneId))
				genes.add(geneId);
		
		return genes;
	}
	
	
	public ArrayList<String> filterOnlyMetabolicGenes(ArrayList<String> geneslist){
		
		ArrayList<String> metabolicGenes = new ArrayList<String>();
		for(int i =0; i < geneslist.size(); i++){
			
			if(genes.containsKey(geneslist.get(i))){
				metabolicGenes.add(geneslist.get(i));
			}
			
		}
		return metabolicGenes;
	}
	
	
   public ArrayList<String> filterOnlyRegulatoryGenes(ArrayList<String> geneslist){
		
		ArrayList<String> regulatoryGenes = new ArrayList<String>();
		for(int i =0; i < geneslist.size(); i++){
			
			if(!genes.containsKey(geneslist.get(i))){
				regulatoryGenes.add(geneslist.get(i));
			}	
		}
		return regulatoryGenes;
	}
	
	
	// get all regulatory genes 
	public ArrayList<String> getGenesInRegulatoryModel(){
		 
		ArrayList<String> res = new ArrayList<String>();
		for(String geneId : this.regulatoryNet.getGenesId()){
		     res.add(geneId);
		}
		
		return res;
	}
	
	
	/*
	public ArrayList<String> getRegulatorygenes(){
		
		return filterOnlyRegulatoryGenes(getGenesInRegulatoryModel());
		
	}*/

	
	/*
	 * Map genes from metabolic model with genes that are present in Regulatory model, the mapping is performed by the name 
	 * of regulatory gene to their position on metabolic model 
	 * 
	 */
	protected void mapRegulatoryToMetabolicGenes(IndexedHashMap<String, Gene> genes){
		mappingRegGenesandMetGenes=new HashMap<String, Integer>();
         
		for(String geneId : regulatoryNet.getGenesId()){
			
			if(genes.containsKey(geneId)){
				mappingRegGenesandMetGenes.put(geneId, getGeneIndex(geneId));
			}
		}	
	}
	
     
	
	
	
	
	/*
	 * Map the variables that are present in Regulatory model and Metabolic Model, also do the mapping of the variables and the linked 
	 * metabolic reactions.
	 */
	
	protected void mapvariables(IndexedHashMap<String, Reaction> checkreactions){
		IndexedHashMap<String, Integer> userConditionsVariablesMappings = new IndexedHashMap<String, Integer>();
		IndexedHashMap<String, Integer> VariablesofNetworkPresentinMetabolicModelMapping = new IndexedHashMap<String, Integer>();
		IndexedHashMap<String, Integer> ALLVariables = new IndexedHashMap<String, Integer>();
		IndexedHashMap<String, String> variablesTypeMap = new IndexedHashMap<String, String>();
		HashMap<String, Integer> VariablestoreactionsMappings = new HashMap<String, Integer>();
		IndexedHashMap<String, Integer> TFVariablesMappings = new IndexedHashMap<String, Integer>();

	
		
		IndexedHashMap<String, RegulatoryVariable> regulatoryVariables= regulatoryNet.getVariablesRegNetwork();

		for (int j = 0; j < regulatoryVariables.size(); j++) {
		    
			if(regulatoryVariables.getValueAt(j).getType().equals(VariableCI.CONDITION)){
			
			     String varAux = regulatoryVariables.getKeyAt(j).split("[<,>]")[0].trim();
			
			     if(checkreactions.containsKey(varAux)){
			    	 VariablesofNetworkPresentinMetabolicModelMapping.put(regulatoryVariables.getKeyAt(j), j);
					 ALLVariables.put(regulatoryVariables.getKeyAt(j), j);
					 VariablestoreactionsMappings.put(regulatoryVariables.getKeyAt(j),getReactionIndex(varAux)); 
			     }
			     else{
			    	 userConditionsVariablesMappings.put(regulatoryVariables.getKeyAt(j),j);
					 ALLVariables.put(regulatoryVariables.getKeyAt(j), j);

					}
			     variablesTypeMap.put(regulatoryVariables.getKeyAt(j), VariableCI.CONDITION);
			}
			else if(regulatoryVariables.getValueAt(j).getType().equals(VariableCI.TF)){
				
				TFVariablesMappings.put(regulatoryVariables.getKeyAt(j), j);
				ALLVariables.put(regulatoryVariables.getKeyAt(j), j);
				variablesTypeMap.put(regulatoryVariables.getKeyAt(j), VariableCI.TF);
				
			}
			
		}
		
		this.varscontainer = new VariablesContainer(ALLVariables,userConditionsVariablesMappings,VariablesofNetworkPresentinMetabolicModelMapping,VariablestoreactionsMappings,variablesTypeMap,TFVariablesMappings);
		this.varscontainer.setRegulatoryModel(regulatoryNet);
		
	}
	
	
	@Override
	public ModelType getModelType() {
		return ModelType.INTEGRATED_STEADY_STATE_MODEL;
	}




	@Override
	public void setRegulatoryNet(IRegulatoryModel regmodel) {
		this.regulatoryNet=regmodel;
		mapRegulatoryToMetabolicGenes(genes);
		mapvariables(reactionMap);
		
	}
	
	
	
}
