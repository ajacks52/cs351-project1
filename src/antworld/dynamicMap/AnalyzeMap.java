package antworld.dynamicmap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import antworld.data.AntData;
import antworld.data.CommData;
import antworld.data.FoodData;
import antworld.data.FoodType;

import javax.imageio.ImageIO;

public class AnalyzeMap
{
  BufferedImage img = null;
  private CommData commD;
  public static int rgb[][] = null;
  public boolean done = false;
  final Color UNKNOWN = Color.decode("0x0000C0");
  final Color WATER = Color.decode("0x0000C8");
  final Color DEFENCE = Color.decode("0xAA00FF");
  final Color ATTACK = Color.decode("0xA300F4");
  final Color SPEED = Color.decode("0x9800E3");
  final Color VISION = Color.decode("0x8E00D5");
  final Color CARRY = Color.decode("0x8900CD");
  final Color MEDIC = Color.decode("0x7C00BA");
  final Color BASIC = Color.decode("0x7000A8");

  public AnalyzeMap()
  {
    try
    {
      img = ImageIO.read(new File("resources/AntWorld.png"));
    }
    catch (IOException e)
    {}
    System.out.println(" STARTED! ");
    findRGB();
    System.out.println(" DONE! ");
    done = true;
  }

  private void findRGB()
  {
    int i = 0;
    rgb = new int[img.getWidth()][img.getHeight()];
    for (int y = 0; y < img.getHeight(); y++)
    {
      for (int x = 0; x < img.getWidth(); x++, i++)
      {
        rgb[x][y] = img.getRGB(x, y); // x, y
        // System.out.printf("%,d, %d\n,", i, rgb[x][y]); // takes very
        // long like 5 minutes when i ask it to print while it's reading
        // in the array
      }
    }
  }

  public static void main(String[] args)
  {
    // AnalyzeMap map = new AnalyzeMap();
  }
}
