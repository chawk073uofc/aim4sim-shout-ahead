package aim4.ShoutAheadAI.actions;

import aim4.vehicle.AutoVehicleDriverView;

public interface Action {
	/**
	 * Take this action.
	 */
	void execute(AutoVehicleDriverView vehicle);

//	@Override
//	/**
//	 * {@inheritDoc}
//	 */
//	public String toString();
		
}
