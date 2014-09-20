/**
 * @author Mitchell Adams
 * @author Erin Sosebee
 * @version 2014-18-09
 * 
 * The AntLogicAttack class sends ants out to attack other ants.
 */
package antworld.client;

import java.awt.Color;
import java.util.ArrayList;

import java.util.Iterator;

// Client
import antworld.client.ClientRandomWalk;

// Data
import antworld.data.AntAction;
import antworld.data.AntData;
import antworld.data.CommData;
import antworld.data.Constants;
import antworld.data.Direction;
import antworld.data.AntAction.AntActionType;
import antworld.dynamicmap.AnalyzeMap;

// AI
import antworld.ai.AntAStar;

public class AntLogicAttack
{
  static boolean DEBUG = false;
  static int numAnts;
  static AntAction drop = new AntAction(AntActionType.DROP,
      Direction.getRandomDir(), 50);
  static AntAction attack = new AntAction(AntActionType.PICKUP,
      Direction.getRandomDir(), 50);
  static AntAction goinside = new AntAction(AntActionType.ENTER_NEST);
  static ArrayList<Integer> eaten = new ArrayList<Integer>();
  static ArrayList<Direction> path = new ArrayList<Direction>();
  static ArrayList<AntAStar> listofpaths = new ArrayList<AntAStar>();

  static Color[][] map = ClientRandomWalk.getMap(); // Map of colors

  /**
   * Tells the ant to leave the nest.
   * 
   * @param action
   * @return the current ant's action - stasis.
   */
  public static AntAction exitNest(AntAction action)
  {
    action.type = AntActionType.EXIT_NEST;

    action.x = ClientRandomWalk.getCenterX() - Constants.NEST_RADIUS
        + Constants.random.nextInt(2 * Constants.NEST_RADIUS);
    action.y = ClientRandomWalk.getCenterY() - Constants.NEST_RADIUS
        + Constants.random.nextInt(2 * Constants.NEST_RADIUS);

    return action;
  }

  /**
   * Gets called from ClientRandomWalk and finds the best next move for each 
   * ant in the ant list.
   * 
   * @param data
   * @param ant
   * @param num
   * @return
   */
  public static AntAction chooseAction(CommData data, AntData ant, int num)
  {
    numAnts = num;
    AntAction action = new AntAction(AntActionType.STASIS);

    if (ant.ticksUntilNextAction > 0)
    {
      return action;
    }

    if (underGround(ant))
    {
      if (ant.health < ant.antType.getMaxHealth() / 2)
      {
        System.out.println("healing ants!");
        action.type = AntActionType.HEAL;
        return action;
      }
      return exitNest(action);
    }

    if (ant.health < ant.antType.getMaxHealth() + 2)
    {
      return findpathStupidWay(ant.gridX, ant.gridY,
          ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action,
          ant);
    }

    if (data.enemyAntSet.size() > 0)
    {
      Iterator<AntData> i = data.enemyAntSet.iterator();
      if (i.hasNext())
      {
        AntData element = i.next();
        int absToAntX = Math.abs(ant.gridX - element.gridX);
        int absToAntY = Math.abs(ant.gridY - element.gridY);

        if (absToAntX < 2 && absToAntY < 2)
        {
          return attack(ant.gridX, ant.gridY, element.gridX, element.gridY);
        }

        return findpathStupidWay(ant.gridX, ant.gridY, element.gridX,
            element.gridY, action, ant);
      }
    }

    Iterator<AntData> i = data.myAntList.iterator();
    if (i.hasNext())
    {
      AntData element = i.next();

      int absToAntX = Math.abs(ant.gridX - element.gridX);
      int absToAntY = Math.abs(ant.gridY - element.gridY);

      if (absToAntX < 2 && absToAntY < 2)
      {
        if (!element.nestName.toString().equals(ant.nestName.toString()))
        {
          return attack(ant.gridX, ant.gridY, element.gridX, element.gridY);
        }
      }

      return findpathStupidWay(ant.gridX, ant.gridY,
          ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action,
          ant);
      // return findpathStupidWay(ant.gridX, ant.gridY,
      // 3448, 568, action,
      // ant);
      // return findpathStupidWay(ant.gridX, ant.gridY, element.gridX,
      // element.gridY, action, ant);
    }

    action.type = AntActionType.MOVE;
    action.direction = Direction.NORTHEAST;
    return action;

  }

  /**
   * Tells the ant which direction to head in order to attack an enemy.
   * 
   * @param antX
   * @param antY
   * @param nestX
   * @param nestY
   * @return
   */
  private static AntAction attack(int antX, int antY, int nestX, int nestY)
  {

    if (antX - 1 == nestX && antY == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.WEST, 50);
    } else if (antX + 1 == nestX && antY == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.EAST, 50);
    } else if (antX == nestX && antY - 1 == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.NORTH, 50);
    } else if (antX == nestX && antY + 1 == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.SOUTH, 50);
    } else if (antX + 1 == nestX && antY + 1 == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.SOUTHEAST, 50);
    } else if (antX + 1 == nestX && antY - 1 == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.NORTHEAST, 50);
    } else if (antX - 1 == nestX && antY + 1 == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.SOUTHWEST, 50);
    } else if (antX - 1 == nestX && antY - 1 == nestY)
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.NORTHWEST, 50);
    } else
    {
      attack = new AntAction(AntActionType.ATTACK, Direction.getRandomDir(), 50);
    }
    return attack;
  }

  /**
   * A naive algorithm for finding a path to a destination.
   * 
   * @param antX
   * @param antY
   * @param locationX
   * @param locationY
   * @param action
   * @param ant
   * @return the AntAction.Direction for the ant to move.
   */
  private static AntAction findpathStupidWay(int antX, int antY, int locationX,
      int locationY, AntAction action, AntData ant)
  {
    if (locationX == ClientRandomWalk.getCenterX()
        && locationY == ClientRandomWalk.getCenterY())
    {
      if (AnalyzeMap.rgb[ant.gridY][ant.gridX].equals(new Color(0xF0E68C)))
      {
        if (ant.carryUnits > 0)
        {
          return drop;
        } else
        {
          return goinside;
        }
      }
    }

    action.type = AntActionType.MOVE;

    if (antX == locationX && antY > locationY)
    {
      action.direction = Direction.NORTH;
      return action;
    }

    if (antX == locationX && antY < locationY)
    {
      action.direction = Direction.SOUTH;
      return action;
    }

    if (antX < locationX && antY == locationY)
    {
      action.direction = Direction.EAST;
      return action;
    }

    if (antX > locationX && antY == locationY)
    {
      action.direction = Direction.WEST;
      return action;
    }

    if (antX > locationX && antY > locationY)
    {
      action.direction = Direction.NORTHWEST;
      return action;
    }

    if (antX > locationX && antY < locationY)
    {
      action.direction = Direction.SOUTHWEST;
      return action;
    }

    if (antX < locationX && antY > locationY)
    {
      action.direction = Direction.NORTHEAST;
      return action;
    }

    if (antX < locationX && antY < locationY)
    {
      action.direction = Direction.SOUTHEAST;
      return action;
    }
    return action;
  }

  /**
   * Checks if ant is underground
   * 
   * @param AntData ant
   * @return true if the ant is underground, otherwise false.
   */
  private static boolean underGround(AntData ant)
  {
    if (ant.underground)
    {
      return true;
    }
    return false;
  }
}