/**
 * 
 */
package aim4.gui.statuspanel;

import aim4.ShoutAheadAI.LearningRun;
import aim4.ShoutAheadAI.Strategy;
import aim4.config.Debug;

/**
 * @author chrishawk_MacBookAir
 *
 */
public class ShoutAheadRuleSetPanel extends ConsolePanel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see aim4.gui.statuspanel.ConsolePanel#update()
	 */
	@Override
	public void update() {
		clear();
		Strategy currentStrategy = LearningRun.getCurrentStrategy();
		if(currentStrategy == null)
			append("Select the Shout Ahead simulation and press Start to see shout ahead rule set");
		else if(Debug.SHOW_STRATEGY_GUI){
				append(currentStrategy.getShoutAheadRuleSet().toString());
		}
	}

}
