import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Hunt extends Entities{


    private KeyHandler key;
    private StripAnimation walking, idle, die, attack;
    public int screenX = 0;
    public  int screenY = 0;
    private int health = 10;
    public int life = health;
    public int hasKey = 0;



    public Hunt(GamePanel gp, KeyHandler key){
        super(gp);

        this.key = key;
        screenX = (gp.getScreenWidth() / 2) - (gp.getTileSize() / 2);
        screenY = (gp.getScreenHeight()/ 2) - (gp.getTileSize() / 2);
        setDefaultValues();
        Image();

        boundingBox = new Rectangle();
        boundingBox.x = 50;
        boundingBox.y = 75;
        boundingBox.width = 15;
        boundingBox.height = 20;

        boundsX = boundingBox.x;
        boundsY = boundingBox.y;

        System.out.println("ScreenX: " + screenX + " ScreenY: " + screenY);

        //this.width = 50;
        //this.height = 50;
    }

    public void setDefaultValues(){
        Worldx = gp.getTileSize() * 15;
        Worldy = gp.getTileSize() * 45;
        speed = 4;
        direction = "idle";
    }

    public void update(){
        boolean directionKeyPressed = false; // Flag to track if any direction key is pressed

        if(key.up){
            direction = "up";
            directionKeyPressed = true;
            //gp.getSoundManager().playClip("walking1", false);

        }
        if(key.down){
            direction = "down";
            directionKeyPressed = true;
            //gp.getSoundManager().playClip("walking1", false);
        }
        if(key.left){
            direction = "left";
            directionKeyPressed = true;
            //gp.getSoundManager().playClip("walking1", false);

        }
        if(key.right){
            direction = "right";
            directionKeyPressed = true;
            //gp.getSoundManager().playClip("walking1", false);

        }

        if(key.attack){
            direction = "attack";
            directionKeyPressed = true;
            attack();
            if(directionKeyPressed){
                key.attack = false;
            }else{
                direction = "idle";
            
            }
        }
    
        // Only set to idle if no direction key is pressed
        if (!directionKeyPressed) {
            direction = "idle";
        }
        
        // Tile Collision
        collision = false;
        gp.cc.checkTile(this);        

        // Boat Collision
        int index = gp.cc.checkBoat(this, true);
        Interact(index);

        int creatures = gp.cc.checkEntity(this, gp.hostile);
        Battle(creatures);
    
        if(collision == false){
            switch (direction) {
                case "down": Worldy += speed; break;
                case "left": Worldx -= speed; break;
                case "right": Worldx += speed; break;
                case "up": Worldy -= speed; break;
            }
        }

        //System.out.println("Worldx: " + Worldx + " Worldy: " + Worldy);
    }


    public void attack() {
        for (int i = 0; i < gp.hostile.length; i++) {
            if (gp.hostile[i] != null && gp.hostile[i] instanceof Bear && canAttackBear(gp.hostile[i])) {
                Bear bear = (Bear) gp.hostile[i];
                bear.life -= 1;
                if (bear.life <= 0) {
                    isDead = true;
                    gp.hostile[i] = null;
                    gp.ui.showMessage("you have slain a bear!");
                }
            }
        }
    
        for (int i = 0; i < gp.neutral.length; i++) {
            if (gp.neutral[i] != null && gp.neutral[i] instanceof Deer && canAttackDeer(gp.neutral[i])) {
                Deer deer = (Deer) gp.neutral[i];
                deer.life -= 1;
                if (deer.life <= 0) {
                    gp.neutral[i] = null;
                    gp.ui.showMessage("you have slain a deer!");
                }
            }
        }
    }


    public void Battle(int index){
        if(index != 99){
            if (index != 99 && gp.hostile[index] != null) {
                Entities bear = gp.hostile[index];
                bear.life -= 1; // Decrement the life of the Bear
                if (bear.life <= 0) {
                    gp.hostile[index] = null; // Remove the Bear from the game
                    // Add any additional logic for handling a defeated Bear
                }
            }
        
        }
    }

    public boolean canAttackDeer(Entities neutral) {
        // Check if the player is within a certain distance from the deer
        int distanceX = Math.abs(Worldx - neutral.Worldx);
        int distanceY = Math.abs(Worldy - neutral.Worldy);
        int attackRange = gp.getTileSize()+3; // Adjust the attack range as needed
    
        return (distanceX <= attackRange && distanceY <= attackRange);
    }

    private boolean canAttackBear(Entities hostile) {
        // Check if the player is within a certain distance from the bear
        int distanceX = Math.abs(Worldx - hostile.Worldx);
        int distanceY = Math.abs(Worldy - hostile.Worldy);
        int attackRange = gp.getTileSize()+3; // Adjust the attack range as needed
    
        return (distanceX <= attackRange && distanceY <= attackRange);
    }



    public void Interact(int index){
        if(index !=99){
            String object = gp.obj[index].getName();
            switch(object){
                case "Key":
                    hasKey = 1;
                    gp.obj[index] = null;
                    gp.ui.showMessage("You have found the Skull key!");
                    break;
                // Further implementation of other objects and entities. If has Key and 5 bear entities and 2 deer entities killed, move to the next level.
                case "Boat":
                    gp.gameState = gp.dialoueState;
                    if(hasKey == 1 ){
                        //gp.ui.showMessage("You have escaped the island!");
                        gp.getSoundManager().stopClip("level1_loop");
                        gp.getSoundManager().playClip("level2_intro", false);
                        
                        //gp.ui.getLevelComplete();
                    }
                    else{
                        gp.ui.Mission = "The is broken. Return to the island and find the key.";
                    }
                    break;
            }
        }

    }
    

    public void Image(){
        walking = new StripAnimation("images//character//Walk.png", 7, 100);
        idle = new StripAnimation("images//character//Idle.png", 8, 100);
        die = new StripAnimation("images//character//dieNob.png", 5, 200);
        attack = new StripAnimation("images//character//attackNob.png", 4, 100);
    }



    public void draw(Graphics2D g2d) {
        int size = gp.getTileSize() * 2;
        BufferedImage image = null;
    
        if (isDead) {
            image = die.getImage();
        } else {
            switch (direction) {
                case "up":
                    image = walking.getImage();
                    image = rotateImageByDegrees(image, 270);
                    break;
                case "down":
                    image = walking.getImage();
                    image = rotateImageByDegrees(image, 90);
                    break;
                case "left":
                    image = walking.getImage();
                    image = flipImageHorizontally(image);
                    break;
                case "right":
                    image = walking.getImage();
                    break;
                default:
                    image = idle.getImage();
                    break;
            }
        }
    
        g2d.drawImage(image, screenX, screenY, size, size, null);
        g2d.setColor(Color.RED);
        g2d.drawRect(screenX, screenY, size, size);
    }
    
    private BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);
    
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);
        at.rotate(rads, w / 2, h / 2);
    
        AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(img, rotated);
    
        return rotated;
    }

    private BufferedImage flipImageHorizontally(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flipped = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                flipped.setRGB(w - x - 1, y, img.getRGB(x, y));
            }
        }
        return flipped;
    }


    public int getWorldX(){
        return Worldx;
    }

    public int getWorldY(){
        return Worldy;
    }

    public int getScreenX(){
        return screenX;
    }

    public int getScreenY(){
        return screenY;
    }
    
    public int getHealth(){
        return health;
    }

    public Image getImage(){
        return idle.getImage();
    }

    public int getLife(){
        return life;
    }
}
