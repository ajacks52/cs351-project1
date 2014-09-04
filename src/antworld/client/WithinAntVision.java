package antworld.client;

import java.util.ArrayList;
import java.awt.Point;
/**
 * Creates an ArrayList of all points within the vision radius of an ant.
 */
public class WithinAntVision
{
	private ArrayList<Point> points = new ArrayList<Point>();
	
	/**
	 * @param centerX
	 * @param centerY
	 * @param radius
	 */
	public WithinAntVision(int centerX, int centerY, int radius)
	{
		withinVision(centerX, centerY, radius);
	}
	
	/**
	 * This method returns an ArrayList containing all the points
	 * within an ant's range of vision 
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @return points - an ArrayList of points that the ant can see.
	 */
	public ArrayList<Point> withinVision(int centerX, int centerY, int radius)
	{
		for (int x = centerX - radius; x <= centerX + radius; x++)
		{
			for (int y = centerY - radius; y <= centerY + radius; y++)
			{
				if (inCircle(centerX, centerY, radius, x, y))
				{
					getPoints().add(new Point(x, y));
				}
			}
		}
//		System.out.println(points);
		return getPoints();
	}
	
	/**
	 * This method determines whether a point is within a RxR square
	 * around the center point, where R is the ant's vision radius.
	 * 
	 * @param centerX
	 * @param centerY
	 * @param r
	 * @param x
	 * @param y
	 * @return true if a point is within the RxR square, otherwise false.
	 */
	public boolean inSquare(int centerX, int centerY, 
			int r, int x, int y)
	{
		return x >= centerX - r && x <= centerX + r &&
				y >= centerY - r && y <= centerY + r;
	}
	
	/**
	 * This method determines whether a point is within a circle with a certain
	 * radius around the ant's current position.
	 * 
	 * @param centerX
	 * @param centerY
	 * @param r
	 * @param x
	 * @param y
	 * @return true if a point is within the circle, otherwise false.
	 */
	public boolean inCircle(int centerX, int centerY, 
			int r, int x, int y)
	{
		if (inSquare(centerX, centerY, r, x, y))
		{
			int dx = centerX - x;
			int dy = centerY - y;
			dx *= dx;
			dy *= dy;
			int distSquared = dx + dy;
			int radSquared = r * r;
			return distSquared <= radSquared;
		}
		return false;
	}

	/**
	 * Get ArrayList of visible points.
	 */
	public ArrayList<Point> getPoints()
  {
	  return points;
  }

	/**
	 * Set ArrayList of visible points.
	 * 
	 * @param points
	 */
	public void setPoints(ArrayList<Point> points)
  {
	  this.points = points;
  }
	
//  Hard-coded main(), for testing only
	public static void main(String[] args)
	{
		new WithinAntVision(5, 5, 3);
	}
}
