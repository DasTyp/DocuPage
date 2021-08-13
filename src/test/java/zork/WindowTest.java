package zork;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method to test, if the window exists, is visible, has the right size and title.
 */
class WindowTest {
    @Test
    public void testWindow(){
        Window testWindow = new Window(400,400,"Test");
        assertNotNull(testWindow);
        assert testWindow.isVisible();
        assert testWindow.contains(0,0);
        assert testWindow.contains(399,399);
        assert Objects.equals(testWindow.getTitle(), "Test");
    }

}