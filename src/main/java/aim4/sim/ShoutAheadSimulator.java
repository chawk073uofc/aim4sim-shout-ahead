
package aim4.sim;

import aim4.map.BasicMap;

/**
 * This class represents a driving simulation where every car is autonomous and operating according to the
 * rule-based system outlined by the Shout-Ahead architecture. Reinforcement learning takes place during a 
 * run of this simulation. Evolutionary learning can be carried out by running this simulation repeatedly.
 *  
 *  
 *  
 * @author christopher.hawk
 */
public class ShoutAheadSimulator extends AutoDriverOnlySimulator implements Simulator {

	public ShoutAheadSimulator(BasicMap basicMap) {
		super(basicMap);
		//Spawn 4 vehicles at the end of each road. Each should be randomly assigned a destination (North, South, East, West)
		//not equal to it's origin. 
			
			//Spawn North car
				//set dest !=North
				//set rules
			//Spawn East car
				//set dest !=East
				//set rules
			//Spawn South car
				//set dest !=South
				//set rules
			//Spawn West car
				//set dest !=West
				//set rules
		}

	
	
	//WHILE there is at least one car that has not reached its destination 
	
		//For each car
			
			//Get the set of rules that are for which the condition is true
			//Get the set of applicable rules that has the maximum weight
			//Randomly choose from among these rules
			//Perform the corresponding action (breaking and steering actions)
	
}
