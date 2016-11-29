package aim4.gui.parampanel;


import javax.swing.BoxLayout;
import javax.swing.JPanel;

import aim4.gui.component.LabeledSlider;
import aim4.sim.setup.BasicSimSetup;

public class ShoutAheadParamPanel extends JPanel{
	 private static final long serialVersionUID = 1L;

	  LabeledSlider trafficRateSlider;
	  LabeledSlider speedLimitSlider;
	  LabeledSlider stopDistToIntersectionSlider;
	  LabeledSlider numOfColumnSlider;
	  LabeledSlider numOfRowSlider;
	  LabeledSlider lanesPerRoadSlider;

	  /**
	   * Create the autonomous driver only simulation parameter panel.
	   *
	   * @param simSetup  the simulation setup
	   */
	  public ShoutAheadParamPanel(BasicSimSetup simSetup) {
	    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

	    // create the components

	    trafficRateSlider =
	      new LabeledSlider(0.0, 2500.0,
	                        simSetup.getTrafficLevel() * 3600.0,
	                        500.0, 100.0,
	                        "Traffic Level: %.0f vehicles/hour/lane",
	                        "%.0f");
	    add(trafficRateSlider);

	    speedLimitSlider =
	      new LabeledSlider(0.0, 80.0,
	                        simSetup.getSpeedLimit(),
	                        10.0, 5.0,
	                        "Speed Limit: %.0f meters/second",
	                        "%.0f");
	    add(speedLimitSlider);

	    stopDistToIntersectionSlider =
	      new LabeledSlider(0.0, 50.0,
	                        simSetup.getStopDistBeforeIntersection(),
	                        10.0, 1.0,
	                        "Stopping Distance Before Intersection: %.0f meters",
	                        "%.0f");
	    add(stopDistToIntersectionSlider);

	    numOfColumnSlider =
	        new LabeledSlider(1.0, 5.0,
	                          simSetup.getColumns(),
	                          1.0, 1.0,
	                          "Number of North-bound/South-bound Roads: %.0f",
	                          "%.0f");
	    add(numOfColumnSlider);

	    numOfRowSlider =
	      new LabeledSlider(1.0, 5.0,
	                        simSetup.getColumns(),
	                        1.0, 1.0,
	                        "Number of East-bound/West-bound Roads: %.0f",
	                        "%.0f");
	    add(numOfRowSlider);

	    lanesPerRoadSlider =
	      new LabeledSlider(1.0, 8.0,
	                        simSetup.getLanesPerRoad(),
	                        1.0, 1.0,
	                        "Number of Lanes per Road: %.0f",
	                        "%.0f");
	    add(lanesPerRoadSlider);


	  }

	  /**
	   * Get the traffic rate.
	   *
	   * @return the traffic rate
	   */
	  public double getTrafficRate() {
	    return trafficRateSlider.getValue() / 3600.0;
	  }

	  /**
	   * Get the speed limit.
	   *
	   * @return the speed limit
	   */
	  public double getSpeedLimit() {
	    return speedLimitSlider.getValue();
	  }

	  /**
	   * Get the stop distance to intersection.
	   *
	   * @return the stop distance to intersection
	   */
	  public double getStopDistToIntersection() {
	    double d = stopDistToIntersectionSlider.getValue();
	    return (d < 1.0)?1.0:d;
	  }

	  /**
	   * Get the number of columns.
	   *
	   * @return the number of columns
	   */
	  public int getNumOfColumns() {
	    return (int)numOfColumnSlider.getValue();
	  }

	  /**
	   * Get the number of rows.
	   *
	   * @return the number of rows
	   */
	  public int getNumOfRows() {
	    return (int)numOfRowSlider.getValue();
	  }

	  /**
	   * Get the number of lanes per road.
	   *
	   * @return the number of lanes per road
	   */
	  public int getLanesPerRoad() {
	    return (int)lanesPerRoadSlider.getValue();
	  }

}
