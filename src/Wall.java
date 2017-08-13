
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Wall extends GameObject{

	private ObjectHandler handler;
	private int width = Game.WIDTH/12;
	private int height = Game.WIDTH/12;
	
	/**
	 * Sets the (x, y) position coordinates and the ID of the object
	 * @param x The x location of the wall, in pixels
	 * @param y The y location of the wall, in pixels
	 * @param id The ID number for the object
	 */
	public Wall(int x, int y, ID id, ObjectHandler handler) {
		super(x, y, id);
		this.handler = handler;
	}
	
	/**
	 * Returns a rectangle with the boundaries of the object
	 * Precondition: Object has a width and a height
	 * @return A rectangle with boundary values is returned
	 */
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	/**
	 * Renders the wall onto the display buffer
	 * Precondition: The image file is readable
	 * @param g The graphics object to render onto
	 */
	public void render(Graphics g) {
		BufferedImage shell = null;
		try {
			shell = ImageIO.read(new File("Images/palmtree.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g.drawImage(shell,(int)x, (int)y, null);
	}

}
