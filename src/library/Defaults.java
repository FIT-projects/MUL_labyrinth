/**
 * 
 */
package library;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
	private static final String[] mapFile = {"resources/level0.map","resources/level1.map"};
	private static final String imageFile = "resources/terrain.png";
	private static final int imageResTile = 32;
	private static final String freeTile = "dirt";
	private static final String blockTile = "rock";
	private static final String underPlayer = "dirt";
	private static final String player = "figure";
	private static final Map<String,Integer> imgNameToId;
	private static final Map<String,Integer> mapNameToId;
	private static final Map<Integer,Integer> tileRemap; // remap tiles from map to image tiles
	
	private static final int maxHealth = 5;
	private static final int playerHurtIntervalMs = 1500;
	private static final boolean renderAlways = true;
	
	
	static {
		// only for better change
		// mapID , imageID
		int[] tmpRock = {1,0};
		int[] tmpDirt = {0,1};
		int[] tmpFigure = {9,17};
		int[] tmpEnemy = {8,18};
		int[] tmpGem = {7,2};
		int[] tmpExit = {6,16};
		
		// map id to image id
		Map<Integer,Integer> temp = new HashMap<Integer,Integer>();
		temp.put(tmpRock[0], tmpRock[1]);
		temp.put(tmpDirt[0], tmpDirt[1]);
		temp.put(tmpFigure[0], tmpFigure[1]);
		temp.put(tmpEnemy[0], tmpEnemy[1]); 
		temp.put(tmpGem[0], tmpGem[1]);
		temp.put(tmpExit[0], tmpExit[1]);
		tileRemap = Collections.unmodifiableMap(temp);
		
		//tile name to id at image
		Map<String,Integer> temp2 = new HashMap<String,Integer>();
		temp2.put("rock", tmpRock[1]);
		temp2.put("dirt", tmpDirt[1]);
		temp2.put("figure", tmpFigure[1]);
		temp2.put("enemy", tmpEnemy[1]);
		temp2.put("gem", tmpGem[1]);
		temp2.put("exit", tmpExit[1]);
		imgNameToId = Collections.unmodifiableMap(temp2);
		
		//tile name to id at map
		Map<String,Integer> temp3 = new HashMap<String,Integer>();
		temp3.put("rock", tmpRock[0]);
		temp3.put("dirt", tmpDirt[0]);
		temp3.put("figure", tmpFigure[0]);
		temp3.put("enemy", tmpEnemy[0]);
		temp3.put("gem", tmpGem[0]);
		temp3.put("exit", tmpExit[0]);
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
	public static String getMoveTileName() {
		return freeTile;
	}
	public static int getMoveTileIndex() {
		return imgNameToId.get(freeTile);
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
	public static int getImgIdByImgName(String name){
		return imgNameToId.get(name);
	}
	public static int getMapNameToId(String name) {
		return mapNameToId.get(name);
	}
	
	public static int getImgUnderPlayer(){
		return imgNameToId.get(underPlayer);
	}
	
	public static int getImgBlockTile(){
		return imgNameToId.get(blockTile);
	}
	
	public static int getImgPlayerId(){
		return imgNameToId.get(player);
	}
	
	public static int getMaxHealth() {
		return maxHealth;
	}
	
	public static int getHurtInterval() {
		return playerHurtIntervalMs;
	}
	
	public static boolean getRenderAlways() {
		return renderAlways;
	}
}
