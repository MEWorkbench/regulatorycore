package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;

public class RegModelAuxiliarStaticMethods {
	
	
	
	public static boolean usingexcelfile(String filepath){
		String fileext = FilenameUtils.getExtension(filepath);
		if(fileext.toLowerCase().equals("xls") || fileext.toLowerCase().equals("xlsx"))
		   return true;
		else
			return false;
	}
	
	
	
	public static String autodetectDelimiter(String line){
		int nmaxWords=0;
		String possibledelimiter=null;
		String[] delimiters = {",",";","=","\\t",":","@"};
		for (int i = 0; i < delimiters.length; i++) {
			String[] nwords = line.split(delimiters[i]);
			
			if(nwords.length>nmaxWords){
				nmaxWords=nwords.length;
				possibledelimiter=delimiters[i];
			}
		}
	   return possibledelimiter;
	}
	
	public static String checkBestPossibleDelimiter(ArrayList<String> lines, int nrows){
		
		HashMap<String, Integer> deltimes=new HashMap<>();
		String bestmatch=null;
		
		for (int i = 0; i < nrows ; i++) {
			String reschar = autodetectDelimiter(lines.get(i));
			if(deltimes.containsKey(reschar))
				deltimes.put(reschar, deltimes.get(reschar)+1);
			else
				deltimes.put(reschar, 1);
		}
		
		int maxValueInMap=(Collections.max(deltimes.values()));  
        for (Entry<String, Integer> entry : deltimes.entrySet()) {  
            if (entry.getValue()==maxValueInMap) {
                bestmatch=entry.getKey(); 
            }
        }
        return bestmatch;
	}
	
	public static int getMaxNumberColumnsCSVFile(ArrayList<String> lines, String delimiter){
		
		int nmaxCol=0;
		
		for (String line : lines) {

			String[] datacol = line.split(delimiter);
			if(datacol.length>nmaxCol)
				nmaxCol=datacol.length;
		}
		
		return nmaxCol;
	}
	
	
	


}
