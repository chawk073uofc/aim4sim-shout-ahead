package aim4.ShoutAheadAI.predicates;

/**
 * Defines an interface for predicates which form the conditions for ShoutAheadRules.
 * 
 * @author chrishawk_MacBookAir
 *
 */
public interface Predicate {
	
	/**
	 * Returns true if the predicate is true at a particular point in the simulation.
	 * @return	true if the predicate is true at a particular point in the simulation.
	 */
	public boolean isTrue();
}
