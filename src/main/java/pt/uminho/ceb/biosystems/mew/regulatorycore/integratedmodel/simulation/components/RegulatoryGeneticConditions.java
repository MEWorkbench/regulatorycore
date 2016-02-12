package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components;

import java.io.Serializable;
import java.util.ArrayList;

import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.gpr.ISteadyStateGeneReactionModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneChangesList;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;

public class RegulatoryGeneticConditions extends GeneticConditions implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    protected GeneregulatorychangesList regulatorygeneList;
    protected IIntegratedStedystateModel model;

   
    
    public RegulatoryGeneticConditions(GeneregulatorychangesList reggeneList, GeneChangesList metgeneList, ISteadyStateGeneReactionModel model, boolean isOverUnder) throws Exception {
		super(metgeneList, model, isOverUnder);
		this.regulatorygeneList = reggeneList;
		this.model = (IIntegratedStedystateModel) model;
		
		// TODO Auto-generated constructor stub
	}
    
    
	
   public GeneregulatorychangesList getRegulatoryGenechangeList(){
	   return this.regulatorygeneList;
   }
	
	
	public void setGeneLists(GeneregulatorychangesList regulatoryGeneList, GeneChangesList geneList, ISteadyStateGeneReactionModel model) throws Exception
	{
		this.regulatorygeneList = regulatoryGeneList;
		this.geneList = geneList;
	    this.reactionList = geneList.getReactionUnderOverList(model);
	}
	
	
	public void setGeneLists(GeneregulatorychangesList regulatoryGeneList, GeneChangesList geneList){
		this.regulatorygeneList = regulatoryGeneList;
		this.geneList = geneList;
	}
	
	
	public boolean equals(RegulatoryGeneticConditions conditions){
		
		if(this.isOverUnder!=conditions.isOverUnder())
			return false;
		
		if(this.reactionList!=null && (!this.reactionList.equals(conditions.getReactionList())))
			return false;
		
		if(this.geneList!=null && (!this.geneList.equals(conditions.getGeneList())))
			return false;
		if(this.regulatorygeneList!=null && (!this.regulatorygeneList.equals(conditions.getRegulatoryGenechangeList())))
		    return false;
		
		return true;
	}
	
	
	 public ArrayList<String> getALLGeneKnockoutList(){
		  ArrayList<String> res = new ArrayList<String>();
		  // add metabolic gene
		  for (String genemet : geneList.getGeneIds()) {
			if(!res.contains(genemet)){ 
			 res.add(genemet);
			 }
		  }
		  for (String genereg: regulatorygeneList.getGeneIds()){
              if(!res.contains(genereg))
			  res.add(genereg);
		  }
		 return res;
		 
	 }
	 
	 
	 public ArrayList<String> getMetabolicKnockoutList(){
		 ArrayList<String> res = new ArrayList<String>();

		  for (String genemet : geneList.getGeneIds()) { 
			 res.add(genemet);
		  }
		  return res;
	 }
	 
	 
	 public ArrayList<String> getRegulatoryKnockoutList(){
		 ArrayList<String> res = new ArrayList<String>();

		  for (String genereg : regulatorygeneList.getGeneIds()) {
			 res.add(genereg);
		  }
		  return res;
	 }
	 
	 
	 public ArrayList<String> getAllContainedGenes(){
			
			ArrayList<String> res = new ArrayList<String>();
			
			res.addAll(geneList.getGeneKnockoutList());
			res.addAll(regulatorygeneList.getGeneKnockoutList());
			
			
			return res;
			
		}
	 
	 public void removeGene(String geneid) throws Exception{
		 
		 if(geneList.containsGene(geneid))
			 geneList.removeGene(geneid);
		 else if(regulatorygeneList.containsGene(geneid))
			 regulatorygeneList.removeGene(geneid);
		 else
			 throw new Exception();
 
	 }
	 
	 public void addGene(String geneid){
		 
		 if(model.isMetabolicGene(geneid))
			 this.geneList.addGeneKnockout(geneid);
		 else
			 this.regulatorygeneList.addGeneKnockout(geneid);
		 
	 }
	 
	 
	 public GeneticConditions clone(){
		 
		 RegulatoryGeneticConditions copy = null;
		try {
			copy = new RegulatoryGeneticConditions(this.regulatorygeneList, this.geneList,this.model , this.isOverUnder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return copy;
	 }
	 
	 
	 
}

