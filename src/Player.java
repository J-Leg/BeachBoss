import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.imageio.ImageIO;

public class Player extends GameObject{
	private ObjectHandler handler;
	private int width = Game.WIDTH/12;
	private int height = Game.WIDTH/12;
	private String imgPath;
	private int playerID;
	
	/**
	 * Creates a player with an initial start position
	 * @param x The start x position
	 * @param y The start y position
	 * @param id The ID of this object
	 * @param handler The handler that organises all the game objects
	 */
	public Player(int x, int y, ID id, ObjectHandler handler, int playerID){
		super(x,y,id);
		this.handler = handler;
		this.playerID = playerID;
		
		if(playerID == 1){
			this.imgPath = "Images/Player1R.png";
		}else if(playerID == 2){
			this.imgPath ="Images/Player2R.png";
		}
	}
	
	/**
	 * Set the location of the image that represents the player
	 * Precondition: The path and image exist and are readable
	 * @param s The relative path to the image
	 */
	public void setImgPath(String s){
		this.imgPath = s;
	}
	
	/**
	 * Sets the ID of the player (useful for multi-player)
	 * @param id The player number
	 */
	public void setPlayerID(int id){
		this.playerID = id;
	}
	
	/**
	 * Gets the ID of this player
	 * @return int The player number
	 */
	public int getPlayerID(){
		return playerID;
	}
	
	/**
	 * Set the size of the player
	 * Precondition: Should be positive integer width, height
	 * @param width The width of the player
	 * @param height The height of the player
	 */
	public void setDimensions(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Get the boundaries of the player
	 * Precondition: The width and height have already been set
	 * @return Rectangle A rectangle with the boundaries of the player
	 */
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int) y, width, height);
	}

	/**
	 * Render the player onto the display buffer
	 * Precondition: The image path has been set to a readable image
	 * @param g The graphics object to render onto
	 */
	@Override
	public void render(Graphics g) {
		BufferedImage shell = null;
		try {
			shell = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		g.drawImage(shell,(int)x, (int)y, null);
	}
	
	
	
	/**
	 * Check if player position equal to some other object
	 * @param move The move the player is attempting to make
	 * @return boolean Whether a collision will occur with the move
	 */
	public boolean collision(Move move){
		for(int i = 0; i < handler.getList().size(); i++){
			GameObject tempObject = handler.getList().get(i);
			
			if(tempObject instanceof SearchStep)
				continue;
			
			if(tempObject.getID() == ID.Player){
				if(this.equals(tempObject))
					continue;
				
				if(getBounds().intersects(tempObject.getBounds())){
					return true;
				}
			}
			
			if(tempObject.getID() == ID.Wall){
				if(getBounds().intersects(tempObject.getBounds())){
					return true;
				}
			}
			if(tempObject.getID() == ID.Crate){
				if(getBounds().intersects(tempObject.getBounds())){
					
					Crate crateObject = (Crate)tempObject;
					
					crateObject.moveCrate(move);
					
					if(getBounds().intersects(tempObject.getBounds())){
						return true;
					}	
				}
			}
		}
		return false;
	}

	/**
	 * Move the player to a new position
	 * @param move The movement to apply to the player
	 */
	public void movePlayer(Move move){
		super.moveObject(move);
		Random r = new Random();
		String soundPath = "Sounds/sand" + r.nextInt(5) + ".wav";

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
			
		/*
		 * Change direction player is facing (model)
		 * Sounds only occur if no collision
		 */
		}else{
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(new File(soundPath));
				Clip c = AudioSystem.getClip();
				c.open(ais);
				c.start();
			} catch (Exception e) {
				//e.printStackTrace();
			}
			if(move == Move.Right){
				if(imgPath.matches(".*L.*")){
					String[] parts = imgPath.split(Pattern.quote("L"));			
					imgPath = parts[0] + "R" + parts[1];
				}
			}else if(move == Move.Left){
				if(imgPath.matches(".*R.*")){
					String[] parts = imgPath.split(Pattern.quote("R"));
					imgPath = parts[0] + "L" + parts[1];
				}
			}		
		}
	}
}
