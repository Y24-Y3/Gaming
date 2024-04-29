import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Entities {

    public GamePanel gp;
    public GameWindow2 gw2;


    public int Worldx, Worldy;
    public int speed;
    public int width, height;
    public int maxLife, life;
    public boolean isDead = false;

    public Image image;
    public StripAnimation anim;
    public String direction;
    public Rectangle boundingBox;
    public int boundsX, boundsY;
    public boolean collision = false;
    public String dialogue = "";
    
    // Animation
    public int actionCounter = 0;
    public String name;

    public Entities(GamePanel gp){
        this.gp = gp;
        getDirection();
    }

    public void setAction(){
        actionCounter++;
    }

    public void update(){
        setAction();
        collision = false;
        gp.cc.checkTile(this);
        gp.cc.checkBoat(this, collision);
        gp.cc.checkPlayer(this);
        gp.cc.checkEntity(this, gp.hostile);
        gp.cc.checkEntity(this, gp.neutral);
        

        if (!collision) {
            switch (direction) {
                case "up":
                    Worldy -= speed;
                    break;
                case "down":
                    Worldy += speed;
                    break;
                case "left":
                    Worldx -= speed;
                    break;
                case "right":
                    Worldx += speed;
                    break;
            }
        }
    }

    public void draw(Graphics2D g2){
        int ScreenX = Worldx - gp.player.Worldx + gp.player.screenX;
        int ScreenY = Worldy - gp.player.Worldy + gp.player.screenY;

        int size = gp.getTileSize();
        anim = getAnim();

        if(Worldx + gp.getTileSize() > gp.player.Worldx - gp.player.screenX &&
            Worldx - gp.getTileSize() < gp.player.Worldx + gp.player.screenX &&
            Worldy + gp.getTileSize() > gp.player.Worldy - gp.player.screenY &&
            Worldy - gp.getTileSize() < gp.player.Worldy + gp.player.screenY){
            
            switch(direction){
                case "up":
                    anim.draw(g2, ScreenX, ScreenY, size, size);
                    break;
                case "down":
                    anim.draw(g2, ScreenX, ScreenY, size, size);
                    break;
                case "left":
                    anim.draw(g2, ScreenX, ScreenY, size, size);
                    break;
                case "right":
                    anim.draw(g2, ScreenX, ScreenY, size, size);
                    break;
                case "attack1":
                    anim.draw(g2, ScreenX, ScreenY, size, size);
                    break;
            }

            g2.setColor(Color.RED);
            g2.drawRect(ScreenX, ScreenY, size, size);
        }
    }

    public void attack(Entities e){
        if (e instanceof Deer) {
            Deer deer = (Deer) e;
            deer.life -= 1; // Decrement the life of the deer
            if (deer.life <= 0) {
                // Remove the deer from the game or perform any other necessary actions
            }
        } else if (e instanceof Bear) {
            Bear bear = (Bear) e;
            bear.life -= 1; // Decrement the life of the bear
            if (bear.life <= 0) {
                // Remove the bear from the game or perform any other necessary actions
            }
        }
    }

    public StripAnimation getAnim() {
        return anim;
    }

    public String getDirection() {
        return direction;
    }


}
