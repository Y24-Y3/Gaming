


public class Boat_Object extends Objects{

    private String dialogue;

    public Boat_Object() {

        img1 = ImageManager2.loadImage("images//boat//broken_boat.png");
        //img2 = ImageManager.loadImage("images//boat//broken_boat.png");
        boundsX = 50;
        boundsY = 50;
        collision = true;
        name = "Boat";
    }


}