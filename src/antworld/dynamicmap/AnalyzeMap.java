/**
 * @author Adam Mitchell
 * @author Erin Sosebee
 * 
 * The AnalyzeMap class creates a 2D array of colors found at each pixel
 * of the map.
 */
package antworld.dynamicmap;

/**
 * Class to store all the values of the map in 2-d array.
 * Gets called in ClientRandomWalk 
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AnalyzeMap
{
  private static final boolean DEBUG = false;
  BufferedImage img = null;
  public static Color rgb[][] = null;
  public boolean done = false;

  /** Constructor */
  public AnalyzeMap()
  {
    try
    {
      img = ImageIO.read(new File("resources/AntWorld.png"));
    } catch (IOException e)
    {
    }

    if (DEBUG)
      System.out.println("STARTED: AnalyzeMap");
    findRGB();
    if (DEBUG)
      System.out.println("FINISHED: AnalyzeMap");
    done = true;
  }

  /**
   * Finds the color from each pixel and stores it into a 2D Color array.
   */
  private void findRGB()
  {
    rgb = new Color[img.getHeight()][img.getWidth()];
    for (int y = 0; y < img.getHeight(); y++)
    {
      for (int x = 0; x < img.getWidth(); x++)
      {
        int rgbVal = img.getRGB(x, y);
        Color color = new Color(rgbVal);
        rgb[y][x] = color;
      }
    }
    if (DEBUG)
    {
      System.out.println("This pixel's value is: " + rgb[1000][1000]
          + " Should be: r=88,g=121,b=78");
    }
  }

  /**
   * Gets the RGB map.
   * @return a 2D array representing the colors of the map.
   */
  public Color[][] getRGB()
  {
    return rgb;
  }

  /**
   * Gets the width of the map.
   * @return the width of the map.
   */
  public int getWidth()
  {
    return img.getWidth();
  }

  /**
   * Gets the height of the map.
   * @return the height of the map.
   */
  public int getHeight()
  {
    return img.getHeight();
  }

  // Unit Testing
  // public static void main(String[] args)
  // {
  // new AnalyzeMap();
  // System.out.println(rgb[1000][2000]);
  // }

}
