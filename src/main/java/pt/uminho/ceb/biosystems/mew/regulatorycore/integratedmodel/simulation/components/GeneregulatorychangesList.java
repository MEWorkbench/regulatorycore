package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components;

import java.util.List;

import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.gpr.ISteadyStateGeneReactionModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneChangesList;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;

public class GeneregulatorychangesList extends GeneChangesList{
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public GeneregulatorychangesList (){
    	super();
    }
    
    public GeneregulatorychangesList (List<String> regulatorygenes, List<Double> expression){
    	super(regulatorygenes,expression);
    }
    
    public GeneregulatorychangesList (List<String> regulatorygenes){
    	super(regulatorygenes);
    }
    
    
    public GeneregulatorychangesList clone(){
		return (GeneregulatorychangesList)(super.clone());
	}
   
    
    @Override
    public void addGene(int geneIndex,double geneRate, ISteadyStateGeneReactionModel model){
		String geneID = ((IIntegratedStedystateModel)model).getGene(geneIndex).getId();
		addGene(geneID, geneRate);
	}


    
    
}