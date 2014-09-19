/**
 * @author Joel Castellanos
 * @author Adam Mitchell
 * @author Erin Sosebee
 * 
 */
package antworld.client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Iterator;

// The data classes we will be using
import antworld.data.AntAction;
import antworld.data.AntAction.AntActionType;
import antworld.data.AntData;
import antworld.data.CommData;
import antworld.data.Constants;
import antworld.data.Direction;
import antworld.data.FoodData;
import antworld.data.NestNameEnum;
import antworld.data.TeamNameEnum;
// The classes we made that we'll be using
import antworld.dynamicmap.AnalyzeMap;
import antworld.dynamicmap.Gui;
import antworld.dynamicmap.JTableDisplay;

public class ClientRandomWalk
{
  private static final boolean DEBUG = false;

  // Login info
  private static final TeamNameEnum myTeam = TeamNameEnum.SpikeDropseed;
  private static final long password = 77265157773L;

  // I/O streams
  private ObjectInputStream inputStream = null;
  private ObjectOutputStream outputStream = null;

  // Connecting
  private boolean isConnected = false;
  private Socket clientSocket;

  // Nest info
  private NestNameEnum myNestName = null;
  private static int centerX;
  private static int centerY;

  // Graphics
  Gui myThreadGui;
  JTableDisplay table;
  static AnalyzeMap map;

  // Specific ants
  static String basic = "basic";
  static String attack = "attack";
  static String enemy = "enemy";

  /**
   * 
   * @param host
   * @param portNumber
   */
  public ClientRandomWalk(String host, int portNumber)
  {
    System.out.println("Starting ClientRandomWalk: "
        + System.currentTimeMillis());
    isConnected = false;
    while (!isConnected)
    {
      isConnected = openConnection(host, portNumber);
    }
    /** Create gui, wait till it the map loads */
    myThreadGui = new antworld.dynamicmap.Gui();
    map = new AnalyzeMap();
    while (map.done == false)
    {
      try
      {
        Thread.sleep(200);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    // Make the table, sleep the main thread while it initializes
    myThreadGui.start();
    table = new JTableDisplay();
    try
    {
      Thread.sleep(500);
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }

    // Chose nest and get game started
    CommData data = chooseNest();
    data.returnToNestOnDisconnect = true;
    mainGameLoop(data);
    closeAll();
  }

  private boolean openConnection(String host, int portNumber)
  {
    try
    {
      clientSocket = new Socket(host, portNumber);
    } catch (UnknownHostException e)
    {
      System.err.println("ClientRandomWalk Error: Unknown Host " + host);
      e.printStackTrace();
      return false;
    } catch (IOException e)
    {
      System.err
          .println("ClientRandomWalk Error: Could not open connection to "
              + host + " on port " + portNumber);
      e.printStackTrace();
      return false;
    }
    try
    {
      outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
      inputStream = new ObjectInputStream(clientSocket.getInputStream());
    } catch (IOException e)
    {
      System.err.println("ClientRandomWalk Error: Could not open i/o streams");
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public void closeAll()
  {
    System.out.println("ClientRandomWalk.closeAll()");
    {
      try
      {
        if (outputStream != null)
          outputStream.close();
        if (inputStream != null)
          inputStream.close();
        clientSocket.close();
      } catch (IOException e)
      {
        System.err.println("ClientRandomWalk Error: Could not close");
        e.printStackTrace();
      }
    }
  }

  public CommData chooseNest()
  {
    while (myNestName == null)
    {
      try
      {
        Thread.sleep(100);
      } catch (InterruptedException e1)
      {
      }
      NestNameEnum requestedNest = NestNameEnum.GUEST;
      CommData data = new CommData(requestedNest, myTeam);
      data.password = password;
      if (sendCommData(data))
      {
        try
        {
          if (DEBUG)
            System.out.println("ClientRandomWalk: listening to socket....");
          CommData recvData = (CommData) inputStream.readObject();
          if (DEBUG)
          {
            System.out.println("ClientRandomWalk: recived <<<<<<<<<"
                + inputStream.available() + "<...\n" + recvData);
          }
          if (recvData.errorMsg != null)
          {
            System.err.println("ClientRandomWalk***ERROR***: "
                + recvData.errorMsg);
            continue;
          }
          if ((myNestName == null) && (recvData.myTeam == myTeam))
          {
            myNestName = recvData.myNest;
            setCenterX(recvData.nestData[myNestName.ordinal()].centerX);
            setCenterY(recvData.nestData[myNestName.ordinal()].centerY);
            System.out
                .println("ClientRandomWalk: !!!!!Nest Request Accepted!!!! "
                    + myNestName);
            return recvData;
          }
        } catch (IOException e)
        {
          System.err.println("ClientRandomWalk***ERROR***: client read failed");
          e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
          System.err
              .println("ClientRandomWalk***ERROR***: client sent incorect data format");
        }
      }
    }
    return null;
  }

  public void mainGameLoop(CommData data)
  {
    while (true)
    {
      try
      {
        if (DEBUG)
        {
          System.out.println("ClientRandomWalk: chooseActions: " + myNestName);
        }
        chooseActionsOfAllAnts(data);
        CommData sendData = data.packageForSendToServer();
        System.out.println("ClientRandomWalk: Sending>>>>>>>: " + sendData);

        outputStream.writeObject(sendData);
        outputStream.flush();
        outputStream.reset();
        if (DEBUG)
        {
          System.out.println("ClientRandomWalk: listening to socket....");
        }

        CommData recivedData = (CommData) inputStream.readObject();
        if (DEBUG)
        {
          System.out.println("ClientRandomWalk: received <<<<<<<<<"
              + inputStream.available() + "<...\n" + recivedData);
        }
        data = recivedData;
        if ((myNestName == null) || (data.myTeam != myTeam))
        {
          System.err.println("ClientRandomWalk: !!!!ERROR!!!! " + myNestName);
        }
      } catch (IOException e)
      {
        System.err.println("ClientRandomWalk***ERROR***: client read failed");
        e.printStackTrace();
        try
        {
          Thread.sleep(1000);
        } catch (InterruptedException e1)
        {
        }
      } catch (ClassNotFoundException e)
      {
        System.err
            .println("ServerToClientConnection***ERROR***: client sent incorect data format");
        e.printStackTrace();
        try
        {
          Thread.sleep(1000);
        } catch (InterruptedException e1)
        {
        }
      }
    }
  }

  /**
   * 
   * @param data
   * @return
   */
  private boolean sendCommData(CommData data)
  {
    CommData sendData = data.packageForSendToServer();
    try
    {
      if (DEBUG)
        System.out.println("ClientRandomWalk.sendCommData(" + sendData + ")");
      outputStream.writeObject(sendData);
      outputStream.flush();
      outputStream.reset();
    } catch (IOException e)
    {
      System.err.println("ClientRandomWalk***ERROR***: client read failed");
      e.printStackTrace();
      try
      {
        Thread.sleep(1000);
      } catch (InterruptedException e1)
      {
      }
      return false;
    }
    return true;
  }

  private void chooseActionsOfAllAnts(CommData commData)
  {

    for (int i = 0; i < 10; i++)
    {
      AntData ant = commData.myAntList.get(i);

      AntAction action = AntLogicBasic.chooseAction(commData, ant, i);
      ant.myAction = action;

      // my ants are being drawn they will be red
      Gui.drawAnts(ant.gridX, ant.gridY, commData, basic);
      JTableDisplay.updateTableAnts(commData);
    }

    for (int i = 10; i < 20; i++)
    {
      AntData ant = commData.myAntList.get(i);

      AntAction action = AntLogicAttack.chooseAction(commData, ant, i);
      ant.myAction = action;

      // my ants are being drawn they will be red
      Gui.drawAnts(ant.gridX, ant.gridY, commData, attack);
      JTableDisplay.updateTableAnts(commData);
    }

    // Give the 5 ants still in the nest an action
    for (int i = commData.myAntList.size() - 5; i < commData.myAntList.size(); i++)
    {
      for (Iterator<AntData> j = commData.enemyAntSet.iterator(); j.hasNext();)
      {
        AntData element = j.next();

        if (AnalyzeMap.rgb[element.gridY][element.gridX].equals(new Color(
            0xF0E68C)))
        {
          AntAction attack = new AntAction(AntActionType.ATTACK);
          AntData ant = commData.myAntList.get(i);
          attack.direction = Direction.getRandomDir();
          ant.myAction = attack;
          if (DEBUG)
            System.out.println("I'm attacking an ant on our nest!!");
        }
      }
    }

    for (FoodData food : commData.foodSet)
    {
      Gui.drawFood(food.gridX, food.gridY);
      JTableDisplay.updateTableFood(commData);
    }
    for (AntData enemyAnts : commData.enemyAntSet)
    {
      Gui.drawAnts(enemyAnts.gridX, enemyAnts.gridY, commData, enemy);
      JTableDisplay.updateTableEnemy(commData);
    }
  }

  public static void main(String[] args)
  {
    // Test servers...
    // b146-75
    // b146-73
    // b146-76
    // DEIMOS
    // deimos.cs.unm.edu
    // b146-28
    // b146-71, b146-74 and b146-76. b146-26
    // our sever is b146-72
    String serverHost = "b146-72.cs.unm.edu";
    new ClientRandomWalk(serverHost, Constants.PORT);
  }

  /**
   * Returns a 2D array representing the colors at each pixel of the map.
   * 
   * @return 2D Color array
   */
  public static Color[][] getMap()
  {
    return map.getRGB();
  }

  public static int getCenterX()
  {
    return centerX;
  }

  public void setCenterX(int centerX)
  {
    this.centerX = centerX;
  }

  public static int getCenterY()
  {
    return centerY;
  }

  public void setCenterY(int centerY)
  {
    this.centerY = centerY;
  }
}
