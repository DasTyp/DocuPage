package zork;

import controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CliffsActionsTest {

    Game game;
    GameController gameController;
    Item banana = new Item("banana", "", "", 0, "");
    Item key = new Item("key", "", "", 0, "");

    /**
     * Set up test data
     */
    public CliffsActionsTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
    }

    /**
     * Assert The apes are distracted and the way north is set to free after the input was use banana with apes.
     */
    @Test
    @DisplayName("useBananaWithApes -> Apes are distracted & way north is free")
    public void testUseBananaWithApes(){
        game.player.setRoomName("cliff wall");
        game.player.inventory.addItem(banana);
        assertFalse(game.getWayForDirection("north").isFree());
        assertNotNull(game.getCurrentRoom().getAnimal("apes"));
        gameController.processInput("use banana with apes");
        assertTrue(game.getWayForDirection("north").isFree());
        assertNull(game.getCurrentRoom().getAnimal("apes"));
        assertTrue(game.player.inventory.hasItem(banana));
    }
    /**
     * Assert Player got key and the way north is set to free after the input was open door with key.
     */
    @Test
    @DisplayName("UseDoorWithKey -> Door is open & way north is free")
    public void testUseDoorWithKey(){
        game.player.setRoomName("front of a wooden house");
        game.player.inventory.addItem(key);
        assertFalse(game.getWayForDirection("north").isFree());
        gameController.processInput("use key with door");
        assertTrue(game.getWayForDirection("north").isFree());
        assertTrue(game.player.inventory.hasItem(key));
    }
}
