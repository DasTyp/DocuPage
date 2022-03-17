package view.mocks;

import view.OptionPane;

import java.awt.*;

/**
 * Mock for JOptionPane, blocks showing up
 *
 * @version 17.03.2022
 * @author Yvonne Rahnfeld
 */
public class MockMessageDialog extends OptionPane {
    @Override
    public void showMessageDialog(Component parentComponent, Object message) {
    }
}