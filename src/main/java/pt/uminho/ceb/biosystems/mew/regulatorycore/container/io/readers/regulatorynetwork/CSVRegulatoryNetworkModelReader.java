package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork;

import java.io.File;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar.RegModelInfoContainer;

public class CSVRegulatoryNetworkModelReader extends AbstractRegulatoryNetworkModelReader{

	

    static final String READERNAME= "Regulatory Network CSV Reader"; 

	 
	public CSVRegulatoryNetworkModelReader(){

	}
	
	 
	 public CSVRegulatoryNetworkModelReader (File regulatoryFile,  String auxiliarInformationpath, RegModelInfoContainer infocontainer) throws Exception{
			
	  		if (regulatoryFile != null && !regulatoryFile.exists()) 
	  			throw new Exception("The file "+regulatoryFile.getAbsolutePath()+" does not exist!"); 
	  		else {
	  		this.regulatoryFile = regulatoryFile;
	  		readModelFile(regulatoryFile);
	  		
	  		if(auxiliarInformationpath!=null)
	  		  this.auxiliarinformation = new File(auxiliarInformationpath);
	  		
	  		this.infocontainer=infocontainer;
	  		this.delimiter = infocontainer.getDelimiter();
	  		readRegulatoryRules();
	  		}
	  	}

	 
	@Override
	public String getReaderName() {
		// TODO Auto-generated method stub
		return READERNAME;
	}



}
