/**
 * 
 */
package entities;

import game.RenderWindow;

import java.awt.Image;

import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;

import library.Defaults;
import library.MapLibrary;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Player figure entity
 */
public class Player extends AbstractEntity {
	private boolean moved = false;
	
	public Player(int locX, int locY, MapLibrary map, Image avatar, RenderWindow window){
		locTile[0] = locX;
		locTile[1] = locY;
		this.map = map;
		this.avatar = avatar;
		this.window = window;
		
		window.addEntity(this);
	}
	
	@Override
	public void move(int direction){
		direction = collisionDetection(direction); // remove collisions
		
		// no move
		if(direction == 0)
			return;
		
		// move processing
		int[] nextTile = {0,0};
		boolean moveTile = false;
		
		// move up
		if((direction & UP) == UP){
			locPix[1] -= speed;
			if(locPix[1] / resTile <= -1){
				moveTile = true;
				locPix[1] = 0;
				locTile[1]--;
				nextTile[1]--;
			}
		}
		// move down
		if((direction & DOWN) == DOWN){
			locPix[1] += speed;
			if(locPix[1] / resTile >= 1){
				moveTile = true;
				locPix[1] = 0;
				locTile[1]++;
				nextTile[1]++;
			}
		}
		// move right
		if((direction & RIGHT) == RIGHT){
			locPix[0] += speed;
			if(locPix[0] / resTile >= 1){
				moveTile = true;
				locPix[0] = 0;
				locTile[0]++;
				nextTile[0]++;
			}
		}
		// move left
		if((direction & LEFT) == LEFT){
			locPix[0] -= speed;
			if(locPix[0] / resTile <= -1){
				moveTile = true;
				locPix[0] = 0;
				locTile[0]--;
				nextTile[0]--;
			}
		}
		

		if(moveTile)
			window.followPlayer(nextTile[0],nextTile[1]);
	}
	
	@Override
	public void action(){
		
	}
	
}
