/**
 * 
 */
package aim4.gui.statuspanel;

import aim4.ShoutAheadAI.ShoutAheadDriverAgent;
import aim4.config.Debug;
import aim4.vehicle.VehicleSimView;
import aim4.vehicle.VinRegistry;

/**
 * @author chrishawk_MacBookAir
 *
 */
public class CurrentRulePanel extends ConsolePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void writeToCurrentRulePanel(String msg){
	}
	
	 /**
	   * Print the current rule being followed if there is one. 
	   */
	  @Override
	  public void update() {
		  VehicleSimView v = VinRegistry.getVehicleFromVIN(Debug.getTargetVIN());
		    if (v != null) {
		    try{
		      ShoutAheadDriverAgent agent = (ShoutAheadDriverAgent) v.getDriver();
		      if(agent.getNumActions() < ShoutAheadDriverAgent.NUM_HEAD_START_ACTIONS){
		    	  append("Head Start. Action = CRUISE\n");
		      }
		      else {
			      if(agent.getLastRuleFollowed() == null)
			    	  append("No applicable rule. Action = CRUISE\n"); 
			      else
			    	  append(agent.getLastRuleFollowed().toString());
			    }
		    }
		    catch (ClassCastException e){
		    	append("Shout Ahead Simulator not selected.");
		    }
		    }
		    else { // No vehicle selected, clear everything
		      clear(); 
		      append("No vehicle selected.\n");
		    }
	  }
}
