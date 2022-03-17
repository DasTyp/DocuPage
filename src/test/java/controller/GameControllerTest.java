package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zork.*;

import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test GameController (most functionalities are already tested in class GameTest and ?ActionTests classes)
 */
public class GameControllerTest {

    Game game;
    GameController gameController;
    Item machete = new Item("machete", "", "", 0, "");
    String offeredItem = "machete";
    String requestedItem = "item2";
    String tradingPartner = "player2";
    String tradersHut = "traders hut";
    String tradingCommand = "trade " + offeredItem + " for " + requestedItem + " with " + tradingPartner;

    /**
     * Set up test data
     */
    public GameControllerTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Zork.game = game;
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
        game.getPlayer().inventory.addItem(machete);
    }

    @Test
    @DisplayName("Process trade offer, trading locked")
    public void testProcessTradeOffer_TradingLocked()
    {
        String expectedOutput = "Sorry, this is not possible. You haven't unlocked the trading feature yet!";
        assertTrue(game.getPlayer().inventory.hasItem(offeredItem));
        assertEquals(0, game.getRoom(tradersHut).getVisited());
        assertEquals(expectedOutput, gameController.processInput(tradingCommand));
    }

    @Test
    @DisplayName("Process trade offer, incorrect trade command")
    public void testProcessTradeOffer_IncorrectCommand()
    {
        String expectedOutput = "This command is not valid. If you want to start a trading offer to someone, the correct " +
                "command is:\n\"trade <offeredItem> for <requestedItem> with <tradingPartner>\"";
        game.getRoom(tradersHut).incrementVisited();
        assertTrue(game.getPlayer().inventory.hasItem(offeredItem));
        assertEquals(1, game.getRoom(tradersHut).getVisited());
        assertEquals(expectedOutput, gameController.processInput("trade something"));
    }

    @Test
    @DisplayName("Process trade offer, item not in inventory")
    public void testProcessTradeOffer_ItemNotInInventory()
    {
        String expectedOutput = "The item " + offeredItem + " is not in your inventory! You can only trade with items you own.";
        game.getRoom(tradersHut).incrementVisited();
        game.getPlayer().inventory.removeItem(offeredItem);
        assertFalse(game.getPlayer().inventory.hasItem(offeredItem));
        assertEquals(1, game.getRoom(tradersHut).getVisited());
        assertEquals(expectedOutput, gameController.processInput(tradingCommand));
    }

    @Test
    @DisplayName("Process trade offer, offer sent")
    public void testProcessTradeOffer_OfferSent()
    {
        String expectedOutput = "Your trading offer was sent to player " + tradingPartner +
                ".\nYou will be notified if the player has accepted your offer.";
        game.getRoom(tradersHut).incrementVisited();
        assertTrue(game.getPlayer().inventory.hasItem(offeredItem));
        assertEquals(1, game.getRoom(tradersHut).getVisited());
        assertEquals(expectedOutput, gameController.processInput(tradingCommand));
    }
}
