package aim4.ShoutAheadAI.actions;

import aim4.vehicle.AutoVehicleDriverView;

public class SlowDown implements Action {
	private double speedDelta = 2;//TODO: get from param panel
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void execute(AutoVehicleDriverView vehicle) {
		vehicle.slowDown(speedDelta);
	}
}
