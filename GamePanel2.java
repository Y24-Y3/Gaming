import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

/**
   A component that displays all the game entities
*/

public class GamePanel2 extends JPanel implements Runnable {

	private SoundManager soundManager;
	boolean[] directions = {false, false, false, false};

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

	private TileMapManager2 tileManager;
	private TileMap2	tileMap;

	private boolean levelChange;
	private int level;
	private boolean gameOver, win;

	public GamePanel2 () {

		isRunning = false;
		isPaused = false;
		isAnimShown = false;
		isAnimPaused = false;


		soundManager = SoundManager.getInstance();

		image = new BufferedImage (1000, 700, BufferedImage.TYPE_INT_RGB);

		level = 1;
		levelChange = false;
	}


	public void createGameEntities() {
		//animation = new StripAnimation();
		// imageEffect = new ImageEffect (this);
	}


	public void run () {
		try {
			isRunning = true;
			while (isRunning) {
				if (!isPaused && !gameOver && !win)
					gameUpdate();
				gameRender();
				Thread.sleep (50);	
			}
		}
		catch(InterruptedException e) {}
	}


	public void gameUpdate() {

		tileMap.moveLeft(directions);
		tileMap.update();

		if (levelChange) {
			levelChange = false;
			tileManager = new TileMapManager2 (this);
			

			try {
				String filename = "maps2/map" + level + ".txt";
				tileMap = tileManager.loadMap(filename) ;
				int w, h;
				w = tileMap.getWidth();
				h = tileMap.getHeight();
				System.out.println ("Changing level to Level " + level);
				System.out.println ("Width of tilemap " + w);
				System.out.println ("Height of tilemap " + h);
			}
			catch (Exception e) {		// no more maps: terminate game
				win = true;
				soundManager.stopClip("transition2");
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

		Font font = new Font("Arial", Font.PLAIN, 20);
		imageContext.setFont(font);
        imageContext.setColor(Color.WHITE);
		imageContext.drawString(" Health: " + tileMap.getPlayer().getHealth(), 900, 70);

		//animation.draw(imageContext);
		if (isAnimShown){
			// animation.draw(imageContext);		// draw the animation
        }
		//imageEffect.draw(imageContext);			// draw the image effect

		if (gameOver) {
			Color darken = new Color (0, 0, 0, 125);
			imageContext.setColor (darken);
			imageContext.fill (new Rectangle2D.Double (0, 0, 1000, 700));
			imageContext.setColor(Color.RED); // Set color for "GAME OVER" text
            imageContext.setFont(imageContext.getFont().deriveFont(50f)); // Set font size
            imageContext.drawString("GAME OVER", 350, 350); // Adjust position as needed
		}

		if (win) {
			Color darken = new Color (0, 0, 0, 125);
			imageContext.setColor (darken);
			imageContext.fill (new Rectangle2D.Double (0, 0, 1000, 700));
			imageContext.setColor(Color.RED); // Set color for "GAME OVER" text
            imageContext.setFont(imageContext.getFont().deriveFont(50f)); // Set font size
            imageContext.drawString("YOU WIN!", 350, 350); // Adjust position as needed
		}

		Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
		g2.drawImage(image, 0, 0, 1000, 700, null);	// draw the image on the graphics context

		imageContext.dispose();
	}


	public void startGame() {				// initialise and start the game thread 

		if (gameThread == null) {
			//soundManager.playSound ("background", true);

			gameOver = false;
			win = false;
			
			soundManager.playClip("transition2", true);	// play the transition sound

			tileManager = new TileMapManager2 (this);

			try {
				tileMap = tileManager.loadMap("maps2/map1.txt");
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
			win = false;
			level = 1;

			
			soundManager.playClip("transition2", true);	// play the transition sound
			tileManager = new TileMapManager2 (this);

			try {
				tileMap = tileManager.loadMap("maps2/map1.txt");
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
		
		soundManager.stopClip("transition2");
	}

	
	public void moveLeft(boolean[] directions) {
		if (!gameOver || !win)
			tileMap.moveLeft(directions);
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

	public void loseGame(){
		gameOver = true;
		soundManager.stopClip("transition2");
		soundManager.playClip("gameover", false);
	}

}