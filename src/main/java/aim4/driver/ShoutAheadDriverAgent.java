
package aim4.driver;

import aim4.map.BasicMap;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.VehicleDriverView;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadDriverAgent extends AutoDriver implements DriverSimView {
	
	 public ShoutAheadDriverAgent(AutoVehicleDriverView vehicle, BasicMap basicMap) {
		super(vehicle, basicMap);
		// TODO Auto-generated constructor stub
	}
	 
	/**
	   * Allow the driver agent to make steering and acceleration decisions based on rule sets and observations.
	   */
	  @Override
	  public void act() {
		  super.act();//debugging stuff 
		  
		  // rule-based decision logic here 
		  
	  }
//	/* (non-Javadoc)
//	 * @see aim4.driver.Driver#getVehicle()
//	 */
//	@Override
//	public VehicleDriverView getVehicle() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
