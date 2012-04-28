package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
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
		System.out.print(tilesNum);
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
}
