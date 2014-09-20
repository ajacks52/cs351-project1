/**
 * @author Adam Mitchell
 * @author Erin Sosebee
 * @version 2014-18-09
 * 
 * The AntLogicBasic class sends ants out to go foraging for food.
 */
package antworld.client;

import java.awt.Color;
import java.util.ArrayList;

// Client
import antworld.client.ClientRandomWalk;

// Data
import antworld.data.AntAction;
import antworld.data.AntData;
import antworld.data.CommData;
import antworld.data.Constants;
import antworld.data.Direction;
import antworld.data.AntAction.AntActionType;
import antworld.data.FoodData;

// AI
import antworld.ai.AntAStar;

public class AntLogicBasic
{
  static boolean DEBUG = false;
  static int numAnts;
  static AntAction drop = new AntAction(AntActionType.DROP,
      Direction.getRandomDir(), 50);
  static AntAction eat = new AntAction(AntActionType.PICKUP,
      Direction.getRandomDir(), 24);
  static AntAction goInside = new AntAction(AntActionType.ENTER_NEST);
  static ArrayList<Integer> eaten = new ArrayList<Integer>();
  static ArrayList<Direction> path = new ArrayList<Direction>();
  static ArrayList<AntAStar> listofpaths = new ArrayList<AntAStar>();

  static Color[][] map = ClientRandomWalk.getMap(); // Map of colors

  /**
   * Instructs an ant to leave the nest.
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
   * Gets called from ClientRandomWalk. Finds the best next move for each ant
   * in the ant list.
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

    if (ant.carryUnits > 0)
    {
      System.out.println("i am carrying food " + ant.id);
      return findpathStupidWay(ant.gridX, ant.gridY,
          ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action,
          ant);
    }

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

    if (ant.health < ant.antType.getMaxHealth() / 2)
    {
      // return findpathStupidWay(ant.gridX, ant.gridY,
      // 2464, 460, action,
      // ant);
      //
      // return findpathStupidWay(ant.gridX, ant.gridY,
      // 3448, 568, action,
      // ant);

      // action.direction = Direction.WEST;
      // return action;
      return findpathStupidWay(ant.gridX, ant.gridY,
          ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action,
          ant);

    }

    if (data.foodStockPile[1] < 50)
    {
      System.out.println("getting water!");
      if (ant.gridX == 3495 && ant.gridY == 832)
      {
        eat.direction = Direction.SOUTHWEST;
        return eat;
      }
      return findpathStupidWay(ant.gridX, ant.gridY, 3495, 832, action, ant);
    }

    if (data.foodSet.size() > 0)
    {
      FoodData foodpiece = foodBeingEatten(data);
      if (foodpiece != null)
      {
        int absToFoodX = Math.abs(ant.gridX - foodpiece.gridX);
        int absToFoodY = Math.abs(ant.gridY - foodpiece.gridY);
        int absX = Math.abs(foodpiece.gridX - ClientRandomWalk.getCenterX());
        int absY = Math.abs(foodpiece.gridY - ClientRandomWalk.getCenterY());

        if (absX > 23 && absY > 23)
        {
          if (DEBUG)
          {
            System.out.println("Food in area? " + foodpiece.gridX);
          }
          if (absToFoodX < 2 && absToFoodY < 2)
          {
            System.out.println("\n\n\n\nshould be eatting food!!");
            return eat(ant.gridX, ant.gridY, foodpiece.gridX, foodpiece.gridY);
          }
          return findpathStupidWay(ant.gridX, ant.gridY, foodpiece.gridX,
              foodpiece.gridY, action, ant);
        }
      }
    }
    action.type = AntActionType.MOVE;

    action.direction = Direction.SOUTHEAST;
    return action;
  }

  /**
   * Tell the ant to eat a piece of food.
   * 
   * @param antX
   * @param antY
   * @param nestX
   * @param nestY
   * @return the AntAction for eating.
   */
  private static AntAction eat(int antX, int antY, int nestX, int nestY)
  {
    if (antX - 1 == nestX && antY == nestY)
    {
      System.out.println("eatting west");
      eat = new AntAction(AntActionType.PICKUP, Direction.WEST, 50);
    } else if (antX + 1 == nestX && antY == nestY)
    {
      System.out.println("eatting east");
      eat = new AntAction(AntActionType.PICKUP, Direction.EAST, 50);
    } else if (antX == nestX && antY - 1 == nestY)
    {
      System.out.println("eatting north");
      eat = new AntAction(AntActionType.PICKUP, Direction.NORTH, 50);
    } else if (antX == nestX && antY + 1 == nestY)
    {
      System.out.println("eatting south");
      eat = new AntAction(AntActionType.PICKUP, Direction.SOUTH, 50);
    } else if (antX + 1 == nestX && antY + 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.SOUTHEAST, 50);
    } else if (antX + 1 == nestX && antY - 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.NORTHEAST, 50);
    } else if (antX - 1 == nestX && antY + 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.SOUTHWEST, 50);
    } else if (antX - 1 == nestX && antY - 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.NORTHWEST, 50);
    } else
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.getRandomDir(), 50);
    }
    return eat;
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
    System.out.println("\n\n\n\nFINDING PATH\n\n\n\n");
    int absX = Math.abs(ant.gridX - ClientRandomWalk.getCenterX());
    int absY = Math.abs(ant.gridY - ClientRandomWalk.getCenterY());

    if (locationX == ClientRandomWalk.getCenterX()
        && locationY == ClientRandomWalk.getCenterY())
    {
      // if (AnalyzeMap.rgb[ant.gridY][ant.gridX].equals(new Color(0xF0E68C)))
      if (absX <= 10 && absY <= 10)
      {
        if (ant.carryUnits > 0)
        {
          return drop;
        } else
        {
          System.out.println("Going inside");
          return goInside;
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
   * Tells an ant to drop food.
   * 
   * @param antX
   * @param antY
   * @param nestX
   * @param nestY
   * @return drop - with the correct direction for the ant to drop its food.
   */
  public AntAction dropFood(int antX, int antY, int nestX, int nestY)
  {
    if (antX - 1 == nestX && antY == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.EAST, 50);
    } else if (antX + 1 == nestX && antY == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.WEST, 50);
    } else if (antX == nestX && antY - 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.NORTH, 50);
    } else if (antX == nestX && antY + 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.SOUTH, 50);
    } else if (antX + 1 == nestX && antY + 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.SOUTHWEST, 50);
    } else if (antX + 1 == nestX && antY - 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.NORTHWEST, 50);
    } else if (antX - 1 == nestX && antY + 1 == nestY)
    {
      // eat = new AntAction(AntActionType.PICKUP, Direction.SOUTHEAST, 50);
      eat = new AntAction(AntActionType.PICKUP, Direction.NORTHWEST, 50);
    } else if (antX - 1 == nestX && antY - 1 == nestY)
    {
      eat = new AntAction(AntActionType.PICKUP, Direction.NORTHEAST, 50);
    } else
      eat = new AntAction(AntActionType.PICKUP, Direction.getRandomDir(), 50);
    return eat;
  }

  /**
   * Checks if the food piece in question is already being eaten
   * 
   * @param CommData data
   * @return the food being eaten.
   */
  private static FoodData foodBeingEatten(CommData data)
  {
    for (FoodData foodpiece : data.foodSet)
    {
      return foodpiece;
    }
    return null;
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