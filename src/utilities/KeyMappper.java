/**
 * 
 */
package utilities;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Key mapping class, store keys and releasing them
 */
public class KeyMappper {
	private static final int isAutorepeat = 5; // timeout for autorepeat (some systems need this)
	private static HashMap<GameKeys,Long> keyReleased = new HashMap<GameKeys,Long>();
	private static HashMap<GameKeys,Long> keyPressed = new HashMap<GameKeys,Long>();
	private static TreeSet<GameKeys> wasReleased = new TreeSet<GameKeys>();
	private static TreeSet<GameKeys> isPressed = new TreeSet<GameKeys>();
	private static HashMap<GameKeys,Boolean> lastReleased = new HashMap<GameKeys,Boolean>();
	
	// Abstract action key pressed for move action
	private static Action movePressed = new AbstractAction(){

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand().equals("w")){
				keyPressed.put(GameKeys.W, e.getWhen());
				lastReleased.put(GameKeys.W, false);
			}
			if(e.getActionCommand().equals("s")){
				keyPressed.put(GameKeys.S, e.getWhen());
				lastReleased.put(GameKeys.S, false);
			}
			if(e.getActionCommand().equals("a")){
				keyPressed.put(GameKeys.A, e.getWhen());
				lastReleased.put(GameKeys.A, false);
			}
			if(e.getActionCommand().equals("d")){
				keyPressed.put(GameKeys.D, e.getWhen());
				lastReleased.put(GameKeys.D, false);
			}
		}
		
	};
	// Abstract action key released for move action
	private static Action moveReleased = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand().equals("w")){
				keyReleased.put(GameKeys.W, e.getWhen());
				lastReleased.put(GameKeys.W, true);
			}
			
			if(e.getActionCommand().equals("s")){
				keyReleased.put(GameKeys.S, e.getWhen());
				lastReleased.put(GameKeys.S, true);
			}
			
			if(e.getActionCommand().equals("a")){
				keyReleased.put(GameKeys.A, e.getWhen());
				lastReleased.put(GameKeys.A, true);
			}
			if(e.getActionCommand().equals("d")){
				keyReleased.put(GameKeys.D, e.getWhen());
				lastReleased.put(GameKeys.D, true);
			}
		
			//System.out.println("Released " + e.getActionCommand());
		}
	};
	
	public static Action getActionMovePressed(){
		return movePressed;
	}
	public static Action getActionMoveReleased(){
		return moveReleased;
	}
	
	/**
	 * Process key events 
	 * Must be called before getting any keys
	 */
	public static void processKeys(){
		wasReleased.clear();
		isPressed.clear();
		long time = System.currentTimeMillis();
		
		for(java.util.Map.Entry<GameKeys, Long> e : keyPressed.entrySet()){
			if(keyReleased.containsKey(e.getKey())){
				// key was released
				if(((time - keyReleased.get(e.getKey())) > isAutorepeat) && lastReleased.get(e.getKey()))
					wasReleased.add(e.getKey());
				else
					isPressed.add(e.getKey());
			}
			else
				isPressed.add(e.getKey());
		}
		
		for(GameKeys k : wasReleased){
			keyPressed.remove(k);
			keyReleased.remove(k);
		}

	}
	
	/**
	 * Get pressed keys
	 * @return TreeSet of GameKeys
	 */
	public static TreeSet<GameKeys> getPressed(){
		return isPressed;
	}
	
	/**
	 * Get released keys
	 * @return TreeSet of GameKeys
	 */
	public static TreeSet<GameKeys> getReleased(){
		return wasReleased;
	}
}
