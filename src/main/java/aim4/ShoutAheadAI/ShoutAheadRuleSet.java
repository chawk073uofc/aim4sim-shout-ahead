package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Random;

/**
 * A rule set that does not allow predicates about the intended actions of other agents.
 * @author christopher.hawk
 *
 */
public class ShoutAheadRuleSet {
	private int numRules; 	//TODO: get numRules from config file set in param panel
	private ArrayList<ShoutAheadRule> rules = new ArrayList<ShoutAheadRule>();
	private Random rand = new Random();//for probabilistic selection of rules
	private double explorationFactor = 0.2;		// TODO get expFac from config file set by param panel


	/**
	 * Create a new random set of rules. 
	 */
	public ShoutAheadRuleSet(){

		for(int i = 0; i < numRules; i++){
			rules.add(new ShoutAheadRule());
		}
		
	}
	
	
	public ShoutAheadRule getRuleToFollow() {
		
		//TODO: add the higher-level decision function 
		  ShoutAheadRule ruleToFollow = null;

		 ArrayList<ShoutAheadRule> applicableRules = getApplicableRules(); //get set of rules having true conditions in this situation
		  if(applicableRules.isEmpty()){
			  //TODO: do nothing. no app rules exception 
		  }
		  else {
			  ArrayList<ShoutAheadRule> appRulesWithMaxWeight = getAppRulesWithMaxWeight(applicableRules);
			  ArrayList<ShoutAheadRule> otherAppRules = applicableRules;
			  otherAppRules.removeAll(appRulesWithMaxWeight);
			  if(shouldExplore()){
				  ruleToFollow = getRandOtherRule(otherAppRules);
			  }
			  else{
				  ruleToFollow = getRandMaxRule(appRulesWithMaxWeight);
			  }
		  }
		  
		  return ruleToFollow;
	}


	/**
	 * 	Get a set of rules having true conditions in the current situation.
	 * @return a set of rules having true conditions in the current situation.
	 */
	private ArrayList<ShoutAheadRule> getApplicableRules() {
		ArrayList<ShoutAheadRule> applicableRules = new ArrayList<ShoutAheadRule>();
		for(ShoutAheadRule rule: rules){
			if (rule.isApplicable())
				applicableRules.add(rule);
		}
		return applicableRules;
	}

	private ArrayList<ShoutAheadRule> getAppRulesWithMaxWeight(ArrayList<ShoutAheadRule> applicableRules) {
		ArrayList<ShoutAheadRule> appRulesWithMaxWeight = new ArrayList<ShoutAheadRule>();
		int maxWeight = getMaxAppRuleWeight(applicableRules);
		for(ShoutAheadRule appRule: applicableRules){
			if(appRule.getWeight() == maxWeight)
				appRulesWithMaxWeight.add(appRule);
		}
		return appRulesWithMaxWeight;
	}
	
	private int getMaxAppRuleWeight(ArrayList<ShoutAheadRule> applicableRules) {
		int maxWeight = Integer.MIN_VALUE;
		for(ShoutAheadRule rule: applicableRules){
			if(rule.getWeight() > maxWeight)
				maxWeight = rule.getWeight();
		}
		return maxWeight;
	}
	
	/**
	 * Determines if the agent should choose one of the applicable rules on maximal weight or one if it should "explore" by choosing one of the other applicable rules.
	 * The exploration factor determines with what probability one of the other rules should be explored. This function returns true with probability <code> explorationFactor </code>.
	 * 
	 * @return true if the agent will choose an applicable rule with non-maximal weight.
	 */
	private boolean shouldExplore() {
		return (rand.nextDouble() < explorationFactor);
	}
	
	/**
	 * From among all the rules that apply in the current situation and have the maximum weight, choose a random rule. 
	 * @param appRulesWithMaxWeight all the rules that apply in the current situation and have the maximum weight
	 * @return a random rule from among the list of rules given
	 */
	private ShoutAheadRule getRandMaxRule(ArrayList<ShoutAheadRule> appRulesWithMaxWeight) {
		int ruleIndex = rand.nextInt(appRulesWithMaxWeight.size() - 1);
		return appRulesWithMaxWeight.get(ruleIndex);
	}
	
	/**
	 * Choose a rule from among all those that apply in the current situation but have less than maximal weight. The probability of choosing a given 
	 * rule in this set is the weight associated with that rule divided by the sum of all the weights of all rules in this set. 
	 * @param otherAppRules
	 * @return a rule in the given set
	 */
	private ShoutAheadRule getRandOtherRule(ArrayList<ShoutAheadRule> otherAppRules) {
		//TODO: There must be a better way. 
		int totalWeight = getTotalWeight(otherAppRules);
		int randInt = rand.nextInt(totalWeight);
		int i = 0;
		for(ShoutAheadRule rule: otherAppRules){
			i += rule.getWeight();
			if(randInt < i)
				return rule;
		}
		System.out.println("Should have returned by now");
		return null;
	}
	
	/**
	 * Returns the total weight of all given rules.
	 * @param rules
	 * @return
	 */
	private int getTotalWeight(ArrayList<ShoutAheadRule> rules) {
		int totalWeight = 0;
		for(ShoutAheadRule rule: rules)
			totalWeight += rule.getWeight();
		return totalWeight;
	}
	
	
}
