/**
 * 
 */
package utilities;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;


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

		/**
		 * For serialization (warning removal)
		 */
		private static final long serialVersionUID = -7092372863911694169L;

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
		
		/**
		 * For serialization (warning removal)
		 */
		private static final long serialVersionUID = 5639324084068547278L;

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
	
	// for control game -> end game, main menu, mute etc...
	private static Action gameControlPressed = new AbstractAction() {
		
		/**
		 * For serialization (warning removal)
		 */
		private static final long serialVersionUID = -502277761389348263L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().hashCode() == KeyEvent.VK_ESCAPE){
				keyPressed.put(GameKeys.ESC, e.getWhen());
				lastReleased.put(GameKeys.ESC, false);
			}
			
		}
	};
	
	private static Action gameControlReleased = new AbstractAction() {
		
		/**
		 * For serialization (warning removal)
		 */
		private static final long serialVersionUID = 7577706256236043935L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().hashCode() == KeyEvent.VK_ESCAPE){
				keyReleased.put(GameKeys.ESC, e.getWhen());
				lastReleased.put(GameKeys.ESC, true);
			}
			
		}
	};
	
	public static Action getActionMovePressed(){
		return movePressed;
	}
	public static Action getActionMoveReleased(){
		return moveReleased;
	}
	
	public static Action getActionControlPressed(){
		return gameControlPressed;
	}
	public static Action getActionControlReleased(){
		return gameControlReleased;
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
	
	/**
	 * Was key released
	 * @param key is this key still pressed
	 * @return true if it was released
	 */
	public static boolean wasReleased(GameKeys key){
		for(GameKeys k : wasReleased){
			if(k == key)
				return true;
		}
		
		return false;
	}
}
