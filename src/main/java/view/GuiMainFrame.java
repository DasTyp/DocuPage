package view;


import controller.GameController;
import zork.Zork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * This class handles the main frame to attach UI to and allows adding new sub-windows.
 * It extends the class JFrame
 * @author Christian Litke
 * @version 11.02.2022
 */

public class GuiMainFrame extends JFrame {
    /**
     * JPanel for the left side of the GUI
     */
    private JPanel leftpanel;

    /**
     * JPanel for the right side of the GUI
     */
    private JPanel rightpanel;

    /**
     * JPanel for the hole size of the GUI
     */
    private JPanel mainpanel;

    /**
     * width of the GUI
     */
    private int width;

    /**
     * height of the GUI
     */
    private int height;

    /**
     * Instance of the class (Singleton)
     */
    private static GuiMainFrame INSTANCE;

    /**
     * Method to get Instance of the class (Singleton)
     * @return
     */
    public static GuiMainFrame getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GuiMainFrame();
        }
        return INSTANCE;
    }

    /**
     * Constructor of the class
     * sets System look and feel
     * initializes the GUI
     */
    private GuiMainFrame(){

        //set System look and feel
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e){
            // ignore error due to no influence on game
        }

        //set GUI Size to maximized window
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setVisible(true);
        setscreenSize();
        this.setPreferredSize(new Dimension(width,height));
        setTitle("Zork4");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Zork.saveGame(Zork.game);
                Zork.exitGame();
            }
        });
        leftpanel = new JPanel();
        rightpanel = new JPanel();
        mainpanel = new JPanel();
        mainpanel.setPreferredSize(new Dimension(width,height));

        leftpanel.setName("leftPanel");
        rightpanel.setName("rightPanel");
        mainpanel.setName("mainPanel");

        mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.LINE_AXIS)); // create main container with left-to-right arrangement

        leftpanel.setLayout(new BoxLayout(leftpanel, BoxLayout.PAGE_AXIS)); // create left container with up-down arrangement
        leftpanel.setBorder(BorderFactory.createLoweredBevelBorder());
        leftpanel.setMaximumSize(new Dimension((int)(width*0.7),height)); // ensure added panels do not go over size

        rightpanel.setLayout(new BoxLayout(rightpanel, BoxLayout.PAGE_AXIS)); // create right container with up-down arrangement
        rightpanel.setBorder(BorderFactory.createLoweredBevelBorder());
        rightpanel.setMaximumSize(new Dimension((int)(width*0.3),height));  // ensure added panels do not go over size

        mainpanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = mainpanel.getSize().width;
                height = mainpanel.getSize().height;
                leftpanel.setMaximumSize(new Dimension((int)(width*0.7),height));
                rightpanel.setMaximumSize(new Dimension((int)(width*0.3),height));
                MapPanel.getInstance(INSTANCE).setImageSize(width, height);
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        this.add(mainpanel);
        mainpanel.add(leftpanel);
        mainpanel.add(Box.createRigidArea(new Dimension(5,0)));
        mainpanel.add(rightpanel);
        this.pack();
        setLocationRelativeTo(null);
        revalidate();
    }
    /**
     * Adds a Swing Panel to the left area of the gui
     * @author Christian Litke
     * @param addedPanel Panel to be added to the left margin
     */
    public void addLeft(JComponent addedPanel){
        leftpanel.add(addedPanel);
        leftpanel.add(Box.createRigidArea(new Dimension(0,5)));
    }
    /**
     * Adds a Swing Panel to the right area of the gui
     * @author Christian Litke
     * @param addedPanel Panel to be added to the right margin
     */
    public void addRight(JComponent addedPanel){
        rightpanel.add(addedPanel);
        rightpanel.add(Box.createRigidArea(new Dimension(0,5)));
    }
    /**
     * Hides/Unhides gui content
     * @author Christian Litke
     */
    public void hideContent(){
        if(leftpanel.isVisible()){
            leftpanel.setVisible(false);
            rightpanel.setVisible(false);
        }
        else{
            leftpanel.setVisible(true);
            rightpanel.setVisible(true);
        }
    }

    /**
     * Displays a Swing panel over the entire GUI
     * @author Christian Litke
     * @param addedPanel Panel to be displayed
     */
    public void addMain(JLayeredPane addedPanel){
        mainpanel.add(addedPanel);
    }

    /**
     * Method to set the width and height to the size of the screen
     */
    private void setscreenSize(){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        this.width = bounds.width;
        this.height = bounds.height;
    }
}
