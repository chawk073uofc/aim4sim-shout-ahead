/**
 * 
 */
package aim4.ShoutAheadAI.predicates;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import aim4.ShoutAheadAI.predicates.PredUtils.Direction;
import aim4.sim.ShoutAheadSimulator;
import aim4.util.GeomMath;
import aim4.util.GeomUtil;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleSimView;

/**
 * Defines predicates which form the conditions for ShoutAheadRules.
 * 
 * @author chrishawk_MacBookAir
 *
 */
public enum Predicates {
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
		
	//OBSTACLE PROXIMITY PREDICATES
		OBSTACLE_LT_10_METERS_NORTH{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return true;
			}
		},
		OBSTACLE_LT_10_METERS_EAST{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return true;
			}
		},
		OBSTACLE_LT_10_METERS_SOUTH{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return true;
			}
		},
		OBSTACLE_LT_10_METERS_WEST{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return true;
			}
		},
		
		OBSTACLE_LT_20_METERS_NORTH{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return true;
			}
		},
		OBSTACLE_LT_20_METERS_EAST{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return true;
			}
		},
		OBSTACLE_LT_20_METERS_SOUTH{
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				return true;
			}
		},
		OBSTACLE_LT_20_METERS_WEST{	
			@Override
			public boolean isTrue(AutoVehicleSimView vehicle){
				
				return true;
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
		};
	
	//POSITION PREDICATES
		//TODO
	
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
		public static Predicates getRandomPredicate(){
			int numPredicates = values().length;
			int randPredIndex = rand.nextInt(numPredicates);
			return values()[randPredIndex];
		}
		private boolean obstacleIsNearby(Direction direction, int distance, AutoVehicleSimView vehicle){
			//draw line, see if it intersects with a car or road boundary
			Point2D centerPoint = vehicle.getCenterPoint();
			//Draw line projected from the center of the vehicle to a point distance meters in from of the vehicle
			Line2D.Double oldLine = new Line2D.Double(centerPoint, vehicle.getPointAtMiddleFront((double) distance));
			Point2D projectedPoint = null;
			switch(direction){
			case NORTH:
				projectedPoint = vehicle.getPointAtMiddleFront((double) distance);
				break;
			case EAST:
				//rotate 90 degres
				projectedPoint = vehicle.getPointAtEastOfVehicle((double) distance);
				break;
			case SOUTH:
				projectedPoint = vehicle.getPointAtSouthOfVehicle((double) distance);
				break;
			case WEST:
				projectedPoint = vehicle.getPointAtWestOfVehicle((double) distance);
				break;
			}
			
			Line2D.Double line = new Line2D.Double(centerPoint, projectedPoint);
			Set<VehicleSimView> otherVehicles = sim.getActiveVehicles();
			otherVehicles.remove(vehicle);
			for(VehicleSimView v : otherVehicles){
				if(line.intersects((Rectangle2D) v.gaugeShape()))
					return true;
			}
			for(Line2D.Double boundary : sim.getMap().getFields()){
				
			}
			return false;
		}
		/**
		 * The cardinal directions. 
		 * 
		 * @author christopher.hawk
		 */
		public enum Direction{
			NORTH,
			SOUTH,
			EAST,
			WEST
		}
}
