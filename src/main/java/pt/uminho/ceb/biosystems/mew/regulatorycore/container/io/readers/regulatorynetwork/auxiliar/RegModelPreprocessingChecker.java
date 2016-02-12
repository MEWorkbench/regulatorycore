package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class RegModelPreprocessingChecker {
	
	protected String filepath;
	protected boolean useexcel=false;
	protected int NSAMPLE=15;
	
	protected int filenumbercolumns=0;
	
	public RegModelPreprocessingChecker(String filepath){
		this.filepath=filepath;
		this.useexcel=RegModelAuxiliarStaticMethods.usingexcelfile(filepath);
	}
	
	
	public int getFileNumberOfColumns(){
		
		if(!useexcel){
			ArrayList<String> lines=null;
			try {
				lines = (ArrayList<String>) FileUtils.readLines(new File(this.filepath), "utf-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(lines.size()<NSAMPLE)
				NSAMPLE=lines.size();
			String bestmatchdelimiter = RegModelAuxiliarStaticMethods.checkBestPossibleDelimiter(lines, NSAMPLE);
			return RegModelAuxiliarStaticMethods.getMaxNumberColumnsCSVFile(lines, bestmatchdelimiter);
		}
		else
			return new AuxiliarRegModelReaderExcel(this.filepath).getNumberColumnsFile();
	 
	}
	
	public boolean usingExcelFile(){
		return this.useexcel;
	}
	



	

}
