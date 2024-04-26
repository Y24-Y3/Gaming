import java.awt.Image;
import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Graphics2D;
/* import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
 */

public class TileMapHelp {

    private static final int TILE_SIZE = 32;
    //private static final int TILE_SIZE_BITS = 6;

    private Image[][] tiles;
    private int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private int offsetY;
    private int offsetX;
    private int x, y;

    private HashMap<Image, Integer> tileIndices = new HashMap<Image, Integer>();


    private GamePanel2 panel;
    private Dimension dimension;

    public TileMapHelp(GamePanel2 panel, int maxScreenCol, int maxScreenRow) {

	this.panel = panel;

    dimension = panel.getPreferredSize();

    screenWidth = dimension.width;
    screenHeight = dimension.height;

    //System.out.println("Dimension: " + dimension);
	System.out.println ("Tile Map Width: " + screenWidth);
	System.out.println ("Tile Map Height: " + screenHeight);

	mapWidth = maxScreenCol;
	mapHeight = maxScreenRow;
        
    tiles = new Image[mapWidth][mapHeight];
    offsetX = screenWidth - tilesToPixels(mapWidth);
    offsetY = screenHeight - tilesToPixels(mapHeight);



    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    //Gets the width of this TileMap (number of pixels across).
    public int getWidthPixels() {
	return tilesToPixels(mapWidth);
    }

    public int getHeightPixels() {
        return tilesToPixels(mapHeight);
    }


    //Gets the width of this TileMap (number of tiles across).
    public int getWidth() {
        return mapWidth;
    }

    //Gets the height of this TileMap (number of tiles down).
    public int getHeight() {
        return mapHeight;
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


    
    //Sets the tile at the specified location.
    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }

    //Gets an Iterator of all the Sprites in this map, excluding the player Sprite.
   /*  public Iterator getSprites() {
        return sprites.iterator();
    }
 */
    
    //Class method to convert a pixel position to a tile position.
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    //Class method to convert a pixel position to a tile position.
    public static int pixelsToTiles(int pixels) {
        return (int)Math.floor((float)pixels / TILE_SIZE);
    }


    //Class method to convert a tile position to a pixel position.
    public static int tilesToPixels(int numTiles) {
        return numTiles * TILE_SIZE;
    }

    //Draws the specified TileMap.
    public void draw(Graphics2D g2) {
        int mapWidthPixels = tilesToPixels(mapWidth);
        int mapHeightPixels = tilesToPixels(mapHeight);
    
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        lastTileX = Math.min(lastTileX, mapWidth);
    
        int firstTileY = pixelsToTiles(-offsetY);
        int lastTileY = firstTileY + pixelsToTiles(screenHeight) + 1;
        lastTileY = Math.min(lastTileY, mapHeight);
    
        int worldCol = 0;
        int worldRow = 0;
        int centerX = panel.player.screenX / 2;
        int centerY = panel.player.screenY / 2;
    
        while (worldCol < mapWidth && worldRow < mapHeight) {
            int worldX = worldCol * TILE_SIZE;
            int worldY = worldRow * TILE_SIZE;
            int screenCol = worldX - panel.player.Worldx + centerX;
            int screenRow = worldY - panel.player.Worldy + centerY + offsetY;
    
            Image tile = tiles[worldCol][worldRow];
        //if (image != null && worldX + panel.tileSize > panel.player.Worldx - panel.player.screenX && worldX  - panel.tileSize < panel.player.Worldx + panel.player.screenX && worldY + panel.tileSize > panel.player.Worldy - panel.player.screenY && worldY - panel.tileSize < panel.player.Worldy + panel.player.screenY) {
            g2.drawImage(tile, screenCol, screenRow, null);
        //}
    
            worldCol++;
            if (worldCol == mapWidth) {
                worldCol = 0;
                worldRow++;
            }
        }
    }




    

    public boolean collision(int x, int y) {
        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
            return true;
        }
        Image tile = tiles[x][y];
        if(tile == null) {
            return false;
        }
        int tileIndex = tileIndices.get(tile);
        return tileIndex == 0 || tileIndex == 4;
    }




}
