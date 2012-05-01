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
	private ArrayList<Entities> ent = new ArrayList<Entities>(); // loaded entities in map
	private int[] startingTile = new int[2]; // player start position
	private int fig = Defaults.getMapNameToId("figure");
	private int enemy = Defaults.getMapNameToId("enemy");
	private short outOfMap = (short)Defaults.getImgIdByImgName("rock");
	private short underPlayer = (short)Defaults.getImgUnderPlayer();
	
	public class Entities{
		public int locX;
		public int locY;
		public int imgID;
		public Entities(int x, int y, int imgID){
			this.locX = x;
			this.locY = y;
			this.imgID = imgID;
		}
	}

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
					else if(get == enemy){
						ent.add(new Entities(row.size(), activeLevel.size(), Defaults.getTileToImage(get)));
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
		
	/**
	 * Return row from active map
	 * @param X axis from where
	 * @param Y axis from where
	 * @param num number of elements to get
	 * @return row of map
	 */
	public short[] getTileLine(int X,int Y, int num){
		short[] ret = new short[num+1];

		try{ // out of map at Y
			List<Short> row = activeLevel.get(Y); 
			
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
	 * Return column from active map
	 * Need X,Y of top point from where start read
	 * 
	 * @param X axis from where to read
	 * @param Y axis from where to read
	 * @param num number of elements to get
	 * @return column of map
	 */
	public short[] getTileColumn(int X,int Y, int num){
		short[] ret = new short[num+1];
		
		// out of map
		if(X < 0){
			for(int i = 0; i < num; i++){
				ret[i] = outOfMap;
			}
			return ret;
		}

		// from where
		for(int i = Y, a = 0; a < num;i--, a++){
			ret[a] = (short) getTile(X, i);
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
		return ent.size() + 1; // plus 1 for player
	}
	
	public Entities[] getLevelEntities(){
		return ent.toArray(new Entities[0]);
	}
}
