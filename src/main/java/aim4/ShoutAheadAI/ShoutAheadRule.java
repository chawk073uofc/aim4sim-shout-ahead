package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Random;

import aim4.ShoutAheadAI.actions.Action;
import aim4.ShoutAheadAI.actions.Cruise;
import aim4.ShoutAheadAI.actions.SlowDown;
import aim4.ShoutAheadAI.actions.SpeedUp;
import aim4.ShoutAheadAI.actions.TurnLeft;
import aim4.ShoutAheadAI.actions.TurnRight;
import aim4.ShoutAheadAI.predicates.ObstacleLT10MetersNorth;
import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.vehicle.AutoVehicleDriverView;

/**
 * A rule that does not include predicates about the intended actions of other agents.
 * @author christopher.hawk
 *
 */
public class ShoutAheadRule {
	/**The number of distinct actions a driver agent can take*/
	private final int NUM_ACTIONS = 5;
	/**he total number of distinct predicates*/
	private final int NUM_PREDICATES = 10; 
	private Random rand = new Random();
	
	/**The number of predicates making up a condition*/
	private int predicatsPerCond; 
	private int weight;
	private ArrayList<Predicate> condition = new ArrayList<Predicate>();
	private Action action;
	
	/**
	 * Create a new rule by randomly choosing non-communicating predicates the form the condition 
	 * and randomly choosing an action to be taken when all of these predicates are true in the current situation. 
	 */
	ShoutAheadRule(){
		//TODO: get numPred's from config file set in param panel
		
		for(int i = 0; i < predicatsPerCond; i++){
			condition.add(chooseRandPredicate());
		}
		action = chooseRandAction();
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
	 * Follow the rule i.e. take the action. 
	 */
	public void execute(AutoVehicleDriverView vehicle){
		action.execute(vehicle);
	}
	/**Choose a random predicate. 
	 * 
	 * @return a random action 
	 */
	private Predicate chooseRandPredicate() {
		int i = rand.nextInt(NUM_PREDICATES);
		switch(i){
		case 0:
			return new ObstacleLT10MetersNorth();
		// ........
		
		}
		return null;
	}
	
	/**
	 * Choose a random action.
	 * 
	 * @return a random action 
	 */
	private Action chooseRandAction() {
		int i = rand.nextInt(NUM_ACTIONS);
		switch(i){
		case 0:
			return new Cruise();
		case 1:
			return new SlowDown();
		case 2:
			return new SpeedUp();
		case 3:
			return new TurnLeft();
		case 4:
			return new TurnRight();		
		}
		
		return null;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public String toString(){
		String str = "";
		str += "RULE:\n";
		str += "\tCondition = {";
		for(Predicate pred: condition){
			str += pred.getClass().getName();
			str += ", ";
		}
		str += "}\n";
		str += "\tAction = " + action.getClass().getName();
		str += "\n\tWeight = " + weight + "\n";
		return str;
	}
}


