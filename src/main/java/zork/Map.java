package zork;

import javax.swing.*;

/**
 * This class creates the window with a picture of the map
 * It extends the class Window
 */
public class Map extends Window{

    private int width, height;

    /**
     * Contructor of class Map
     * It calls the Constructor of Window and sets the variables width and height
     * @param width width of the window and the map
     * @param height height of the window and the map
     * @param titleOfWindow title of the window
     */
    public Map(int width, int height, String titleOfWindow) {
        super(width, height, titleOfWindow);
        setAlwaysOnTop(true);
        this.width = width;
        this.height = height;
    }

    /**
     * Method init
     * It creates a JLabel, in which the image of the map is placed. Afterwards the label is added to the window.
     */
    public void init(){
        JLabel map = new JLabel();
        map.setIcon(new ImageIcon(Constants.MAP));
        JPanel panel = new JPanel();
        panel.add(map);
        panel.setBounds(0,0,width,height);
        this.add(panel);
        revalidate();
    }
}
