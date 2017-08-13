import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
	private ID ID;
	protected int x, y;
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	/**
	 * Object initialises with x,y coordinates and ID of object
	 * @param x
	 * @param y
	 * @param id
	 */
	public GameObject(int x, int y, ID id){
		this.x = x;
		this.y = y;
		this.setID(id);
	}

	/**
	 * get x position of object
	 * precondition: x value exists
	 * postcondition: int value returned
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * get y position of object
	 * precondition: y value exists
	 * postcondition: int value returned
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * set x value of object
	 * postcondition: x is set to given int
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
		
	/**
	 * set y value of object
	 * postcondition: y is set to given int
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * get ID of object
	 * precondition: ID exists
	 * postcondition: int value returned
	 * @return
	 */
	public ID getID() {
		return ID;
	}

	/**
	 * set ID of object
	 * postcondition: ID is set to given int
	 * @param id
	 */
	public void setID(ID id) {
		ID = id;
	}
	

	/**
	 * Function changes the position of given object with given move
	 * postCondition: object is moved in given direction
	 */
	public void moveObject(Move move){
		if(move == Move.Up){
			this.y += 90;
		}else if(move == Move.Down){
			this.y -= 90;
		}else if(move == Move.Right){
			this.x += 90;
		}else if(move == Move.Left){
			this.x -= 90;
		}
	}
	
}
