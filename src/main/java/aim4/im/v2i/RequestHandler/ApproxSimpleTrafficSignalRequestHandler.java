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
package aim4.im.v2i.RequestHandler;

import aim4.config.Debug;
import aim4.config.TrafficSignal;
import java.util.List;

import aim4.im.v2i.policy.BasePolicy;
import aim4.im.v2i.policy.BasePolicyCallback;
import aim4.im.v2i.policy.BasePolicy.ProposalFilterResult;
import aim4.im.v2i.policy.BasePolicy.ReserveParam;
import aim4.map.Road;
import aim4.msg.i2v.Reject;
import aim4.msg.v2i.Request;
import aim4.sim.StatCollector;

/**
 * The approximate traffic signal request handler.
 */
public class ApproxSimpleTrafficSignalRequestHandler implements
    TrafficSignalRequestHandler {

  /////////////////////////////////
  // CONSTANTS
  /////////////////////////////////

  /**
   * The length of the green light duration
   */
  private static final double DEFAULT_GREEN_LIGHT_DURATION = 15.0;
  /**
   * The length of the yellow light duration
   */
  private static final double DEFAULT_YELLOW_LIGHT_DURATION = 5.0;


  /////////////////////////////////
  // PRIVATE FIELDS
  /////////////////////////////////

  /** The duration of the green light signal */
  private double greenLightDuration = DEFAULT_GREEN_LIGHT_DURATION;
  /** The duration of the yellow light signal */
  private double yellowLightDuration = DEFAULT_YELLOW_LIGHT_DURATION;
  /** The base policy */
  private BasePolicyCallback basePolicy;

  /////////////////////////////////
  // CONSTRUCTORS
  /////////////////////////////////

  public ApproxSimpleTrafficSignalRequestHandler(double greenLightDuration,
                                                 double yellowLightDuration) {
    this.greenLightDuration = greenLightDuration;
    this.yellowLightDuration = yellowLightDuration;
  }


  /////////////////////////////////
  // PUBLIC METHODS
  /////////////////////////////////

  /**
   * Get the duration of the green light signal.
   *
   * @param greenLightDuration  the duration of the green light signal
   */
  public void setGreenLightDuration(double greenLightDuration) {
    this.greenLightDuration = greenLightDuration;
  }

  /**
   * Get the duration of the yellow light signal.
   *
   * @param yellowLightDuration  the duration of the yellow light signal
   */
  public void setYellowLightDuration(double yellowLightDuration) {
    this.yellowLightDuration = yellowLightDuration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBasePolicyCallback(BasePolicyCallback basePolicy) {
    this.basePolicy = basePolicy;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void act(double timeStep) {
    // do nothing
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processRequestMsg(Request msg) {
    int vin = msg.getVin();

    // If the vehicle has got a reservation already, reject it.
    if (basePolicy.hasReservation(vin)) {
      basePolicy.sendRejectMsg(vin, msg.getRequestId(),
                               Reject.Reason.CONFIRMED_ANOTHER_REQUEST);
      return;
    }

    // filter the proposals
    ProposalFilterResult filterResult =
      BasePolicy.standardProposalsFilter(msg.getProposals(),
                                         basePolicy.getCurrentTime());
    if (filterResult.isNoProposalLeft()) {
      basePolicy.sendRejectMsg(vin,
                               msg.getRequestId(),
                               filterResult.getReason());
    }

    List<Request.Proposal> proposals = filterResult.getProposals();

    // If cannot enter from lane according to canEnterFromLane(), reject it.
    if (!canEnterFromLane(proposals.get(0).getArrivalLaneID())){
      basePolicy.sendRejectMsg(vin, msg.getRequestId(),
                               Reject.Reason.NO_CLEAR_PATH);
      return;
    }
    // try to see if reservation is possible for the remaining proposals.
    ReserveParam reserveParam = basePolicy.findReserveParam(msg, proposals);
    if (reserveParam != null) {
      basePolicy.sendComfirmMsg(msg.getRequestId(), reserveParam);
    } else {
      basePolicy.sendRejectMsg(vin, msg.getRequestId(),
                               Reject.Reason.NO_CLEAR_PATH);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StatCollector<?> getStatCollector() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TrafficSignal getSignal(int laneId) {
    Road road = Debug.currentMap.getRoad(laneId);

    double period = greenLightDuration + yellowLightDuration;
    int id = (int) Math.floor(basePolicy.getCurrentTime() / period);

    int phaseId = id % 4;

    boolean canPass = false;
    if (phaseId == 0) {
      canPass = road.getName().equals("1st Street W");
    } else if (phaseId == 1) {
      canPass = road.getName().equals("1st Avenue S");
    } else if (phaseId == 2) {
      canPass = road.getName().equals("1st Street E");
    } else if (phaseId == 3) {
      canPass = road.getName().equals("1st Avenue N");
    } else {
      throw new RuntimeException("Error in isGreenTrafficLightNow()");
    }

    if (canPass) {
      double t = basePolicy.getCurrentTime() - id * period;
      if (t <= greenLightDuration) {
        return TrafficSignal.GREEN;
      } else {
        return TrafficSignal.YELLOW;
      }
    } else {
      // it is either yellow or red signal
      return TrafficSignal.RED;
    }
  }

  /////////////////////////////////
  // PRIVATE FIELDS
  /////////////////////////////////

  /**
   * Check whether the vehicle can enter the intersection from a lane at
   * the current time.
   *
   * @param laneId  the id of the lane from which the vehicle enters
   *                the intersection.
   * @return whether the vehicle can enter the intersection
   */
  private boolean canEnterFromLane(int laneId) {
    Road road = Debug.currentMap.getRoad(laneId);

    double period = greenLightDuration + yellowLightDuration;
    int id = (int) Math.floor(basePolicy.getCurrentTime() / period);

    int phaseId = id % 4;

    boolean canPass = false;

    if (phaseId == 0) {
      canPass = road.getName().equals("1st Street W");
    } else if (phaseId == 1) {
      canPass = road.getName().equals("1st Avenue S");
    } else if (phaseId == 2) {
      canPass = road.getName().equals("1st Street E");
    } else if (phaseId == 3) {
      canPass = road.getName().equals("1st Avenue N");
    } else {
      throw new RuntimeException("Error in isGreenTrafficLightNow()");
    }

    if (canPass) {
      double t = basePolicy.getCurrentTime() - id * period;
      if (t <= greenLightDuration) {
        return true;
      } else {
        return false;
      }
    } else {
      // it is either yellow or red signal
      return false;
    }

  }


}
