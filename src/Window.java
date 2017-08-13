import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends Canvas{

	private static final long serialVersionUID = 9034494958129720942L;

	/**
	 * Creates a window to display the game, then starts the game
	 * Precondition: game has otherwise been fully initialised,
	 *  and the game can be started by this function.
	 * @param width The width of the desired content window
	 * @param height The height of the desired content window
	 * @param title The title of the window
	 * @param game The game object to create the window for
	 */
	public Window(int width, int height, String title, Game game){
		JFrame frame = new JFrame(title);
			
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}
}
