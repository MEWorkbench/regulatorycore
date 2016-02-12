package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.integratedmodel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.ErrorsException;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLReader;
import pt.uminho.ceb.biosystems.mew.biocomponents.validation.io.JSBMLValidationException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.RegulatoryContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryGeneCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryRuleCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces.IContainerIntegratedMRBuilder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces.IContainerRegulatoryBuilder;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class SBMLIntegratedModelReader extends JSBMLReader implements IContainerIntegratedMRBuilder{
    

	private static final long serialVersionUID = 1L;
	protected IndexedHashMap<String,RegulatoryGeneCI> regulatoryGeneSet;
    protected IndexedHashMap<String,RegulatoryRuleCI> regulatoryGeneRules;
	protected IndexedHashMap<String,VariableCI> regulatoryVariables;
	protected IndexedHashMap<String, String> mapofGeneIDtoASTgeneID;
	protected RegulatoryContainer regcontainer= null;
	
	public SBMLIntegratedModelReader(String filePath, String organismName,IContainerRegulatoryBuilder regreader) throws XMLStreamException,ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException {
		super(filePath, organismName);
		populateRegulatoryInformation(regreader);
	}
	
	public SBMLIntegratedModelReader(String filePath, String organismName, boolean checkConsistency,IContainerRegulatoryBuilder regreader) throws FileNotFoundException, XMLStreamException, ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException{
		super(new FileInputStream(filePath), organismName, checkConsistency, 0.0, 10000.0);
		populateRegulatoryInformation(regreader);
	}
	
	public SBMLIntegratedModelReader(String filePath, String organismName, Double defLb, Double defUb,IContainerRegulatoryBuilder regreader) throws XMLStreamException, ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException {
		super(new FileInputStream(filePath), organismName, true, defLb, defUb);
		populateRegulatoryInformation(regreader);
	}
	
	public SBMLIntegratedModelReader(String filePath, String organismName, boolean checkConsistency, Double defLb, Double defUb,IContainerRegulatoryBuilder regreader) throws XMLStreamException, ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException {
		super(new FileInputStream(filePath), organismName, checkConsistency, defLb, defUb);
		populateRegulatoryInformation(regreader);
	}
	
	public SBMLIntegratedModelReader(InputStream data, String organismName, boolean checkConsistency,IContainerRegulatoryBuilder regreader) throws XMLStreamException, ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException {
		super(data, organismName, checkConsistency, 0.0, 10000.0);
		populateRegulatoryInformation(regreader);
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
