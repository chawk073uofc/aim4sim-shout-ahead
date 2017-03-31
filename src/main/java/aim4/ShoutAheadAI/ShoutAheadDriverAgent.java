
package aim4.ShoutAheadAI;

import java.util.ArrayList;
import java.util.Random;

import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.config.Debug;
import aim4.driver.AutoDriver;
import aim4.driver.DriverSimView;
import aim4.map.BasicMap;
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
	private static final int NUM_HEAD_START_ACTIONS = 50;
	private AutoVehicleSimView vehicle;
	private ShoutAheadSimulator sim;
	private int actionNumber = 0;

	public ShoutAheadDriverAgent(AutoVehicleSimView vehicle, BasicMap basicMap, ShoutAheadSimulator sim) {
		super(vehicle, basicMap);
		this.vehicle = vehicle;
		this.sim = sim;

	}

	/**
	 * Allow the driver agent to make steering and acceleration decisions based
	 * on rule sets and observations.
	 */
	@Override
	public void act() {
		actionNumber++;
		if (Debug.SHOW_PERCEPTIONS) {
			System.out.println(Predicate.showAllPredicates((AutoVehicleSimView) vehicle));
		}
		if (Debug.SHOW_TRUE_PERCEPTIONS) {
			System.out.println(Predicate.showTruePredicates(vehicle));
		}
		if (Debug.SHOW_APPLICABLE_RULES) {
			System.out.println("Applicable Rules For VIN " + vehicle.getVIN());
			System.out.println(sim.getStrategy().getRuleSet().getApplicableRules(vehicle));
		}
		if (actionNumber < NUM_HEAD_START_ACTIONS && Debug.GIVE_HEAD_START)
			headStart();
		else {
			try {
				Rule ruleToFollow = sim.getStrategy().getRuleToFollow(vehicle);
				ruleToFollow.execute(vehicle);
			} catch (NoApplicableRulesException e) {
				Action.CRUISE.execute(vehicle);// do nothing
			}
		}
		 


	}

	private void headStart() {
		if (Debug.GIVE_HEAD_START) {
			Action.CRUISE.execute(vehicle);
//			 vehicle.setTargetVelocityWithMaxAccel(.5);
//			 vehicle.goStraight();
		}

	}

}
