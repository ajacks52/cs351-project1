package antworld.client;

import java.awt.Color;
import java.util.ArrayList;

import antworld.data.AntAction;
import antworld.data.AntData;
import antworld.data.AntType;
import antworld.data.CommData;
import antworld.data.Constants;
import antworld.data.Direction;
import antworld.data.AntAction.AntActionType;
import antworld.data.FoodData;
import antworld.data.FoodType;
// AI
import antworld.ai.AntAStar;
import antworld.ai.Node;

public class AntLogic
{
  static AntData ant;

  static AntAction drink = new AntAction(AntActionType.PICKUP, Direction.SOUTHEAST, 50);
  static AntAction goHome = new AntAction(AntActionType.MOVE, Direction.NORTHWEST);
  static AntAction findWater = new AntAction(AntActionType.MOVE, Direction.SOUTHEAST);
  static AntAction drop = new AntAction(AntActionType.DROP, Direction.getRandomDir(), 50);
  static ArrayList<Integer> eatten = new ArrayList<Integer>();

  public static ArrayList<Direction> path = new ArrayList<Direction>();
  public static ArrayList<AntAStar> listofpaths = new ArrayList<AntAStar>();
  public static ArrayList<ArrayList> listofdirs = new ArrayList<ArrayList>();

  /**
   * 
   * @param data
   * @return
   */
  public static AntAction chooseAction(CommData data, AntData ant)
  {
    AntAction action = new AntAction(AntActionType.STASIS);
    if (ant.ticksUntilNextAction > 0)
    {
      return action;
    }
    if (underGround(ant))
    {
      action.type = AntActionType.EXIT_NEST;
      action.x = ClientRandomWalk.getCenterX() - Constants.NEST_RADIUS
          + Constants.random.nextInt(2 * Constants.NEST_RADIUS);
      action.y = ClientRandomWalk.getCenterY() - Constants.NEST_RADIUS
          + Constants.random.nextInt(2 * Constants.NEST_RADIUS);
      return action;
    }
    if (ant.health < ant.antType.getMaxHealth() / 2)
    {
      action.type = AntActionType.MOVE;
      AntAStar pathlist = usingAstar(ant.id);
      if (pathlist != null) // the ant is on already using astar
      {
        action.direction = pathlist.getPath().remove(0);
        return action;
      }
      else
      {
        path = new AntAStar(ant.gridX, ant.gridY, ClientRandomWalk.getCenterX(),
            ClientRandomWalk.getCenterY(), ant.id).getPath();
        listofdirs.add(path);
        action.direction = path.remove(0);
        return action;
      }
    }
    // Distance to nest
    int absToNestX = Math.abs(ant.gridX - ClientRandomWalk.getCenterX());
    int absToNestY = Math.abs(ant.gridY - ClientRandomWalk.getCenterY());
    if (ant.antType.equals(AntType.BASIC))
    {
      if (ant.carryUnits > 0)
      {
        if (absToNestX < 10 && absToNestY < 10)
        {
          System.out.println("/n/n/n/n I am dropping food!! /n//n/n/");
          return drop;
        }
        return findpath(ant.gridX, ant.gridY,  ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action);
      }
    }
    FoodData foodpiece = foodBeingEatten(data);
    if (foodpiece != null)
    {
      // Distance to food
      int absfoodX = Math.abs(ant.gridX - foodpiece.gridX);
      int absfoodY = Math.abs(ant.gridY - foodpiece.gridY);
      return findpath(ant.gridX, ant.gridY,  foodpiece.gridX, foodpiece.gridY, action);
    }
    // no food near you find some!!
    if (data.foodStockPile[FoodType.WATER.ordinal()] < 100)
    {
      
      return findpath(ant.gridX, ant.gridY,  3495, 832, action); 
    }
    
    action.type = AntActionType.MOVE;
    action.direction = Direction.getRandomDir();
    return action;
  }

  /**
   * 
   * @param data
   * @return
   */
  private static AntAction findpath(int antX, int antY, int locationX, int locationY, AntAction action)
  {
    AntAStar pathlist = usingAstar(ant.id);
    if (pathlist != null) // the ant is on already using astar
    {
      action.type = AntActionType.MOVE;
      action.direction = pathlist.getPath().remove(0);
      return action;
    }
    else
    {
      path = new AntAStar(antX, antY, locationX, locationY, ant.id).getPath();
      listofdirs.add(path);
      action.type = AntActionType.MOVE;
      action.direction = path.remove(0);
      return action;
    }
    
  }
  /**
   * 
   * @param data
   * @return
   */
  private static FoodData foodBeingEatten(CommData data)
  {
    if (data.foodSet.size() > 0)
    {
      for (FoodData foodpiece : data.foodSet)
      {
        for (int e : eatten)
        {
          if (foodpiece.hashCode() == e)
          {
            break;
          }
        }
        eatten.add(foodpiece.hashCode());
        return foodpiece;
      }
    }
    return null;
  }
  
  /**
   * 
   * @param data
   * @return
   */
  private static AntAStar usingAstar(int id)
  {
    for (AntAStar i : listofpaths)
    {
      if (id == i.getID())
      {
        return i;
      }
    }
    return null;
  }

  /**
   * 
   * @param data
   * @return
   */
  private static Direction nextDir(AntAStar path)
  {
    return path.getPath().remove(0);
  }

  /**
   * 
   * @param data
   * @return
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
