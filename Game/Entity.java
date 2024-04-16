import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Entity {
    public void draw(Graphics2D g2d);
    public Rectangle2D.Double getBounds();
}