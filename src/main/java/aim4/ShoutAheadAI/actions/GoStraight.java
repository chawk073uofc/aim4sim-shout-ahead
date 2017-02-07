package aim4.ShoutAheadAI.actions;

import aim4.vehicle.AutoVehicleDriverView;

public class GoStraight implements Action {

	@Override
	/**
	 * {@inheretDoc}
	 */
	public void execute(AutoVehicleDriverView vehicle) {
		vehicle.goStraight();
	}

}
