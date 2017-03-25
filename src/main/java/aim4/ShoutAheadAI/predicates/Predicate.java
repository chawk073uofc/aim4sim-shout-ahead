/**
 * 
 */
package aim4.ShoutAheadAI.predicates;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.Set;

import aim4.sim.ShoutAheadSimulator;
import aim4.util.GeomMath;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleSimView;

/**
 * Defines predicates which form the conditions for ShoutAheadRules.
 * 
 * @author Chris Hawk
 *
 */
public enum Predicate {
	//HEADING PREDICATES
		HEADING_NORTH{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getHeading() > GeomMath.PI) && (vehicle.getHeading() < GeomMath.TWO_PI);
			}
		},
		HEADING_EAST{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getHeading() > GeomMath.ONE_AND_HALF_PI) || (vehicle.getHeading() < GeomMath.HALF_PI);
			}
		},
		HEADING_SOUTH{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getHeading() > 0) && (vehicle.getHeading() < GeomMath.PI);
			}
		},
		HEADING_WEST{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getHeading() > GeomMath.HALF_PI) && (vehicle.getHeading() < GeomMath.ONE_AND_HALF_PI);
			}
		},
	//POSITION PREDICATES
		//TODO: make sure destination of driver agents is set correctly
		DESTINATION_NORTH_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getPosition().getY() > vehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getY());
			}
		},
		DESTINATION_EAST_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getPosition().getX() < vehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getX());
			}
		},DESTINATION_SOUTH_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getPosition().getY() < vehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getY());
			}
		},
		DESTINATION_WEST_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return (vehicle.getPosition().getX() > vehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getX());
			}
		},
		
	//VEHICLE SPEED PREDICATES
		SPEED_GT_25_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView vehicle){
				return vehicle.gaugeVelocity() > 25.0;
			}
		},
		SPEED_LT_25_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView vehicle){
				return vehicle.gaugeVelocity() < 25.0;
			}
		},
		SPEED_GT_15_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView vehicle){
				return vehicle.gaugeVelocity() > 15.0;
			}
		},
		SPEED_LT_15_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView vehicle){
				return vehicle.gaugeVelocity() < 15.0;
			}
		},
		SPEED_GT_5_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView vehicle){
				return vehicle.gaugeVelocity() > 15.0;
			}
		},
		SPEED_LT_5_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView vehicle){
				return vehicle.gaugeVelocity() < 15.0;
			}
		},
				
	//OBSTACLE PROXIMITY PREDICATES
		OBSTACLE_LT_10_METERS_IN_FRONT{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.IN_FRONT, 10, vehicle);
			}
		},
		OBSTACLE_LT_10_METERS_RIGHT{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.RIGHT, 10, vehicle);
			}
		},
		OBSTACLE_LT_10_METERS_BEHIND{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.BEHIND, 10, vehicle);
			}
		},
		OBSTACLE_LT_10_METERS_LEFT{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.LEFT, 10, vehicle);
			}
		},
		
		OBSTACLE_LT_20_METERS_IN_FRONT{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.IN_FRONT, 20, vehicle);
			}
		},
		OBSTACLE_LT_20_METERS_RIGHT{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.RIGHT, 20, vehicle);
			}
		},
		OBSTACLE_LT_20_METERS_BEHIND{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.BEHIND, 20, vehicle);
			}
		},
		OBSTACLE_LT_20_METERS_LEFT{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return obstacleIsNearby(Direction.LEFT, 20, vehicle);
			}
		};
		
	
	
	
		public static ShoutAheadSimulator sim;//common to all elements?
		private static Random rand = new Random();
		
		/**
		 * Returns true if the predicate is true at a particular point in the simulation for a particular car.
		 * @return	true if the predicate is true at a particular point in the simulation for a particular car.
		 */
		public abstract boolean isTrue(AutoVehicleSimView vehicle);
		/**
		 * Returns a random predicate.
		 * @return a random predicate.
		 */
		public static Predicate getRandomPredicate(){
			int numPredicates = values().length;
			int randPredIndex = rand.nextInt(numPredicates);
			return values()[randPredIndex];
		}
		
		/**
		 * Show the truth values for all predicates for the given vehicle.
		 * @param vehicle
		 * @return the truth values for all predicates for the given vehicle.
		 */
		public static String showAllPredicates(AutoVehicleSimView vehicle){
			String str = "";
			str += "PREDICATES AND TRUTH VALUES FOR VEHICLE " + vehicle.getVIN() + ":\n\n";
			
			int i = 1;
			for(Predicate pred: Predicate.values()) {
				str += pred.name() + "\t\t" + pred.isTrue(vehicle) + "\n";
			if((i % 4) == 0)
				str += "\n";
			i++;
			}
			str += "\n";

			return str;
		}
		
		/**
		 * Show the truth values for all predicates for the given vehicle that are true at a particular point in the simulation.
		 * @param vehicle
		 * @return the truth values for all predicates for the given vehicle that are true at a particular point in the simulation.
		 */
		public static String showTruePredicates(AutoVehicleSimView vehicle){
			String str = "";
			str += "TRUE PREDICATES FOR VEHICLE " + vehicle.getVIN() + ":\n\n";
			str += "**Destination =" + vehicle.getDriver().getDestination() + "\n";
			for(Predicate pred: Predicate.values()){
				if(pred.isTrue(vehicle))
					str += pred.name() + "\n";
			}
			str += "\n";
			return str;
		}
		/**
		 * Check if there is at least one vehicle or building within the specified distance of this vehicle in the given direction.
		 * @param direction
		 * @param distance
		 * @param vehicle
		 * @return True if there is at least one vehicle or building within the specified distance of this vehicle in the given direction.
		 */
		private static boolean obstacleIsNearby(Direction direction, int distance, AutoVehicleSimView vehicle){
			//draw line, see if it intersects with a car or road boundary
			Point2D centerPoint = vehicle.getCenterPoint();
			//Draw line projected from the center of the vehicle to a point distance meters in from of the vehicle
			Point2D projectedPoint = null;
			switch(direction){
			case IN_FRONT:
				projectedPoint = vehicle.getPointAtMiddleFront((double) distance);
				break;
			case RIGHT:
				projectedPoint = vehicle.getPointAtMiddleRight((double) distance);
				break;
			case BEHIND:
				projectedPoint = vehicle.getPointAtMiddleRear((double) distance);
				break;
			case LEFT:
				projectedPoint = vehicle.getPointAtMiddleLeft((double) distance);
				break;
			}
			//detect nearby cars
			Line2D.Double line = new Line2D.Double(centerPoint, projectedPoint);
			Set<VehicleSimView> otherVehicles = sim.getActiveVehicles();
			otherVehicles.remove(vehicle);
			for(VehicleSimView v : otherVehicles){
				if(line.intersects((Rectangle2D) v.gaugeShape().getBounds2D()))
					return true;
			}
			//detect nearby buildings
			for(Rectangle2D.Double building : sim.getMap().getFields()){
				if(line.intersects(building))
					return true;
			}
			return false;
		}
		
		
		/**
		 * The cardinal directions. 
		 * 
		 * @author christopher.hawk
		 */
		public enum Direction{
			IN_FRONT,
			BEHIND,
			RIGHT,
			LEFT
		}
}
