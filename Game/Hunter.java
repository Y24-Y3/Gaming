import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;

public class Hunter implements Entity{
    private JPanel panel;
    private int x, y, width, height, dx, dy;
    private Image image;
    private SoundManager soundManager;
    private Dimension d;
    private Color color;

    public Hunter(JPanel panel, int xpos, int ypos){
        this.panel = panel;
        d = panel.getSize();
        color = panel.getBackground();
        x = xpos;
        y = ypos;
        width = 50;
        height = 50;
        dx = 0;
        dy = 0;

        image = ImageManager.loadImage("images//character//hunter.png");
        soundManager = SoundManager.getInstance();
    }

    public void draw(Graphics2D g2d){
        g2d.drawImage(image, x, y, width, height, panel);
    }

    public Rectangle2D.Double getBounds(){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void erase(){
        Graphics g = panel.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fill(new Rectangle2D.Double(x, y, width, height));
        g.dispose(); 
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
