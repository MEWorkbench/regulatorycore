package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.criticalgenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.model.exceptions.NonExistentIdException;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.FluxValueMap;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneChangesList;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SimulationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.GeneregulatorychangesList;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.controlcenter.IntegratedSimulationControlCenter;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.status.StatusHandlerCriticalgenes;
import pt.uminho.ceb.biosystems.mew.solvers.SolverType;

public class CriticalRegulatoryGenes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double MINIMAL_VALUE = 0.05;
	protected List<String> criticalGenesId = null;
	protected IIntegratedStedystateModel model;
	protected String simulationMethod = SimulationProperties.FBA;
	protected IntegratedSimulationControlCenter center = null;
	protected EnvironmentalConditions envConditions = null;
	protected String biomassFlux;
	protected FluxValueMap wildTypeFluxes = null;
	protected double wildtypebiomassflux;
	protected SolverType solvertype;
	protected VariablesContainer vars = null;
	protected boolean debug = false;
	protected int CurrentIteration = 0;
	protected boolean terminationFlag = false;
	protected StatusHandlerCriticalgenes status= null;
	

	

	public CriticalRegulatoryGenes(IIntegratedStedystateModel model, EnvironmentalConditions envConditions,ArrayList<String>  trueVariables , HashSet<String> falseNodesInInitialStep,String IntegratedSimMethod,String metabolicSimMethod,String regNetSimMethod , SolverType solver) throws Exception 
	{
		this.model = model;		
		this.envConditions = envConditions;
		vars = (VariablesContainer) model.getVariablescontainer().clone();

		if(trueVariables !=null)
		  vars.setVariablesssToActive(trueVariables);
		
		this.biomassFlux = ((IIntegratedStedystateModel) model).getBiomassFlux();
		
		Map<String, Double> obj_coef = new HashMap<String, Double>();
		obj_coef.put(this.biomassFlux, 1.0);

		
		this.center = new IntegratedSimulationControlCenter(this.envConditions, null, this.model,IntegratedSimMethod,metabolicSimMethod,regNetSimMethod,vars,falseNodesInInitialStep,true,solver);
	    center.setObjectiveFunction(obj_coef);
		
	    computewildbiomassFlux();
		
		//wildTypeFluxes = computeWildType();
		//System.out.println("Flux biomass "+wildTypeFluxes.getValue(biomassFlux));
	}
	
	


	public CriticalRegulatoryGenes(List<String> criticalGenesId , IIntegratedStedystateModel model){
		this.criticalGenesId = criticalGenesId;
		this.model = model;
		
	}
	
	/*private FluxValueMap computeWildType() throws Exception{

		IntegratedSimulationResult res = (IntegratedSimulationResult) center.simulate();
		//res.getFluxValues();
		FluxValueMap ret = res.getFluxValues();
		
		
		
		return ret;
	}*/
	
	private void computewildbiomassFlux() throws Exception{
		IntegratedSimulationResult res = (IntegratedSimulationResult) center.simulate();
		this.wildtypebiomassflux=res.getOFvalue();
	}
	
	
	public CriticalRegulatoryGenesResults identifyCriticalgenes() throws Exception
	{

		criticalGenesId = new ArrayList<String>();
		if(status!=null)
		status.setNumberOfFunctionEvaluations(model.getGenesInRegulatoryModel().size());
		
		for(int i=0; i < model.getRegulatoryNet().getNumberOfGenes(); i++)
		{   
			CurrentIteration++;
			if(status!=null)
			status.processEvaluationEvent(i);
			if(terminationFlag == false){
			String geneId = model.getRegulatoryNet().getGene(i).getId();
			if(isRegulatoryGeneCritical(geneId))
				criticalGenesId.add(geneId);
			}
			
		}
		
		return new CriticalRegulatoryGenesResults((ArrayList<String>) criticalGenesId, model);
		
	}
	
	
	
	public void setMINIMAL_VALUE(double minimal_value) {
		MINIMAL_VALUE = minimal_value;
	}

	public void setSimulationMethod(String simulationMethod) {
		this.simulationMethod = simulationMethod;
	}

	public void setEnvConditions(EnvironmentalConditions envConditions) throws Exception {
		this.envConditions = envConditions;
		computewildbiomassFlux();
		//wildTypeFluxes = computeWildType();
	}


	public void setSolver(SolverType solver) {
		center.setSolver(solver);
	}



	public boolean isRegulatoryGeneCritical(String geneId) throws Exception{
		
		boolean ret = true;
		
		if(debug)
		System.out.print("Testing gene "+ geneId);
		
		
		GeneChangesList metgeneList = new GeneChangesList();
		GeneregulatorychangesList reggeneList = new GeneregulatorychangesList();
		
		if (model.isMetabolicGene(geneId)){
			if(debug)
			System.out.print(" Metabolic Gene ");
			metgeneList.addGeneKnockout(geneId);
		}
		else if (model.isRegulatoryGene(geneId)){
			if(debug)
			System.out.print(" Regulatory Gene ");
			reggeneList.addGeneKnockout(geneId);
		}
		else throw new Exception();
		
	
		RegulatoryGeneticConditions genecond = new RegulatoryGeneticConditions(reggeneList, metgeneList, model, false);
		Map<String, Double> obj_coef = new HashMap<String, Double>();
		obj_coef.put(this.biomassFlux, 1.0);

		center.setGeneticConditions(genecond);

		IntegratedSimulationResult result = (IntegratedSimulationResult) center.simulate();
	   
		//double wtbiomass = wildTypeFluxes.getValue(this.biomassFlux);
		
		
		if(result != null){
			//FluxValueMap fluxes = result.getFluxValues();
			double biomassfluxz=result.getOFvalue();
		   
			if(debug)
			System.out.print("-->Biomass="+biomassfluxz+">="+(MINIMAL_VALUE*wildtypebiomassflux)+"-->"+(biomassfluxz >= (MINIMAL_VALUE*wildtypebiomassflux)));
		
			if(biomassfluxz >= (MINIMAL_VALUE*wildtypebiomassflux)){
				
				if(debug)
				System.out.print(" ---> Not critical");
				
				ret = false;
			}
			else
				if(debug)
				System.out.print(" ---> Is critical");
		}
		if(debug)
		System.out.println();
		
		return ret;
	}
	
	public void setStatusHandler(StatusHandlerCriticalgenes st){
		this.status=st;
	}
	
	public int getCurrentIteration(){
		return this.CurrentIteration;
	}
	
	public int getNumberMaxIteration(){
		return this.getModel().getGenesInRegulatoryModel().size();
	}
	
	public void setTerminationFlag(boolean bol){
		this.terminationFlag = bol ;
	}
	
	public boolean stopedoperation(){
		return terminationFlag;
	}
	
	public double getMINIMAL_VALUE() {
		return MINIMAL_VALUE;
	}

	public List<String> getCriticalGenesId() {
		return criticalGenesId;
	}

	public void setCriticalGenesId(List<String> criticalGenesId) {
		this.criticalGenesId = criticalGenesId;
	}

	public IIntegratedStedystateModel getModel() {
		return model;
	}

	public String getSimulationMethod() {
		return simulationMethod;
	}

	public IntegratedSimulationControlCenter getCenter() {
		return center;
	}

	public EnvironmentalConditions getEnvConditions() {
		return envConditions;
	}

	public String getBiomassFlux() {
		return biomassFlux;
	}

	public FluxValueMap getWildTypeFluxes() {
		return wildTypeFluxes;
	}
	
	public void saveInFile(String file) throws IOException{
		
		FileWriter wfile = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(wfile);
		
		for(int i =0; i< criticalGenesId.size(); i++){
			writer.write(criticalGenesId.get(i)+"\n");
		}
		
		writer.close();
		wfile.close();
	}
	
	
	static public CriticalRegulatoryGenesResults loadCriticalRegulatoryGenes(String file, IIntegratedStedystateModel model) throws Exception{
		
		CriticalRegulatoryGenesResults result = null;
		
		FileReader rfile = new FileReader(file);
		BufferedReader reader = new BufferedReader(rfile);
		boolean geneInModel = true;
		
		ArrayList<String> criticalGenesId = new ArrayList<String>();
		
		String line = reader.readLine();
		while(geneInModel && line != null && line != ""){
			if(model.getRegulatoryNet().getGene(line)!=null)
				criticalGenesId.add(line);
			else
				geneInModel = false;
			line = reader.readLine();
		}
		
		if(!geneInModel)
			throw new NonExistentIdException();
		else
			result = new CriticalRegulatoryGenesResults(criticalGenesId, model);
	
		
		return result;
	}


   
}
