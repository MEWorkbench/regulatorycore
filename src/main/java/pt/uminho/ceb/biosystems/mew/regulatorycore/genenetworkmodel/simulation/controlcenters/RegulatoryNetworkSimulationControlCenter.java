package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.controlcenters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.UnregistaredMethodException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.RegulatoryNetworkSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components.RegulatoryNetworkSimulationMethodsFactory;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.formulations.methods.BRNV;

public class RegulatoryNetworkSimulationControlCenter extends AbstractRegulatoryNetworkSimulationControlCenter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static RegulatoryNetworkSimulationMethodsFactory factory;
	
	
	static{
		
		LinkedHashMap<String, Class<?>> mapMethods = new LinkedHashMap<String,Class<?>>();
		
		mapMethods.put(RegulatoryNetworkSimulationMethods.BRNV, BRNV.class);

		
		factory = new RegulatoryNetworkSimulationMethodsFactory(mapMethods);
	}
	
	

	public RegulatoryNetworkSimulationControlCenter(IRegulatoryModel regmodel,String regmethodType, ArrayList<String> genesKnockout, HashSet<String> InifalseNetNodes, ArrayList<String> trueVariables, ArrayList<String> falseTfsvalues, boolean isintegrated) {
		super(regmodel,regmethodType,genesKnockout,InifalseNetNodes,trueVariables, falseTfsvalues, isintegrated);

	}

	@Override
	protected RegulatoryNetworkSimulationMethodsFactory getFactory() {
		return factory;
	}
	
	static public void registMethod(String methodId, Class<?> klass) throws Exception{
		factory.addSimulationMethod(methodId, klass);
	}
	
	public static Set<String> getRegisteredMethods(){
		return factory.getRegisteredMethods();
	}
	
	public static Class<?> getMethod(String method) throws UnregistaredMethodException{
		return factory.getClassProblem(method);
	}


	
	
	

}
