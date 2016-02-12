package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.MandatoryPropertyException;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.PropertyCastException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.IRegulatoryNetworkSimulationResult;

public interface IRegulatoryNetworkSimulationMethod {
	
	
	IRegulatoryModel getModel();
	
	public IRegulatoryNetworkSimulationResult simulate();
	

	public void setInitialDeactivatedGenes(HashSet<String> initdeactivatedgenes);
	public HashSet<String> getInitDeactivatedGenes() throws PropertyCastException, MandatoryPropertyException;
	
	public void setGeneKnockouts(ArrayList<String> genesknockout);
	public ArrayList<String> getGeneKnockouts() throws PropertyCastException, MandatoryPropertyException;
	
	public void setTrueVariables(ArrayList<String> truevars);
	public ArrayList<String> getTrueVariables() throws PropertyCastException, MandatoryPropertyException;
	
	
	
    Set<String> getPossibleProperties();
	
	Set<String> getMandatoryProperties();

	void setProperty(String m, Object o);
	
	void putAllProperties(Map<String, Object> properties);
	
	<T> T getProperty (String k);

	
	
	
	
	
	

}
