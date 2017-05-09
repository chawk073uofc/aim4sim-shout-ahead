/**
 * 
 */
package aim4.ShoutAheadAI;

import java.util.ArrayList;

import aim4.vehicle.AutoVehicleSimView;

/**
 * @author Chris Hawk
 * 
 * Contains the two rule sets that define the behavior of cars in a given simulation run.
 */
public class Strategy implements Comparable<Strategy> {
	private RuleSet ruleSet;
	private ShoutAheadRuleSet shoutAheadRuleSet;
	private double fitness;
	
	/**
	 * Create a new randomly generated strategy. 
	 */
	public Strategy() {
		ruleSet = new RuleSet();
		shoutAheadRuleSet = new ShoutAheadRuleSet();
	}
	
	public static Strategy getRandomStrategy(){
		return new Strategy();
	}
	/**
	 * Create a new strategy by breeding two existing strategies. 
	 * @param strat1
	 * @param strat2
	 */
	public Strategy(Strategy parent1, Strategy parent2) {
		ruleSet = new RuleSet(parent1.getRuleSet(), parent2.getRuleSet());
		shoutAheadRuleSet = new ShoutAheadRuleSet(parent1.getShoutAheadRuleSet(), parent2.getShoutAheadRuleSet());
	}
	public Rule getPart1RuleToFollow(AutoVehicleSimView vehicle) throws NoApplicableRulesException {
		return ruleSet.getRuleToFollow(vehicle);
	}
	
	public ShoutAheadRule getPart2RuleToFollow(AutoVehicleSimView vehicle) throws NoApplicableRulesException {
		return (ShoutAheadRule) shoutAheadRuleSet.getRuleToFollow(vehicle);
	}

	/**
	 * @return the ruleSet
	 */
	public RuleSet getRuleSet() {
		return ruleSet;
	}
	/**
	 * @return the shoutAheadRuleSet
	 */
	public ShoutAheadRuleSet getShoutAheadRuleSet() {
		return shoutAheadRuleSet;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "Strategy for current simulation:\n\n";
			   str += ruleSet;
			   str += shoutAheadRuleSet;
		return str;
	}

	@Override
	public int compareTo(Strategy strat) {
		if(fitness > strat.fitness) return 1;
		if(fitness < strat.fitness) return -1;
		return 0;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * @return the fitness
	 */
	public double getFitness() {
		return fitness;
	}
}


