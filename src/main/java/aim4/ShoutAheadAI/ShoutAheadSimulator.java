
package aim4.ShoutAheadAI;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Random;

import aim4.ShoutAheadAI.predicates.VehiclePredicate;
import aim4.config.Debug;
import aim4.config.DebugPoint;
import aim4.map.BasicMap;
import aim4.map.GridMap;
import aim4.map.SpawnPoint;
import aim4.map.SpawnPoint.SpawnSpec;
import aim4.map.lane.Lane;
import aim4.sim.AutoDriverOnlySimulator;
import aim4.sim.ShoutAheadSimInterface;
import aim4.util.GeomMath;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.BasicAutoVehicle;
import aim4.vehicle.VehicleSimView;
import aim4.vehicle.VehicleSpec;

/**
 * This class represents a driving simulation where every car is autonomous and
 * operating according to the rule-based system outlined by the Shout-Ahead
 * architecture. Reinforcement learning takes place during a run of this
 * simulation. Evolutionary learning can be carried out by running this
 * simulation repeatedly.
 * 
 * 
 * @author christopher.hawk
 */
public class ShoutAheadSimulator extends AutoDriverOnlySimulator implements ShoutAheadSimInterface {
	private static final double COLLISION_BACKOFF_DISTANCE = 1;// meters
	private static final double CLOSE_TO_DEST_DISTANCE = 20;// meter
	private static final String COMMA = ",";
	private static Random rand = new Random();

	private int maxActiveVehicles = ShoutAheadSimSetup.getMaxNumActiveCars();
	private double simTimeLimit = ShoutAheadSimSetup.getSimTimeLimit();
	private Strategy strategy;
	private Object simSyncObject;

	// Strategy fitness parameters
	private int totalBuildingCollisions = 0;
	private int totalCarCollisions = 0;
	private double runningAveAcceleration = 0.0;
	private double runningAveNetDistMovedTowardsDest;
	private double totalDistanceTravelled = 0.0;

	public ShoutAheadSimulator(BasicMap basicMap) {
		super(basicMap);
		VehiclePredicate.sim = this;
	}

	public ShoutAheadSimulator(GridMap basicMap, Strategy strategy, Object simSyncObject) {
		super(basicMap);
		VehiclePredicate.sim = this;
		this.strategy = strategy;
		this.simSyncObject = simSyncObject;
	}

	// the main loop

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized AutoDriverOnlySimStepResult step(double timeStep) {
		if (timesUp()) {
			endSimulation();
		}
		// limit the number of active vehicles
		if (getNumActiveVehicles() < maxActiveVehicles) {
			spawnVehicles(timeStep);
		}
		decideDrivingActions();
		moveVehicles(timeStep);
		detectCollisions();
		updateRuleWeights();
		updateFitness();
		List<Integer> completedVINs = cleanUpCompletedVehicles();
		currentTime += timeStep;
		return new AutoDriverOnlySimStepResult(completedVINs);
	}

	private void endSimulation() {
		Debug.clearShortTermDebugPoints();
		logResults();
		notifyLearningHarness();
	}

	private void logResults() {
		LearningRun.log(runningAveNetDistMovedTowardsDest + COMMA + totalBuildingCollisions + COMMA
				+ totalCarCollisions + COMMA + runningAveAcceleration + COMMA + numOfCompletedVehicles + COMMA
				+ strategy.getFitness() + LearningRun.NEW_LINE);
		LearningRun.getCurrentGen().addStats(runningAveNetDistMovedTowardsDest, totalBuildingCollisions, totalCarCollisions, runningAveAcceleration, numOfCompletedVehicles, strategy.getFitness());
	}

	protected void decideDrivingActions() {
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM: Decide Driving Actions ---------------\n");
		}
		for (VehicleSimView vehicle : vinToVehicles.values()) {
			((ShoutAheadDriverAgent) vehicle.getDriver()).decideNonCommAction();
		}

		for (VehicleSimView vehicle : vinToVehicles.values()) {
			((ShoutAheadDriverAgent) vehicle.getDriver()).decideCommAction();
		}

		for (VehicleSimView vehicle : vinToVehicles.values()) {
			((ShoutAheadDriverAgent) vehicle.getDriver()).decideFinalAction();
		}

		for (VehicleSimView vehicle : vinToVehicles.values()) {
			((ShoutAheadDriverAgent) vehicle.getDriver()).executeFinalAction();
		}

	}

	private void updateFitness() {
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM: updateFitness---------------\n");
		}
		updateRunningAveAcceleration();
		updateRunningAveNetDistMovedTowardsDest();
		updateTotalDistanceTravelled();

		double fitness = 0;
		fitness += runningAveNetDistMovedTowardsDest * ShoutAheadSimSetup.getTotalNetDistanceTowardsDestWeight();
		fitness += totalDistanceTravelled * ShoutAheadSimSetup.getTotalDistanceTravelledWeight();
		fitness += runningAveAcceleration * ShoutAheadSimSetup.getTotalAccelWeight();
		fitness += numOfCompletedVehicles * ShoutAheadSimSetup.getTotalCompletedVehiclesWeight();
		fitness += totalBuildingCollisions * ShoutAheadSimSetup.getTotalBuildingCollisonsWeight();
		fitness += totalCarCollisions * ShoutAheadSimSetup.getTotalCarCollisonsWeight();

		strategy.setFitness(fitness);
	}

	private void updateRunningAveAcceleration() {
		for (VehicleSimView v : vinToVehicles.values()) {
			runningAveAcceleration = ((runningAveAcceleration + v.getAcceleration()) / 2.0);
		}

	}

	private void updateRunningAveNetDistMovedTowardsDest() {
		for (VehicleSimView v : vinToVehicles.values()) {
			runningAveNetDistMovedTowardsDest += v.getChangeInDistanceFromDestination();
		}
	}

	private void updateTotalDistanceTravelled() {
		for (VehicleSimView v : vinToVehicles.values()) {
			totalDistanceTravelled += v.getDistanceTravelledInLastStep();
		}
	}

	private void notifyLearningHarness() {
		synchronized (simSyncObject) {
			simSyncObject.notify();
		}
	}

	private void detectCollisions() {
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM: detectCollisions---------------\n");
		}
		if (!vinToVehicles.isEmpty()) {
			detectVehicleCollisions();
			detectBuildingCollisions();
			// detectMapBoundaryCollisions();TODO: does not work. Maybe remove?
		}
	}

	private void detectVehicleCollisions() {
		VehicleSimView v1 = (VehicleSimView) vinToVehicles.values().toArray()[0];
		ShoutAheadDriverAgent d1 = (ShoutAheadDriverAgent) v1.getDriver();
		for (VehicleSimView v2 : vinToVehicles.values()) {

			ShoutAheadDriverAgent d2 = (ShoutAheadDriverAgent) v2.getDriver();
			if (vehicalsHaveCollided(v1, v2)) {
				if (Debug.BEEP_ON_COLLISONS)
					Toolkit.getDefaultToolkit().beep();
				// blame assigned to both drivers
				v1.incrementVehicleCollisionCount();
				stopAndMoveAway(v1, v2.getShape());
				d1.setHasJustHitCar(true);
				v2.incrementVehicleCollisionCount();
				stopAndMoveAway(v2, v1.getShape());
				d2.setHasJustHitCar(true);

				totalCarCollisions += 2;
			}
		}
	}

	private void detectBuildingCollisions() {
		for (VehicleSimView vehicle : vinToVehicles.values()) {
			ShoutAheadDriverAgent driver = (ShoutAheadDriverAgent) vehicle.getDriver();
			for (Rectangle2D building : basicMap.getBuildings()) {
				if (vehicleHasHitBuilding(vehicle, building)) {
					if (Debug.BEEP_ON_COLLISONS)
						Toolkit.getDefaultToolkit().beep();
					vehicle.incrementBuildingCollisionCount();
					stopAndMoveAway(vehicle, building);
					driver.setHasJustHitBuilding(true);
					totalBuildingCollisions++;
				}
			}
		}
	}

	private boolean vehicleHasHitBuilding(VehicleSimView vehicle, Rectangle2D building) {
		return vehicle.getShape().intersects(building);
	}

	private void detectMapBoundaryCollisions() {
		// Debug.addShortTermDebugPoint(new
		// DebugPoint(basicMap.getNorthBoundary(), "north"));
		// Debug.addShortTermDebugPoint(new
		// DebugPoint(basicMap.getEastBoundary(), "east"));
		// Debug.addShortTermDebugPoint(new
		// DebugPoint(basicMap.getSouthBoundary(), "south"));
		// Debug.addShortTermDebugPoint(new
		// DebugPoint(basicMap.getWestBoundary(), "west"));

		for (VehicleSimView v : vinToVehicles.values()) {
			List<Line2D> mapBoundaries = GeomMath.polygonalShapePerimeterSegments(basicMap.getDimensions());
			for (Line2D boundary : mapBoundaries) {
				if (!(boundary.ptLineDist(v.getDestinationPoint()) < 1.0)) {
					if (!((ShoutAheadDriverAgent) v.getDriver()).isGettingHeadStart()) {
						System.out.println("vin " + v.getVIN() + " out of bounds");
						stopAndMoveAway(v, boundary);
					}
				}
			}
		}

	}

	private boolean vehicalsHaveCollided(VehicleSimView v1, VehicleSimView v2) {
		return v1.getShapeForCollisoinDetection().intersects(v2.getShapeForCollisoinDetection().getBounds2D())
		&& v1 != v2;
	}

	/**
	 * Stop and reverse after a collision.
	 * 
	 * @param vehicle
	 */
	private void stopAndMoveAway(VehicleSimView vehicle, Shape obstacle) {
		Point2D resetPoint;
		if (vehicle.getFrontEdge().intersects(obstacle.getBounds2D())) {
			resetPoint = vehicle.getPointAtMiddleRear(COLLISION_BACKOFF_DISTANCE);
			vehicle.resetPositionAfterCrash(resetPoint);
		} else if (vehicle.getBackEdge().intersects(obstacle.getBounds2D())) {
			resetPoint = vehicle.getPointAtMiddleFront(COLLISION_BACKOFF_DISTANCE);
			vehicle.resetPositionAfterCrash(resetPoint);
		} 
	}

	private void updateRuleWeights() {
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM: updateRuleWeights---------------\n");
		}
		for (VehicleSimView vehicle : vinToVehicles.values()) {
			((ShoutAheadDriverAgent) vehicle.getDriver()).updatePrevRuleWeight();
		}
	}

	/**
	 * Create a vehicle at a spawn point with a ShoutAheadDriverAgent
	 * controlling it.
	 *
	 * @param spawnPoint
	 *            the spawn point
	 * @param spawnSpec
	 *            the spawn specification
	 * @return the vehicle
	 */
	@Override
	protected VehicleSimView makeVehicle(SpawnPoint spawnPoint, SpawnSpec spawnSpec) {
		VehicleSpec spec = spawnSpec.getVehicleSpec();
		Lane lane = spawnPoint.getLane();
		// Now just take the minimum of the max velocity of the vehicle, and
		// the speed limit in the lane
		double initVelocity = Math.min(spec.getMaxVelocity(), lane.getSpeedLimit());
		// Obtain a Vehicle
		AutoVehicleSimView vehicle = new BasicAutoVehicle(spec, spawnPoint.getPosition(), spawnPoint.getHeading(),
				spawnPoint.getSteeringAngle(), initVelocity, // velocity
				initVelocity, // target velocity
				spawnPoint.getAcceleration(), spawnSpec.getSpawnTime());
		// Set the driver
		ShoutAheadDriverAgent driver = new ShoutAheadDriverAgent(vehicle, basicMap, this);
		driver.setCurrentLane(lane);
		driver.setSpawnPoint(spawnPoint);
		driver.setDestination(spawnSpec.getDestinationRoad());
		vehicle.setDriver(driver);

		return vehicle;
	}

	public boolean timesUp() {
		return getSimTimeRemaining() <= 0;
	}

	@Override
	protected boolean shouldRemoveVehicle(Rectangle2D mapBoundary, VehicleSimView v) {
		return vehicalHasReachedDestination(mapBoundary, v);
	}

	private boolean vehicalHasReachedDestination(Rectangle2D mapBoundary, VehicleSimView v) {
		return (super.shouldRemoveVehicle(mapBoundary, v) // out of bounds
				&& (v.getDistanceFromDestination(v.getPosition()) < CLOSE_TO_DEST_DISTANCE));
	}

	public Strategy getStrategy() {
		return strategy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShoutAheadSimulator [targetNumCompleteVehicles=");
		builder.append(", maxActiveVehicles=");
		builder.append(maxActiveVehicles);
		builder.append(", strategy=");
		builder.append(strategy);
		builder.append(", complete=");
		builder.append(", vinToVehicles=");
		builder.append(vinToVehicles);
		builder.append(", currentTime=");
		builder.append(currentTime);
		builder.append(", numOfCompletedVehicles=");
		builder.append(numOfCompletedVehicles);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the totalBuildingCollisions
	 */
	@Override
	public int getTotalBuildingCollisions() {
		return totalBuildingCollisions;
	}

	/**
	 * @return the totalCarCollisions
	 */
	@Override
	public int getTotalCarCollisions() {
		return totalCarCollisions;
	}

	/**
	 * @return the totalCarsCompleted
	 */
	@Override
	public int getTotalCarsCompleted() {
		return numOfCompletedVehicles;
	}

	/**
	 * @return the runningAveAcceleration
	 */
	@Override
	public double getRunningAveAccelration() {
		return runningAveAcceleration;
	}

	/**
	 * @return the fitness
	 * @deprecated Use {@link aim4.ShoutAheadAI.Strategy#getFitness()} instead
	 */
	@Override
	public double getFitness() {
		return strategy.getFitness();
	}

	@Override
	public double getSimTimeRemaining() {
		return simTimeLimit - currentTime;
	}

	public double getAveNetDistanceMovedTowardsDest() {
		return runningAveNetDistMovedTowardsDest;
	}

	public static VehicleSimView getRandomVehicle() {
		return vinToVehicles.get(rand.nextInt(vinToVehicles.size()));
	}
}
