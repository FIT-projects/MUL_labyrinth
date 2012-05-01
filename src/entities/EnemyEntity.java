/**
 * 
 */
package entities;

import game.RenderWindow;

import java.awt.Image;

import library.MapLibrary;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Enemy entity (for testing purpose now)
 */
public class EnemyEntity extends AbstractEntity {

	public EnemyEntity(int locX, int locY, MapLibrary map, Image avatar, RenderWindow window){
		super(locX, locY, map, avatar, window);
		
		window.addEntity(this);
	}
	/* (non-Javadoc)
	 * @see entities.AbstractEntity#move(int)
	 */
	@Override
	public void move(int direction) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see entities.AbstractEntity#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

}
