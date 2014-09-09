package antworld.client;

import java.awt.Color;
import antworld.data.AntAction;
import antworld.data.AntData;
import antworld.data.AntType;
import antworld.data.CommData;
import antworld.data.Constants;
import antworld.data.Direction;
import antworld.data.AntAction.AntActionType;
import antworld.data.FoodData;
import antworld.data.LandType;

public class AntLogic
{
	static CommData data;
	static AntData ant;
	static AntAction action = new AntAction(AntActionType.STASIS);
	static int countBasic = 0;
	static int countMedic = 0;
	static int countVision = 0;
	static int countCarry = 0;
	static int countSpeed = 0;
	static int countDefence = 0;
	static int countAttack = 0;

	public static AntAction chooseAction(CommData data, AntData ant)
	{
		//data = d;
		//ant  = a;
	    AntAction action = new AntAction(AntActionType.STASIS);
 	    //AntAction eat_drop =  public AntAction(, Direction, 50);
	    //AntAction action = new AntAction(AntActionType.STASIS);

		
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
		if (ant.health < ant.antType.getMaxHealth()/2)
		{
			action.type = AntActionType.MOVE;
		    action.direction = Direction.getRandomDir(); //location of base told by astar!!!
		    return action;
		}
		
    
    

		
		for (FoodData foodpiece : data.foodSet) {
			int absX = Math.abs(ant.gridX - foodpiece.gridX);
			int absY = Math.abs(ant.gridY - foodpiece.gridY);
		    if( absX < 20 && absY < 20)
		    {
		    	action.type = AntActionType.MOVE;
		    	if(ant.gridX < foodpiece.gridX)
		    	{	    		
		    		action.direction = Direction.EAST;
		    		if(absX == 1)
		    		{
		    			AntAction eat = new AntAction(AntActionType.PICKUP, Direction.EAST, 50);
		    			return eat;
		    		}
		    		return action;
		    	}
		    	if(ant.gridY < foodpiece.gridY)
		    	{
		    		action.direction = Direction.SOUTH;
		    		if(absY == 1)
            {
              AntAction eat = new AntAction(AntActionType.PICKUP, Direction.SOUTH, 50);
              return eat;
            }
		    		return action;
		    	}
		    	
		    	if(ant.gridX > foodpiece.gridX)
          {         
            action.direction = Direction.WEST;
            if(absX == 1)
            {
              AntAction eat = new AntAction(AntActionType.PICKUP, Direction.WEST, 50);
              return eat;
            }
            return action;
          }
          if(ant.gridY > foodpiece.gridY)
          {
            action.direction = Direction.NORTH;
            if(absY == 1)
            {
              AntAction eat = new AntAction(AntActionType.PICKUP, Direction.NORTH, 50);
              return eat;
            }
            return action;
          }		    	
		    }
		    System.out.println("no food");
        
      
		}
		action.type = AntActionType.MOVE;
    action.direction = Direction.SOUTHEAST;
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
		//if(lt.)//found food!!
		{
		
		}
		return action;
	}

}
