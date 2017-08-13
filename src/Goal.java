import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Goal extends GameObject{
	private ObjectHandler handler;
	private int width = Game.WIDTH/12;
	private int height = Game.WIDTH/12;

	/**
	 * goal initiates with GameObject fields (position and id), as well as handler
	 * @param x
	 * @param y
	 * @param id
	 * @param handler
	 */
	public Goal(int x, int y, ID id, ObjectHandler handler) {
		super(x, y, id);
		this.handler = handler;
	}
	
	/**
	 * return a rectangle of the bounds of the object
	 * postcondition: returns rectangle of given values
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	/**
	 * Renders goal with given image
	 * precondition: image files exist in folder system
	 * postcondition: shell image is displayed
	 */
	@Override
	public void render(Graphics g) {
		BufferedImage shell = null;
		try {
			shell = ImageIO.read(new File("Images/shell.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g.drawImage(shell,(int)x, (int)y, null);
	}
		
	/**
	 * Checks if the goal has been fulfilled on this current goal object
	 * postcondition: returns true or false accordingly
	 * @return
	 */
	public boolean getGoalState(){
		
		for(int i = 0; i < handler.getList().size(); i++){
			GameObject tempObject = handler.getList().get(i);
			
			if(tempObject instanceof SearchStep)
				continue;
			
			if(tempObject.getID() == ID.Crate){
				Crate c = (Crate) tempObject;
				if(getBounds().intersects(c.getBounds())){					
					return true;
				}else{
					continue;
				}
			}
		}
		return false;
	}
}
