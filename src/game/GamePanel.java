package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import utilities.KeyMappper;

import entities.AbstractEntity;
import entities.EnemyEntity;
import entities.GemEntity;
import entities.Player;
import library.*;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Draw game table
 */
public class GamePanel extends JPanel{
	/**
	 * For serialization (warning removal)
	 */
	private static final long serialVersionUID = 5851298974168429267L;
	
	private ImageLibrary iLib = null;
	private MapLibrary mLib = null;
	private final int resolution = Defaults.getImageResTile();
	private final String imagesFile = Defaults.getImageFile();
	private int tilesNum; // number of tiles to render
	private RenderWindow render;
	private BufferedImage input;
	private Player refPlayer;
	private int ticksToShow = 0, fpsToShow = 0;
	 

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
		
		g2.setColor(Color.white);
		g2.drawString("GEMS: "+refPlayer.getCollectedGemCount()+"/"+mLib.getGemCount(), 0, 30);
		g2.drawString("HEALTH: "+refPlayer.getHealth()+"/"+Defaults.getMaxHealth(), 0, 15);	
		
	
		
		g2.drawString("Ticks: "+ticksToShow, Defaults.getAppResolutionX() - 60, 15);
		g2.drawString("FPS: "+fpsToShow, Defaults.getAppResolutionX() - 60, 30);
		
		if (refPlayer.getHealth() <= 0 || refPlayer.exited())
		{
			Font f = new Font(g2.getFont().getName(), g2.getFont().getStyle(), g2.getFont().getSize()+20);
	        g2.setFont(f);
	        FontMetrics fmH = getFontMetrics(g2.getFont());
	        
	        String str;
	        if (refPlayer.getHealth() <= 0)
	        	str = "Game over!";
	        else
	        	str = "Level "+(mLib.getActiveLevelId()+1)+" completed!";
	        	
	        int w = fmH.stringWidth(str);
	        			
	        g2.drawString(str, Defaults.getAppResolutionX() / 2  - w/2, Defaults.getAppResolutionY()/2 - 20);
		}

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
		MapLibrary.Entities[] mapEnt = mLib.getLevelEntities();
		
		int[] loc = new int[2];
		loc = mLib.getStartLocation();
		refPlayer = new Player(loc[0],loc[1],mLib,iLib.getImageTile(Defaults.getImgPlayerId()),render, mLib.getGemCount());
		ret[0] = refPlayer;
				
		int counter = 1;
		for(MapLibrary.Entities e : mapEnt){
			if (e.imgID == Defaults.getImgIdByImgName("enemy"))
				ret[counter] = new EnemyEntity(e.locX, e.locY, mLib, iLib.getImageTile(e.imgID), render);
			else if (e.imgID == Defaults.getImgIdByImgName("gem"))
				ret[counter] = new GemEntity(e.locX, e.locY, mLib, iLib.getImageTile(e.imgID), render);
			counter++;
		}
		
		return ret;
	}
	
	public int numberEntities(){
		return mLib.getNumberEntLvl();
	}
	
	
	/**
	 * Restart game
	 */
	public void restartGame(int level){
		
		this.mLib = new MapLibrary(level);
		int[] start = mLib.getStartLocation();
		render = new RenderWindow(mLib, iLib, tilesNum, tilesNum, start[0], start[1]);
		render.loadScreen();
	}
	
	public void setShowInfo(int ticks, int frames)
	{
		ticksToShow = ticks;
		fpsToShow = frames;		
	}
}
