package antworld.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class CommData implements Serializable
{
  private static final long serialVersionUID = Constants.VERSION;

  public long wallClockMilliSec; //latency debugging
  public int gameTick; // simtime - if received by serv and past this value, data dropped

  public NestNameEnum myNest;
  public TeamNameEnum myTeam;
  
  //To get the location of your own nest, use: 
  //    nestData[myNestName.ordinal()].centerX;
  //    nestData[myNestName.ordinal()].centerY;

  // Return myAntList ordered where the ant's actions are executed from first
  // element to last.
  // Set Ant's action
  // Add ants you want to birth to myAntList.
  public ArrayList<AntData> myAntList = new ArrayList<AntData>();

  
  
  // To reduce network traffic, set each of these to null when returning
  // CommData to server
  public NestData[] nestData; // set to null before sending to server.
  public int[] foodStockPile; // set to null before sending to server.
  public HashSet<AntData> enemyAntSet;  // set to null before sending to server.
  public HashSet<FoodData> foodSet;  // set to null before sending to server.

  //The server will automatically set requestNestData=true whenever
  //   a new client attaches or whenever a client changes nest homes.
  public boolean requestNestData = false;
  public boolean returnToNestOnDisconnect = true;

  public CommData(NestNameEnum nestName, TeamNameEnum team)
  {
    this.myNest = nestName;
    this.myTeam = team;
  }
  
  public void clear()
  {
    wallClockMilliSec = 0;
    myAntList = null;
    nestData = null;
    foodStockPile = null;
    enemyAntSet =  null;
    foodSet = null;
  }
  

  public String toString()
  {
    String out = "CommData["+gameTick+":"+wallClockMilliSec+"]: "+ myNest + ", " + myTeam +"\n  myAntList:";
    for (AntData ant : myAntList)
    { out = out + "\n     " + ant;
    }
    if (enemyAntSet != null) 
    {  out = out + "\n     enemyAntSet:";
      for (AntData ant : enemyAntSet)
      { out = out + "\n     " + ant;
      }
    }
    return out;
  }
}