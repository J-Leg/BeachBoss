import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class HUD{
	private long startTime;
	private long toDisplay;
	private Game game;
	
	public HUD(Game g){
		this.game = g;
	}
	
	public void render(Graphics g){
		
		if(game.gameState != STATE.Game){
			return;
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		//Format string
		this.toDisplay = elapsedTime / 1000;
		
		String toDisplay = "TIMER: " + Long.toString(this.toDisplay);
		g.setFont(new Font("arial", 1, 25));
		
		g.setColor(new Color(0,0,0,127));
		g.fillRect(Game.WIDTH*6/7 - game.WIDTH/50, Game.HEIGHT*1/16 - Game.HEIGHT/22, game.WIDTH/5, game.HEIGHT/15);
		
		g.setColor(Color.WHITE);
		g.drawString(toDisplay, Game.WIDTH*6/7, Game.HEIGHT*1/16);
		
		
	}
	
	public void reset(){
		startTime = System.currentTimeMillis();
	}
	
	public long getFinalTime(){
		return toDisplay;
	}
}
