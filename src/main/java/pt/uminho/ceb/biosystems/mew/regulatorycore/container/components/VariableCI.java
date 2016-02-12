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
package pt.uminho.ceb.biosystems.mew.regulatorycore.container.components;

import java.io.Serializable;

public class VariableCI implements Serializable, Cloneable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	protected String type;
	protected boolean usercondition = false;
	
	public static String CONDITION="cond";
	public static String TF ="tf";
	
	
	public VariableCI(String id, String name){
		this.id = id;
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getType() {
		return type;
	}
    
	public void setIsUserCondition(boolean st){
		this.usercondition=st;
	}
	
	public boolean isUserCondition(){
		return this.usercondition;
	}
	
	public void setTypeVar(String type) throws UnsuportedVariableException{
		if(!type.equals(CONDITION) && !type.equals(TF))
			throw new UnsuportedVariableException();
		else
		this.type=type;
	}

	public Object clone() throws CloneNotSupportedException{
		VariableCI clone = (VariableCI)super.clone();
		clone.setId(this.id);
		clone.setName(this.name);
		clone.setIsUserCondition(this.isUserCondition());
		try {
			clone.setTypeVar(this.type);
		} catch (UnsuportedVariableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return clone;
	}
	
	public static void main(String[] args) {
        
	
    }
	

}
