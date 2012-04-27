/**
 * 
 */
package game;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import library.Defaults;
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
	// number of tiles in width[0] and height[1]
	private int[] tiles = new int[2];
	private int[] halfTiles = new int[2];
	// starting position
	private int[] start = new int[2];
	// data store row[column]
	private BufferedImage window;
	private WritableRaster raster;
	// store image types
	private short[][] imgMap;
	
	/**
	 * Get pixels from libraries and create abstraction for rendering in JPanel
	 * 
	 * @param map Loading information about map
	 * @param numtilesX number of tiles X coordinate which we will see in main window
	 * @param numtilesY number of tiles Y coordinate which we will see in main window
	 * @param startX starting X position of this window
	 * @param startY starting Y position of this window
	 */
	public RenderWindow(MapLibrary map, int numtilesX, int numtilesY, int startX, int startY){
		mSource = map;
		// for rendering we need to see one more tile
		tiles[0] = numtilesX + 2;
		tiles[1] = numtilesY + 2;
		start[0] = startX - tiles[0] / 2 + 1;
		start[1] = startY - tiles[1] / 2 - 1;
		// create pixel map
		window = new BufferedImage(tiles[0]*Defaults.getImageResTile(), 
				tiles[1]*Defaults.getImageResTile(), BufferedImage.TYPE_INT_RGB);
		
		raster = window.getRaster();
		
		// create image map
		imgMap = new short[tiles[0]][tiles[1]];
	}
	
	public void loadScreen(){
		// load image types from map
		System.out.println("X: " + tiles[0] + " Y: " + tiles[1]);
		
		for(int i = 0; i < tiles[0];i++){
			imgMap[i] = mSource.getTileLine(start[0], start[1]+i,tiles[1]);
		}
		//System.out.println(i);
		printMap();
		
		//TODO load pixels to raster
	}
	
	/**
	 * For testing purpose
	 */
	private void printMap(){
		for(int i = 0;i < tiles[0];i++){
			for(int ii = 0; ii < tiles[1];ii++)
				System.out.print(imgMap[i][ii]+",");
			
			System.out.print('\n');
		}
	}
}
