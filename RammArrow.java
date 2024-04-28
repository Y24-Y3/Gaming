import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class RammArrow implements Enemy{
    
    private static final int XSIZE = 52;		// width of the image
	private static final int YSIZE = 8;		// height of the image
	//private static final int DX = 2;		// amount of pixels to move in one update
	private static final int YPOS = 150;		// vertical position of the image

	private int x;
	private int y;
	private int dx;
    private int originalX;
    private int count;

	private Hunter player;
	private TileMap2 map;

	private Image spriteImage;			// image for sprite
	private Image spriteLeftImage;
	private Image spriteRightImage;

    private boolean fired;
    private int direction;
 

    public RammArrow(TileMap2 map, int direction, int x, int y){
        this.map = map;
        this.player = map.getPlayer();
        fired = true;
        this.direction = direction;
        this.x = x;
        this.y = y;
        dx = 15;
        count = 0;

        spriteLeftImage = ImageManager2.loadImage("images2/ramm/ArrowLeft52x8.png");
        spriteRightImage = ImageManager2.loadImage("images2/ramm/ArrowRight52x8.png");

        if(direction ==1){
            spriteImage = spriteLeftImage;
        }else{
            spriteImage = spriteRightImage;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(spriteImage, x, y, XSIZE, YSIZE, null);
    }

    public boolean collidesWithPlayer () {
		Rectangle2D.Double myRect = getBounds();
		Rectangle2D.Double playerRect = player.getBounds();
		
		if (myRect.intersects(playerRect)) {
			System.out.println ("Collision with player!");
			return true;
		}
		else
			return false;
	}

    @Override
    public Double getBounds() {
        return new Rectangle2D.Double (x, y-spriteImage.getHeight(null)+35, XSIZE, YSIZE);
    }

    @Override
    public void update() {

        count++;
        if (count > 20)
            fired = false;

        if(direction == 1){
            //code for left arrow
            // Point tilePos = collidesWithTile((x-dx), y);
            // if(tilePos != null)
            //     fired = false;
                
            x = x - dx;
        }else{
            //code for right arrow
            // Point tilePos = collidesWithTile((x+dx+ spriteImage.getWidth(null)), y);
            // if(tilePos != null)
            //     fired = false;

            x = x + dx;
        }

        if(collidesWithPlayer()){
            player.takeDamage(1);
            fired = false;
        }

    }

    @Override
    public int getWidth() {
        return 52;
    }

    @Override
    public void setX(int i) {
        this.x = i;
    }

    @Override
    public int getHeight() {
        return 8;
    }

    @Override
    public void setY(int i) {
        this.y = i;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Image getImage() {
        return spriteImage;
    }

    @Override
    public void wakeUp() {
        // peewooo
        return;
    }

    @Override
    public void setOriginalX(int i) {
        this.originalX = x;
    }

    public RammArrow clone(){
        return this;
       }

    public boolean fired(){
        return fired;
    }

    // public Point collidesWithTile(int newX, int newY) {

    //     //int playerWidth = spriteImage.getWidth(null);
    //     int offsetY = map.getOffsetY();
    //     int xTile = map.pixelsToTiles(newX);
    //     int yTile = map.pixelsToTiles(newY - offsetY);

    //     if (map.getTile(xTile, yTile) != null) {
    //             Point tilePos = new Point (xTile, yTile);
    //         return tilePos;
    //     }
    //     else {
    //         return null;
    //     }
    // }


}