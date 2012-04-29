/**
 * 
 */
package library;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 */
public class MapLibrary {
	final private String[] mapFile = Defaults.getMapFile();
	private List<List<Short>> activeLevel = new ArrayList<List<Short>>();
	private int levelWidth = 0;
	private int levelHeight = 0;
	private int[] startingTile = new int[2]; // player start position
	private int fig = Defaults.getMapNameToId("figure");
	private short outOfMap = (short)Defaults.getImgIdByImgName("rock");
	private short underPlayer = (short)Defaults.getImgUnderPlayer();
	

	public MapLibrary(){
	}
	
	public MapLibrary(int level){
		loadLevel(level);
	}


	/**
	 * Load level file and set it as active
	 * @param level Id of the level
	 */
	public void loadLevel(int level){
		InputStream stream = null;
		//open file
		try
		{
			stream = this.getClass().getResourceAsStream(mapFile[0]);
		}
		catch(NullPointerException e)
		{
			System.err.print(e.toString());
			System.exit(1);
		}
		
		//read the file
		try{
			int data = stream.read();
			activeLevel.add(new ArrayList<Short>());
			List<Short> row = new ArrayList<Short>();
			Short get;
			while(data != -1){
				if(data != '\n' && data != ' '){
					get = new Short((short)(data - '0'));

					if(get == fig){
						startingTile[1] = activeLevel.size();
						startingTile[0] = row.size();
						row.add(underPlayer);
					}
					else
						row.add((short) Defaults.getTileToImage(get));
				}
				else if(data == '\n'){
					activeLevel.add(row);
					row = new ArrayList<Short>();
				}
				data = stream.read();
			}
			activeLevel.add(row);
		}
		catch(IOException e){
			System.err.print(e.toString());
			System.exit(1);
		}
		
		levelWidth = activeLevel.get(0).size()-1;
		levelHeight = activeLevel.size()-1;
		
		//has the map same number of columns in rows
		if(activeLevel.size() % levelWidth != 0){
			System.err.print("File have bad format");
			System.exit(1);
		}

	}
		
	
	public short[] getTileLine(int X,int Y, int num){
		short[] ret = new short[num+1];

		try{
			List<Short> row = activeLevel.get(Y); // out of map at Y
			
			for(int i = X, a = 0; a < num; i++, a++){
				try{
					ret[a] = row.get(i);
				} catch(IndexOutOfBoundsException e){
					ret[a] = outOfMap;
				}
				
			}
			 
			
		} catch (IndexOutOfBoundsException e) {
			for(int i = 0; i < num; i++)
				ret[0] = outOfMap;
		}
		
		return ret;
	}
	
	/**
	 * Get value on tile X, Y at the map
	 * @param X location X coordinate of tile
	 * @param Y location Y coordinate of tile
	 * @return image id
	 */
	public int getTile(int X, int Y){
		try{
			return activeLevel.get(Y).get(X);
		} catch (IndexOutOfBoundsException e) {
			return outOfMap;
		}
	}
	
	/**
	 * Get starting location of player in map
	 * @return format [X,Y]
	 */
	public int[] getStartLocation(){
		return startingTile;
	}

	public int getLevelWidth() {
		return levelWidth;
	}

	public int getLevelHeight() {
		return levelHeight;
	}
	
	/**
	 * Get number of entities in actual level
	 * @return number of entities
	 */
	public int getNumberEntLvl(){
		return 1;
	}
}
