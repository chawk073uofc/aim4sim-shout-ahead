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
package aim4.noise;

import aim4.util.Util;

/**
 * NoiseFunction that adds noise uniformly within a fixed proportion of the
 * true value.
 */
public class UniformProportionalNoiseFunction implements NoiseFunction {

  /**
   * The proportion of the true value to use as a range for noise.  For
   * example, if this is .1, then each value will vary uniformly between
   * 90% and 110% of the true value.
   */
  private double proportion;

  /**
   * Class constructor.
   *
   * @param proportion the proportion of the true value to use as bounds for
   *        the noise function.
   */
  public UniformProportionalNoiseFunction(double proportion) {
    this.proportion = proportion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double apply(double trueValue) {
    double range = 2 * trueValue * proportion;
    return trueValue + (Util.random.nextDouble() * range) - range/2;
  }
}
