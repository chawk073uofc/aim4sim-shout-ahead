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
	private static final String COMMA = ",";
	double totalNetDistGained = 0;
	double totalBuildingCollisions = 0;
	double totalCarCollisions = 0;
	double totalAcceleration = 0;
	double totalCompletedVehicles = 0;
	double totalFitness = 0;
	
	double aveNetDistGained;
	double aveBuildingCollisions;
	double aveCarCollisions;
	double aveAcceleration;
	double aveCompletedVehicles;
	double aveFitness;

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

	public void addStats(double runningAveNetDistMovedTowardsDest, int totalBuildingCollisions2,
			int totalCarCollisions2, double runningAveAcceleration, int numOfCompletedVehicles, double fitness) {
		
		totalNetDistGained += runningAveNetDistMovedTowardsDest;
		 totalBuildingCollisions += totalBuildingCollisions;
		 totalCarCollisions += totalCarCollisions;
		 totalAcceleration += runningAveAcceleration;
		 totalCompletedVehicles += numOfCompletedVehicles;
		 totalFitness += fitness;
	}
	public String getGenSummaryCSV(){
		String str = "";
		 str += aveNetDistGained + COMMA;
		 str += aveBuildingCollisions + COMMA;
		 str += aveCarCollisions + COMMA;
		 str += aveAcceleration + COMMA;
		 str += aveCompletedVehicles + COMMA;
		 str += aveFitness;
		 
		return str;
	}
	private void computeAverages(){
		 aveNetDistGained = totalNetDistGained / size();
		 aveBuildingCollisions = totalBuildingCollisions / size();
		 aveCarCollisions = totalCarCollisions / size();
		 aveAcceleration = totalAcceleration / size();
		 aveCompletedVehicles = totalCompletedVehicles / size();
		 aveFitness = totalFitness / size();
	}
	
//	private void calculateGenSummary(){
//		for(int i = 0; i< size(); i++){
//			totalNetDistGained += get(i).
//		
//		}
	
	
}