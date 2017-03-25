
package aim4.driver;

import java.util.ArrayList;
import java.util.Random;

import aim4.ShoutAheadAI.Rule;
import aim4.ShoutAheadAI.RuleSet;
import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.config.Debug;
import aim4.map.BasicMap;
import aim4.sim.ShoutAheadSimulator;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.BasicAutoVehicle;
import aim4.vehicle.BasicVehicle.Movement;
import aim4.vehicle.AutoVehicleDriverView.LRFMode;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleDriverView;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadDriverAgent extends AutoDriver implements DriverSimView {
	private AutoVehicleSimView vehicle;
	private RuleSet ruleSet;//TODO
	private ShoutAheadSimulator sim;
	
	 public ShoutAheadDriverAgent(AutoVehicleSimView vehicle, BasicMap basicMap, ShoutAheadSimulator sim) {
		super(vehicle, basicMap);
		this.vehicle = vehicle;
		this.sim = sim;
		ruleSet = new RuleSet();
		
		
	}
	 
	/**
	   * Allow the driver agent to make steering and acceleration decisions based on rule sets and observations.
	   */
	  @Override
	  public void act() {
		  Rule ruleToFollow = ruleSet.getRuleToFollow(vehicle);
		  if(Debug.SHOW_PERCEPTIONS){
			  System.out.println(Predicate.showAllPredicates((AutoVehicleSimView) vehicle));
		  }
		  if(Debug.SHOW_TRUE_PERCEPTIONS) {
			  System.out.println(Predicate.showTruePredicates(vehicle));
		  }
		
		  //debug
		  vehicle.setTargetVelocityWithMaxAccel(.5);
	      vehicle.goStraight();
	      

		  
		  }
		  
	}








	
	
	



