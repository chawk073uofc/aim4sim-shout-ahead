/**
 * 
 */
package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import aim4.config.Constants;
import aim4.vehicle.AutoVehicleSimView;

/**
 * A rule set that includes predicates about the intended actions of other cars. 
 * @author Chris Hawk
 * 
 */
public class ShoutAheadRuleSet extends RuleSet {
	private HashSet<ShoutAheadRule> rules = new HashSet<ShoutAheadRule>();//no duplicates
	
	public ShoutAheadRuleSet() {
		for(int i = 0; i < numRules; i++){
			rules.add(ShoutAheadRule.getRandomRule());
		}
	}
	
	public static ShoutAheadRuleSet getRandomSARuleSet(){
		return new ShoutAheadRuleSet();
	}
	
	/**
	 * Creates a new rule set by combining the best 50% of rules from two other rule sets.
	 * The resulting rule set may have less than numRules rules because of duplicates. 
	 * @param parent1
	 * @param parent2
	 */
	public ShoutAheadRuleSet(ShoutAheadRuleSet parent1, ShoutAheadRuleSet parent2) {
		rules.addAll(parent1.getBestRules());
		rules.addAll(parent2.getBestRules());
		while(rules.size() < ShoutAheadSimSetup.getNumRulesPerRuleSet())
			rules.add(ShoutAheadRule.getRandomRule());
	}	
	
	public ShoutAheadRule getRuleToFollow(AutoVehicleSimView vehicle) throws NoApplicableRulesException {
		
		  ShoutAheadRule ruleToFollow = null;

		 ArrayList<ShoutAheadRule> applicableRules = getApplicableRules(vehicle); //get set of rules having true conditions in this situation
		  if(applicableRules.isEmpty()){
			  throw new NoApplicableRulesException(vehicle);
		  }
		  else {
			  ArrayList<ShoutAheadRule> appRulesWithMaxWeight = getAppRulesWithMaxWeight(applicableRules);
			  ArrayList<ShoutAheadRule> otherAppRules = applicableRules;
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
	 * @param vehicle 
	 * @return a set of rules having true conditions in the current situation.
	 */
	private ArrayList<ShoutAheadRule> getApplicableRules(AutoVehicleSimView vehicle) {
		ArrayList<ShoutAheadRule> applicableRules = new ArrayList<ShoutAheadRule>();
		for(ShoutAheadRule rule: rules){
			if (rule.isApplicable(vehicle))
				applicableRules.add(rule);
		}
		return applicableRules;
	}
	
	/**
	 * From among all the rules that apply in the current situation and have the maximum weight, choose a random rule. 
	 * @param appRulesWithMaxWeight all the rules that apply in the current situation and have the maximum weight
	 * @return a random rule from among the list of rules given
	 */
	private ShoutAheadRule getRandMaxRule(ArrayList<ShoutAheadRule> appRulesWithMaxWeight) {
		int ruleIndex = rand.nextInt(appRulesWithMaxWeight.size());
		return appRulesWithMaxWeight.get(ruleIndex);
	}
	
	private ArrayList<ShoutAheadRule> getAppRulesWithMaxWeight(ArrayList<ShoutAheadRule> applicableRules) {
		ArrayList<ShoutAheadRule> appRulesWithMaxWeight = new ArrayList<ShoutAheadRule>();
		double maxWeight = getMaxAppRuleWeight(applicableRules);
		for(ShoutAheadRule appRule: applicableRules){
			if(Math.abs(maxWeight-appRule.getWeight()) < Constants.DOUBLE_EQUAL_PRECISION){
				appRulesWithMaxWeight.add(appRule);
			}
		}
		return appRulesWithMaxWeight;
	}
	
	private double getMaxAppRuleWeight(ArrayList<ShoutAheadRule> applicableRules) {
		double maxWeight = Double.NEGATIVE_INFINITY;
		for(Rule rule: applicableRules){
			if(rule.getWeight() > maxWeight)
				maxWeight = rule.getWeight();
		}
		return maxWeight;
	}
	/**
	 * Choose a rule from among all those that apply in the current situation but have less than maximal weight. The probability of choosing a given 
	 * rule in this set is the weight associated with that rule divided by the sum of all the weights of all rules in this set. 
	 * @param otherAppRules
	 * @return a rule in the given set
	 */
	private ShoutAheadRule getRandOtherRule(ArrayList<ShoutAheadRule> otherAppRules) {
		setNormalizedWeights(otherAppRules);
		double totalNormalizedWeight = getTotalNormalizedWeight(otherAppRules);
		for(ShoutAheadRule rule: otherAppRules){
			double prob = rule.getNormalizedWeight()/totalNormalizedWeight;
			if(returnTrueWithProb(prob))
				return rule;
		}
		//return the last rule in the list
		return otherAppRules.get(otherAppRules.size()-1);
	}
	
	private void setNormalizedWeights(ArrayList<ShoutAheadRule> ruleList) {
		double minWeight = getMinWeight(ruleList);
		for(Rule rule: ruleList){
			rule.setNormalizedWeight(rule.getWeight() + minWeight + 1);
		}
		
	}
	/**
	 * Returns the total weight of all given rules.
	 * @param ruleList 
	 * @return
	 */
	private int getTotalNormalizedWeight(ArrayList<ShoutAheadRule> ruleList) {
		int totalWeight = 0;
		for(ShoutAheadRule rule: ruleList)
			totalWeight += rule.getNormalizedWeight();
		return totalWeight;
	}
	
	private double getMinWeight(ArrayList<ShoutAheadRule> ruleList) {
		double minWeight = Double.POSITIVE_INFINITY;
		for(ShoutAheadRule rule: ruleList){
			if(rule.getWeight() < minWeight)
				minWeight = rule.getWeight();
		}
		return minWeight;
	}

	@Override
	public String toString() {
		String str = "";
		for(ShoutAheadRule shoutAheadRule: rules)
			str += shoutAheadRule + "\n";
		return str;
	}
	
	private List<ShoutAheadRule> getBestRules() {
		ArrayList<ShoutAheadRule> sortedRules = new ArrayList<ShoutAheadRule>(rules);
		sortedRules.sort(new RuleComparator());
		return sortedRules.subList(numRules/2, numRules);
	}
}
