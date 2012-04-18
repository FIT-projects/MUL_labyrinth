/**
 * Starting class with main  game loop
 */
package game;

import javax.swing.JFrame;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 */
public class GameStart{
	final private int appResolution = 512;
	
	/**
	 * Constructor
	 */
	public GameStart(){
	}
	
	public void gameLoop(){
		JFrame frame = new JFrame("Labyrinth game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new GamePanel());
		//frame.add(new MainMenu());
		frame.setSize(appResolution, appResolution);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * @param args No use
	 */
	public static void main(String[] args) {
		
		GameStart game = new GameStart();
		
		game.gameLoop();
	}

}
