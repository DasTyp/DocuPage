package zork;

import javax.swing.*;

/**
 * With this class you can create a window
 * It extends the class JFrame
 */
public class Window extends JFrame {

    /**
     * Contructor of class Window
     * Deactivates Layout-Manager, sets the window visible, sets the title of the window,
     * sets closing operation
     * @param width width of window (positive)
     * @param height height of window (positive)
     * @param titleOfWindow title of window
     */
    public Window(int width, int height, String titleOfWindow) {
        if(width <=0 || height <= 0) {
            throw new ArithmeticException("Width or Height of the Window is negative or zero");
        }
        setVisible(true);
        setTitle(titleOfWindow);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //window will be closed in the background too
    }
}
