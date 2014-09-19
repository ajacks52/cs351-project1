/**
 * @author Adam Mitchell
 * 
 * The Gui class creates a GUI.
 */
package antworld.dynamicmap;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import antworld.data.CommData;

public class Gui extends Thread
{
  private static final boolean DEBUG = false;
  private static TransformingCanvas canvas;
  static BufferedImage bImage;
  static Label stat;
  static Label stat2;
  static JButton button1;

  static int rgb; // use in the form "rgb = WATER.getRGB();"

  // the array of colors to redraw the map in all of it's beauty
  private static LinkedList<DrawnAnt> oldColors = new LinkedList<DrawnAnt>();
  static Timer timer;

  /**
   * Initializes the JFrame window.
   */
  private static void init()
  {
    try
    {
      bImage = ImageIO.read(new File("resources/AntWorld.png"));
    } catch (IOException e)
    {
      e.printStackTrace();
    }

    JFrame frame = new JFrame();
    frame.setTitle("AntWorld!");
    canvas = new TransformingCanvas();
    TranslateHandler translater = new TranslateHandler();
    canvas.addMouseListener(translater);
    canvas.addMouseMotionListener(translater);
    canvas.addMouseWheelListener(new ScaleHandler());
    frame.setLayout(new BorderLayout());
    frame.getContentPane().add(canvas, BorderLayout.CENTER);
    frame.add(BorderLayout.SOUTH, stat2 = new Label());
    frame.add(BorderLayout.NORTH, stat = new Label());
    frame.add(BorderLayout.EAST, button1 = new JButton());

    frame.setSize(bImage.getWidth() / 4, bImage.getHeight() / 4);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);

    timer = new Timer();
    timer.schedule(new Times(), 3000, 1 * 1000);

  }

  /**
   * @author Adam Mitchell
   *
   * The TransformingCanvas class handles all the zooming effects and 
   * scrolling.
   */
  @SuppressWarnings("serial")
  private static class TransformingCanvas extends JComponent
  {
    private double translateX;
    private double translateY;
    private double scale;

    TransformingCanvas()
    {
      translateX = 0;
      translateY = 0;
      scale = 1;
      setOpaque(true);
      setDoubleBuffered(true);
    }

    /**
     * Paints on the canvas.
     */
    @Override
    public void paint(Graphics g)
    {
      Dimension dims = getSize();
      AffineTransform tx = new AffineTransform();
      tx.translate(translateX, translateY);
      // start the map zoomed in
      tx.scale(scale + 2, scale + 2);

      Graphics2D graph = (Graphics2D) g;
      graph.setColor(Color.BLACK);
      graph.fillRect(0, 0, getWidth(), getHeight());
      graph.setTransform(tx);
      graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      // start at our map location
      graph.drawImage(bImage, -800, -150, dims.width, dims.height, this);
    }
  }

  /**
   * @author Adam Mitchell
   *
   * The TranslateHandler class handles all the mouse listening events.
   */
  private static class TranslateHandler implements MouseListener,
      MouseMotionListener
  {
    private int lastOffsetX;
    private int lastOffsetY;

    public void mousePressed(MouseEvent e)
    {
      // capture starting point
      lastOffsetX = e.getX();
      lastOffsetY = e.getY();
    }

    public void mouseDragged(MouseEvent e)
    {

      int newX = e.getX() - lastOffsetX;
      int newY = e.getY() - lastOffsetY;
      lastOffsetX += newX;
      lastOffsetY += newY;
      canvas.translateX += newX;
      canvas.translateY += newY;
      canvas.repaint();
    }

    public void mouseClicked(MouseEvent e)
    {
      stat.setText("Point is: " + e.getX() + " " + e.getY());
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseMoved(MouseEvent e)
    {
      stat.setText("Mouse to " + e.getPoint());
    }

    public void mouseReleased(MouseEvent e)
    {
    }
  }

  private static class ScaleHandler implements MouseWheelListener
  {
    public void mouseWheelMoved(MouseWheelEvent e)
    {
      if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
      {
        canvas.scale -= (.3 * e.getWheelRotation());
        canvas.scale = Math.max(0.0001, canvas.scale);
        canvas.repaint();
      }
    }
  }

  @Override
  public void run()
  {
    System.out.println("CONSTRUCTING THE MAP NOW");
    init();
  }

  /**
   * Erases pixels drawn as ant moves.
   */
  static void erasePixels()
  {
    int i = 0;
    if (oldColors.size() > 100)
      while (i < 98)
      {
        i++;
        DrawnAnt e = oldColors.remove(0);
        bImage.setRGB(e.x, e.y, e.c);
        canvas.repaint();
      }
  }

  /**
   * Draws ants on the board.
   * 
   * @param x
   * @param y
   * @param data
   * @param mine
   */
  public static void drawAnts(int x, int y, CommData data, String mine)
  {
    int totalScore = 0;

    if (mine.equalsIgnoreCase("basic"))
    {
      rgb = new Color(0xFFA07A).getRGB();
    } else if (mine.equalsIgnoreCase("enemy"))
    {
      rgb = new Color(0xADFF2F).getRGB();
    } else if (mine.equalsIgnoreCase("attack"))
    {
      rgb = new Color(0xB22222).getRGB();
    }

    for (int i = 0; i < data.foodStockPile.length; i++)
    {
      if (i != 1)
      {
        totalScore += data.foodStockPile[i];
      }
    }

    stat2.setText("Our score is: " + totalScore + ";\t We number of ants: "
        + data.myAntList.size() + "; Our water: " + data.foodStockPile[1]);

    Color color = AnalyzeMap.rgb[y][x];

    bImage.setRGB(x, y, rgb);
    DrawnAnt currentAnt = new DrawnAnt(x, y, color.getRGB());
    oldColors.add(currentAnt);
    canvas.repaint();
  }

  /**
   * Draws food on the board.
   * 
   * @param x
   * @param y
   */
  public static void drawFood(int x, int y)
  {
    rgb = Color.BLUE.getRGB();
    bImage.setRGB(x, y, rgb);
    canvas.repaint();
  }
}

/**
 * @author Adam Mitchell
 * 
 * The DrawnAnt class stores the ant to be drawn.
 */
class DrawnAnt
{
  int x, y, c;

  DrawnAnt(int x, int y, int color)
  {
    this.x = x;
    this.y = y;
    this.c = color;
  }
}

/**
 * @author Adam Mitchell
 * 
 * The TimerTask class provides a timer for the erasing pixels method.
 *
 */
class Times extends TimerTask
{
  public void run()
  {
    System.out.println("\n\n\n\n\n\nerasing pixels!!\n\n\n\n\n\n\n");
    Gui.erasePixels();
  }
}
