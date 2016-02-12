package pt.uminho.ceb.biosystems.mew.regulatorycore.converters;

import java.util.ArrayList;

import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.RegulatoryContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryGeneCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryRuleCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.UnsuportedVariableException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryRule;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components.RegulatoryVariable;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.BooleanRegulatoryNetworkModel;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.parser.ParseException;

public class ConvertToRegulatoryNetModel {
	
	protected RegulatoryContainer regcontainer;
	protected IndexedHashMap<String, Gene> genes;
	protected IndexedHashMap<String, RegulatoryRule> generule;
	protected IndexedHashMap<String, RegulatoryVariable> regulatoryvariables;
	protected IndexedHashMap<String, String> mappingOfGeneIDtoASTRuleID;
	protected ArrayList<String> regVariables;
	protected String modelid = "";
	
	public ConvertToRegulatoryNetModel(RegulatoryContainer container){
		this.regcontainer = container;
		try {
			getDataFromRegulatoryContainer();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsuportedVariableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void getDataFromRegulatoryContainer() throws ParseException, UnsuportedVariableException{
		
	
		genes = new IndexedHashMap<String, Gene>();
		for (RegulatoryGeneCI reggene: regcontainer.getRegulatoryGenes().values()) {
			genes.put(reggene.getGeneId(), new Gene(reggene.getGeneId(), reggene.getGeneName()));
		}
		
		generule = new IndexedHashMap<String, RegulatoryRule>();
		IndexedHashMap<String, RegulatoryRuleCI> regrulesci= regcontainer.getRegulatorygeneRules();
		for (int i = 0; i < regrulesci.size(); i++) {
			RegulatoryRule regrule = new RegulatoryRule(regrulesci.getValueAt(i).getRuleId(), regrulesci.getValueAt(i).getRule());
			generule.put(regrulesci.getKeyAt(i), regrule);
		}
		

		regulatoryvariables = new IndexedHashMap<String, RegulatoryVariable>();
		regVariables = new ArrayList<String>();
		for (VariableCI contregvar : regcontainer.getAllVariables().values()) {
			regVariables.add(contregvar.getId());
			RegulatoryVariable regvar = new RegulatoryVariable(contregvar.getId(), contregvar.getName());
			regvar.setTypeVar(contregvar.getType());
			regvar.setIsUserCondition(contregvar.isUserCondition());
			regulatoryvariables.put(contregvar.getId(), regvar);
		}
		
		
		
		mappingOfGeneIDtoASTRuleID = regcontainer.getMapofGeneIDtoASTRuleID();
		
		
		
		
		
		if (regcontainer.getModelName() !=null)
		this.modelid = regcontainer.getModelName();
	
		
	}
	
	
	
	
	public BooleanRegulatoryNetworkModel convertContainerToGeneNetworkModel(){
		
		BooleanRegulatoryNetworkModel regNetworkModel = new BooleanRegulatoryNetworkModel(modelid,generule, genes, regulatoryvariables, mappingOfGeneIDtoASTRuleID);
		
		return regNetworkModel;
	 }
		
	
	}
	
	
	
	


