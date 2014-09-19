/**
 * @author Erin Sosebee
 * 
 * The Node class creates Nodes for path values and heuristics to be stored.
 */
package antworld.ai;

public class Node
{
  private int x;
  private int y;
  private int F;
  private int G;
  private int H;

  // Parent coordinates
  private int px;
  private int py;

  /**
   * Constructor for the open/closed lists.
   * @param x
   * @param y
   * @param F
   * @param G
   * @param H
   * @param px
   * @param py
   */
  public Node(int x, int y, int F, int G, int H, int px, int py)
  {
    this.x = x;
    this.y = y;
    this.F = F;
    this.G = G;
    this.H = H;
    this.px = px;
    this.py = py;
  }

  /**
   * Nodes for the points in the path.
   * @param x
   * @param y
   */
  public Node(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * @return x
   */
  public int getX()
  {
    return x;
  }

  /**
   * @return y
   */
  public int getY()
  {
    return y;
  }

  /**
   * @return F
   */
  public int getF()
  {
    return F;
  }

  /**
   * @return G
   */
  public int getG()
  {
    return G;
  }

  /**
   * @return H
   */
  public int getH()
  {
    return H;
  }

  /**
   * @return px
   */
  public int getParentX()
  {
    return px;
  }

  /**
   * @return py
   */
  public int getParentY()
  {
    return py;
  }
}
