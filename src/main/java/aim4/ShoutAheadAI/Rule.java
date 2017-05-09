package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.ShoutAheadAI.predicates.VehiclePredicate;
import aim4.config.Debug;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VinRegistry;

/**
 * A Rule.
 * 
 * @author christopher.hawk
 *
 */
public class Rule /* implements Comparable<Rule> */ {
	// private AutoVehicleSimView vehicle;
	/** The number of distinct actions a driver agent can take */
	private final int NUM_ACTIONS = Action.values().length;

	protected Random rand = new Random();

	/** The number of predicates making up a condition */
	protected int predicatsPerCond = ShoutAheadSimSetup.getNumPredsPerCond();
	protected double weight = 0;
	protected double normalizedWeight = 0;
	private TreeSet<VehiclePredicate> condition = new TreeSet<VehiclePredicate>(); // no
																					// duplicate
																				   // elements
	private int vehicleArgumentVin;
	protected Action action;

	/**
	 * Create a new rule by randomly choosing predicates to form the condition
	 * and randomly choosing an action to be taken when all of these predicates
	 * are true in the current situation.
	 */
	public Rule() {
		// this.vehicle = vehicle;
		
		while (condition.size() < predicatsPerCond){
			VehiclePredicate randomPred = VehiclePredicate.getRandomPredicate();
			if(!isContradictory(randomPred))
					condition.add(randomPred);
		}
		vehicleArgumentVin = rand.nextInt(ShoutAheadSimSetup.getMaxNumActiveCars()) + 1000;
		
		action = Action.getRandomAction();
	}

	private boolean isContradictory(VehiclePredicate randomPred) {
		if(randomPred == VehiclePredicate.HEADING_NORTH && condition.contains(VehiclePredicate.HEADING_NORTH))
			return true;
		if(randomPred == VehiclePredicate.HEADING_EAST && condition.contains(VehiclePredicate.HEADING_WEST))
			return true;
		if(randomPred == VehiclePredicate.DESTINATION_NORTH_OF_VEHICLE && condition.contains(VehiclePredicate.DESTINATION_SOUTH_OF_VEHICLE))
			return true;
		if(randomPred == VehiclePredicate.DESTINATION_EAST_OF_VEHICLE && condition.contains(VehiclePredicate.DESTINATION_WEST_OF_VEHICLE))
			return true;
		if(randomPred == VehiclePredicate.SPEED_GT_25_METERS_PER_SEC && condition.contains(VehiclePredicate.SPEED_LT_25_METERS_PER_SEC))
			return true;
		if(randomPred == VehiclePredicate.SPEED_GT_25_METERS_PER_SEC && condition.contains(VehiclePredicate.SPEED_LT_15_METERS_PER_SEC))
			return true;
		if(randomPred == VehiclePredicate.SPEED_GT_25_METERS_PER_SEC && condition.contains(VehiclePredicate.SPEED_LT_5_METERS_PER_SEC))
			return true;
		if(randomPred == VehiclePredicate.SPEED_GT_15_METERS_PER_SEC && condition.contains(VehiclePredicate.SPEED_LT_15_METERS_PER_SEC))
			return true;
		if(randomPred == VehiclePredicate.SPEED_GT_15_METERS_PER_SEC && condition.contains(VehiclePredicate.SPEED_LT_5_METERS_PER_SEC))
			return true;
		if(randomPred == VehiclePredicate.SPEED_GT_5_METERS_PER_SEC && condition.contains(VehiclePredicate.SPEED_LT_5_METERS_PER_SEC))
			return true;
		if(randomPred == VehiclePredicate.CAR_LT_10_METERS_IN_FRONT && condition.contains(VehiclePredicate.CAR_LT_10_METERS_BEHIND))
			return true;
		if(randomPred == VehiclePredicate.CAR_LT_10_METERS_RIGHT && condition.contains(VehiclePredicate.CAR_LT_10_METERS_LEFT))
			return true;
		if(randomPred == VehiclePredicate.CAR_LT_20_METERS_IN_FRONT && condition.contains(VehiclePredicate.CAR_LT_20_METERS_BEHIND))
			return true;
		if(randomPred == VehiclePredicate.CAR_LT_20_METERS_RIGHT && condition.contains(VehiclePredicate.CAR_LT_20_METERS_LEFT))
			return true;
		if(randomPred == VehiclePredicate.CAR_LT_20_METERS_BEHIND && condition.contains(VehiclePredicate.CAR_LT_10_METERS_IN_FRONT))
			return true;
				
		return false;
	}

	public static Rule getRandomRule() {
		return new Rule();
	}

	/**
	 * Returns true if all the predicates that make up the condition are true.
	 * i.e. the rule may be applied in the current situation.
	 * 
	 * @param vehicle
	 * @return true if all the predicates that make up this condition are true
	 */
	public boolean isApplicable(AutoVehicleSimView vehicle) {
		for (VehiclePredicate p : condition) {
			if (!p.isTrue(vehicle, (AutoVehicleSimView) VinRegistry.peekVehicleFromVIN(vehicleArgumentVin))) {
				return false;
			}
		}
		return true;
	}

	protected double getWeight() {
		return weight;
	}

	/**
	 * Follow the rule i.e. take the action.
	 */
	protected void execute(AutoVehicleSimView vehicle) {
		if (Debug.SHOW_RULE_USED)
			System.out.println("Vehicle " + vehicle.getVIN() + "Following rule \n" + this);
		action.execute(vehicle);
	}

	/**
	 * Two rules are equal if they have the same condition and action.
	 * 
	 * @param otherRule
	 * @return
	 */
	@Override
	public boolean equals(Object otherRule) {
		try {
			Rule otherRuleCast = (Rule) otherRule;

			if (this.condition == otherRuleCast.getCondition() && this.action == otherRuleCast.getAction())
				return true;
			else
				return false;
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		String str = "";
		str += "Condition = {";
		int i = 1;
		for (VehiclePredicate pred : condition) {
			str += pred.name();
			if (i < condition.size())
				str += ", ";
			i++;
		}
		str += "}\n";
		str += "Weight = " + weight;
		str += " Action = " + action.name() + "\n";
		return str;
	}

	/**
	 * @return the condition
	 */
	public TreeSet<VehiclePredicate> getCondition() {
		return condition;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

//	public void addWeight(double weightToAdd) {
//		weight += weightToAdd;
//	}
//	
	

	/**
	 * @return the normalizedWeight
	 */
	public double getNormalizedWeight() {
		return normalizedWeight;
	}

	/**
	 * @param normalizedWeight
	 *            the normalizedWeight to set
	 */
	public void setNormalizedWeight(double normalizedWeight) {
		this.normalizedWeight = normalizedWeight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	// @Override
	// public int compareTo(Rule r) {
	// return (int) (this.weight - r.getWeight());
	// }
}