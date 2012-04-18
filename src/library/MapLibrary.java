/**
 * 
 */
package library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 */
public class MapLibrary {
	final private String[] mapFile = {"resources/level0.map"};
	private Vector<Short> activeLevel = new Vector<Short>();
	private int levelColumns = 0;  
	
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
			stream = new FileInputStream(mapFile[0]);
		}
		catch(FileNotFoundException e)
		{
			System.err.print(e.toString());
			System.exit(1);
		}
		
		//read the file
		try{
			levelColumns = 0;
			boolean firstRow = true; 
			int data = stream.read();
			while(data != -1){
				if(data != '\n' && data != ' '){
					activeLevel.add(new Short((short)(data - '0')));
					//pocitani hodnot v prvnim radku
					if(firstRow){
						levelColumns++;
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
		if(activeLevel.size() % levelColumns != 0){
			System.err.print("File have bad format");
			System.exit(1);
		}
	}
	
	
	public short getTile(int index){
		return(activeLevel.get(index));
	}
}
