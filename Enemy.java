import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public interface Enemy {
    
    public Enemy clone();

    public int getWidth();

    public void setX(int i);

    public int getHeight();

    public void setY(int i);

    public int getX();

    public int getY();

    public Image getImage();

    public void wakeUp();

    public void update();

    public void setOriginalX(int i);

    public void draw(Graphics2D g2d);
    
    public Rectangle2D.Double getBounds();
}
