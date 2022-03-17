package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommunicationControllerTest {

    @Test
    @DisplayName("getInstance() always returns a CommunicationController-Object")
    public void testGetInstance(){
        assertNotNull(CommunicationController.getInstance());   //make sure getInstance doesn't return null
        assertSame(CommunicationController.getInstance(), CommunicationController.getInstance());   //make sure that getInstance always returns same instance
        assertEquals(CommunicationController.getInstance().getClass().getName(), "controller.CommunicationController"); //make sure return is a CommunicationController-object
    }

    @Test
    @DisplayName("setCurrentTopic() always sets the current topic correctly")
    public void testSetCurrentTopic(){
        CommunicationController.getInstance().setCurrentTopic("test_topic");
        assertEquals("test_topic", CommunicationController.getInstance().getCurrentTopic());
    }

    @Test
    @DisplayName("setPlayersName() always sets the players name correctly")
    public void testSetPlayerName(){
        CommunicationController.getInstance().setPlayerName("test_name");
        assertEquals(CommunicationController.getInstance().getPlayerName(), "test_name");
    }
}
