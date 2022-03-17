package view;

import controller.GameController;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.junit.jupiter.api.*;
import zork.Game;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class GamePanelTest {
    private static FrameFixture window;
    private static GuiMainFrame frame;
    private static GamePanel panel;

    /**
     * Method to build gui before testing
     */
    @BeforeAll
    public static void onSetUp(){
        frame = GuiActionRunner.execute(GuiMainFrame::getInstance);
        panel = GuiActionRunner.execute(GamePanel::getInstance);
        frame.addLeft(panel);
        window = new FrameFixture(frame);
        window.show();
    }


    /**
     * Method to clean up the gui after the tests
     */
    @AfterAll
    public static void afterAll(){
        window.cleanUp();
    }

    /**
     * Tests if gamePanel is visible
     */
    @Test
    @DisplayName("Tests if gamePanel is visible")
    public void testGamePanel(){
        JPanelFixture gamePanel = window.panel("gamePanel");
        assertNotNull(gamePanel);
        gamePanel.requireVisible();
    }

    /**
     * Tests if inputTextField works properly
     */
    @Test
    @DisplayName("Tests if inputTextField works properly")
    public void testInputField(){
        JTextComponentFixture inputField = window.textBox("gameInput");
        assertNotNull(inputField);
        inputField.requireVisible();
        inputField.requireEditable();
        inputField.requireEmpty();
        inputField.pressAndReleaseKeys(KeyEvent.VK_T, KeyEvent.VK_E, KeyEvent.VK_S, KeyEvent.VK_T);
        inputField.requireText("test");
    }

    /**
     * Tests if outputTextArea works properly
     */
    @Test
    @DisplayName("Tests if outputTextArea works properly")
    public void testOutputField(){
        JTextComponentFixture outputField = window.textBox("outputTextArea");

        panel.addToOutput("TEST");
        String actual = outputField.text();
        assertEquals(actual, "<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body>\n" +
                "    TEST\n" +
                "  </body>\n" +
                "</html>\n");
    }
}