package zork;

import javax.swing.*;

public class Window extends JFrame {

    public Window(int width, int height, String titleOfWindow) {
        setLayout(null); //deactivate Layout-Manager to place window absolut
        setVisible(true);
        setSize(width, height);
        setTitle(titleOfWindow);
        setLocationRelativeTo(null); //Window will appear in the middle of tge screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //window will be closed in the background too
    }
}
