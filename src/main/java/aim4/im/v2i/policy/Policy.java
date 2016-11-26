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
package aim4.im.v2i.policy;

import aim4.im.v2i.V2IManagerCallback;
import aim4.msg.v2i.V2IMessage;
import aim4.sim.StatCollector;

/**
 * An interface for intersection control policies for V2IManagers.
 */
public interface Policy {

  /**
   * Set the V2I manager call-back.
   *
   * @param im  the V2I manager's call-back
   */
  void setV2IManagerCallback(V2IManagerCallback im);

  /**
   * Give the policy a chance to do any processing it might need to do in
   * order to respond to requests, if it hasn't responded to them already.
   * Only used for policies that don't respond immediately to requests.
   *
   * @param timeStep  the size of the time step to simulate, in seconds
   */
  void act(double timeStep);

  /**
   * Process a V2I message
   *
   * @param msg  the V2I message
   */
  void processV2IMessage(V2IMessage msg);

  /**
   * Get the statistic collector.
   *
   * @return the statistic collector
   */
  StatCollector<?> getStatCollector();

}
