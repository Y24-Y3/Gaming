import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel implements Runnable {

	// ==================== Screen variables ====================
    private final int originalSize = 18;
    private final int scale = 3;

    private final int maxScreenCol = 20;
    private final int maxScreenRow = 14;
    private final int tileSize = originalSize * scale;
    private final int screenWidth = 1000; //maxScreenCol * tileSize;
    private final int screenHeight = 700; //maxScreenRow * tileSize;

	// ================ KeyHandler and Thread ====================
    private final KeyHandler key = new KeyHandler(this);
    Thread gameThread;

    // ===================== World variables =====================
    private final int worldCol = 74;
    private final int worldRow = 55;
    private final int worldWidth = worldCol * tileSize;
    private final int worldHeight = worldRow * tileSize;
    //private final int maxMap = 500;

	// ================== Sound variables ==========================
    private SoundManager sm;

    // ====================== Image variables ======================
    private ImageManager im = new ImageManager();


	// =================== Control Variables =======================
	boolean[] directions = {false, false, false, false};
	// Additional GameState
	private boolean isRunning;
	private boolean isPaused;

	// ==================== System Variables ======================
	public UI ui = new UI(this);
    public int gameState;
    private final int menuState = 0;
    private final int playState = 1;
    private final int pauseState = 2;
    public final int gameOverState = 3;
    public final int dialoueState = 4;

    // ====================== FPS Variable ========================
    private int FPS = 60;

	// ==================== Player variables =======================
    public Hunt player = new Hunt(this, key);
    public AssertObjects ao = new AssertObjects(this);
    public CollisionChecker cc = new CollisionChecker(this);
    public TileMapManagerHelp tmm = new TileMapManagerHelp(this);
    public Objects obj[] = new Objects[10];
    public Entities hostile[] = new Entities[15]; 
    public Entities neutral[] = new Entities[15];

	// ==============================================================

	// ==================== Game Visual =========================
	private BufferedImage image;
	private volatile boolean isAnimShown;

	// ===================== Game Level 2 Tile ====================
	private TileMapManager tileManager;
	private TileMap	tileMap;

	// ====================== Game Level ============================
	private boolean levelChange;
	private int level;
	private boolean gameOver;

	public GamePanel () {
		// Size Initialization
		this.setPreferredSize(new Dimension(getScreenWidth(), getScreenHeight()));
		this.setDoubleBuffered(true);
        this.addKeyListener(key);
        this.setFocusable(true);
		sm = SoundManager.getInstance();

		// Game State Initialization
		isRunning = false;
		isPaused = false;
		isAnimShown = false;
		image = new BufferedImage (screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);

		// Game Level Initialization
	}

	// ==================== Game Start ========================
	public void gameSetup(){
        ao.setObjects();
        ao.setHostile();
        ao.setNeutral();
        sm.setVolume("level1_loop", 0.2f);
        sm.playClip("level1_loop", true);
        gameState = 0;
		level = 1;
		levelChange = false;
    }

	// ==================== Game Thread ========================
	public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                requestFocusInWindow();
            }
        });
    }

	public void run () {
		double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int frames = 0;

		while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
				if(level == 1){
					update();
                	repaint();
					delta--;
					frames++;
				}
				else if(level == 2){
					gameUpdate();
					gameRender();
					delta--;
					frames++;
				}
			}

			if(timer >= 1000000000){
				//System.out.println("FPS: " + frames);
				frames = 0;
				timer = 0;
			}
		}
	}


	public void gameUpdate() {

		tileMap.moveLeft(directions);
		tileMap.update();

		if (levelChange) {
			levelChange = false;
			tileManager = new TileMapManager (this);

			try {
				String filename = "Game/maps/map" + level + ".txt";
				tileMap = tileManager.loadMap(filename) ;
				int w, h;
				w = tileMap.getWidth();
				h = tileMap.getHeight();
				System.out.println ("Changing level to Level " + level);
				System.out.println ("Width of tilemap " + w);
				System.out.println ("Height of tilemap " + h);
			}
			catch (Exception e) {		// no more maps: terminate game
				gameOver = true;
				System.out.println(e);
				System.out.println("Game Over"); 
				return;

			}

			return;
				
		}

	}


	public void gameRender() {

		// draw the game objects on the image

		Graphics2D imageContext = (Graphics2D) image.getGraphics();
		tileMap.draw (imageContext);

		if (gameOver) {
			Color darken = new Color (0, 0, 0, 125);
			imageContext.setColor (darken);
			imageContext.fill (new Rectangle2D.Double (0, 0, 1000, 700));
		}

		Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
		g2.drawImage(image, 0, 0, 1000, 700, null);	// draw the image on the graphics context

		imageContext.dispose();
	}


	public void startGame() {				// initialise and start the game thread 

		if (level == 2) {
			gameOver = false;

			tileManager = new TileMapManager (this);
			try {
				tileMap = tileManager.loadMap("Game/maps/map1.txt");
				int w, h;
				w = tileMap.getWidth();
				h = tileMap.getHeight();
				System.out.println ("Width of tilemap " + w);
				System.out.println ("Height of tilemap " + h);
			}
			catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}

			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	public void startNewGame() {				// initialise and start a new game thread 
		if (gameThread != null || !isRunning) {
			//endGame();

			gameOver = false;
			level = 1;

			tileManager = new TileMapManager (this);

			try {
				tileMap = tileManager.loadMap("Game/maps/map1.txt");
				int w, h;
				w = tileMap.getWidth();
				h = tileMap.getHeight();
				System.out.println ("Width of tilemap " + w);
				System.out.println ("Height of tilemap " + h);
			}
			catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}


			gameThread = new Thread(this);
			gameThread.start();			

		}
	}

	// ================== Level 1 Update ========================
	public void update(){
        if(gameState == playState){
            player.update();

            for(int i = 0; i < hostile.length; i++){
                if(hostile[i] != null){
                    //hostile[i].update();
                }
            }

            for(int i = 0; i < neutral.length; i++){
                if(neutral[i] != null){
                    //neutral[i].update();
                } 
            }
        }

        if(gameState == pauseState){
            // Pause
        }
        if(gameState == gameOverState){
            // Game Over
        }

    }

	// ================== Level 1 Render ========================
	public void paintComponent(Graphics g){
        super.paintComponent(g);  
        Graphics2D g2 = (Graphics2D) g;

        long start = 0;
        if(key.debugger == true){
            start = System.nanoTime();
        }

        if(gameState == menuState){
            ui.draw(g2);
        }
        else{
            tmm.draw(g2);

            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    obj[i].draw(g2, this);
                }
            }
            for(int i = 0; i < hostile.length; i++){
                if(hostile[i] != null){
                    hostile[i].draw(g2);
                }
            }
            for(int i = 0; i < neutral.length; i++){
                if(neutral[i] != null){
                    //neutral[i].draw(g2);
                }
            }
    
            player.draw(g2);
            ui.draw(g2);
        }
        
        if(key.debugger == true){
            long elapsed = System.nanoTime() - start;
            g2.setColor(Color.WHITE);
            g2.drawString("FPS: " + (1000000000 / elapsed), 10, 10);
            System.out.println("FPS: " + (1000000000 / elapsed));
        }

        g2.dispose();
    }





	 public void endGame() {					// end the game thread
		isRunning = false;
		//soundManager.stopClip ("background");
	} 

	
	public void moveLeft(boolean[] directions) {
		if (!gameOver)
			tileMap.moveLeft(directions);
	}


	public void endLevel() {
		level = level + 1;
		levelChange = true;
	}

	public void setDirections(boolean[] directions) {
		if(directions[1] && directions[2]){ directions[1] = false;} // prioritize goinf right over left to avoid bad movement
		this.directions = directions;
	}

	// ===============================================================================================================================

	public int getTileSize(){
        return tileSize;
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

    public int getWorldWidth(){
        return worldWidth;
    }

    public int getWorldHeight(){
        return worldHeight;
    }

    public int getWorldCol(){
        return worldCol;
    }

    public int getWorldRow(){
        return worldRow;
    }

    public int getScreenCol(){
        return maxScreenCol;
    }

    public int getScreenRow(){
        return maxScreenRow;
    }

    public KeyHandler getKeyHandler(){
        return key;
    }
    
    public Hunt getPlayer(){
        return player;
    }

    public int getOriginalSize(){
        return originalSize;
    }

    public SoundManager getSoundManager(){
        return sm;
    }

    public ImageManager getImageManager(){
        return im;
    }

    public int getGameState(){
        return gameState;
    }

    public int getMenuState(){
        return menuState;
    }

    public int getPlayState(){
        return playState;
    }

    public int getPauseState(){
        return pauseState;
    }

}