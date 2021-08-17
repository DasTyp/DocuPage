package zork;

import javax.swing.*;
import java.awt.*;

/**
 * This class creates the window with a picture of the map
 * It extends the class Window
 */
public class Map extends Window {

    JLabel map;
    JPanel panel;

    /**
     * Contructor of class Map
     * It calls the Constructor of Window and sets position to the middle of the screen, adds the image and the size of the window
     *
     * @param width         width of the window and the map
     * @param height        height of the window and the map
     * @param titleOfWindow title of the window
     * @param mapPath Path of the image of the map
     */
    public Map(int width, int height, String titleOfWindow, String mapPath) {
        super(width, height, titleOfWindow);
        setAlwaysOnTop(true);
        map = new JLabel();
        panel = new JPanel();
        map.setIcon(new ImageIcon(mapPath));
        panel.add(map);
        panel.setBounds(0, 0, width, height);
        panel.setPreferredSize(new Dimension(width,height));
        this.add(panel);
        this.pack();
        setLocationRelativeTo(null); //Window will appear in the middle of tge screen
        revalidate();
    }
}
