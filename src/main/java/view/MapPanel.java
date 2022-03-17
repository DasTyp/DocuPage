package view;

import zork.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class creates the window with a picture of the map. The picture is resized if needed.
 * It extends the class JFrame
 * @author Patrick Mayer
 * @version 21.08.2021
 */
public class MapPanel extends JLayeredPane {

    /**
     * JLabel where the map image is placed
     */
    JLabel map = new JLabel();

    /**
     * Button to close map
     */
    JButton exit = new JButton("X");

    /**
     * Original mage of the map
     */
    Image mapImage = null;

    /**
     * Displayed image of the map
     */
    Image displayedImage = null;

    /**
     * Size of the monitor of the used device.
     */
    double screenWidth, screenHeight;

    /**
     * GUI size
     */
    int guiWidth, guiHeight;

    /**
     * current size of the mapImage
     */
    int imageWidth, imageHeight;

    /**
     * GUI object
     */
    GuiMainFrame guiObject;

    /**
     * Variable of instance of class
     */
    private static MapPanel INSTANCE;

    /**
     * Constructor of class Map
     * reads the image and places it in mapImage
     * resizes image if needed with screen size and image size
     * It sets position to the middle of the screen, adds the image and the size of the window,
     * sets the window visible, sets the title of the window sets closing operation
     * @author Patrick Mayer
     */

    public static MapPanel getInstance(GuiMainFrame gui){
        if(INSTANCE == null){
            INSTANCE = new MapPanel(gui);
        }
        return INSTANCE;
    }
    private MapPanel(GuiMainFrame gui) {
        try {
            InputStream instream = MapPanel.class.getResourceAsStream(Constants.MAP);
            mapImage = ImageIO.read(instream);
        }
        catch (IOException ex){
            System.out.println("Error resizing the image.");
            ex.printStackTrace();
        }

        guiObject = gui;
        displayedImage = mapImage;
        imageHeight = mapImage.getHeight(null);
        imageWidth = mapImage.getWidth(null);
        exit.setAlignmentX(1.0f);
        exit.setAlignmentY(0.0f);
        map.setAlignmentX(1.0f);
        map.setAlignmentY(0.0f);
        exit.addActionListener(new ActionListener() { // add functionality to exit button
            @Override
            public void actionPerformed(ActionEvent e) {
                closeMap();
            }
        });
        map.setIcon(new ImageIcon(displayedImage));
        this.setLayout(new OverlayLayout(this));
        setImageSize(gui.getSize().width, gui.getSize().height);
        this.add(map,JLayeredPane.DEFAULT_LAYER);
        this.add(exit,JLayeredPane.PALETTE_LAYER);
        this.setVisible(false);
        //for tests
        this.setName("mapPanel");
        map.setName("imageLabel");
    }

    /**
     * Mathod to close the Map
     */
    private void closeMap(){
        this.setVisible(false);
        guiObject.hideContent();
    }

    /**
     * Method to open the Map
     */
    public void openMap(){
        guiObject.hideContent();
        this.setVisible(true);
    }

    /**
     * Method to resize the image
     * @param width the width of the mainPanel
     * @param height the height of the mainPanel
     */
    public void setImageSize(double width, double height) {
        if (width != 0 && height != 0) {
            this.setMaximumSize(new Dimension((int)width, (int)height));
            height = height - this.exit.getSize().height;
            // Check to scale to proper aspect ratio
            if ((height / (double) imageHeight) <= (width / (double) imageWidth)) {
                displayedImage = mapImage.getScaledInstance(-1, (int) height, Image.SCALE_DEFAULT);
            } else {
                displayedImage = mapImage.getScaledInstance((int) width, -1, Image.SCALE_DEFAULT);
            }
            map.setIcon(new ImageIcon(displayedImage));
        }
    }
}
