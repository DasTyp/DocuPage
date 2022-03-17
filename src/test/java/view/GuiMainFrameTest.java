package view;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuiMainFrameTest {
    private static FrameFixture window;
    private static GuiMainFrame frame;

    /**
     * Method to build gui before testing
     */
    @BeforeAll
    public static void onSetUp(){
        frame = GuiActionRunner.execute(GuiMainFrame::getInstance);
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
     * Tests if GuiMainFrame is Visible
     */
    @Test
    @DisplayName("Tests if GuiMainFrame is Visible")
    public void testAllVisible(){
        window.requireVisible();
        window.requireTitle("Zork4");

        JPanelFixture leftPanel = window.panel("leftPanel");
        JPanelFixture rightPanel = window.panel("rightPanel");
        JPanelFixture mainPanel = window.panel("mainPanel");

        leftPanel.requireVisible();
        rightPanel.requireVisible();
        mainPanel.requireVisible();
    }
}