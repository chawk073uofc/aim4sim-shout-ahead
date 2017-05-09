
package aim4.ShoutAheadAI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import aim4.ShoutAheadAI.predicates.VehiclePredicate;
import aim4.config.Debug;
import aim4.config.DebugPoint;
import aim4.driver.AutoDriver;
import aim4.driver.DriverSimView;
import aim4.map.BasicMap;
import aim4.vehicle.AutoVehicleDriverView;
import aim4.vehicle.BasicAutoVehicle;
import aim4.vehicle.BasicVehicle.Movement;
import aim4.vehicle.AutoVehicleDriverView.LRFMode;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleDriverView;

/**
 * @author christopher.hawk
 *
 */
public class ShoutAheadDriverAgent extends AutoDriver implements DriverSimView {
	public static final int NUM_HEAD_START_ACTIONS = ShoutAheadSimSetup.getNumHeadStartActions(); //TODO param
	private AutoVehicleSimView vehicle;
	private ShoutAheadSimulator sim;
	private int actionNumber = 0;
	
	private Rule nonCommRuleToFollow; 
	private ShoutAheadRule commRuleToFollow;
	private Rule finalRuleToFollow;
	
	private Rule prevRuleFollowed; 
	//private ArrayList<Rule> rulesFollowedList = new ArrayList<Rule>();
	
	private boolean hasJustHitBuilding = false;
	
	private double rewardFromAction = 0;
	//private ArrayList<Double> rewardsList = new ArrayList<Double>();
			
	private double prevRewardFromAction;
	private double distanceReward = 0;
	private double distanceRewardWeight = ShoutAheadSimSetup.getDistanceWeight();
	
	private double accelerationPenalty = 0;
	private double accelerationPenaltyWeight = ShoutAheadSimSetup.getAccelerationWeight();

	private double carCollisionPenalty = 0;
	private double carCollisionWeight = ShoutAheadSimSetup.getCarCollisionWeight();
	private boolean hasJustHitCar = false;

	private double buildingCollisionPenalty = 0;
	private double buildingCollisionWeight = ShoutAheadSimSetup.getBuildingCollisionWeight();
	private Random rand = new Random();
	public ShoutAheadDriverAgent(AutoVehicleSimView vehicle, BasicMap basicMap, ShoutAheadSimulator sim) {
		super(vehicle, basicMap);
		this.vehicle = vehicle;
		this.sim = sim;
	}

	/**
	 * Allow the driver agent to make steering and acceleration decisions based
	 * on the first rule set.
	 */
	public void decideNonCommAction() {
		actionNumber++;
		showDestinationLines();
		showVehicleDebugDots();
		printRuleInfo();
		if (isGettingHeadStart())
			headStart();
		
		else {
			try {
				nonCommRuleToFollow = sim.getStrategy().getPart1RuleToFollow(vehicle);
				//finalRuleToFollow = sim.getStrategy().getPart2RuleToFollow(vehicle);
				//finalRuleToFollow.execute(vehicle);

			} catch (NoApplicableRulesException e) {
				nonCommRuleToFollow = null;
				//Action.CRUISE.execute(vehicle);// do nothing
			} catch (NullPointerException e) {
				//Action.CRUISE.execute(vehicle);
				nonCommRuleToFollow = null;
			}
			
		}		 
	}

	public void decideCommAction() {
		try {
			commRuleToFollow = sim.getStrategy().getPart2RuleToFollow(vehicle);
		} catch (NoApplicableRulesException e) {
			commRuleToFollow = null;
		} catch (NullPointerException e) {
			commRuleToFollow = null;
		}
	}

	public void decideFinalAction() {
		if(nonCommRuleToFollow == null && commRuleToFollow == null)
			finalRuleToFollow = null;
		else if(nonCommRuleToFollow == null && commRuleToFollow != null)
			finalRuleToFollow = commRuleToFollow;
		else if(nonCommRuleToFollow != null && commRuleToFollow == null)
			finalRuleToFollow = nonCommRuleToFollow;
		else if(nonCommRuleToFollow != null && commRuleToFollow != null){
			if(nonCommRuleToFollow.getWeight() < commRuleToFollow.getWeight())
				finalRuleToFollow = commRuleToFollow;
			else if(returnTrueWithProb(ShoutAheadSimSetup.getCommRuleProb()))
				finalRuleToFollow = commRuleToFollow;
			else
				finalRuleToFollow = nonCommRuleToFollow;
		}
	}

	public void executeFinalAction() {
		try {
			finalRuleToFollow.execute(vehicle);	
			prevRuleFollowed = finalRuleToFollow;
		} catch (NullPointerException e) {
			Action.CRUISE.execute(vehicle);
		}
		prevRuleFollowed = finalRuleToFollow;

	}

	private void printRuleInfo() {
		if (Debug.SHOW_PERCEPTIONS) {
			System.out.println(VehiclePredicate.showAllPredicates((AutoVehicleSimView) vehicle));
		}
		if (Debug.SHOW_TRUE_PERCEPTIONS) {
			System.out.println(VehiclePredicate.showTruePredicates(vehicle));
		}
		if (Debug.SHOW_APPLICABLE_RULES) {
			System.out.println("Applicable Rules For VIN " + vehicle.getVIN());
		}
	}

	private void showVehicleDebugDots() {
		if(Debug.DRAW_FRONT_BACK_CENTER) {
			Double vehicleHalfLength = vehicle.getSpec().getHalfLength();
			Point2D frontPoint = vehicle.getPointAtMiddleFront(vehicleHalfLength);
			Point2D backPoint = vehicle.getPointAtMiddleRear(vehicleHalfLength);
			
			Debug.addShortTermDebugPoint(new DebugPoint(vehicle.getCenterPoint(), Color.MAGENTA.brighter()));
			Debug.addShortTermDebugPoint(new DebugPoint(vehicle.gaugePointBetweenFrontWheels(), Color.GREEN.brighter()));
			Debug.addShortTermDebugPoint(new DebugPoint(backPoint, Color.RED.brighter()));
		}
	}

	private void showDestinationLines() {
		if(Debug.isTargetVIN(vehicle.getVIN()) || Debug.SHOW_ALL_DESTINATIONS){
			 Color lineColor = (vehicle.getChangeInDistanceFromDestination() < 0) ? Color.RED.brighter() : Color.GREEN;
	          Debug.addShortTermDebugPoint(
	              new DebugPoint(
	                ((BasicAutoVehicle) getVehicle()).getDestinationPoint(),
	                getVehicle().gaugePosition(),
	                lineColor) );
	    }
	}
	
	public boolean isGettingHeadStart() {
		return actionNumber < NUM_HEAD_START_ACTIONS && Debug.GIVE_HEAD_START;
	}

	public void updateRewardsFromLastAction() {
		prevRewardFromAction = rewardFromAction;
		clearRewardsFromLastAction();
		
		distanceReward = vehicle.getChangeInDistanceFromDestination() * distanceRewardWeight;
		carCollisionPenalty = hasJustHitCar ? carCollisionWeight : 0;
		buildingCollisionPenalty = hasJustHitBuilding ? buildingCollisionWeight : 0;
		accelerationPenalty = Math.abs(vehicle.getAcceleration()) * accelerationPenaltyWeight;
		
		rewardFromAction += distanceReward;
		rewardFromAction += carCollisionPenalty;
		rewardFromAction += buildingCollisionPenalty;
		rewardFromAction += accelerationPenalty;
		
		if(Debug.SHOW_DRIVER_INFO && Debug.isTargetVIN(vehicle.getVIN())){
			System.out.println("After Updating:\n"+ this);
		}
	}

	public void clearRewardsFromLastAction() {
		distanceReward = 0;
		accelerationPenalty = 0;
		carCollisionPenalty = 0;
		hasJustHitCar = false;
		buildingCollisionPenalty = 0;
		hasJustHitBuilding = false;	
		rewardFromAction = 0;
	}

	private void headStart() {
		if (Debug.GIVE_HEAD_START) {
			Action.CRUISE.execute(vehicle);
		}
	}

	/**
	 * @return the currentRuleToFollow
	 */
	public Rule getCurrentRuleToFollow() {
		return finalRuleToFollow;
	}

	public int getNumActions() {
		return actionNumber;
	}

	public Rule getLastRuleFollowed() {
		return finalRuleToFollow;
	}

	public void setHasJustHitBuilding(boolean hasHit) {
		hasJustHitBuilding = hasHit;
	}

	public void setHasJustHitCar(boolean hasHit) {
		hasJustHitCar = hasHit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShoutAheadDriverAgent:\n");

		builder.append(", actionNumber=");
		builder.append(actionNumber);
		builder.append(", currentRuleToFollow=");
		builder.append(nonCommRuleToFollow);
		builder.append("\nrewardFromLastAction=");
		builder.append(rewardFromAction);
		builder.append("\ndistanceReward=");
		builder.append(distanceReward);
		builder.append(", distanceRewardWeight=");
		builder.append(distanceRewardWeight);
		builder.append("\naccelerationPenalty=");
		builder.append(accelerationPenalty);
		builder.append(", accelerationPenaltyWeight=");
		builder.append(accelerationPenaltyWeight);
		builder.append("\ncarCollisionPenalty=");
		builder.append(carCollisionPenalty);
		builder.append(", carCollisionWeight=");
		builder.append(carCollisionWeight);
		builder.append(", hasJustHitCar=");
		builder.append(hasJustHitCar);
		builder.append("\nbuildingCollisionPenalty=");
		builder.append(buildingCollisionPenalty);
		builder.append(", buildingCollisionWeight=");
		builder.append(buildingCollisionWeight);
		builder.append(", hasJustHitBuilding=");
		builder.append(hasJustHitBuilding);
		return builder.toString();
	}

	/**
	 * @return the rewardFromAction
	 */
	public double getRewardFromAction() {
		return rewardFromAction;
	}

	public boolean getHasJustHitBuilding() {
		return hasJustHitBuilding;
	}

	public boolean getHasJustHitCar() {
		return hasJustHitCar;
	}

	public Action getIntendedAction() {
		return nonCommRuleToFollow.getAction();
	}

	/**
	 * This function returns true with probability prob.
	 * @param prob
	 * @return
	 */
	protected boolean returnTrueWithProb(double prob) {
		return rand.nextDouble() < prob;
	}
	/*
	 * rl_n_weight = rule_n_weight + \alpha[rule_n_rewart + \gamma*rule_n+1_weight - currentRlWeight]
	 */
	public void updatePrevRuleWeight() {
		updateRewardsFromLastAction();
		
		try{
			double prevRuleWeight = getPrevRuleWeight() + ShoutAheadSimSetup.getAlpha()
							  * (
									    getPrevReward() 
									  + ShoutAheadSimSetup.getGamma()*getCurrentRuleWeight()
									  - getPrevRuleWeight()
								);
			prevRuleFollowed.setWeight(prevRuleWeight);
		} catch (NoApplicableRulesException e){
			//do nothing. no rule to update
		}
//		try {
//			Rule lastFollowedRule = driver.getCurrentRuleToFollow();
//			if (lastFollowedRule == null)
//				throw new NoApplicableRulesException(vehicle);
//			driver.updateRewardsFromLastAction();
//			double rewardFromLastAction = driver.getRewardFromCurrentAction();
//			//lastFollowedRule.addWeight(rewardFromAction);
//			driver.clearRewardsFromLastAction();
//
//		} catch (NoApplicableRulesException e) {
//			driver.clearRewardsFromLastAction();
//		}		
	}

	private double getCurrentRuleWeight() {
		if (finalRuleToFollow != null)
			return finalRuleToFollow.getWeight();
		else
			return 0;
	}

	private double getPrevReward() throws NoApplicableRulesException {
		if(prevRuleFollowed != null)
			return prevRewardFromAction;
		else
			throw new NoApplicableRulesException(vehicle);
	}

	private double getPrevRuleWeight() throws NoApplicableRulesException {
		if( prevRuleFollowed != null)
			return prevRuleFollowed.getWeight();
		else  
			throw new NoApplicableRulesException(vehicle);
	}
}
