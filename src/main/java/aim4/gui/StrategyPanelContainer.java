package aim4.gui;

import aim4.gui.statuspanel.RuleSetPanel;
import aim4.gui.statuspanel.ShoutAheadRuleSetPanel;

public class StrategyPanelContainer extends StatusPanelContainer {
	private static final long serialVersionUID = 1L;
	private RuleSetPanel ruleSetPanel;
	private ShoutAheadRuleSetPanel shoutAheadRuleSetPanel;

	public StrategyPanelContainer(Viewer viewer) {
		ruleSetPanel = new RuleSetPanel();//TODO create new subclasses
	    add("Rule Set", ruleSetPanel);
	    shoutAheadRuleSetPanel = new ShoutAheadRuleSetPanel();
	    add("ShoutAhead Rule Set", shoutAheadRuleSetPanel);

	    addChangeListener(this);

	}

	/* (non-Javadoc)
	 * @see aim4.gui.StatusPanelContainer#clear()
	 */
	@Override
	public void clear() {
		ruleSetPanel.clear();
		shoutAheadRuleSetPanel.clear();
	}

}
