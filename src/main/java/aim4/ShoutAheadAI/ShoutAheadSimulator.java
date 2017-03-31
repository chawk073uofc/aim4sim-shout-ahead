
package aim4.ShoutAheadAI;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import aim4.ShoutAheadAI.predicates.Predicate;
import aim4.config.Debug;
import aim4.driver.AutoDriver;
import aim4.map.BasicMap;
import aim4.map.GridMap;
import aim4.map.Road;
import aim4.map.SpawnPoint;
import aim4.map.SpawnPoint.SpawnSpec;
import aim4.map.lane.Lane;
import aim4.sim.AutoDriverOnlySimulator;
import aim4.sim.Simulator;
import aim4.sim.AutoDriverOnlySimulator.AutoDriverOnlySimStepResult;
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
 * @author christopher.hawk
 */
public class ShoutAheadSimulator extends AutoDriverOnlySimulator implements Simulator {
	private int targetNumCompleteVehicles = ShoutAheadSimSetup.getNumCarsPerSim();
	private int maxActiveVehicles = ShoutAheadSimSetup.getMaxNumActiveCars();
	private Strategy strategy;
	private boolean complete = false;
	private Object simSyncObject;

	public ShoutAheadSimulator(BasicMap basicMap) {
		super(basicMap);
		Predicate.sim = this;
	}

	public ShoutAheadSimulator(GridMap basicMap, Strategy strategy, Object simSyncObject) {
		super(basicMap);
		Predicate.sim = this;
		this.strategy = strategy;
		this.simSyncObject = simSyncObject;
	}

	// the main loop

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized AutoDriverOnlySimStepResult step(double timeStep) {
		if(complete)
			notifyLearningHarness();
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("--------------------------------------\n");
			System.err.printf("------SIM:spawnVehicles---------------\n");
		}
		// limit the number of active vehicles
		if ((getNumActiveVehicles() < maxActiveVehicles) && !complete)
			spawnVehicles(timeStep);
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM:letDriversAct---------------\n");
		}
		 if(Debug.SHOW_STRATEGY){
			System.out.println(strategy);  
		  }
		letDriversAct();
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM:communication---------------\n");
		}
//		communication();// TODO: remove? - replace with shout ahead?
//		if (Debug.PRINT_SIMULATOR_STAGE) {
//			System.err.printf("------SIM:moveVehicles---------------\n");
//		}
		moveVehicles(timeStep);
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM: detectCollisions---------------\n");
		}
		detectCollisions();
		if (Debug.PRINT_SIMULATOR_STAGE) {
			System.err.printf("------SIM:cleanUpCompletedVehicles---------------\n");
		}
		List<Integer> completedVINs = cleanUpCompletedVehicles();
		currentTime += timeStep;
		complete = isComplete();
		if (complete && Debug.PRINT_SIMULATOR_STAGE){
				System.err.printf("------SIM COMPLETE---------------\n");
		}
		return new AutoDriverOnlySimStepResult(completedVINs);
	}

	private void notifyLearningHarness() {
		synchronized(simSyncObject) {
			simSyncObject.notify();
		}
	}

	private void detectCollisions() {
		if(!vinToVehicles.isEmpty()){
			detectVehicleCollisions();
		    detectBuildingCollisions();	
		  }
	}

	private void detectBuildingCollisions() {
		for(VehicleSimView vehicle: vinToVehicles.values()){
		    for(Rectangle2D building: basicMap.getBuildings()){
		    	if(vehicle.getShape().intersects(building)){
		    		vehicle.incrementBuildingCollisionCount();
		    	}
		    }
		}
	}

	private void detectVehicleCollisions() {
		VehicleSimView v1 = (VehicleSimView) vinToVehicles.values().toArray()[0];
		for(VehicleSimView v2 : vinToVehicles.values()) {
			if(v1.getShape().intersects(v2.getShape().getBounds2D()) && v1 != v2){
				v1.incrementVehicleCollisionCount();
				v2.incrementVehicleCollisionCount();
			}
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

	public boolean isComplete() {
		return numOfCompletedVehicles >= targetNumCompleteVehicles;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShoutAheadSimulator [targetNumCompleteVehicles=");
		builder.append(targetNumCompleteVehicles);
		builder.append(", maxActiveVehicles=");
		builder.append(maxActiveVehicles);
		builder.append(", strategy=");
		builder.append(strategy);
		builder.append(", complete=");
		builder.append(complete);
		builder.append(", vinToVehicles=");
		builder.append(vinToVehicles);
		builder.append(", currentTime=");
		builder.append(currentTime);
		builder.append(", numOfCompletedVehicles=");
		builder.append(numOfCompletedVehicles);
		builder.append("]");
		return builder.toString();
	}
}
