import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel implements Runnable {

	private SoundManager soundManager;
	boolean[] directions = {false, false, false, false};
	private final int FPS = 60;

	private boolean isRunning;
	private boolean isPaused;

	private Thread gameThread;

	private BufferedImage image;
 	private Image backgroundImage;

	// private BirdAnimation animation;
	//private StripAnimation animation;
	private volatile boolean isAnimShown;
	private volatile boolean isAnimPaused;

	// private ImageEffect imageEffect;		// sprite demonstrating an image effect

	private TileMapManager tileManager;
	private TileMap	tileMap;

	private boolean levelChange;
	private int level;
	private boolean gameOver;

	public GamePanel () {

		isRunning = false;
		isPaused = false;
		isAnimShown = false;
		isAnimPaused = false;


		soundManager = SoundManager.getInstance();

		image = new BufferedImage (1200, 700, BufferedImage.TYPE_INT_RGB);

		level = 1;
		levelChange = false;
	}


	public void createGameEntities() {
		//animation = new StripAnimation();
		// imageEffect = new ImageEffect (this);
	}


	public void run() {
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int frames = 0;
	
		isRunning = true;
		while (isRunning) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				if (!isPaused && !gameOver) {
					gameUpdate(); // Assuming gameUpdate() is equivalent to the update() method in RPG
				}
				gameRender(); // Assuming gameRender() is equivalent to the repaint() method in RPG
				delta--;
				frames++;
			}

			if (timer >= 1000000000) {
				System.out.println("FPS: " + frames);
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
				String filename = "Game/maps/map4" + level + ".txt";
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
/*
				System.exit(0);
*/
			}

			createGameEntities();
			return;
				
		}

		//animation.update();
		if (!isPaused && isAnimShown){
			// animation.update();
        }
		// imageEffect.update();
	}


	public void gameRender() {

		// draw the game objects on the image

		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		tileMap.draw (imageContext);

		//animation.draw(imageContext);
		if (isAnimShown){
			// animation.draw(imageContext);		// draw the animation
        }
		//imageEffect.draw(imageContext);			// draw the image effect

		if (gameOver) {
			Color darken = new Color (0, 0, 0, 125);
			imageContext.setColor (darken);
			imageContext.fill (new Rectangle2D.Double (0, 0, 600, 500));
		}

		Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
		g2.drawImage(image, 0, 0, 600, 500, null);	// draw the image on the graphics context

		imageContext.dispose();
	}


	public void startGame() {				// initialise and start the game thread 

		if (gameThread == null) {
			//soundManager.playSound ("background", true);

			gameOver = false;

			tileManager = new TileMapManager (this);

			try {
				tileMap = tileManager.loadMap("Game/maps/map4.txt");
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

			createGameEntities();

			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	public void startNewGame() {				// initialise and start a new game thread 
		if (gameThread != null || !isRunning) {
			//soundManager.playSound ("background", true);

			endGame();

			gameOver = false;
			level = 1;

			tileManager = new TileMapManager (this);

			try {
				tileMap = tileManager.loadMap("Game/maps/map4.txt");
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

			createGameEntities();

			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	public void pauseGame() {				// pause the game (don't update game entities)
		if (isRunning) {
			if (isPaused)
				isPaused = false;
			else
				isPaused = true;

			if (isAnimShown) {
				// if (isPaused)
				// 	animation.stopSound();
				// else
				// 	animation.playSound();
			}
		}
	}


	public void endGame() {					// end the game thread
		isRunning = false;
		//soundManager.stopClip ("background");
	}

	
	public void moveLeft(boolean[] directions) {
		if (!gameOver)
			tileMap.moveLeft(directions);
	}


	public void moveRight(boolean[] directions) {
		if (!gameOver)
			tileMap.moveRight(directions);
	}

	public void stopMoveLeft(boolean[] directions) {
		if (!gameOver)
			tileMap.stopMoveLeft(directions);
	}


	public void stopMoveRight(boolean[] directions) {
		if (!gameOver)
			tileMap.stopMoveRight(directions);
	}


	public void jump(boolean[] directions) {
		if (!gameOver)
			tileMap.jump(directions);
	}
	public void stopJump(boolean[] directions) {
		if (!gameOver)
			tileMap.stopJump(directions);
	}

	
	// public void showAnimation() {
	// 	isAnimShown = true;
	// 	animation.start();
		
	// }


	public void endLevel() {
		level = level + 1;
		levelChange = true;
	}

	public void setDirections(boolean[] directions) {
		if(directions[1] && directions[2]){ directions[1] = false;} // prioritize goinf right over left to avoid bad movement
		this.directions = directions;
	}

}