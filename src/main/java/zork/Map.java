package zork;

import javax.swing.*;
import java.awt.*;

/**
 * This class creates the window with a picture of the map
 * It extends the class JFrame
 * @author Patrick Mayer
 * @version 19.08.2021
 */
public class Map extends JFrame {

    JLabel map;
    JPanel panel;

    /**
     * Contructor of class Map
     * It  sets position to the middle of the screen, adds the image and the size of the window,
     * sets the window visible, sets the title of the window sets closing operation
     * @author Patrick Mayer
     * @version 19.08.2021
     *
     * @param width         width of the window and the map
     * @param height        height of the window and the map
     * @param titleOfWindow title of the window
     * @param mapPath Path of the image of the map
     */
    public Map(int width, int height, String titleOfWindow, String mapPath) {
        if(width <=0 || height <= 0) {
            throw new ArithmeticException("Width or Height of the Window is negative or zero");
        }
        setTitle(titleOfWindow);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //window will be closed in the background too
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
        setVisible(true);
    }
}
