package pt.uminho.ceb.biosystems.mew.regulatorycore.container.components;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;

public class RegulatoryGeneCI extends GeneCI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegulatoryGeneCI(GeneCI geneCI) {
		super(geneCI);
	}
	
	public RegulatoryGeneCI(String geneId, String geneName) {
		super(geneId,geneName);
	}

}
