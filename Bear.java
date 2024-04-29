import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

public class Bear extends Entities{

    private StripAnimation idle, run, attack1, attack2, die;

    public Bear(GamePanel gp){
    super(gp);


    speed = 6;
    maxLife = 15;
    life = maxLife;
    direction = "down";

    boundingBox = new Rectangle();
    boundingBox.x = 8;
    boundingBox.y = 10;
    boundingBox.width = 8;
    boundingBox.height = 0;
    boundsX = boundingBox.x + 10;
    boundsY = boundingBox.y + 10;
    Image();
    anim = idle;
    
    }

    public void Image(){
        idle = new StripAnimation("images//sprites//Bear_Idle.png", 12, 100);
        run = new StripAnimation("images//sprites//Bear_Run.png", 5, 100);
        attack1 = new StripAnimation("images//sprites//beer_attack1.png", 9, 120);
        attack2 = new StripAnimation("images//sprites//beer_attack2.png", 9, 120);
        die = new StripAnimation("images//sprites//beer_dead.png", 9, 200);
    }


    public void setAction(){
        actionCounter++;


        if(actionCounter == 150){
            Random ran = new Random();
            int action = ran.nextInt(100)+1;

            if(action <= 25){
                direction = "up";
            }
            else if(action > 25 && action <= 50){
                direction = "down";

            }
            else if(action > 50 && action <= 75){
                direction = "left";
            }
            else if(action > 75 && action <= 100){
                direction = "right";
            }
            if (canAttackPlayer(gp.player)) {
                direction = "attack1";
                attackPlayer(gp.getPlayer());
            }

            actionCounter = 0;
            
        }
        
    }

    public StripAnimation getAnim(){
        return anim;
    }


    private boolean canAttackPlayer(Hunt player) {
        int distanceX = Math.abs(Worldx - player.getWorldX());
        int distanceY = Math.abs(Worldy - player.getWorldY());
        int attackRange = gp.getTileSize(); // Adjust the attack range as needed
    
        return (distanceX <= attackRange && distanceY <= attackRange);
    }

    public void attackPlayer(Hunt player) {
        player.life -= 1; // Decrement the life of the player
        if (player.life <= 0) {
            player.isDead = true;
            gp.ui.Mission = "You have been defeated by the Bear. Game Over!";
        }
    }

    public void draw(Graphics2D g2d) {
        int ScreenX = Worldx - gp.player.Worldx + gp.player.screenX;
        int ScreenY = Worldy - gp.player.Worldy + gp.player.screenY;
        int size = gp.getTileSize();
    
        if (life <= 0) {
            die.draw(g2d, ScreenX, ScreenY, size, size);
        } else {
            // Draw the bear as usual
            switch (direction) {
                case "up":
                    anim.draw(g2d, ScreenX, ScreenY, size, size);
                    break;
                case "down":
                    anim.draw(g2d, ScreenX, ScreenY, size, size);
                    break;
                case "left":
                    anim.draw(g2d, ScreenX, ScreenY, size, size);
                    break;
                case "right":
                    anim.draw(g2d, ScreenX, ScreenY, size, size);
                    break;
            }
    
            g2d.setColor(Color.RED);
            g2d.drawRect(ScreenX, ScreenY, size, size);
        }
    }

    
    }
