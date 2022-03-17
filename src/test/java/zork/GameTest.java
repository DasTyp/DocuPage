package zork;

import controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game testGame;
    Game game;
    GameController gameController;
    GameController testGameController;
    Item emptyBottle = new Item("empty bottle", "", "", 0, "");
    Item waterBottle = new Item("water bottle", "", "", 0, "");
    Item machete = new Item("machete", "", "", 0, "");

    /**
     * Set up test data
     */
    public GameTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        testGame = Zork.parseData(Constants.TEST_DATA);
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
        testGameController = new GameController(testGame);
    }

    /**
     * Assert item will be added to Inventory and leaves the room if item exists in current room,
     * is removable and Inventory is not full
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Take, case: item's in room, inventory isn't full, item's removable → item's in inventory and has left room")
    public void testTake_ItemExistsInRoomInventoryNotFullItemIsRemovable_ItemInInventoryItemLeftRoom()
    {
        String itemName = "key";
        testGame.player.setRoomName("test room 1");
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is added to inventory
        testGameController.processInput("take " + itemName);
        assertTrue(testGame.player.inventory.hasItem(itemName));
        // assert item is removed from players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
    }

    /**
     * Assert item will be added to Inventory and is still in room if item exists in current room,
     * is fixed and Inventory is not full
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Take, case: item's in room, inventory isn't full, item's fixed → item's in inventory and is still in room")
    public void testTake_ItemExistsInRoomInventoryNotFullItemIsFixed_ItemInInventoryItemStillInRoom()
    {
        String itemName = "berry";
        testGame.player.setRoomName("test room 2");
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is added to inventory
        testGameController.processInput("take " + itemName);
        assertTrue(testGame.player.inventory.hasItem(itemName));
        // assert item is still in players current room
        assertTrue(checkIfItemIsInCurrentRoom(itemName));
    }


    /**
     * Assert item won't be added to Inventory and is still in room if item exists in current room and Inventory is full
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Take, case: item's in room, inventory's full → item isn't in inventory and is still in room")
    public void testTake_ItemExistsInRoomInventoryFull_ItemNotInInventoryItemStillInRoom()
    {
        String itemName = "key";
        Item item = new Item("berry", "berry description", "fixed", 0, "berry position");
        // fill inventory (10 items)
        for (int i = 0; i < testGame.player.maxInventorySize; i++) {
            testGame.player.inventory.addItem(item);
        }
        testGame.player.setRoomName("test room 1");
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is not added to inventory
        testGameController.processInput("take " + itemName);
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is still in players current room
        assertTrue(checkIfItemIsInCurrentRoom(itemName));
    }

    /**
     * Assert item won't be added to Inventory and is still in room if item isn't in current room and Inventory isn't full
     *
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Take, case: item isn't in room, inventory isn't full → item isn't in inventory and is still not in room")
    public void testTake_ItemNotInRoomInventoryNotFull_ItemNotInInventoryItemStillNotInRoom()
    {
        String itemName = "key";
        testGame.player.setRoomName("test room 2");
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is not in players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
        // assert item is not added to inventory
        testGameController.processInput("take " + itemName);
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is still not players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
    }

    /**
     * Assert item will be added to room and leaves the inventory if item exists in inventory and is dropped
     *
     * @author Christian Litke
     */
    @Test
    public void testDrop_ItemInInventoryItemNotInRoom_ItemNotInInventoryItemInRoom() {
        Item item = new Item("test item", "test description", "removable", 0, "somewhere");
        String itemName = item.getName();
        testGame.player.inventory.addItem(item);
        testGame.player.setRoomName("test room 1");
        // assert item is in inventory
        assertTrue(testGame.player.inventory.hasItem(itemName));
        // assert item is not in players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
        testGameController.processInput("drop " + itemName);
        // assert item is no longer in inventory
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is in players current room
        assertTrue(checkIfItemIsInCurrentRoom(itemName));
    }

    /**
     * Assert dropped items are removable / not fixed
     *
     * @author Christian Litke
     */
    @Test
    public void testDrop_StateDroppedItemNotFixed() {
        Item item = new Item("test item", "test description", "fixed", 0, "somewhere");
        String itemName = item.getName();
        testGame.player.inventory.addItem(item);
        testGame.player.setRoomName("test room 1");
        testGameController.processInput("drop " + itemName);
        assertNotEquals("fixed", item.getState());
    }

    /**
     * Assert dropping an item not in your inventory does not change anything
     *
     * @author Christian Litke
     */
    @Test
    public void testDrop_ItemNotInRoomInventoryNotFull_ItemNotInInventoryItemStillNotInRoom() {
        Item item = new Item("test item", "test description", "removable", 0, "somewhere");
        String itemName = item.getName();
        testGame.player.setRoomName("test room 1");
        // assert item is not in inventory
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is not in players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
        testGameController.processInput("drop " + itemName);
        // assert item is not in inventory
        assertFalse(testGame.player.inventory.hasItem(itemName));
        // assert item is not in players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
    }

    /**
     * Assert dropping an item multiple times does not create multiple items
     *
     * @author Christian Litke
     */
    @Test
    public void testDrop_ItemDroppedMultipleTimes_OneItemInRoom() {
        Item item = new Item("test item", "test description", "fixed", 0, "somewhere");
        String itemName = item.getName();
        testGame.player.inventory.addItem(item);
        testGame.player.inventory.addItem(item);
        testGame.player.inventory.addItem(item);
        assertTrue(testGame.player.inventory.hasItem(itemName));
        testGame.player.setRoomName("test room 1");
        testGameController.processInput("drop " + itemName);
        testGameController.processInput("drop " + itemName);
        testGameController.processInput("drop " + itemName);
        assertFalse(testGame.player.inventory.hasItem(itemName));
        testGameController.processInput("take " + itemName);
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
    }

    /**
     * Assert Player moves from test room 1 to test room 2 after the input of "east"
     * @author Patrick Mayer
     */
    @Test
    @DisplayName("CommandAlias case: player is in test room 1 and input is east -> player is in test room 2")
    public void testCommandAlias(){
        testGame.player.setRoomName("test room 1");
        testGameController.processInput("east");
        assertEquals("test room 2", testGame.player.getRoomName());
    }

    /**
     * Assert that (if empty bottle is in inventory and water in current room) after command "take water" the empty
     * bottle will be exchanged with water bottle in inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Take water, case: empty bottle in inventory → empty bottle gets exchanged by water bottle")
    public void testTakeWater_EmptyBottleInInventory_WaterBottleInInventory()
    {
        String itemName = "water";
        game.player.setRoomName("western beach");
        game.player.inventory.addItem(emptyBottle);
        assertNotNull(game.getCurrentRoom().getItem(itemName));

        gameController.processInput("take " + itemName);

        assertNotNull(game.getCurrentRoom().getItem(itemName));
        assertFalse(game.player.inventory.hasItem(emptyBottle.getName()));
        assertTrue(game.player.inventory.hasItem(waterBottle.getName()));
    }

    /**
     * Assert that (if empty bottle is not in inventory and water in current room) after command "take water" the empty
     * bottle will not be exchanged with water bottle in inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Take water, case: empty bottle not in inventory → empty bottle does not get exchanged by water bottle")
    public void testTakeWater_EmptyBottleNotInInventory_WaterBottleNotInInventory()
    {
        String itemName = "water";
        game.player.setRoomName("western beach");
        assertFalse(game.player.inventory.hasItem(emptyBottle.getName()));
        assertNotNull(game.getCurrentRoom().getItem(itemName));

        testGameController.processInput("take " + itemName);

        assertNotNull(game.getCurrentRoom().getItem(itemName));
        assertFalse(game.player.inventory.hasItem(waterBottle.getName()));
    }

    /**
     * Filling up energy after eating or drinking was not implemented yet!
     *
     * Assert that (if water bottle is in inventory) after command "drink water" the water bottle will be exchanged
     * with empty bottle in inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Drink water, case: water bottle in inventory, no water in room → water bottle gets exchanged by empty bottle")
    public void testDrinkWater_WaterBottleInInventory_EmptyBottleInInventory()
    {
        String itemName = "water";
        game.player.setRoomName("middle beach");
        game.player.inventory.addItem(waterBottle);
        assertNull(game.getCurrentRoom().getItem(itemName));

        gameController.processInput("drink " + itemName);

        assertTrue(game.player.inventory.hasItem(emptyBottle.getName()));
        assertFalse(game.player.inventory.hasItem(waterBottle.getName()));
    }

    /**
     * Assert that difficulty level equals 1 after setting it to "easy"
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Set difficulty level to easy")
    public void testSetDifficultyLevel_Easy()
    {
        Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
        gameController.processInput("1");

        assertEquals(1, game.getDifficulty());
    }

    /**
     * Assert that difficulty level equals 2 after setting it to "medium"
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Set difficulty level to medium")
    public void testSetDifficultyLevel_Medium()
    {
        Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
        gameController.processInput("2");

        assertEquals(2, game.getDifficulty());
    }

    /**
     * Assert that difficulty level equals 3 after setting it to "hard"
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Set difficulty level to hard")
    public void testSetDifficultyLevel_Hard()
    {
        Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
        gameController.processInput("3");

        assertEquals(3, game.getDifficulty());
    }

    /**
     * Assert that no difficulty level was set with wrong user input
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("No difficulty level set with wrong user input")
    public void testSetDifficultyLevel_WrongInput()
    {
        Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
        gameController.processInput("abc");

        assertEquals(1, game.getDifficulty());
    }

    /**
     * Look for item with given name in players current room
     * @author Yvonne Rahnfeld
     * @param itemName of the searched item
     * @return true if item is in players current room
     */
    private boolean checkIfItemIsInCurrentRoom(String itemName) {
        List<Room> rooms = testGame.rooms;
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(testGame.player.getRoomName())) {
                List<Item> items = room.getRoomItemList();
                for (Item item : items) {
                    if (item.getName().equals(itemName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
