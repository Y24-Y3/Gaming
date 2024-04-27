import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class Ramm implements Enemy{

    private static final int XSIZE = 64;		// width of the image
	private static final int YSIZE = 64;		// height of the image
	//private static final int DX = 2;		// amount of pixels to move in one update
	private static final int YPOS = 150;		// vertical position of the image

	private int x;
	private int y;
	private int dx;
    private int originalX;

	private Hunter player;

	private Image spriteImage;			// image for sprite
	private Image spriteLeftImage;
	private Image spriteRightImage;

	//Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed
	boolean originalImage, grayImage;

	int count;


	public Ramm (Hunter player) {  
        
		// x = 4128;
		// y = 270;
		dx = 0;
        originalX = x;

		this.player = player;

		time = 0;				// range is 0 to 10
		timeChange = 1;				// set to 1
		originalImage = true;
		grayImage = false;
		count = 0;

		spriteLeftImage = ImageManager.loadImage("Game/images/ramm/rammShootLeft100.gif");
		spriteRightImage = ImageManager.loadImage("Game/images/ramm/rammShootRight100.gif");
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
		return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
	}

    public void update() {	

		if(player.getX() < (x + (spriteImage.getWidth(null)/2))){
			spriteImage = spriteLeftImage;
		}else{
			spriteImage = spriteRightImage;
		}

	
		// boolean shooting;
		// if (shooting){
		// 	count++;
		// 	if(count >2){
		// 		shooting = false;
				
		// 	}
		// }
    
        
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
        dx = 2;
        return;
    };

}