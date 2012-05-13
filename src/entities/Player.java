/**
 * 
 */
package entities;

import game.RenderWindow;

import java.awt.Image;

import library.Defaults;
import library.MapLibrary;
import library.SoundLibrary;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Player figure entity
 */
public class Player extends AbstractEntity {
	private boolean justExited = false;
	private int health = Defaults.getMaxHealth();
	private int collectedGemCount = 0;
	private int gemCount;
	private long lastHurtMs = 0;
	private long endTimeMs = 0;
	
	public Player(int locX, int locY, MapLibrary map, Image avatar, RenderWindow window, int gemCount){
		super(locX, locY, map, avatar, window);
		
		name = "Player";
		this.gemCount = gemCount; 
		
		window.addEntity(this);

	}
	
	@Override
	public void move(int direction){
		if (health <= 0 || justExited)
			return;		
		
		direction = collisionDetection(direction); // remove collisions
		//justExited = false;
		
		// no move
		if(direction == 0)
			return;
		
		// move processing
		int[] nextTile = {0,0};
		boolean moveTile = false;
		//moved = true;
		
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
		

		if(moveTile){
			System.out.println("Actual tile: " + map.getTile(locTile[0], locTile[1]) + " " + locTile[0] + " " + locTile[1]);
			window.followPlayer(nextTile[0],nextTile[1]);
			
			if (map.getTile(locTile[0], locTile[1]) == 16)
			{
				if (collectedGemCount == gemCount)
				{
					justExited = true;
					endTimeMs = System.currentTimeMillis();
					SoundLibrary.play(SoundLibrary.SoundNames.EXIT, 0);
				}
				else
					SoundLibrary.play(SoundLibrary.SoundNames.LOCKED, 0);
					
			}
			
			AbstractEntity todel = null;
			for (AbstractEntity a : window.getEntities())
			{
				if (a instanceof GemEntity)
				{
					if (a.getTileLocation()[0] == locTile[0] && a.getTileLocation()[1] == locTile[1])
					{
						SoundLibrary.play(SoundLibrary.SoundNames.GEM, 0);
						todel = a;					
						System.out.println("gem");
						collectedGemCount++;
						
					}
				}
			}		
			window.removeEntity(todel);
		}	
	}
	
	@Override
	public void action(){

	}
	
	public boolean exited()
	{
		return justExited;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public int getCollectedGemCount()
	{
		return collectedGemCount;
	}
	
	public void hurt()
	{
		if (System.currentTimeMillis() - lastHurtMs > Defaults.getHurtInterval())		
		{
			health--;
			if (health == 0)
				SoundLibrary.play(SoundLibrary.SoundNames.DEATH, 300);
			else
				SoundLibrary.play(SoundLibrary.SoundNames.LIVELOST, 300);
			
			lastHurtMs = System.currentTimeMillis();
		}
	}
	
	public long getEndTime() {
		return endTimeMs;
	}
	
	public void resetEndTime() {
		endTimeMs = 0;
	}
	
	
	
}
