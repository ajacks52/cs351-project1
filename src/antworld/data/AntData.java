package antworld.data;

import java.io.Serializable;

import antworld.data.AntAction.AntActionType;

public class AntData implements Comparable<AntData>, Serializable
{
  private static final long serialVersionUID = Constants.VERSION;

  public final NestNameEnum nestName;
  public final TeamNameEnum teamName;

  public int id = Constants.UNKNOWN_ANT_ID; //Use whenever you birth an ant

  public int gridX, gridY;
  public boolean alive = true;

  public AntType antType;
  public FoodType carryType = null;
  public int carryUnits = 0;

  public AntAction myAction;

  public int ticksUntilNextAction = 0;

  public int health;

  public boolean underground = true;

  public AntData(int id, AntType type, NestNameEnum nestName, TeamNameEnum teamName)
  {
    this.id = id;
    antType = type;
    this.nestName = nestName;
    this.teamName = teamName;
    health = type.getMaxHealth();
    myAction = new AntAction(AntActionType.BIRTH);
  }

  public String toString()
  {
    String out = "AntData: [id=" + id + ", nest=" + nestName + ", team=" + teamName + ", " + antType + ", health="
        + health + ", " + myAction;
    if (carryUnits > 0) out += ", carry: [" + carryType + ", " + carryUnits + "]";
    if (underground) out += ", underground ]";
    else out += ", x=" + gridX + ", y=" + gridY + "]";

    return out;
  }

  @Override
  public int compareTo(AntData otherAnt)
  {
    return id - otherAnt.id;
  }

}
