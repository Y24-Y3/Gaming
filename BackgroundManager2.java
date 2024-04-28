/* BackgroundManager manages many backgrounds (wraparound images 
   used for the game's background). 

   Backgrounds 'further back' move slower than ones nearer the
   foreground of the game, creating a parallax distance effect.

   When a sprite is instructed to move left or right, the sprite
   doesn't actually move, instead the backgrounds move in the 
   opposite direction (right or left).

*/

import java.awt.Graphics2D;
import javax.swing.JPanel;


public class BackgroundManager2 {

	private String bgImages[] = {"images2/background/1.png",
				     "images2/background/2.png",
				     "images2/background/3.png",
				     "images2/background/4.png",
			       	     "images2/background/5.png"};

	  private int moveAmount[] = {10, 4, 3, 2, 1};  
						// pixel amounts to move each background left or right
     						// a move amount of 0 makes a background stationary

  	private Background2[] backgrounds;
  	private int numBackgrounds;

  	private JPanel panel;			// JPanel on which backgrounds are drawn

  	public BackgroundManager2(JPanel panel, int moveSize) {
						// ignore moveSize
    		this.panel = panel;

    		numBackgrounds = bgImages.length;
    		backgrounds = new Background2[numBackgrounds];

    		for (int i = 0; i < numBackgrounds; i++) {
       			backgrounds[i] = new Background2(panel, bgImages[i], moveAmount[i]);
    		}
  	} 


  	public void moveRight() { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveRight();
  	}


  	public void moveLeft() {
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].moveLeft();
  	}


  	// The draw method draws the backgrounds on the screen. The
  	// backgrounds are drawn from the back to the front.

  	public void draw (Graphics2D g2) { 
		for (int i=0; i < numBackgrounds; i++)
      			backgrounds[i].draw(g2);
  	}

}

