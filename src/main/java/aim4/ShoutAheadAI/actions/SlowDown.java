package aim4.ShoutAheadAI.actions;

import aim4.vehicle.AutoVehicleDriverView;

public class SlowDown implements Action {
	private double speedDelta = 2;
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public void execute(AutoVehicleDriverView vehicle) {
		vehicle.slowDown(speedDelta);
	}
}