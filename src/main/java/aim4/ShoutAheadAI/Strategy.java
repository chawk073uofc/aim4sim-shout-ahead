/**
 * 
 */
package aim4.ShoutAheadAI;

/**
 * @author Chris Hawk
 * 
 * Contains the two rule sets that define the behavior of cars in a given simulation run.
 */
public class Strategy {
	private RuleSet ruleSet;
	private ShoutAheadRuleSet shoutAheadRuleSet;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RULE SET:\n\n" + ruleSet + "\nSHOUT AHEAD RULE SET:\n\n" + shoutAheadRuleSet;
	}
}


