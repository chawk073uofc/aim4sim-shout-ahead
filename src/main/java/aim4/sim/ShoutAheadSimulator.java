
package aim4.sim;

import aim4.map.BasicMap;

/**
 * This class represents a driving simulation where every car is autonomous and operating according to the
 * rule-based system outlined by the Shout-Ahead architecture. Reinforcement learning takes place during a 
 * run of this simulation. Evolutionary learning can be carried out by running this simulation repeatedly.
 *  
 * @author christopher.hawk
 */
public class ShoutAheadSimulator extends AutoDriverOnlySimulator {

	public ShoutAheadSimulator(BasicMap basicMap) {
		super(basicMap);
		// TODO Auto-generated constructor stub
	}

}
