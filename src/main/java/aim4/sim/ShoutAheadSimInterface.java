package aim4.sim;

public interface ShoutAheadSimInterface extends Simulator {
	  int getTotalBuildingCollisions();

		int getTotalCarCollisions();
		
		int getTotalCarsCompleted();
		
		double getRunningAveAccelration();
		
		double getFitness();

		double getSimTimeRemaining();
}
