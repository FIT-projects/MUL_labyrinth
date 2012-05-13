/**
 * 
 */
package entities;

import game.RenderWindow;

import java.awt.Image;
import java.util.Random;

import library.MapLibrary;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Enemy entity (for testing purpose now)
 */
public class EnemyEntity extends AbstractEntity {
	
	private int lastDirection;
	private Random rnd = new Random();
	
	public EnemyEntity(int locX, int locY, MapLibrary map, Image avatar, RenderWindow window){
		super(locX, locY, map, avatar, window);
		
		window.addEntity(this);
		
		lastDirection = getRandomDirection();	
		speed = 1;
	}
	/* (non-Javadoc)
	 * @see entities.AbstractEntity#move(int)
	 */
	@Override
	public void move(int direction) {
		// TODO Auto-generated method stub
		
		// move up
		if((direction & UP) == UP){
			locPix[1] -= speed;
			if(locPix[1] / resTile <= -1){
				locPix[1] = 0;
				locTile[1]--;
			}
		}
		// move down
		if((direction & DOWN) == DOWN){
			locPix[1] += speed;
			if(locPix[1] / resTile >= 1){
				locPix[1] = 0;
				locTile[1]++;
			}
		}
		// move right
		if((direction & RIGHT) == RIGHT){
			locPix[0] += speed;
			if(locPix[0] / resTile >= 1){
				locPix[0] = 0;
				locTile[0]++;
			}
		}
		// move left
		if((direction & LEFT) == LEFT){
			locPix[0] -= speed;
			if(locPix[0] / resTile <= -1){
				locPix[0] = 0;
				locTile[0]--;
			}
		}
	}

	/* (non-Javadoc)
	 * @see entities.AbstractEntity#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		int playerX = 0, playerY = 0;
		Player pl = null;
		
		for (AbstractEntity a : window.getEntities()) {
			if (a instanceof Player) {
				playerX = a.getTileLocation()[0];
				playerY = a.getTileLocation()[1];
				pl = (Player) a;
				break;
			}				
		}
		
		if (playerX == locTile[0] && playerY == locTile[1])
			pl.hurt();
		
		int direction = collisionDetection(lastDirection);
		
		if(direction == 0) {
			int newDir = getRandomDirection();
			move(newDir);			
			lastDirection = newDir;
		}
		else {
			
			int r = rnd.nextInt(2);
			if (r == 1)
			{
				if (locTile[1] == playerY && (direction == UP || direction == DOWN))
				{
					int diff = playerX - locTile[0];
					if (diff < 0 && diff > -5)
					{
						move(LEFT);
						lastDirection = LEFT;
					}
					else if (diff > 0 && diff < 5)
					{
						move(RIGHT);
						lastDirection = RIGHT;
					}
					else
						move(direction);					
				}
				else if (locTile[0] == playerX && (direction == LEFT || direction == RIGHT))
				{	
					int diff = playerY - locTile[1];
					if (diff < 0 && diff > -5)
					{
						move(UP);
						lastDirection = UP;
					}
					else if (diff > 0 && diff < 5)
					{
						move(DOWN);
						lastDirection = DOWN;
					}
					else
						move(direction);	
					
				}
				else
					move(direction);					
			}
			else
			{
				move(direction);				
			}
		}
	}
	
	private int getRandomDirection() {
		int rndDir = rnd.nextInt(4);
		switch (rndDir) {
			case 0: return UP;
			case 1: return DOWN;
			case 2: return LEFT;
			case 3: return RIGHT;		
		}
		return 0;
	}

}
