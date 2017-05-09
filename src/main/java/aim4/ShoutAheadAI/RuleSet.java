package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import aim4.ShoutAheadAI.ShoutAheadSimSetup;
import aim4.config.Constants;
import aim4.vehicle.AutoVehicleSimView;

/**
 * A rule set that allows predicates about the intended actions of other agents.
 * @author christopher.hawk
 *
 */
public class RuleSet {
	protected int numRules = ShoutAheadSimSetup.getNumRulesPerRuleSet(); 
	private HashSet<Rule> rules = new HashSet<Rule>();// no duplicates
	protected Random rand = new Random();//for probabilistic selection of rules
	protected double explorationFactor = ShoutAheadSimSetup.getExplorationFactor();
	
	
	/**
	 * Create a new random set of rules. 
	 */
	public RuleSet(){
		for(int i = 0; i < numRules; i++){
			rules.add(Rule.getRandomRule());
		}
	}
	
	public static RuleSet getRandomRuleSet(){
		return new RuleSet();
	}
	

	private List<Rule> getBestRules() {
		ArrayList<Rule> sortedRules = new ArrayList<Rule>(rules);
		sortedRules.sort(new RuleComparator());
		return sortedRules.subList(numRules/2, numRules);
	}
	
	/**
	 * Creates a new rule set by combining the best 50% of rules from two other rule sets.
	 * The resulting rule set may have less than numRules rules because of duplicates. 
	 * @param parent1
	 * @param parent2
	 */
	public RuleSet(RuleSet parent1,RuleSet parent2) {
		rules.addAll(parent1.getBestRules());
		rules.addAll(parent2.getBestRules());
		while(rules.size() < ShoutAheadSimSetup.getNumRulesPerRuleSet())
			rules.add(ShoutAheadRule.getRandomRule());
	}	

	public Rule getRuleToFollow(AutoVehicleSimView vehicle) throws NoApplicableRulesException {
		
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
	private ArrayList<Rule> getApplicableRules(AutoVehicleSimView vehicle) {
		ArrayList<Rule> applicableRules = new ArrayList<Rule>();
		for(Rule rule: rules){
			if (rule.isApplicable(vehicle))
				applicableRules.add(rule);
		}
		return applicableRules;
	}

	private ArrayList<Rule> getAppRulesWithMaxWeight(ArrayList<Rule> applicableRules) {
		ArrayList<Rule> appRulesWithMaxWeight = new ArrayList<Rule>();
		double maxWeight = getMaxAppRuleWeight(applicableRules);
		for(Rule appRule: applicableRules){
			if(Math.abs(maxWeight-appRule.getWeight()) < Constants.DOUBLE_EQUAL_PRECISION){
				appRulesWithMaxWeight.add(appRule);
			}
		}
		return appRulesWithMaxWeight;
	}
	
	private double getMaxAppRuleWeight(ArrayList<Rule> applicableRules) {
		double maxWeight = Double.NEGATIVE_INFINITY;
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
	protected boolean shouldExplore() {
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
		setNormalizedWeights(otherAppRules);
		double totalNormalizedWeight = getTotalNormalizedWeight(otherAppRules);
		for(Rule rule: otherAppRules){
			double prob = rule.getNormalizedWeight()/totalNormalizedWeight;
			if(returnTrueWithProb(prob))
				return rule;
		}
		//return the last rule in the list
		return otherAppRules.get(otherAppRules.size()-1);
	}
	
	private void setNormalizedWeights(ArrayList<Rule> ruleList) {
		double minWeight = getMinWeight(ruleList);//TODO: sort with comparator instead for better efficiency
		for(Rule rule: ruleList){
			rule.setNormalizedWeight(rule.getWeight() + minWeight + 1);
		}
		
	}


	private double getMinWeight(ArrayList<Rule> ruleList) {
		double minWeight = Double.POSITIVE_INFINITY;
		for(Rule rule: ruleList){
			if(rule.getWeight() < minWeight)
				minWeight = rule.getWeight();
		}
		return minWeight;
	}


	/**
	 * This function returns true with probability prob.
	 * @param prob
	 * @return
	 */
	protected boolean returnTrueWithProb(double prob) {
		return rand.nextDouble() < prob;
	}


	/**
	 * Returns the total weight of all given rules.
	 * @param ruleList 
	 * @return
	 */
	private int getTotalNormalizedWeight(ArrayList<Rule> ruleList) {
		int totalWeight = 0;
		for(Rule rule: ruleList)
			totalWeight += rule.getNormalizedWeight();
		return totalWeight;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(Rule rule: rules)
			str += rule;
		return str;
	}
}
