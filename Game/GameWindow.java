import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class GameWindow extends JFrame implements ActionListener, KeyListener{

    // labels
    private JLabel questItems, health, status, gamelevel;

    // text areas
    private JTextArea questItemsText, levelText;

    // scroll panes
    //private JScrollPane inventoryScroll, questItemsScroll, healthScroll, statusScroll, levelScroll;

    // buttons
    private JButton start, quit, pause, restart;
    
    // panels
    private JPanel questItemsPanel, healthPanel, main, controls;
    private GamePanel gamePanel;
    
    //private Keylisteners keylisteners;
    private Container c;

    @SuppressWarnings("unchecked")
    public GameWindow(){
        setTitle("Nomad");
        setSize(1200, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(200, 0);

        // User Interface Components
        questItems = new JLabel("Quest Items");
        health = new JLabel("Health");
        status = new JLabel("Status");
        gamelevel = new JLabel("Game-Level");


        questItemsPanel = new JPanel();
        questItemsPanel.setLayout(new BoxLayout(questItemsPanel, BoxLayout.Y_AXIS));
        questItemsPanel.add(questItems);
        //questItemsPanel.add(questItemsScroll);


        healthPanel = new JPanel();
        healthPanel.setLayout(new BoxLayout(healthPanel, BoxLayout.Y_AXIS));
        healthPanel.add(health);

        
        controls = new JPanel();
        controls.setBackground(Color.ORANGE);
        main = new JPanel();
        main.setBackground(Color.RED);

        // Text Fields
        questItemsText = new JTextArea(10, 20);
        levelText = new JTextArea(10, 20);

		questItemsText.setEditable(false);
		levelText.setEditable(false);

        // buttons
        start = new JButton("Start Game");
        quit = new JButton("Quit");
        pause = new JButton("Pause");
        restart = new JButton("Restart");

        start.addActionListener(this);
        quit.addActionListener(this);
        pause.addActionListener(this);
        restart.addActionListener(this);
        controls.add(start);
        controls.add(quit);
        controls.add(pause);
        controls.add(restart);

        // Scroll Panes
        //questItemsScroll = new JScrollPane(questItemsText);
        //levelScroll = new JScrollPane(levelText);

        // Game Panel
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(1150, 780));

        //Main Panel
        main.add(gamePanel);
        main.add(controls);

        // msn must respond to keyboard and mouse
		main.addKeyListener(this);


        // Container
        c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(main, BorderLayout.CENTER);
        //c.add(controls, BorderLayout.SOUTH);
        setVisible(true);
        setResizable(false);

    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == start){
            gamePanel.startGame();
        }
        if(e.getSource() == quit){
            System.exit(0);
        }
        if(e.getSource() == pause){
            //gamePanel.pauseGame();

            // if (command.equals("Pause Game"))
			// 	pauseB.setText ("Resume");
			// else
			// 	pauseB.setText ("Pause Game");
        }
        if(e.getSource() == restart){
            //gamePanel.restartGame();
        }

        main.requestFocus();
    }
    // implement methods in KeyListener interface

	public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			gamePanel.moveLeft();
		}
		else
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			gamePanel.moveRight();
		}
        else
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			gamePanel.jump();
		}
        // else
		// if (e.getKeyCode() == KeyEvent.VK_UP) {
		// 	gamePanel.moveUp();
		// }
        // else
		// if (e.getKeyCode() == KeyEvent.VK_DOWN) {
		// 	gamePanel.moveDown();
		// }
		
	}

	public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			gamePanel.stopMoveLeft();
		}
		else
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			gamePanel.stopMoveRight();
		}
        else
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			gamePanel.stopJump();
		}

	}

	public void keyTyped(KeyEvent e) {
	}
    
}
