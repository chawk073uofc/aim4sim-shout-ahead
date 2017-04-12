/**
 * 
 */
package aim4.ShoutAheadAI;

import java.util.TreeSet;

import aim4.ShoutAheadAI.predicates.IntendedActionPredicate;
import aim4.ShoutAheadAI.predicates.VehiclePredicate;
import aim4.config.Debug;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleSimView;
import aim4.vehicle.VinRegistry;

/**A rule that contains predicates about the intended actions of other cars.
 * 
 * @author Chris Hawk
 * 
 */
public class ShoutAheadRule extends Rule{
	private TreeSet<VehiclePredicate> vehCondition = new TreeSet<VehiclePredicate>(); //no duplicate elements
	private TreeSet<IntendedActionPredicate> intentCondition = new TreeSet<IntendedActionPredicate>(); //no duplicate elements
	private int vehicleArgumentVin;//the argument for an intention predicate
	public ShoutAheadRule(){
		
		while((vehCondition.size() + intentCondition.size()) < predicatsPerCond){
			if(rand.nextInt(VehiclePredicate.size() + IntendedActionPredicate.size()) < VehiclePredicate.size() )
				vehCondition.add(VehiclePredicate.getRandomPredicate());
			else{
				intentCondition.add(IntendedActionPredicate.getRandomPredicate());
				vehicleArgumentVin = rand.nextInt(ShoutAheadSimSetup.getMaxNumActiveCars()) + 1000;
			}
		}
		
		action = Action.getRandomAction();
	}
	
	public static ShoutAheadRule getRandomRule() {
		return new ShoutAheadRule();
	}
	
	/**
	 * Returns true if all the predicates that make up the condition are true.
	 * i.e. the rule may be applied in the current situation. 
	 * @param vehicle 
	 * @return true if all the predicates that make up this condition are true
	 */
	public boolean isApplicable(AutoVehicleSimView vehicle){
		AutoVehicleSimView vehicleArgument = (AutoVehicleSimView) VinRegistry.peekVehicleFromVIN(vehicleArgumentVin);
		if(vehicleArgument == null)
			return false;
		for(VehiclePredicate p: vehCondition){
			if (!p.isTrue(vehicle, null)) {
				return false;
			}
		}
		for(IntendedActionPredicate p: intentCondition){
			if (!p.isTrue(vehicleArgument)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Two rules are equal if they have the same condition and action. 
	 * @param otherRule
	 * @return
	 */
	@Override
	public boolean equals(Object otherRule) {
		try{
			ShoutAheadRule otherRuleCast = (ShoutAheadRule) otherRule;
		
		if(this.vehCondition == otherRuleCast.getCondition() && this.action == otherRuleCast.getAction())
			return true;
		else
			return false;
		}
		catch(ClassCastException e){
			return false;
		}
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public String toString(){
		String str = "";
		str += "VehConditoin = {";
		int i = 1;
		for(VehiclePredicate pred: vehCondition){
			str += pred.name();
			if(i<vehCondition.size())
				str += ", ";
			i++;
		}
		str += "\nIntention Condition = {";

		int j = 1;
		for(IntendedActionPredicate pred: intentCondition){
			str += pred.name() + "(" + vehicleArgumentVin + ")";
			//todo add vehicle
			if(j<vehCondition.size())
				str += ", ";
			j++;
		}
		str += "}\n";
		str += "Weight = " + weight;
		str += " Action = " + action.name() + "\n";
		
		return str;
	}

	
}
