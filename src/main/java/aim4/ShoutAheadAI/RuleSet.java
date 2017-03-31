package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Random;

import aim4.vehicle.AutoVehicleSimView;

/**
 * A rule set that allows predicates about the intended actions of other agents.
 * @author christopher.hawk
 *
 */
public class RuleSet {
	private int numRules = ShoutAheadSimSetup.getNumRulesPerRuleSet(); 
	private ArrayList<Rule> rules = new ArrayList<Rule>();
	private Random rand = new Random();//for probabilistic selection of rules
	private double explorationFactor = ShoutAheadSimSetup.getExplorationFactor();


	/**
	 * Create a new random set of rules. 
	 */
	public RuleSet(){

		for(int i = 0; i < numRules; i++){
			rules.add(new Rule());
		}
		
	}
	
	
	public Rule getRuleToFollow(AutoVehicleSimView vehicle) throws NoApplicableRulesException {
		
		//TODO: add the higher-level decision function 
		  Rule ruleToFollow = null;

		 ArrayList<Rule> applicableRules = getApplicableRules(vehicle); //get set of rules having true conditions in this situation
		  if(applicableRules.isEmpty()){
			  throw new NoApplicableRulesException(vehicle);
		  }
		  else {
			  ArrayList<Rule> appRulesWithMaxWeight = getAppRulesWithMaxWeight(applicableRules);
			  ArrayList<Rule> otherAppRules = applicableRules;
			  otherAppRules.removeAll(appRulesWithMaxWeight);
			  if(shouldExplore() && !otherAppRules.isEmpty()){
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
	 * @param vehicle TODO
	 * @return a set of rules having true conditions in the current situation.
	 */
	public ArrayList<Rule> getApplicableRules(AutoVehicleSimView vehicle) {
		ArrayList<Rule> applicableRules = new ArrayList<Rule>();
		for(Rule rule: rules){
			if (rule.isApplicable(vehicle))
				applicableRules.add(rule);
		}
		return applicableRules;
	}

	private ArrayList<Rule> getAppRulesWithMaxWeight(ArrayList<Rule> applicableRules) {
		ArrayList<Rule> appRulesWithMaxWeight = new ArrayList<Rule>();
		int maxWeight = getMaxAppRuleWeight(applicableRules);
		for(Rule appRule: applicableRules){
			if(appRule.getWeight() == maxWeight)
				appRulesWithMaxWeight.add(appRule);
		}
		return appRulesWithMaxWeight;
	}
	
	private int getMaxAppRuleWeight(ArrayList<Rule> applicableRules) {
		int maxWeight = Integer.MIN_VALUE;
		for(Rule rule: applicableRules){
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
	private Rule getRandMaxRule(ArrayList<Rule> appRulesWithMaxWeight) {
		int ruleIndex = rand.nextInt(appRulesWithMaxWeight.size());
		return appRulesWithMaxWeight.get(ruleIndex);
	}
	
	/**
	 * Choose a rule from among all those that apply in the current situation but have less than maximal weight. The probability of choosing a given 
	 * rule in this set is the weight associated with that rule divided by the sum of all the weights of all rules in this set. 
	 * @param otherAppRules
	 * @return a rule in the given set
	 */
	private Rule getRandOtherRule(ArrayList<Rule> otherAppRules) {
		//TODO: There must be a better way. what if total weight is non-positive?
		int totalWeight = getTotalWeight();
		int randInt = rand.nextInt(totalWeight);
		int i = 0;
		for(Rule rule: otherAppRules){
			i += rule.getWeight();
			if(randInt < i)
				return rule;
		}
		System.out.println("Should have returned by now");
		return null;
	}
	
	/**
	 * Returns the total weight of all given rules.
	 * @return
	 */
	private int getTotalWeight() {
		int totalWeight = 0;
		for(Rule rule: rules)
			totalWeight += rule.getWeight();
		return totalWeight;
	}
	
	@Override
	public String toString() {
		String str = "Rule Set:\n";
		for(Rule rule: rules)
			str += rule + "\n";
		return str;
	}
}
