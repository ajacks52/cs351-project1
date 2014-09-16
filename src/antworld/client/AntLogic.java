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
  static AntAction drop = new AntAction(AntActionType.DROP, Direction.getRandomDir(), 50);
  static ArrayList<Integer> eatten = new ArrayList<Integer>();
  static ArrayList<Direction> path = new ArrayList<Direction>();
  static ArrayList<AntAStar> listofpaths = new ArrayList<AntAStar>();
  static ArrayList<ArrayList> listofdirs = new ArrayList<ArrayList>();

  /**
   * Gets called from ClientRandomWalk finds the best next move for each ant in the ant list
   * @param CommData, AntData
   * @return AntAction 
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
        return findpath(ant.gridX, ant.gridY,  ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action, ant);
      }
    }
    FoodData foodpiece = foodBeingEatten(data);
    if (foodpiece != null)
    {
      // Distance to food
      int absfoodX = Math.abs(ant.gridX - foodpiece.gridX);
      int absfoodY = Math.abs(ant.gridY - foodpiece.gridY);
      return findpath(ant.gridX, ant.gridY,  foodpiece.gridX, foodpiece.gridY, action, ant);
    }
    // no food near you find some!!
    if (data.foodStockPile[FoodType.WATER.ordinal()] < 100)
    {
      
      return findpath(ant.gridX, ant.gridY,  3495, 832, action, ant); 
    }
    
    action.type = AntActionType.MOVE;
    action.direction = Direction.getRandomDir();
    return action;
  }

  /**
   * Checks if ant is currently using astar i.e. has a path. If the ant 
   * is already using astar then it returns the next action the ant needs to take 
   * according to astar if it's not using astar then it makes a new instance of astar
   * and then return the ants first action based on astars calculation
   * @param int antX
   * @param int antY
   * @param int locationX
   * @param int locationY
   * @param AntAction action
   * @param AntData ant
   * @return AntAction
   */
  private static AntAction findpath(int antX, int antY, int locationX, int locationY, AntAction action, AntData ant)
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
   * Checks if the food piece in question is already being eatten 
   * @param CommData data
   * @return FoodData
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
   * Checks whether ant is using astar already
   * @param int the ant id
   * @return AntAStar
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
   * Checks if ant is underground
   * @param AntData ant
   * @return boolean
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
