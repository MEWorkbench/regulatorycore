/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 * Author: Orlando Rocha
 */
package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryGeneCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.RegulatoryRuleCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.UnsuportedVariableException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces.IContainerRegulatoryBuilder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar.RegModelInfoContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar.RegModelTempinformationContainer;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public abstract class AbstractRegulatoryNetworkModelReader implements IContainerRegulatoryBuilder{
	
	/**
	 * 
	 */

	protected String modelName;
	protected String organismName;
	protected String notes;
	protected int version;
	protected IndexedHashMap<String,RegulatoryGeneCI> regulatoryGeneSet;
	protected IndexedHashMap<String,RegulatoryRuleCI> regulatoryGeneRules;
	protected IndexedHashMap<String,VariableCI> regulatoryVariables;
	
	protected File regulatoryFile;
	protected String delimiter = ";";
	protected ArrayList<String> identifiedConditionsByUser=null;
	protected ArrayList<String> identifiedTFsByUser=null;
	protected HashMap<String, String> mapidentifiedTFNameWithbnumberID;
	protected HashSet<String> matchsbetweenUserIdentTFssandmodelTFs=null;
	protected HashSet<String> matchsbetweenUserIdentCondandmodelCond=null;
	protected HashSet<String> remainingunknownVars=null;
	protected File auxiliarinformation = null;
	protected ArrayList<String> modelLines=null;
	protected RegModelInfoContainer infocontainer=null;
	
	abstract public String getReaderName();
	
	
	public void setRegulatoryFile(File regulatoryFile) throws Exception {
		
		if (regulatoryFile != null && !regulatoryFile.exists()) throw new Exception("The file "+regulatoryFile.getAbsolutePath()+" does not exist!"); 
		else {
		this.regulatoryFile = regulatoryFile;
		readModelFile(regulatoryFile);
		}
		
	}
	
	public void setAuxiliarFile(File auxiliarFile) throws Exception {
		this.auxiliarinformation= auxiliarFile;
		
	}
	
	
	public HashSet<String> getRemainingunknownVars() {
		return remainingunknownVars;
	}
	
	protected void readModelFile(File regulatoryfile) throws IOException{
		this.modelLines = (ArrayList<String>) FileUtils.readLines(regulatoryfile, "utf-8");
	}

	
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
	
	
    protected void setRegulatoryRulesandGeneSet(RegModelTempinformationContainer tempinformation) throws Exception{
		
		regulatoryGeneRules = new IndexedHashMap<String, RegulatoryRuleCI>();
	    regulatoryGeneSet = new IndexedHashMap<String, RegulatoryGeneCI>();
		

		
		IndexedHashMap<String, String> mapruletogeneid = tempinformation.getmapGeneIDwithGeneRule();
		IndexedHashMap<String, String> geneidMapToTFid = tempinformation.getGeneidMapToTFid();
		IndexedHashMap<String, String> geneidMapToGeneName = tempinformation.getGeneidMapToGeneName();
		
		HashSet<String> mappedTFalreadyinregulatorygeneRulesSet= new HashSet<String>();
         
		for (int i = 0; i < mapruletogeneid.size(); i++) {
		
			
			String bindTFid=geneidMapToTFid.get(mapruletogeneid.getKeyAt(i));
			regulatoryGeneRules.putAt(i, mapruletogeneid.getKeyAt(i), new RegulatoryRuleCI(bindTFid, mapruletogeneid.getValueAt(i)));
			if (mappedTFalreadyinregulatorygeneRulesSet.contains(bindTFid))
				throw new Exception(mapruletogeneid.getKeyAt(i)+" have a duplicated link to TFs that bind to diferent Regulatory rules");
			else
				mappedTFalreadyinregulatorygeneRulesSet.add(bindTFid);
			
			if(geneidMapToGeneName!=null){
				String genename = geneidMapToGeneName.get(mapruletogeneid.getKeyAt(i));
				regulatoryGeneSet.putAt(i, mapruletogeneid.getKeyAt(i), new RegulatoryGeneCI(mapruletogeneid.getKeyAt(i), genename));
			}
			 else
				regulatoryGeneSet.putAt(i, mapruletogeneid.getKeyAt(i), new RegulatoryGeneCI(mapruletogeneid.getKeyAt(i), mapruletogeneid.getKeyAt(i)));
          }
	}
    
    
  protected void FilterandmaptoVarsidentifiedByUser(HashSet<String> variablestofilter){
		
		HashSet<String> filteringStep1=null;
		HashSet<String> filteringStep2= null;

	     if(this.auxiliarinformation!=null){
		    
          if(identifiedConditionsByUser!=null){
       	   
       	    
		        for (String var : variablestofilter) {
					    	
                    if(!identifiedConditionsByUser.contains(var)){
                    	if(filteringStep1==null)
                    		filteringStep1= new HashSet<String>();
               	      filteringStep1.add(var);
               	      
                     }
                    else if(identifiedConditionsByUser.contains(var)){
                    	if(matchsbetweenUserIdentCondandmodelCond==null)
                    		matchsbetweenUserIdentCondandmodelCond=new HashSet<String>();
               	      matchsbetweenUserIdentCondandmodelCond.add(var);
               	     
                   }
		         }
          }
		    
		    if(filteringStep1==null)
		    	filteringStep1=variablestofilter;
		    
		    HashSet<String> toremove=null;
		    
		    if(identifiedTFsByUser!=null){
		    	
		    	toremove = new HashSet<String>();
		    	
		        for (String varr : filteringStep1) {

		    	    if(!identifiedTFsByUser.contains(varr)){
		    	    	if(filteringStep2==null)
                    		filteringStep2= new HashSet<String>();
               	       filteringStep2.add(varr);
               	       toremove.add(varr);
               	     
                    }
		    		
		    	    else if(identifiedTFsByUser.contains(varr)){
		    	    	if(matchsbetweenUserIdentTFssandmodelTFs==null)
		    	    		matchsbetweenUserIdentTFssandmodelTFs=new HashSet<String>();
		    	    	matchsbetweenUserIdentTFssandmodelTFs.add(varr);
		    	    	toremove.add(varr);
		    	    	
		    	    }     
		    	}
			}
		    
		    if(filteringStep2==null)
		    	filteringStep2=filteringStep1;
		    if(matchsbetweenUserIdentTFssandmodelTFs!=null)
		    	filteringStep2.removeAll(matchsbetweenUserIdentTFssandmodelTFs);
		    
		   
	  
	     }
	     else{
	    	 filteringStep2=variablestofilter;
	     }
	    	 
	    
	     
	     if(filteringStep2 != null && filteringStep2.size()>0){
	    	 if(remainingunknownVars==null){
	    		 remainingunknownVars=new HashSet<String>();	
	    	  }
	    	 
	    	 remainingunknownVars.addAll(filteringStep2);
	       }
			
	}
  
  
	protected void processKnownConditions(RegModelTempinformationContainer tempinformation) throws UnsuportedVariableException{
		
		if(regulatoryVariables==null)
		regulatoryVariables = new IndexedHashMap<String, VariableCI>();
		
		if(tempinformation.getListofdetectedConditions()!=null){
			
			for (String cond : tempinformation.getListofdetectedConditions()) {
				VariableCI var = new VariableCI(cond, cond);
				var.setTypeVar(VariableCI.CONDITION);
				regulatoryVariables.put(cond, var);
			}
		}
		
		if(matchsbetweenUserIdentCondandmodelCond!=null){
			
			for (String identifiedCond : matchsbetweenUserIdentCondandmodelCond) {
				VariableCI var = new VariableCI(identifiedCond, identifiedCond);
				var.setTypeVar(VariableCI.CONDITION);
				var.setIsUserCondition(true);
				regulatoryVariables.put(identifiedCond, var);
			}
	
		}	
	}
	
	
	protected void processkownTFs(RegModelTempinformationContainer tempinformation) throws UnsuportedVariableException{
		
		if(regulatoryVariables==null)
			regulatoryVariables = new IndexedHashMap<String, VariableCI>();
		

		ArrayList<String> TFslinkedtoGenes = tempinformation.getTransFactorsNamesAssociGenes();
		
		if(identifiedTFsByUser!=null){
			
			for (String identifiedTF : identifiedTFsByUser) {
				if(!TFslinkedtoGenes.contains(identifiedTF)){
				VariableCI tf = new VariableCI(identifiedTF, identifiedTF);
				tf.setTypeVar(VariableCI.TF);
				tf.setIsUserCondition(true);
				regulatoryVariables.put(identifiedTF, tf);
				}
			}
		}
	}
	
	
	
	public void setVariablesAsTFs(HashSet<String> tfs) throws UnsuportedVariableException{
		
		for (String identifiedTF : tfs) {
			VariableCI tf = new VariableCI(identifiedTF, identifiedTF);
			tf.setTypeVar(VariableCI.TF);
			tf.setIsUserCondition(true);
			regulatoryVariables.put(identifiedTF, tf);
		}
		
	}
	

	public void setVariablesAsConditions(HashSet<String> conds) throws UnsuportedVariableException{
		
		for (String identifiedcond : conds) {
			VariableCI cond = new VariableCI(identifiedcond, identifiedcond);
			cond.setTypeVar(VariableCI.CONDITION);
			cond.setIsUserCondition(true);
			regulatoryVariables.put(identifiedcond, cond);
		}
	}
	
	
	
	
    protected void readAuxiliarInformation(File auxfile) throws IOException{
		
		FileReader file = new FileReader(auxfile);
		BufferedReader reader = new BufferedReader(file);
		
		
		boolean conditionvar = false;
		boolean TFvar = false;
		
		String line = reader.readLine();

		
		while(line != null){
			if(line.contains("#CONDITIONS#")){
				conditionvar=true;
				TFvar=false;
			}
			else if(line.contains("#TFs#")){
				conditionvar=false;
				TFvar=true;
			}
			
			if(conditionvar){
				if(identifiedConditionsByUser == null)
					identifiedConditionsByUser=new ArrayList<String>();
				if(!line.matches("#CONDITIONS#") && !line.isEmpty())
				   identifiedConditionsByUser.add(line);
			}
			
			if(TFvar){
				if(identifiedTFsByUser == null)
					identifiedTFsByUser = new ArrayList<String>();
				if(!line.matches("#TFs#") && !line.isEmpty()){
					 
					if(line.contains(delimiter)){
						   if(mapidentifiedTFNameWithbnumberID == null)
							   mapidentifiedTFNameWithbnumberID=new HashMap<String, String>();
						   String[] splitline = getTFNameVsID(line);
						   identifiedTFsByUser.add(splitline[1]);
					       mapidentifiedTFNameWithbnumberID.put(splitline[0], splitline[1]);
					}
					else{
					
				       identifiedTFsByUser.add(line);
					}
				}
			}

			
			line = reader.readLine();
		}
		reader.close();
		file.close();

	}
	
    private String[] getTFNameVsID(String line){
	   
	      String [] res = new String[2];
	      String [] dataline =null;
	      line=line.replaceAll("\\s+","");
          dataline = line.split(delimiter);
	   
         if(dataline[0].matches("\\w\\d{4}")){
 	       res[0]=dataline[0];
 	       res[1]=dataline[1];
    }
      else{
 	      res[0]=dataline[1];
 	      res[1]=dataline[0];
    }
    
       return res;
   }
	

	public String getModelName() {
		return modelName;
	}

	
	public String getOrganismName() {
		return organismName;
	}
	

	public String getNotes() {
		return notes;
	}
	

	public Integer getVersion() {
		return version;
	}
	

	public IndexedHashMap<String, RegulatoryGeneCI> getRegulatoryGenes() {
		return regulatoryGeneSet;
	}
	
	public IndexedHashMap<String, VariableCI> getRegulatoryVariables(){
		return regulatoryVariables;
	}
	
	
	public IndexedHashMap<String, RegulatoryRuleCI> getRegulatoryGeneRules() {
		return regulatoryGeneRules;
	}
	
	
	public void setDelimiter(String del){
		this.delimiter = del;
	}

	

}
