package antworld.dynamicmap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Gui extends Component implements MouseListener, MouseWheelListener, MouseMotionListener
{

  static Label stat;
  BufferedImage bImage;
  int curX = 0, curY = 0;
   
  
  // Colors to be used to tell which ant are around and which food types 
  final Color UNKNOWN  = Color.decode("0x0000C0");
  final Color WATER    = Color.decode("0x0000C8");
  final Color DEFENCE  = Color.decode("0xAA00FF");
  final Color ATTACK   = Color.decode("0xA300F4");
  final Color SPEED    = Color.decode("0x9800E3");
  final Color VISION   = Color.decode("0x8E00D5");
  final Color CARRY    = Color.decode("0x8900CD");
  final Color MEDIC    = Color.decode("0x7C00BA");
  final Color BASIC    = Color.decode("0x7000A8");
  
  int rgb;   // use in the form  "rgb = WATER.getRGB();"

  public static void main(String[] av)
  {
    JFrame jFrame = new JFrame("Map");
    Container cPane = jFrame.getContentPane();
    BufferedImage img = null;
    try
    {
      img = ImageIO.read(new File("resources/AntWorld.png"));
    }
    catch (IOException e)
    {}
    Gui sk = new Gui(img);
    cPane.setPreferredSize(new Dimension(img.getWidth()/4 , img.getHeight()/4 )); //4th the maps real size
    cPane.setLayout(new BorderLayout());
    cPane.add(BorderLayout.NORTH, new Label(""));
    cPane.add(BorderLayout.CENTER, sk);
    cPane.add(BorderLayout.SOUTH, stat = new Label());
    stat.setSize(jFrame.getSize().width, stat.getSize().height);
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.pack();
    jFrame.setVisible(true);
  }

  public Gui(BufferedImage i)
  {
    super();
    bImage = i;
    setSize(500, 500);
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  public void showStatus(String s)
  {
    stat.setText(s);
  }
  
  @Override
  public void paint(Graphics graphic)
  {
    Dimension dims = getSize();

    // in here we need to specify where to paint the map based on where the other ants and food types are and etc.
     //bImage.setRGB(curX, curY, rgb);//ints: x, y, rgb
     
    graphic.drawImage(bImage, 0, 0, dims.width, dims.height, this);
  }

  @Override
  public void mouseClicked(MouseEvent event)
  {}

  @Override
  public void mouseEntered(MouseEvent event)
  {}

  @Override
  public void mouseExited(MouseEvent event)
  {}

  @Override
  public void mousePressed(MouseEvent event)
  {}

  @Override
  public void mouseReleased(MouseEvent event)
  {}

  @Override
  public void mouseDragged(MouseEvent event)
  {}

  @Override
  public void mouseMoved(MouseEvent e)
  {
    showStatus("Mouse to " + e.getPoint());
    Point p = e.getPoint();
    System.out.println("x, y " + curX + ", " + curY);
    curX = p.x;
    curY = p.y;
    repaint();
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent arg0)
  {}
}