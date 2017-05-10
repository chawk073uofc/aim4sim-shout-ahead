
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
	/**The number that will fit on one page*/
	private static final int DEMO_NUM_RULES = 27;
	
	// Default parameter values
	private static double steeringDelta = 1.0;// degrees
	private static double speedDelta = 1.0;// m/s
	private static double simTimeLimit = 50.0;
	private static int maxNumActiveCars = 8;

	private static int numPredsPerCond = 3;
	private static int numRulesPerRuleSet = DEMO_NUM_RULES;
	private static double explorationFactor = 0.3;
	private static double learningFactor = 0.5;
	private static int numRoundsPerGeneration = 10;
	private static int numGenerations = 100;
	
	private static int numHeadStartActions = 30;
	 
	//Reinforcement learning reward weights
	private static double distanceWeight = 10;
	private static double accelerationWeight = -0.1;
	private static double carCollisionWeight = -3;
	private static double buildingCollisionWeight = -5;
	
	//Evolutionary learning fitness weights. 
	private static double totalNetDistanceTowardsDestWeight = 10.0;
	private static double totalAccelWeight = -0.1;
	private static double totalDistanceTravelledWeight = -0.1;
	private static double totalCompletedVehiclesWeight = 50;
	private static double totalBuildingCollisonsWeight = -10;
	private static double totalCarCollisonsWeight = -50;

	private static double fractionStratsToCarryForward = 0.3;//Carry the best 1/3 of the strategies through to the next generation
	private static double offspringFraction = 0.3; // 1/3 of the strategies in gen n+1 are created by breeding the best strategies in gen n
	private static double commRuleProb = 0.5; // 1/3 chance of choosing a communicating rule in the case that there are both comm and non-comm rules applicalbe to the given situation and vehicle and the weight of the comm rule is less than that of the comm rule.

	//sarsa params
	private static double alpha = 0.3; // discount rate
	private static double gamma = 0.5; // learning factor
	
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

//	/**
//	 * @return the numCarsPerSim
//	 */
//	public static int getNumCarsPerSim() {
//		return numCarsPerSim;
//	}

	/**
//	 * @param numCarsPerSim
//	 *            the numCarsPerSim to set
//	 */
//	public void setNumCarsPerSim(int numCarsPerSim) {
//		ShoutAheadSimSetup.numCarsPerSim = numCarsPerSim;
//	}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("steeringDelta=");
		builder.append(steeringDelta);
		builder.append("\n speedDelta=");
		builder.append(speedDelta);
		builder.append("\n numCarsPerSim=");
		builder.append("\n simTimeLimit=");
		builder.append(simTimeLimit);
		builder.append("\n maxNumActiveCars=");
		builder.append(maxNumActiveCars);
		builder.append("\n numPredsPerCond=");
		builder.append(numPredsPerCond);
		builder.append("\n numRulesPerRuleSet=");
		builder.append(numRulesPerRuleSet);
		builder.append("\n explorationFactor=");
		builder.append(explorationFactor);
		builder.append("\n learningFactor=");
		builder.append(learningFactor);
		builder.append("\n numRoundsPerGeneration=");
		builder.append(numRoundsPerGeneration);
		builder.append("\n numGenerations=");
		builder.append(numGenerations);
		builder.append("\n numHeadStartActions=");
		builder.append(numHeadStartActions);
		builder.append("\n distanceWeight=");
		builder.append(distanceWeight);
		builder.append("\n accelerationWeight=");
		builder.append(accelerationWeight);
		builder.append("\n carCollisionWeight=");
		builder.append(carCollisionWeight);
		builder.append("\n buildingCollisionWeight=");
		builder.append(buildingCollisionWeight);
		builder.append("\n totalNetDistanceTowardsDestWeight=");
		builder.append(totalNetDistanceTowardsDestWeight);
		builder.append("\n totalAccelWeight=");
		builder.append(totalAccelWeight);
		builder.append("\n totalDistanceTravelledWeight=");
		builder.append(totalDistanceTravelledWeight);
		builder.append("\n totalCompletedVehiclesWeight=");
		builder.append(totalCompletedVehiclesWeight);
		builder.append("\n totalBuildingCollisonsWeight=");
		builder.append(totalBuildingCollisonsWeight);
		builder.append("\n totalCarCollisonsWeight=");
		builder.append(totalCarCollisonsWeight);
		builder.append("\n fractionStratsToCarryForward=");
		builder.append(fractionStratsToCarryForward);
		builder.append("\n offspringFraction=");
		builder.append(offspringFraction);
		builder.append("\n commRuleProb=");
		builder.append(commRuleProb);
		builder.append("\n alpha=");
		builder.append(alpha);
		builder.append("\n gamma=");
		builder.append(gamma);
		builder.append("\n numOfColumns=");
		builder.append(numOfColumns);
		builder.append("\n numOfRows=");
		builder.append(numOfRows);
		builder.append("\n laneWidth=");
		builder.append(laneWidth);
		builder.append("\n speedLimit=");
		builder.append(speedLimit);
		builder.append("\n lanesPerRoad=");
		builder.append(lanesPerRoad);
		builder.append("\n medianSize=");
		builder.append(medianSize);
		builder.append("\n distanceBetween=");
		builder.append(distanceBetween);
		builder.append("\n trafficLevel=");
		builder.append(trafficLevel);
		builder.append("\n stopDistBeforeIntersection=");
		builder.append(stopDistBeforeIntersection);
		return builder.toString();
	}

	/**
	 * @return the numStartAheadActions
	 */
	public static int getNumHeadStartActions() {
		return numHeadStartActions;
	}

	/**
	 * @return the distanceWeight
	 */
	public static double getDistanceWeight() {
		return distanceWeight;
	}

	/**
	 * @return the accelerationWeight
	 */
	public static double getAccelerationWeight() {
		return accelerationWeight;
	}

	/**
	 * @return the carCollisionWeight
	 */
	public static double getCarCollisionWeight() {
		return carCollisionWeight;
	}

	/**
	 * @return the buildingCollisionWeight
	 */
	public static double getBuildingCollisionWeight() {
		return buildingCollisionWeight;
	}

	/**
	 * @param numStartAheadActions the numStartAheadActions to set
	 */
	public void setNumStartAheadActions(int numStartAheadActions) {
		ShoutAheadSimSetup.numHeadStartActions = numStartAheadActions;
	}

	/**
	 * @param distanceWeight the distanceWeight to set
	 */
	public void setDistanceWeight(double distanceWeight) {
		ShoutAheadSimSetup.distanceWeight = distanceWeight;
	}

	/**
	 * @param accelerationWeight the accelerationWeight to set
	 */
	public void setAccelerationWeight(double accelerationWeight) {
		ShoutAheadSimSetup.accelerationWeight = accelerationWeight;
	}

	/**
	 * @param carCollisionWeight the carCollisionWeight to set
	 */
	public void setCarCollisionWeight(double carCollisionWeight) {
		ShoutAheadSimSetup.carCollisionWeight = carCollisionWeight;
	}

	/**
	 * @param buildingCollisionWeight the buildingCollisionWeight to set
	 */
	public void setBuildingCollisionWeight(double buildingCollisionWeight) {
		ShoutAheadSimSetup.buildingCollisionWeight = buildingCollisionWeight;
	}

	public static double getSimTimeLimit() {
		return simTimeLimit;
	}

	public static double getTotalNetDistanceTowardsDestWeight() {
		return totalNetDistanceTowardsDestWeight;
	}

	public static double getTotalAccelWeight() {
		return totalAccelWeight;
	}

	public static double getTotalCompletedVehiclesWeight() {
		return totalCompletedVehiclesWeight;
	}

	public static double getTotalBuildingCollisonsWeight() {
		return totalBuildingCollisonsWeight;
	}

	public static double getTotalCarCollisonsWeight() {
		return totalCarCollisonsWeight;
	}

	public static double getTotalDistanceTravelledWeight() {
		return totalDistanceTravelledWeight;
	}

	public static double getFractionStratsToCarryForward() {
		return fractionStratsToCarryForward;
	}

	public static double getOffspringFraction() {
		return offspringFraction;
	}

	public static double getCommRuleProb() {
		return commRuleProb ;
	}

	public static double getAlpha() {
		return alpha;
	}

	public static double getGamma() {
		return gamma;
	}	
}