import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Game extends Canvas{
	
	private static final long serialVersionUID = 6892297169509475163L;
	public static final int WIDTH = 1080, HEIGHT = WIDTH/12 * 9;
	private boolean running = false;
	
	private Menu menu;
	private HUD hud;
	private ObjectHandler handler;
	
	private DIFFICULTY gameDifficulty;
	private String modelPath;
	
	public STATE gameState = STATE.Menu;
	public MODE gameMode = MODE.SinglePlayer;
	
	/**
	 * Game initialises handler and menu when initialised
	 * initialises window
	 */
	public Game(){
		handler = new ObjectHandler();
		hud = new HUD(this);
		menu = new Menu(this, hud);
		
				
		this.addMouseListener(menu);
		this.addKeyListener(new keyInput(handler, this));
		
		new Window(WIDTH, HEIGHT, "Warehouse Boss - COMP2911", this);
	}

	/**
	 * Start plays music, handles returns to menu, and initiates cleanup and level generation,
	 *  according to the game's state
	 *  precondition: sound files exist in sound system
	 *  postcondition: music is played on a loop according to game state
	 */
	public void start(){
		running = true;
		Level level = null;
		AudioInputStream ais = null;
		String ostSrc = "";
		boolean musicIsPlaying = false;
			
		while(running == true){
									
			if(this.gameState == STATE.Menu){
				

							
				ostSrc = "Sounds/loop4.wav";
				/*
				 * Re-intialises level if level is already initialised
				 * on return to the menu via 'ESC'
				 */
				if(level != null){
					level.clearLevel();
					level = null;
				}
			}else if(this.gameState == STATE.Options){
				this.modelPath = menu.getModelPath();	
			}else if(this.gameState == STATE.Game){
				
				
				ostSrc = "Sounds/loop2.wav";
								
				if(level == null || this.gameState == STATE.LevelComplete){
					
					level = new Level(handler, gameDifficulty, this.modelPath);
					
					if(this.gameMode == MODE.MultiPlayer){
						level.setMode(MODE.MultiPlayer);
					}else if(this.gameMode == MODE.SinglePlayer){
						level.setMode(MODE.SinglePlayer);
					}
					level.generate();
					
				}else if(level.getGoalState() == true){
					this.gameState = STATE.LevelComplete;
					
					try {
						AudioInputStream ais2 = AudioSystem.getAudioInputStream(new File("Sounds/deposit.wav"));
						Clip cd = AudioSystem.getClip();
						cd.open(ais2);
						cd.start();
					} catch (Exception e1) {
						//e1.printStackTrace();
					}
					
					level.clearLevel();
					level = null;
				}
			}
			render();	
			
			
			if(musicIsPlaying == false){// || srcHasChanged(ostSrc)){
				musicIsPlaying = true;
				try {
					File file = new File(ostSrc);
					ais = AudioSystem.getAudioInputStream(file);
					Clip c = AudioSystem.getClip();
					c.open(ais);
					c.setLoopPoints(0, -1);
					c.loop(Clip.LOOP_CONTINUOUSLY);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
			
		}
	}
	
	/*
	private boolean srcHasChanged(String s){
		return true;
	}
	*/
	
	/**
	 * Initiates the rendering of the screen according to the current gamestate,
	 * calling handler or menu to render objects
	 * precondition: files for images exist in folder system
	 * postcondition: background image is displayed, 
	 * a render method is called out of one of the other classes
	 */
	private void render() {
	
	BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		
		BufferedImage bg = null;
		try {
			bg = ImageIO.read(new File("Images/bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(bg, 0, 0, this);
		
		
		if(gameState == STATE.Game){
			handler.render(g);
			hud.render(g);
		}else if(gameState == STATE.Menu || gameState == STATE.Help
				|| gameState == STATE.LevelComplete || gameState == STATE.MenuSelect
				|| gameState == STATE.Options){
			menu.render(g);
		}
		g.dispose();
		bs.show();
	}
		
	/**
	 * main calls game when run
	 * @param args
	 */
	public static void  main(String args[]){
		new Game();
	}
	
	/**
	 * sets game difficulty to given difficulty
	 * postCondition: difficulty is set to given difficulty
	 * @param
	 */
	public void setGameDifficulty(DIFFICULTY d){
		this.gameDifficulty = d;
	}
}
