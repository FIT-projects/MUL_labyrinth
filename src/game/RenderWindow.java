/**
 * 
 */
package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

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
	private int[] halfTiles = new int[2];
	// starting position
	private int[] start = new int[2];
	// data store row[column]
	private BufferedImage innerWindow;
	private WritableRaster raster;
	
	// store image types
	private short[][] imgMap;
	// resolution of outer window
	private int[] outerRes = new int[2];
	// optimization
	private final int tileRes = Defaults.getImageResTile(); 
	
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
		start[0] = startX - tiles[0] / 2 + 1;
		start[1] = startY - tiles[1] / 2 - 1;
		outerRes[0] = numtilesX * tileRes;
		outerRes[0] = numtilesY * tileRes;
		// create pixel map
		innerWindow = new BufferedImage(tiles[0]*tileRes, 
				tiles[1]*tileRes, BufferedImage.TYPE_INT_ARGB);
		
		raster = innerWindow.getRaster();
		//raster = imgLib.createCompatibleRaster(tiles[0]*tileRes, tiles[1]*tileRes);

		// create image map
		imgMap = new short[tiles[0]][tiles[1]];
	}
	
	public void loadScreen(){
		// load image types from map
		System.out.println("X: " + tiles[0] + " Y: " + tiles[1]);
		
		for(int i = 0; i < tiles[0];i++){
			imgMap[i] = mSource.getTileLine(start[0], start[1]+i,tiles[1]);
		}
		
		Graphics2D g = innerWindow.createGraphics();
		
		System.out.println("Render:" + raster.getTransferType()+" SM: "+raster.getSampleModel());
		int res = Defaults.getImageResTile();
		for(int y = 0; y < tiles[1];y++){
			for(int x = 0; x < tiles[0];x++){
				g.drawImage(iSource.getImageTile(imgMap[x][y]), x*res, y*res, null);
				//raster.setDataElements(x*res, y*res, iSource.getRasterById(imgMap[x][y]));
				//System.out.println("X:"+x*res+ " maxX:"+ raster.getWidth() + " Y:"+y*res+" maxY:"+raster.getHeight());
				//break;
			}
			//break;
		}
		//raster.setDataElements(1*res, 0*res, iSource.getRasterById(imgMap[1][0]));
		
		/*
		int[] i = iSource.getPixelsById(0);
		System.out.println(i.length);
		raster.setPixels(0, 0, tileRes-1, tileRes-1, iSource.getPixelsById(0));
		*/
		/*
		Graphics2D g = innerWindow.createGraphics();
		
		g.drawImage(iSource.getImageTile(0), 0, 0, null);
		*/
	}
	
	
	public Image getImageToRender(){
		//innerWindow.setData(raster);
		//return(innerWindow.getSubimage(0, 0, outerRes[0]*tileRes, outerRes[1]*tileRes));
		return(innerWindow);
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
