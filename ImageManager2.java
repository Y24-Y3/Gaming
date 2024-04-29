import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;


public class ImageManager2 {

    public ImageManager2(){

    }

    public static Image loadImage(String fileName){
        return new ImageIcon(fileName).getImage();
    }

    public static BufferedImage loadBufferedImage(String filename){
        BufferedImage bi = null;

        File file = new File(filename);
        try{
            bi = ImageIO.read(file);
        }
        catch(IOException ioe){
            System.out.println("Error opening file " + filename + ":" + ioe);
        }
        return bi;
    }

    public static BufferedImage copyImage(BufferedImage src){
        if(src == null)
            return null;

        int imWidth = src.getWidth();
        int imHeight = src.getHeight();

        BufferedImage copy = new BufferedImage(imWidth, imHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = copy.createGraphics();

        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();

        return copy;
    }

    public static Image tintImage(Image originalImage, Color color) {
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        // Apply tinting effect only to non-transparent pixels
        int tintAlpha = 200; // Adjust the alpha value for a less harsh tint
        Color tinted = new Color(color.getRed(), color.getGreen(), color.getBlue(), tintAlpha);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bufferedImage.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF; // Extract alpha value (transparency)
                if (alpha != 0) { // If not fully transparent
                    bufferedImage.setRGB(x, y, tinted.getRGB());
                }
            }
        }

        // Convert the BufferedImage back to Image
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }

    
}
