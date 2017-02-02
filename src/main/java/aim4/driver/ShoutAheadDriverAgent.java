
package aim4.driver;

import java.util.ArrayList;
import java.util.Random;

import aim4.ShoutAheadAI.ShoutAheadRule;
import aim4.map.BasicMap;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.VehicleDriverView;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadDriverAgent extends AutoDriver implements DriverSimView {
	private int numRules;
	private ArrayList<ShoutAheadRule> ruleSet;
	private double explorationFactor = 0.2;
	private Random rand = new Random();

	 public ShoutAheadDriverAgent(AutoVehicleDriverView vehicle, BasicMap basicMap) {
		super(vehicle, basicMap);
		// TODO Auto-generated constructor stub
	}
	 
	/**
	   * Allow the driver agent to make steering and acceleration decisions based on rule sets and observations.
	   */
	  @Override
	  public void act() {
		  super.act();//debugging stuff 
		  ArrayList<ShoutAheadRule> applicableRules = getApplicableRules(); //get set of rules having true conditions in this situation
		  if(applicableRules.isEmpty())
			  //do nothing
			  
		  else {
			  ArrayList<ShoutAheadRule> appRulesWithMaxWeight = getAppRulesWithMaxWeight(applicableRules);
			  ArrayList<ShoutAheadRule> otherAppRules = applicableRules;
			  otherAppRules.removeAll(appRulesWithMaxWeight);
			  ShoutAheadRule ruleToFollow;
			  if(shouldExplore()){
				  ruleToFollow = getRandOtherRule(otherAppRules);
			  }
			  else{
				  ruleToFollow = getRandMaxRule(appRulesWithMaxWeight);
			  }
		  
		  }
		  
	  }

	/**
	 * 	Get a set of rules having true conditions in the current situation.
	 * @return a set of rules having true conditions in the current situation.
	 */
	private ArrayList<ShoutAheadRule> getApplicableRules() {
		ArrayList<ShoutAheadRule> applicableRules = new ArrayList<ShoutAheadRule>();
		for(ShoutAheadRule rule: ruleSet){
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
