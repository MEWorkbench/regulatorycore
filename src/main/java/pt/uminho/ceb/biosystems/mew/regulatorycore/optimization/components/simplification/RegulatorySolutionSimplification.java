package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.simplification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.IObjectiveFunction;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.IntegratedSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.controlcenter.IntegratedSimulationControlCenter;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.results.RegulatorySolution;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.results.RegulatorySolutionSet;
import pt.uminho.ceb.biosystems.mew.solvers.SolverType;

public class RegulatorySolutionSimplification implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private double delta = 0.000001;

	protected IIntegratedStedystateModel model;
	
	protected List<IObjectiveFunction> objectiveFunctions;
		
	protected String methodType = IntegratedSimulationMethods.INTEGRATED_BRN;
	
	protected IntegratedSimulationControlCenter center = null;
	
	public RegulatorySolutionSimplification(IIntegratedStedystateModel model, 
			List<IObjectiveFunction> objFunctions, 
			String methodType,
			String metabolimethodtype,
			String regulatorysimmethodtype,
			VariablesContainer variables,
			HashSet<String> falsenodes, 
			EnvironmentalConditions envCond, 
			SolverType solver)
	{
		this.model = model;
		this.objectiveFunctions = objFunctions;
		this.methodType = methodType;
		this.center = new IntegratedSimulationControlCenter(envCond, null, model, methodType, metabolimethodtype, regulatorysimmethodtype, variables, falsenodes, true, solver);

	}
	

	public void setSolver(SolverType solver) {
		center.setSolver(solver);
	}
	
	public void setMargin (double delta)
	{
		this.delta = delta;
	}
  

	public RegulatorySolutionSet simplifySteadyStateOptimizationResult(RegulatorySolutionSet optResultIN, boolean isGeneOpt) throws Exception{
		RegulatorySolutionSet optResultOut = new RegulatorySolutionSet(optResultIN.getBaseConfiguration());
		
		List<RegulatorySolution> regList = optResultIN.getResultList();
//		
		for (RegulatorySolution regSol : regList) {
			
			RegulatorySolution simp = null;
			
			simp = simplifyRegulatoryGenesSolution((RegulatoryGeneticConditions) regSol.getGeneticConditions(), regSol);
	
//			IntegratedSimulationResult resOut = simp.getSimulationResult();
			List<Double> fits = simp.getAttributes();
//			
			ArrayList<Double> fitnesses = new ArrayList<Double>();
			for(double f: fits)
				fitnesses.add(f);
			
			optResultOut.addSolutionNoRepeat(simp);
		}
		
//		for(String id: optResultIN.getSimulationMap().keySet()){			
//			
//			IntegratedSimulationResult resOrig = (IntegratedSimulationResult) optResultIN.getSimulationResult(id);
//			RegulatorySolutionSimplificationResult simp = null;
//			
//			
//			simp = simplifyRegulatoryGenesSolution((RegulatoryGeneticConditions) resOrig.getOldRegulatoryGeneticConditions(), resOrig);
//	
//			IntegratedSimulationResult resOut = simp.getSimulationResult();
//			double[] fits = simp.getFitnesses();
//			
//			ArrayList<Double> fitnesses = new ArrayList<Double>();
//			for(double f: fits)
//				fitnesses.add(f);
//			
//			optResultOut.addOptimizationResultNoRepeated(resOut, fitnesses);
//		}
		
		return optResultOut;
	}
	

	
	public RegulatorySolutionSimplificationResult simplifyRegulatoryGenesSolution (RegulatoryGeneticConditions initialSolution, IntegratedSimulationResult initialRes)
			throws Exception
			{
				
				IntegratedSimulationResult origRes;
				if (initialRes == null)
				{
					center.setGeneticConditions(initialSolution);
					origRes = (IntegratedSimulationResult) center.simulate();
				}
				else origRes = initialRes;
				
				double[] initialFitnesses = evaluateSolution(origRes);
				
				Set<String> geneIds = initialSolution.getGeneList().getGeneIds();
			
				
				List<String> genesIDsIterator = new ArrayList<String>(geneIds); 
				
				RegulatoryGeneticConditions finalSolution = initialSolution;
				double [] finalFitnesses = initialFitnesses;
				IntegratedSimulationResult finalRes = origRes;
				
				
				for (String geneId: genesIDsIterator)
				{
								
					finalSolution.getGeneList().removeGene(geneId);
					finalSolution.updateReactionsList((IIntegratedStedystateModel) model);
								
					center.setGeneticConditions(finalSolution);

					IntegratedSimulationResult res = (IntegratedSimulationResult) center.simulate();
					
					double [] simpfitnesses = evaluateSolution(res);
					
					if (compare(finalFitnesses, simpfitnesses))
					{
					
						finalFitnesses = simpfitnesses;
						finalRes = res;
					}
					else
					{			
						finalSolution.addGene(geneId);
						finalSolution.updateReactionsList((IIntegratedStedystateModel) model);
						
					}
				}				
				
				RegulatorySolutionSimplificationResult optimizationSimplification = new RegulatorySolutionSimplificationResult(finalRes,finalFitnesses);
				
				return optimizationSimplification;
			}
	
	public RegulatorySolution simplifyRegulatoryGenesSolution(RegulatoryGeneticConditions initialSolution, RegulatorySolution initialRes) throws Exception{
			
		IntegratedSimulationResult origSimResult;
		HashMap<String, SteadyStateSimulationResult> resMap = new HashMap<>();
		
		
		if (initialRes == null){
			center.setGeneticConditions(initialSolution);
			origSimResult = (IntegratedSimulationResult) center.simulate();
		}
		else{
			origSimResult = (IntegratedSimulationResult) initialRes.getSimulationResultMap().values().iterator().next();
			String m = origSimResult.getMethod();
		}
		
		resMap.put(origSimResult.getMethod(), origSimResult);
		
		
		TreeSet<String> geneIds = new TreeSet<String>(initialSolution.getGeneList().getGeneIds());
//		Set<String> geneIds = initialSolution.getGeneList().getGeneIds();
		
		double[] initialFitnesses = evaluateSolution(origSimResult);
	
		
		List<String> genesIDsIterator = new ArrayList<String>(geneIds); 
		
		RegulatoryGeneticConditions finalSolution = initialSolution;
		double [] finalFitnesses = initialFitnesses;
		IntegratedSimulationResult finalRes = origSimResult;
		
		
		for (String geneId: genesIDsIterator)
		{
			finalSolution.getGeneList().removeGene(geneId);
			finalSolution.updateReactionsList((IIntegratedStedystateModel) model);
						
			center.setGeneticConditions(finalSolution);

			IntegratedSimulationResult res = (IntegratedSimulationResult) center.simulate();
			
			double [] simpfitnesses = evaluateSolution(res);
			
			if (compare(finalFitnesses, simpfitnesses))
			{
				finalFitnesses = simpfitnesses;
				finalRes = res;
			}
			else
			{			
				finalSolution.addGene(geneId);
				finalSolution.updateReactionsList((IIntegratedStedystateModel) model);
				
			}
		}				
		
		List<Double> fitness = new ArrayList<>();
		for (int i = 0; i < finalFitnesses.length; i++)
			fitness.add(finalFitnesses[i]);
		
		resMap.put(origSimResult.getMethod(), finalRes);
		
		RegulatorySolution toRet = new RegulatorySolution(finalSolution, resMap, fitness);
		
		return toRet;
	}
	
	
	
	
	
	private double[] evaluateSolution (IntegratedSimulationResult result) throws Exception
	{
		int size = objectiveFunctions.size();
		double resultList[] = new double[size];
		for(int i=0;i<size;i++)
		{
			IObjectiveFunction of = objectiveFunctions.get(i);
			resultList[i]  = of.evaluate(result);
		}
		return resultList;
	}
	
	private boolean compare(double[] fitnesses, double[] simplifiedFitness)
	{
		boolean res = true;
		int i = 0;

		while(res && i < objectiveFunctions.size())
		{
			IObjectiveFunction of = objectiveFunctions.get(i);
			if (of.isMaximization()) {
				if (fitnesses[i] - simplifiedFitness[i] > delta) res = false;
			}
			else if (simplifiedFitness[i] - fitnesses[i] > delta) res = false;
			i++;
		}
		
		return res;
	}	
	
	
}
