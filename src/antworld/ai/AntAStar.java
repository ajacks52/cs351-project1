package antworld.ai;

import java.awt.*;
import java.applet.*;
import java.util.ArrayList;

// TODO: This class was originally using the ListNode and PathNode classes, 
// 			 but there were weird errors, so for now, every single parameter for
//			 the nodes is being represented as its own separate ArrayList. I would
//			 like to change this for the future. But for now, here's an program 
//			 that calculates the shortest path to a destination, and draws it on
//			 an applet screen (we should probably not have this run on an applet,
//			 so that's another future change).

// For now, this class is just a test.

// NOTE: The path changes every time you interact with the applet window.

public class AntAStar extends Applet
{
	// TODO: Currently uses this board that I created. Need to load provided map instead
	int board[][] =
		{
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		  { 0, 1, 1, 1, 0, 1, 1, 0, 1, 0 },
		  { 0, 0, 0, 1, 0, 1, 0, 0, 0, 0 },
		  { 0, 0, 0, 1, 0, 1, 0, 0, 0, 0 },
		  { 1, 0, 1, 1, 0, 0, 0, 1, 0, 0 },
		  { 1, 0, 0, 1, 0, 1, 0, 0, 0, 0 },
		  { 0, 0, 0, 1, 0, 1, 0, 1, 0, 0 },
		  { 0, 0, 1, 1, 0, 1, 0, 1, 0, 0 },
		  { 0, 0, 0, 0, 1, 1, 0, 1, 1, 0 },
		  { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 } 
		};
	
	// TODO: Don't want hard-coded values here
	int mapWidth = 9;
	int mapHeight = 9;
	int cellWidth = 32;
	int cellHeight = 24;

	int startX, startY, destX, destY;

	// TODO: Make these ArrayLists of nodes instead
	// ArrayList<ListNode> open
	// Open list ( x, y, f, g, h, parentx, parenty )
	ArrayList<Integer> openX = new ArrayList<Integer>();
	ArrayList<Integer> openY = new ArrayList<Integer>();
	ArrayList<Integer> openF = new ArrayList<Integer>();
	ArrayList<Integer> openG = new ArrayList<Integer>();
	ArrayList<Integer> openH = new ArrayList<Integer>();
	ArrayList<Integer> openPx = new ArrayList<Integer>();
	ArrayList<Integer> openPy = new ArrayList<Integer>();
	
	// TODO: ArrayList<ListNode> closed
	// Closed list ( x, y, f, g, h, parentx, parenty )
	ArrayList<Integer> closedX = new ArrayList<Integer>();
	ArrayList<Integer> closedY = new ArrayList<Integer>();
	ArrayList<Integer> closedF = new ArrayList<Integer>();
	ArrayList<Integer> closedG = new ArrayList<Integer>();
	ArrayList<Integer> closedH = new ArrayList<Integer>();
	ArrayList<Integer> closedPx = new ArrayList<Integer>();
	ArrayList<Integer> closedPy = new ArrayList<Integer>();
	
	// TODO: ArrayLIst<PathNode> path
	// Path
	ArrayList<Integer> pathX = new ArrayList<Integer>();
	ArrayList<Integer> pathY = new ArrayList<Integer>();

	/**
	 * Initialize applet window
	 */
	public void init()
	{
		setBackground(new Color(155, 205, 155));
	}

	/**
	 * Once the destination has been reached, builds the path based on the 
	 * nodes present in the closed list.
	 */
	public void traversePath()
	{
		boolean exit = false;
		int x = destX;
		int y = destY;
		while (exit == false)
		{
			for (int i = 0; i < closedX.size(); i++)
			{
				if (closedX.get(i) == x && closedY.get(i) == y)
				{
					x = closedPx.get(i);
					y = closedPy.get(i);
					pathX.add(x);
					pathY.add(y);
				}
			}
			if (x == startX && y == startY)
			{
				exit = true;
			}
		}
	}

	/**
	 * Removes a node from the open list.
	 */
	public boolean removeFromOpen(int x, int y)
	{
		for (int i = 0; i < openX.size(); i++)
		{
			if (openX.get(i) == x && openY.get(i) == y)
			{
				openX.remove(i);
				openY.remove(i);
				openF.remove(i);
				openG.remove(i);
				openH.remove(i);
				openPx.remove(i);
				openPy.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a node is in the closed list.
	 */
	public boolean inClosed(int x, int y)
	{
		for (int i = 0; i < closedX.size(); i++)
		{
			if (closedX.get(i) == x && closedY.get(i) == y)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given point is in the open list.
	 */
	public boolean inOpen(int x, int y)
	{
		for (int i = 0; i < openX.size(); i++)
		{
			if (openX.get(i) == x && openY.get(i) == y)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the open list is empty.
	 */
	public boolean emptyOpen()
	{
		if (openX.size() > 0)
		{
			return false;
		}
		return true;
	}

	/**
	 * Sets the start and end coordinates (random for now).
	 */
	public void setCoordinates()
	{
		boolean exit = false;
		while (exit == false)
		{
			startX = (int) (Math.random() * mapWidth);
			startY = (int) (Math.random() * mapHeight);
			destX = (int) (Math.random() * mapWidth);
			destY = (int) (Math.random() * mapHeight);
			if (board[startY][startX] == 0 && board[destY][destX] == 0)
			{
				if (startX != destX && startY != destY)
				{
					exit = true;
				}
			}
		}
	}

	/**
	 * Gets the position of a point within walkable distance of the current point.
	 * Adds the point onto the open list.
	 */
	public void getPosition(int newX, int newY, int tempX, int tempY, int tempG)
	{
		if (newX > -1 && newY > -1 && newX < mapWidth + 1
		    && newY < mapHeight + 1)
		{
			if (inOpen(newX, newY) == false)
			{
				if (inClosed(newX, newY) == false)
				{
					if (board[newY][newX] == 0)
					{
						openX.add(newX);
						openY.add(newY);
						openG.add(tempG + 1);
						openH.add(getDistance(newX, newY, destX, destY));
						openF.add((tempG + 1) + getDistance(newX, newY, destX, destY));
						openPx.add(tempX);
						openPy.add(tempY);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void findPath()
	{
		// Clear all the lists and the path data
		openX.clear();
		openY.clear();
		openF.clear();
		openG.clear();
		openH.clear();
		openPx.clear();
		openPy.clear();

		closedX.clear();
		closedY.clear();
		closedF.clear();
		closedG.clear();
		closedH.clear();
		closedPx.clear();
		closedPy.clear();

		pathX.clear();
		pathY.clear();

		// Place the start position onto the open list
		openX.add(startX);
		openY.add(startY);
		openF.add(0);
		openG.add(0);
		openH.add(0);
		openPx.add(0);
		openPy.add(0);

		// Initialize temp values
		boolean exit = false;
		int tempX = 0;
		int tempY = 0;
		int tempF = 0;
		int tempG = 0;
		int tempH = 0;
		int tempPx = 0;
		int tempPy = 0;

		int minFCost = 0;

		while (exit == false)
		{
			// Exit the loop if the open list is empty
			if (emptyOpen() == true)
			{
				exit = true;
			}
			
			minFCost = Integer.MAX_VALUE; // Set the F cost to arbitrary large number for comparison
			for (int i = 0; i < openX.size(); i++)
			{
				if (openF.get(i) < minFCost)
				{
					minFCost = openF.get(i);
					tempX = openX.get(i);
					tempY = openY.get(i);
					tempF = openF.get(i);
					tempG = openG.get(i);
					tempH = openH.get(i);
					tempPx = openPx.get(i);
					tempPy = openPy.get(i);
				}
			}

			// If the current point is the target location, then the path has 
			// been found.
			if (tempX == destX && tempY == destY)
			{
				exit = true;
				closedX.add(tempX);
				closedY.add(tempY);
				closedF.add(tempF);
				closedG.add(tempG);
				closedH.add(tempH);
				closedPx.add(tempPx);
				closedPy.add(tempPy);
				traversePath();
			}
			else
			{
				// Move the current position onto the closed list
				closedX.add(tempX);
				closedY.add(tempY);
				closedF.add(tempF);
				closedG.add(tempG);
				closedH.add(tempH);
				closedPx.add(tempPx);
				closedPy.add(tempPy);
				
				// Remove the current position from the open list
				removeFromOpen(tempX, tempY);
				
				// Get the eight positions around the current point
				// Move them onto the open list
				getPosition(tempX, tempY + 1, tempX, tempY, tempG); // Up
				getPosition(tempX, tempY - 1, tempX, tempY, tempG); // Down
				getPosition(tempX - 1, tempY, tempX, tempY, tempG); // Left
				getPosition(tempX + 1, tempY, tempX, tempY, tempG); // Right
				getPosition(tempX - 1, tempY + 1, tempX, tempY, tempG); // Left up diagonal
				getPosition(tempX - 1, tempY - 1, tempX, tempY, tempG); // Left down diagonal
				getPosition(tempX + 1, tempY + 1, tempX, tempY, tempG); // Right up diagonal
				getPosition(tempX + 1, tempY - 1, tempX, tempY, tempG); // Right down diagonal
			}
		}
	}

	/**
	 * Calculates the distance using the equation:
	 * d = sqrt((x1 - x2)^2 + (y1 - y2)^2)
	 */
	public int getDistance(int x1, int y1, int x2, int y2)
	{
		int distance = (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
		    * (y1 - y2));
		return distance;
	}

	/**
	 * Draws the board, the start and destination points, and the path  on the applet screen.
	 * This method is necessary for using the applet.
	 */
	public void paint(Graphics g)
	{
		setCoordinates();
		findPath();
		
		// DRAWING THE BOARD	
		// Draw the obstacles
		g.setColor(new Color(46, 139, 87));
		for (int y = 0; y < mapHeight; y++)
		{
			for (int x = 0; x < mapWidth; x++)
			{
				if (board[y][x] == 1)
				{
					g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
				}
			}
		}
		
		// Draw the start position
		g.setColor(Color.GREEN);
		g.setColor(new Color(0, 0, 238));
		g.fillOval(startX * cellWidth + 4, startY * cellHeight + 4, 8, 8);
		
		// Draw the destination position
		g.setColor(new Color(255, 48, 48));
		g.fillOval(destX * cellWidth + 4, destY * cellHeight + 4, 8, 8);

		// Draw the path
		g.setColor(new Color(255, 255, 240));
		for (int i = 0; i < pathX.size(); i++)
		{
			g.fillOval(pathX.get(i) * cellWidth + 4, pathY.get(i) * cellHeight + 8, 8, 8);
		}
	}
}