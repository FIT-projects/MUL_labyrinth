/**
 * 
 */
package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	// player location and pixel location
	private int[] locP = {0,0};
	private int[] pixP = {0,0};
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
		minmaxY[1] = minmaxY[0] + tiles[1];
		System.out.println("minX:" + minmaxX[0] + " maxX:" + minmaxX[1]);
		System.out.println("minY:" + minmaxY[0] + " maxY:" + minmaxY[1]);
		
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
		System.out.println("Tiles X: " + tiles[0] + " Y: " + tiles[1]);
		
		for(int i = 0; i < tiles[0];i++){
			imgMap[i] = mSource.getTileLine(minmaxX[0], minmaxY[0]+i,tiles[1]);
		}

		drawBackground();
		printMap();
		
		renderEntities();

	}
	
	/**
	 * Move screen to other tile (no need for tile change)
	 * @param x +/- change in x axis
	 * @param y +/- change in y axis
	 */
	public void followPlayer(int x, int y){
		System.out.println("move tile: " + x + " " + y);
		short[][] newMap = new short[tiles[1]][tiles[0]];
		
		// move two directions at once
		if(x != 0 && y != 0){
			minmaxX[0] += x;
			minmaxX[1] += x;
			minmaxY[0] += y;
			minmaxY[1] += y;
			
			for(int i = 0; i < tiles[0];i++){
				newMap[i] = mSource.getTileLine(minmaxX[0], minmaxY[0]+i,tiles[1]);
			}
		}
		
		// change X tile - we need load new column
		else if(x != 0){
			minmaxX[0] += x;
			minmaxX[1] += x;
			
			if(x < 0){ // move to left
				for(int i = 0; i < tiles[1]; i++){
					newMap[i][0] = (short) mSource.getTile(minmaxX[0], minmaxY[0]+i);
					for(int a = 1; a < tiles[1]; a++){
						newMap[i][a] = imgMap[i][a-1];
					}
				}
			}
			else{ // move to right
				for(int i = 0; i < tiles[1]; i++){
					for(int a = 0; a < (tiles[1]-1); a++){
						newMap[i][a] = imgMap[i][a+1];
					}
					newMap[i][tiles[1]-1] = (short) mSource.getTile(minmaxX[1]-1, minmaxY[0]+i);
				}
			}
			
		}
		// change Y tile - we need load new row
		else if(y != 0){
			minmaxY[0] += y;
			minmaxY[1] += y;
			
			if(y < 0){ // move up
				newMap[0] = mSource.getTileLine(minmaxX[0], minmaxY[0], tiles[1]);
				
				for(int i = 1; i < tiles[1]; i++){
					newMap[i]  = imgMap[i-1];
				}
			}
			else{ // move down
				for(int i = 0; i < (tiles[1]-1); i++){
					newMap[i]  = imgMap[i+1];
				}
				
				newMap[tiles[0]-1] = mSource.getTileLine(minmaxX[0], minmaxY[1]-1, tiles[1]);
			}
			
			
		}
		
		imgMap = newMap;
		//draw new background
		drawBackground();
	}
	
	/**
	 * Draw background image
	 */
	private void drawBackground(){
		Graphics2D g = background.createGraphics();
		
		int res = Defaults.getImageResTile();
		for(int y = 0; y < tiles[1];y++){
			for(int x = 0; x < tiles[0];x++){
					g.drawImage(iSource.getImageTile(imgMap[y][x]), x*res, y*res, null);
				
			}

		}
		
		g.dispose();
	}
	
	/**
	 * Get final image to render
	 * @return image to render on JPanel
	 */
	public Image getImageToRender(){
		//innerWindow.setData(raster);
		return(finalImg.getSubimage(tileRes+pixP[0], tileRes+pixP[1], 
				background.getWidth()-tileRes-pixP[0], background.getHeight()-tileRes-pixP[1]));
	}
	
	/**
	 * Render entities in window
	 */
	public void renderEntities(){
		Graphics2D g2 = finalImg.createGraphics();
		g2.drawImage(background, 0, 0, null);
		
		for(AbstractEntity e : ent){
			if(e instanceof Player){
				locP = e.getTileLocation();
				pixP = e.getPixelLocation();
				g2.drawImage(e.getAvatarImage(), ((locP[0] - minmaxX[0])*tileRes)+pixP[0], 
						((locP[1] - minmaxY[0])*tileRes)+pixP[1], null);
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
	
	public ArrayList<AbstractEntity> getEntities()
	{
		return ent;		
	}
}
