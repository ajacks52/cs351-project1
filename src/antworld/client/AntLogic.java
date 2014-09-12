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
import antworld.data.LandType;
import antworld.dynamicmap.AnalyzeMap;
// AI
import antworld.ai.AntAStar;
import antworld.ai.Node;


public class AntLogic
{
  static AntData ant;
  static int countBasic = 0;
  static int countMedic = 0;
  static int countVision = 0;
  static int countCarry = 0;
  static int countSpeed = 0;
  static int countDefence = 0;
  static int countAttack = 0;
  static AntAction drink = new AntAction(AntActionType.PICKUP, Direction.SOUTHEAST, 50);
  static AntAction goHome = new AntAction(AntActionType.MOVE, Direction.NORTHWEST);
  static AntAction findWater = new AntAction(AntActionType.MOVE, Direction.SOUTHEAST);
  static AntAction drop = new AntAction(AntActionType.DROP, Direction.getRandomDir(), 50);
  static AntAction action = new AntAction(AntActionType.STASIS);
  static ArrayList<Integer> eatten = new ArrayList<Integer>();
  // TODO: don't know if I should use coordinates or directions to represent the path
  public static ArrayList<String> path = new ArrayList<String>();

  public static AntAction chooseAction(CommData data, AntData ant)
  {
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
      
      // TODO: get ant to travel path to nest
      //action.direction = Direction.getRandomDir(); // location of base told by
                                                   // astar!!!
      path = new AntAStar(ant.gridX, ant.gridY, 
      		ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY()).getPath();
      
      // TODO: not sure if I want to use coordinates or directions here
      //			 not even sure if this is correct
      //			 how do we tell the ant which direction to go?
      // leaving this next part commented out for now
//      for (int i = 0; i < path.size(); i++)
//      {
//      	if (path.get(i).equals("NORTH")) 
//      	{
//      		action.direction = Direction.NORTH;
//      		return action;
//      	}
//      	else if (path.get(i).equals("SOUTH")) 
//      	{
//      		action.direction = Direction.SOUTH;
//      		return action;
//      	}
//      	else if (path.get(i).equals("EAST")) 
//      	{
//      		action.direction = Direction.EAST;
//      		return action;
//      	}
//      	else if (path.get(i).equals("WEST")) 
//      	{
//      		action.direction = Direction.WEST;
//      		return action;
//      	}
//      	else if (path.get(i).equals("NORTHEAST")) 
//      	{
//      		action.direction = Direction.NORTHEAST;
//      		return action;
//      	}
//      	else if (path.get(i).equals("NORTHWEST")) 
//      	{
//      		action.direction = Direction.NORTHWEST;
//      		return action;
//      	}
//      	else if (path.get(i).equals("SOUTHEAST")) 
//      	{
//      		action.direction = Direction.SOUTHEAST;
//      		return action;
//      	}
//      	else if (path.get(i).equals("SOUTHWEST")) 
//      	{
//      		action.direction = Direction.SOUTHWEST;
//      		return action;
//      	}
//      }
      return action;
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
          System.out.println("/n/n/n/n I am dropping water!! /n//n/n/");
          return drop;
        }
        // TODO: changes made here
        // Path that the ant needs to travel
        // NOTE: AntAStar still needs to load in the map somehow
        path = new AntAStar(ant.gridX, ant.gridY, 
        		ClientRandomWalk.getCenterX(), ClientRandomWalk.getCenterY()).getPath();
        //return goHome; // astar
      }
    }
    
    // you dont have food..
    // action.direction = Direction.getRandomDir();
    // drink.direction = Direction.SOUTHEAST;
    // drink.type = AntActionType.PICKUP;
    // if(true){return drink;}
    // find food near you
    
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
        
        // Distance to food
        int absfoodX = Math.abs(ant.gridX - foodpiece.gridX);
        int absfoodY = Math.abs(ant.gridY - foodpiece.gridY);
        
        // TODO: left all your stuff in for now, but we might want to replace
        //			 all this stuff with astar
        // So, for example:
        /*
        if (absfoodX < 20 && absfoodY < 20)
        {
        	action.type = AntActionType.MOVE;
        	// have ant travel along astar path
        	path = new AntAStar(ant.gridX, ant.gridY, foodpiece.gridX, foodpiece.gridY).getPath();
        }
        */
        if (absfoodX < 20 && absfoodY < 20)
        {
          action.type = AntActionType.MOVE;
          if (ant.gridX < foodpiece.gridX)
          {
            action.direction = Direction.EAST;
            if (absfoodX == 1)
            {
              AntAction eat = new AntAction(AntActionType.PICKUP, Direction.EAST, 50);
              return eat;
            }
            return action;
          }
          else if (ant.gridX > foodpiece.gridX)
          {
            action.direction = Direction.WEST;
            if (absfoodX == 1)
            {
              AntAction eat = new AntAction(AntActionType.PICKUP, Direction.WEST, 50);
              return eat;
            }
            return action;
          }
          if (ant.gridY < foodpiece.gridY)
          {
            action.direction = Direction.SOUTH;
            if (absfoodY == 1)
            {
              AntAction eat = new AntAction(AntActionType.PICKUP, Direction.SOUTH, 50);
              return eat;
            }
            return action;
          }
          else if (ant.gridY > foodpiece.gridY)
          {
            action.direction = Direction.NORTH;
            if (absfoodY == 1)
            {
              AntAction eat = new AntAction(AntActionType.PICKUP, Direction.NORTH, 50);
              return eat;
            }
            return action;
          }
        }
      }
    }
    
    // no food near you find some!!
    if(data.foodStockPile[FoodType.WATER.ordinal()] < 100)
    {
     
     // if(antworld.client.ClientRandomWalk.map.rgb[ant.gridX+1][ant.gridY] == LandType.WATER)
//      {
//        
//      }
      
      action.type = AntActionType.MOVE;
      action.direction = Direction.EAST;
    }
    action.type = AntActionType.MOVE;
    action.direction = Direction.getRandomDir();
    return action;
  }

  private static boolean underGround(AntData ant)
  {
    if (ant.underground)
    {
      switch (ant.antType)
      {
      case BASIC:
        countBasic++;
        break;
      case ATTACK:
        countAttack++;
        break;
      case VISION:
        countVision++;
        break;
      case MEDIC:
        countMedic++;
        break;
      case CARRY:
        countCarry++;
        break;
      case SPEED:
        countSpeed++;
        break;
      case DEFENCE:
        countDefence++;
        break;
      default:
        break;
      }
      return true;
    }
    return false;
  }

  private AntAction explore()
  {
    action.type = AntActionType.MOVE;
    action.direction = Direction.NORTH;
    return action;
  }

  private AntAction findWater()
  {
    action.type = AntActionType.MOVE;
    action.direction = Direction.EAST;
    return action;
  }

  private AntAction findFood()
  {
    action.type = AntActionType.MOVE;
    action.direction = Direction.NORTH;
    // if(lt.)//found food!!
    //{}
    return action;
  }
}
