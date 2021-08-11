package zork;

import javax.swing.*;

/**
 * With this class you can create a window
 * It extends the class JFrame
 */
public class Window extends JFrame {

    /**
     * Contructor of class Window
     * Deactivates Layout-Manager, sets the window visible, sets the size and the title of the window,
     * sets position to the middle of the screen, sets closing operation
     * @param width width of window (positive)
     * @param height height of window (positive)
     * @param titleOfWindow title of window
     */
    public Window(int width, int height, String titleOfWindow) {
        setLayout(null); //deactivate Layout-Manager to place window absolut
        setVisible(true);
        setSize(width, height);
        setTitle(titleOfWindow);
        setLocationRelativeTo(null); //Window will appear in the middle of tge screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //window will be closed in the background too
    }
}
