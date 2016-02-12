package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.simulation.formulations;

import java.io.Serializable;
import java.util.ArrayList;



public class NetworkMemory implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The size of the memory. The number of steps that the memory can hold. */
	private static final int LOOKBACK = 10;
	/** <code>boolean</code> matrix implementation of the memory for CPU performance and VM memory saving. */
	private ArrayList<ArrayList<Boolean>> memory;
	/** The remaining capacity of the memory at any given time. */
	private int capacity = LOOKBACK;
	/** The length of each network stored in memory.  */
	private int net_length;
	/** The total number of iterations executed over the memory. */
	private int iterations;
	
	/**
	 * @param sim the <code>BooleanNetworkSimulation</code> that contains the network to be queued. 
	 */
	public NetworkMemory(ArrayList<Boolean> firstStep){
		memory = new ArrayList<ArrayList<Boolean>>();
		memory.add(firstStep);
		net_length = firstStep.size();
	}

	/**
	 * @return the lOOKBACK value for this queue.
	 */
	public static int getLOOKBACK() {
		return LOOKBACK;
	}
	
	/**
	 * Inserts a new set of values at the top of the queue.
	 * 
	 * @param n the new set of values
	 */
	public void push(ArrayList<Boolean> exp){
		if(exp.size() != net_length)
			System.out.println("erro!!");
		//TODO: criar exp
		
		
		
		if(capacity==0)
			memory.remove(memory.size()-1);
		
		memory.add(null);
		for(int i = memory.size()-2; i >= 0; i--){
			ArrayList<Boolean> expAux = memory.get(i);
			memory.set(i+1, expAux);
		}
		memory.set(0, exp);
		
		if(capacity>0) capacity--;
	}
	
	/**
	 * @return true if the queue is full, false otherwise.
	 */
	public boolean isFull(){
		return capacity == 0;
	}
	
	/**
	 * @return the set of values at the bottom of the queue.
	 */
	public ArrayList<Boolean> lookTop(){	
		ArrayList<Boolean> toret =memory.get(0);
		return toret;		                     
	}
	
	/**
	 * Search for repeated sets of values within the queue.
	 * 
	 * @return i the length of the detected cycle or -1 if no cycles detected.
	 */
	public int checkRedundancy() {
		ArrayList<Boolean> fist = memory.get(0);
		for(int i = 1;i<memory.size();i++){
			if(fist.equals(memory.get(i))){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Comparison between two arrays of <code>booleans</code>.
	 * 
	 * @param first the first array of booleans.
	 * @param sec the second array of booleans.
	 * @return true if both arrays are equal, false otherwise.
	 */
//	public boolean boolArrayequals(ArrayList first,boolean[] sec){
//		if(first.length!=sec.length)
//			return false;
//		for(int i=0;i<first.length;i++)
//			if(first[i]!=sec[i])
//				return false;
//		
//		return true;
//	}

	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}

	/**
	 * @param iterations the iterations to set
	 */
	public void addIteration() {
		this.iterations++;
	}
	
	/**
	 * Clears the <code>NetworkMemory</code>.
	 * 
	 * @return the free memory in the JVM.
	 */
//	public long clearMemory(){
//		memory = null;
//		System.gc(); //NOTE: Force the garbage collector to run now.		 
//		memory = new boolean[LOOKBACK][net_length];
//		iterations = 0;
//		capacity = LOOKBACK;
//		return Runtime.getRuntime().freeMemory(); //NOTE: calculate the total amount of free memory in the JVM... maybe unnecessary...
//	}
	
	public int size(){
		return memory.size();
	}
	
	public ArrayList<ArrayList<Boolean>> getCycle(){
		ArrayList<ArrayList<Boolean>> ret = new ArrayList<ArrayList<Boolean>>();
		int cycleLenght = checkRedundancy();
		if(cycleLenght <= 0)
			ret.add(memory.get(0));
		else
			for(int i = 0; i < cycleLenght; i++){
				ret.add(memory.get(i));
			}
		
		return ret;
	}
	
	public ArrayList<ArrayList<Boolean>> getLastNSteps(int n){
		ArrayList<ArrayList<Boolean>> ret = new ArrayList<ArrayList<Boolean>>();
		for(int i = 0; i < n ;i++){
			ret.add(memory.get(i));
		}
		return ret;
	}

	public String toString(){
		String text = "";
		for(int i = 0; i < memory.size(); i++){
			text += memory.get(i).toString()+ "\n";
		}
		return text;
	}
}


