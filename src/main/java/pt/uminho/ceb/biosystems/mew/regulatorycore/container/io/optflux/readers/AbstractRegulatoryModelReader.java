package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.optflux.readers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.RegulatoryContainer;

public abstract class AbstractRegulatoryModelReader {

	
    protected String modelname;
	protected Map<String,String> filesToBuild;
	protected LinkedHashSet<String> possibleFiles;
	protected LinkedHashSet<String> criticalFiles;
	
	protected RegulatoryContainer regcontainer;
	
	private ArrayList<String> wizardSteps;
	private int currentStep;
	
	
	/**
	 * Function that returns the name that will appear in the table of readers
	 * @return the name of the reader
	 */
	abstract public String getReaderName();
	
	/**
	 * Function that returns the Regulatory container of the model. If
	 * the container is null, it creates the container using the specific
	 * files reader.
	 * @throws Exception
	 */
	abstract public void createContainer() throws Exception/*, WarningsException*/;
	
	/**
	 * Gets the Configuration Panel configurations, and returns a boolean that indicates that something was modified from the previous configurations
	 * @return boolean: true if there are modifications, false if don't 
	 */
	public boolean setConfigurationsReader(){
		return false;
	}
	
	/**
	 * Specifies if the reader needs files loaded from the wizard,
	 * to load the model.
	 * @return boolean: true == needs files
	 */
	abstract public boolean needsSelectionFiles();
	
	/**
	 * Function that determines if this reader has need of 
	 * additional configurations to work. If this value is
	 * true the reader must also specify the correspondant
	 * configuration panel.
	 * @return boolean: true == needs configuration panel
	 */
	abstract public boolean needsConfiguration();
	

	/**
	 * Since the readers are stored in a static table (singleton)
	 * everytime the operation reaches it's end a reset to the reader
	 * is needed, so if another new project is created, the data from
	 * the previous one is deleted from the wizard.
	 */
	public void resetReader(){
		
		modelname = null;
		filesToBuild = new HashMap<String, String>();
		
		regcontainer= null;
	}
	
	
	
	/**
	 * Gets the current step of the wizard
	 * @return current step
	 */
	public int getCurrentStep() {
		return currentStep;
	}
	
	/**
	 * Sets the current step of the wizard
	 * @param currentStep
	 */
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}
	
	
	
}
