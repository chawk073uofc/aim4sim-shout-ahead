package aim4.ShoutAheadAI;
import aim4.config.Debug;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.VehicleSimView;

public class NoApplicableRulesException extends Exception {
	public NoApplicableRulesException(VehicleSimView vehicle){
		if(Debug.SHOW_RULE_USED){
			System.out.println("VIN " + vehicle.getVIN() + ": No applicabel rules.\n");
		}
	}
}
