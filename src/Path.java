
public class Path{
	//Path used by level to represent generated path to goals from start
	private int x;
	private int y;
	
	/**
	 * Make a path
	 * @param x The x location of the path
	 * @param y The y location of the path
	 */
	public Path (int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get the X location of the path
	 * @return int The X location of the path
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * Get the Y location of the path
	 * @return int The Y location of the path
	 */
	public int getY(){
		return this.y;
	}
}
