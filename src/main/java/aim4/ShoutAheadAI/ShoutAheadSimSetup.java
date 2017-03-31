
package aim4.ShoutAheadAI;

import aim4.config.Debug;
import aim4.config.SimConfig;
import aim4.driver.pilot.V2IPilot;
import aim4.gui.component.LabeledSlider;
import aim4.im.v2i.reservation.ReservationGridManager;
import aim4.map.BasicMap;
import aim4.map.GridMap;
import aim4.map.GridMapUtil;
import aim4.sim.AutoDriverOnlySimulator;
import aim4.sim.Simulator;
import aim4.sim.setup.AutoDriverOnlySimSetup;
import aim4.sim.setup.BasicSimSetup;
import aim4.sim.setup.SimSetup;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadSimSetup extends AutoDriverOnlySimSetup implements SimSetup {
	// Default parameter values
	private static double steeringDelta = 10.0;// degrees
	private static double speedDelta = 5.0;// m/s
	private static int numCarsPerSim = 8;
	private static int maxNumActiveCars = 4;

	private static int numPredsPerCond = 2;
	private static int numRulesPerRuleSet = 100;
	private static double explorationFactor = 0.5;
	private static double learningFactor = 0.5;
	private static int numRoundsPerGeneration = 10;
	private static int numGenerations = 10;

	public ShoutAheadSimSetup(BasicSimSetup basicSimSetup) {
		super(basicSimSetup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Simulator getSimulator() {
		GridMap layout = getMap();

		GridMapUtil.setUniformRandomSpawnPoints(layout, trafficLevel);

		return new ShoutAheadSimulator(layout);
	}
	
	public ShoutAheadSimulator getSimulator(Strategy strategy, Object simSyncObject) {
		GridMap layout = getMap();
		GridMapUtil.setUniformRandomSpawnPoints(layout, trafficLevel);
		return new ShoutAheadSimulator(layout, strategy, simSyncObject);
	}

	public GridMap getMap() {
		double currentTime = 0.0;
		GridMap layout = new GridMap(currentTime, numOfColumns, numOfRows, laneWidth, speedLimit, lanesPerRoad,
				medianSize, distanceBetween);
		return layout;
	}

	/**
	 * @return the steeringDelta
	 */
	public static double getSteeringDelta() {
		return steeringDelta;
	}

	/**
	 * @param steeringDelta
	 *            the steeringDelta to set
	 */
	public void setSteeringDelta(double steeringDelta) {
		ShoutAheadSimSetup.steeringDelta = steeringDelta;
	}

	/**
	 * @return the speedDelta
	 */
	public static double getSpeedDelta() {
		return speedDelta;
	}

	/**
	 * @param speedDelta
	 *            the speedDelta to set
	 */
	public void setSpeedDelta(double speedDelta) {
		ShoutAheadSimSetup.speedDelta = speedDelta;
	}

	/**
	 * @return the numCarsPerSim
	 */
	public static int getNumCarsPerSim() {
		return numCarsPerSim;
	}

	/**
	 * @param numCarsPerSim
	 *            the numCarsPerSim to set
	 */
	public void setNumCarsPerSim(int numCarsPerSim) {
		ShoutAheadSimSetup.numCarsPerSim = numCarsPerSim;
	}

	/**
	 * @return the maxNumActiveCars
	 */
	public static int getMaxNumActiveCars() {
		return maxNumActiveCars;
	}

	/**
	 * @param maxNumActiveCars the maxNumActiveCars to set
	 */
	public void setMaxNumActiveCars(int maxNumActiveCars) {
		ShoutAheadSimSetup.maxNumActiveCars = maxNumActiveCars;
	}

	/**
	 * @return the numPredsPerCond
	 */
	public static int getNumPredsPerCond() {
		return numPredsPerCond;
	}

	/**
	 * @param numPredsPerCond
	 *            the numPredsPerCond to set
	 */
	public void setNumPredsPerCond(int numPredsPerCond) {
		ShoutAheadSimSetup.numPredsPerCond = numPredsPerCond;
	}

	/**
	 * @return the explorationFactor
	 */
	public static double getExplorationFactor() {
		return explorationFactor;
	}

	/**
	 * @param explorationFactor
	 *            the explorationFactor to set
	 */
	public void setExplorationFactor(double explorationFactor) {
		ShoutAheadSimSetup.explorationFactor = explorationFactor;
	}

	/**
	 * @return the learningFactor
	 */
	public static double getLearningFactor() {
		return learningFactor;
	}

	/**
	 * @param learningFactor
	 *            the learningFactor to set
	 */
	public void setLearningFactor(double learningFactor) {
		ShoutAheadSimSetup.learningFactor = learningFactor;
	}

	/**
	 * @return the numRoundsPerGeneration
	 */
	public static int getNumRoundsPerGeneration() {
		return numRoundsPerGeneration;
	}

	/**
	 * @param numRoundsPerGeneration
	 *            the numRoundsPerGeneration to set
	 */
	public void setNumRoundsPerGeneration(int numRoundsPerGeneration) {
		ShoutAheadSimSetup.numRoundsPerGeneration = numRoundsPerGeneration;
	}

	/**
	 * @return the numGenerations
	 */
	public static int getNumGenerations() {
		return numGenerations;
	}

	/**
	 * @param numGenerations
	 *            the numGenerations to set
	 */
	public void setNumGenerations(int numGenerations) {
		ShoutAheadSimSetup.numGenerations = numGenerations;
	}

	/**
	 * 
	 * @return the number of rules per rule set
	 */
	public static int getNumRulesPerRuleSet() {
		return numRulesPerRuleSet;
	}

	/**
	 * 
	 * @param numRulesPerRuleSet
	 *            the number of rules per rule set to set
	 */
	public void setNumRulesPerRuleSet(int numRulesPerRuleSet) {
		ShoutAheadSimSetup.numRulesPerRuleSet = numRulesPerRuleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "steeringDelta=" + steeringDelta + "\nspeedDelta=" + speedDelta + "\nnumCarsPerSim=" + numCarsPerSim
				+ "\nnumPredsPerCond=" + numPredsPerCond + "\nnumRulesPerRuleSet=" + numRulesPerRuleSet
				+ "\nexplorationFactor=" + explorationFactor + "\nlearningFactor=" + learningFactor
				+ "\nnumRoundsPerGeneration=" + numRoundsPerGeneration + "\nnumGenerations=" + numGenerations
				+ "\n\nnumOfColumns=" + numOfColumns + "\nnumOfRows=" + numOfRows + "\nlaneWidth=" + laneWidth
				+ "\nspeedLimit=" + speedLimit + "\nlanesPerRoad=" + lanesPerRoad + "\nmedianSize=" + medianSize
				+ "\ndistanceBetween=" + distanceBetween + "\ntrafficLevel=" + trafficLevel
				+ "\nstopDistBeforeIntersection=" + stopDistBeforeIntersection;
	}



}
