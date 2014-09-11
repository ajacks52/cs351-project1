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
	
	// Nodes for the open/closed lists
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
	
	// Nodes for the points in the path
	public Node(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getF()
	{
		return F;
	}
	
	public int getG()
	{
		return G;
	}
	
	public int getH()
	{
		return H;
	}
	
	public int getParentX()
	{
		return px;
	}
	
	public int getParentY()
	{
		return py;
	}
}
