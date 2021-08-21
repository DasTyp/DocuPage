package zork;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game game = Zork.parseData(Constants.TEST_DATA);

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
        game.player.setRoomName("test room 1");
        assertFalse(game.player.inventory.hasItem(itemName));
        // assert item is added to inventory
        game.take(itemName);
        assertTrue(game.player.inventory.hasItem(itemName));
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
        game.player.setRoomName("test room 2");
        assertFalse(game.player.inventory.hasItem(itemName));
        // assert item is added to inventory
        game.take(itemName);
        assertTrue(game.player.inventory.hasItem(itemName));
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
        for (int i = 0; i < game.player.maxInventorySize; i++) {
            game.player.inventory.addItem(item);
        }
        game.player.setRoomName("test room 1");
        assertFalse(game.player.inventory.hasItem(itemName));
        // assert item is not added to inventory
        game.take(itemName);
        assertFalse(game.player.inventory.hasItem(itemName));
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
        game.player.setRoomName("test room 2");
        assertFalse(game.player.inventory.hasItem(itemName));
        // assert item is not in players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
        // assert item is not added to inventory
        game.take(itemName);
        assertFalse(game.player.inventory.hasItem(itemName));
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
        game.player.inventory.addItem(item);
        game.player.setRoomName("test room 1");
        // assert item is in inventory
        assertTrue(game.player.inventory.hasItem(itemName));
        // assert item is not in players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
        game.drop(itemName);
        // assert item is no longer in inventory
        assertFalse(game.player.inventory.hasItem(itemName));
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
        game.player.inventory.addItem(item);
        game.player.setRoomName("test room 1");
        game.drop(itemName);
        assertFalse(Objects.equals(item.getState(), "fixed"));
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
        game.player.setRoomName("test room 1");
        // assert item is not in inventory
        assertFalse(game.player.inventory.hasItem(itemName));
        // assert item is not in players current room
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
        game.drop(itemName);
        // assert item is not in inventory
        assertFalse(game.player.inventory.hasItem(itemName));
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
        game.player.inventory.addItem(item);
        game.player.inventory.addItem(item);
        game.player.inventory.addItem(item);
        assertTrue(game.player.inventory.hasItem(itemName));
        game.player.setRoomName("test room 1");
        game.drop(itemName);
        game.drop(itemName);
        game.drop(itemName);
        assertFalse(game.player.inventory.hasItem(itemName));
        game.take(itemName);
        assertFalse(checkIfItemIsInCurrentRoom(itemName));
    }


    /**
     * Look for item with given name in players current room
     *
     * @param itemName of the searched item
     * @return true if item is in players current room
     */
    private boolean checkIfItemIsInCurrentRoom(String itemName) {
        List<Room> rooms = game.rooms;
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(game.player.getRoomName())) {
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
