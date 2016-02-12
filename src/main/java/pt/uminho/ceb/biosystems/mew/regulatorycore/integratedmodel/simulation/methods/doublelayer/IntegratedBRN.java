package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.methods.doublelayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneChangesList;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SimulationSteadyStateControlCenter;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.ManagerExceptionUtils;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.MandatoryPropertyException;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.PropertyCastException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.controlcenters.RegulatoryNetworkSimulationControlCenter;
import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.results.IRegulatoryNetworkSimulationResult;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.GeneregulatorychangesList;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.IntegratedSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatorySimulationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.methods.doublelayer.abstractions.AbstractTwoStepIntegratedSimulation;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;
import pt.uminho.ceb.biosystems.mew.solvers.SolverType;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class IntegratedBRN extends AbstractTwoStepIntegratedSimulation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	/** The reggencond. */
    protected RegulatoryGeneticConditions reggencond = null; 
    

    
    /** The varcontainer. */
    protected VariablesContainer varcontainer = null;
    
    /** The false values init step. */
    protected HashSet<String> falseValuesInitStep;
    
    
    /** The gene list. */
    protected GeneChangesList geneList = new GeneChangesList();
    
    /** The regulatorygene list. */
    protected GeneregulatorychangesList regulatorygeneList = new GeneregulatorychangesList();
    
    

    private ArrayList<String> metabolicGenesKnockoutAfterSim;
    protected List<Integer> metabolicGenesKnockout = null;
    
    protected HashSet<String> initialfalsenodes;
    
    protected SolverType solver;
    
    
/**
 * Instantiates a new BRN.
 *
 * @param model the model
 * @throws PropertyCastException the property cast exception
 * @throws MandatoryPropertyException the mandatory property exception
 */
    public IntegratedBRN(ISteadyStateModel model) {
    	super(model);
        initBRNpossiblekeys();
    }
    
    /**
     * Instantiates a new BRN.
     *
     * @param model the model
     * @param solver the solver
     * @throws PropertyCastException the property cast exception
     * @throws MandatoryPropertyException the mandatory property exception
     */
    public IntegratedBRN(ISteadyStateModel model, SolverType solver) {
    	super(model);
        initBRNpossiblekeys();
        setSolver(solver);
    }




/**
 * Init the BRN possiblekeys.
 */
private void initBRNpossiblekeys(){
	possibleProperties.add(RegulatorySimulationProperties.FALSE_VALUES_INITSTEP);	
}



/**
 * Gets the variables container.
 *
 * @return the variables container
 */
private void setVariablesContainer(){
	
if (this.varcontainer == null){
	VariablesContainer container = null;
  try{
		
      container = (VariablesContainer)ManagerExceptionUtils.testCast(propreties, VariablesContainer.class, RegulatorySimulationProperties.VARIABLES_CONTAINER, false);
	} catch (PropertyCastException e) {
		System.err.println("The property " + e.getLocalizedMessage() + " was ignored!!\n Reason: " + e.getMessage());
	} catch (MandatoryPropertyException e) {} 
  
    if (container != null)
    	this.varcontainer = container;
    else	
    	this.varcontainer = ((IIntegratedStedystateModel) integratedmodel).getVariablescontainer();   
   }
}



/**
 * Gets the genes knockouts.
 *
 * @return the genes knockouts
 */
private ArrayList<String> getGenesKnockout(){
	
	ArrayList<String> genesknockouts=null;
	if(this.reggencond==null){
		RegulatoryGeneticConditions geneCond = null;
		
		try {
			
			geneCond=(RegulatoryGeneticConditions) getGeneticConditions();
		
		} catch (PropertyCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MandatoryPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	   if(geneCond != null){     
		genesknockouts = geneCond.getALLGeneKnockoutList();
	
		
	   }
	}
	return genesknockouts;
}


/**
 * Gets the initial false values.
 *
 * @return tthe initial false values
 */
private void  getInitFalseValues() {
	
	HashSet<String> falsenodes = null;
	if (falseValuesInitStep == null)
	try{
	
		falsenodes = (HashSet<String>) ManagerExceptionUtils.testCast(propreties, HashSet.class, RegulatorySimulationProperties.FALSE_VALUES_INITSTEP, true);
		initialfalsenodes=falsenodes;
	} catch (PropertyCastException e) {
		System.err.println("The property " + e.getLocalizedMessage() + " was ignored!!\n Reason: " + e.getMessage());
	} catch (MandatoryPropertyException e) {} 
	
	if (falsenodes != null)
		falseValuesInitStep = falsenodes;
	
	else{
		if(getGenesKnockout()!=null){
		falseValuesInitStep = new HashSet<String>();
		ArrayList<String> geneknockouts = getGenesKnockout();
		for (String i: geneknockouts){
			falseValuesInitStep.add(i);
		 }
	  }
			
	}
	

			
}








/* (non-Javadoc)
* @see regulatorynetwork.simulation.methods.AbstractTwoStepIntegratedSimulation#simulate()
*/
@Override
public IntegratedSimulationResult simulate() throws Exception {

           setVariablesContainer();
           getInitFalseValues();
           

////////////////// Boolean regulatory Network Simulation /////////////////////////////////// 
   IRegulatoryNetworkSimulationResult regulatorynetworksimulation = null;     
   IRegulatoryModel regulatoryNet = ((IIntegratedStedystateModel) integratedmodel).getRegulatoryNet();
  
   
   RegulatoryNetworkSimulationControlCenter regulatorycontrolcenter = new RegulatoryNetworkSimulationControlCenter(regulatoryNet, getRegulatoryNetworkSimulationMethod(), getGenesKnockout(), this.falseValuesInitStep, this.varcontainer.getAllConditionsVarsWithTrueState(), this.varcontainer.getAllTFsVarsWithFalseState(), true);
   regulatorynetworksimulation = regulatorycontrolcenter.simulate();

//////////////// get all gene knockouts after boolean regulatory simulation //////////////////////
   ArrayList<String> geneKnockoutAfterSim = regulatorynetworksimulation.getGenesAlwaysFalseInAttractor();
   
   GeneChangesList metabolicgenes = new GeneChangesList(((IIntegratedStedystateModel) integratedmodel).filterOnlyMetabolicGenes(geneKnockoutAfterSim)); 
   
   GeneregulatorychangesList regulatorygenes = new GeneregulatorychangesList(((IIntegratedStedystateModel) integratedmodel).filterOnlyRegulatoryGenes(geneKnockoutAfterSim));

   RegulatoryGeneticConditions RegulatoryGeneConditionsNewKnockouts = new RegulatoryGeneticConditions(regulatorygenes, metabolicgenes, (IIntegratedStedystateModel)integratedmodel, false);

   SimulationSteadyStateControlCenter simulationControlCenter = new SimulationSteadyStateControlCenter(getEnvironmentalConditions(), RegulatoryGeneConditionsNewKnockouts, getModel(),getMetabolicSimulationMethod());

   if(getSolverType() == null)
	   simulationControlCenter.setSolver(getSolver());
   else
	   simulationControlCenter.setSolver(getSolverType());
   

    simulationControlCenter.setFBAObj(getObjectiveFunction());
   
   
   
	
   simulationControlCenter.setMaximization(getIsMaximization());


    SteadyStateSimulationResult metabolicsolution = simulationControlCenter.simulate();


	IntegratedSimulationResult res = new IntegratedSimulationResult((IIntegratedStedystateModel) integratedmodel,
			getEnvironmentalConditions(),
			RegulatoryGeneConditionsNewKnockouts,
			IntegratedSimulationMethods.INTEGRATED_BRN + " with "+getMetabolicSimulationMethod(),
			metabolicsolution.getFluxValues(), 
			metabolicsolution.getSolverOutput(), 
			metabolicsolution.getOFvalue(),
			metabolicsolution.getOFString(),
			metabolicsolution.getSolutionType(),
			regulatorynetworksimulation.getSimulationSolution(),
			this.varcontainer,
			getMetabolicSimulationMethod());
	
	res.setComplementaryInfoMetabolites(metabolicsolution.getComplementaryInfoMetabolites());
	res.setComplementaryInfoReactions(metabolicsolution.getComplementaryInfoReactions());
    res.setOldRegulatoryGeneticConditions((RegulatoryGeneticConditions) getGeneticConditions());
    res.setUsedfalsenodes(falseValuesInitStep);
    res.setInitialusedfalsenodes(initialfalsenodes);
    res.setInitialUsedVariablesContainer(varcontainer);


     return res;

}



private IndexedHashMap<String, Boolean> buildAtractor(ArrayList<ArrayList<Boolean>> atractor) throws Exception{

    ArrayList<Boolean> oldatractor = atractor.get(0);
    IndexedHashMap<String, Boolean> res = new IndexedHashMap<String, Boolean>();
    ArrayList<String> allregulatoryandmetabolicgenes = ((IIntegratedStedystateModel)integratedmodel).getGenesInRegulatoryModel();
    if (oldatractor.size() != allregulatoryandmetabolicgenes.size())
	throw new Exception();

    else {
	   for (int i = 0; i < allregulatoryandmetabolicgenes.size(); i++) {
		  res.put(allregulatoryandmetabolicgenes.get(i), oldatractor.get(i));
	     }
    }

    return res;
  }



/* (non-Javadoc)
* @see regulatorynetwork.simulation.methods.AbstractTwoStepIntegratedSimulation#getModel()
*/
@Override
public ISteadyStateModel getModel() {
// TODO Auto-generated method stub
return integratedmodel;
}




//@Override
//public void preSimulateActions() {
//	// TODO Auto-generated method stub
//	
//}
//
//
//
//
//@Override
//public void postSimulateActions() {
//	// TODO Auto-generated method stub
//	
//}

public void setSolver(SolverType solver){
	this.solver = solver;
}

public SolverType getSolver(){
	return this.solver;
}


@Override
public void clearAllProperties() {
	// TODO Auto-generated method stub
	
}




//@Override
//public void setRecreateOF(boolean recreateOF) {
//	// TODO Auto-generated method stub
//	
//}
//
//
//
//
//@Override
//public boolean isRecreateOF() {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//
//
//
//@Override
//public void addPropertyChangeListener(PropertyChangeListener listener) {
//	// TODO Auto-generated method stub
//	
//}
//
//
//
//
//@Override
//public void saveModelToMPS(String file, boolean includeTime) {
//	// TODO Auto-generated method stub
//	
//}
//
//
//
//
//@Override
//public void forceSolverCleanup() {
//	// TODO Auto-generated method stub
//	
//}



}
