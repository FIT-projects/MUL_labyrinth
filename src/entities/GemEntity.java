package entities;

import game.RenderWindow;

import java.awt.Image;

import library.MapLibrary;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 *
 * Gem entity.
 */
public class GemEntity extends AbstractEntity {

	public GemEntity(int locX, int locY, MapLibrary map, Image avatar, RenderWindow window) {
		super(locX, locY, map, avatar, window);
		// TODO Auto-generated constructor stub
		
		window.addEntity(this);
	}

	@Override
	public void move(int direction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

}
