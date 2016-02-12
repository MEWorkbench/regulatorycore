package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.controlcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.jecoli.algorithm.AlgorithmTypeEnum;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.algorithm.IAlgorithm;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.algorithm.writer.IAlgorithmResultWriter;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.configuration.IConfiguration;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.configuration.InvalidConfigurationException;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.container.ReproductionOperatorContainer;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.hybridset.crossover.HybridSetUniformCrossover;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.hybridset.mutation.HybridSetGaussianPertubationMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.hybridset.mutation.HybridSetGrowthMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.hybridset.mutation.HybridSetRandomIntegerMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.hybridset.mutation.HybridSetRandomSetMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.hybridset.mutation.HybridSetShrinkMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.set.SetGrowthMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.set.SetRandomMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.set.SetShrinkMutation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.reproduction.set.SetUniformCrossover;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.selection.EnvironmentalSelection;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.operator.selection.TournamentSelection;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.randomnumbergenerator.DefaultRandomNumberGenerator;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.randomnumbergenerator.IRandomNumberGenerator;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.representation.IElementsRepresentation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.representation.hybridset.IntIntHybridSetRepresentationFactory;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.representation.integer.IntegerSetRepresentationFactory;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.representation.set.SetRepresentation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.solution.ISolution;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.solution.ISolutionFactory;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.solution.ISolutionSet;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.statistics.StatisticsConfiguration;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.terminationcriteria.ITerminationCriteria;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.terminationcriteria.IterationTerminationCriteria;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.terminationcriteria.NumFunctionEvaluationsListenerHybridTerminationCriteria;
import pt.uminho.ceb.biosystems.jecoli.algorithm.components.terminationcriteria.NumberOfFunctionEvaluationsTerminationCriteria;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.aggregation.IAggregationFunction;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.aggregation.SimpleMultiplicativeAggregation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.components.ArchiveManager;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.components.InsertionStrategy;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.components.ProcessingStrategy;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.trimming.ITrimmingFunction;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.archive.trimming.ZitzlerTruncation;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.spea2.SPEA2;
import pt.uminho.ceb.biosystems.jecoli.algorithm.multiobjective.spea2.SPEA2Configuration;
import pt.uminho.ceb.biosystems.jecoli.algorithm.singleobjective.evolutionary.EvolutionaryAlgorithm;
import pt.uminho.ceb.biosystems.jecoli.algorithm.singleobjective.evolutionary.EvolutionaryConfiguration;
import pt.uminho.ceb.biosystems.jecoli.algorithm.singleobjective.evolutionary.RecombinationParameters;
import pt.uminho.ceb.biosystems.jecoli.algorithm.singleobjective.simulatedannealing.AnnealingSchedule;
import pt.uminho.ceb.biosystems.jecoli.algorithm.singleobjective.simulatedannealing.IAnnealingSchedule;
import pt.uminho.ceb.biosystems.jecoli.algorithm.singleobjective.simulatedannealing.SimulatedAnnealing;
import pt.uminho.ceb.biosystems.jecoli.algorithm.singleobjective.simulatedannealing.SimulatedAnnealingConfiguration;
import pt.uminho.ceb.biosystems.mew.core.model.components.EnvironmentalConditions;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SimulationProperties;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.configuration.GenericOptimizationProperties;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.objectivefunctions.IObjectiveFunction;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.optimizationresult.archivetrimming.SelectionValueTrimmer;
import pt.uminho.ceb.biosystems.mew.core.strainoptimization.strainoptimizationalgorithms.jecoli.components.decoder.ISteadyStateDecoder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.IntegratedSimulationMethods;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.controlcenter.IntegratedSimulationControlCenter;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.configuration.RegulatoryGenericConfiguration;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.decoders.RegulatoryGenesKnockoutDecoder;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.components.evaluationfunctions.RegulatoryGeneKnockoutEvaluationFunction;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.results.RegulatorySolution;
import pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.results.RegulatorySolutionSet;
import pt.uminho.ceb.biosystems.mew.solvers.SolverType;

public class RegulatoryGeneKnockoutOptimizationControlCenter {
	
	
    protected IIntegratedStedystateModel model;
	
	
	boolean overUnder = false; // if false = KOs
	
	boolean variableSize = false;
	
	int maxSolutionSize = 6; //default
	
	protected AlgorithmTypeEnum optimizationMethod = AlgorithmTypeEnum.EA;

	protected IAlgorithm<IElementsRepresentation<?>> optimizationAlgorithm;
	
	protected ISteadyStateDecoder decoder;
	
	protected RegulatoryGeneKnockoutEvaluationFunction evaluationFunction;
	
	protected IAggregationFunction aggregationFunction;
	
	protected ArchiveManager<?,IElementsRepresentation<?>> archive = null;

	protected IRandomNumberGenerator randomGenerator;
	
	protected SolverType solver;
	
	protected List<String> notAllowedIDs;
	
	protected EnvironmentalConditions environmentalConditions;
	
	protected String simulationMethod;
	protected String metabolicSimulationMethod;
	protected String regulatorynetowrksimulationmethod;
	
	protected VariablesContainer variablescontainer;
	
	protected HashSet<String> initialFalseNodes;
	

	protected RegulatoryGenericConfiguration regulatoryConfiguration = new RegulatoryGenericConfiguration();
	protected Map<String, Object> simulationConfiguration;
	

	public RegulatoryGeneKnockoutOptimizationControlCenter(
			IIntegratedStedystateModel model,
			boolean overUnder, 
			boolean variableSize,
			int maxSolutionSize, 
			AlgorithmTypeEnum optimizationMethod,
			SolverType solver, 
			List<String> notAllowedIDs,
			EnvironmentalConditions environmentalConditions,
			IConfiguration<IElementsRepresentation<?>> algorithmConfiguration,
			List<IObjectiveFunction> objFunctions,
			String simulationMethod,
			String metabolicsimulationMethod,
			String regnetworksimulationmethod,
			String simulationMethodOverUnderReference,
			VariablesContainer varscontainer,
			HashSet<String> initialFalseNodes
			) throws Exception {
		super();
		this.model = model;
		this.overUnder = overUnder;
		this.variableSize = variableSize;
		this.maxSolutionSize = maxSolutionSize;
		this.optimizationMethod = optimizationMethod;
		this.solver = solver;
		this.notAllowedIDs = notAllowedIDs;
		this.environmentalConditions = environmentalConditions;
		this.randomGenerator = new DefaultRandomNumberGenerator();
		this.simulationMethod = simulationMethod;
		this.metabolicSimulationMethod = metabolicsimulationMethod;
		this.regulatorynetowrksimulationmethod = regnetworksimulationmethod;
		this.variablescontainer = varscontainer;
		this.initialFalseNodes = initialFalseNodes;
		
		createDecoder(notAllowedIDs);
		createEvaluationFunction(environmentalConditions, decoder, objFunctions, varscontainer, simulationMethod, metabolicsimulationMethod,regnetworksimulationmethod, initialFalseNodes, this.solver);
		if(algorithmConfiguration==null)
			configureAlgorithm(optimizationMethod,  maxSolutionSize, variableSize);
		optimizationAlgorithm.getConfiguration().getStatisticConfiguration().setScreenIterationInterval(1);
		
		buildConfiguration();
	}
	
	
	
	
	
	/**
	 * <p> Creates a decoder, taking into account if it should use Gene-Protein-Reaction (GPR) information and/or over/under expression approach. </p>
	 * 
	 * @param geneOpt uses GPR information if true, doesn't use otherwise 
	 * @param overUnder uses the over/under expression approach if true, uses the knockout approach otherwise
	 * @throws Exception if adding not allowed IDs to decoder catches an Exception 
	 */
	public void createDecoder (List<String> notAllowedIDs) throws Exception{
		

			decoder = new RegulatoryGenesKnockoutDecoder(model, notAllowedIDs);

	}
	
	
	public void createEvaluationFunction(EnvironmentalConditions envConds, ISteadyStateDecoder decoder, List<IObjectiveFunction> ofs, VariablesContainer varscontainer,String simulationMethod, String metabolicsimulmethod,String regulatorynetowrksimulmethod, HashSet<String> initialFalseNodes,SolverType solv) throws Exception {
		this.evaluationFunction = new RegulatoryGeneKnockoutEvaluationFunction(model,decoder, ofs, envConds, varscontainer, simulationMethod, metabolicsimulmethod,regulatorynetowrksimulmethod ,initialFalseNodes,solv);
	}
    
	
	/**
	 * 
	 * 
	 * @param geneOpt
	 * @param overUnder
	 * @param maxSetSize
	 * @return
	 */
	public ISolutionFactory<?> createSolutionFactory (boolean overUnder, int maxSetSize)
	{
		ISolutionFactory<?> res = null;
		int minSetSize = 0;
		int maxSetValue = decoder.getNumberVariables();
		


		if (overUnder) {
			int n = 5;					
			res = new IntIntHybridSetRepresentationFactory(minSetSize, maxSetSize, maxSetValue,n, -(n+1), evaluationFunction.getNumberOfObjectives());
		}
		//else res = new IntegerSetRepresentationFactory(maxSetValue, maxSetSize, evaluationFunction.getNumberOfObjectives()); // check this ...	
		else res = new IntegerSetRepresentationFactory(maxSetValue,minSetSize, maxSetSize,minSetSize,maxSetSize,evaluationFunction.getNumberOfObjectives());
		
		return res;
	}
	
	
	/**
	 * 
	 * @param algorithmType
	 * @param maxSetSize
	 * @param isVariableSizeGenome
	 * @throws Exception
	 */
	public void configureAlgorithm(AlgorithmTypeEnum algorithmType, int maxSetSize, boolean isVariableSizeGenome) throws Exception
	{
		this.optimizationMethod = algorithmType;
		int minSize = 1;

		int numberOfObjectives = evaluationFunction.getNumberOfObjectives();
		
		ISolutionFactory solutionFactory = createSolutionFactory(this.overUnder, maxSetSize);
			
		if(optimizationMethod.equals(AlgorithmTypeEnum.EA)){
			configureEA(solutionFactory,isVariableSizeGenome);
		}
		else if(optimizationMethod.equals(AlgorithmTypeEnum.SA)){
			configureSA(solutionFactory,isVariableSizeGenome);
		}
		else if(optimizationMethod.equals(AlgorithmTypeEnum.SPEA2)){
			configureSPEA2(solutionFactory,isVariableSizeGenome);
		}
		else throw new Exception("Unsupported optimization algorithm");
	}
	

	public void configureEA(ISolutionFactory solutionFactory, boolean isVariableSizeGenome) throws Exception, InvalidConfigurationException
	{
		EvolutionaryConfiguration configuration = new EvolutionaryConfiguration();	
		configuration.setEvaluationFunction(evaluationFunction);
		configuration.setSolutionFactory(solutionFactory); 
		
		int populationSize = 100;
		configuration.setPopulationSize(populationSize);

	
		configuration.setRandomNumberGenerator(randomGenerator);
		configuration.setProblemBaseDirectory("nullDirectory");
		configuration.setAlgorithmStateFile("nullFile");
		configuration.setSaveAlgorithmStateDirectoryPath("nullDirectory");
		configuration.setAlgorithmResultWriterList(new ArrayList<IAlgorithmResultWriter<SetRepresentation>>());
		configuration.setStatisticsConfiguration(new StatisticsConfiguration());
		
		int numberIterations = 500;
		ITerminationCriteria terminationCriteria = new IterationTerminationCriteria(numberIterations);
		configuration.setTerminationCriteria(terminationCriteria);
		
		RecombinationParameters recombinationParameters = new RecombinationParameters(50,49,1,true);
		configuration.setRecombinationParameters(recombinationParameters);
		
		configuration.setSelectionOperator(new TournamentSelection<SetRepresentation>(1,2));
		configuration.setSurvivorSelectionOperator(new TournamentSelection<SetRepresentation>(1,2));
		configuration.getStatisticConfiguration().setNumberOfBestSolutionsToKeepPerRun(15);
		
		ReproductionOperatorContainer reproductionOperatorContainer = new ReproductionOperatorContainer();
		reproductionOperatorContainer = createEAReproductionOperatorContainer(solutionFactory, isVariableSizeGenome);
		configuration.setReproductionOperatorContainer(reproductionOperatorContainer);
		
		optimizationAlgorithm = new EvolutionaryAlgorithm(configuration);	
		
		regulatoryConfiguration.setTerminationCriteria(terminationCriteria);
		
	}
		
	
	protected ReproductionOperatorContainer createEAReproductionOperatorContainer(ISolutionFactory<SetRepresentation> solutionFactory,boolean isVariableSizeGenome) throws Exception{
		ReproductionOperatorContainer reproductionOperatorContainer = new ReproductionOperatorContainer();
		
		if (this.overUnder)  {
			if(isVariableSizeGenome){
				reproductionOperatorContainer.addOperator(0.15, new HybridSetUniformCrossover());				
				reproductionOperatorContainer.addOperator(0.2, new HybridSetRandomSetMutation(2));
				reproductionOperatorContainer.addOperator(0.2, new HybridSetRandomIntegerMutation(2));
				reproductionOperatorContainer.addOperator(0.15, new HybridSetGaussianPertubationMutation(2));
				reproductionOperatorContainer.addOperator(0.15, new HybridSetGrowthMutation());
				reproductionOperatorContainer.addOperator(0.15, new HybridSetShrinkMutation());					
			}else{
				reproductionOperatorContainer.addOperator(0.15, new HybridSetUniformCrossover());
				reproductionOperatorContainer.addOperator(0.3, new HybridSetRandomSetMutation(2));
				reproductionOperatorContainer.addOperator(0.3, new HybridSetRandomIntegerMutation(2));
				reproductionOperatorContainer.addOperator(0.25, new HybridSetGaussianPertubationMutation(2));
			}
		}
		else {
			if(isVariableSizeGenome){
				reproductionOperatorContainer.addOperator(0.25, new SetUniformCrossover());
				reproductionOperatorContainer.addOperator(0.5, new SetRandomMutation());	
				reproductionOperatorContainer.addOperator(0.125, new SetGrowthMutation());
				reproductionOperatorContainer.addOperator(0.125, new SetShrinkMutation());
			}
			else {
				reproductionOperatorContainer.addOperator(0.5, new SetUniformCrossover());
				reproductionOperatorContainer.addOperator(0.5, new SetRandomMutation());
			}
		}
		return reproductionOperatorContainer;	
	}
	
	
	public void configureSA(ISolutionFactory solutionFactory, boolean isVariableSizeGenome) throws Exception, InvalidConfigurationException
	{
		SimulatedAnnealingConfiguration configuration = new SimulatedAnnealingConfiguration();
		configuration.setEvaluationFunction(evaluationFunction);
		configuration.setSolutionFactory(solutionFactory); 
		
		IAnnealingSchedule annealingSchedule = new AnnealingSchedule(0.007,0.000006,50,50000);
		configuration.setAnnealingSchedule(annealingSchedule);
		

		configuration.setRandomNumberGenerator(randomGenerator);
		configuration.setProblemBaseDirectory("nullDirectory");
		configuration.setAlgorithmStateFile("nullFile");
		configuration.setSaveAlgorithmStateDirectoryPath("nullDirectory");
		configuration.setAlgorithmResultWriterList(new ArrayList<IAlgorithmResultWriter<SetRepresentation>>());
		configuration.setStatisticsConfiguration(new StatisticsConfiguration());
		
		int numberOfFunctionEvaluations = 50000;
		ITerminationCriteria terminationCriteria = new NumberOfFunctionEvaluationsTerminationCriteria(numberOfFunctionEvaluations);
		configuration.setTerminationCriteria(terminationCriteria);
		configuration.getStatisticConfiguration().setNumberOfBestSolutionsToKeepPerRun(15);
		
		ReproductionOperatorContainer reproductionOperatorContainer = createSAReproductionOperatorContainer(solutionFactory, isVariableSizeGenome);
		configuration.setMutationOperatorContainer(reproductionOperatorContainer);
		
		optimizationAlgorithm = new SimulatedAnnealing(configuration);
		
		regulatoryConfiguration.setTerminationCriteria(terminationCriteria);
	}
	
	
	protected ReproductionOperatorContainer createSAReproductionOperatorContainer(ISolutionFactory<SetRepresentation> solutionFactory,boolean isVariableSizeGenome) throws Exception{
		ReproductionOperatorContainer reproductionOperatorContainer = new ReproductionOperatorContainer();


		if (this.overUnder)  {
			if(isVariableSizeGenome)
			{			
				reproductionOperatorContainer.addOperator(0.2, new HybridSetRandomSetMutation(2));
				reproductionOperatorContainer.addOperator(0.2, new HybridSetRandomIntegerMutation(2));
				reproductionOperatorContainer.addOperator(0.2, new HybridSetGaussianPertubationMutation(2));	
				reproductionOperatorContainer.addOperator(0.2, new HybridSetGrowthMutation());
				reproductionOperatorContainer.addOperator(0.2, new HybridSetShrinkMutation());
			}
			else
			{
				reproductionOperatorContainer.addOperator(0.333, new HybridSetRandomSetMutation(2));
				reproductionOperatorContainer.addOperator(0.333, new HybridSetRandomIntegerMutation(2));
				reproductionOperatorContainer.addOperator(0.334, new HybridSetGaussianPertubationMutation(2));	
			}
		}
		else {
			if(isVariableSizeGenome){
				reproductionOperatorContainer.addOperator(0.50,new SetRandomMutation(2));	
				reproductionOperatorContainer.addOperator(0.25,new SetGrowthMutation());
				reproductionOperatorContainer.addOperator(0.25,new SetShrinkMutation());
			}
			else
			{
				reproductionOperatorContainer.addOperator(1,new SetRandomMutation(2));
			}
		}	

		return reproductionOperatorContainer;	
	}
	
	public void configureSPEA2(ISolutionFactory solutionFactory,boolean isVariableSizeGenome) throws Exception, InvalidConfigurationException{
		
		SPEA2Configuration configuration = new SPEA2Configuration();	
		configuration.setEvaluationFunction(evaluationFunction);
		configuration.setSolutionFactory(solutionFactory); 
		configuration.setNumberOfObjectives(evaluationFunction.getNumberOfObjectives());
		configuration.setRandomNumberGenerator(randomGenerator);
		configuration.setProblemBaseDirectory("nullDirectory");
		configuration.setAlgorithmStateFile("nullFile");
		configuration.setSaveAlgorithmStateDirectoryPath("nullDirectory");
		configuration.setAlgorithmResultWriterList(new ArrayList<IAlgorithmResultWriter<SetRepresentation>>());
	
		configuration.setStatisticsConfiguration(new StatisticsConfiguration());
		
		int populationSize = 100;
		int archiveSize = 100;
		
		configuration.setPopulationSize(populationSize);
		configuration.setMaximumArchiveSize(archiveSize);
		configuration.getStatisticConfiguration().setNumberOfBestSolutionsToKeepPerRun(archiveSize);

		int numberIterations = 1000;
		ITerminationCriteria terminationCriteria = new IterationTerminationCriteria(numberIterations);
		configuration.setTerminationCriteria(terminationCriteria);
		
		RecombinationParameters recombinationParameters = new RecombinationParameters(0,100,0,true);
		configuration.setRecombinationParameters(recombinationParameters);
		
		configuration.setEnvironmentalSelectionOperator(new EnvironmentalSelection());
		configuration.setSelectionOperator(new TournamentSelection(1,2));
		
		
		ReproductionOperatorContainer reproductionOperatorContainer = new ReproductionOperatorContainer();
		if (this.overUnder)  {
			if(isVariableSizeGenome){
				reproductionOperatorContainer.addOperator(0.15, new HybridSetUniformCrossover());				
				reproductionOperatorContainer.addOperator(0.2, new HybridSetRandomSetMutation(2));
				reproductionOperatorContainer.addOperator(0.2, new HybridSetRandomIntegerMutation(2));
				reproductionOperatorContainer.addOperator(0.15, new HybridSetGaussianPertubationMutation(2));
				reproductionOperatorContainer.addOperator(0.15, new HybridSetGrowthMutation());
				reproductionOperatorContainer.addOperator(0.15, new HybridSetShrinkMutation());					
			}else{
				reproductionOperatorContainer.addOperator(0.15, new HybridSetUniformCrossover());
				reproductionOperatorContainer.addOperator(0.3, new HybridSetRandomSetMutation(2));
				reproductionOperatorContainer.addOperator(0.3, new HybridSetRandomIntegerMutation(2));
				reproductionOperatorContainer.addOperator(0.25, new HybridSetGaussianPertubationMutation(2));
			}
		}else{
			reproductionOperatorContainer.addOperator(0.25, new SetUniformCrossover());
			reproductionOperatorContainer.addOperator(0.5, new SetRandomMutation());	
			reproductionOperatorContainer.addOperator(0.125, new SetGrowthMutation(2));
			reproductionOperatorContainer.addOperator(0.125, new SetShrinkMutation(1));
		}
		configuration.setReproductionOperatorContainer(reproductionOperatorContainer);
		optimizationAlgorithm = new SPEA2(configuration);	
		
		regulatoryConfiguration.setTerminationCriteria(terminationCriteria);
	
	}
	
	
	// rever isto ...
		public void setNumberFunctionEvaluations(int nfe) throws Exception
		{		
			IConfiguration configuration = optimizationAlgorithm.getConfiguration();
			ITerminationCriteria terminationCriteria = new NumFunctionEvaluationsListenerHybridTerminationCriteria(nfe);
			configuration.setTerminationCriteria(terminationCriteria);
		}
		
		public void setTerminationCriteria(ITerminationCriteria terminationCriteria){
			IConfiguration configuration = optimizationAlgorithm.getConfiguration();
			configuration.setTerminationCriteria(terminationCriteria);
		}
		
		
		public void configureDefaultArchive(){

			archive = new ArchiveManager(
					optimizationAlgorithm,
					InsertionStrategy.ADD_ON_SINGLE_EVALUATION_FUNCTION_EVENT,
					InsertionStrategy.ADD_SMART,
					ProcessingStrategy.PROCESS_ARCHIVE_ON_SINGLE_EVALUATION_FUNCTION_EVENT
					);

			archive.setMaximumArchiveSize(100);

			ITrimmingFunction trimmer = (evaluationFunction.getNumberOfObjectives()>1) 	? new ZitzlerTruncation(archive.getMaximumArchiveSize(), evaluationFunction)
			: new SelectionValueTrimmer(archive.getMaximumArchiveSize(), 0.000002);

			archive.addTrimmingFunction(trimmer);		
		}

		/**
		 * Runnable. Must be invoked after all the necessary parameters have been set. <br>
		 * Returns a solution set that contains the solutions kept by the archive during the execution of the algorithm. <br>
		 * 
		 * @return result the resulting <code>SteadyStateOptimizationResult</code>.
		 * 
		 * @throws Exception
		 */
		public RegulatorySolutionSet run () throws Exception{		

			//archive
			if(archive==null)
				configureDefaultArchive();
			
			// evaluation function multi -> single objective aggregation policy
			if(aggregationFunction==null && !optimizationMethod.isMultiObjective()){ 
				aggregationFunction = new SimpleMultiplicativeAggregation();
				evaluationFunction.setFitnessAggregation(aggregationFunction);
			}
			
			// execute algorithm
			optimizationAlgorithm.run();
			
			
			// build the results container
			RegulatorySolutionSet result = new RegulatorySolutionSet(regulatoryConfiguration);
			
			ISolutionSet<IElementsRepresentation<?>> finalSolutionSet = archive.getArchive();
			//archive.printArchive();
			
			// rebuild simulations from solutions
			IntegratedSimulationControlCenter intSimulationCC = evaluationFunction.getSimulationControlCenter();
			
			
			for(ISolution<IElementsRepresentation<?>> solution : finalSolutionSet.getListOfSolutions()){
				

				RegulatoryGeneticConditions gc = (RegulatoryGeneticConditions) decoder.decode(solution.getRepresentation());

				intSimulationCC.setGeneticConditions(gc);
				
				// simulate phenotype
				IntegratedSimulationResult simResult = (IntegratedSimulationResult) intSimulationCC.simulate();
				
				
				RegulatorySolution regulatorySolution = new RegulatorySolution(simResult.getGeneticConditions());
				String simMethod = (simulationMethod.equals(IntegratedSimulationMethods.SRFBA)) ? simulationMethod : simulationMethod + " with " + metabolicSimulationMethod;
				
				regulatorySolution.addSimulationResultForMethod(simMethod, simResult);
				
				// retrieve fitnesses
				ArrayList<Double> fitnesses = new ArrayList<Double>();
				for(int i=0; i<solution.getNumberOfObjectives(); i++)
					fitnesses.add(i,solution.getFitnessValue(i));
				
				regulatorySolution.setAttributes(fitnesses);
				
				// add new optimization result
				result.addSolutionNoRepeat(regulatorySolution);
			}

			return result;
		}
		
		/**
		 * Computes a reference flux distribution for over/under expression problems. <br>
		 * 
		 * @param simulationMethod the method to be used in the computation of the flux distribution
		 * @param envCond the set of environmental conditions to be taken into account
		 * @param model the base model
		 * 
		 * @return <code>Map<String,Double></code> a map containing a <code>String</code> for the flux id and a <code>Double</code> for the respective flux value 
		 * 
		 * @throws OverUnderReferenceComputationException if any problem occurs when calculating the flux distribution
		 */
		public Map<String,Double> computeOverUnderReferenceDistribution(String simulationMethod,EnvironmentalConditions envCond, ISteadyStateModel model, String metabsimulationMethod, VariablesContainer variables, HashSet<String> falsenodes, SolverType solverType ) /*throws OverUnderReferenceComputationException*/{
			IntegratedSimulationControlCenter intSimCC = new IntegratedSimulationControlCenter(envCond, null, model, simulationMethod, metabsimulationMethod,regulatorynetowrksimulationmethod, variables, falsenodes, true,solverType);

			Map<String,Double> result = null;
			try {
				result = intSimCC.simulate().getFluxValues();
			} catch (Exception e) {
				e.printStackTrace();
//				throw new OverUnderReferenceComputationException("Problem computing over/under expression reference distribution while using method ["+simulationMethod+"]");
			}
			
			return result;
		}
		
		public void setModel(IIntegratedStedystateModel model) {
			this.model =  model;
		}

		public IIntegratedStedystateModel getModel() {
			return model;
		}
		
		
		public Object getSimulationProperty(String propertyKey)
		{
			return evaluationFunction.getSimulationProperty(propertyKey);
		}
		
		public void setSimulationProperty(String key, Object value)
		{
			evaluationFunction.setSimulationProperty(key, value);
		}
		
		public void setSimulationMethod (String simulationMethod)
		{
			this.evaluationFunction.setMethodType(simulationMethod);
		}
		
		public String getSimulationMethod()
		{
			return this.evaluationFunction.getMethodType();
		}

		
		public AlgorithmTypeEnum getOptimizationMethod() {
			return optimizationMethod;
		}

		public void setOptimizationMethod(AlgorithmTypeEnum optimizationMethod) {
			this.optimizationMethod = optimizationMethod;
		}

		/**
		 * @return the archive
		 */
		public ArchiveManager<?, IElementsRepresentation<?>> getArchive() {
			return archive;
		}

		/**
		 * @param archive the archive to set
		 */
		public void setArchive(ArchiveManager<?,IElementsRepresentation<?>> archive) {
			this.archive = archive;
		}

		/**
		 * @return the solver
		 */
		public SolverType getSolver() {
			return solver;
		}

		/**
		 * @param solver the solver to set
		 */
		public void setSolver(SolverType solver) {
			this.solver = solver;
		}
		
		
		public void setOverUnderReferenceDistribution(Map<String,Double> reference){
			evaluationFunction.setOverUnderReferenceDistribution(reference);
		}
		
		public IAlgorithm getOptimizationAlgorithm(){
			return optimizationAlgorithm;
		}
		
		public RegulatoryGeneKnockoutEvaluationFunction getEvaluationFunction(){
			return evaluationFunction;
		}

		/**
		 * @return the aggregationFunction
		 */
		public IAggregationFunction getAggregationFunction() {
			return aggregationFunction;
		}

		/**
		 * @param aggregationFunction the aggregationFunction to set
		 */
		public void setAggregationFunction(IAggregationFunction aggregationFunction) {
			this.aggregationFunction = aggregationFunction;
		}
		
		public ISteadyStateDecoder getdecoder(){
			return this.decoder;
		}
		
		protected void buildConfiguration(){
//			regulatoryConfiguration = new RegulatoryGenericConfiguration();
			regulatoryConfiguration.setModel(model);
			regulatoryConfiguration.setArchiveManager(archive);
			regulatoryConfiguration.setNotAllowedIds(notAllowedIDs);
			regulatoryConfiguration.setIsVariableSizeGenome(variableSize);
			regulatoryConfiguration.setMaxSetSize(maxSolutionSize);
			regulatoryConfiguration.setOptimizationAlgorithm(optimizationMethod.getShortName());
			regulatoryConfiguration.setRegulatorySimulationMethod(simulationMethod);
			
			simulationConfiguration = new HashMap<>();
			String simMethod = (simulationMethod.equals(IntegratedSimulationMethods.SRFBA)) ? simulationMethod : simulationMethod + " with " + metabolicSimulationMethod;
			
			simulationConfiguration.put(SimulationProperties.METHOD_ID, simMethod);
			simulationConfiguration.put(SimulationProperties.MODEL, model);
			simulationConfiguration.put(SimulationProperties.IS_MAXIMIZATION, true);
			simulationConfiguration.put(SimulationProperties.SOLVER, solver);
			simulationConfiguration.put(SimulationProperties.IS_OVERUNDER_SIMULATION, overUnder);
			simulationConfiguration.put(SimulationProperties.ENVIRONMENTAL_CONDITIONS, environmentalConditions);
			simulationConfiguration.put(SimulationProperties.OBJECTIVE_FUNCTION, getSimulationProperty(SimulationProperties.OBJECTIVE_FUNCTION));
			
			Map<String, Map<String, Object>> simulationConfig = new HashMap<>();
			simulationConfig.put(simMethod, simulationConfiguration);
			
			regulatoryConfiguration.setProperty(GenericOptimizationProperties.SIMULATION_CONFIGURATION, simulationConfig);
			
		}

		
	}

