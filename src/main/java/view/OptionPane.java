package view;

import javax.swing.*;
import java.awt.*;

/**
 * For message and option panes (necessary for mocks)
 *
 * @version 17.03.2022
 * @author Yvonne Rahnfeld
 */
public class OptionPane {
    /**
     *  @see JOptionPane#showConfirmDialog(Component, Object, String, int);
     */
    public int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType);
    }

    /**
     *  @see JOptionPane#showMessageDialog(Component, Object);
     */
    public void showMessageDialog(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }
}
