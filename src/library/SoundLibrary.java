/**
 * 
 */
package library;


import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Sound library for playing sounds.
 */
public class SoundLibrary {
	private static String path = "resources/sounds/"; //path for loading sound files 
	private HashMap<SoundNames,Long> timeout = new HashMap<SoundNames,Long>(); // timeout for repeating sounds
	// names of sounds for timeout
	private enum SoundNames{
		MOVE
	}
	
	
	/**
	 * Play move sound
	 * @param ms Time in milliseconds when this move sound can't be played (from this caller)
	 */
	public synchronized void playMove(int ms){
		if(timeout.containsKey(SoundNames.MOVE))
			if(timeout.get(SoundNames.MOVE) >= System.currentTimeMillis())
				return;
		
		timeout.put(SoundNames.MOVE, System.currentTimeMillis()+ms);
			
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							this.getClass().getResourceAsStream(path + "sfx.wav"));
					clip.open(inputStream);
					clip.start(); 
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}
