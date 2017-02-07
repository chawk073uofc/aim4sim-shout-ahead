
package aim4.sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import aim4.config.Debug;
import aim4.driver.ShoutAheadDriverAgent;
import aim4.map.BasicMap;
import aim4.map.GridMap;
import aim4.map.Road;
import aim4.map.SpawnPoint;
import aim4.map.SpawnPoint.SpawnSpec;
import aim4.map.lane.Lane;
import aim4.sim.AutoDriverOnlySimulator.AutoDriverOnlySimStepResult;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleSimView;
import aim4.vehicle.VehicleSpec;
import aim4.vehicle.VehicleSpecDatabase;
import aim4.vehicle.VinRegistry;
import aim4.vehicle.BasicVehicle;
/**
 * This class represents a driving simulation where every car is autonomous and operating according to the
 * rule-based system outlined by the Shout-Ahead architecture. Reinforcement learning takes place during a 
 * run of this simulation. Evolutionary learning can be carried out by running this simulation repeatedly.
 *  
 *  
 *  
 * @author christopher.hawk
 */
public class ShoutAheadSimulator extends AutoDriverOnlySimulator implements Simulator {

	public ShoutAheadSimulator(BasicMap basicMap) {
		super(basicMap);
		//Spawn 4 vehicles at the end of each road. Each should be randomly assigned a destination (North, South, East, West)
		//not equal to it's origin. 
			
			//Spawn Northbound car
		   GridMap map = (GridMap) basicMap;
		   Road verticalRoad = map.getVerticalRoads().get(0);
		   Lane northBoundLane = verticalRoad.getLanes().get(0);

		   SpawnPoint spawnPoint = map.makeSpawnPoint(currentTime, northBoundLane);
		   VehicleSpec vehicleSpec = VehicleSpecDatabase.getVehicleSpecByName("COUPE");
		   SpawnSpec spawnSpec = new SpawnSpec(currentTime, vehicleSpec, verticalRoad);//hopefully destination is North
		   AutoVehicleSimView vehicle = (AutoVehicleSimView) makeVehicle(spawnPoint, spawnSpec);
		   ShoutAheadDriverAgent driverAgent = new ShoutAheadDriverAgent(vehicle, basicMap);
		   driverAgent.setSpawnPoint(spawnPoint);
		   driverAgent.setDestination(verticalRoad);
		   vehicle.setDriver(driverAgent);
		  
           VinRegistry.registerVehicle(vehicle); // Get vehicle a VIN number
           vinToVehicles.put(vehicle.getVIN(), vehicle);
           
      
			//Spawn East bound car
           Road horizontalRoad = map.getHorizontalRoads().get(0);
		   Lane eastBoundLane = horizontalRoad.getLanes().get(0);
		   
		   SpawnPoint spawnPointE = map.makeSpawnPoint(currentTime, eastBoundLane);
		   SpawnSpec spawnSpecE = new SpawnSpec(currentTime, vehicleSpec, horizontalRoad);//hopefully destination is North
		   AutoVehicleSimView vehicleE = (AutoVehicleSimView) makeVehicle(spawnPointE, spawnSpecE);
		   ShoutAheadDriverAgent driverAgentE = new ShoutAheadDriverAgent(vehicleE, basicMap);
		   driverAgentE.setSpawnPoint(spawnPointE);
		   driverAgentE.setDestination(horizontalRoad);
		   vehicleE.setDriver(driverAgentE);
		  
           VinRegistry.registerVehicle(vehicleE); // Get vehicle a VIN number
           vinToVehicles.put(vehicleE.getVIN(), vehicleE);


		   
			//Spawn South bound car
       //   List<Lane> lanes = verticalRoad.getLanes();
		   Road southBoundRoad = map.getVerticalRoads().get(1);

		   Lane southBoundLane = southBoundRoad.getLanes().get(0);
		   
		   SpawnPoint spawnPointS = map.makeSpawnPoint(currentTime, southBoundLane);
		   SpawnSpec spawnSpecS = new SpawnSpec(currentTime, vehicleSpec, southBoundRoad);
		   AutoVehicleSimView vehicleS = (AutoVehicleSimView) makeVehicle(spawnPointS, spawnSpecS);
		   ShoutAheadDriverAgent driverAgentS = new ShoutAheadDriverAgent(vehicleS, basicMap);
		   driverAgentS.setSpawnPoint(spawnPointS);
		   driverAgentS.setDestination(verticalRoad);
		   vehicleS.setDriver(driverAgentS);
		  
           VinRegistry.registerVehicle(vehicleS); // Get vehicle a VIN number
           vinToVehicles.put(vehicleS.getVIN(), vehicleS);
           
			//Spawn West bound car
			Road westBoundRoad = map.getHorizontalRoads().get(1);
			Lane westBoundLane = westBoundRoad.getLanes().get(0);
		   
		   SpawnPoint spawnPointW = map.makeSpawnPoint(currentTime, westBoundLane);
		   SpawnSpec spawnSpecW = new SpawnSpec(currentTime, vehicleSpec, westBoundRoad);//dest not right
		   AutoVehicleSimView vehicleW = (AutoVehicleSimView) makeVehicle(spawnPointW, spawnSpecW);
		   ShoutAheadDriverAgent driverAgentW = new ShoutAheadDriverAgent(vehicleW, basicMap);
		   driverAgentW.setSpawnPoint(spawnPointW);
		   driverAgentW.setDestination(westBoundRoad);//dest not right
		   vehicleW.setDriver(driverAgentW);
		  
           VinRegistry.registerVehicle(vehicleW); // Get vehicle a VIN number
           vinToVehicles.put(vehicleW.getVIN(), vehicleW);
		}

	  // the main loop

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public synchronized AutoDriverOnlySimStepResult step(double timeStep) {
//	    if (Debug.PRINT_SIMULATOR_STAGE) {
//	      System.err.printf("--------------------------------------\n");
//	      System.err.printf("------SIM:spawnVehicles---------------\n");
//	    }
//	    spawnVehicles(timeStep);
//	    if (Debug.PRINT_SIMULATOR_STAGE) {
//	      System.err.printf("------SIM:provideSensorInput---------------\n");
//	    }
//	    provideSensorInput();
	    if (Debug.PRINT_SIMULATOR_STAGE) {
	      System.err.printf("------SIM:letDriversAct---------------\n");
	    }
	   letDriversAct();
//	    if (Debug.PRINT_SIMULATOR_STAGE) {
//	      System.err.printf("------SIM:letIntersectionManagersAct--------------\n");
//	    }
//	    letIntersectionManagersAct(timeStep);
	    if (Debug.PRINT_SIMULATOR_STAGE) {
	      System.err.printf("------SIM:communication---------------\n");
	    }
	    communication();//TODO: remove?
	    if (Debug.PRINT_SIMULATOR_STAGE) {
	      System.err.printf("------SIM:moveVehicles---------------\n");
	    }
	    moveVehicles(timeStep);//TODO:write SAVehicalSimView.move()
	    if (Debug.PRINT_SIMULATOR_STAGE) {
	      System.err.printf("------SIM:cleanUpCompletedVehicles---------------\n");
	    }
	    List<Integer> completedVINs = cleanUpCompletedVehicles();
	    currentTime += timeStep;
	    // debug
	    checkClocks();

	    return new AutoDriverOnlySimStepResult(completedVINs);
	  }
	
	  /**
	   * If this simulation includes communication among agents, allow agents to broadcast their intended actions to others. 
	   */
	  @Override
	  protected void communication() {
		 
	  }
	  

	
}
