package aim4.ShoutAheadAI;

import java.util.ArrayList;

public class ShoutAheadRuleSet {
	int numRules; 
	ArrayList<ShoutAheadRule> rules = new ArrayList<ShoutAheadRule>();
	
	ShoutAheadRuleSet(int numRules){
		this.numRules = numRules;
		for(int i = 0; i < numRules; i++){
			rules.add(new ShoutAheadRule());
		}
		
	}
}
