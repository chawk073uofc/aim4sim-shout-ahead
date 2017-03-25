package aim4.ShoutAheadAI;

import java.util.Random;

import aim4.sim.setup.ShoutAheadSimSetup;
import aim4.vehicle.AutoVehicleSimView;

public enum Action {
	CRUISE {
		@Override
		public void execute(AutoVehicleSimView vehicle) {
			//TODO
			
		}
	},
	GO_STRAIGHT {
		@Override
		public void execute(AutoVehicleSimView vehicle) {
			vehicle.goStraight();
			
		}
	},
	SLOW_DOWN {
		@Override
		public void execute(AutoVehicleSimView vehicle) {
			vehicle.slowDown(ShoutAheadSimSetup.getSpeedDelta()); 
			
		}
	},
	SPEED_UP {
		@Override
		public void execute(AutoVehicleSimView vehicle) {
			vehicle.speedUp(ShoutAheadSimSetup.getSpeedDelta());
			
		}
	},
	TURN_LEFT {
		@Override
		public void execute(AutoVehicleSimView vehicle) {
			vehicle.turnLeft(ShoutAheadSimSetup.getSteeringDelta());
			
		}
	},
	TRUN_RIGHT {
		@Override
		public void execute(AutoVehicleSimView vehicle) {
			vehicle.turnRight(ShoutAheadSimSetup.getSteeringDelta());
			
		}
	};
	
	public abstract void execute(AutoVehicleSimView vehicle);
	
	public static Action getRandomAction(){
		Random rand = new Random();
		int numActions = values().length;
		int randActionIndex = rand.nextInt(numActions);
		return values()[randActionIndex];
	}
}
