import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class keyInput extends KeyAdapter {
	
	private ObjectHandler handler;
	private boolean[] keyDown = new boolean[4];
	private Game game;
	
	/**
	 * keyInput initialises with a handler and a game.
	 * Default set to no keys pressed
	 * @param handler
	 * @param game
	 */
	public keyInput(ObjectHandler handler, Game game){
		this.handler = handler;
		this.game = game;
		
		keyDown[0] = false;
		keyDown[1] = false;
		keyDown[2] = false;
		keyDown[3] = false;
	}
	
	/**
	 * Method handles keypresses, altering game state/display as required
	 */
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.getList().size(); i++){
			GameObject tempObject = handler.getList().get(i);
			
			/**
			 * tempObject.getID() == ID.Crate
			 */
			
			if(tempObject.getID() == ID.Player){

				Player playerObject = (Player) tempObject;
				
				//key events for players 1 & 2
				if(playerObject.getPlayerID() == 1){
					if(key == KeyEvent.VK_UP) {playerObject.movePlayer(Move.Down); keyDown[0] = true;}
					if(key == KeyEvent.VK_DOWN) {playerObject.movePlayer(Move.Up); keyDown[1] = true;}
					if(key == KeyEvent.VK_LEFT) {playerObject.movePlayer(Move.Left); keyDown[2] = true;}
					if(key == KeyEvent.VK_RIGHT) {playerObject.movePlayer(Move.Right); keyDown[3] = true;}
				}else if(playerObject.getPlayerID() == 2){
					if(key == KeyEvent.VK_W) {playerObject.movePlayer(Move.Down); keyDown[0] = true;}
					if(key == KeyEvent.VK_S) {playerObject.movePlayer(Move.Up); keyDown[1] = true;}
					if(key == KeyEvent.VK_A) {playerObject.movePlayer(Move.Left); keyDown[2] = true;}
					if(key == KeyEvent.VK_D) {playerObject.movePlayer(Move.Right); keyDown[3] = true;}
				}
			}
		}
		
		if(game.gameState == STATE.Menu){
			if(key == KeyEvent.VK_ESCAPE)
				System.exit(1);
		}
		
		if(key == KeyEvent.VK_ESCAPE){
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Sounds/back.wav"));
				Clip c = AudioSystem.getClip();
				c.open(ais);
				c.start();
			} catch (Exception e1) {
				//e1.printStackTrace();
			}
			game.gameState = STATE.Menu;
		}

	}
}