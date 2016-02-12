package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.results;
//package org.optflux.regulatorycore.optimization.results;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.optflux.regulatorytool.integratedmodel.model.IIntegratedStedystateModel;
//import org.optflux.regulatorytool.integratedmodel.simulation.components.RegulatoryGeneticConditions;
//import org.optflux.regulatorytool.integratedmodel.simulation.results.IntegratedSimulationResult;
//
//import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.IObjectiveFunction;
//
//public class IntegratedOptimizationResult extends SteadyStateOptimizationResult{
//	
//
//	private static final long serialVersionUID = 1L;
//
//
//	public IntegratedOptimizationResult(IIntegratedStedystateModel model, List<IObjectiveFunction> objectiveFunctions) {
//		super(model, objectiveFunctions);
//	}
//	
//	
//	@Override
//	public void writeToFile(String file, String delimiter) throws IOException{
//		FileWriter fw = new FileWriter(file);
//		BufferedWriter bw = new BufferedWriter(fw);
//		
//		for(String simID : this.simulationMap.keySet()){
//			IntegratedSimulationResult res = (IntegratedSimulationResult) simulationMap.get(simID);
//			RegulatoryGeneticConditions genecond = res.getOldRegulatoryGeneticConditions();
//			
//			ArrayList<String> resgenes = null;
//			if(genecond!=null){
//				resgenes = genecond.getAllContainedGenes();
//				
//			}
//
//			ArrayList<Double> fits = fitnessesMap.get(simID);
//			
//			StringBuffer towrite = new StringBuffer();
//			
//			// fits
//			for(Double d: fits)
//				towrite.append(d+delimiter);
//			
//			// solutions
//			if(genecond!=null){
//			for(String sol : resgenes){
//				towrite.append(delimiter+sol);
//			}
//			
//			bw.append(towrite.toString());
//			bw.newLine();
//			}
//		}
//
//		bw.flush();
//		fw.flush();
//		bw.close();			
//		fw.close();		
//	}
//
//
//}
