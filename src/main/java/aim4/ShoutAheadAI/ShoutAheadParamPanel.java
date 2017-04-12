package aim4.ShoutAheadAI;


import javax.swing.BoxLayout;
import javax.swing.JPanel;

import aim4.ShoutAheadAI.predicates.VehiclePredicate;
import aim4.gui.component.LabeledSlider;
import aim4.sim.setup.BasicSimSetup;

public class ShoutAheadParamPanel extends JPanel{
	 private static final long serialVersionUID = 1L;
	 private ShoutAheadSimSetup simSetup;
	 	 
	 LabeledSlider steeringAngleDeltaSlider;
	 LabeledSlider speedDeltaSlider;
	 LabeledSlider numCarsPerSimSlider;
	 LabeledSlider maxNumActiveCarsSlider;
	 
	 LabeledSlider numPredsPerCondSlider;
	 LabeledSlider rulesPerRuleRuleSetSlider;
	 LabeledSlider explorationFactorSlider;
	 LabeledSlider learningFactorSlider;
	 LabeledSlider numRoundsPerGenerationSlider;
	 LabeledSlider numGenerationsSlider;
	 //TODO output file name;
	 
	 
	  /**
	   * Create the autonomous driver only simulation parameter panel.
	   *
	   * @param simSetup  the simulation setup
	 * @param shoutAheadSetup 
	   */
	  public ShoutAheadParamPanel(ShoutAheadSimSetup simSetup) {
	    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	    this.simSetup = simSetup;
	    
	    // create the components

	    steeringAngleDeltaSlider =
	      new LabeledSlider(0.0, 360.0,
	    		  ShoutAheadSimSetup.getSteeringDelta(),
	                        45.0, 0.5,
	                        "Steering Angle Delta: %.0f degrees",
	                        "%.0f");
	    add(steeringAngleDeltaSlider);

	    speedDeltaSlider =
	      new LabeledSlider(0.0, 50.0,
	                        ShoutAheadSimSetup.getSpeedDelta(),
	                        10.0, 0.5,
	                        "Speed Delta: %.0f meters/second",
	                        "%.0f");
	    add(speedDeltaSlider);

	    
	    
	    numCarsPerSimSlider =
	      new LabeledSlider(0.0, 50.0,
	                        ShoutAheadSimSetup.getNumCarsPerSim(),
	                        10.0, 1.0,
	                        "Number of Cars Per Simulation: %.0f ",
	                        "%.0f");
	    add(numCarsPerSimSlider);

	    
	    maxNumActiveCarsSlider = 
	    		new LabeledSlider(0.0, 50.0,
                        ShoutAheadSimSetup.getMaxNumActiveCars(),
                        10.0, 1.0,
                        "Maximum Number of Active Cars: %.0f ",
                        "%.0f");
	    add(maxNumActiveCarsSlider);

	    
	    numPredsPerCondSlider =
	  	      new LabeledSlider(0.0, VehiclePredicate.values().length,
	  	    		  ShoutAheadSimSetup.getNumPredsPerCond(),
	  	                        5, 1,
	  	                        "Number of Predicates Per Rule: %.0f ",
	  	                        "%.0f");
	  	    add(numPredsPerCondSlider);
	  	 
	  	//num unique rules = (numPreds + 1) choose (num preds per cond + 1)
  	    rulesPerRuleRuleSetSlider =
  		      new LabeledSlider(1, 5000,
  		                        ShoutAheadSimSetup.getNumRulesPerRuleSet(),
  		                        500, 1,
  		                        "Number of Rules Per Rule Set: %.0f ",
  		                        "%.0f");
  		    add(rulesPerRuleRuleSetSlider);
	  	    
	  	  explorationFactorSlider =
	  	      new LabeledSlider(0.0, 1.0,
	  	                        ShoutAheadSimSetup.getExplorationFactor(),
	  	                        0.25, 0.1,
	  	                        "Exploration Factor: %.2f ",
	  	                        "%.0f");
	  	    add(explorationFactorSlider);

	  	  learningFactorSlider =
	  	      new LabeledSlider(0.0, 1.0,
	  	                        ShoutAheadSimSetup.getLearningFactor(),
	  	                        0.25, 0.1,
	  	                        "Learning Factor: %.2f ",
	  	                        "%.0f");
	  	    add(learningFactorSlider);
	  	    
	  	  numRoundsPerGenerationSlider =
		  	      new LabeledSlider(0, 200,
		  	                        ShoutAheadSimSetup.getNumRoundsPerGeneration(),
		  	                        10, 1,
		  	                        "Number of Simulation Runs Per Generation: %.0f ",
		  	                        "%.0f");
		  	    add(numRoundsPerGenerationSlider);

		  	  numGenerationsSlider =
  	      new LabeledSlider(0, 200,
  	                        ShoutAheadSimSetup.getNumGenerations(),
  	                        10.0, 1.0,
  	                        "Number of Generations: %.0f ",
  	                        "%.0f");
  	    add(numGenerationsSlider);
  	    
	  }

	  /**
	   * Get the traffic rate. 
	   *
	   * @return the traffic rate
	   */
	  public double getTrafficRate() {
		  
		  //return trafficRateSlider.getValue() / 3600.0;
	   return getDefaultTrafficRate();
	  }
	  /**
	   * Get the default traffic rate. 
	   *
	   * @return the traffic rate
	   */
	  private double getDefaultTrafficRate(){
		  return simSetup.getTrafficLevel();
	  }

	  /**
	   * Get the speed limit.
	   *
	   * @return the speed limit
	   */
	  public double getSpeedLimit() {
	    //return speedLimitSlider.getValue();
		  return getDefaultSpeedLimit();
	  }
	  
	  /**
	   * Get the default speed limit.
	   *
	   * @return the speed limit
	   */
	  private double getDefaultSpeedLimit() {
		  return simSetup.getSpeedLimit();
	  }
	  /**
	   * Get the stop distance to intersection.
	   *
	   * @return the stop distance to intersection
	   */
	  public double getStopDistToIntersection() {
//	    double d = stopDistToIntersectionSlider.getValue();
//	    return (d < 1.0)?1.0:d;
		return getDefaultDtopDistToIntersection();
	  }
	  /**
	   * Get the default stop distance to intersection.
	   *
	   * @return the stop distance to intersection
	   */
	  private double getDefaultDtopDistToIntersection() {
		  return simSetup.getStopDistBeforeIntersection();
	  }
	  /**
	   * Get the number of columns.
	   *
	   * @return the number of columns
	   */
	  public int getNumOfColumns() {
	    //return (int)numOfColumnSlider.getValue();
		return getDefaultNumOfColumns();
	  }
	  /**
	   * Get the default number of columns.
	   *
	   * @return the number of columns
	   */
	  private int getDefaultNumOfColumns() {
		  return simSetup.getColumns();
	  }

	  /**
	   * Get the number of rows.
	   *
	   * @return the number of rows
	   */
	  public int getNumOfRows() {
	    //return (int)numOfRowSlider.getValue();
		return getDefaultNumOfRows();
	  }
	  
	  /**
	   * Get the default number of rows.
	   *
	   * @return the number of rows
	   */
	  private int getDefaultNumOfRows() {
		  return simSetup.getRows(); //original called getColumns() by mistake(?)
	  }

	  /**
	   * Get the number of lanes per road.
	   *
	   * @return the number of lanes per road
	   */
	  public int getLanesPerRoad() {
	    //return (int)lanesPerRoadSlider.getValue();
		return getDefaultLanesPerRoad();
	  }
	  
	  /**
	   * Get the default number of lanes per road.
	   *
	   * @return the number of lanes per road
	   */
	  private int getDefaultLanesPerRoad() {
		  return simSetup.getLanesPerRoad();
	  }

	/**
	 * @return the steeringAngleDeltaSlider
	 */
	public double getSteeringAngleDeltaSlider() {
		return steeringAngleDeltaSlider.getValue();
	}

	/**
	 * @return the speedDeltaSlider
	 */
	public double getSpeedDeltaSlider() {
		return speedDeltaSlider.getValue();
	}

	/**
	 * @return the numCarsPerSimSlider
	 */
	public int getNumCarsPerSimSlider() {
		return (int) numCarsPerSimSlider.getValue();
	}
	/**
	 * @return the max number of active cars
	 */
	public int getMaxNumOfActiveCarsSlider() {
		return (int) maxNumActiveCarsSlider.getValue();
	}
	/**
	 * @return the numPredsPerCondSlider
	 */
	public int getNumPredsPerCondSlider() {
		return (int) numPredsPerCondSlider.getValue();
	}

	/**
	 * @return the explorationFactorSlider
	 */
	public double getExplorationFactorSlider() {
		return explorationFactorSlider.getValue();
	}

	/**
	 * @return the learningFactorSlider
	 */
	public double getLearningFactorSlider() {
		return learningFactorSlider.getValue();
	}

	/**
	 * @return the numRoundsPerGenerationSlider
	 */
	public int getNumRoundsPerGenerationSlider() {
		return (int) numRoundsPerGenerationSlider.getValue();
	}

	/**
	 * @return the numGenerationsSlider
	 */
	public int getNumGenerationsSlider() {
		return (int) numGenerationsSlider.getValue();
	}
	/**
	 * Computes N choose K
	 * 
	 * Adapted from http://stackoverflow.com/questions/2201113/combinatoric-n-choose-r-in-java-math
	 * Retrived March 12, 2017
	 * @param N
	 * @param K
	 * @return N choose K
	 */
	private int nCooseK(int N, int K) {
	    int ret = 1;
	    for (int k = 0; k < K; k++) {
	        ret = ret * (N-k);
	        ret = ret / (k+1);
	    }
	    return ret;
	}

	public int getNumRulesPerRuleSet() {
		return (int) rulesPerRuleRuleSetSlider.getValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShoutAheadParamPanel [getTrafficRate()=" + getTrafficRate() + "]";
	}

	
}
