package antworld.dynamicmap;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.util.Timer;
import java.util.TimerTask;

public class Gui extends Thread
{
  private static TransformingCanvas canvas;
  static BufferedImage bImage;
  static BufferedImage bImageEraser;
  static Label stat;
  static int cnt;
  // Colors to be used to tell which ant are around and which food types
  final static Color UNKNOWN = Color.decode("0x0000C0");
  final Color WATER = Color.decode("0x0000C8");
  final static Color DEFENCE = Color.decode("0xAA00FF");
  final static Color ATTACK = Color.decode("0xA300F4");
  final static Color SPEED = Color.decode("0x9800E3");
  final static Color VISION = Color.decode("0x8E00D5");
  final static Color CARRY = Color.decode("0x8900CD");
  final static Color MEDIC = Color.decode("0x7C00BA");
  final static Color BASIC = Color.decode("0x7000A8");
  static int rgb; // use in the form "rgb = WATER.getRGB();"
  private static BufferedImage bImageErase;


  private static void init()
  {
    try
    {
      bImage = ImageIO.read(new File("resources/AntWorld.png"));
      bImageEraser = ImageIO.read(new File("resources/AntWorld.png"));
    } catch (IOException e)
    {
      e.printStackTrace();
    }

    JFrame frame = new JFrame();
    canvas = new TransformingCanvas();
    TranslateHandler translater = new TranslateHandler();
    canvas.addMouseListener(translater);
    canvas.addMouseMotionListener(translater);
    canvas.addMouseWheelListener(new ScaleHandler());
    frame.setLayout(new BorderLayout());
    frame.getContentPane().add(canvas, BorderLayout.CENTER);
    frame.add(BorderLayout.NORTH, new Label(""));
    frame.add(BorderLayout.SOUTH, stat = new Label());
    frame.setSize(bImage.getWidth() / 4, bImage.getHeight() / 4);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
    Timer timer = new Timer("Printer");
    MyTask t = new MyTask();
    timer.schedule(t, 0, 2000);
    
  }

  public static void updateImage()
  {
  }

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

    @Override
    public void paint(Graphics g)
    {
      Dimension dims = getSize();
      AffineTransform tx = new AffineTransform();
      tx.translate(translateX, translateY);
      tx.scale(scale, scale);
      Graphics2D ourGraphics = (Graphics2D) g;
      ourGraphics.setColor(Color.BLACK);
      ourGraphics.fillRect(0, 0, getWidth(), getHeight());
      ourGraphics.setTransform(tx);
      ourGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      ourGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      ourGraphics.drawImage(bImage, 0, 0, dims.width, dims.height, this);
      // super.paint(g);
    }
  }

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
      // new x and y are defined by current mouse location subtracted
      // by previously processed mouse location
      int newX = e.getX() - lastOffsetX;
      int newY = e.getY() - lastOffsetY;
      // increment last offset to last processed by drag event.
      lastOffsetX += newX;
      lastOffsetY += newY;
      // update the canvas locations
      canvas.translateX += newX;
      canvas.translateY += newY;
      // schedule a repaint.
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
        // make it a reasonable amount of zoom
        // .1 gives a nice slow transition
        canvas.scale -= (.2 * e.getWheelRotation());
        // don't cross negative threshold.
        // also, setting scale to 0 has bad effects
        canvas.scale = Math.max(0.00001, canvas.scale);
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

  static void erasePixels()
  {
    //bImage = bImageErase;
    canvas.repaint();
  }

  public static void drawAnts(int x, int y)
  {
    rgb = Color.ORANGE.getRGB();
    bImage.setRGB(x, y, rgb);
    
  //  canvas.repaint();
  }

  public static void drawFood(int x, int y)
  {
    rgb = Color.BLUE.getRGB();
    bImage.setRGB(x, y, rgb);
    canvas.repaint();

  }
}


class MyTask extends TimerTask {
  private int times = 0;

  public void run() {
      times++;
      if (times <= 5) {
          Gui.erasePixels();
      } else {

          //Stop Timer.
          this.cancel();
      }
  }
}
