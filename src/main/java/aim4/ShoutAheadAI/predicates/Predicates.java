/**
 * 
 */
package aim4.ShoutAheadAI.predicates;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import aim4.sim.ShoutAheadSimulator;
import aim4.util.GeomMath;
import aim4.util.GeomUtil;
import aim4.vehicle.AutoVehicleSimView;

/**
 * @author chrishawk_MacBookAir
 *
 */
public enum Predicates {
	/**The number of predicates defined in this class.*/
//	private int numPredicates;
//	private Method[] predicates;
	
//	public Predicates(ShoutAheadSimulator sim){
//		this.sim = sim;
//		predicates = Predicates.class.getDeclaredMethods();
//		numPredicates = predicates.length;
//		
//	}
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

}
