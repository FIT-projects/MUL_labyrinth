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
	final private int resolution = 32;
	final static private String imagesFile = "resources/terrain.png";

	GamePanel(){
		super();
		this.iLib = new ImageLibrary(imagesFile, resolution);
		this.mLib = new MapLibrary(0);
		
		System.out.print(mLib.getTile(0));
	}
	
	/**
	 * Override method for painting the game
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		BufferedImage img = (BufferedImage)iLib.getImageTile(0);
		
		g2.drawImage(iLib.getImageTile(0), 0, 0, this);
		g2.drawImage(iLib.getImageTile(1), 32, 0, this);
		
	}
}
