/**
 * 
 */
package library;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import utilities.*;
import utilities.ExceptionNameNotFound;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Default values of the game
 */
public class Defaults {
	private static final int appResolutionX = 544;
	private static final int appResolutionY = 544;
	private static final String[] mapFile = {"resources/level0.map"};
	private static final String imageFile = "resources/terrain.png";
	private static final Map<String,Integer> imgNameToId;
	private static final Map<String,Integer> mapNameToId;
	private static final int imageResTile = 32;
	private static final Map<Integer,Integer> tileRemap; // remap tiles from map to image tiles
	
	static {
		// tile id to image tile id
		Map<Integer,Integer> temp = new HashMap<Integer,Integer>();
		temp.put(1, 0);
		temp.put(0, 2);
		temp.put(5, 28); 
		tileRemap = Collections.unmodifiableMap(temp);
		
		//tile name to id
		Map<String,Integer> temp2 = new HashMap<String,Integer>();
		temp2.put("rock", 0);
		temp2.put("dirt", 2);
		temp2.put("figure", 28);
		imgNameToId = Collections.unmodifiableMap(temp2);
		
		//tile name to id
		Map<String,Integer> temp3 = new HashMap<String,Integer>();
		temp3.put("rock", 1);
		temp3.put("dirt", 0);
		temp3.put("figure", 5);
		mapNameToId = Collections.unmodifiableMap(temp3);
	}
	
	/* Getters and setters */
	public static int getAppResolutionX() {
		return appResolutionX;
	}
	public static int getAppResolutionY() {
		return appResolutionY;
	}
	public static String[] getMapFile() {
		return mapFile;
	}
	public static String getImageFile() {
		return imageFile;
	}
	public static int getImageResTile() {
		return imageResTile;
	}
	public static int getTileToImage(int id){
		return tileRemap.get(id);
	}
	public static String getImgNameByImgId(int id) throws ExceptionNameNotFound{
		for(Map.Entry<String,Integer> ent : imgNameToId.entrySet()){
			if(ent.getValue() == id)
				return ent.getKey();
		}
		throw new ExceptionNameNotFound();
	}
	public static int imgIdByImgName(String name){
		return imgNameToId.get(name);
	}
	public static int mapNameToId(String name) {
		return mapNameToId.get(name);
	}
	
}
