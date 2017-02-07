
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
	private AutoVehicleDriverView vehicle;
	private ShoutAheadRuleSet nonCommRuleSet;//does not include predicates about other agents' intended actions

	 public ShoutAheadDriverAgent(AutoVehicleDriverView vehicle, BasicMap basicMap) {
		super(vehicle, basicMap);
		this.vehicle = vehicle;
		nonCommRuleSet = new ShoutAheadRuleSet();
	}
	 
	/**
	   * Allow the driver agent to make steering and acceleration decisions based on rule sets and observations.
	   */
	  @Override
	  public void act() {
		  //super.act();//debugging stuff 
		  ShoutAheadRule ruleToFollow = nonCommRuleSet.getRuleToFollow();
		  
		  //debug
		  vehicle.setTargetVelocityWithMaxAccel(.5);
	      vehicle.goStraight();

		  
		  }
		  
	}








	
	
	



