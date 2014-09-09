package antworld.ai;

public class ListNode
{
	private int x;
	private int y;
	private int F;
	private int G; 
	private int H;
	// Parent coordinates
	private int px;
	private int py;
	
	public ListNode(int x, int y, int F, int G, int H, int px, int py)
	{
		this.x = x;
		this.y = y;
		this.F = F;
		this.G = G;
		this.H = H;
		this.px = px;
		this.py = py;
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