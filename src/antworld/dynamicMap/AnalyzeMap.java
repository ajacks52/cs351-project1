package antworld.dynamicmap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AnalyzeMap
{
  BufferedImage img = null;
  int rgb[][] = null;


  AnalyzeMap()
  {
    try
    {
      img = ImageIO.read(new File("resources/AntWorld.png"));
    }
    catch (IOException e)
    {}
    findRGB();
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
        //System.out.printf("%,d, %d\n,", i,  rgb[x][y]);   // takes very long like 5 minutes when i ask it to print while it's reading in the array
      }
    }
    System.out.println("done");
  }

  public static void main(String[] args)
  {
    AnalyzeMap map = new AnalyzeMap();
  }
}
