package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.integratedmodel;

import java.io.File;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.MatrixEnum;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.FlatFilesReader;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.RegulatoryContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryGeneCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryRuleCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces.IContainerIntegratedMRBuilder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces.IContainerRegulatoryBuilder;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class FlatFileIntegratedModelReader extends FlatFilesReader implements IContainerIntegratedMRBuilder{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected File regulatorymodel = null;
    protected IndexedHashMap<String,RegulatoryGeneCI> regulatoryGeneSet;
    protected IndexedHashMap<String,RegulatoryRuleCI> regulatoryGeneRules;
	protected IndexedHashMap<String,VariableCI> regulatoryVariables;
	protected IndexedHashMap<String, String> mapofGeneIDtoASTgeneID;
	protected RegulatoryContainer regcontainer= null;
	
	public FlatFileIntegratedModelReader(File reactionsFile, File matrixFile, File metabolitesFile, File genesFile, String modelID, MatrixEnum mt, String userReactionsDelimiter, String userMetabolitesDelimiter, String userMatrixDelimiter, IContainerRegulatoryBuilder reader) throws Exception{
		super(reactionsFile,matrixFile,metabolitesFile, genesFile, modelID, mt, userReactionsDelimiter, userMetabolitesDelimiter,userMatrixDelimiter );
		populateRegulatoryInformation(reader);
	}
    
	public FlatFileIntegratedModelReader(String reactionsFilePath, String matrixFilePath, String metabolitesFilePath, String genesFilePath, String modelID, MatrixEnum mt, String userReactionsDelimiter, String userMetabolitesDelimiter, String userMatrixDelimiter, IContainerRegulatoryBuilder reader) throws Exception{
		super(reactionsFilePath,matrixFilePath,metabolitesFilePath, genesFilePath, modelID, mt, userReactionsDelimiter, userMetabolitesDelimiter,userMatrixDelimiter );
		populateRegulatoryInformation(reader);
	}
	
	
	
	protected void populateRegulatoryInformation(IContainerRegulatoryBuilder reader ){
		regcontainer = new RegulatoryContainer(reader);
		regulatoryGeneSet = regcontainer.getRegulatoryGenes();
		regulatoryGeneRules = regcontainer.getRegulatorygeneRules();
		regulatoryVariables = regcontainer.getAllVariables();
	}
	

	@Override
	public IndexedHashMap<String, RegulatoryGeneCI> getRegulatoryGenes() {
		// TODO Auto-generated method stub
		return regulatoryGeneSet;
	}


	@Override
	public IndexedHashMap<String, VariableCI> getRegulatoryVariables() {
		// TODO Auto-generated method stub
		return regulatoryVariables;
	}


	@Override
	public IndexedHashMap<String, RegulatoryRuleCI> getRegulatoryGeneRules() {
		// TODO Auto-generated method stub
		return regulatoryGeneRules;
	}
	
	
	


	public IndexedHashMap<String, String> getMappingOfGeneIDtoASTgeneID() {
		return mapofGeneIDtoASTgeneID;
	}

	@Override
	public RegulatoryContainer getRegulatoryContainer() {
		// TODO Auto-generated method stub
		return regcontainer;
	}
	      
   

	
	
	
}
