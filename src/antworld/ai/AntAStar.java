package antworld.ai;


/**
 * The AntAStar class determines the least costly path for an ant to travel
 * from its current position to its desired destination using the A* 
 * pathfinding algorithm.
 * @author 	Erin Sosebee
 * @version	2014.18.09
 */

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import antworld.data.Direction;

public class AntAStar
{
  BufferedImage img = null;
  Color[][] map;

  int mapWidth;
  int mapHeight;

  int startX, startY, destX, destY, antID;

  // Open/closed lists (x, y, f, g, h, parentX, parentY)
  ArrayList<Node> open = new ArrayList<Node>();
  ArrayList<Node> closed = new ArrayList<Node>();

  // Path (x, y)
  ArrayList<Node> path = new ArrayList<Node>();

  // Constructor
  public AntAStar(int startX, int startY, int destX, int destY, int id,
      Color[][] map)
  {
    this.startX = startX;
    this.startY = startY;
    this.destX = destX;
    this.destY = destY;
    this.antID = id;
    this.map = map;

    try
    {
      img = ImageIO.read(new File("resources/AntWorld.png"));
    } catch (IOException e)
    {
    }

    mapHeight = img.getHeight();
    mapWidth = img.getWidth();

    findPath(startX, startY, destX, destY);
  }

  /**
   * Once the destination has been reached, the path is built based on which
   * nodes are present in the closed list.
   * 
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   */
  public void traversePath(int x1, int y1, int x2, int y2)
  {
    boolean exit = false;
    int startX = x1;
    int startY = y1;
    int endX = x2;
    int endY = y2;

    path.add(new Node(endX, endY));

    while (exit == false)
    {
      for (int i = 0; i < closed.size(); i++)
      {
        if (closed.get(i).getX() == endX && closed.get(i).getY() == endY)
        {
          endX = closed.get(i).getParentX();
          endY = closed.get(i).getParentY();
          path.add(new Node(endX, endY));
        }
      }
      if (endX == startX && endY == startY)
      {
        exit = true;
      }
    }
  }

  /**
   * Removes a node from the open list.
   * 
   * @param x
   * @param y
   * @return true if a node has been removed, else false.
   */
  public boolean removeFromOpen(int x, int y)
  {
    for (int i = 0; i < open.size(); i++)
    {
      if (open.get(i).getX() == x && open.get(i).getY() == y)
      {
        open.remove(i);
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a point is contained within the list of 'closed' nodes.
   * 
   * @param x
   * @param y
   * @return true if the given point is in the closed list, otherwise false.
   */
  public boolean inClosed(int x, int y)
  {
    for (int i = 0; i < closed.size(); i++)
    {
      if (closed.get(i).getX() == x && closed.get(i).getY() == y)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines whether or not a point is found within the 'open' list of nodes.
   * 
   * @param x
   * @param y
   * @return true if the given point is in the open list, otherwise false.
   */
  public boolean inOpen(int x, int y)
  {
    for (int i = 0; i < open.size(); i++)
    {
      if (open.get(i).getX() == x && open.get(i).getY() == y)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if there are any nodes in the open list.
   * 
   * @return true if the list is empty, otherwise false.
   */
  public boolean emptyOpen()
  {
    if (open.size() > 0)
      return false;
    return true;
  }

  /**
   * Gets the position of a point within reachable distance of the current
   * point. Adds the point onto the open list.
   * 
   * @param newX
   * @param newY
   * @param tempX
   * @param tempY
   * @param tempG
   */
  public void getPosition(int newX, int newY, int tempX, int tempY, int tempG)
  {
    if (newX > -1 && newY > -1 && newX < mapWidth + 1 && newY < mapHeight + 1)
    {
      if (inOpen(newX, newY) == false)
      {
        if (inClosed(newX, newY) == false)
        {
          if (map[newY][newX].getRed() == 0 && map[newY][newX].getBlue() == 0
              && map[newY][newX].getGreen() > 0)
          {
            int newG = tempG + 1;
            int newH = getDistance(newX, newY, destX, destY);
            int newF = newG + newH;
            open.add(new Node(newX, newY, newG, newH, newF, tempX, tempY));
          }
        }
      }
    }
  }

  /**
   * Calculates the shortest path between two points using the A* path finding
   * algorithm.
   * 
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   */
  public void findPath(int x1, int y1, int x2, int y2)
  {
    // Clear all the lists and the path data
    open.clear();
    closed.clear();
    path.clear();

    // Place the start position onto the open list
    open.add(new Node(x1, y1, 0, 0, 0, 0, 0));

    // Initialize temp/comparison values
    boolean exit = false;
    int tempX = 0, tempY = 0;
    int tempF = 0, tempG = 0, tempH = 0;
    int tempPx = 0, tempPy = 0;
    int minFCost = 0;

    while (exit == false)
    {
      // Exit the loop if the open list is empty
      if (emptyOpen() == true)
        exit = true;

      // Set the F cost to arbitrary large number for sake of comparison.
      minFCost = Integer.MAX_VALUE;

      // Finds which point has the lowest F score.
      for (int i = 0; i < open.size(); i++)
      {
        if (open.get(i).getF() < minFCost)
        {
          minFCost = open.get(i).getF();
          tempX = open.get(i).getX();
          tempY = open.get(i).getY();
          tempG = open.get(i).getG();
          tempH = open.get(i).getH();
          tempPx = open.get(i).getParentX();
          tempPy = open.get(i).getParentY();
        }
      }

      // If the current point is the target location, then the path has
      // been found.
      if (tempX == destX && tempY == destY)
      {
        exit = true;
        closed.add(new Node(tempX, tempY, tempF, tempG, tempH, tempPx, tempPy));

        // Get path to be traveled

        traversePath(x1, y1, x2, y2);
      } else
      {
        // Move the current position onto the closed list
        closed.add(new Node(tempX, tempY, tempF, tempG, tempH, tempPx, tempPy));

        // Remove the current position from the open list
        removeFromOpen(tempX, tempY);

        // Get the eight positions around the current point
        // Move them onto the open list
        getPosition(tempX, tempY + 1, tempX, tempY, tempG); // North
        getPosition(tempX, tempY - 1, tempX, tempY, tempG); // South
        getPosition(tempX - 1, tempY, tempX, tempY, tempG); // West
        getPosition(tempX + 1, tempY, tempX, tempY, tempG); // East
        getPosition(tempX - 1, tempY + 1, tempX, tempY, tempG); // Northwest
        getPosition(tempX - 1, tempY - 1, tempX, tempY, tempG); // Southwest
        getPosition(tempX + 1, tempY + 1, tempX, tempY, tempG); // Northeast
        getPosition(tempX + 1, tempY - 1, tempX, tempY, tempG); // Southeast
      }
    }
  }

  /**
   * Calculates the distance between two points using the formula: d = sqrt((x1
   * - x2)^2 + (y1 - y2)^2)
   * 
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @return an int representing the distance between two points.
   */
  public int getDistance(int x1, int y1, int x2, int y2)
  {
    int distance = (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
        * (y1 - y2));
    return distance;
  }

  /**
   * Gets the ID of the ants using A*.
   */
  public int getID()
  {
    return this.antID;
  }

  /**
   * Gets the path the ant has to travel to get to its destination.
   */
  public ArrayList<Direction> getPath()
  {
    Collections.reverse(path);
    ArrayList<Direction> directions = new ArrayList<Direction>();

    System.out.println(path.size());

    for (int i = 1; i < path.size(); i++)
    {
      int tempX = path.get(i - 1).getX();
      int tempY = path.get(i - 1).getY();
      int x = path.get(i).getX();
      int y = path.get(i).getY();

      if (x == tempX && y > tempY)
      {
        directions.add(Direction.NORTH);
      } else if (x == tempX && y < tempY)
      {
        directions.add(Direction.SOUTH);
      } else if (x == tempX + 1 && y == tempY)
      {
        directions.add(Direction.EAST);
      } else if (x == tempX - 1 && y == tempY)
      {
        directions.add(Direction.WEST);
      } else if (x == tempX - 1 && y == tempY + 1)
      {
        directions.add(Direction.NORTHWEST);
      } else if (x == tempX + 1 && y == tempY + 1)
      {
        directions.add(Direction.NORTHEAST);
      } else if (x == tempX + 1 && y == tempY - 1)
      {
        directions.add(Direction.SOUTHEAST);
      } else if (x == tempX - 1 && y == tempY - 1)
      {
        directions.add(Direction.SOUTHWEST);
      }
    }
    return directions;
  }

  /** Unit test */
  // public static void main(String[] args)
  // {
  // new AntAStar(1,2,2,5).getPath();
  // }
}