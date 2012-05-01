/**
 * 
 */
package entities;

import game.RenderWindow;

import java.awt.Image;

import library.Defaults;
import library.MapLibrary;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Abstract entity class, for storing enemies, player ...
 */
public abstract class AbstractEntity {
	protected Image avatar;
	protected RenderWindow window;
	protected MapLibrary map;
	protected String name;
	protected int[] locTile = new int[2];
	//relative pixel location from tile (upper left corner)
	protected int[] locPix = {0,0};
	protected int resTile = Defaults.getImageResTile();
	protected int speed = 3;
	protected int blocker = Defaults.getImgBlockTile();
	
	// constants
	// used to move with entity, sum this int's for requested direction
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 4;
	public static final int LEFT = 8;
	
	
	public AbstractEntity(int locX, int locY, MapLibrary map, Image avatar, RenderWindow window){
		locTile[0] = locX;
		locTile[1] = locY;
		this.map = map;
		this.avatar = avatar;
		this.window = window;
	}
	
	/**
	 * Get Image for render this entity
	 * @return Image for render
	 */
	public Image getAvatarImage(){
		return avatar;
	}
	
	/**
	 * Location of tile where this entity is
	 * @return location X and Y
	 */
	public int[] getTileLocation(){
		return locTile;
	}
	
	/**
	 * Relative pixel location of this entity
	 * Length from upper left corner from his tile
	 * @return relative pixel location X, Y
	 */
	public int[] getPixelLocation(){
		return locPix;
	}
	
	/**
	 * Move with this entity
	 * @param direction - direction where we move to, it is sum of directions
	 */
	public abstract void move(int direction);
	
	/**
	 * Collision detection and correction
	 * @param direction input direction
	 * @return correct direction
	 */
	protected int collisionDetection(int direction){

		for(int mod = -1; mod <= 1; mod++){
		
			if(mod == 0 || (locPix[0] < -5 && mod == -1) || (locPix[0] > 5 && mod == 1))
			{
			
				// collision up?
				if((direction & UP) == UP){
					if(map.getTile(locTile[0]+mod, locTile[1]-1) == blocker){
						if(locPix[1] <= 0){
							direction -= UP;
							locPix[1] = 0;
						}
					}
				}
				
				// collision down?
				if((direction & DOWN) == DOWN){
					if(map.getTile(locTile[0]+mod, locTile[1]+1) == blocker){
						if(locPix[1] >= 0){
							direction -= DOWN;
							locPix[1] = 0;
						}
					}
				}
			}
			
			if(mod == 0 || (locPix[1] < -5 && mod == -1) || (locPix[1] > 5 && mod == 1))
			{
				// collision left
				if((direction & LEFT) == LEFT){
					if(map.getTile(locTile[0]-1, locTile[1]+mod) == blocker){
						if(locPix[0] <= 0){
							direction -= LEFT;
							locPix[0] = 0;
						}
					}
				}
				
				// collision right
				if((direction & RIGHT) == RIGHT){
					if(map.getTile(locTile[0]+1, locTile[1]+mod) == blocker){
						if(locPix[0] >= 0){
							direction -= RIGHT;
							locPix[0] = 0;
						}
					}
				}
			}
		}
		
		// return directions without collisions
		return direction;
	}
	
	/**
	 * Do needed action, AI computing or so
	 */
	public abstract void action();
	
	
}
