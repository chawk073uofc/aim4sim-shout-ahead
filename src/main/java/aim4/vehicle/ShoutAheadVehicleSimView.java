package aim4.vehicle;

import aim4.ShoutAheadAI.ShoutAheadDriverAgent;
import aim4.driver.ProxyDriver;

/**
 * The interface of a shout ahead vehicle from the viewpoint of a simulator.
 */
public interface ShoutAheadVehicleSimView extends AutoVehicleSimView {

  /**
   * {@inheritDoc}
   */
  @Override
  ShoutAheadDriverAgent getDriver();
}
