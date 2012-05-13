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
	private static SoundLibrary instance;
	private static String path = "resources/sounds/"; //path for loading sound files 
	private HashMap<SoundNames,Long> timeout = new HashMap<SoundNames,Long>(); // timeout for repeating sounds
	
	// names of sounds for timeout
	public enum SoundNames{
		GEM, LOCKED, EXIT, LIVELOST, DEATH 
	}	
	
	private SoundLibrary() {}
	
	private String GetClipPath(SoundNames clip)
	{
		switch (clip) {
			case GEM:      return path + "gem.wav";
			case LOCKED:   return path + "locked.wav";
			case EXIT:     return path + "exit.wav";
			case LIVELOST: return path + "livelost.wav";
			case DEATH:    return path + "death.wav";
			default:       return "";
		}
	}
	
	public static synchronized void play(SoundNames clip, int ms) {
		if (instance == null)
			instance = new SoundLibrary();
		
		instance.playClip(clip, ms);
	}
	
	private synchronized void playClip(final SoundNames clipId, int ms)
	{
		if(timeout.containsKey(clipId))
			if(timeout.get(clipId) >= System.currentTimeMillis())
				return;
		
		timeout.put(clipId, System.currentTimeMillis()+ms);
			
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							this.getClass().getResourceAsStream(GetClipPath(clipId)));
					clip.open(inputStream);
					clip.start(); 
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}
