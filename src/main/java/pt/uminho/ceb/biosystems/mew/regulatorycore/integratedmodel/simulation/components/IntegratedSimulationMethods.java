package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class IntegratedSimulationMethods {
	
	// Attention with the order of how methods are declared in th static fields
	
	public static final String INTEGRATED_BRN = "INTEGRATED_BRN";
	public static final String SRFBA = "SRFBA";
	
	
	public ArrayList<String> getMethods(){
	ArrayList <String> names = new ArrayList<String>();
	Field[] declaredFields = IntegratedSimulationMethods.class.getDeclaredFields();

	for (Field field : declaredFields) {
	    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
	    	names.add(field.getName());
	    
	    }
	   }
	return names;
	}
	

}
