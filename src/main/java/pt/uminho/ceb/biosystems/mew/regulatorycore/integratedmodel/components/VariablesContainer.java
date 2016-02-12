package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class VariablesContainer implements Cloneable, Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected IndexedHashMap<String, Integer> userConditionsVariablesMappings;
	protected IndexedHashMap<String, Integer> VariablesofNetworkPresentinMetabolicModelMapping;
	protected IndexedHashMap<String, Integer> ALLVariables;
	protected IndexedHashMap<String, Boolean> ALLVariablesState;
	protected IndexedHashMap<String, String> variablesTypeMap;
	protected IndexedHashMap<String, Integer> TFsVariablesMappings;
	protected HashMap<String, Integer> VariablestoreactionsMappings ;
	protected IRegulatoryModel regulatoryNet;
	
	private boolean debug = false;
	

	
	public VariablesContainer(IndexedHashMap<String, Integer> ALLVariables, IndexedHashMap<String, Integer> userConditionsVariablesMappings, IndexedHashMap<String, Integer> metabolicModelVariablesMappings,HashMap<String, Integer> reactionsToVariablesMappings, IndexedHashMap<String, String> variablesTypeMap, IndexedHashMap<String, Integer> TFsVariablesMappings ){
       
	   this.ALLVariables = ALLVariables;
       this.userConditionsVariablesMappings = userConditionsVariablesMappings;
       this.VariablesofNetworkPresentinMetabolicModelMapping = metabolicModelVariablesMappings;
       this.VariablestoreactionsMappings = reactionsToVariablesMappings;
       this.variablesTypeMap = variablesTypeMap;
       this.TFsVariablesMappings = TFsVariablesMappings;
       
      
       initVariablesState();
	}
	
	public void setRegulatoryModel(IRegulatoryModel regmodel){
		this.regulatoryNet = regmodel;
	}
	
	public IndexedHashMap<String, Boolean> getALLVariablesState(){
		return this.ALLVariablesState;
	}
     
	
	public void setALLVariablesState(
			IndexedHashMap<String, Boolean> aLLVariablesState) {
		ALLVariablesState = aLLVariablesState;
	}


	public Integer getNumberOfVariablesInRegNetAndModel(){
		return this.VariablesofNetworkPresentinMetabolicModelMapping.size();
	}
	
	public Integer getIndexReactionForVariable(String variable){
		int index = -1;
		
		if(VariablesofNetworkPresentinMetabolicModelMapping.containsKey(variable))
			index = VariablestoreactionsMappings.get(variable);
		
		return index;
	}
	
	public String getVariableForReactionIndex(int reactionIdx){
		String name ="";
		
		for (Map.Entry<String, Integer> set: VariablestoreactionsMappings.entrySet()) {
			if(set.getValue() ==reactionIdx)
				name = set.getKey();
		}
		
		return name;
	}

	public IndexedHashMap<String, Integer> getuserConditionsVariablesMappings() {
		return userConditionsVariablesMappings;
	}

	public IndexedHashMap<String, Integer> VariablesofNetworkPresentinMetabolicModelMapping() {
		return VariablesofNetworkPresentinMetabolicModelMapping;
	}

	public IndexedHashMap<String, Integer> getALLVariables() {
		return ALLVariables;
	}

	public HashMap<String, Integer> getVariablestoReactionsMappings() {
		return VariablestoreactionsMappings;
	}

	
	public Boolean isUserVarible(int idxVariable) {
		return userConditionsVariablesMappings.containsValue(idxVariable);
	}
	
	public Boolean isUserVarible(String Variable){
		return userConditionsVariablesMappings.containsKey(Variable);
	}
	
	public ArrayList<String> getUserVariablesList(){
		ArrayList<String> variablesID = new ArrayList<String>();
		Iterator<String> i = userConditionsVariablesMappings.keySet().iterator();
		while(i.hasNext()){
			variablesID.add(i.next());	
		}
		return variablesID;
	}
	
	public ArrayList<String> getALLVariablesList(){
		ArrayList<String> variablesID = new ArrayList<String>();
		Iterator<String> i = ALLVariables.keySet().iterator();
		while(i.hasNext()){
			variablesID.add(i.next());	
		}
		return variablesID;
	}
	
	public int getNumberOfUserVariables(){
		
		return userConditionsVariablesMappings.size();
	}
	

	
	public Boolean[] getOrderedBooleanVariablesstate(){
		Boolean [] res = new Boolean[ALLVariablesState.size()];
		
		int i=0;
		for (Boolean value : ALLVariablesState.values()){
		     res[i]= value;
			i++;
		}
		return res;
	}
	
	
	
	public boolean[] getOrderedUserVariablesState(){
		boolean [] res = new boolean[userConditionsVariablesMappings.size()];
		
		int n=0;
		for (int i = 0; i < ALLVariablesState.size(); i++) {
			if(userConditionsVariablesMappings.containsKey(ALLVariablesState.getKeyAt(i)))
				res[n]= ALLVariablesState.getValueAt(i);
			n++;
		}
		
		return res;
	}
	
	
	public ArrayList<String> getALLVariablesWithTrueState(){
		
		ArrayList<String> truevars = new ArrayList<String>();
		
		for (Map.Entry<String, Boolean> varstatemap : ALLVariablesState.entrySet()){

			if (varstatemap.getValue() == true)
				truevars.add(varstatemap.getKey());	
			
		}
		   return truevars;
	}
	
	public ArrayList<String> getAllConditionsVarsWithTrueState(){
		
		ArrayList<String> truevars = new ArrayList<String>();
		for (Map.Entry<String, Boolean> varstatemap : ALLVariablesState.entrySet()){
			if(varstatemap.getValue()== true && getVarType(varstatemap.getKey()).equals(VariableCI.CONDITION))
				truevars.add(varstatemap.getKey());
		}

		return truevars;
	}
	
	
	public ArrayList<String> getAllTFsVarsWithFalseState(){
		
		ArrayList<String> TfsFalse = null;
		
		for (Map.Entry<String, Boolean> varstatemap : ALLVariablesState.entrySet()){
			if(getVarType(varstatemap.getKey()).equals(VariableCI.TF) && varstatemap.getValue()==false){
				if(TfsFalse==null)
					TfsFalse=new ArrayList<String>();
				TfsFalse.add(varstatemap.getKey());
			}
		}
		
		return TfsFalse;	
	}
	
	
	public Integer getNumberLinkedVariables(){
		return VariablesofNetworkPresentinMetabolicModelMapping.size();
	}
	
	public Integer getLinkedVariavelIndex(String var){
		return VariablesofNetworkPresentinMetabolicModelMapping.getIndexOf(var);
	}
	
	
	public String getLinkedVariableId(int linkedVariableIndex){
		return VariablesofNetworkPresentinMetabolicModelMapping.getKeyAt(linkedVariableIndex);
	}
	
	public Integer getLinkedVariableIndex(int linkedVariableIndex){
		String varId = getLinkedVariableId(linkedVariableIndex);
		return getALLVariablesList().indexOf(varId);
	}
	

	public IndexedHashMap<String, Integer> getRegulatoryLinkedToMetabolicModelVariables() {
		return VariablesofNetworkPresentinMetabolicModelMapping;
	}
	
	

	  private void initVariablesState(){
			
			this.ALLVariablesState= new IndexedHashMap<String, Boolean>();
			int i=0;
			for (String key : ALLVariables.keySet()){
				if(TFsVariablesMappings.containsKey(key))
					ALLVariablesState.putAt(i,key, true);
				else
				  ALLVariablesState.putAt(i,key, false);
				i++;
			}
			
		}
	
	  public void setVariableToActive(String var){
		
			if(ALLVariablesState.containsKey(var)){
				ALLVariablesState.putAt(ALLVariablesState.getIndexOf(var), var, true);
			}   
		}
	
	  public void setVariableState(String var, boolean bol) throws Exception{
			
			if(ALLVariablesState.containsKey(var)){
				ALLVariablesState.putAt(ALLVariablesState.getIndexOf(var), var, bol);
			} 
			else throw new Exception();
		}
	  
	  public void setVariableStatebyIDX(int idx, boolean bol) throws Exception{
			    
		        String var = ALLVariablesState.getKeyAt(idx);
				ALLVariablesState.putAt(idx, var, bol);
		}
	  
	  
	  
	  public void setVariablesssToActive(ArrayList<String> vars){
			
			for (int i = 0; i < vars.size(); i++) {
				
			if (ALLVariablesState.containsKey(vars.get(i))){
				ALLVariablesState.putAt(ALLVariablesState.getIndexOf(vars.get(i)), vars.get(i), true);
			  }
			
			}
		}
	  
	  
	  public void setVariablesToNOTActiveState(ArrayList<String> vars){
			if(vars!=null){
			for (int i = 0; i < vars.size(); i++) {
				
			if (ALLVariablesState.containsKey(vars.get(i))){
				ALLVariablesState.putAt(ALLVariablesState.getIndexOf(vars.get(i)), vars.get(i), false);
			  }
			}
		  }
		}
	  
	  
	  
	  
	  public Boolean getUserVariableState(String var){
		  
		  boolean value = false;
		  if (userConditionsVariablesMappings.containsKey(var))
			  value=ALLVariablesState.get(var);
		  return value;  
	  }
	  
	  
      public Boolean getTFVariableState(String var){
		  
		  boolean value = false;
		  if (TFsVariablesMappings.containsKey(var))
			  value=ALLVariablesState.get(var);
		  return value;  
	  }
	
	  public static String getOnlyVarName(String var){
			
			Pattern pattern = Pattern.compile("(.+)[><](.+)");
			Matcher matcher = pattern.matcher(var.trim());
			if (matcher.find())
			var = matcher.group(1).trim();

				
			return var;
			
			
		}
	  
	  public IndexedHashMap<String, Boolean> getUserVariablesState(){
		  
		  IndexedHashMap<String, Boolean> res = new IndexedHashMap<String, Boolean>();
		  for (String key : userConditionsVariablesMappings.keySet()){
			  if(ALLVariablesState.containsKey(key)){
				  res.put(key, ALLVariablesState.get(key));
			  }		
		}
		   return res;
	  }
	  
     public IndexedHashMap<String, Boolean> getTFsVariablesState(){
		  IndexedHashMap<String, Boolean> res = new IndexedHashMap<String, Boolean>();
		  for (String key : ALLVariablesState.keySet()){
			  if(getVarType(key).equals(VariableCI.TF)){
				  res.put(key, ALLVariablesState.get(key));
			  }		
		}
		   return res;
	  }
	  
	  
	  
	  
     public IndexedHashMap<String, Boolean> getmetabolicModelVariablesState(){
		  
		  IndexedHashMap<String, Boolean> res = new IndexedHashMap<String, Boolean>();
		  for (String key : VariablesofNetworkPresentinMetabolicModelMapping.keySet()){
			  if(ALLVariablesState.containsKey(key)){
				  res.put(key, ALLVariablesState.get(key));
			  }		
		}
		   return res;
	  }
	  
     

	public IndexedHashMap<String, String> getVariablesTypeMap() {
		return variablesTypeMap;
	}
	
	
	public String getVarType(String var){
		
		for(Map.Entry<String, String> map : variablesTypeMap.entrySet()){
			if(map.getKey().equals(var))
				return map.getValue();
		}
		
		return null;	
	}
	
	

	public IndexedHashMap<String, Integer> getTFsVariablesMappings() {
		return TFsVariablesMappings;
	}

	@SuppressWarnings("unchecked")
	public Object clone(){
		  
		  try{
			  VariablesContainer clone = (VariablesContainer)super.clone();
			  clone.ALLVariablesState = (IndexedHashMap<String, Boolean>)ALLVariablesState.clone();
			  clone.ALLVariables = (IndexedHashMap<String, Integer>)ALLVariables.clone();
			  clone.VariablesofNetworkPresentinMetabolicModelMapping =(IndexedHashMap<String, Integer>)VariablesofNetworkPresentinMetabolicModelMapping.clone();
			  clone.VariablestoreactionsMappings = (HashMap<String, Integer>)VariablestoreactionsMappings.clone();
			  clone.userConditionsVariablesMappings = (IndexedHashMap<String, Integer>)userConditionsVariablesMappings.clone();
			  return clone;
		  }
		  catch(CloneNotSupportedException e){
			  System.out.println(e);
			  return null;
	  
	  }
	}

}
