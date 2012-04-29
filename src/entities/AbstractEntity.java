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
	protected int[] locTile = new int[2];
	//relative pixel location from tile (upper left corner)
	protected int[] locPix = {0,0};
	protected int resTile = Defaults.getImageResTile();
	
	// constants
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 4;
	public static final int LEFT = 8;
	
	public Image getAvatarImage(){
		return avatar;
	}
	
	public int[] getTileLocation(){
		return locTile;
	}
	
	public int[] getPixelLocation(){
		return locPix;
	}
	
	public abstract void move(int direction);
}
