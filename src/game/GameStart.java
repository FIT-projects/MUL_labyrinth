/**
 * Starting class with main  game loop
 */

package game;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.text.AbstractDocument.Content;

import utilities.GameKeys;
import utilities.KeyMappper;

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
	private MediaPlayer vidPlay;
	private boolean wantPlay = false;
	private boolean screenChange = false;
	private Player player; // player class
	private ArrayList<AbstractEntity> ent = new ArrayList<AbstractEntity>(); // other entities in game (like enemies)
	private TreeSet<GameKeys> keysPressed = new TreeSet<GameKeys>();
	private TreeSet<GameKeys> keyReleased = new TreeSet<GameKeys>(); // the same as typed keys

	// specify part of the game
	public enum GamePart {
		MENU, GAME, INTRO
	}
	
	/**
	 * Constructor
	 */
	public GameStart(){
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().contains("Start New Game")){
			setScreen(GamePart.INTRO);
			//setScreen(GamePart.GAME);
			ent.clear();
			game.restartGame();
			loadEntities();
			checkScreenChange();
		}
		else if(e.getActionCommand().contains("Resume Game")){
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
			game.revalidate();
			game.repaint();
			System.out.println("set game");
		}
		else if(screen == GamePart.MENU){
			frame.add(menu);
			menu.revalidate();
			menu.repaint();
			System.out.println("set menu");
		}
		else if(screen == GamePart.INTRO){
			wantPlay = true;
		}
		
		frame.revalidate();
		//game.revalidate();
		
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
		frame.setLocation(150, 150);
		frame.setResizable(false);
		vidPlay = new MediaPlayer();
		
		loadEntities();
		
		//frame.add(menu);
		setScreen(GamePart.MENU);
		frame.setVisible(true);
		System.out.println("Game started!");
		
	}
	
	/**
	 * Load all game level entities
	 */
	private void loadEntities(){
		//create game entities
		ent.addAll(Arrays.asList(game.getEntities()));
		
		for(AbstractEntity e : ent){
			if(e instanceof Player){
				player = (Player)e;
				if(!ent.remove(e)){
					System.err.println("Active level doesn't have player starting position");
					System.exit(1);
				}
				break;
			}
		}
	}
	
	/**
	 * Provide actions for loaded entities
	 */
	private void entitiesActions(){
		for(AbstractEntity a : ent){
			a.action();
		}
		player.action();
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
		else if(this.screen == GamePart.GAME || this.screen == GamePart.INTRO){ // wake up
			super.notify();
		}
		
		screenChange = false;
	}
	
	/**
	 * Wait for some time
	 * @param ms Time to wait in milliseconds
	 */
	public synchronized void waitHere(long ms){
		try {
			wait(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Play game Intro
	 */
	private void playVideo(){	
		frame.revalidate();
		Container pane = frame.getContentPane();
		frame.setContentPane(vidPlay.getMedia());
		frame.setSize(appResolutionX, appResolutionY);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(true);
		
		vidPlay.play(); // play video (block program when playing this)
		
		//wait for some time for initialization of video
		waitHere(5000);
		
		// is really not playing now
		while(vidPlay.isPlaying()){
			waitHere(50);
			if(!frame.isVisible())
				vidPlay.stop();
		}
		
		Point loc = frame.getLocation();
		frame.setContentPane(pane);
		frame.setLocation(loc);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setScreen(GamePart.GAME);
		wantPlay = false;
	}
	
	/**
	 * Main game loop
	 */
	public void gameLoop(){
		init();
		
		if(screenChange){
			checkScreenChange();
			if(wantPlay)
				playVideo();
		}
		
		// main loop
		while(true){
			waitHere(10);
			
			processEvents();
			gameLogic();
			
			if(screenChange){
				checkScreenChange();
				if(wantPlay)
					playVideo();
			}
			
			game.render();
			
		}
		
	}
	
	/**
	 * Process events like keys here
	 */
	private void processEvents(){
		KeyMappper.processKeys();
		keysPressed = KeyMappper.getPressed();
		keyReleased = KeyMappper.getReleased();

	}
	
	/**
	 * Moving with screen, moving with enemies etc...
	 */
	private void gameLogic(){
		
		for(GameKeys k : keyReleased){
			// reaction on game control keys
			if(k == GameKeys.ESC){
				menu.resumeGameVisibility(true);
				setScreen(GamePart.MENU);
				//System.exit(0);
			}
		}
		
		int move = 0;
		for(GameKeys k : keysPressed){
			// reaction on movement keys
			if(k == GameKeys.W)
				move += AbstractEntity.UP;
			else if(k == GameKeys.S)
				move += AbstractEntity.DOWN;
			else if(k == GameKeys.A)
				move += AbstractEntity.LEFT;
			else if(k == GameKeys.D)
				move += AbstractEntity.RIGHT;
		}
		keysPressed.clear();
		
		/* Game actions */
		player.move(move);
		entitiesActions();
	}
	
	/**
	 * Entry point of program
	 * @param args No use
	 */
	public static void main(String[] args) {
		GameStart game = new GameStart();
		
		game.gameLoop();
	}

}
