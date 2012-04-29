package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import utilities.GameKeys;

import entities.AbstractEntity;
import entities.Player;
import library.*;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Draw game table
 */
public class GamePanel extends JPanel{
	private ImageLibrary iLib = null;
	private MapLibrary mLib = null;
	private final int resolution = Defaults.getImageResTile();
	private final String imagesFile = Defaults.getImageFile();
	private int tilesNum; // number of tiles to render
	private RenderWindow render;
	private BufferedImage input;
	private TreeSet<GameKeys> keyPressed = new TreeSet<GameKeys>();
	private TreeSet<GameKeys> keyReleased = new TreeSet<GameKeys>();
	 

	GamePanel(){
		super();
		this.iLib = new ImageLibrary(imagesFile, resolution);
		this.mLib = new MapLibrary(0);
		tilesNum = Defaults.getAppResolutionX();
		tilesNum /= resolution;
		
		int[] start = mLib.getStartLocation();
		render = new RenderWindow(mLib, iLib, tilesNum, tilesNum, start[0], start[1]);
		
		//addKeyListener(this);
		setFocusable(true);
		Action a = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("pressed " + arg0.getActionCommand() + " " + arg0.getWhen());
				keyPressed(arg0.getActionCommand());
			}
		};
		Action a2 = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("released " + arg0.getActionCommand() + " " + arg0.getWhen());
			}
		};
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "try");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "try");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "try");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "try");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"), "try2");
		this.getActionMap().put("try", a);
		this.getActionMap().put("try2", a2);
		
		render.loadScreen(); // load default screen
		System.out.print(tilesNum);
	}
	
	//TODO bad key listener
	private void keyPressed(String key){
		
		if(key.equals("w")){
			keyPressed.add(GameKeys.W);
		}
		else if(key.equals("s")){
			keyPressed.add(GameKeys.S);
		}
		else if(key.equals("a")){
			keyPressed.add(GameKeys.A);
		}
		else if(key.equals("d")){
			keyPressed.add(GameKeys.D);
		}
	}
	
	private void keyReleased(int keyCode) {
		
		if(keyCode == KeyEvent.VK_W){
			keyReleased.add(GameKeys.W);
		}
		else if(keyCode == KeyEvent.VK_S){
			keyReleased.add(GameKeys.S);
		}
		else if(keyCode == KeyEvent.VK_A){
			keyReleased.add(GameKeys.A);
		}
		else if(keyCode == KeyEvent.VK_D){
			keyReleased.add(GameKeys.D);
		}
	}
	
	/**
	 * Renderer of the game screen
	 */
	public void render(){
		input = (BufferedImage) render.getImageToRender();
		
		this.repaint();
	}
	
	/**
	 * Override method for painting the game
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.drawImage(input, 0, 0, this);
	}
	
	/**
	 * Get game inner window (window + 2)
	 * @return game inner window
	 */
	public RenderWindow getWindow(){
		return render;
	}
	
	public AbstractEntity[] getEntities(){
		AbstractEntity[] ret = new AbstractEntity[mLib.getNumberEntLvl()];
		
		int[] loc = new int[2];
		loc = mLib.getStartLocation();
		
		ret[0] = new Player(loc[0],loc[1],mLib,iLib.getImageTile(Defaults.getImgPlayerId()),render);
		
		return ret;
	}
	
	public int numberEntities(){
		return mLib.getNumberEntLvl();
	}
	
	public GameKeys getNextKeyPressed(){
		if(keyPressed.isEmpty())
			return GameKeys.NONE;
		GameKeys b = keyPressed.first();
		keyPressed.remove(b);
		return b;
	}
	
	public GameKeys getNextKeyReleased(){
		if(keyReleased.isEmpty())
			return GameKeys.NONE;
		
		GameKeys b = keyReleased.first();
		keyReleased.remove(b);
		
		return b;
	}
}
