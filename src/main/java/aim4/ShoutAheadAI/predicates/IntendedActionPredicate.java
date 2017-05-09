package aim4.ShoutAheadAI.predicates;

import java.util.Random;

import aim4.ShoutAheadAI.Action;
import aim4.ShoutAheadAI.ShoutAheadDriverAgent;
import aim4.ShoutAheadAI.ShoutAheadSimulator;
import aim4.util.GeomMath;
import aim4.vehicle.AutoVehicleSimView;

public enum IntendedActionPredicate /*implements Predicate*/{
	WILL_CRUISE{
		@Override
		public boolean isTrue(AutoVehicleSimView vehicle){
			ShoutAheadDriverAgent driver = (ShoutAheadDriverAgent) vehicle.getDriver();
			return driver.getIntendedAction() == Action.CRUISE;
		}
	},
	WILL_GO_STRAIGHT{
		@Override
		public boolean isTrue(AutoVehicleSimView vehicle){
			ShoutAheadDriverAgent driver = (ShoutAheadDriverAgent) vehicle.getDriver();
			return driver.getIntendedAction() == Action.GO_STRAIGHT;
		}
	},
	WILL_SLOW_DOWN{
		@Override
		public boolean isTrue(AutoVehicleSimView vehicle){
			ShoutAheadDriverAgent driver = (ShoutAheadDriverAgent) vehicle.getDriver();
			return driver.getIntendedAction() == Action.SLOW_DOWN;
		}
	},
	WILL_SPEED_UP{
		@Override
		public boolean isTrue(AutoVehicleSimView vehicle){
			ShoutAheadDriverAgent driver = (ShoutAheadDriverAgent) vehicle.getDriver();
			return driver.getIntendedAction() == Action.SPEED_UP;
		}
	},
	WILL_TURN_LEFT{
		@Override
		public boolean isTrue(AutoVehicleSimView vehicle){
			ShoutAheadDriverAgent driver = (ShoutAheadDriverAgent) vehicle.getDriver();
			return driver.getIntendedAction() == Action.TURN_LEFT;
		}
	},
	WILL_TURN_RIGHT{
		@Override
		public boolean isTrue(AutoVehicleSimView vehicle){
			ShoutAheadDriverAgent driver = (ShoutAheadDriverAgent) vehicle.getDriver();
			return driver.getIntendedAction() == Action.TRUN_RIGHT;
		}
	};
	public static ShoutAheadSimulator sim;
	private static Random rand = new Random();
	/**
	 * Returns true if the predicate is true at a particular point in the simulation for a particular car.
	 * @return	true if the predicate is true at a particular point in the simulation for a particular car.
	 */
	public abstract boolean isTrue(AutoVehicleSimView vehicle);
	
	/**
	 * Returns a random predicate.
	 * @return a random predicate.
	 */
	public static IntendedActionPredicate getRandomPredicate(){
		int numPredicates = values().length;
		int randPredIndex = rand.nextInt(numPredicates);
		return values()[randPredIndex];
	}

	public static int size() {
		return values().length;
	}
}
