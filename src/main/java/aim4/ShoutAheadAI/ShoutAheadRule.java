package aim4.ShoutAheadAI;

import java.util.ArrayList;

import aim4.ShoutAheadAI.actions.Action;
import aim4.ShoutAheadAI.predicates.Predicate;

/**
 * A rule that does not include predicates about the intended actions of other agents.
 * @author christopher.hawk
 *
 */
public class ShoutAheadRule {
	private int numPredicates; //The max number of predicates making up a condition.
	private int weight;
	private ArrayList<Predicate> condition = new ArrayList<Predicate>();
	private Action action;
	
	/**
	 * Create a new rule by randomly choosing non-communicating predicates the form the condition 
	 * and randomly choosing an action to be taken when all of these predicates are true in the current situation. 
	 */
	ShoutAheadRule(){
		//TODO: get numPred's from config file set in param panel
		
		for(int i = 0; i < numPredicates; i++){
			condition.add(chooseRandPredicate());
		}
	}
	
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



	public int getWeight() {
		return weight;
	}

	/**
	 * TODO:
	 *       make predicate an enum?
	 * @return
	 */
	private Predicate chooseRandPredicate() {
		// TODO Auto-generated method stub
		return null;
	}
}


