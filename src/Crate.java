import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Crate extends GameObject {
	private ObjectHandler handler;
	private int width = Game.WIDTH/12;
	private int height = Game.WIDTH/12;
	
	/**
	 * 
	 * @param x position of crate
	 * @param y position of crate
	 * @param id given ID of object
	 * @param handler : Game handler
	 */
	public Crate(int x, int y, ID id, ObjectHandler handler) {
		super(x, y, id);
		this.handler = handler;
	}
	
	/**
	 * returns the boundary of the object
	 * precondition: object has a width and height value
	 * postcondition: rectangle of set values returned
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	/**
	 * renders the object on the window
	 * precondition: given files exist in folder systems for images
	 */
	@Override
	public void render(Graphics g) {
		BufferedImage crab = null;
		try {
			crab = ImageIO.read(new File("Images/pinkCrabSmall.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g.drawImage(crab,(int)x, (int)y, null);
	}
	
	/**
	 * returns whether a move causes a collision with another object 
	 * @param move
	 * @return
	 * postcondition: returns true or false accordingly
	 */
	public boolean collision(Move move){
		
		for(int i = 0; i < handler.getList().size(); i++){
			GameObject tempObject = handler.getList().get(i);
			
			if(tempObject.equals(this))
				continue;
			
			if(tempObject instanceof SearchStep)
				continue;
					
			if(tempObject.getID() == ID.Wall || tempObject.getID() == ID.Crate 
					|| tempObject.getID() == ID.Player){
				if(getBounds().intersects(tempObject.getBounds())){
					return true;
				}	
			}
		}
		return false;
		
	}
	
	/**
	 * Moves the crate in the given direction
	 * Moves it back if the move collides with anything
	 * @param move
	 * postCondition: object is either moved, or is moved back if collision
	 *  indicates so
	 */
	public void moveCrate(Move move){
		super.moveObject(move);
		
		if(collision(move)){
			if(move == Move.Up){
				super.moveObject(Move.Down);
			}else if(move == Move.Right){
				super.moveObject(Move.Left);
			}else if(move == Move.Left){
				super.moveObject(Move.Right);
			}else if(move == Move.Down){
				super.moveObject(Move.Up);
			}
		}
		
	}
}
