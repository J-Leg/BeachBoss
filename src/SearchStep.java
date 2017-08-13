import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class SearchStep extends GameObject{
	private int width = Game.WIDTH/12;
	private int height = Game.WIDTH/12;

	/**
	 * SearchStep initialises with a position and an ID
	 * @param x
	 * @param y
	 * @param id
	 */
	public SearchStep(int x, int y, ID id) {
		super(x, y, id);
	}

	/**
	 * renders the steps as a white empty box
	 */
	@Override
	public void render(Graphics g) {
		//g.setColor(Color.WHITE);
		//g.drawRect(x, y, width, height);
	}

	/**
	 * returns the bounds of the object
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int) y, width, height);
	}
}
