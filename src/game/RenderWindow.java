/**
 * 
 */
package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import entities.AbstractEntity;
import entities.Player;

import library.Defaults;
import library.ImageLibrary;
import library.MapLibrary;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Storing content for rendering by JPanel
 * Creating abstraction between loading map and rendering game window 
 */
public class RenderWindow {
	private MapLibrary mSource; // source map, from which we get raw data
	private ImageLibrary iSource; // source of images
	// number of tiles in width[0] and height[1]
	private int[] tiles = new int[2];
	// minimum and maximum tile in the inner window (map coordinates)
	private int[] minmaxX = new int[2];
	private int[] minmaxY = new int[2];
	// background image to render (without player or enemies)
	private BufferedImage background;
	// final image
	private BufferedImage finalImg;
	private ArrayList<AbstractEntity> ent = new ArrayList<AbstractEntity>();
	
	// store image types
	private short[][] imgMap;
	// resolution of outer window
	private int[] outerRes = new int[2];
	// optimization
	private final int tileRes = Defaults.getImageResTile(); 
	// open way index type
	private final int freeW = Defaults.getMoveTileIndex();
	
	/**
	 * Get pixels from libraries and create abstraction for rendering in JPanel
	 * 
	 * @param mapLib Loading information about map
	 * @param imgLib Images Library - compatible with map
	 * @param numtilesX number of tiles X coordinate which we will see in main window
	 * @param numtilesY number of tiles Y coordinate which we will see in main window
	 * @param startX starting X position of this window
	 * @param startY starting Y position of this window
	 */
	public RenderWindow(MapLibrary mapLib, ImageLibrary imgLib, int numtilesX, int numtilesY, int startX, int startY){
		mSource = mapLib;
		iSource = imgLib;
		// for rendering we need to see one more tile
		tiles[0] = numtilesX + 2;
		tiles[1] = numtilesY + 2;
		minmaxX[0] = startX - tiles[0] / 2;
		minmaxY[0] = startY - tiles[1] / 2;
		
		minmaxX[1] = minmaxX[0] + tiles[0];
		minmaxY[1] = minmaxY[1] + tiles[1];
		
		outerRes[0] = numtilesX * tileRes;
		outerRes[0] = numtilesY * tileRes;
		// create pixel map
		background = new BufferedImage(tiles[0]*tileRes, 
				tiles[1]*tileRes, BufferedImage.TYPE_INT_ARGB);
		
		finalImg = new BufferedImage(tiles[0]*tileRes, 
				tiles[1]*tileRes, BufferedImage.TYPE_INT_ARGB);

		// create image map
		imgMap = new short[tiles[1]][tiles[0]];
	}
	
	/**
	 * Load default screen of the map
	 */
	public void loadScreen(){
		// load image types from map
		System.out.println("X: " + tiles[0] + " Y: " + tiles[1]);
		
		for(int i = 0; i < tiles[0];i++){
			imgMap[i] = mSource.getTileLine(minmaxX[0], minmaxY[0]+i,tiles[1]);
		}
		
		Graphics2D g = background.createGraphics();
		
		int res = Defaults.getImageResTile();
		for(int y = 0; y < tiles[1];y++){
			for(int x = 0; x < tiles[0];x++){
					g.drawImage(iSource.getImageTile(imgMap[y][x]), x*res, y*res, null);
				
			}

		}
		
		g.dispose();
		printMap();
		
		renderEntities();

	}
	
	/**
	 * Final image to render
	 * @return image to render on JPanel
	 */
	public Image getImageToRender(){
		//innerWindow.setData(raster);
		return(finalImg.getSubimage(tileRes, tileRes, background.getWidth()-tileRes, background.getHeight()-tileRes));
	}
	
	/**
	 * Render entities in window
	 */
	private void renderEntities(){
		//TODO this won't function for more entities -> new erase image before
		Graphics2D g2 = finalImg.createGraphics();
		g2.drawImage(background, 0, 0, null);
		System.out.println("Num ent: " + ent.size());
		
		for(AbstractEntity e : ent){
			if(e instanceof Player){
				int[] loc = e.getTileLocation();
				int[] pix = e.getPixelLocation();
				g2.drawImage(e.getAvatarImage(), ((loc[0] - minmaxX[0])*tileRes)+pix[0], 
						((loc[1] - minmaxY[0])*tileRes)+pix[1], null);
			}
			else
			{
				int[] loc = e.getTileLocation();
				int[] pix = e.getPixelLocation();
				g2.drawImage(e.getAvatarImage(), ((loc[0] - minmaxX[0])*tileRes)+pix[0], 
						((loc[1] - minmaxY[0])*tileRes)+pix[1], null);
			}
		}
		
		g2.dispose();
	}
	
	/**
	 * Entity is in render window (we can see him)
	 * @param e same entity object as Player
	 */
	public void addEntity(AbstractEntity e){
		if(!ent.contains(e))
			ent.add(e);
		renderEntities();
	}
	
	/**
	 * Entity is out of window (we can't see him)
	 * @param e AbstractEntity which we want remove
	 * @return false if this entity wasn't here
	 */
	public boolean removeEntity(AbstractEntity e){
		return ent.remove(e);
	}
	
	/**
	 * For testing purpose
	 */
	private void printMap(){
		for(int y = 0;y < tiles[0];y++){
			for(int x = 0; x < tiles[1];x++)
				System.out.print(imgMap[y][x]+",");
			
			System.out.print('\n');
		}
	}
	
	/**
	 * Return X minimum and maximum of the inner window (window + 2)
	 * @return [minimum, maximum]
	 */
	public int[] getMinMaxX(){
		return minmaxX;
	}
	
	/**
	 * Return Y minimum and maximum of the inner window (window + 2)
	 * @return [minimum, maximum]
	 */
	public int[] getMinMaxY(){
		return minmaxY;
	}
}
