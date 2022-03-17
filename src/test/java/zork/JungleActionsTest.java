package zork;

import controller.GameController;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JungleActionsTest {
    Game game;
    GameController gameController;

    /**
     * Set up test data
     */
    public JungleActionsTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
    }

    @Test
    void BunnySpawned(){
        game.player.setRoomName("sparse jungle");
        assertTrue(game.getCurrentRoom().getRoomEnemyList().isEmpty());
        assertFalse(game.getCurrentRoom().getRoomNonItemList().isEmpty());
        List<Item> item = game.getCurrentRoom().getNonItem("paw print").getContainedItemsList();
        item.get(0).setName("15");
        gameController.processInput("track paw print");
        assertFalse(game.getCurrentRoom().getRoomEnemyList().isEmpty());
        assertFalse(game.getCurrentRoom().getRoomNonItemList().isEmpty());
    }

    @Test
    void TracksMoved(){
        game.player.setRoomName("sparse jungle");
        assertTrue(game.getCurrentRoom().getRoomEnemyList().isEmpty());
        assertFalse(game.getCurrentRoom().getRoomNonItemList().isEmpty());
        gameController.processInput("track paw print");
        assertTrue(game.getCurrentRoom().getRoomEnemyList().isEmpty());
        assertTrue(game.getCurrentRoom().getRoomNonItemList().isEmpty());
    }

    @Test
    void wrongTrack(){
        game.player.setRoomName("sparse jungle");
        assertTrue(game.getCurrentRoom().getRoomEnemyList().isEmpty());
        assertFalse(game.getCurrentRoom().getRoomNonItemList().isEmpty());
        gameController.processInput("track somethingsomething");
        assertTrue(game.getCurrentRoom().getRoomEnemyList().isEmpty());
        assertFalse(game.getCurrentRoom().getRoomNonItemList().isEmpty());
    }

    @Test
    void bananaNoRope(){
        game.player.setRoomName("dense jungle");
        assertFalse(game.player.inventory.hasItem("rope"));
        assertNull(game.getCurrentRoom().getItem("banana"));
        gameController.processInput("use rope with banana tree");
        assertNull(game.getCurrentRoom().getItem("banana"));
    }

    @Test
    void bananaWithRope(){
        Item rope = new Item("rope","test","fixed",0,"test");
        game.player.inventory.addItem(rope);
        game.player.setRoomName("dense jungle");
        assertTrue(game.player.inventory.hasItem(rope));
        assertNull(game.getCurrentRoom().getItem("banana"));
        gameController.processInput("use rope with banana tree");
        assertFalse(game.player.inventory.hasItem(rope));
        assertNotNull(game.getCurrentRoom().getItem("banana"));
    }
}
