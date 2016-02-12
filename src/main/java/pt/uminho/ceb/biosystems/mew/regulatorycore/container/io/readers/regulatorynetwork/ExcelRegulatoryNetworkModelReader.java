package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork;

import java.io.File;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar.AuxiliarRegModelReaderExcel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar.RegModelInfoContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar.RegModelTempinformationContainer;

public class ExcelRegulatoryNetworkModelReader extends AbstractRegulatoryNetworkModelReader{

	
	
	static final String READERNAME= "Regulatory Network Excel Reader"; 

	
	public ExcelRegulatoryNetworkModelReader(){
		
	}
	
	
	public ExcelRegulatoryNetworkModelReader(String filepath){
		try {
		this.modelLines= new AuxiliarRegModelReaderExcel(filepath).getLines();
		readRegulatoryRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ExcelRegulatoryNetworkModelReader(String filepath, String auxiliarInformationpath, RegModelInfoContainer infocontainer){
		try {
		this.modelLines= new AuxiliarRegModelReaderExcel(filepath).getLines();
		if(auxiliarInformationpath!=null)
	  		  this.auxiliarinformation = new File(auxiliarInformationpath);
		this.infocontainer=infocontainer;
		readRegulatoryRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
    public void setRegulatoryFile(File regulatoryFile) throws Exception {
		
		if (regulatoryFile != null && !regulatoryFile.exists()) throw new Exception("The file "+regulatoryFile.getAbsolutePath()+" does not exist!"); 
		else {
		this.regulatoryFile = regulatoryFile;
		this.modelLines= new AuxiliarRegModelReaderExcel(this.regulatoryFile).getLines();
		}
		
	} 
	
	
	@Override
	public void readRegulatoryRules() throws Exception {
		
	    
        if(auxiliarinformation!=null){
	    	
	    	readAuxiliarInformation(this.auxiliarinformation);
	    }
		
		RegModelTempinformationContainer tempinformation = new RegModelTempinformationContainer(this.modelLines, this.infocontainer);
		
		setRegulatoryRulesandGeneSet(tempinformation);
		
		if(tempinformation.haveUnknownVarsTypeInRules())
			FilterandmaptoVarsidentifiedByUser(tempinformation.getUnkownvariablestype());
		
		
		processKnownConditions(tempinformation);
		processkownTFs(tempinformation);
	
	 }

	
	@Override
	public String getReaderName() {
		return READERNAME;
	}



	

}
