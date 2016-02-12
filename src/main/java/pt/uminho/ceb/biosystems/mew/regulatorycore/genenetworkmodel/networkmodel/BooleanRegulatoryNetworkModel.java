package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryRule;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryVariable;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.parser.ParseException;

public class BooleanRegulatoryNetworkModel implements IRegulatoryModel, Serializable{
	
	private static final long serialVersionUID = 1L;
	private boolean debug=true;
	protected String modelID;
	protected IndexedHashMap<String, Gene> genesRegNetwork;
	protected IndexedHashMap<String, Gene> genesIntegratedRegNetwork; // used to include  all metabolic genes that are not defined in regulatory model
	protected ArrayList<String> missingMetabolicGenes=null;
	
	// RegulatoryRules Ids = transcriptional factor name
	protected IndexedHashMap<String, RegulatoryRule> geneRulesRegNetwork;
	protected IndexedHashMap<String, RegulatoryRule> geneRulesIntegratedRegNetwork;
	protected IndexedHashMap<String, RegulatoryRule> mapASTRuleIdWithRegulatoryRule;
	protected IndexedHashMap<String, RegulatoryVariable> variablesRegNetwork;
	protected IndexedHashMap<String, String> RegVariableType;
	protected IndexedHashMap<String, String> mapOfRegGenesToASTRuleID; // key=geneID   value=TFnameAssociatedtogene;
	protected HashMap<String, String> mapASTIDtoGeneID=null;
    protected boolean useintegratedSimul=true;
    

	public BooleanRegulatoryNetworkModel(String modelid,IndexedHashMap<String, RegulatoryRule> regulatoryRules, IndexedHashMap<String, Gene> genes, IndexedHashMap<String, RegulatoryVariable> vars, IndexedHashMap<String, String> mapgeneidtoASTgeneID){
		this.modelID=modelid;
		this.geneRulesRegNetwork = regulatoryRules;
		setGeneRulesIntegratedRegNetwork();
		this.genesRegNetwork = genes;
		setGenesIntegratedRegNetwork();
		this.variablesRegNetwork = vars;
		this.mapOfRegGenesToASTRuleID = mapgeneidtoASTgeneID;
		setMapOfVariablesType();
		setmapASTRuleIDtoGeneId();
		setMapASTRULEidtoRegRule();
	}
	
	public BooleanRegulatoryNetworkModel(IndexedHashMap<String, RegulatoryRule> regulatoryRules, IndexedHashMap<String, RegulatoryRule> rulesIntegratedRegNetwork,IndexedHashMap<String, Gene> genes,IndexedHashMap<String, Gene> genesIntegratedRegNetwork, IndexedHashMap<String, RegulatoryVariable> vars,IndexedHashMap<String, String> regulatoryVariablesType, IndexedHashMap<String, String> mapgeneidtoASTgeneID, ArrayList<String> missingmetabolicgenes, boolean isuseintegrated){
		this.geneRulesRegNetwork = regulatoryRules;
		this.geneRulesIntegratedRegNetwork=rulesIntegratedRegNetwork;
		this.genesRegNetwork = genes;
		this.genesIntegratedRegNetwork=genesIntegratedRegNetwork;
		this.variablesRegNetwork = vars;
		this.mapOfRegGenesToASTRuleID = mapgeneidtoASTgeneID;
		this.RegVariableType= regulatoryVariablesType;
		this.missingMetabolicGenes=missingmetabolicgenes;
		setmapASTRuleIDtoGeneId();
		setMapASTRULEidtoRegRule();
		this.useintegratedSimul=isuseintegrated;
	}
	
	@Override
	public void setUseForIntegratedSimulation(boolean isintegrated) {
		this.useintegratedSimul=isintegrated;
	}
	
	private void setGenesIntegratedRegNetwork(){
		this.genesIntegratedRegNetwork= new IndexedHashMap<>();
		for (int i = 0; i < genesRegNetwork.size(); i++) {
			genesIntegratedRegNetwork.putAt(i, genesRegNetwork.getKeyAt(i), genesRegNetwork.getValueAt(i));
		}
	
	}
	
	private void setGeneRulesIntegratedRegNetwork(){
		this.geneRulesIntegratedRegNetwork=new IndexedHashMap<>();
		for (int i = 0; i < geneRulesRegNetwork.size(); i++) {
			geneRulesIntegratedRegNetwork.putAt(i, geneRulesRegNetwork.getKeyAt(i), geneRulesRegNetwork.getValueAt(i));
		}
		
	}
	
	
   private void setMapOfVariablesType(){
	   RegVariableType = new IndexedHashMap<String, String>();
	   for (int i = 0; i < variablesRegNetwork.size(); i++) {
		   RegVariableType.put(variablesRegNetwork.getKeyAt(i), variablesRegNetwork.getValueAt(i).getType());
	    }
   }
	

	public IndexedHashMap<String, Gene> getGenesRegNetwork() {
		if(useintegratedSimul)
			return genesIntegratedRegNetwork;
		else
		    return genesRegNetwork;
	}
	
	
	
	public ArrayList<String> getRegulatoryGeneIDs(){
		ArrayList<String> genesnames = new ArrayList<>();
		if(useintegratedSimul){
			for (Gene gene : genesIntegratedRegNetwork.values()) {
				genesnames.add(gene.getId());
			}
		}
		else{
		   for (Gene gene : genesRegNetwork.values()) {
			    genesnames.add(gene.getName());
		   }
		}
		return genesnames;
	}

	public IndexedHashMap<String, RegulatoryRule> getGeneRegRules() {
		if(useintegratedSimul)
			return geneRulesIntegratedRegNetwork;
		else
		    return geneRulesRegNetwork;
	}


	public IndexedHashMap<String, RegulatoryVariable> getVariablesRegNetwork() {
		return this.variablesRegNetwork;
	}
	

	public Integer getNumberOfRegulatoryRules() {
		if(useintegratedSimul)
			return geneRulesIntegratedRegNetwork.size();
		else
		   return geneRulesRegNetwork.size();
	}

	public RegulatoryRule getRegulatoryRule(int ruleIndex) {
		if(useintegratedSimul)
			return geneRulesIntegratedRegNetwork.getValueAt(ruleIndex);
		else
		    return geneRulesRegNetwork.getValueAt(ruleIndex);
	}
 
	public RegulatoryRule getRegulatoryRule(String ruleId) {
		if(useintegratedSimul)
			return geneRulesIntegratedRegNetwork.get(ruleId);
		else
		    return geneRulesRegNetwork.get(ruleId);
	}
	
	
	public HashMap<String, String> getmapASTIDtoGeneName(){
		return this.mapASTIDtoGeneID;
	}

	@Override
	public Set<String> getGenesId() {
		if(useintegratedSimul)
			return genesIntegratedRegNetwork.keySet();
		else
		    return genesRegNetwork.keySet();
	}

	@Override
	public Integer getNumberOfGenes() {
		if(useintegratedSimul)
			return genesIntegratedRegNetwork.size();
		else
		    return genesRegNetwork.size();
	}
	
	public Gene getGene(int geneIndex){
		if(useintegratedSimul)
			return genesIntegratedRegNetwork.getValueAt(geneIndex);
		else
		    return genesRegNetwork.getValueAt(geneIndex);
	}
	
	public Gene getGene(String geneId){
		if(useintegratedSimul)
			return genesIntegratedRegNetwork.get(geneId);
		else
		    return genesRegNetwork.get(geneId);
	}
    
	public IndexedHashMap<String, Gene> getRegulatoryGenes(){
		if(useintegratedSimul)
			return this.genesIntegratedRegNetwork;
		else
		    return this.genesRegNetwork;
	}
	
	
	public void setRegulatoryRules(IndexedHashMap<String, RegulatoryRule> newrules){
		this.geneRulesRegNetwork=newrules;
		this.geneRulesIntegratedRegNetwork=newrules;
	}
	
	
	
	public String getVarType(String var){
		for(Map.Entry<String, RegulatoryVariable> map : variablesRegNetwork.entrySet()){
			if(map.getKey().equals(var))
				return map.getValue().getType();
		}
		
		return null;	
	}
    
	
	public IndexedHashMap<String, String> getRegulatoryVariablesType(){
		return this.RegVariableType;
	}
	

	
	@Override
	public Integer getNumberOfVariables() {
		return variablesRegNetwork.size();
	}

	@Override
	public Integer getVariableIndex(String variableName) throws Exception {
		int index = -1;
		if (variablesRegNetwork.containsKey(variableName))
			index = variablesRegNetwork.getIndexOf(variableName);
		else throw new Exception("Variable "+variableName+" not found");
			
		return index;	
	
	}
	
	
	public RegulatoryVariable getVariableByIndex(int variableIDX){
		
		return variablesRegNetwork.getValueAt(variableIDX);
	}
	
	
	public ArrayList<String> getVariablesInNetwork(){
		
		ArrayList<String> vars = new ArrayList<String>();
		for (RegulatoryVariable var : variablesRegNetwork.values()) {
			vars.add(var.getId());
		}
		
		return vars;
	}
	
	
	
	public Integer getComponentRuleIndex(String ruleId){
			return mapASTRuleIdWithRegulatoryRule.getIndexOf(ruleId);
	}


	public String getRegulatoryRuleGeneLinkId(Integer ruleIndex) {
		if(useintegratedSimul)
			return geneRulesIntegratedRegNetwork.getKeyAt(ruleIndex);
		else
		    return geneRulesRegNetwork.getKeyAt(ruleIndex);
	}
	
	@Override
	public String getRuleLinkId(Integer ruleIndex) {
		if(useintegratedSimul)
			return geneRulesIntegratedRegNetwork.getValueAt(ruleIndex).getRuleId();
		else
			return geneRulesRegNetwork.getValueAt(ruleIndex).getRuleId();
	}

	public Integer getRegulatoryGeneIndex(String geneId) {
		if(useintegratedSimul)
			return genesIntegratedRegNetwork.getIndexOf(geneId);
		else
		    return genesRegNetwork.getIndexOf(geneId);
	}
	
	public String getRuleLinkIDByGeneID(String geneid){
		return mapASTIDtoGeneID.get(geneid);
	}
	
	
	public Integer getGeneIndex(String geneId){
		if(useintegratedSimul)
			return genesIntegratedRegNetwork.getIndexOf(geneId);
		else
		    return genesRegNetwork.getIndexOf(geneId);
	}
	


	public IndexedHashMap<String, String> getMapOfRegGenesToASTgeneID() {
		return mapOfRegGenesToASTRuleID;
	}


    public String getGeneIDassociatedtoTFinAST(String tfname){
    	
    	for (Map.Entry<String, String> map : mapOfRegGenesToASTRuleID.entrySet()) {
    		
    		if(map.getValue().equals(tfname))
			 return map.getKey();
		}
    	return null;
    	
    }
    
    
    private void setmapASTRuleIDtoGeneId(){
    	if (this.mapASTIDtoGeneID == null)
    		 this.mapASTIDtoGeneID=new HashMap<>();
    	
    		 for (int i = 0; i < genesRegNetwork.size(); i++) {
				String geneid = genesRegNetwork.getKeyAt(i);
				String ASTID = geneRulesRegNetwork.getValueAt(i).getRuleId();
				this.mapASTIDtoGeneID.put(ASTID,geneid);
			}
    }
    
    private void setMapASTRULEidtoRegRule(){
    	if(this.mapASTRuleIdWithRegulatoryRule==null)
    		this.mapASTRuleIdWithRegulatoryRule=new IndexedHashMap<>();
    	
    	for (int i = 0; i < geneRulesIntegratedRegNetwork.size(); i++) {
			String ruleid = geneRulesIntegratedRegNetwork.getValueAt(i).getRuleId();
			RegulatoryRule rule = geneRulesIntegratedRegNetwork.getValueAt(i);
			this.mapASTRuleIdWithRegulatoryRule.putAt(i, ruleid, rule);
		}
    	
    }

	public ArrayList<String> getTFsNames(){
		
		ArrayList<String> tfs = new ArrayList<>();
		for (Map.Entry<String, RegulatoryRule> regrule : geneRulesRegNetwork.entrySet()) {
			 tfs.add(regrule.getValue().getRuleId());	
		}
		
       return tfs;
	}
	
	
	
  
	
	
	public void checkMetabolicGeneMissingRules(IndexedHashMap<String,Gene> metabolicgenes) throws ParseException{
	     
		IndexedHashMap<String, RegulatoryRule> extraRegRules=null;
		IndexedHashMap<String, Gene> extraGenes=null;
		
		for (int i = 0; i < metabolicgenes.size(); i++) {
			if(!this.genesIntegratedRegNetwork.containsKey(metabolicgenes.getKeyAt(i))){
				if(this.missingMetabolicGenes==null)
					this.missingMetabolicGenes=new ArrayList<>();
                if(extraRegRules==null)
                	extraRegRules= new IndexedHashMap<>();
                if(extraGenes==null)
                	extraGenes=new IndexedHashMap<>();
				
			    String geneid= metabolicgenes.getKeyAt(i); 
				RegulatoryRule metgenerule = new RegulatoryRule(geneid, "");	
				Gene mettoreggene = new Gene(geneid, metabolicgenes.getValueAt(i).getName());	
				
				
				extraRegRules.put(geneid, metgenerule);
				extraGenes.put(geneid, mettoreggene);
				missingMetabolicGenes.add(metabolicgenes.getKeyAt(i));

			}	
		}
		
		if(extraRegRules!=null)
		extendRulesAndGenesWithMissingMetabolicGenes(extraRegRules, extraGenes);
		
	}
	
	
	
	private void extendRulesAndGenesWithMissingMetabolicGenes(IndexedHashMap<String, RegulatoryRule> extraRegRules, IndexedHashMap<String, Gene> extraGenes){
		
		int newrulessize=geneRulesIntegratedRegNetwork.size()+extraRegRules.size();
		int newgenesize=genesIntegratedRegNetwork.size()+extraGenes.size();
		
		IndexedHashMap<String, RegulatoryRule> newRulesMap=new IndexedHashMap<>(newrulessize);
		IndexedHashMap<String, Gene> newGeneMap= new IndexedHashMap<>(newgenesize);
		int n=0;
		for (int i = 0; i < geneRulesIntegratedRegNetwork.size(); i++) {
			newRulesMap.putAt(i, geneRulesIntegratedRegNetwork.getKeyAt(i), geneRulesIntegratedRegNetwork.getValueAt(i));
			newGeneMap.putAt(i, genesIntegratedRegNetwork.getKeyAt(i), genesIntegratedRegNetwork.getValueAt(i));
			n++;
		}
		

		int dif = extraRegRules.size();
		
		int pos=0;
		for (int j = n; pos < dif; j++) {
			newRulesMap.putAt(j, extraRegRules.getKeyAt(pos), extraRegRules.getValueAt(pos));
			newGeneMap.putAt(j, extraGenes.getKeyAt(pos), extraGenes.getValueAt(pos));
			pos++;
		}

		
		this.geneRulesIntegratedRegNetwork=newRulesMap;
		this.genesIntegratedRegNetwork=newGeneMap;

	}
	
	
	
	
	
    public IRegulatoryModel clone() {
    	
    	
    	
    	IndexedHashMap<String, Gene> genesetclone= new IndexedHashMap<String, Gene>();
    	IndexedHashMap<String, RegulatoryRule> regulatoryRulesclone = new IndexedHashMap<String, RegulatoryRule>();
    	IndexedHashMap<String, RegulatoryVariable> regulatoryvariablesclone = new IndexedHashMap<String, RegulatoryVariable>();
    	IndexedHashMap<String, String> RegulatoryVariablesTypeclone = new IndexedHashMap<String, String>();
        IndexedHashMap<String, String> mapOfRegGenesToASTgeneIDclone = new IndexedHashMap<String, String>();
        IndexedHashMap<String, Gene> genesIntegratedRegNetworkclone = new IndexedHashMap<String, Gene>();
        IndexedHashMap<String, RegulatoryRule> geneRulesIntegratedRegNetworkclone = new IndexedHashMap<String, RegulatoryRule>();
        ArrayList<String> missingmetabolicgenesclone=null;
        
    	try {
    		
    	for (int i = 0; i < genesRegNetwork.size(); i++) {
    		Gene oldGene = genesRegNetwork.getValueAt(i);
    		Gene geneclone= new Gene(oldGene.getId(), oldGene.getName());
    		genesetclone.put(genesRegNetwork.getKeyAt(i), geneclone);	
		}
    	
        for (int i = 0; i < genesIntegratedRegNetwork.size(); i++) {
        	Gene geneclone= new Gene(genesIntegratedRegNetwork.getValueAt(i).getId(),genesIntegratedRegNetwork.getValueAt(i).getName());
        	genesIntegratedRegNetworkclone.put(genesIntegratedRegNetwork.getKeyAt(i), geneclone);
		}
    	
    	
    	for (int i = 0; i < geneRulesRegNetwork.size(); i++) {
			
    		regulatoryRulesclone.put(geneRulesRegNetwork.getKeyAt(i), geneRulesRegNetwork.getValueAt(i).copy());
		}
    	
    	for (int i = 0; i < geneRulesIntegratedRegNetwork.size(); i++) {
    		geneRulesIntegratedRegNetworkclone.put(geneRulesIntegratedRegNetwork.getKeyAt(i), geneRulesIntegratedRegNetwork.getValueAt(i).copy());
		}
    	

    	for (int i = 0; i < this.variablesRegNetwork.size(); i++) {
				RegulatoryVariable varclone = (RegulatoryVariable) this.variablesRegNetwork.getValueAt(i).clone();
				regulatoryvariablesclone.put(this.variablesRegNetwork.getKeyAt(i), varclone);
		}
    	
    	for (int i = 0; i < RegVariableType.size(); i++) {
			
    		RegulatoryVariablesTypeclone.put(new String(RegVariableType.getKeyAt(i)), new String(RegVariableType.getValueAt(i)));
		}
    	
    	for (int i = 0; i < mapOfRegGenesToASTRuleID.size(); i++) {
			
    		mapOfRegGenesToASTgeneIDclone.put(new String(mapOfRegGenesToASTRuleID.getKeyAt(i)), new String(mapOfRegGenesToASTRuleID.getValueAt(i)));
    		}
    	
    	if(this.missingMetabolicGenes!=null)
    		missingmetabolicgenesclone=(ArrayList<String>) this.missingMetabolicGenes.clone();

    	} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return new BooleanRegulatoryNetworkModel(regulatoryRulesclone,geneRulesIntegratedRegNetworkclone, genesetclone,genesIntegratedRegNetworkclone, regulatoryvariablesclone, RegulatoryVariablesTypeclone, mapOfRegGenesToASTgeneIDclone, missingmetabolicgenesclone, this.useintegratedSimul);
    	
    }

	



	

}
