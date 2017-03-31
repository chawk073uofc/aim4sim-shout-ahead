/**
 * 
 */
package aim4.ShoutAheadAI;

import java.util.ArrayList;

/**
 * A Generation is made up of a number of strategies (rule sets). 
 *  
 * @author Chris Hawk
 *
 */
public class Generation {
	private int numStrategiesPerGen = ShoutAheadSimSetup.getNumRoundsPerGeneration();
	private int generationIndex;
	private int currentStratIndex = 1;
	private ArrayList<RuleSet> strategies = new ArrayList<RuleSet>();
	
	/**
	 * No argument constructor to create the first generation of strategies by randomly generating rule sets.
	 *   
	 */
	public Generation(){
		//	TODO: allow selection of params for gen1, strat 1 only
		generationIndex = 1;
		LearningHarness.makeDirectory(generationPath());
		
		//for(currentStratIndex = 1; currentStratIndex <= numStrategiesPerGen; currentStratIndex++) {
			//create strategy1 dir
			LearningHarness.makeDirectory(generationPath() + currentStratPath());
			RuleSet strategy1 = new RuleSet();
			LearningHarness.writeToNewFile(generationPath() + currentStratPath() + "rulesBefore", strategy1.toString());
			//generate rule set
			strategies.add(new RuleSet());
			//create gen1/rulesBefore.xml
			//Serialize rule set 
		//}
			
		
	}
	/**
	 * Create a new generation of strategies from the previous generation. 
	 * @param previousGen the previous generation of strategies
	 */
	public void evolve(){
		generationIndex++;
	}
	private String currentStratPath() {
		return "strategy" + ((currentStratIndex < 10) ? "0" : "") + currentStratIndex + "/";
	}
	private String generationPath() {
		return "gen" + ((generationIndex < 10) ? "0" : "") + generationIndex + "/";
	}
}
