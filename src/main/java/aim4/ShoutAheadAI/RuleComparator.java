/**
 * 
 */
package aim4.ShoutAheadAI;

import java.util.Comparator;

/**
 * @author chrishawk_MacBookAir
 *
 */
public class RuleComparator implements Comparator<Rule> {
	
	    @Override
	    public int compare(Rule r1, Rule r2) {
	    	if(r1.getWeight() > r2.getWeight())
	    		return 1;
	    	if(r1.getWeight() < r2.getWeight())
	    		return -1;
	    	return 0;
	    }
}
