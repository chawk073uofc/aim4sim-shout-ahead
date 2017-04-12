/**
 * 
 */
package aim4.ShoutAheadAI;

import java.util.ArrayList;

/**
 * A Generation is made up of a number of strategies. 
 *  
 * @author Chris Hawk
 *
 */
@SuppressWarnings("serial")
public class Generation extends ArrayList<Strategy>{

	double aveFitness = 0;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";
//		builder.append("aveFitness=");
//		builder.append(aveFitness);
		for(int i = 0; i< size(); i++)
			str += get(i).getFitness() + "\n";
		return str;
	}
	
}