package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.sim.setup.ShoutAheadSimSetup;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.AutoVehicleSimView;

/**
 * A Shout Ahead Rule.
 * @author christopher.hawk
 *
 */
public class Rule {
	//private AutoVehicleSimView vehicle;
	/**The number of distinct actions a driver agent can take*/
	private final int NUM_ACTIONS = Action.values().length;
	
	private Random rand = new Random();
	
	/**The number of predicates making up a condition*/
	private int predicatsPerCond = ShoutAheadSimSetup.getNumPredsPerCond();
	private int weight;
	private TreeSet<Predicate> condition = new TreeSet<Predicate>(); //no duplicate elements
	private Action action;
	
	/**
	 * Create a new rule by randomly choosing predicates to form the condition 
	 * and randomly choosing an action to be taken when all of these predicates are true in the current situation. 
	 */
	Rule(){
		//this.vehicle = vehicle;
		
		while(condition.size() < predicatsPerCond)
			condition.add(Predicate.getRandomPredicate());
		
		action = Action.getRandomAction();
	}
	
	/**
	 * Returns true if all the predicates that make up the condition are true.
	 * i.e. the rule may be applied in the current situation. 
	 * @param vehicle 
	 * @return true if all the predicates that make up this condition are true
	 */
	public boolean isApplicable(AutoVehicleSimView vehicle){
		for(Predicate p: condition){
			if (!p.isTrue(vehicle)) return false;
		}
		return true;
	}


	
	public int getWeight() {
		return weight;
	}
	/**
	 * Follow the rule i.e. take the action. 
	 */
	public void execute(AutoVehicleSimView vehicle){
		action.execute(vehicle);
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public String toString(){
		String str = "";
		//str += "\n";
		str += "Condition = {";
		for(Predicate pred: condition){
			str += pred.name();
			str += ", ";
		}
		str += "}\n";
		str += "Action = " + action.name();
		str += "\nWeight = " + weight + "\n";
		return str;
	}
}


