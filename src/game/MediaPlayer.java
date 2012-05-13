/**
 * 
 */
package game;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Play video file
 */
public class MediaPlayer {
	private static String path = "media/";
	private static String musicFile = "music.mp3";
	private static String videoFile = "intro.wmv";	

	// vlcj media player
	private EmbeddedMediaPlayerComponent mediaPlayerComponent = null;
	private HeadlessMediaPlayer musicPlayer;
	
	public MediaPlayer(){
		
		// add path for search vlc shared library
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/usr/lib"
				);
		// dunno if this is functional
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files/VideoLAN/VLC"
				);

		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		
		//create media player
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		// music player
		musicPlayer = (new MediaPlayerFactory()).newHeadlessMediaPlayer();
		musicPlayer.setRepeat(true);
		musicPlayer.setVolume(70);
	}
	
	public void finalize(){
		mediaPlayerComponent.getMediaPlayer().release();
		musicPlayer.release();
	}
	
	
	/**
	 * Get content for JFrame 
	 * @return content for JFrame
	 */
	public EmbeddedMediaPlayerComponent getMedia(){
		return mediaPlayerComponent;
	}
	
	/**
	 * Play video file in JFrame
	 * @throws InterruptedException  Interrupted from sleep (block system when play)
	 */
	public void play(){
		musicPlayer.stop();
		mediaPlayerComponent.getMediaPlayer().playMedia(path + videoFile);
		
	}
	
	public void stop(){
		mediaPlayerComponent.getMediaPlayer().stop();
	}
	
	public void playMusic(){	    
	    musicPlayer.playMedia(path + musicFile);	    
	}
	
	public void stopMusic(){
		musicPlayer.stop();
	}
	
	/**
	 * Get length of the video
	 * @return Time in ms
	 */
	public long videoLength(){
		if(mediaPlayerComponent == null)
			return 0;
		
		return mediaPlayerComponent.getMediaPlayer().getLength();
	}
	
	/**
	 * Is this video playing?
	 * @return true if now playing
	 */
	public boolean isPlaying(){
		if(mediaPlayerComponent == null)
			return false;
		
		return mediaPlayerComponent.getMediaPlayer().isPlaying();
	}
	
}
