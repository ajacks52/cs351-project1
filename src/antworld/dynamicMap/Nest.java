package antworld.dynamicmap;

import java.awt.Color;

import antworld.data.NestNameEnum;
import antworld.data.TeamNameEnum;


public class Nest extends antworld.data.CommData
{

  public Nest(NestNameEnum nestName, TeamNameEnum team)
  {
    super(nestName, team);
    System.out.println(nestName);
  }

  public static void main(String[] args)
  {

  }
  
  
  
}
