import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class GameWindow extends JFrame implements ActionListener{

    private JLabel inventory, questItems, health, status, gamelevel;
    private JPanel inventoryPanel, questItemsPanel, healthPanel, main, controls;
    //private GamePanel gamePanel;


    private JTextArea inventoryText, questItemsText, levelText;
    private JScrollPane inventoryScroll, questItemsScroll, healthScroll, statusScroll, levelScroll;
    private JButton start, quit, pause, restart;
    
    //private Keylisteners keylisteners;
    private Container c;

    @SuppressWarnings("unchecked")
    public GameWindow(){
        setTitle("Nomad");
        setSize(1200, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(200, 0);

        // User Interface Components
        inventory = new JLabel("Inventory");
        questItems = new JLabel("Quest Items");
        health = new JLabel("Health");
        status = new JLabel("Status");
        gamelevel = new JLabel("Game-Level");

        // Panel Fields
        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        inventoryPanel.add(inventory);
        //inventoryPanel.add(inventoryScroll);


        questItemsPanel = new JPanel();
        questItemsPanel.setLayout(new BoxLayout(questItemsPanel, BoxLayout.Y_AXIS));
        questItemsPanel.add(questItems);
        //questItemsPanel.add(questItemsScroll);


        healthPanel = new JPanel();
        healthPanel.setLayout(new BoxLayout(healthPanel, BoxLayout.Y_AXIS));
        healthPanel.add(health);

        
        controls = new JPanel();
        main = new JPanel();

        // Text Fields
        inventoryText = new JTextArea(10, 20);
        questItemsText = new JTextArea(10, 20);
        levelText = new JTextArea(10, 20);

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
        inventoryScroll = new JScrollPane(inventoryText);
        questItemsScroll = new JScrollPane(questItemsText);
        levelScroll = new JScrollPane(levelText);

        // Game Panel
        //gamePanel = new GamePanel();
        //gamePanel.setPreferredSize(new Dimension(1100, 950));

        //Main Panel
        //main.add(gamePanel);
        main.add(controls);


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
            //gamePanel.startGame();
        }
        if(e.getSource() == quit){
            System.exit(0);
        }
        if(e.getSource() == pause){
            //gamePanel.pauseGame();
        }
        if(e.getSource() == restart){
            //gamePanel.restartGame();
        }
    }
    
}
