import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
    The TileMap class contains the data for a tile-based
    map, including Sprites. Each tile is a reference to an
    Image. Images are used multiple times in the tile map.
    map.
*/

public class TileMap2 {

    private static final int TILE_SIZE = 32;
    private static final int TILE_SIZE_BITS = 6;

    private Image[][] tiles;
    private int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private int offsetY;

    private LinkedList sprites;
    private LinkedList sprites2;
    private LinkedList arrows;
    private LinkedList arrows2;
    private LinkedList bullets;
    private LinkedList bullets2;
    private Hunter player;
    private Heart2 heart;

    private boolean boss;
    private int lastOffsetX;

    BackgroundManager2 bgManager;

    private GamePanel2 panel;
    private Dimension dimension;

    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap2(GamePanel2 panel, int width, int height) {

	this.panel = panel;
	dimension = panel.getSize();

    boss = false;
    lastOffsetX = 0;

	screenWidth = dimension.width;
	screenHeight = dimension.height;

	System.out.println ("Width: " + screenWidth);
	System.out.println ("Height: " + screenHeight);

	mapWidth = width;
	mapHeight = height;

        // get the y offset to draw all sprites and tiles

       	offsetY = screenHeight - tilesToPixels(mapHeight);
	System.out.println("offsetY: " + offsetY);

	bgManager = new BackgroundManager2 (panel, 12);

        tiles = new Image[mapWidth][mapHeight];
	player = new Hunter (panel, this, bgManager);
	heart = new Heart2 (panel, player);
		
        sprites = new LinkedList();
        sprites2 = new LinkedList();
        arrows = new LinkedList();
        arrows2 = new LinkedList();
        bullets = new LinkedList();
        bullets2 = new LinkedList();

	Image playerImage = player.getImage();
	int playerHeight = playerImage.getHeight(null);

	int x, y;
	x = (dimension.width / 2) + TILE_SIZE;		// position player in middle of screen

	//x = 1000;					// position player in 'random' location
	y = dimension.height - (TILE_SIZE*4 + playerHeight);

        player.setX(x);
        player.setY(y);

	System.out.println("Player coordinates: " + x + "," + y);

    }


    /**
        Gets the width of this TileMap (number of pixels across).
    */
    public int getWidthPixels() {
	return tilesToPixels(mapWidth);
    }


    /**
        Gets the width of this TileMap (number of tiles across).
    */
    public int getWidth() {
        return mapWidth;
    }


    /**
        Gets the height of this TileMap (number of tiles down).
    */
    public int getHeight() {
        return mapHeight;
    }


    public int getOffsetY() {
	return offsetY;
    }

    /**
        Gets the tile at the specified location. Returns null if
        no tile is at the location or if the location is out of
        bounds.
    */
    public Image getTile(int x, int y) {
        if (x < 0 || x >= mapWidth ||
            y < 0 || y >= mapHeight)
        {
            return null;
        }
        else {
            return tiles[x][y];
        }
    }


    /**
        Sets the tile at the specified location.
    */
    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }


    /**
        Gets an Iterator of all the Sprites in this map,
        excluding the player Sprite.
    */

    public Iterator getSprites() {
        sprites = sprites2;
        return sprites.iterator();
    }

    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(int pixels) {
        return (int)Math.floor((float)pixels / TILE_SIZE);
    }


    /**
        Class method to convert a tile position to a pixel position.
    */

    public static int tilesToPixels(int numTiles) {
        return numTiles * TILE_SIZE;
    }

    /**
        Draws the specified TileMap.
    */
    public void draw(Graphics2D g2)
    {
        int mapWidthPixels = tilesToPixels(mapWidth);

        // get the scrolling position of the map
        // based on player's position
        if(player.getX() >= 11578 && (player.getY() + player.getImage().getHeight(null)) > 300)
            boss = true;

        int offsetX;

        if(boss){
            offsetX = lastOffsetX;
        }else{
            offsetX = screenWidth / 2 -
            Math.round(player.getX()) - TILE_SIZE;
            offsetX = Math.min(offsetX, 0);
            offsetX = Math.max(offsetX, screenWidth - mapWidthPixels);
            lastOffsetX = offsetX;
        }
        

/*
        // draw black background, if needed
        if (background == null ||
            screenHeight > background.getHeight(null))
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
*/
	// draw the background first

	bgManager.draw (g2);

	//Draw white background (for screen capture)
/*
	g2.setColor (Color.WHITE);
	g2.fill (new Rectangle2D.Double (0, 0, 600, 500));
*/
        // draw the visible tiles

        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<mapHeight; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = getTile(x, y);
                if (image != null) {
                    g2.drawImage(image,
                        tilesToPixels(x) + offsetX,
                        tilesToPixels(y) + offsetY,
                        null);
                }
            }
        }


        // draw player

        g2.drawImage(player.getImage(),
            Math.round(player.getX()) + offsetX,
            Math.round(player.getY()), //+ offsetY,
            null);

	// draw Heart sprite

        g2.drawImage(heart.getImage(),
            Math.round(heart.getX()) + offsetX,
            Math.round(heart.getY()), 40, 40, //+ offsetY, 50, 50,
            null);


        // draw sprites
        Iterator i = getSprites();
        while (i.hasNext()) {
            Enemy sprite = (Enemy)i.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;
            if (sprite instanceof Ramm) {
                // If sprite is an instance of the Ramm class
                g2.drawImage(sprite.getImage(), x, y - sprite.getHeight() + 5, null);
            }
            if (sprite instanceof Stein) {
                // If sprite is an instance of the Ramm class
                g2.drawImage(sprite.getImage(), x, y + 32, null);
            }

            // wake up the creature when it's on screen
            if (x >= 0 && x < screenWidth){
                ((Enemy)sprite).wakeUp();
            }
        }

        // draw arrow sprites
        arrows = arrows2;
        Iterator i2 = arrows.iterator();
        while (i2.hasNext()) {
            RammArrow sprite = (RammArrow)i2.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;
            // Draw the arrow
            g2.drawImage(sprite.getImage(), x, y - sprite.getHeight() + 35, null);
        }

        // draw bullets sprites
        bullets = bullets2;
        Iterator i3 = bullets.iterator();
        while (i3.hasNext()) {
            Bullet sprite = (Bullet)i3.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;
            // Draw the bullet
            g2.drawImage(sprite.getImage(), x, y - sprite.getHeight() + 115, null);
        }


    }


    public void moveLeft(boolean[] directions) {
	int x, y;
	x = player.getX();
	y = player.getY();

	String mess = "Going left. x = " + x + " y = " + y;
	System.out.println(mess);

	player.move(directions);

    }

    public void stopMoveLeft(boolean[] directions) {
        player.move(directions);
        System.out.println("stoped going left");

    }


    public void moveRight(boolean[] directions) {
	int x, y;
	x = player.getX();
	y = player.getY();

	String mess = "Going right. x = " + x + " y = " + y;
	System.out.println(mess);

	player.move(directions);

    }

    public void stopMoveRight(boolean[] directions) {
        player.move(directions);
        System.out.println("stoped going right");
    }



    public void jump(boolean[] directions) {
	int x, y;
	x = player.getX();
	y = player.getY();

	String mess = "Jumping. x = " + x + " y = " + y;
	System.out.println(mess);

	player.move(directions);

    }

    public void stopJump(boolean[] directions) {

        player.move(directions);
        System.out.println("stoped jumping");
    }


    public void update() {
	player.update();

	if (heart.collidesWithPlayer()) {
		panel.endLevel();
		return;
	}

	heart.update();

	if (heart.collidesWithPlayer()) {
		panel.endLevel();
	}

    arrows2.clear();

    sprites2 = new LinkedList(sprites);
    Iterator i = sprites2.iterator();
        while (i.hasNext()) {
            Enemy sprite = (Enemy)i.next();
            sprite.update();

            if(sprite instanceof Stein){
                if(((Stein)sprite).dead()){
                    i.remove();
                }
            }

            if(sprite instanceof Ramm){
                if(((Ramm)sprite).dead()){
                    i.remove();
                }
                arrows2.addAll(((Ramm)sprite).getArrows());
            }
        }
    
    arrows = arrows2;

    Iterator i2 = arrows.iterator();
        while (i2.hasNext()) {
            RammArrow sprite = (RammArrow)i2.next();
            sprite.update();
        }

 
    bullets2 = player.getBullets();
    Iterator i3 = bullets2.iterator();
        while (i3.hasNext()) {
            Bullet sprite = (Bullet)i3.next();
            sprite.update();
        }



    }


    @SuppressWarnings("unchecked")
    public void addSprite(Enemy sprite) {
        sprites.add(sprite);
        return;
    }

    public Hunter getPlayer(){
        return player;
    }

}