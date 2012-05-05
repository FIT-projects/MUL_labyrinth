/**
 * 
 */
package game;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;


/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Play video file
 */
public class MediaPlayer {

	// vlcj media player
	private EmbeddedMediaPlayerComponent mediaPlayerComponent = null;
	
	public MediaPlayer(){
		
		// add path for search vlc shared library
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/usr/lib"
				);
		// dunno if this is functional
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC"
				);

		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		
		//create media player
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
	
	}
	
	public void finalize(){
		mediaPlayerComponent.getMediaPlayer().release();
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
		mediaPlayerComponent.getMediaPlayer().playMedia("vid/CF_Axel_F_400x300.avi");
		
	}
	
	public void stop(){
		mediaPlayerComponent.getMediaPlayer().stop();
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
