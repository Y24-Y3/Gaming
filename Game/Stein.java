import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.LinkedList;

public class Stein implements Enemy{

    private static final int XSIZE = 64;		// width of the image
	private static final int YSIZE = 64;		// height of the image
	//private static final int DX = 2;		// amount of pixels to move in one update
	private static final int YPOS = 150;		// vertical position of the image

	private int x;
	private int y;
	private int dx;
    private int originalX;

	private Hunter player;
	private TileMap map;

	private Image spriteImage;			// image for sprite
	private Image spriteLeftImageWalk;
	private Image spriteRightImageWalk;
    private Image spriteLeftImageIdle;
    private Image spriteRightImageIdle;
    private Image spriteLeftImageAttack;
    private Image spriteRightImageAttack;
    int lastDirection;
    boolean gapClosed;
    private boolean dead;
	private int health;
	private LinkedList bullets;

	//Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, grayImage;

	int count;
	int count2;


	public Stein (TileMap map) {  
        
		// x = 4128;
		// y = 270;
		dx = 3;
        originalX = x;

		this.map = map;
		this.player = map.getPlayer();

		time = 0;				// range is 0 to 10
		timeChange = 1;				// set to 1
		originalImage = true;
		grayImage = false;
		count = 0;
        lastDirection = 2;
        gapClosed = false;
        dead = false;
		health = 10;
        bullets = player.getBullets();
		spriteLeftImageWalk = ImageManager.loadImage("Game/images/stein/walkLeft60x100.gif");
	    spriteRightImageWalk = ImageManager.loadImage("Game/images/stein/walkRight60x100.gif");
        spriteLeftImageIdle = ImageManager.loadImage("Game/images/stein/idleLeft60x100.gif");
        spriteRightImageIdle = ImageManager.loadImage("Game/images/stein/idleRight60x100.gif");
        spriteLeftImageAttack = ImageManager.loadImage("Game/images/stein/attackLeft100x100.gif");
        spriteRightImageAttack = ImageManager.loadImage("Game/images/stein/attackRight100x100.gif");
		
		spriteImage = spriteLeftImageIdle;

	}


	public void draw (Graphics2D g2) {

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


	public Rectangle2D.Double getBounds() {
		return new Rectangle2D.Double (x, y+32, spriteImage.getWidth(null), spriteImage.getHeight(null));
	}

    public void update() {	

        if(health <= 0){
			dead = true;
            System.out.println("Stein killed");
		}

		if((player.getX() + 25) < (x + (spriteImage.getWidth(null)/2))){ //facing left
			lastDirection = 1;
		}else{
			lastDirection = 2;
		}

		count++;

        double distance = Math.sqrt(Math.pow(player.getX() - getX(), 2) + Math.pow(player.getY() - getY(), 2));

        if(distance < 80 && lastDirection == 2){ //attack range
                spriteImage = spriteRightImageAttack;
        }else if(distance < 50 && lastDirection == 1){
            spriteImage = spriteLeftImageAttack;
        }else if(distance < 400){// pursue range

            if(lastDirection == 1){
                Point tilePos = collidesWithTile((x-dx), y);

                if(tilePos == null){
                    spriteImage = spriteLeftImageWalk;
                    x = x - dx;
                }else{
                    spriteImage = spriteLeftImageIdle;
                }
                
            }else{
                Point tilePos = collidesWithTile((x+dx+ spriteImage.getWidth(null)), y);

                if(tilePos == null){
                    spriteImage = spriteRightImageWalk;
                    x = x + dx;
                }else{
                    spriteImage = spriteRightImageIdle;
                }
            } 

        }else{// do nothing

            if(lastDirection == 1){
                spriteImage = spriteLeftImageIdle;
            }else{
                spriteImage = spriteRightImageIdle;
            }

        }

        bullets = player.getBullets();
        Iterator i2 = bullets.iterator();
        while (i2.hasNext()) {
            Bullet bullet = (Bullet)i2.next();
			if(getBounds().intersects(bullet.getBounds())){
				health = health - 1;
                System.out.println("hit -1");
				bullet.setFired();
			}
		}
		
        
    }


	public int getX() {
      		return x;
   	}


   	public void setX(int x) {
      		this.x = x;
   	}


   	public int getY() {
      		return y;
   	}


   	public void setY(int y) {
      		this.y = y;
   	}

    public void setOriginalX(int x) {
        this.originalX = x;
    }


   	public Image getImage() {
      		return spriteImage;
   	}

       public Stein clone(){
        return this;
       }


    @Override
    public int getWidth() {
        // return spriteImage.getWidth(null);
        return spriteImage.getWidth(null);
    }


    @Override
    public int getHeight() {
        // return spriteImage.getHeight(null);
        return spriteImage.getHeight(null);
    }


    @Override
    public void wakeUp() {
        return;
    };

    public Point collidesWithTile(int newX, int newY) {

        //int playerWidth = spriteImage.getWidth(null);
        int offsetY = map.getOffsetY();
        int xTile = map.pixelsToTiles(newX);
        int yTile = map.pixelsToTiles(newY - offsetY);

        if (map.getTile(xTile, yTile) != null) {
                Point tilePos = new Point (xTile, yTile);
            return tilePos;
        }
        else {
            return null;
        }
    }

    public boolean dead(){
		return dead;
	}

}