/**
 * 
 */
package aim4.ShoutAheadAI.predicates;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.Set;

import aim4.ShoutAheadAI.ShoutAheadSimulator;
import aim4.config.Debug;
import aim4.config.DebugPoint;
import aim4.util.GeomMath;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleSimView;

/**
 * Defines predicates which form the conditions for ShoutAheadRules.
 * 
 * @author Chris Hawk
 *
 */
public enum VehiclePredicate /*implements Predicate*/{
	//HEADING PREDICATES
		HEADING_NORTH{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getHeading() > GeomMath.PI) && (thisVehicle.getHeading() < GeomMath.TWO_PI);
			}
		},
		HEADING_EAST{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getHeading() > GeomMath.ONE_AND_HALF_PI) || (thisVehicle.getHeading() < GeomMath.HALF_PI);
			}
		},
		HEADING_SOUTH{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getHeading() > 0) && (thisVehicle.getHeading() < GeomMath.PI);
			}
		},
		HEADING_WEST{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getHeading() > GeomMath.HALF_PI) && (thisVehicle.getHeading() < GeomMath.ONE_AND_HALF_PI);
			}
		},
	//POSITION PREDICATES
		DESTINATION_NORTH_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getPosition().getY() > thisVehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getY());
			}
		},
		DESTINATION_EAST_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getPosition().getX() < thisVehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getX());
			}
		},DESTINATION_SOUTH_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getPosition().getY() < thisVehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getY());
			}
		},
		DESTINATION_WEST_OF_VEHICLE{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return (thisVehicle.getPosition().getX() > thisVehicle.getDriver().getDestination().getLanes().get(0).getEndPoint().getX());
			}
		},
		
	//VEHICLE SPEED PREDICATES
		SPEED_GT_25_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return thisVehicle.gaugeVelocity() > 25.0;
			}
		},
		SPEED_LT_25_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return thisVehicle.gaugeVelocity() < 25.0;
			}
		},
		SPEED_GT_15_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return thisVehicle.gaugeVelocity() > 15.0;
			}
		},
		SPEED_LT_15_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return thisVehicle.gaugeVelocity() < 15.0;
			}
		},
		SPEED_GT_5_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return thisVehicle.gaugeVelocity() > 15.0;
			}
		},
		SPEED_LT_5_METERS_PER_SEC{
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return thisVehicle.gaugeVelocity() < 15.0;
			}
		},
		
	//CAR PROXIMITY PREDICATES
		CAR_LT_10_METERS_IN_FRONT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				
				return specficCarIsNearby(Direction.IN_FRONT, 10, thisVehicle, otherVehicle);
			}
		},
		CAR_LT_10_METERS_RIGHT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return specficCarIsNearby(Direction.RIGHT, 10, thisVehicle, otherVehicle);
			}
		},
		CAR_LT_10_METERS_BEHIND{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return specficCarIsNearby(Direction.BEHIND, 10, thisVehicle, otherVehicle);
			}
		},
		CAR_LT_10_METERS_LEFT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return specficCarIsNearby(Direction.LEFT, 10, thisVehicle, otherVehicle);
			}
		},
		CAR_LT_20_METERS_IN_FRONT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return specficCarIsNearby(Direction.IN_FRONT, 20, thisVehicle, otherVehicle);
			}
		},
		CAR_LT_20_METERS_RIGHT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return specficCarIsNearby(Direction.RIGHT, 20, thisVehicle, otherVehicle);
			}
		},
		CAR_LT_20_METERS_BEHIND{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return specficCarIsNearby(Direction.BEHIND, 20, thisVehicle, otherVehicle);
			}
		},
		CAR_LT_20_METERS_LEFT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return specficCarIsNearby(Direction.LEFT, 20, thisVehicle, otherVehicle);
			}
		},
		
	//OBSTACLE PROXIMITY PREDICATES
		OBSTACLE_LT_10_METERS_IN_FRONT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.IN_FRONT, 10, thisVehicle);
			}
			public void foo(){
				int i =3;
			}
		},
		OBSTACLE_LT_10_METERS_RIGHT{	
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.RIGHT, 10, thisVehicle);
			}
		},
		OBSTACLE_LT_10_METERS_BEHIND{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.BEHIND, 10, thisVehicle);
			}
		},
		OBSTACLE_LT_10_METERS_LEFT{	
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.LEFT, 10, thisVehicle);
			}
		},
		
		OBSTACLE_LT_20_METERS_IN_FRONT{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.IN_FRONT, 20, thisVehicle);
			}
		},
		OBSTACLE_LT_20_METERS_RIGHT{	
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.RIGHT, 20, thisVehicle);
			}
		},
		OBSTACLE_LT_20_METERS_BEHIND{
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.BEHIND, 20, thisVehicle);
			}
		},
		OBSTACLE_LT_20_METERS_LEFT{	
			@Override
			public boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle){
				return obstacleIsNearby(Direction.LEFT, 20, thisVehicle);
			}
		};
		
	
	
	
		public static ShoutAheadSimulator sim;//common to all elements?
		private static Random rand = new Random();
		
		/**
		 * Returns true if the predicate is true at a particular point in the simulation for a particular car.
		 * @param otherVehicle TODO
		 * @return	true if the predicate is true at a particular point in the simulation for a particular car.
		 */
		public abstract boolean isTrue(AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle);
		//public abstract boolean isTrue(AutoVehicleSimView vehicle, AutoVehicleSimView vehicleInFront);
		
		/**
		 * Returns a random predicate.
		 * @return a random predicate.
		 */
		public static VehiclePredicate getRandomPredicate(){
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
			for(VehiclePredicate pred: VehiclePredicate.values()) {
				str += pred.name() + "\t\t" + pred.isTrue(vehicle, null) + "\n";
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
			for(VehiclePredicate pred: VehiclePredicate.values()){
				if(pred.isTrue(vehicle, null))
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
			Line2D.Double line = drawLineInDirection(direction, distance, vehicle);
			Set<VehicleSimView> otherVehicles = sim.getActiveVehicles();
			otherVehicles.remove(vehicle);
			for(VehicleSimView v : otherVehicles){
				if(line.intersects((Rectangle2D) v.gaugeShape().getBounds2D()))
					return true;
			}
			//detect nearby buildings
			for(Rectangle2D.Double building : sim.getMap().getBuildings()){
				if(line.intersects(building))
					return true;
			}
			return false;
		}
		
		protected boolean specficCarIsNearby(Direction direction, int distance, AutoVehicleSimView thisVehicle, AutoVehicleSimView otherVehicle) {
			if(otherVehicle == null)
				return false;
			Line2D.Double line = drawLineInDirection(direction, distance, thisVehicle);
			Set<VehicleSimView> otherVehicles = sim.getActiveVehicles();
			otherVehicles.remove(thisVehicle);
			for(VehicleSimView v : otherVehicles){
				if(line.intersects((Rectangle2D) v.gaugeShape().getBounds2D()) && v.getVIN() == otherVehicle.getVIN())
					return true;
			}		
			return false;
		}
		
		private static Line2D.Double drawLineInDirection(Direction direction, int distance,
				AutoVehicleSimView vehicle) {
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
			return line;
		}
		
		/**
		 * . 
		 * 
		 * @author christopher.hawk
		 */
		public enum Direction{
			IN_FRONT,
			BEHIND,
			RIGHT,
			LEFT
		}

		public static int size() {
			return values().length;
		}
}
