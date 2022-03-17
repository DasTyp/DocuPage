package view;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MapPanelTest {
    private static FrameFixture window;
    private static GuiMainFrame frame;
    private static MapPanel panel;

    /**
     * Method to build gui before testing
     */
    @BeforeAll
    public static void onSetUp(){
        frame = GuiActionRunner.execute(GuiMainFrame::getInstance);
        panel = MapPanel.getInstance(frame);
        frame.addMain(panel);
        panel.openMap();
        window = new FrameFixture(MapPanelTest.frame);
        window.show();
        window.resizeTo(new Dimension(500,500));
    }

    /**
     * Method to clean up the gui after the tests
     */
    @AfterAll
    public static void afterAll(){
        window.cleanUp();
    }

    /**
     * Tests if Map is visible and becomes invisible after clicking exitButton.
     */
    @Test
    @DisplayName("Tests if Map is visible and becomes invisible after clicking exitButton.")
    public void testMapVisibility(){
        JPanelFixture mainPanel = window.panel("mainPanel");
        JPanelFixture mapPanel = mainPanel.panel();
        JLabelFixture imageLabel = mapPanel.label("imageLabel");
        JButtonFixture exitButton = mapPanel.button();

        mapPanel.requireVisible();
        imageLabel.requireVisible();
        exitButton.requireVisible();

        exitButton.click();

        mapPanel.requireVisible();
        imageLabel.requireVisible();
        exitButton.requireVisible();
    }
}