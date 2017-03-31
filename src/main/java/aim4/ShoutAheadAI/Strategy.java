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
public class Strategy {
	private RuleSet ruleSet;
	private ShoutAheadRuleSet shoutAheadRuleSet;
	
	/**
	 * Create a new randomly generated strategy. 
	 */
	public Strategy() {
		ruleSet = new RuleSet();
	}
	/**
	 * Create a new strategy by breeding two existing strategies. 
	 * @param strat1
	 * @param strat2
	 */
	public Strategy(Strategy strat1, Strategy strat2) {
		//TODO:make strat breeder
	}
	public Rule getRuleToFollow(AutoVehicleSimView vehicle) throws NoApplicableRulesException {
		// TODO add the shout ahead part
		return ruleSet.getRuleToFollow(vehicle);
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
}


