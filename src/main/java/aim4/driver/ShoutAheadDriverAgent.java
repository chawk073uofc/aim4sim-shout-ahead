
package aim4.driver;

import java.util.ArrayList;
import java.util.Random;

import aim4.ShoutAheadAI.ShoutAheadRule;
import aim4.ShoutAheadAI.ShoutAheadRuleSet;
import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.config.Debug;
import aim4.map.BasicMap;
import aim4.sim.ShoutAheadSimulator;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.BasicAutoVehicle;
import aim4.vehicle.AutoVehicleDriverView.LRFMode;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleDriverView;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadDriverAgent extends AutoDriver implements DriverSimView {
	private AutoVehicleDriverView vehicle;
	private ShoutAheadRuleSet ruleSet;//includes predicates about other agents' intended actions
	private ShoutAheadSimulator sim;
	
	 public ShoutAheadDriverAgent(AutoVehicleDriverView vehicle, BasicMap basicMap, ShoutAheadSimulator sim) {
		super(vehicle, basicMap);
		this.vehicle = vehicle;
		this.sim = sim;
		ruleSet = new ShoutAheadRuleSet();
		
		
	}
	 
	/**
	   * Allow the driver agent to make steering and acceleration decisions based on rule sets and observations.
	   */
	  @Override
	  public void act() {
		  ShoutAheadRule ruleToFollow = ruleSet.getRuleToFollow();
		  if(Debug.SHOW_PERCEPTIONS){
			  System.out.println(Predicate.showAllPredicates((AutoVehicleSimView) vehicle));
		  }
		  
		  //debug
		  vehicle.setTargetVelocityWithMaxAccel(.5);
	      vehicle.goStraight();

		  
		  }
		  
	}








	
	
	



