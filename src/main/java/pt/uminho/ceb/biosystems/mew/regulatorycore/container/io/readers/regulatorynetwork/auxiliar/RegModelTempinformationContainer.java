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
package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class RegModelTempinformationContainer {
	
	private boolean debug =false;

	protected ArrayList<String> TFsLinkByIDs = new ArrayList<String>();
	protected ArrayList<String> geneNames = new ArrayList<String>();
	protected ArrayList<String> geneIDs = new ArrayList<String>();
	protected ArrayList<String> RegulatoryRulesToTreat = new ArrayList<String>();
	protected IndexedHashMap<String, String> mapGeneIDwithGeneRule = new IndexedHashMap<String, String>();
	protected IndexedHashMap<String, String> GeneidMapToTFid = new IndexedHashMap<String, String>();
	protected IndexedHashMap<String, String> GeneidMapToGeneName = null;
	

	protected String delimiter =";";
	protected HashSet<String> unknownBnumbersFound = null;
	protected HashSet<String> ListofdetectedConditions = null;
	protected HashSet<String> Listofallvariablesdefinedinrules =new HashSet<String>();
	protected HashSet<String> knownTFsandGenesInRules = new HashSet<String>();
	protected HashSet<String> Unkownvariablestype =null;
	protected RegModelInfoContainer modelinfocontainer=null;
	
	protected ArrayList<String> modelLines=null;
	
	public RegModelTempinformationContainer (){

	}

	
	public RegModelTempinformationContainer (ArrayList<String> regulatorymodellines, RegModelInfoContainer modelinfocontainer){
        this.modelLines=regulatorymodellines;
        this.modelinfocontainer=modelinfocontainer;
        if(modelinfocontainer.getDelimiter()!=null)
        	this.delimiter=modelinfocontainer.getDelimiter();
        try {
			getdefinedLabelsInRegModel();
			performcheckRulesOperations();
	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	

	public ArrayList<String> getTransFactorsNamesAssociGenes() {
		return TFsLinkByIDs;
	}
   


	public void setSingleTransFactorsNameAssocitoGene(String transFactorName) {
		if(this.TFsLinkByIDs==null){
			this.TFsLinkByIDs = new ArrayList<String>();
		}
		this.TFsLinkByIDs.add(transFactorName);
	}


	public ArrayList<String> getRegulatoryGeneIDs() {
		return geneIDs;
	}



	public void setRegulatoryGeneIDs(String regulatoryGeneID) {
		if(this.geneIDs==null){
			this.geneIDs = new ArrayList<String>();
		}
		this.geneIDs.add(regulatoryGeneID);
	}

	public HashSet<String> getUnknownBnumbersFound() {
		return unknownBnumbersFound;
	}
	
	public boolean haveUnknownBnumbersInRules(){
		if(this.unknownBnumbersFound !=null)
			return true;
		else
			return false;
	}
	
	public boolean haveUnknownVarsTypeInRules(){
		if(this.Unkownvariablestype !=null)
			return true;
		else
			return false;
	}
	
	

	public HashSet<String> getKnownTfsinRules() {
		return knownTFsandGenesInRules;
	}

	public HashSet<String> getUnkownvariablestype() {
		return Unkownvariablestype;
	}

	public HashSet<String> getListofdetectedConditions() {
		return ListofdetectedConditions;
	}
	
	public HashSet<String> getListofALLdetectedVariables() {
		return Listofallvariablesdefinedinrules;
	}
	
	public IndexedHashMap<String, String> getmapGeneIDwithGeneRule(){
		return this.mapGeneIDwithGeneRule;
	}
	
	public IndexedHashMap<String, String> getGeneidMapToTFid(){
		return this.GeneidMapToTFid;
	}
    
	public IndexedHashMap<String, String> getGeneidMapToGeneName(){
		return this.GeneidMapToGeneName;
	}
	

	
	protected void readModelFile(File regulatoryfile) throws IOException{
		this.modelLines = (ArrayList<String>) FileUtils.readLines(regulatoryfile, "utf-8");
	}
	
	
	protected void getdefinedLabelsInRegModel() throws IOException{
		
		int GPRlinkcolumn = this.modelinfocontainer.getGPRLinkColumn();
		int TFsLinkcolumn = this.modelinfocontainer.getTFsLinkColumn();
		int GeneNamescolumn = this.modelinfocontainer.getGeneNamesColumn();
		int totacolumns = this.modelinfocontainer.getTotalcolumns();

	    for (String line : modelLines) {
            
			String[] dataline = line.split(delimiter);
			
			this.geneIDs.add(dataline[GPRlinkcolumn]);
			
			if(GeneNamescolumn!=-1 && GeneNamescolumn>-1){
               this.geneNames.add(dataline[GeneNamescolumn]);
               if(GeneidMapToGeneName==null)
            	   GeneidMapToGeneName=new IndexedHashMap<>();
               this.GeneidMapToGeneName.put(dataline[GPRlinkcolumn], dataline[GeneNamescolumn]);	   
               
			}
			
			
			this.TFsLinkByIDs.add(dataline[TFsLinkcolumn]);
			
			this.GeneidMapToTFid.put(dataline[GPRlinkcolumn],dataline[TFsLinkcolumn] );
			
			
	
			
			
			if(dataline.length == totacolumns){
				this.RegulatoryRulesToTreat.add(dataline[totacolumns-1]);
				this.mapGeneIDwithGeneRule.put(dataline[GPRlinkcolumn], dataline[totacolumns-1]);
			}
			else{
				this.mapGeneIDwithGeneRule.put(dataline[GPRlinkcolumn], "");
			}

			if(debug){
				if(dataline.length == totacolumns){
					if(GeneNamescolumn!=-1)
				     System.out.print(dataline[GPRlinkcolumn]+"|"+dataline[GeneNamescolumn]+"|"+dataline[TFsLinkcolumn]+"|"+dataline[totacolumns-1]+"\n");
					else
						 System.out.print(dataline[GPRlinkcolumn]+"|"+"|"+dataline[TFsLinkcolumn]+"|"+dataline[totacolumns-1]+"\n");	
				}
				else
					if(GeneNamescolumn!=-1)
					   System.out.print(dataline[GPRlinkcolumn]+"|"+dataline[GeneNamescolumn]+"|"+dataline[TFsLinkcolumn]+"\n");	
					else
						System.out.print(dataline[GPRlinkcolumn]+"|"+"|"+dataline[TFsLinkcolumn]+"\n");
						
			}
		}

		
	}
	
	
	protected  void performcheckRulesOperations(){
		
		for (String rule : this.RegulatoryRulesToTreat) {
			
			findPossiblevariablesinrule(rule);
			findallvariablesdefinedinrule(rule);
		}
		checkUnknownVariablesType();
	}
	
	

	// Find only Conditions on regulatory model
	protected void findPossiblevariablesinrule(String rule){
		
        String pattern="(\\w+([-,_]\\w+)*(\\(\\w\\))*[<>]\\w+)";
   		
   		Pattern patt = Pattern.compile(pattern);
   		Matcher match = patt.matcher(rule);
		
   		if(ListofdetectedConditions ==null){
   			ListofdetectedConditions = new HashSet<String>();	
   		}
   		
   		while(match.find()){
   			ListofdetectedConditions.add(match.group());
   	
   		}
   		
	}
	
	
	// find all variables defined in regulatory model including TFs, Conditions
	protected void findallvariablesdefinedinrule(String rule){
		String pattern="(\\w+(([-,_]\\w+)*(\\(\\w\\))*[<>]\\w+)*)";
	
		Pattern patt = Pattern.compile(pattern);
		Matcher match = patt.matcher(rule);
		while(match.find()){
			String oc = match.group();

			if(!oc.matches("or|OR|and|AND|not|NOT")){
   			    this.Listofallvariablesdefinedinrules.add(oc);
   			    if(this.TFsLinkByIDs.contains(oc) || this.geneIDs.contains(oc))
   			    	this.knownTFsandGenesInRules.add(oc);
			}
   		}
		
	}
	
	protected void checkUnknownVariablesType(){
		
	   for (String var : this.Listofallvariablesdefinedinrules) {
		 if(!this.ListofdetectedConditions.contains(var) && !this.knownTFsandGenesInRules.contains(var)){
			 if(this.Unkownvariablestype ==null)
				 this.Unkownvariablestype =new HashSet<String>();
			 this.Unkownvariablestype.add(var);
			 
		 }
	  }
		
		
		
		
		
	}
	
	
	
	public void printrulestotreat(){
		
		for (String rule : this.RegulatoryRulesToTreat) {
			System.out.println(rule);
		}

	}
	

	
	public void printunknownbnumber(){
		if(this.unknownBnumbersFound!=null){
		for (String bn : this.unknownBnumbersFound) {
			System.out.println(bn);
		}
		}
	}
	
	public void printconditions(){
		if(this.ListofdetectedConditions!=null){
		for (String con : this.ListofdetectedConditions) {
			System.out.println(con);
		}
		}
	}
	
	public void printallvariables(){
		if(this.Listofallvariablesdefinedinrules!=null){
		for (String con : this.Listofallvariablesdefinedinrules) {
			System.out.println(con);
		}
		}
	}
	
	public void printTFs(){
		if(this.knownTFsandGenesInRules!=null){
		for (String con : this.knownTFsandGenesInRules) {
			System.out.println(con);
		}
		}
	}
	
	
	public void printunknownvartype(){
		if(this.Unkownvariablestype!=null){
		for (String con : this.Unkownvariablestype) {
			System.out.println(con);
		}
		}

	}
	

	
	public void saveListofConditionstoFile(String filepath){
		
		try {
    		File file = new File(filepath);
    		FileWriter f=new FileWriter(file +".txt");
			BufferedWriter bw=new BufferedWriter(f);
			
			// write conditions
			for (String var : ListofdetectedConditions) {
				bw.write(var+"\n");
			}
			
			
			bw.close();
			f.close();
	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	


}
