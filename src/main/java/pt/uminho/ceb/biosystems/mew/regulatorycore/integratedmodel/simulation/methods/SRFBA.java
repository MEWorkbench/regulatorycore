package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.core.model.components.Gene;
import pt.uminho.ceb.biosystems.mew.core.model.components.GeneReactionRule;
import pt.uminho.ceb.biosystems.mew.core.model.components.ReactionConstraint;
import pt.uminho.ceb.biosystems.mew.core.model.exceptions.NonExistentIdException;
import pt.uminho.ceb.biosystems.mew.core.model.steadystatemodel.ISteadyStateModel;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.FluxValueMap;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneChangesList;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SimulationProperties;
import pt.uminho.ceb.biosystems.mew.core.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.abstractions.AbstractSSBasicSimulation;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.abstractions.VarTerm;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.abstractions.WrongFormulationException;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.ManagerExceptionUtils;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.MandatoryPropertyException;
import pt.uminho.ceb.biosystems.mew.core.simulation.formulations.exceptions.PropertyCastException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.components.VariablesContainer;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.model.IIntegratedStedystateModel;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.GeneregulatorychangesList;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatoryGeneticConditions;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.components.RegulatorySimulationProperties;
import pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.results.IntegratedSimulationResult;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPConstraintType;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPMapVariableValues;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPProblemRow;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPSolution;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPSolutionType;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPVariable;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPVariableType;
import pt.uminho.ceb.biosystems.mew.solvers.lp.MILPProblem;
import pt.uminho.ceb.biosystems.mew.solvers.lp.exceptions.LinearProgrammingTermAlreadyPresentException;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.MapStringNum;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTree;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTreeNode;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.And;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Not;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Or;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Variable;

public class SRFBA extends AbstractSSBasicSimulation<MILPProblem>{
	
	boolean debug = false;
	
	private VariablesContainer variablescontainer;
	private IndexedHashMap<String, Boolean> conditionVariablesState;
	private IndexedHashMap<String, Boolean> proteinVariablesState;
	private ArrayList<String> GeneKnockOuts;
	private IndexedHashMap<String, Integer> booleanreactionassociations = null;
	private IndexedHashMap<String, Integer> conditionsassociations =null;
	private IndexedHashMap<Integer, String> mapreggenestoregrules=null;
	
	private static String BOOLID="BV_";
	private static String BOOLNOT="XNot_";
	private static String BOOLOR="XOr_";
	private static String BOOLAND="XAnd_";
	private static String CVAR="XC_";
	private static double epsilon = 0.001;
	static private double INF = 100000000.0;

	

	public SRFBA(ISteadyStateModel model) {
		super(model);
		((IIntegratedStedystateModel)model).getRegulatoryNet().setUseForIntegratedSimulation(true);
		initSRFBAProsKeys();
		
	}
	
	
	
	private void initSRFBAProsKeys(){
		mandatoryProperties.add(RegulatorySimulationProperties.OBJECTIVE_FUNCTION);
		mandatoryProperties.add(RegulatorySimulationProperties.VARIABLES_CONTAINER);
		
		
		optionalProperties = new HashSet<String>();
		optionalProperties.add(RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS);

		optionalProperties.add(RegulatorySimulationProperties.ENVIRONMENTAL_CONDITIONS);
	}
	
	
	@Override
	public GeneticConditions getGeneticConditions() throws PropertyCastException, MandatoryPropertyException{
		
		RegulatoryGeneticConditions reggencond = (RegulatoryGeneticConditions) ManagerExceptionUtils.testCast(properties, RegulatoryGeneticConditions.class, RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS, true);
		return reggencond;
	}
	
	public boolean getIsMaximization() throws PropertyCastException, MandatoryPropertyException{
		return ManagerExceptionUtils.testCast(properties, Boolean.class, SimulationProperties.IS_MAXIMIZATION, false);
	}


	
	@Override
	public String getObjectiveFunctionToString() {
		String ret = "";
		boolean max = true;
		try {
			max = getIsMaximization();
		} catch (PropertyCastException e) {
			e.printStackTrace();
		} catch (MandatoryPropertyException e) {
			e.printStackTrace();
		}
		
		if(max)
			ret = "max:";
		else
			ret = "min:";
		Map<String, Double> obj_coef = getObjectiveFunction();
		for(String id : obj_coef.keySet()){
			double v = obj_coef.get(id);
			if(v!=1)
				ret += " " + v;
			ret +=  " " + id;
		}
		
		return ret;
	}
	
	/////////////////////////////////////////////////////////////////// Simulation and solution treatment methods ///////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public IntegratedSimulationResult simulate() throws PropertyCastException, MandatoryPropertyException  {

		
		LPSolution solution=null;
		try {
			solution = simulateProblem();
		
			
		} catch (Exception e) {
//			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		IntegratedSimulationResult result = convertSolutionToIntegratedSimulationResult(solution);

		
		return result;
	}
	
	
	public IntegratedSimulationResult convertSolutionToIntegratedSimulationResult(LPSolution solution) throws PropertyCastException, MandatoryPropertyException {
	
		// Initiate IntegratedSimulationResult  
		FluxValueMap fluxValues = getFluxValueListFromLPSolution(solution);
		IntegratedSimulationResult res = new IntegratedSimulationResult( model, "SRFBA", fluxValues);
	      
	    // add additional parameter
		if(solution ==null){
			res.setSolutionType(LPSolutionType.ERROR);
			String solverout = "The solver " + getSolverType() + " cannot generate an output..." ;
			res.setSolverOutput(solverout);
			res.setOFvalue(Double.NaN);
		}
		else{
	    putMetaboliteExtraInfo(solution,res);
	    putReactionExtraInfo(solution,res);
		res.setSolutionType(solution.getSolutionType());
		res.setSolverOutput(solution.getSolverOutput());
		res.setOFvalue(solution.getOfValue());
	
		try {
			res.setGeneticConditions(newRegulatoryGeneticConditions(solution));
			res.setReactionsKnockouts(getNameReactionsKnockouts(getKnockoutsByIDx(solution,getIndexarrayValuesFromIndexHasmap(this.booleanreactionassociations))));
			res.setRegulatoryAtractor(buildAtractor(solution));
			res.setOFString(getObjectiveFunctionToString());
			res.setFinalvariablesContaineraftersimulation(getNewVariablesContainer(solution));
			res.setInitialUsedVariablesContainer(variablescontainer);
			
			res.setUsedfalsenodes(null);
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		res.setEnvironmentalConditions(getEnvironmentalConditions());
		res.setOldRegulatoryGeneticConditions((RegulatoryGeneticConditions) getGeneticConditions());
	   
	
		
		return res;
	}
	
	
	
	
	
	
	
	//############################# Support methods to build the IntegratedSimulationResult ######################## 
	
	protected void putReactionExtraInfo(LPSolution solution,SteadyStateSimulationResult res) {
		Map<String, MapStringNum> complementary = new HashMap<String, MapStringNum>();
		
		for(String id: solution.getVariableMetricsIds()){
			
			LPMapVariableValues cInfo =  solution.getPerVariableMetric(id);
			MapStringNum values = convertLPMapToMapString(model, cInfo, true);

			complementary.put(id, values);
		}
		
		res.setComplementaryInfoReactions(complementary);
		
	}
	

	
	protected ArrayList<ArrayList<Boolean>> buildAtractor(LPSolution solution){
		
		ArrayList<Boolean> regGenesKnock = new ArrayList<Boolean>();
		ArrayList<ArrayList<Boolean>> atractor = new ArrayList<ArrayList<Boolean>>();
		
		for(int i =0; i < ((IIntegratedStedystateModel)model).getRegulatoryNet().getNumberOfGenes(); i++)
			regGenesKnock.add(true);
		
		ArrayList<String> genesknockout = getNameGeneKnockouts(getKnockoutsByIDxMap(solution,this.mapreggenestoregrules));

		
		for (int i = 0; i < genesknockout.size(); i++) {
			int pos = ((IIntegratedStedystateModel)model).getRegulatoryNet().getGeneIndex(genesknockout.get(i));

			regGenesKnock.set(pos, false);
		}
		
		atractor.add(regGenesKnock);
		
		
		return atractor;
	}
	
	
	private RegulatoryGeneticConditions newRegulatoryGeneticConditions(LPSolution solution) throws Exception{
		
		GeneChangesList metabolicGenesKnockout = new GeneChangesList();
		GeneregulatorychangesList regulatoryGenesKnockout = new GeneregulatorychangesList();
		
		ArrayList<String> totalgeneknockoutsNames = getNameGeneKnockouts(getKnockoutsByIDxMap(solution, this.mapreggenestoregrules));
		
		for (String gene : totalgeneknockoutsNames) {
			if(((IIntegratedStedystateModel) model).isMetabolicGene(gene)){
				metabolicGenesKnockout.addGeneKnockout(gene);
			}
			else if (((IIntegratedStedystateModel) model).isRegulatoryGene(gene)){
			   regulatoryGenesKnockout.addGeneKnockout(gene);}
		   else 
			   throw new Exception();
			   	   
		}
		
		RegulatoryGeneticConditions newRegGeneCond = new RegulatoryGeneticConditions(regulatoryGenesKnockout, metabolicGenesKnockout, (IIntegratedStedystateModel)model, false);

		return newRegGeneCond;
	}
	
	
	
	
	private ArrayList<Integer> getKnockoutsByIDx(LPSolution solution, int [] ArrayOfID){
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		LPMapVariableValues varValueList = null;
		if(solution != null)
			varValueList = solution.getValues();
		
		for (int i = 0; i < ArrayOfID.length; i++) {

			double value = varValueList.get(ArrayOfID[i]);
		    long roudedValue = Math.round(value);
		    if(roudedValue == 0){
		    	res.add(i);
		    }
		}
		
		return res;
		
	}
	
   private ArrayList<Integer> getKnockoutsByIDxMap(LPSolution solution, IndexedHashMap<Integer, String> mapids){
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		LPMapVariableValues varValueList = null;
		if(solution != null){
			varValueList = solution.getValues();
			/*for (Map.Entry<Integer,Double> r : varValueList.entrySet()) {
				System.out.println(r.getKey()+"  "+r.getValue());
			}*/
			
		}
		
		for (int i = 0; i < mapids.size(); i++) {
	
			double value = varValueList.get(mapids.getKeyAt(i));
			//System.out.println(value+" "+mapids.getKeyAt(i));
		    long roudedValue = Math.round(value);
		    if(roudedValue == 0){
		    	res.add(i);
		    }
		}
		
		return res;
		
	}
	

	
	private int[] getIndexarrayValuesFromIndexHasmap(IndexedHashMap<String, Integer> map){
		
		int[] arrayvalues = new int[map.size()];
		
		for (int i = 0; i < arrayvalues.length; i++) {
			arrayvalues[i]= map.getValueAt(i);
		}
		return arrayvalues;
		
	}
	

	private ArrayList<String> getNameGeneKnockouts(ArrayList<Integer> res){
		
		ArrayList<String> genesout = new ArrayList<String>();
		ArrayList<String> genes = ((IIntegratedStedystateModel) model).getGenesInRegulatoryModel();
		for (int i = 0; i < res.size(); i++) {
			genesout.add(genes.get(res.get(i)));
		}

		return genesout;
	}
	
	
    private ArrayList<String> getNameReactionsKnockouts(ArrayList<Integer> res){
		
		ArrayList<String> reactionsout = new ArrayList<String>();
		for (int i = 0; i < res.size(); i++) {
			reactionsout.add(((IIntegratedStedystateModel) model).getReactionId(res.get(i)));
		}
		
		return reactionsout;
	}
	

	private VariablesContainer getNewVariablesContainer(LPSolution solution){
		
		VariablesContainer container = (VariablesContainer) ((IIntegratedStedystateModel)model).getVariablescontainer().clone();
		
		if(this.conditionsassociations!=null){
		int[] conditionsAssociations = getIndexarrayValuesFromIndexHasmap(this.conditionsassociations);
		
		try {
		for(int i =0; i < conditionsAssociations.length; i++){
			int idx = container.getLinkedVariableIndex(i);
			if(conditionsAssociations[i] >=0){
				double value = solution.getValues().get(conditionsAssociations[i]);
				long roudedValue = Math.round(value);
				
				if(roudedValue == 1){
					container.setVariableStatebyIDX(idx, true);
				
				}
			}else if(conditionsAssociations[i] == -1){
				container.setVariableStatebyIDX(idx, true);
			}else if(conditionsAssociations[i] == -2){
				container.setVariableStatebyIDX(idx, false);
			} else
				throw new Exception();
				} 
		}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return container;
		
	}
	
	

	///////////////////////////////////////////////// Get necessary input information, variables, gene knockouts etc../////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	   private void setVariables(){

		
		VariablesContainer vars = null;
		
		try{
			
			vars = (VariablesContainer)ManagerExceptionUtils.testCast(properties, VariablesContainer.class, RegulatorySimulationProperties.VARIABLES_CONTAINER, false);
			if(vars!=null){
				this.variablescontainer=vars;
				this.conditionVariablesState=vars.getUserVariablesState();
				this.proteinVariablesState=vars.getTFsVariablesState();
		
			  }

		    } catch (PropertyCastException e) {
			System.err.println("The property " + e.getLocalizedMessage() + " was ignored!!\n Reason: " + e.getMessage());
		    } catch (MandatoryPropertyException e) {} 
  
		}
	
	   
		private void setAllGeneKnockouts(){
			
			
			RegulatoryGeneticConditions geneCond = null;
			try {				
				geneCond =(RegulatoryGeneticConditions) ManagerExceptionUtils.testCast(properties, RegulatoryGeneticConditions.class, RegulatorySimulationProperties.REGULATORY_GENETIC_CONDITIONS, true);

			
			} catch (PropertyCastException e) {
				
				e.printStackTrace();
			} catch (MandatoryPropertyException e) {
				
				e.printStackTrace();
			}
			if (geneCond!=null){
				this.GeneKnockOuts=geneCond.getALLGeneKnockoutList();	
					
				}
          
		}
	


	
	//////////////////////////////////////////////////// Build Initial Milp Problem and define objective function ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public MILPProblem constructEmptyProblem() {
		
		return new MILPProblem();
	}
	
	
	 @SuppressWarnings("unchecked")
     public Map<String, Double> getObjectiveFunction(){
	   Map<String, Double> obj_coef = null;
	   try {
		 obj_coef = ManagerExceptionUtils.testCast(properties, Map.class, RegulatorySimulationProperties.OBJECTIVE_FUNCTION, false);
		    } catch (Exception e) {
			obj_coef = new HashMap<String, Double>();
			obj_coef.put(((IIntegratedStedystateModel) model).getBiomassFlux(), 1.0);
			}
			return obj_coef;
		   }
	
	

	@Override
	protected void createObjectiveFunction() throws WrongFormulationException,PropertyCastException, MandatoryPropertyException {
        problem.setObjectiveFunction(new LPProblemRow(), getIsMaximization());
		
		Map<String, Double> obj_coef = getObjectiveFunction();
		for (String r : obj_coef.keySet()) {
			double coef = obj_coef.get(r);
			objTerms.add(new VarTerm(getIdToIndexVarMapings().get(r), coef, 0.0));
		}
	}
	
	

		
	//////////////////////////////////////// set information in MILP Problem ////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	private void addBooleanVarToMILP(String namevar){
		LPVariable binaryvar= new LPVariable(namevar,0,1, LPVariableType.BINARY);
		problem.addVariable(binaryvar);
	}

	
	

	
	private void addEquationToMILP(int[] colIdx, double[] colVal, double lowerB, double upperB) throws LinearProgrammingTermAlreadyPresentException{
		LPProblemRow left = new LPProblemRow();
		
		for(int i =0; i < colIdx.length; i++){
			left.addTerm(colIdx[i], colVal[i]);
		}

		problem.addConstraint(left, LPConstraintType.LESS_THAN, upperB);
		problem.addConstraint(left,LPConstraintType.GREATER_THAN,lowerB);	
	}
	

	
	private int getlastVariableindex(){
		return this.indexToIdVarMapings.size();
	}
	
	
	
	private  int checkUserVarState(String var) throws PropertyCastException, MandatoryPropertyException{
		int ret = -2;
		
        if(conditionVariablesState.containsKey(var)){
        	// if true state
	         if (conditionVariablesState.get(var)){
			    ret = -1;
	         }
        }
        else if(proteinVariablesState.containsKey(var)){
        	// if true state
        	if(proteinVariablesState.get(var)){
        		
		        ret=-1;
        	}
	    }
      
		return ret;
	}
	
	
	
	@Override
	protected void createVariables() throws PropertyCastException, MandatoryPropertyException, WrongFormulationException {
		super.createVariables();
		setAllGeneKnockouts();
		setVariables();
		
		int nlinkedvars = variablescontainer.getNumberLinkedVariables();
		if(nlinkedvars>0)
		 this.conditionsassociations=new IndexedHashMap<>(nlinkedvars);
		geneVariables();
		reactionVariables();
		
		
	}
	
	@Override
	protected void createConstraints() throws WrongFormulationException, PropertyCastException, MandatoryPropertyException{
		super.createConstraints();
		
		
		try {
			genesregulatoryequations();
			gprsequations();
			reaction_bounds_equations();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	
	private void geneVariables(){
		
		try{
		int ngenesregmodel = ((IIntegratedStedystateModel) model).getRegulatoryNet().getNumberOfGenes();

		mapreggenestoregrules= new IndexedHashMap<>(ngenesregmodel);
	
		
		   for (int i = 0; i < ngenesregmodel; i++) {
			   String boolgeneid = BOOLID+((IIntegratedStedystateModel) model).getRegulatoryNet().getGene(i).getId();
			   addBooleanVarToMILP(boolgeneid);
			   int lastvarindex=getlastVariableindex();
			   mapreggenestoregrules.putAt(i, lastvarindex, boolgeneid);
			   putVarMappings(boolgeneid,lastvarindex);

		    }
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} 

	}
	
	
	private void reactionVariables(){
		
		try{
		int nreactions = ((IIntegratedStedystateModel) model).getNumberOfReactions();
		this.booleanreactionassociations = new IndexedHashMap<>(nreactions);	
		
		
		for (int i = 0; i < nreactions; i++) {
			
			String boolreactid= BOOLID+((IIntegratedStedystateModel) model).getReactionId(i);

			addBooleanVarToMILP(boolreactid);
			int lastvarindex=getlastVariableindex();
			this.booleanreactionassociations.put(boolreactid, lastvarindex);
			putVarMappings(boolreactid,lastvarindex);
			
		}
		
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void genesregulatoryequations(){
		
		try{

		int ngenesregmodel = ((IIntegratedStedystateModel) model).getRegulatoryNet().getNumberOfGenes();

		IndexedHashMap<String, Gene> regGenes = ((IIntegratedStedystateModel) model).getRegulatoryNet().getRegulatoryGenes();
	
		
		for (int i = 0; i < ngenesregmodel; i++) {
			
			int elementIndex = mapreggenestoregrules.getKeyAt(i);

			int index = -3;

				
				AbstractSyntaxTree<DataTypeEnum, IValue> generule =((IIntegratedStedystateModel) model).getRegulatoryNet().getRegulatoryRule(i).getBooleanRule();
				index= convertRulesToEquations(generule);
				
				if(this.GeneKnockOuts!=null){
					if(this.GeneKnockOuts.contains(regGenes.getKeyAt(i)))
					index=-2;
				}

		       convertValueRuleToMilpRule(index, elementIndex);
		 }

	  } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
     protected void gprsequations() throws LinearProgrammingTermAlreadyPresentException, NonExistentIdException, Exception{
   
    	 
		HashMap<String, GeneReactionRule> rules = ((IIntegratedStedystateModel) model).getGeneReactionRules();
		int nboolreactassoc = this.booleanreactionassociations.size();

		
		for(int i =0; i< nboolreactassoc;i++){
			int elementIndex = this.booleanreactionassociations.getValueAt(i);

			GeneReactionRule rule = rules.get(model.getReactionId(i));

			int index = -3;
			if(rule != null)
				index = convertRulesToEquations(rule.getRule());

			convertValueRuleToMilpRule(index, elementIndex);
		}
		
	}
	
	
	
	
	
	
	@SuppressWarnings("unused")
	private void reaction_bounds_equations() throws LinearProgrammingTermAlreadyPresentException, PropertyCastException, MandatoryPropertyException{
		
		int nreactions = ((IIntegratedStedystateModel) model).getNumberOfReactions();
		
		for(int i =0; i < nreactions; i++){
			String namereaction = ((IIntegratedStedystateModel) model).getReactionId(i);
			
			double vMin = getReactionsConstraint(namereaction).getLowerLimit();
			double vMax = getReactionsConstraint(namereaction).getUpperLimit();
   
			int[] colIdxs = {i,this.booleanreactionassociations.getValueAt(i)};
	
			double[] colVals1 = {1, -vMax};
			addEquationToMILP(colIdxs, colVals1, -INF, 0);
			
			double[] colVals2 ={1,-vMin};

			addEquationToMILP(colIdxs, colVals2, 0, INF);
			
			
		}
	}
	
	

	private void convertValueRuleToMilpRule(int valueRule, int variable) throws LinearProgrammingTermAlreadyPresentException{
		if(valueRule == -1){
			int[] colIdxs = {variable};
			double[] colVals = {1};
			addEquationToMILP(colIdxs, colVals, 1.0, 1.0);
			
		}else if(valueRule == -2){
			int[] colIdxs = {variable};
			double[] colVals = {1};
			addEquationToMILP(colIdxs, colVals, 0, 0);
			
		}else if(valueRule == -3){
			int[] colIdxs = {variable};
			double[] colVals = {1};
			addEquationToMILP(colIdxs, colVals, 0, 1.0);
			
		}else{
			int[] colIdxs = {variable, valueRule};
			double[] colVals = {1, -1};
			
			addEquationToMILP(colIdxs, colVals, 0, 0);
			
		}
		
	}
	
	private int convertRulesToEquations(AbstractSyntaxTree<DataTypeEnum, IValue> rule) throws LinearProgrammingTermAlreadyPresentException, NonExistentIdException, Exception{
		
		AbstractSyntaxTreeNode<DataTypeEnum, IValue> root = rule.getRootNode();
		ArrayList<Integer> ruleDS_indices = new ArrayList<Integer>();
		Integer ret = null;
		
		ArrayList<String> booloperators = null;
		
		if(root!=null){
			booloperators= new ArrayList<>();
			convertVariablesRules(root,booloperators,ruleDS_indices);
			ret = ruleDS_indices.get(0);
		}
		else{
			ret = -3;
		}
       
		return ret;
	}
	
	
	protected void convertVariablesRules(AbstractSyntaxTreeNode<DataTypeEnum, IValue> node, ArrayList<String> variables, ArrayList<Integer> ruleDS_indices) throws LinearProgrammingTermAlreadyPresentException, NonExistentIdException, Exception {

		if(node!=null){
			for(int i =0 ; i < node.getNumberOfChildren(); i++){
				convertVariablesRules(node.getChildAt(i), variables,ruleDS_indices);
			
			}
			
			if(node instanceof Variable){

				String var = node.toString();
				
			
				if(var.contains("<") || var.contains(">")){		
					if(variablescontainer.isUserVarible(var)){
						ruleDS_indices.add(checkUserVarState(var));
						
					}
				else{
						int val = convertLinkedReactionsVariables(var);
		                this.conditionsassociations.put(var, val);
						ruleDS_indices.add(val);

				    }
				}
				
				else {

					int RegRuleIndex=((IIntegratedStedystateModel) model).getRegulatoryNet().getComponentRuleIndex(var);

					int RegGeneIndex=((IIntegratedStedystateModel) model).getRegulatoryNet().getRegulatoryGeneIndex(var);

					if(RegRuleIndex!=-1){
						ruleDS_indices.add(mapreggenestoregrules.getKeyAt(RegRuleIndex));
					}
					else if(RegGeneIndex!=-1){
						ruleDS_indices.add(mapreggenestoregrules.getKeyAt(RegGeneIndex));
					}else{
						
						ruleDS_indices.add(checkUserVarState(var));
					}
					
				}
				
			}
			else if(node instanceof Not){

				variables.add("NOT");
				int lastIndex = ruleDS_indices.size();
				int entry = ruleDS_indices.get(lastIndex-1);
				ruleDS_indices.remove(lastIndex-1);
				
				ruleDS_indices.add(convertNotOperator(entry));
			}
			else if(node instanceof Or){
	
				variables.add("OR");
				int lastIndex = ruleDS_indices.size();
				int rightEntryValue = ruleDS_indices.get(lastIndex-1);
				int leftEntryValue = ruleDS_indices.get(lastIndex-2);
				
				ruleDS_indices.remove(lastIndex-1);
				ruleDS_indices.remove(lastIndex-2);
				
				
				ruleDS_indices.add(convertOrOperator(leftEntryValue, rightEntryValue));
			}
			else if(node instanceof And){
	
				variables.add("AND");
				int lastIndex = ruleDS_indices.size();
				int rightEntryValue = ruleDS_indices.get(lastIndex-1);
				
				int leftEntryValue = ruleDS_indices.get(lastIndex-2);
			
				
				ruleDS_indices.remove(lastIndex-1);
				ruleDS_indices.remove(lastIndex-2);
				
				ruleDS_indices.add(convertAndOperator(leftEntryValue, rightEntryValue));
			}

		}

	}
	
	
	
	 protected int convertLinkedReactionsVariables(String var) throws LinearProgrammingTermAlreadyPresentException, NonExistentIdException, Exception{
			
		  
			int ret = -3;

			if(var.contains("<")){
				 // Here I translate the meaning of constarains from the GLPK equations to constrains in the meaing of the 
				 //regulatory rules. 
	             // This means that if the rule asked if (flux < val), the real question is if (flux > -val).
	             // Two equations that are added (view document 'How to build the MIP problem.doc'):
	             //       1.	c * (const - Vmax) + v <= const
	             //       2.	Vmin <= c * (Vmin - const) + v
				
				String[] data = var.split("<");

				
				double vMax = getReactionsConstraint(data[0]).getUpperLimit(); 
				double vMin = getReactionsConstraint(data[0]).getLowerLimit(); 

				double c = Double.parseDouble(data[1]); 
				double compareVal = -c + epsilon;
				
		
				if(compareVal < vMin){
					ret = -1;
				}else if(compareVal >= vMax){
					ret = -2;	
				}

				else{
					//Add one more variable 'c' of the equations

					int lastvaridx = problem.getNumberVariables();
					String cvar = CVAR+lastvaridx;
					putVarMappings(cvar, lastvaridx);
					addBooleanVarToMILP(cvar);
					
					int reactionindex= ((IIntegratedStedystateModel) model).getReactionIndex(data[0]);
					
					int[] colIdx = {lastvaridx, reactionindex};
					double [] colVal1={(compareVal-vMax), 1};
					
					addEquationToMILP(colIdx, colVal1, -INF, compareVal);
					
					
					double [] colVal2={(vMin-compareVal), 1};
					
					addEquationToMILP(colIdx, colVal2, vMin, INF);
					
					
					ret = lastvaridx;

				}
				
			}
			else if (var.contains(">")){
				//This means that if the rule asked if (flux > val), the real question is if (flux < -val).
	            //Two equations that are added (view document 'How to build the MIP problem.doc'):
	           //       1.	c * (Vmax - const) + v <= Vmax
	           //       2.	const <= c * (const - Vmin) + v
				
				String[] data = var.split(">");

				double vMax = getReactionsConstraint(data[0]).getUpperLimit();
				double vMin = getReactionsConstraint(data[0]).getLowerLimit();
				
				double c = Double.parseDouble(data[1]);
				double compareVal = -c-epsilon;
					
				if(compareVal<=vMin)
						ret = -2;
				else if(vMax<compareVal)
						ret = -1;
				else{
					    
					    int lastvaridx = problem.getNumberVariables();
						String cvar = CVAR+lastvaridx;
						putVarMappings(cvar, lastvaridx);
						addBooleanVarToMILP(cvar);
						
						int reactionindex= ((IIntegratedStedystateModel) model).getReactionIndex(data[0]);
						
						int[] colIdx = {lastvaridx, reactionindex};
						double [] colVal1={(vMax-compareVal), 1};
						
						addEquationToMILP(colIdx, colVal1, -INF, vMax);
						
						
						double [] colVal2={(compareVal-vMin), 1};
						
						addEquationToMILP(colIdx, colVal2, compareVal, INF);
						

						ret = lastvaridx;
				}
			}
			
			return ret; 
		}
	
	
	protected int convertNotOperator(int entryNum) throws Exception{
		int ret = -3;
		
		if(entryNum == -2)
			ret = -1;
		else if(entryNum==-1)
			ret=-2;
		else{
			int numvar = getCurrentNumOfVar();
			String notvar = BOOLNOT+numvar;
			putVarMappings(notvar, numvar);
			addBooleanVarToMILP(notvar);
			
			// a = not (b) is converted to  (b + a = 1)
			int[] colIdxs = {entryNum,numvar};
			double[] colVals = {1,1};
			addEquationToMILP(colIdxs, colVals, 1, 1);
			
			ret = numvar;
		}
		return ret;
	}
	
	
	protected int convertOrOperator(int leftEntryValue, int rightEntryValue) throws Exception{
		int ret = -3;

		if(rightEntryValue==-1 || leftEntryValue==-1)
			ret = -1;
		else if(rightEntryValue==-2 && leftEntryValue==-2)
			ret = -2;
		else{
			if(leftEntryValue == -2)
				ret = rightEntryValue;
			else if(rightEntryValue == -2)
				ret = leftEntryValue;
			else{
				//a = (b or c) is converted to (-2 <= 2*b + 2*c - 4*a <= 1)
				
				int numvar = getCurrentNumOfVar();
				String orvar = BOOLOR+numvar;
				putVarMappings(orvar, numvar);
				addBooleanVarToMILP(orvar);
				
				int[] colIdxs = {leftEntryValue,rightEntryValue, numvar};
				double[] colVals = {2,2,-4};
				addEquationToMILP(colIdxs, colVals, -2, 1);
				
				ret = numvar;
			}
				
		}
		return ret;
	}
	
	
	protected int convertAndOperator(int leftEntryValue, int rightEntryValue) throws LinearProgrammingTermAlreadyPresentException, Exception{
		int ret = -3;

		if(rightEntryValue==-2 || leftEntryValue==-2)
			ret = -2;
		else if(rightEntryValue==-1 && leftEntryValue==-1)
			ret = -1;
		else{
			if(leftEntryValue == -1)
				ret = rightEntryValue;
			else if(rightEntryValue == -1)
				ret = leftEntryValue;
			else{
				//a = (b and c) is converted to (-1 <= 2*b + 2*c + 4*a <= 3)
				
				int numvar = getCurrentNumOfVar();
				String andvar = BOOLAND+numvar;
				putVarMappings(andvar, numvar);
				addBooleanVarToMILP(andvar);
				
				int[] colIdxs = {leftEntryValue,rightEntryValue, numvar};
				double[] colVals = {2,2,-4};
				addEquationToMILP(colIdxs, colVals, -1, 3);
				
				ret = numvar;
			}
				
		}
		return ret;
	}
	
	
	private ReactionConstraint getReactionsConstraint(String reactionId) throws PropertyCastException, MandatoryPropertyException{
         return model.getReactionConstraint(reactionId);
	}


}
