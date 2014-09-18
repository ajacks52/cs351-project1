/**
 * @author
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
import antworld.dynamicmap.AnalyzeMap;

// AI
import antworld.ai.AntAStar;

public class AntLogic
{
  static AntAction drop;
  // static AntAction drop = new AntAction(AntActionType.DROP,
  //    Direction.getRandomDir(), 50);
  static ArrayList<Integer> eaten = new ArrayList<Integer>();
  static ArrayList<Direction> path = new ArrayList<Direction>();
  static ArrayList<AntAStar> listofpaths = new ArrayList<AntAStar>();

  static Color[][] map = ClientRandomWalk.getMap(); // Map of colors

  /**
   * 
   * @param antX
   * @param antY
   * @param nestX
   * @param nestY
   * @return drop - with the correct direction for the ant to drop its food.
   */
  public AntAction dropFood(int antX, int antY, int nestX, int nestY)
  {
    if (antX < nestX && antY == nestY)
    {
      drop = new AntAction(AntActionType.DROP, Direction.EAST, 50);
    }
    else if (antX > nestX && antY == nestY)
    {
      drop = new AntAction(AntActionType.DROP, Direction.WEST, 50);
    }
    
    else if (antX == nestX && antY < nestY)
    {
      drop = new AntAction(AntActionType.DROP, Direction.NORTH, 50);
    }
    
    else if (antX == nestX && antY > nestY)
    {
      drop = new AntAction(AntActionType.DROP, Direction.SOUTH, 50);
    }
    
    else if (antX > nestX && antY > nestY) 
    {
      drop = new AntAction(AntActionType.DROP, Direction.SOUTHWEST, 50);
    }
    
    else if (antX > nestX && antY < nestY)
    {
      drop = new AntAction(AntActionType.DROP, Direction.NORTHWEST, 50);
    }
    
    else if (antX < nestX && antY > nestY)
    {
      drop = new AntAction(AntActionType.DROP, Direction.SOUTHEAST, 50);
    }
    
    else if (antX < nestX && antY < nestY)
    {
      drop = new AntAction(AntActionType.DROP, Direction.NORTHEAST, 50);
    }
    else drop = new AntAction(AntActionType.DROP, Direction.getRandomDir(), 50);
    
    return drop;
  }

  /**
   * 
   * @param action
   * @return the current ant's action.
   */
  public AntAction putInStasis(AntAction action)
  {
    action.type = AntActionType.STASIS;
    action.x = ClientRandomWalk.getCenterX() - Constants.NEST_RADIUS
        + Constants.random.nextInt(2 * Constants.NEST_RADIUS);
    action.y = ClientRandomWalk.getCenterY() - Constants.NEST_RADIUS
        + Constants.random.nextInt(2 * Constants.NEST_RADIUS);
    return action;
  }

  /**
   * Gets called from ClientRandomWalk finds the best next move for each ant in
   * the ant list
   * 
   * @param CommData, AntData
   * @return AntAction
   */
  public static AntAction chooseAction(CommData data, AntData ant)
  {
    AntAction action = new AntAction(AntActionType.STASIS);

    int absToNestX = Math.abs(ant.gridX - ClientRandomWalk.getCenterX());
    int absToNestY = Math.abs(ant.gridY - ClientRandomWalk.getCenterY());

    if (ant.ticksUntilNextAction > 0)
    {
      return action;
    }

    if (underGround(ant))
    {

      action.type = AntActionType.STASIS;
      return action;
      
      // return new AntLogic().putInStasis(action);
      // action.type = AntActionType.STASIS;
      //    action.x = ClientRandomWalk.getCenterX() - Constants.NEST_RADIUS
      //    + Constants.random.nextInt(2 * Constants.NEST_RADIUS);
      // action.y = ClientRandomWalk.getCenterY() - Constants.NEST_RADIUS
      //    + Constants.random.nextInt(2 * Constants.NEST_RADIUS);
      // return action;
    }

    System.out.println(AnalyzeMap.rgb[ant.gridY][ant.gridX] + ",   "
        + new Color(0xF0E68C));

    if (AnalyzeMap.rgb[ant.gridY][ant.gridX].equals(new Color(0xF0E68C)))
    {
      if (ant.carryUnits > 0)
      {
        // TODO: drops food 
        action.type = AntActionType.DROP;
        return new AntLogic().dropFood(ant.gridX, ant.gridY, 
            ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY());
        //return drop;
      }
      System.out.println("\n\n\nit's working!!!\n\n\n");
      action.type = AntActionType.ENTER_NEST;
      return action;
    }
    
    if (ant.health < ant.antType.getMaxHealth()/2)
    {
      action.type = AntActionType.MOVE;
      return findPathStupidWay(ant.gridX, ant.gridY, 
          ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action, ant);
    }
    // TODO: uncomment these to manually control ant if needed
    //        comment out return statement below if doing this
    // action.direction = Direction.NORTHWEST;
    // action.type = AntActionType.MOVE;
    // return action;

    return findPathStupidWay(ant.gridX, ant.gridY,
        ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(), action,
        ant);
    
    //
    // if (ant.health < ant.antType.getMaxHealth() / 2)
    // {
    //
    // return findpathStupidWayHome(ant.gridX, ant.gridY,
    // ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(),
    // action, ant);
    //
    //
    // }
    // // action.type = AntActionType.MOVE;
    // // AntAStar pathlist = usingAstar(ant.id);
    // // if (pathlist != null) // the ant is on already using astar
    // // {
    // // action.direction = pathlist.getPath().remove(0);
    // // return action;
    // // } else
    // // {
    // // path = new AntAStar(ant.gridX, ant.gridY,
    // // ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(),
    // // ant.id, map).getPath();
    // // listofdirs.add(path);
    // // action.direction = path.remove(0);
    // // return action;
    // // }
    // // }
    //
    // // TODO: store this in method
    // // Distance to nest

    // if (ant.antType.equals(AntType.BASIC))
    // {
    // if (ant.carryUnits > 0)
    // {
    // if (absToNestX < 13 && absToNestY < 13)
    // {
    // System.out.println("/n/n/n/n I am dropping food!! /n//n/n/");
    // return drop;
    // }
    //
    // System.out.println("i am carrying food " + ant.id);
    // return findpathStupidWayHome(ant.gridX, ant.gridY,
    // ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY(),
    // action, ant);
    // }
    // }
    //
    // FoodData foodpiece = foodBeingEatten(data);
    // if (foodpiece != null)
    // {
    // // Distance to food
    // // TODO: commented these out
    // // int absfoodX = Math.abs(ant.gridX - foodpiece.gridX);
    // // int absfoodY = Math.abs(ant.gridY - foodpiece.gridY);
    //
    // // System.out.println("Food in area? " + foodpiece.gridX);
    // return findpathStupidWay(ant.gridX, ant.gridY, foodpiece.gridX,
    // foodpiece.gridY, action, ant);
    // // return findpathAstar(ant.gridX, ant.gridY, foodpiece.gridX,
    // // foodpiece.gridY, action, ant);
    // }
    // // no food near you find some!!
    // // TODO: commented this out
    // // if (data.foodStockPile[FoodType.WATER.ordinal()] < 100)
    // // {
    // // return findpathAstar(ant.gridX, ant.gridY, 3495, 832, action, ant);
    // // }
    //
    // action.type = AntActionType.MOVE;
    // action.direction = Direction.SOUTHEAST;
    // return action;
  }

  /**
   * 
   * @param antX
   * @param antY
   * @param locationX
   * @param locationY
   * @param action
   * @param ant
   * @return the current ant's action to be performed.
   */
  private static AntAction findPathStupidWay(int antX, int antY,
      int locationX, int locationY, AntAction action, AntData ant)
  {
    action.type = AntActionType.MOVE;
    action.direction = Direction.getRandomDir();

    if (antX == locationX && antY > locationY)
    {
      action.direction = Direction.NORTH;
      return action;
    }

    if (antX == locationX && antY < locationY)
    {
      action.direction = Direction.SOUTH;
    }

    if (antX < locationX && antY == locationY)
    {
      action.direction = Direction.EAST;
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
      action.direction = Direction.SOUTHEAST;
      return action;
    }

    if (antX < locationX && antY > locationY)
    {
      action.direction = Direction.NORTHEAST;
      return action;
    }

    if (antX < locationX && antY < locationY)
    {
      action.direction = Direction.SOUTHWEST;
      return action;
    }
    // action.direction = Direction.NORTH;
    return action;
  }

  // private static AntAction findpathStupidWay(int antX, int antY, int
  // locationX,
  // int locationY, AntAction action, AntData ant)
  // {
  // action.type = AntActionType.MOVE;
  //
  // if (antX + 1 == locationX && antY == locationY)
  // {
  // action.quantity = 50;
  // action.direction = Direction.EAST;
  // action.type = AntActionType.PICKUP;
  // return drop;
  // }
  //
  // if (antX - 1 == locationX && antY == locationY)
  // {
  // action.quantity = 50;
  // action.direction = Direction.WEST;
  // action.type = AntActionType.PICKUP;
  // return action;
  // }
  //
  // if (antX == locationX && antY + 1 == locationY)
  // {
  // action.quantity = 50;
  // action.direction = Direction.SOUTH;
  // action.type = AntActionType.PICKUP;
  // return action;
  // }
  //
  // if (antX == locationX && antY - 1 == locationY)
  // {
  // action.quantity = 50;
  // action.direction = Direction.NORTH;
  // action.type = AntActionType.PICKUP;
  // return action;
  // }
  //
  // if (antX < locationX)
  // {
  // action.direction = Direction.EAST;
  // return action;
  // }
  // if (antX > locationX)
  // {
  // action.direction = Direction.WEST;
  // return action;
  // }
  // if (antY < locationY)
  // {
  // action.direction = Direction.SOUTH;
  // return action;
  // }
  // if (antY > locationY)
  // {
  // action.direction = Direction.NORTH;
  // return action;
  // }
  // return action;
  // }

  // private static AntAction findpathAstar(int antX, int antY, int locationX,
  // int locationY, AntAction action, AntData ant)
  // {
  // AntAStar pathlist = usingAstar(ant.id);
  // if (pathlist != null) // the ant is on already using astar
  // {
  // action.type = AntActionType.MOVE;
  // action.direction = pathlist.getPath().remove(0);
  // return action;
  // } else
  // {
  // path = new AntAStar(antX, antY, locationX, locationY, ant.id, map)
  // .getPath();
  // // listofdirs.add(path);
  // action.type = AntActionType.MOVE;
  // System.out.println("path size " + path.size());
  // action.direction = path.remove(0);
  // return action;
  // }
  //
  // }

  /**
   * Checks if the food piece in question is already being eaten
   * 
   * @param CommData data
   * @return FoodData
   */
  private static FoodData foodBeingEatten(CommData data)
  {
    if (data.foodSet.size() > 0)
    {
      for (FoodData foodpiece : data.foodSet)
      {
        for (int e : eaten)
        {
          if (foodpiece.hashCode() == e)
          {
            break;
          }
        }
        eaten.add(foodpiece.hashCode());
        return foodpiece;
      }
    }
    return null;
  }

  /**
   * Checks if ant is underground
   * 
   * @param AntData ant
   * @return true if an ant is underground, otherwise false.
   */
  private static boolean underGround(AntData ant)
  {
    if (ant.underground) return true;
    return false;
  }
}
