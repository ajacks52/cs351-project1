package antworld.client;

import java.awt.Point;

//TODO: this code only traverses a 2d int array right now for testing 
//		 purposes. We will need to replace the 2d array with the map 
//		 somehow and get this class to read the color values to find food.
/**
 * This class finds the position at which food is located on a map.
 */
public class FindFood
{
	int radius;
	int antX, antY; // ant's current position
	int foodX, foodY; // the position of the food

	// 2 = food
	int[][] map = 
		{ 
			{0, 2, 0, 0, 1}, 
			{0, 0, 1, 1, 1}, 
			{1, 1, 1, 1, 0},
	    {0, 0, 0, 0, 0}, 
	    {1, 1, 1, 1, 1}, 
	  };

	int mapHeight = map.length;
	int mapWidth = map[0].length;

	/**
	 * @param x - ant's x position
	 * @param y - ant's y position
	 * @param r - ant's vision radius
	 */
	FindFood(int x, int y, int r)
	{
		antX = x;
		antY = y;
		radius = r;
		findFood(antX, antY);
	}

	/**
	 * Determines whether there is food within the ant's vision radius.
	 * 
	 * @param point
	 * @return true if there is food within the ant's line of sight, otherwise
	 *         false.
	 */
	public boolean foodDetected(Point point)
	{
		int x = (int) point.getX();
		int y = (int) point.getY();

		// System.out.println(point);

		if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight)
		{
			if (map[y][x] == 2)
			{
				foodX = x;
				foodY = y;
				System.out.println("Found food at: " + "(" + foodX + "," + foodY + ")");
				// TODO: Now we need to get the ant to A* (or some other 
				//			 pathfinding algorithm) to the food.
				//			 Find path between (antX, antY) and (foodX, foodY).
				return true;
			}
		}
		return false;
	}

	/**
	 * This method takes a point within the ant's vision radius and identifies
	 * whether or not food is in the area.
	 * 
	 * @param x
	 * @param y
	 */
	public void findFood(int x, int y)
	{
		WithinAntVision vision = new WithinAntVision(x, y, radius);
		// System.out.println(vision.getPoints());
		for (int i = 0; i < vision.getSize(); i++)
		{
			if (foodDetected(vision.getPoints().get(i)))
				break; // food values are set in foodDetected
		}
		// System.out.println(foodX + "," + foodY);
	}

//	public static void main(String[] args)
//	{
//		new FindFood(0, 0, 2);
//	}
}
