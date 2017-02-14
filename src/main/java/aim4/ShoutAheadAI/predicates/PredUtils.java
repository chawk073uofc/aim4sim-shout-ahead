package aim4.ShoutAheadAI.predicates;

public class PredUtils {
	/**
	 * The cardinal directions. 
	 * 
	 * @author christopher.hawk
	 */
	public enum Direction{
		NORTH,
		SOUTH,
		EAST,
		WEST
	}

	public boolean obstacleIsNearby(Direction direction, int distance){
		//draw line, see if it intersects with a car or road boundary
		//java.awt.geom.Line2D.Double
		return true;
	}
}

