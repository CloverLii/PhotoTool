package nz.ac.waikato.phototool;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Maintains a 2D matrix of RGB pixels, for image processing.
 *
 * It also provides methods for reading the pixels from an image file,
 * writing the pixels to an image file,
 * and displaying the image in a popup window.
 *
 * @author Mark Utting
 */
class Picture {
	private int width;
	private int height;
	//private Color[][] pixels
	private int[] pixels; 	//replace array of Color by array of int

	public Picture(String filename) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(filename));
		}
		catch (IOException ex) {
			throw new RuntimeException("Could not open file: " + filename + ": " + ex.getMessage());
		}
		if (image == null) {
			throw new RuntimeException("Invalid image file: " + filename);
		}
		width = image.getWidth();
		height = image.getHeight();
		//pixels = new Color[width][height];
		
		//replace array of Color by array of int
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		//change to row-major order
		for (int y = 0; y < height; y++) { 
			for (int x = 0; x < width; x++) {
				//pixels[x][y] = new Color(image.getRGB(x, y));
				pixels[y * width + x] = image.getRGB(x, y);		
				
			}
		}
	}

	public Picture(int w, int h) {

		width = w;
		height = h;
		//pixels = new Color[width][height];
		pixels = new int[width *height]; 		//replace array of Color by array of int

		//chagne to row-major order
		for (int y = 0; y < height; y++) {  
			for (int x = 0; x < width; x++) {
				//pixels[x][y] = Color.BLACK;
				pixels[y * width + x] = 0; 				//the value of BLACK is 0
			}
		}
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public Color get(int x, int y) {
		//return pixels[x][y];
		
		//replace array of Color by array of int
		int red = (pixels[y * width + x] >> 16) & 0xFF;
		int green = (pixels[y * width + x] >> 8) & 0xFF;
		int blue = pixels[y * width + x] & 0xFF;
		
		Color pixel = new Color(red, green, blue);
		return pixel;

	}

	public void set(int x, int y, Color newPixel) {
		//pixels[x][y] = newPixel;
		pixels[y * width + x] = newPixel.getRGB(); //replace array of Color by array of int
		
	}

	private BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//replace array of Color by array of int
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		 
		//change to row-major order
		for (int y = 0; y < height; y++) {    
			for (int x = 0; x < width; x++) {
				image.setRGB(x, y, pixels[y * width + x]);
			}
		}
		return image;
	}

	public void show(String name) {
		JFrame frame = new JFrame();
		ImageIcon icon = new ImageIcon(getImage());
		frame.setContentPane(new JLabel(icon));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle(name);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	public void save(String newName) throws IOException {
		BufferedImage image = getImage();
		final int dot = newName.lastIndexOf('.');
		final String extn = newName.substring(dot + 1);
		final File outFile = new File(newName);
		ImageIO.write(image, extn, outFile);
	}
}