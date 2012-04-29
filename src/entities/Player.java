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
 * Player figure entity
 */
public class Player extends AbstractEntity {
	private int block = Defaults.getImgBlockTile();
	private boolean moved = false;
	
	public Player(int locX, int locY, MapLibrary map, Image avatar, RenderWindow window){
		locTile[0] = locX;
		locTile[1] = locY;
		this.map = map;
		this.avatar = avatar;
		this.window = window;
		
		window.addEntity(this);
	}
	
	
	public void move(int direction){
		if((direction & UP) == UP){
			locPix[1]--;
			moved = true;
			if(locPix[1] / resTile <= -1){
				locPix[1] = 0;
				locTile[1]--;
			}
			System.out.println("loc:" + locPix[1]);
		}
		if((direction & DOWN) == DOWN){
			locPix[1]++;
			moved = true;
			if(locPix[1] / resTile >= 1){
				locPix[1] = 0;
				locTile[1]++;
			}
		}
		if((direction & RIGHT) == RIGHT)
		{
			locPix[0]++;
			moved = true;
			if(locPix[0] / resTile >= 1){
				locPix[0] = 0;
				locTile[0]++;
			}
		}
		if((direction & LEFT) == LEFT){
			locPix[0]--;
			moved = true;
			if(locPix[0] / resTile <= -1){
				locPix[0] = 0;
				locTile[0]--;
			}
		}
		
		if(moved){
			moved = false;
			window.addEntity(this);
		}
	}
	
}
