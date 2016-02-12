package pt.uminho.ceb.biosystems.mew.regulatorycore.converters;

import pt.uminho.ceb.biosystems.mew.core.model.converters.ContainerConverter;
import pt.uminho.ceb.biosystems.mew.core.model.exceptions.InvalidSteadyStateModelException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.IntegratedMRContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.BooleanRegulatoryNetworkModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IntegratedSteadyStateModel;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.parser.ParseException;

public class ConvertToIntegratedModel extends ContainerConverter {

     
	
	
	public ConvertToIntegratedModel(IntegratedMRContainer container) {
		super(container);
	
	}
	
	
	
	public IRegulatoryModel convertRegulatoryNetworkmodel(){
		
		ConvertToRegulatoryNetModel regconverter = new ConvertToRegulatoryNetModel(((IntegratedMRContainer)container).getRegcontainer());
		
		BooleanRegulatoryNetworkModel regmodel = regconverter.convertContainerToGeneNetworkModel();
        
        try {
			regmodel.checkMetabolicGeneMissingRules(genes);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return regmodel;
	}
	
	
	
	public IntegratedSteadyStateModel convertToIntegratedModel() throws InvalidSteadyStateModelException{
		
		getSSGeneModelInfoFromContainer();
		getSSModelInfoFromContainer();
		
		IntegratedSteadyStateModel integratedmodel = new IntegratedSteadyStateModel(container.getModelName(), 
				smatrix, 
				reactions, 
				metabolites, 
				compartments, 
				pathways, 
				genes, 
				null, 
				geneReactionRules, 
				null, 
				convertRegulatoryNetworkmodel());
		
		integratedmodel.setBiomassFlux(container.getBiomassId());
	
		return integratedmodel;

	}
	

	
	
	
	
	
	

}
