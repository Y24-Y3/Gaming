import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Hunter {

    private static final int DX = 10;	// amount of X pixels to move in one keystroke
    private static final int DY = 16;	// amount of Y pixels to move in one keystroke
    private static final int TILE_SIZE = 32;

    //public HashSet<Integer> directions;
    int lastDirection;
 

    private JPanel panel;
    private int x, y, width, height, dx, dy;
    private Image l2PlayerImage, l2PlayerLeftImage, l2PlayerRightImage, l2PlayerIdleRightImage, l2PlayerIdleLeftImage, l2PlayerShootRightImage, l2PlayerShootLeftImage;
    private SoundManager2 soundManager;
    private Dimension d;
    private Color color;
    private TileMap2 tileMap;
    private BackgroundManager2 bgManager;

    private boolean jumping;
    private boolean boss;
    private int timeElapsed;
    private int startY;

    private boolean goingUp;
    private boolean goingDown;

    private boolean inAir;
    private int initialVelocity;
    private int startAir;

    private boolean shot;
    private int count;
    private LinkedList bullets;

    private int health;
    private boolean dead;



    public Hunter(JPanel panel, TileMap2 t, BackgroundManager2 b){
        //directions = new HashSet<>();
        this.panel = panel;
        tileMap = t;			// tile map on which the player's sprite is displayed
        bgManager = b;			// instance of BackgroundManager

        goingUp = goingDown = false;
        inAir = false;
        boss = false;
        shot = false; // use to make to fire
        count = 0;
        lastDirection = 2;
        bullets = new LinkedList();
        health = 10;
        dead = false;

        l2PlayerLeftImage = ImageManager2.loadImage("images2/character/walkLeft50x64.gif");
        l2PlayerRightImage = ImageManager2.loadImage("images2/character/walkRight50x64.gif");
        l2PlayerIdleRightImage = ImageManager2.loadImage("images2/character/idleRight50x64.gif");
        l2PlayerIdleLeftImage = ImageManager2.loadImage("images2/character/idleLeft50x64.gif");
        l2PlayerShootRightImage = ImageManager2.loadImage("images2/character/shootRight50x64.gif");
        l2PlayerShootLeftImage = ImageManager2.loadImage("images2/character/shootLeft50x64.gif");
        l2PlayerImage = l2PlayerIdleRightImage;
        soundManager = SoundManager2.getInstance();


    }

    public Point collidesWithTile(int newX, int newY) {

        int playerWidth = l2PlayerImage.getWidth(null);
        int offsetY = tileMap.getOffsetY();
        int xTile = tileMap.pixelsToTiles(newX);
        int yTile = tileMap.pixelsToTiles(newY - offsetY);

        if (tileMap.getTile(xTile, yTile) != null) {
                Point tilePos = new Point (xTile, yTile);
            return tilePos;
        }
        else {
            return null;
        }
    }

    public Point collidesWithTileDown (int newX, int newY) {

        int playerWidth = l2PlayerImage.getWidth(null);
              int playerHeight = l2PlayerImage.getHeight(null);
              int offsetY = tileMap.getOffsetY();
        int xTile = tileMap.pixelsToTiles(newX);
        int yTileFrom = tileMap.pixelsToTiles(y - offsetY);
        int yTileTo = tileMap.pixelsToTiles(newY - offsetY + playerHeight);
  
        for (int yTile=yTileFrom; yTile<=yTileTo; yTile++) {
          if (tileMap.getTile(xTile, yTile) != null) {
                  Point tilePos = new Point (xTile, yTile);
                return tilePos;
            }
          else {
              if (tileMap.getTile(xTile+1, yTile) != null) {
                  int leftSide = (xTile + 1) * TILE_SIZE;
                  if (newX + playerWidth > leftSide) {
                      Point tilePos = new Point (xTile+1, yTile);
                      return tilePos;
                      }
              }
          }
        }
  
        return null;
     }

    public Point collidesWithTileUp (int newX, int newY) {

        int playerWidth = l2PlayerImage.getWidth(null);
  
              int offsetY = tileMap.getOffsetY();
        int xTile = tileMap.pixelsToTiles(newX);
  
        int yTileFrom = tileMap.pixelsToTiles(y - offsetY);
        int yTileTo = tileMap.pixelsToTiles(newY - offsetY);
       
        for (int yTile=yTileFrom; yTile>=yTileTo; yTile--) {
          if (tileMap.getTile(xTile, yTile) != null) {
                  Point tilePos = new Point (xTile, yTile);
                return tilePos;
          }
          else {
              if (tileMap.getTile(xTile+1, yTile) != null) {
                  int leftSide = (xTile + 1) * TILE_SIZE;
                  if (newX + playerWidth > leftSide) {
                      Point tilePos = new Point (xTile+1, yTile);
                      return tilePos;
                      }
              }
          }
                      
        }
  
        return null;
    }

    public synchronized void move (boolean[] directions) {

        int newX = x;
        Point tilePos = null;
        if (newX >= 11578){
            boss = true;
        }
        if(shot){
            count++;
        }
    
        if(count > 10){
            shot = false;
        }
  
        if (!panel.isVisible ()) return;
        
        if (directions[1] == true) {		// move left
            l2PlayerImage = l2PlayerLeftImage;
            lastDirection = 1;
            newX = x - DX;
        if (newX < 0) {
          x = 0;
          return;
        }
          
        tilePos = collidesWithTile(newX, y);
        }	
        //else				
        if (directions[2] == true) {		// move right
            l2PlayerImage = l2PlayerRightImage;
            lastDirection = 2;
              int playerWidth = l2PlayerImage.getWidth(null);
            newX = x + DX;
  
              int tileMapWidth = tileMap.getWidthPixels();
  
        if (newX + l2PlayerImage.getWidth(null) >= tileMapWidth) {
            x = tileMapWidth - l2PlayerImage.getWidth(null);
            return;
        }
  
        tilePos = collidesWithTile(newX+playerWidth, y);			
        }
        //else				// jump
        if (directions[3] && !jumping && !goingDown) {	
            jump();
        return;
        }

        if(directions[0] && !shot){
            if(lastDirection == 1){
                l2PlayerImage = l2PlayerShootLeftImage;
            }
            else{
                l2PlayerImage = l2PlayerShootRightImage;
            }


            shoot();
            count = 0;
            shot = true;
        }
        //else
        if (!directions[0] && !directions[1] && !directions[2] && lastDirection == 1){
            l2PlayerImage = l2PlayerIdleLeftImage;
        }
        //else
        if (!directions[0] && !directions[1] && !directions[2] && lastDirection == 2){
            l2PlayerImage = l2PlayerIdleRightImage;
        }
        

        if (tilePos != null) {  
           if (directions[1]) {
           System.out.println (": Collision going left");
               x = ((int) tilePos.getX() + 1) * TILE_SIZE;	   // keep flush with right side of tile
       }
           else
           if (directions[2]) {
           System.out.println (": Collision going right");
                 int playerWidth = l2PlayerImage.getWidth(null);
               x = ((int) tilePos.getX()) * TILE_SIZE - playerWidth; // keep flush with left side of tile
       }
        }
        else {
            if (directions[1] == true) {
            x = newX;
            if(!boss)
                bgManager.moveLeft();
            }
        else
        if (directions[2] == true) {
            x = newX;
            if(!boss)
                bgManager.moveRight();
           }
  
            if (isInAir()) {
            System.out.println("In the air. Starting to fall.");
            if (directions[1] == true) {				// make adjustment for falling on left side of tile
                      int playerWidth = l2PlayerImage.getWidth(null);
                      x = x + DX -10 ;   //x = x - playerWidth + DX; having player width throws him off the left from far away, however having playwidth in the other location fixes that; only problem is he gets stuck; subbing 10 however fixes it well;
            }
            fall();
            }
        }

        // Iterator i = bullets.iterator();
        // while (i.hasNext()) {
        //     Bullet bullet = (Bullet)i.next();
		// 	if(!arrow.fired()){
		// 		arrows.remove(arrow);
		// 	}
		// }
     }

     public boolean isInAir() {

        int playerHeight;
        int playerWidth;
        Point tilePos;
  
        if (!jumping && !inAir) {   
        playerHeight = l2PlayerImage.getHeight(null);
        playerWidth = l2PlayerImage.getWidth(null);
        tilePos = collidesWithTile(x + playerWidth, y + playerHeight + 1); 	// check below player to see if there is a tile
      
          if (tilePos == null)				   	// there is no tile below player, so player is in the air
          return true;
        else							// there is a tile below player, so the player is on a tile
          return false;
        }
  
        return false;
     }

     private void fall() {

        jumping = false;
        inAir = true;
        timeElapsed = 0;
  
        goingUp = false;
        goingDown = true;
  
        startY = y;
        initialVelocity = 0;
     }
    
     public void jump () {  

        if (!panel.isVisible ()) return;
  
        jumping = true;
        timeElapsed = 0;
  
        goingUp = true;
        goingDown = false;
  
        startY = y;
        initialVelocity = 50;
     }

     public void update () {
        int distance = 0;
        int newY = 0;

        if(health <= 0){
            dead = true;
        }
  
        timeElapsed++;
  
        if (jumping || inAir) {
         distance = (int) (initialVelocity * timeElapsed - 
                               4.9 * timeElapsed * timeElapsed);
         newY = startY - distance;
  
         if (newY > y && goingUp) {
          goingUp = false;
             goingDown = true;
         }
  
         if (goingUp) {
          Point tilePos = collidesWithTileUp (x, newY);	
             if (tilePos != null) {				// hits a tile going up
                 System.out.println ("Jumping: Collision Going Up!");
  
                      int offsetY = tileMap.getOffsetY();
              int topTileY = ((int) tilePos.getY()) * TILE_SIZE + offsetY;
              int bottomTileY = topTileY + TILE_SIZE;
  
                 y = bottomTileY;
                 fall();
          }
             else {
              y = newY;
              System.out.println ("Jumping: No collision.");
  
              // the following if-statement is to pause the jump to capture the screen
  
  /*
              if (x > 1608 && y < 300) {
                  try {
                      Thread.sleep (1000);
                  }
                  catch (Exception e) {
                      System.out.println ("ERROR! " + e);
                  }
              }
  */
             }
              }
          else
          if (goingDown) {			
          Point tilePos = collidesWithTileDown (x, newY);	
             if (tilePos != null) {				// hits a tile going up
              System.out.println ("Jumping: Collision Going Down!");
                int playerHeight = l2PlayerImage.getHeight(null);
              goingDown = false;
  
                        int offsetY = tileMap.getOffsetY();
              int topTileY = ((int) tilePos.getY()) * TILE_SIZE + offsetY;
  
                  y = topTileY - playerHeight;
                jumping = false;
              inAir = false;
             }
             else {
              y = newY;
              System.out.println ("Jumping: No collision.");
             }
         }
        }

        Iterator i = bullets.iterator();
        while (i.hasNext()) {
            Bullet bullet = (Bullet)i.next();
			if(!bullet.fired()){
				bullets.remove(bullet);
			}
		}
     }

     public void moveUp () {

        if (!panel.isVisible ()) return;
  
        y = y - DY;
     }
    
    public void draw(Graphics2D g2d){
        // g2d.drawImage(image, x, y, width, height, panel);
    }

    public Rectangle2D.Double getBounds(){
        int playerWidth = l2PlayerImage.getWidth(null);
        int playerHeight = l2PlayerImage.getHeight(null);

        return new Rectangle2D.Double (x, y- tileMap.getOffsetY(), playerWidth, playerHeight);
    }

    public int getX(){
        return x;
    }
    public void setX(int x) {
        this.x = x;
     }

    public int getY(){
        return y;
    }
    public void setY(int y) {
        this.y = y;
     }

     public Image getImage() {
        return l2PlayerImage;
     }

     private void shoot() {
		//pow pow pow
		Bullet temp = new Bullet(tileMap, lastDirection, this.getX(), this.getY());
		bullets.add(temp);
	}

    public LinkedList getBullets(){
		return bullets;
	}

    public void takeDamage(int x){
        health = health - x;
        return;
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int x){
        health = x;
        return;
    }

    public boolean dead(){
        return dead;
    }
}