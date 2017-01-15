package aim4.ShoutAheadAI;

import java.util.ArrayList;

import aim4.ShoutAheadAI.predicates.Predicate;

/**
 * @author chrishawk_MacBookAir  
 *
 */
public class ShoutAheadRule {
	private int maxLength; //The max number of predicates making up a condition.
	private int weight;
	private ArrayList<Predicate> condition;
	
	
	
	/**
	 * Returns true if all the predicates that make up this condition are true.
	 * i.e. the rule may be applied in the current situation. 
	 * @return true if all the predicates that make up this condition are true
	 */
	public boolean isApplicable(){
		for(Predicate p: condition){
			if (!p.isTrue()) return false;
		}
		return true;
	}
}


