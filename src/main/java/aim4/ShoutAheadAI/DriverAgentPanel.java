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
package aim4.ShoutAheadAI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import aim4.config.Constants;
import aim4.config.Debug;
import aim4.config.DebugPoint;
import aim4.gui.StatusPanelInterface;
import aim4.gui.component.FormattedLabel;
import aim4.vehicle.AutoVehicleSimView;
import aim4.vehicle.BasicAutoVehicle;
import aim4.vehicle.VehicleSimView;
import aim4.vehicle.VinRegistry;

/**
 * The Vehicle Information Panel
 */
public class DriverAgentPanel extends JPanel implements StatusPanelInterface {

	private static final long serialVersionUID = 1L;

	// ///////////////////////////////
	// PRIVATE FIELDS
	// ///////////////////////////////

	/** The number of actions performed by this driver agent so far */
	private FormattedLabel numActionsLabel = new FormattedLabel("Num Actions: ", "%6d", 6);
	/** The time elapsed since this vehicle was spawned */
	private FormattedLabel timeSinceSpawnedLabel = new FormattedLabel("Time Since Spawned: ", "%7.2f s", 11);
	/** The selected vehicle's destinaton. */
	private FormattedLabel destinationLabel = new FormattedLabel("Destination: ", "%20s", 20);
	/** Straight line distance from destination */
	private FormattedLabel distToDestLabel = new FormattedLabel("Distance to Dest: ", "%6.2f m", 10);
	private FormattedLabel numCarCollisionsLabel = new FormattedLabel("Num Car Collisions: ", "%3d", 3);
	private FormattedLabel numBuildingCollisionsLabel = new FormattedLabel("Num Building Collisions: ", "%3d", 3);

	// ///////////////////////////////
	// CONSTRUCTORS
	// ///////////////////////////////

	/**
	 * Create a vehicle information panel.
	 */
	public DriverAgentPanel() {
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		c.gridwidth = 1; // restore default
		gridbag.setConstraints(numActionsLabel, c);
		add(numActionsLabel);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		gridbag.setConstraints(timeSinceSpawnedLabel, c);
		add(timeSinceSpawnedLabel);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		gridbag.setConstraints(destinationLabel, c);
		add(destinationLabel);

		c.gridwidth = 1; // default
		gridbag.setConstraints(distToDestLabel, c);
		add(distToDestLabel);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		gridbag.setConstraints(numCarCollisionsLabel, c);
		add(numCarCollisionsLabel);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		gridbag.setConstraints(numBuildingCollisionsLabel, c);
		add(numBuildingCollisionsLabel);
	}

	// ///////////////////////////////
	// PUBLIC METHODS
	// ///////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		VehicleSimView v = VinRegistry.getVehicleFromVIN(Debug.getTargetVIN());
		if (v != null) {
			try {
				ShoutAheadDriverAgent agent = (ShoutAheadDriverAgent) v.getDriver();

				numActionsLabel.update(agent.getNumActions());
				timeSinceSpawnedLabel.update(v.gaugeTime());
				destinationLabel.update(agent.getDestination());
				distToDestLabel.update(v.getDistanceFromDestination(v.getPosition()));
				numCarCollisionsLabel.update(((BasicAutoVehicle) v).getVehicleCollisionCount());
				numBuildingCollisionsLabel.update(((BasicAutoVehicle) v).getBuildingCollisionCount());
			} catch (ClassCastException e) {
				// not a shout ahead sim
			}
		} else { // No vehicle selected, clear everything
			clear();

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		numActionsLabel.clear();
		timeSinceSpawnedLabel.clear();
		destinationLabel.clear();
		distToDestLabel.clear();
		numCarCollisionsLabel.clear();
		numBuildingCollisionsLabel.clear();
	}
}