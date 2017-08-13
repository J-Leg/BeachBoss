
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Menu extends MouseAdapter {
	
	private Game game;
	private String[] menuList = {"Play", "Help", "Quit"};
	private String[] selectList = {"I'm new", "I like puzzle games", "PreGen Levels"};
	private int menuItems = 3;
	private int posX, posY, lengthX, lengthY, stringX, stringY;	
	private HUD hud;
	private int selectionX = game.WIDTH*2/3 - 60;
	private int selectionY = game.HEIGHT*2/5;
	private String modelPath;
	

	
	/*
	 * Button/Text Attributes
	 */
	private Color shade = new Color(0, 0, 0, 127);
	private Font fnt = new Font("arial", 1, game.WIDTH/16);		// Heading
	private Font fnt2 = new Font("arial", 1, game.WIDTH/36);	// Button Font
	private Font fnt3 = new Font("arial", 1 , game.WIDTH/48);   // Difficulty button font
	private Font fnt4 = new Font("arial", 1 ,25);				// Options Text Font
	
	/**
	 * Menu initialises with a game and a hud
	 * @param game
	 * @param hud
	 */
	public Menu(Game game, HUD hud){
		this.game = game;
		this.hud = hud;
	}
	
	/**
	 * draws the various menues of the game, depending on game state
	 * precondition: image files are in folder system
	 * postcondition: display is updated according to game state
	 * @param g
	 */
	public void render(Graphics g){
				
		/*
		 * Initialise variables
		 */
		initialise();
		
		if(game.gameState == STATE.Menu){
			

			Image background = new ImageIcon("Images/beach.gif").getImage();
			g.drawImage(background, 0, 0, game.WIDTH, game.HEIGHT, null);
												
			drawTitle(g, "Beach Boss", game.WIDTH/2, game.HEIGHT/4, fnt);
			
			/*
			 * Play, Help, Quit (Add more later)
			 */
			for(int i = 0; i < menuItems; i++){
				posY += game.HEIGHT/9;
				stringY += game.HEIGHT/9;

								
				drawButton(g, menuList[i], posX, posY, lengthX, lengthY, 
						new Color(0,0,0,127), Color.WHITE, fnt2);
			}
			
			/*
			 * Mechanical cog (Options button)
			 */
			BufferedImage options = null;
			try {
				options = ImageIO.read(new File("Images/gears.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			g.drawImage(options, Game.WIDTH*4/5, Game.HEIGHT*3/4, 65, 65, null);

			reset();
			
		}else if(game.gameState == STATE.MenuSelect){
			

			BufferedImage background = null;
			try {
				background = ImageIO.read(new File("Images/sunsetBeachPNG.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(background, 0, 0, game.WIDTH, game.HEIGHT, game);
						
			drawButton(g, "Back", posX, posY + game.HEIGHT/3 + 5, lengthX, lengthY, 
					new Color(0,0,0,127), Color.WHITE, fnt3);
			
			
			Image m1 = new ImageIcon("Images/emGif.gif").getImage();
			Image m2 = new ImageIcon("Images/esGIF.gif").getImage();
			
			g.drawImage(m1, game.WIDTH*1/10, game.HEIGHT/2, 90, 90, null);
			g.drawImage(m2, game.WIDTH*8/10, game.HEIGHT/2, 90, 90, null);
			
			
			/*
			 * Draw Difficulty Buttons
			 */		
			posY = game.HEIGHT/15;
			stringY = posY + game.HEIGHT/25;
			for(int i = 0; i < menuItems; i++){
				posX = game.WIDTH/25 + i*game.WIDTH/3;
			

				drawButton(g, selectList[i], posX, posY - 2, (int) ((int)lengthX*1.4), lengthY, 
						new Color(0,0,0,127), Color.WHITE, fnt3);
			}
			
			/*
			 * Draw 2-Player button
			 */
			int multiX = Game.WIDTH*2/3 + Game.WIDTH/18;
			int multiY = game.HEIGHT/2 - game.HEIGHT/20 + game.HEIGHT/3;
			
			drawButton(g, "2-Player", multiX, multiY, lengthX, lengthY, 
					new Color(0,0,0,127), Color.WHITE, fnt3);
			
		}else if(game.gameState == STATE.Options){
						
			drawTitle(g, "Options", game.WIDTH/2 - 70, game.HEIGHT/10, fnt);					
			drawButton(g, "Back", posX, posY + game.HEIGHT/3 + 5, lengthX, lengthY, 
					new Color(0,0,0,127), Color.WHITE, fnt2);
			
			drawTitle(g, "Pick a Skin!", game.WIDTH*2/3, game.HEIGHT*2/7 + game.HEIGHT*1/10, fnt2);
			
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File("Images/Player1R.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.setColor(Color.WHITE);
			g.drawRect(selectionX, selectionY, 90, 90);
			g.drawImage(img, game.WIDTH*2/3 - 60, game.HEIGHT*2/5, null);
			
			
			try {
				img = ImageIO.read(new File("Images/Player2R.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img, game.WIDTH*2/3 + 60, game.HEIGHT*2/5, null);
			
			
			try {
				img = ImageIO.read(new File("Images/Player3R.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img, game.WIDTH*2/3 + 180, game.HEIGHT*2/5, null);
			
			if(selectionX == game.WIDTH*2/3 - 60 && selectionY == game.HEIGHT*2/5){
				modelPath = "Images/Player1R.png";
			}else if(selectionX == game.WIDTH*2/3 + 60 && selectionY == game.HEIGHT*2/5){
				modelPath = "Images/Player2R.png";
			}else if(selectionX == game.WIDTH*2/3 + 180 && selectionY == game.HEIGHT*2/5){
				modelPath = "Images/Player3R.png";
			}
			
		}else if(game.gameState == STATE.Help){
			
			BufferedImage background = null;
			try {
				background = ImageIO.read(new File("Images/beach.jpeg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			g.drawImage(background, 0, 0, game.WIDTH, game.HEIGHT, game);
						
			drawTitle(g, "Help", game.WIDTH/2 - 70, game.HEIGHT/10, fnt);				
			drawButton(g, "Back", posX, posY + game.HEIGHT/3, lengthX, lengthY, 
					new Color(0,0,0,127), Color.WHITE, fnt2);

			String[] helpContent = {"Use arrow keys to move your player. (W, A, S, D for 2P)", "Fill shells with crabs!",
					"Try to minimise your number of moves"};
		
		
			int helpPosX = Game.WIDTH/4;
			int helpPosY = Game.HEIGHT/3;
			
			g.setFont(fnt4);
			
			for(int i = 0; i < 3; i++){
				g.drawString(helpContent[i], helpPosX, helpPosY + i * 40);
			}
	
		}else if(game.gameState == STATE.LevelComplete){
						
			drawTitle(g, "Level Complete.", game.WIDTH/2, game.HEIGHT/10, fnt2);
			
			drawButton(g, "Next Level", posX, posY + game.HEIGHT/3, lengthX, lengthY, 
					new Color(0,0,0,127), Color.WHITE, new Font("arial", 1 ,25));
			
			String timer = "Time Elapsed: " + Long.toString(hud.getFinalTime()) + " Seconds";
			g.setFont(fnt4);
			g.drawString(timer, game.WIDTH/2, game.HEIGHT/2);
			
		}
	}
	
	/**
	 * Return the string path
	 * @return
	 */
	public String getModelPath(){
		return modelPath;
	}
	
	/**
	 * Draw a title on the screen with given position, string, and font
	 * postcondition: text drawn on screen with given values
	 * @param g
	 * @param s
	 * @param x
	 * @param y
	 * @param f
	 */
	private void drawTitle(Graphics g, String s, int x, int y, Font f){
		g.setFont(f);
		g.setColor(Color.white);
		g.drawString(s, x, y);
	}
	
	/**
	 * draw a button on the screen with given position, string, and font
	 * postcondition: button drawn on screen with given values
	 * @param g
	 * @param s
	 * @param x
	 * @param y
	 * @param lenghtX
	 * @param lenghtY
	 * @param shadeC
	 * @param textC
	 * @param f
	 */
	private void drawButton(Graphics g, String s, int x, int y, 
			int lenghtX, int lenghtY, Color shadeC, Color textC, Font f){
		
		g.setFont(f);
		g.setColor(shadeC);
		g.fillRect(x, y, lenghtX, lenghtY);
		g.setColor(textC);
		g.drawRect(x, y, lenghtX, lenghtY);
		g.drawString(s, x + Game.WIDTH/50, y + game.HEIGHT/22);

	}

	/**
	 * handles mouseclicks when in the menu's, most importantly to handle button clicks
	 * postcondition: changes gameState accordingly when a button is pressed
	 */
	public void mousePressed(MouseEvent e){
		int mx = e.getX();
		int my = e.getY();

		if(game.gameState == STATE.Menu){
			
			for(int i = 0; i < menuItems; i++){
				posY += game.HEIGHT/9;
				stringY += game.HEIGHT/9;
				
				/*
				 * Mouse over main menu list
				 */
				if(mouseOver(mx, my, posX, posY, lengthX, lengthY)){

					try {
						AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
						Clip c = AudioSystem.getClip();
						c.open(ais);
						c.start();
					} catch (Exception e1) {
						//e1.printStackTrace();
					}

					if(menuList[i] == "Play"){
						game.gameState = STATE.MenuSelect;
						break;
					}
					
					if(menuList[i] == "Help"){
						game.gameState = STATE.Help;
					}
					
					if(menuList[i] == "Quit"){
						System.exit(1);
					}
				}
			}
			
			/*
			 * Mouse over options bar
			 */
			if(mouseOver(mx, my, Game.WIDTH*4/5, Game.HEIGHT*3/4, 65, 65)){
				
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
					Clip c = AudioSystem.getClip();
					c.open(ais);
					c.start();
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
				game.gameState = STATE.Options;
			}

		}else if(game.gameState == STATE.MenuSelect){
			
			for(int i = 0; i < menuItems; i++){
				
				posX = game.WIDTH/25 + i*game.WIDTH/3;
				stringX = posX + game.WIDTH/50;

				if(mouseOver(mx, my, posX, posY, lengthX, lengthY)){
					
					try {
						AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
						Clip c = AudioSystem.getClip();
						c.open(ais);
						c.start();
					} catch (Exception e1) {
						//e1.printStackTrace();
					}

					/*
					 * Set Difficulty accordingly
					 */
					if(selectList[i] == "I'm new"){
						game.gameState = STATE.Game;
						game.setGameDifficulty(DIFFICULTY.Easy);
						game.gameMode = MODE.SinglePlayer;
						hud.reset();
					}
				
					if(selectList[i] == "I like puzzle games"){
						game.gameState = STATE.Game;
						game.setGameDifficulty(DIFFICULTY.Medium);
						game.gameMode = MODE.SinglePlayer;
						hud.reset();
					}
				
					if(selectList[i] == "PreGen Levels"){
						game.gameState = STATE.Game;
						game.setGameDifficulty(DIFFICULTY.Hard);
						game.gameMode = MODE.SinglePlayer;
						hud.reset();
					}
				}
			}

			/*
			 * 2-Player button
			 */
			if(mouseOver(mx, my, Game.WIDTH*2/3 + Game.WIDTH/18, 
					game.HEIGHT/2 - game.HEIGHT/20 + game.HEIGHT/3, lengthX, lengthY)){
				
				
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
					Clip c = AudioSystem.getClip();
					c.open(ais);
					c.start();
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
		
				game.gameMode = MODE.MultiPlayer;
				game.gameState = STATE.Game;
				game.setGameDifficulty(DIFFICULTY.Easy);
				hud.reset();
			}
			
			/*
			 * Back button
			 */
			if (mouseOver(mx, my, game.WIDTH/10 - game.WIDTH/25, 
					game.HEIGHT/2 - game.HEIGHT/20 + game.HEIGHT/3, lengthX, lengthY)){
				
				
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
					Clip c = AudioSystem.getClip();
					c.open(ais);
					c.start();
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
				
				game.gameState = STATE.Menu;
				return;
			}
		}else if(game.gameState == STATE.Options){
			boolean clicked = false;
			
			if(mouseOver(mx, my, game.WIDTH*2/3 - 60, game.HEIGHT*2/5, 90, 90)){
				selectionX = game.WIDTH*2/3 - 60;
				selectionY = game.HEIGHT*2/5;
				clicked = true;
			}else if(mouseOver(mx, my, game.WIDTH*2/3 + 60, game.HEIGHT*2/5, 90, 90)){
				selectionX = game.WIDTH*2/3 + 60;
				selectionY = game.HEIGHT*2/5;
				clicked = true;
			}else if(mouseOver(mx, my, game.WIDTH*2/3 + 180, game.HEIGHT*2/5, 90, 90)){
				selectionX = game.WIDTH*2/3 + 180;
				selectionY = game.HEIGHT*2/5;
				clicked = true;
			}
			
			if(clicked){
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
					Clip c = AudioSystem.getClip();
					c.open(ais);
					c.start();
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
			}
		}
		
		//Back
		if(game.gameState == STATE.Help || game.gameState == STATE.Options){
			
			if(mouseOver(mx, my, posX, posY + game.HEIGHT/3, lengthX, lengthY)){

				
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
					Clip c = AudioSystem.getClip();
					c.open(ais);
					c.start();
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
				game.gameState = STATE.Menu;
				return;
			}
		}
		
		//Completed Level
		//Next level button
		if(game.gameState == STATE.LevelComplete){
			
			if(mouseOver(mx, my, posX, posY + game.HEIGHT/3, lengthX, lengthY)){
				
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/triggered.wav"));
					Clip c = AudioSystem.getClip();
					c.open(ais);
					c.start();
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
				hud.reset();
				game.gameState = STATE.Game;
				return;
			}
		}
		
	}
	
	/**
	 * no function for mouse release
	 */
	public void mouseReleased(MouseEvent e){
		
	}
	
	/**
	 * initialises the x and y positions of the elements of each menu, including the string
	 * position for buttons
	 */
	public void initialise(){
		stringX = game.WIDTH/10;
		posX = stringX - game.WIDTH/25;
		
		reset();

		lengthX = game.WIDTH/5;
		lengthY = game.HEIGHT/15;
	}

	/**
	 * resets the value of the y position, playing the location of elements back up the top
	 */
	public void reset(){
		stringY = game.HEIGHT/2;
		posY = stringY - game.HEIGHT/20;
	}
	
	
	
	/**
	 * Based from example
	 * https://docs.oracle.com/javase/tutorial/uiswing/events/mouselistener.html
	 * Mouse listener
	 */
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height){
		if(mx > x && mx < x + width){
			if(my > y && my < y + height){
				return true;
			}else return false;
		}else return false;
	}
}