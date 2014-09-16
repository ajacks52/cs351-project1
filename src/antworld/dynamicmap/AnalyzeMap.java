package antworld.dynamicmap;

/**
 * 
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AnalyzeMap
{
  BufferedImage img = null;
  public static Color rgb[][] = null;
  public boolean done = false;


  /** Constructor */
  public AnalyzeMap()
  {
    try
    {
      img = ImageIO.read(new File("resources/AntWorld.png"));
    }
    catch (IOException e) {}
    
    System.out.println("STARTED: AnalyzeMap");
    findRGB();
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
  }
  
  /**
   * @return a 2D array representing the colors of the map.
   */
  public Color[][] getRGB()
  {
  	return rgb;
  }

  /**
   * @return the width of the map.
   */
  public int getWidth()
  {
  	return img.getWidth();
  }
  
  /**
   * @return the height of the map.
   */
  public int getHeight()
  {
  	return img.getHeight();
  }
  
//  public static void main(String[] args)
//  {
//     new AnalyzeMap();
//     System.out.println(rgb[1000][2000]);
//  }
}
