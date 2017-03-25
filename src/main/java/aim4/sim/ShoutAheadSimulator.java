
package aim4.sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import aim4.ShoutAheadAI.Strategy;
import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.config.Debug;
import aim4.driver.AutoDriver;
import aim4.driver.ShoutAheadDriverAgent;
import aim4.map.BasicMap;
import aim4.map.GridMap;
import aim4.map.Road;
import aim4.map.SpawnPoint;
import aim4.map.SpawnPoint.SpawnSpec;
import aim4.map.lane.Lane;
import aim4.sim.AutoDriverOnlySimulator.AutoDriverOnlySimStepResult;
import aim4.sim.setup.ShoutAheadSimSetup;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.BasicAutoVehicle;
import aim4.vehicle.VehicleSimView;
import aim4.vehicle.VehicleSpec;
import aim4.vehicle.VehicleSpecDatabase;
import aim4.vehicle.VinRegistry;
import aim4.vehicle.BasicVehicle;

/**
 * This class represents a driving simulation where every car is autonomous and
 * operating according to the rule-based system outlined by the Shout-Ahead
 * architecture. Reinforcement learning takes place during a run of this
 * simulation. Evolutionary learning can be carried out by running this
 * simulation repeatedly.
 * 
 * 
 * 
 * @author christopher.hawk
 */
public class ShoutAheadSimulator extends AutoDriverOnlySimulator implements Simulator {
	private int targetNumCompleteVehicles = ShoutAheadSimSetup.getNumCarsPerSim();
	private int maxActiveVehicles = 3;// TODO
	private Strategy strategy;

	public ShoutAheadSimulator(BasicMap basicMap) {
		super(basicMap);
		Predicate.sim = this;
	}

	public ShoutAheadSimulator(GridMap basicMap, Strategy strategy) {
		super(basicMap);
		Predicate.sim = this;
		this.strategy = strategy;

	}

	// the main loop

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized AutoDriverOnlySimStepResult step(double timeStep) {
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("--------------------------------------\n");
			System.err.printf("------SIM:spawnVehicles---------------\n");
		}
		// limit the number of active vehicles
		if ((getNumActiveVehicles() < maxActiveVehicles) && !isComplete())
			spawnVehicles(timeStep);
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM:letDriversAct---------------\n");
		}
		letDriversAct();
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM:communication---------------\n");
		}
//		communication();// TODO: remove?
//		if (Debug.PRINT_SIMULATOR_STAGE) {
//			System.err.printf("------SIM:moveVehicles---------------\n");
//		}
		moveVehicles(timeStep);
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM:cleanUpCompletedVehicles---------------\n");
		}
		detectCollisions();
		List<Integer> completedVINs = cleanUpCompletedVehicles();
		currentTime += timeStep;

		return new AutoDriverOnlySimStepResult(completedVINs);
	}

	private void detectCollisions() {
		// TODO collision detection
		
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

	public boolean isComplete() {
		return numOfCompletedVehicles >= targetNumCompleteVehicles;
	}
}
