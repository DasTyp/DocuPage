package zork;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class creates the window with a picture of the map. The picture is resized if needed.
 * It extends the class JFrame
 * @author Patrick Mayer
 * @version 21.08.2021
 */
public class Map extends JFrame {

    /**
     * JLabel where the map image is placed
     */
    JLabel map;
    /**
     * JPanel where JLabel is placed. Panel is placed in the window.
     */
    JPanel panel;
    /**
     * Image of the map
     */
    BufferedImage mapImage = null;
    /**
     * Size of the monitor of the used device.
     */
    double screenWidth, screenHeight;
    /**
     * current size of the mapImage
     */
    int imageWidth, imageHeight;

    /**
     * Contructor of class Map
     * reads the image and places it in mapImage
     * resizes image if needed with screensize and imagesize
     * It sets position to the middle of the screen, adds the image and the size of the window,
     * sets the window visible, sets the title of the window sets closing operation
     * @author Patrick Mayer
     * @param titleOfWindow title of the window
     * @param mapPath Path of the image of the map
     */
    public Map( String titleOfWindow, String mapPath) {
        try {
            mapImage = readImage(mapPath);
        }
        catch (IOException ex){
            System.out.println("Error resizing the image.");
            ex.printStackTrace();
        }
        setScreenSize();
        setImageSize(mapImage);
        if(isScreenToSmall()){
                mapImage = resize(mapImage, ((screenWidth / imageWidth) - 0.25));
                setImageSize(mapImage);
        }
        setTitle(titleOfWindow);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //window will be closed in the background too
        setAlwaysOnTop(true);
        map = new JLabel();
        panel = new JPanel();
        map.setIcon(new ImageIcon(mapImage));
        panel.add(map);
        panel.setBounds(0, 0, imageWidth, imageHeight);
        panel.setPreferredSize(new Dimension(imageWidth,imageHeight));
        this.add(panel);
        this.pack();
        setLocationRelativeTo(null); //Window will appear in the middle of tge screen
        revalidate();
        setVisible(true);
    }

    /**
     * reads an image of the given path and retruns it
     * @author Patrick Mayer
     * @param inputImagePath Path of the image, which shoud be read
     * @return Bufferedimage with the image
     * @throws IOException if import fails
     */
    private BufferedImage readImage(String inputImagePath) throws IOException{
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        return inputImage;
    }

    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @author Patrick Mayer
     * @param inputImage Path of the original image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     */
    private BufferedImage resize(BufferedImage inputImage, int scaledWidth, int scaledHeight) {

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // returns output image
        return outputImage;
    }

    /**
     * Resizes an image by a percentage of original size (proportional).
     * @author Patrick Mayer
     * @param inputImage Path of the original image
     * @param percent a double number specifies percentage of the output image over the input image.
     */
    private BufferedImage resize(BufferedImage inputImage, double percent){
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        return resize(inputImage, scaledWidth, scaledHeight);
    }

    /**
     * sets screenWidth and screenHeight to the current size of the monitor
     * @author Patrick Mayer
     */
    private void setScreenSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
    }

    /**
     * sets imageHeight and imageWidth to the current size of the given image
     * @author Patrick Mayer
     * @param image image of which the size is set
     */
    private void setImageSize(BufferedImage image){
        imageHeight = image.getHeight();
        imageWidth = image.getWidth();
    }

    /**
     * Checks if the screen is to small for the image
     * @author Patrick Mayer
     * @return boolean if screen is to small for the image
     */
    private boolean isScreenToSmall(){
        return screenWidth <= 1700 || screenHeight <= 1000;
    }
}
