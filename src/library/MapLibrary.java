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
	private List<Short> activeLevel = new ArrayList<Short>();
	private int levelWidth = 0;
	private int levelHeight = 0;
	private int screenColumns = 0;
	private int screenRows = 0; 
	private int toRender = 0; // number of tiles to render 
	private int activeTile = 0; // active tile for iteration
	private int startingTile = 0; // left up corner tile
	private int endingTile = 0; // right botton corner tile
	private int fig = Defaults.imgIdByImgName("figure");
	private short outOfMap = (short)Defaults.imgIdByImgName("rock");
	//private InnerWindow window;
	
	/**
	 * Inner class witch set on rendering window
	 *//*
	private class InnerWindow{
		public int rowNum = 0;
		public int colNum = 0;
		public short[][] window;
		
		public InnerWindow(int row, int col) {
			window = new short[row][col];
		}
		
		//TODO loading methods for move in rows and cols
	}
	*/
	public MapLibrary(){
	}
	
	public MapLibrary(int level){
		loadLevel(level);
	}
	/*
	private void defaultView(){
		screenColumns = Defaults.getAppResolutionX() / Defaults.getImageResTile();
		screenRows = Defaults.getAppResolutionY() / Defaults.getImageResTile();
		
		// set starting ending active tiles for rendering
		int counter = 0;
		for(Short id : activeLevel){
			if(id == fig){
				//TODO bad idea maybe all this will change
				activeTile = counter;
				int up = screenRows / 2;
				int left = screenColumns / 2;
				
				startingTile -= (levelColumns * up) + left;
				endingTile -= startingTile + activeTile;
				startingTile += activeTile;
				
				System.out.println(startingTile);
				System.out.println(activeTile);
				System.out.println(endingTile);
				
				break;
			}
			counter++;
		}
		
		activeTile = startingTile;
	}
	*/
	/*
	private void loadInner(){
		window = new InnerWindow(levelRows, levelColumns);
		
		//TODO loading to window
	}
	
	public void nextTile(){
		//TODO implement this
	}
	
	public int getActiveTile(){
		return activeTile;
	}
	
	public void resetActiveTile(){
		//TODO this will be changed
		activeTile = startingTile;
	}
	*/
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
			levelWidth = 0;
			boolean firstRow = true; 
			int data = stream.read();
			Short get;
			while(data != -1){
				if(data != '\n' && data != ' '){
					get = new Short((short)(data - '0'));
					activeLevel.add(get);
					//pocitani hodnot v prvnim radku
					if(firstRow){
						levelWidth++;
					}
					if(get == Defaults.mapNameToId("figure")){
						startingTile = activeLevel.size()-1;
					}
				}
				else if(data == '\n'){
					firstRow = false;
				}
				data = stream.read();
			}
		}
		catch(IOException e){
			System.err.print(e.toString());
			System.exit(1);
		}
		
		//has the map same number of columns in rows
		if(activeLevel.size() % levelWidth != 0){
			System.err.print("File have bad format");
			System.exit(1);
		}
		
		levelHeight = activeLevel.size() / levelWidth;
		// remap tiles from map to image id
		remap();
		// defaultView();
	}
	
	/**
	 * Remap locally map tile indexing to image tile indexing
	 */
	private void remap(){
		List<Short> tmp = new ArrayList<Short>();
		for(Short id : activeLevel){
			tmp.add((short) Defaults.getTileToImage((int)id));
		}
		
		activeLevel = tmp;
	}
	
	
	/* Getters and setters */
	//TODO comments
	public short getTile(int index){
		try{
			return(activeLevel.get(index));
		}
		catch (IndexOutOfBoundsException e) {
			return outOfMap;
		}
	}
	public short getTile(int X, int Y){
		try{
			return(activeLevel.get(Y*levelWidth+X));
		}
		catch (IndexOutOfBoundsException e) {
			return(outOfMap);
		}
	}
	public short[] getTileLine(int X,int Y, int num){
		short[] ret = new short[num+1];

		//bad row
		if(Y < 0 || Y > levelHeight){
			for(int i = 0; i < num; i++)
				ret[i] = outOfMap;
			
			return ret;
		}
		
		int a = 0;
		// column out of range
		if(X < 0){
			for(a = 0;X < 0; X++,a++){
				ret[a] = outOfMap;
				if(a >= num)
					return ret;
			}
		}
		System.out.println(Y*levelWidth+X + " a:" + a+" num:"+num);
		for(int i = Y*levelWidth+X, c = 0;a < num; i++, a++,c++){
			if(c > levelWidth) //column out of range
				ret[a] = outOfMap;
			else{
				try{
					ret[a] = activeLevel.get(i);
				}catch (IndexOutOfBoundsException e) {
					ret[a] = outOfMap;
				}
			}
		}
		
		return ret;
	}

	public int getToRender() {
		return toRender;
	}

	public void setToRender(int toRender) {
		this.toRender = toRender;
	}
	
	/**
	 * Get starting location of player in map
	 * @return format [X,Y]
	 */
	public int[] getStartLocation(){
		int[] ret = new int[2];
		
		ret[0] = startingTile / levelWidth;
		ret[1] = startingTile - (levelWidth * ret[0]);

		return ret;
	}
}
