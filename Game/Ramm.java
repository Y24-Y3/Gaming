import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.LinkedList;

public class Ramm implements Enemy{

    private static final int XSIZE = 64;		// width of the image
	private static final int YSIZE = 64;		// height of the image
	//private static final int DX = 2;		// amount of pixels to move in one update
	private static final int YPOS = 150;		// vertical position of the image

	private int x;
	private int y;
	private int dx;
    private int originalX;

	private LinkedList arrows;
	private LinkedList bullets;
	private Hunter player;
	private TileMap map;

	private Image spriteImage;			// image for sprite
	private Image spriteLeftImage;
	private Image spriteRightImage;

	//Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, grayImage;

	int count;
	private boolean dead;
	private int health;


	public Ramm (TileMap map) {  
        
		// x = 4128;
		// y = 270;
		dx = 0;
        originalX = x;

		this.map = map;
		this.player = map.getPlayer();

		time = 0;				// range is 0 to 10
		timeChange = 1;				// set to 1
		originalImage = true;
		grayImage = false;
		count = 0;
		arrows = new LinkedList();
		bullets = player.getBullets();
		dead = false;
		health = 5;

		spriteLeftImage = ImageManager.loadImage("Game//images//ramm//rammShootLeft100.gif");
		spriteRightImage = ImageManager.loadImage("Game//images//ramm//rammShootRight100.gif");
		spriteImage = spriteRightImage;

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
		return new Rectangle2D.Double (x, y-spriteImage.getHeight(null)+35, spriteImage.getWidth(null), spriteImage.getHeight(null));
	}

    public void update() {	

		if(health <= 0){
			dead = true;
			System.out.println("Ramm killed");
		}

		if(player.getX() < (x + (spriteImage.getWidth(null)/2))){
			spriteImage = spriteLeftImage;
		}else{
			spriteImage = spriteRightImage;
		}

		count++;

		if(count == 22){
			//shoot
			shoot();
		}

		if(count% 28 == 0){
			//reset
			count = 0;
		}

		Iterator i = arrows.iterator();
        while (i.hasNext()) {
            RammArrow arrow = (RammArrow)i.next();
			if(!arrow.fired()){
				arrows.remove(arrow);
			}
		}

		bullets = player.getBullets();
		Iterator i2 = bullets.iterator();
        while (i2.hasNext()) {
            Bullet bullet = (Bullet)i2.next();
			System.out.println("bullet: " + bullet.getBounds());
			System.out.println("ramm: " + getBounds());
			if(bullet.getBounds().intersects(getBounds())){
				health = health - 1;
				System.out.println("hit -1");
				bullet.setFired();
			}
		}
		//map.setArrowSprites(arrows);

	
		// boolean shooting;
		// if (shooting){
		// 	count++;
		// 	if(count >2){
		// 		shooting = false;
				
		// 	}
		// }
    
        
    }

   	private void shoot() {
		//pow pow pow
		int x = 0;
		if(spriteImage == spriteLeftImage)
			x = 1;
			
		if(spriteImage == spriteRightImage)
			x = 2;

		RammArrow temp = new RammArrow(map, x, this.getX(), this.getY());
		arrows.add(temp);
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

       public Ramm clone(){
        return this;
       }


    @Override
    public int getWidth() {
        // return spriteImage.getWidth(null);
        return 50;
    }


    @Override
    public int getHeight() {
        // return spriteImage.getHeight(null);
        return 50;
    }


    @Override
    public void wakeUp() {
        return;
    };

	public LinkedList getArrows(){
		return arrows;
	}

	public boolean dead(){
		return dead;
	}

}