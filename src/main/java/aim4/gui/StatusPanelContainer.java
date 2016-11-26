/*
Copyright (c) 2011 Tsz-Chiu Au, Peter Stone
University of Texas at Austin
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3. Neither the name of the University of Texas at Austin nor the names of its
contributors may be used to endorse or promote products derived from this
software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package aim4.gui;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import aim4.gui.statuspanel.ConsolePanel;
import aim4.gui.statuspanel.AdminControlPanel;
import aim4.gui.statuspanel.SimControlPanel;
import aim4.gui.statuspanel.StatPanel;
import aim4.gui.statuspanel.SystemPanel;
import aim4.gui.statuspanel.VehicleInfoPanel;

/**
 * A tabbed panel for showing statistics and status of the simulator.
 */
public class StatusPanelContainer extends JTabbedPane
                                  implements ChangeListener {

  private static final long serialVersionUID = 1L;

  // ///////////////////////////////
  // PRIVATE FIELDS
  // ///////////////////////////////

  /** The simulation control panel */
  SimControlPanel controlPanel;
  /** The statistic panel */
  StatPanel statPanel;
  /** The system panel */
  SystemPanel systemPanel;
  /** The console panel */
  ConsolePanel consolePanel;
  /** The vehicle information panel */
  VehicleInfoPanel vehicleInfoPanel;
  /** The administration control panel */
  AdminControlPanel adminControlPanel;

  // ///////////////////////////////
  // CLASS CONSTRUCTORS
  // ///////////////////////////////

  /**
   * Create a new status pane with the given preferred width.
   *
   * @param viewer  the viewer object
   */
  public StatusPanelContainer(Viewer viewer) {
    controlPanel = new SimControlPanel(viewer);
    add("Simulation", controlPanel);
    statPanel = new StatPanel(viewer);
    add("Statistics", statPanel);
    systemPanel = new SystemPanel();
    add("System", systemPanel);
    consolePanel = new ConsolePanel();
    add("Console", consolePanel);
    vehicleInfoPanel = new VehicleInfoPanel();
    add("Vehicle", vehicleInfoPanel);
    adminControlPanel = new AdminControlPanel(viewer);
    add("Admin", adminControlPanel);

    addChangeListener(this);
  }

  // ///////////////////////////////
  // PUBLIC METHODS
  // ///////////////////////////////

  /**
   * Initialize the status panel before the beginning of the simulation
   */
  public void init() {
    clear();
  }

  /**
   * Update the status pane to reflect the latest information
   */
  public void update() {
    StatusPanelInterface selectedPanel =
      (StatusPanelInterface) getSelectedComponent();
    selectedPanel.update();
  }

  /**
   * Clear the content on the status pane
   */
  public void clear() {
    controlPanel.clear();
    statPanel.clear();
    systemPanel.clear();
    consolePanel.clear();
    vehicleInfoPanel.clear();
  }

  /**
   * Write a message on the console
   *
   * @param str  the message
   */
  public void writeToConsole(String str) {
    consolePanel.append(str);
  }

  /**
   * Get the simulation speed.
   *
   * @return the simulation speed
   */
  public double getSimSpeed() {
    return controlPanel.getSimSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stateChanged(ChangeEvent evt) {
    update();
  }
}