/**
 * Starting class with main  game loop
 */

package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JFrame;

import utilities.GameKeys;

import entities.AbstractEntity;
import entities.Player;

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
	private Player player; // player class
	private ArrayList<AbstractEntity> ent = new ArrayList<AbstractEntity>(); // other entities in game (like enemies)
	private TreeSet<GameKeys> keysPressed = new TreeSet<GameKeys>();

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
	 * @param screen enumeration GamePart, choose witch game screen you want
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
		
		frame.revalidate();
		game.revalidate();
		
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
		
		//create game entities
		ent.addAll(Arrays.asList(game.getEntities()));
		
		for(AbstractEntity e : ent){
			if(e instanceof Player){
				player = (Player)e;
				if(!ent.remove(e)){
					System.err.println("Active level doesn't have player start position");
					System.exit(1);
				}
				break;
			}
		}
		
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
	
	public synchronized void waitHere(int ms){
		try {
			wait(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main game loop
	 */
	public void gameLoop(){
		init();
		
		if(screenChange)
			checkScreenChange();
		
		// main loop
		while(true){
			waitHere(50);
			
			processEvents();
			gameLogic();

			game.render();
			
			//System.out.println(this.screen.toString());
		}
		
	}
	
	/**
	 * Process events like keys here
	 */
	private void processEvents(){
		GameKeys k;
		while((k = game.getNextKeyPressed()) != GameKeys.NONE){ 
			keysPressed.add(k);
		}
	}
	
	/**
	 * Moving with screen, moving with enemies etc...
	 */
	private void gameLogic(){
		// reaction on movement keys
		int keys = 0;
		for(GameKeys k : keysPressed){
			if(k == GameKeys.W)
				keys += AbstractEntity.UP;
			else if(k == GameKeys.S)
				keys += AbstractEntity.DOWN;
			else if(k == GameKeys.A)
				keys += AbstractEntity.LEFT;
			else if(k == GameKeys.D)
				keys += AbstractEntity.RIGHT;
		}
		keysPressed.clear();
		player.move(keys);
	}
	
	/**
	 * @param args No use
	 */
	public static void main(String[] args) {
		
		GameStart game = new GameStart();
		
		game.gameLoop();
	}

}
