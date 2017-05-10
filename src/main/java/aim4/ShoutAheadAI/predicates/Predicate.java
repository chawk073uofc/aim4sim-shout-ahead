package aim4.ShoutAheadAI.predicates;

import aim4.vehicle.AutoVehicleSimView;

/**
 * Marker interface.
 * @author chrishawk_MacBookAir
 *
 */
public interface Predicate {
	/**
	 * Returns true if the predicate is true at a particular point in the simulation for a particular car.
	 * @return	true if the predicate is true at a particular point in the simulation for a particular car.
	 */
	public abstract boolean isTrue(AutoVehicleSimView vehicle);
	
	/**
	 * Returns a random predicate.
	 * @return a random predicate.
	 */
	//public static Predicate getRandomPredicate();
}
