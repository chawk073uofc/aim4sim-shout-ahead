
package aim4.driver;

import aim4.vehicle.VehicleDriverView;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadDriver extends Driver implements DriverSimView {
	 /**
	   * Allow the driver agent to make steering and acceleration decisions based on rule sets and observations.
	   */
	  @Override
	  public void act() {
		  super.act();//debugging stuff 
		  
	  }
	/* (non-Javadoc)
	 * @see aim4.driver.Driver#getVehicle()
	 */
	@Override
	public VehicleDriverView getVehicle() {
		// TODO Auto-generated method stub
		return null;
	}

}
