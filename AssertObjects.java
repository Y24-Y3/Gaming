import java.util.Random;

public class AssertObjects {
    public GamePanel gp;

    public AssertObjects(GamePanel gp) {
        this.gp = gp;
        
    }

    public void setObjects(){
        gp.obj[0] = new Boat_Object();
        gp.obj[0].Worldx = gp.getTileSize() * 48;
        gp.obj[0].Worldy = gp.getTileSize() * 12;

        gp.obj[1] = new Key_Object();
        gp.obj[1].Worldx = gp.getTileSize() * 12;
        gp.obj[1].Worldy = gp.getTileSize() * 8;

    }

    public void setHostile(){
        gp.hostile[0] = new Bear(gp);
        gp.hostile[0].Worldx = gp.getTileSize() * 18;
        gp.hostile[0].Worldy = gp.getTileSize() * 29;

        gp.hostile[1] = new Bear(gp);
        gp.hostile[1].Worldx = gp.getTileSize() * 33;
        gp.hostile[1].Worldy = gp.getTileSize() * 19;

        gp.hostile[2] = new Bear(gp);
        gp.hostile[2].Worldx = gp.getTileSize() * 38;
        gp.hostile[2].Worldy = gp.getTileSize() * 29;

        gp.hostile[3] = new Bear(gp);
        gp.hostile[3].Worldx = gp.getTileSize() * 38;
        gp.hostile[3].Worldy = gp.getTileSize() * 42;

        gp.hostile[4] = new Bear(gp);
        gp.hostile[4].Worldx = gp.getTileSize() * 52;
        gp.hostile[4].Worldy = gp.getTileSize() * 42;

        gp.hostile[5] = new Bear(gp);
        gp.hostile[5].Worldx = gp.getTileSize() * 52;
        gp.hostile[5].Worldy = gp.getTileSize() * 26;

        gp.hostile[6] = new Bear(gp);
        gp.hostile[6].Worldx = gp.getTileSize() * 52;
        gp.hostile[6].Worldy = gp.getTileSize() * 12;

        gp.hostile[7] = new Bear(gp);
        gp.hostile[7].Worldx = gp.getTileSize() * 38;
        gp.hostile[7].Worldy = gp.getTileSize() * 12;
    }

    public void setNeutral(){
        gp.neutral[0] = new Deer(gp);
        gp.neutral[0].Worldx = gp.getTileSize() * 15;
        gp.neutral[0].Worldy = gp.getTileSize() * 36;

        gp.neutral[1] = new Deer(gp);
        gp.neutral[1].Worldx = gp.getTileSize() * 15;
        gp.neutral[1].Worldy = gp.getTileSize() * 12;

        gp.neutral[2] = new Deer(gp);
        gp.neutral[2].Worldx = gp.getTileSize() * 25;
        gp.neutral[2].Worldy = gp.getTileSize() * 12;

    }
}
