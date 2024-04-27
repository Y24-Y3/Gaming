import java.awt.Image;

public interface Enemy extends Entity {
    
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
}
