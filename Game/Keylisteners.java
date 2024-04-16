import java.awt.event.*;

import javax.swing.JFrame;

public class Keylisteners extends JFrame implements KeyListener {

    public Keylisteners() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        keyPressed(null);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) {
            System.out.println("UP");
        }
        if (code == KeyEvent.VK_DOWN) {
            System.out.println("DOWN");
        }
        if (code == KeyEvent.VK_LEFT) {
            System.out.println("LEFT");
        }
        if (code == KeyEvent.VK_RIGHT) {
            System.out.println("RIGHT");
        }
        if (code == KeyEvent.VK_SPACE) {
            System.out.println("Jump");
        }
        if (code == KeyEvent.VK_P){
            System.out.println("Shoot");
        }
        if (code == KeyEvent.VK_R){
            System.out.println("Reload");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}
