package antworld.data;

import java.io.Serializable;

public class AntAction implements Serializable
{
  private static final long serialVersionUID = Constants.VERSION;
  public enum AntActionType 
  { MOVE,       // MOVE direction
    ATTACK,     // ATTACK direction
    PICKUP,     // PICKUP direction quantity
    DROP,       // DROP direction quantity
    HEAL,       // HEAL direction (must be medic ant) | HEAL (must be underground)
    ENTER_NEST, // ENTER_NEST (must be on home nest area)
    EXIT_NEST,  // EXIT_NEST x y (must be underground and x,y must be in home nest area)
    BIRTH,      // Client adds new ant to antlist, sets ant type. Server deducts needed food from nest store.
    DIED,       // 
    STASIS      // STASIS
  }; 
  
  public AntActionType type;
  public Direction direction;
  public int x, y, quantity;
  
  public AntAction(AntActionType type)
  {
    this.type = type;
  }
  
  
  public AntAction(AntActionType type, Direction dir)
  {
    this.type = type;
    this.direction = dir;
  }
  
  
  
  public AntAction(AntActionType type, Direction dir, int quantity)
  {
    this.type = type;
    this.direction = dir;
    this.quantity = quantity;
  }
  
  
  
  public AntAction(AntActionType type, int x, int y)
  {
    this.type = type;
    this.x = x;
    this.y = y;
  }
  
  public void copyFrom(AntAction source)
  {
    type = source.type;
    direction = source.direction;
    x = source.x;
    y = source.y;
    quantity = source.quantity;
  }
  
  public String toString()
  {
    String out = "AntAction: ["+type+", ";
    if (type == AntActionType.MOVE) out += direction +"]";
    else if (type == AntActionType.ATTACK) out += direction +"]";
    else if (type == AntActionType.PICKUP) out += direction +" quentity="+quantity+"]";
    else if (type == AntActionType.DROP) out += direction +" quentity="+quantity+"]";
    else if (type == AntActionType.HEAL) out += direction +"]";
    else if (type == AntActionType.ENTER_NEST) out += "]";
    else if (type == AntActionType.EXIT_NEST) out += "("+x + ", " + y + ")]";
    else if (type == AntActionType.STASIS) out += "]";
    
    return out;
  }
}
