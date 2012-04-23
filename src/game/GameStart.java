/**
 * Starting class with main  game loop
 */

package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import library.Defaults;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 */
public class GameStart implements ActionListener{
	JFrame frame;
	private final int appResolutionX = Defaults.getAppResolutionX();
	private final int appResolutionY = Defaults.getAppResolutionY();
	public GamePart screen;
	private MainMenu menu = new MainMenu(this);
	private GamePanel game = new GamePanel();
	private boolean screenChange = false;

	public enum GamePart {
		MENU, GAME
	}
	
	/**
	 * Constructor
	 */
	public GameStart(){
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().contains("Start New Game")){
			setScreen(GamePart.GAME);
			checkScreenChange();
		}
	}
	
	/* Getters and setters */
	public GamePart getScreen() {
		return screen;
	}

	/**
	 * Set JPanel on JFrame 
	 * @param screen enumaration GamePart, choose witch game screen you want
	 */
	public void setScreen(GamePart screen) {
		this.screen = screen;
		
		//change JPanel on frame
		frame.remove(menu);
		frame.remove(game);
		if(screen == GamePart.GAME){
			frame.add(game);
			System.out.println("set game");
		}
		else{
			frame.add(menu);
			System.out.println("set menu");
		}
		
		frame.repaint();
		game.repaint();
		
		this.screen = screen;
		
		// sign of change
		screenChange = true;
		System.out.println(screen.toString());
	}
	
	/**
	 * Initialize game before start
	 */
	public void init(){
		this.frame = new JFrame("Labyrinth game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(appResolutionX, appResolutionY);
		frame.setResizable(false);
		
		//frame.add(menu);
		setScreen(GamePart.MENU);
		frame.setVisible(true);
		System.out.println("Game started!");
		
	}
	
	/**
	 * Manage threads
	 * When we are at MainMenu, we don't need main game loop   
	 */
	public synchronized void checkScreenChange(){
		if(this.screen == GamePart.MENU){ // wait with thread
			try {
				System.out.println("Going to sleep");
				wait();
			} catch (InterruptedException e1) {
				System.err.println("Thread interrupted");
				System.exit(1);
			}
		}
		else if(this.screen == GamePart.GAME){ // wake up
			
			if(this.screen == GamePart.GAME){
				super.notify();
			}
		}
		
		screenChange = false;
	}
	
	
	/**
	 * Main game loop
	 */
	public void gameLoop(){
		init();
		
		// main loop
		while(true){
			
			if(screenChange)
				checkScreenChange();
	
			
			//System.out.println(this.screen.toString());
		}
		
	}
	
	/**
	 * @param args No use
	 */
	public static void main(String[] args) {
		
		GameStart game = new GameStart();
		
		game.gameLoop();
	}

}
