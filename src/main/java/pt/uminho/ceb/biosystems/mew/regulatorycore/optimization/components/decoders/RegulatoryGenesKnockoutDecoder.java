package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.decoders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.jecoli.algorithm.components.representation.IRepresentation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.representation.set.SetRepresentation;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneChangesList;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.strainoptimizationalgorithms.jecoli.components.decoder.GKDecoder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.GeneregulatorychangesList;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;

public class RegulatoryGenesKnockoutDecoder extends GKDecoder{
    
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> regulatorygenes = null;

	public RegulatoryGenesKnockoutDecoder(IIntegratedStedystateModel model) {
		super(model);

		
	}

	
	public RegulatoryGenesKnockoutDecoder(IIntegratedStedystateModel model,List<String> notAllowedGeneKnockouts){
		super(model);

		try {
			if(notAllowedGeneKnockouts != null)
			createNotAllowedGenesFromIds(notAllowedGeneKnockouts);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public int getNumberVariables()
	{
		if (this.notAllowedKnockouts==null) 
			return ((IIntegratedStedystateModel)model).getRegulatoryNet().getNumberOfGenes();
		else 
			return ((IIntegratedStedystateModel)model).getRegulatoryNet().getNumberOfGenes() - notAllowedKnockouts.size();
	}
	
	

	
	
	@Override
	public int getInitialNumberVariables()
	{
		return ((IIntegratedStedystateModel)model).getRegulatoryNet().getNumberOfGenes();
	}
	

	
	
	
	
	@Override
	public void createNotAllowedGenesFromIds (List<String> notAllowedGeneIds) throws Exception
	{
		notAllowedKnockouts = new ArrayList<Integer>(notAllowedGeneIds.size());
		Iterator<String> it = notAllowedGeneIds.iterator();

		
		while(it.hasNext())
		{
			
			String nextId = it.next();
			notAllowedKnockouts.add(((IIntegratedStedystateModel)model).getRegulatoryNet().getRegulatoryGeneIndex(nextId));
		}
		
		createInternalDecodeTable();
	}
	


	
	@Override
	public RegulatoryGeneticConditions decode (IRepresentation solution) throws Exception
	{
		TreeSet<Integer> genome = ((SetRepresentation)solution).getGenome();
		
		List<Integer> geneKnockoutList = decodeGeneKnockouts(genome);
	
		ArrayList<String> metabGenes = new ArrayList<String>();
		ArrayList<String> regGenes = new ArrayList<String>();
		
		for (Integer gene : geneKnockoutList) {
			if (((IIntegratedStedystateModel)model).isMetabolicGene(((IIntegratedStedystateModel)model).getRegulatoryNet().getGene(gene).getId())){
			
					metabGenes.add(((IIntegratedStedystateModel)model).getRegulatoryNet().getGene(gene).getId());
					}
			else{
				
				regGenes.add(((IIntegratedStedystateModel)model).getRegulatoryNet().getGene(gene).getId());
			}
		}
		
		GeneChangesList metchlist = new GeneChangesList(metabGenes);
		GeneregulatorychangesList regchlist = new GeneregulatorychangesList(regGenes);

		
		return new RegulatoryGeneticConditions(regchlist, metchlist, (IIntegratedStedystateModel)model, false);
	}
	

	
}
