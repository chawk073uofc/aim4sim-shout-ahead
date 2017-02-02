
package aim4.driver;

import java.util.ArrayList;
import java.util.Random;

import aim4.ShoutAheadAI.ShoutAheadRule;
import aim4.ShoutAheadAI.ShoutAheadRuleSet;
import aim4.map.BasicMap;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.VehicleDriverView;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadDriverAgent extends AutoDriver implements DriverSimView {
	private ShoutAheadRuleSet nonCommRuleSet;//does not include predicates about other agents' intended actions

	 public ShoutAheadDriverAgent(AutoVehicleDriverView vehicle, BasicMap basicMap) {
		super(vehicle, basicMap);
		nonCommRuleSet = new ShoutAheadRuleSet();
	}
	 
	/**
	   * Allow the driver agent to make steering and acceleration decisions based on rule sets and observations.
	   */
	  @Override
	  public void act() {
		  super.act();//debugging stuff 
		  ShoutAheadRule ruleToFollow = nonCommRuleSet.getRuleToFollow();
		  //take the appropriate driving action
		 
		  
		  }
		  
	}








	
	
	



