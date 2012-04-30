package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.TreeSet;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import utilities.GameKeys;
import utilities.KeyMappper;

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
	 

	GamePanel(){
		super();
		this.iLib = new ImageLibrary(imagesFile, resolution);
		this.mLib = new MapLibrary(0);
		tilesNum = Defaults.getAppResolutionX();
		tilesNum /= resolution;
		
		int[] start = mLib.getStartLocation();
		render = new RenderWindow(mLib, iLib, tilesNum, tilesNum, start[0], start[1]);
		render.loadScreen(); // load default screen
		
		setFocusable(true);

		//game move
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "pressedMove");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "pressedMove");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "pressedMove");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "pressedMove");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"), "releasedMove");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), "releasedMove");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"), "releasedMove");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"), "releasedMove");
		this.getActionMap().put("pressedMove", KeyMappper.getActionMovePressed());
		this.getActionMap().put("releasedMove", KeyMappper.getActionMoveReleased());
		
		//game control
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pressedControl");
		this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ESCAPE"), "releasedControl");
		this.getActionMap().put("pressedControl", KeyMappper.getActionControlPressed());
		this.getActionMap().put("releasedControl", KeyMappper.getActionControlReleased());
		
		System.out.print(tilesNum);
	}
	
	
	/**
	 * Renderer of the game screen
	 */
	public void render(){
		render.renderEntities();
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
	
	/**
	 * Return all game entities
	 * @return Abstract entity array object 
	 */
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
	
	
	/**
	 * Restart game
	 */
	public void restartGame(){
		int[] start = mLib.getStartLocation();
		render = new RenderWindow(mLib, iLib, tilesNum, tilesNum, start[0], start[1]);
		render.loadScreen();
	}
}
