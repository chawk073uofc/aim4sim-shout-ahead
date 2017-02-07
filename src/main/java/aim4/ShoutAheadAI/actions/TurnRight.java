/**
 * 
 */
package aim4.ShoutAheadAI.actions;

import java.awt.geom.Point2D;

import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.VehicleSpec;

/**
 * @author chrishawk_MacBookAir
 *
 */
public class TurnRight implements Action{

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void execute(AutoVehicleDriverView vehicle) {
		vehicle.turnRight(0.175);
	}
	

}
