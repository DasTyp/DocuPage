package zork;

import controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class VillageActionsTest {
//TODO fix
    Game game;
    GameController gameController;
    Item mushroom = new Item("mushroom", "", "", 0, "");
    Item emptyJar = new Item("empty jar", "", "", 0, "");
    Item coconut = new Item("coconut", "", "", 0, "");
    Item berries = new Item("berries", "", "", 0, "");
    Item potion = new Item("healing potion", "", "", 0, "");
    Item seashell = new Item("sea shell", "", "", 0, "");
    Item machete = new Item("machete","","",20,"");

    /**
     * Set up test data
     */
    public VillageActionsTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
    }

    @Test
    @DisplayName("Test empty cauldron")
    public void testEmptyCauldron(){
        game.player.setRoomName("village center");
        game.player.inventory.addItem(emptyJar);

        gameController.processInput("use empty jar with cauldron");
        assertFalse(game.player.inventory.hasItem("healing potion"));
        assertTrue(game.player.inventory.hasItem("empty jar"));
    }

    @Test
    @DisplayName("Test if cauldron takes item twice")
    public void testDuplicateInCauldron(){
        game.player.setRoomName("village center");

        game.player.inventory.addItem(berries);
        gameController.processInput("use berries with cauldron");
        assertFalse(game.player.inventory.hasItem("berries"));

        game.player.inventory.addItem(berries);
        gameController.processInput("use berries with cauldron");
        assertTrue(game.player.inventory.hasItem("berries"));
    }

    @Test
    @DisplayName("Test if cauldron works properly")
    public void testCauldronWorks(){
        game.player.setRoomName("village center");
        game.player.inventory.addItem(mushroom);
        game.player.inventory.addItem(coconut);
        game.player.inventory.addItem(berries);
        game.player.inventory.addItem(emptyJar);

        gameController.processInput("use mushroom with cauldron");
        assertFalse(game.player.inventory.hasItem("mushroom"));
        gameController.processInput("use coconut with cauldron");
        assertFalse(game.player.inventory.hasItem("coconut"));
        gameController.processInput("use berries with cauldron");
        assertFalse(game.player.inventory.hasItem("berries"));
        gameController.processInput("use empty jar with cauldron");
        assertTrue(game.player.inventory.hasItem("healing potion"));
        assertFalse(game.player.inventory.hasItem("empty jar"));
    }

    @Test
    @DisplayName("Test if cauldron gets cleared")
    public void testCauldronCleared(){
        game.player.setRoomName("village center");
        game.player.inventory.addItem(mushroom);
        game.player.inventory.addItem(coconut);
        game.player.inventory.addItem(berries);
        game.player.inventory.addItem(emptyJar);

        gameController.processInput("use mushroom with cauldron");
        gameController.processInput("use coconut with cauldron");
        gameController.processInput("use berries with cauldron");
        gameController.processInput("use empty jar with cauldron");

        game.player.inventory.removeItem("healing potion");
        game.player.inventory.addItem(emptyJar);
        gameController.processInput("use empty jar with cauldron");
        assertFalse(game.player.inventory.hasItem("healing potion"));
    }

    /**
     * Test if the healing of the survivor works properly
     */
    @Test
    @DisplayName("Testing the healing of the survivor")
    public void testHealing(){
        game.player.setRoomName("healers hut");
        game.player.inventory.addItem(potion);
        gameController.processInput("use healing potion with survivor");
        assertFalse(game.player.inventory.hasItem("healing potion"));
        assertTrue(game.player.inventory.hasItem("note"));
        assertTrue(game.player.inventory.hasItem("empty jar"));
    }

    /**
     * Test if the ritual works properly
     */
    @Test
    @DisplayName("Testing the ritual")
    public void testRitual(){
        //can't get the pickaxe without the seashell
        game.player.setRoomName("mystic hut");
        gameController.processInput("use sea shell with ritual");
        assertFalse(game.player.inventory.hasItem("pickaxe"));
        //ritual is successful with seashell in inventory (+seashell gets removed)
        game.player.inventory.addItem(seashell);
        gameController.processInput("use sea shell with ritual");
        assertFalse(game.player.inventory.hasItem(seashell));
        assertTrue(game.player.inventory.hasItem("pickaxe"));
    }

    /**
     * Test if strength is maxStrength after using grindstone
     */
    @Test
    @DisplayName("Test if strength is set to maxStrength if strength != maxStrength")
    public void testGrindstoneWithBluntWeapon(){
        game.player.setRoomName("traders hut");
        machete.setMaxStrength(20);
        machete.setStrength(10);
        game.player.inventory.addItem(machete);
        assertNotNull(game.getCurrentRoom().getNonItem("grindstone"));
        gameController.processInput("use machete with grindstone");
        assertEquals(20,game.player.inventory.getItem("machete").getStrength());
    }

    /**
     * Test if strength is not changed after using the grindstone
     */
    @Test
    @DisplayName("Test if strength is not changed if strength = maxStrength")
    public void testGrindstoneWithSharpWeapon(){
        game.player.setRoomName("traders hut");
        machete.setMaxStrength(20);
        machete.setStrength(20);
        game.player.inventory.addItem(machete);
        assertNotNull(game.getCurrentRoom().getNonItem("grindstone"));
        gameController.processInput("use machete with grindstone");
        assertEquals(20,game.player.inventory.getItem("machete").getStrength());
    }
}