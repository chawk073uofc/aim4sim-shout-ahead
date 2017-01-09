
package aim4.sim.setup;

import aim4.config.Debug;
import aim4.map.GridMap;
import aim4.sim.ShoutAheadSimulator;
import aim4.sim.Simulator;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadSimSetup extends AutoDriverOnlySimSetup implements SimSetup {

	public ShoutAheadSimSetup(BasicSimSetup basicSimSetup) {
		super(basicSimSetup);
		// TODO Auto-generated constructor stub
	}
	
	 /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Simulator getSimulator() {
		  double currentTime = 0.0;
		  GridMap layout = new GridMap(currentTime,
		                                       numOfColumns,
		                                       numOfRows,
		                                       laneWidth,
		                                       speedLimit,
		                                       lanesPerRoad,
		                                       medianSize,
		                                       distanceBetween);
		  Debug.SHOW_VEHICLE_COLOR_BY_MSG_STATE = true;
		  return new ShoutAheadSimulator(layout);

	  }

}
