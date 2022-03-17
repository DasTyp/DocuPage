package view.mocks;

import view.OptionPane;

import javax.swing.*;
import java.awt.*;

/**
 * Mock for JOptionPane, ConfirmDialog returns yes
 *
 * @version 17.03.2022
 * @author Yvonne Rahnfeld
 */
public class MockOptionPaneYes extends OptionPane {
    @Override
    public int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        return JOptionPane.YES_OPTION;
    }
}
