package library;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

/**
 * @author Jiri Konecny <xkonec28>
 * @author Tomas Kimer <xkimer00>
 * 
 * Load and get needed images
 */
public class ImageLibrary {
	private List<BufferedImage> textures;
	private int resolution;
	
	/**
	 * Create object and load images
	 * @param name Name of the file with images
	 * @param resolution Resolution of one image in file
	 */
	public ImageLibrary(String name, int resolution){
		BufferedImage img = null;
		this.resolution = resolution;
		textures = new ArrayList<BufferedImage>();
		
		// load image from file
		try {
			img = ImageIO.read(this.getClass().getResource(name));
		} catch (Exception e) {
			System.err.println("Error: Image file can't be loaded.");
			e.printStackTrace();
			System.exit(1);
		}
		
		// image must have good resolution for parsing
		if((img.getWidth() % resolution != 0) && (img.getHeight() % resolution != 0))
		{
			System.err.println("Error: Image have bad resolution.");
			System.exit(1);
		}
		
		// parse large image on images with given resolution
		for(short y = 0; y < img.getWidth(); y += resolution){
			for(short x = 0; x < img.getHeight(); x += resolution){
				textures.add(img.getSubimage(x, y, resolution, resolution));
			}
		}
		
	}
	
	public WritableRaster createCompatibleRaster(int width, int height){
		return (textures.get(0).getRaster().createCompatibleWritableRaster(width, height));
	}
	
	/**
	 * Return image at index from internal image
	 * @param index index of tile in image 
	 * @return Image object
	 */
	public Image getImageTile(int index){
		return(textures.get(index));
	}
	
	/**
	 * Return raster at index from internal image
	 * @param index index of tile in image
	 * @return Raster object
	 */
	public Raster getRasterById(int index){
		System.out.println("TransferType: "+textures.get(index).getData().getTransferType()+" SM: "+textures.get(index).getData().getSampleModel());
		System.out.println("Image id: "+index);
		return(textures.get(index).getData());
	}
	
	public int[] getPixelsById(int index){
		int[] ret = new int[resolution*resolution];
		System.out.println(textures.get(0).getColorModel().hasAlpha());
		PixelGrabber pg = new PixelGrabber(textures.get(index), 0, 0, resolution, resolution, ret, 0, resolution);
		try{
			pg.grabPixels();
		} catch(InterruptedException e){
			System.err.println(e.getMessage());
		}
		return ret;
	}
}
