package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RegulatoryNetworkSimulationMethods {
    
	// declared Field Must be equal, example String TEST = "TEST";
	public static final String BRNV = "BRNV";
	
	
	
	
	
	
	public ArrayList<String> getMethods(){
	ArrayList <String> names = new ArrayList<String>();
	Field[] declaredFields = RegulatoryNetworkSimulationMethods.class.getDeclaredFields();
	for (Field field : declaredFields) {
	    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
	    	names.add(field.getName());

	    }
	   }
	return names;
	}

}
