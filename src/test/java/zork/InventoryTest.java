package zork;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Inventory inventory = new Inventory(2);
    Item item0 = new Item("Item 0", "0", "no state", 0, "nowhere");
    Item item1 = new Item("Item 1", "1", "no state", 1, "nowhere");
    Item item2 = new Item("Item 2", "2", "no state", 2, "nowhere");
    Item emptyBottle = new Item("empty bottle", "", "", 0, "");
    Item waterBottle = new Item("water bottle", "", "", 0, "");

    @Test
    void addItem() {
        //adding should be successful
        assertTrue(inventory.addItem(item0));
        assertTrue(inventory.addItem(item1));
        //adding should fail due to not enough space in inventory
        assertFalse(inventory.addItem(item2));
    }

    @Test
    void removeItem() {
        inventory.addItem(item0);
        inventory.addItem(item1);
        //remove should be successful
        assertTrue(inventory.removeItem(item0));
        //remove should fail because item was already removed
        assertFalse(inventory.removeItem(item0));
        //remove by item name should be successful
        assertTrue(inventory.removeItem("Item 1"));
        //remove by item name should fail because item was already removed
        assertFalse(inventory.removeItem("Item 1"));
    }

    @Test
    void hasItem() {
        inventory.addItem(item0);
        //should return true because item is in inventory
        assertTrue(inventory.hasItem(item0));
        assertTrue(inventory.hasItem("Item 0"));
        //should return false because item in not in inventory
        assertFalse(inventory.hasItem(item1));
        assertFalse(inventory.hasItem("Item 1"));
    }

    @Test
    void show() {
        //should show "empty inventory"
        inventory.show();
        //should show all added items
        inventory.addItem(item0);
        inventory.addItem(item1);
        inventory.show();
    }

    /**
     * Assert max inventory size gets increased by given value
     * @author Yvonne Rahnfeld
     */
    @Test
    public void testIncreaseMaxInventorySize()
    {
        int oldSize = inventory.getMaxSize();
        int value = 1;
        inventory.increaseMaxInventorySize(value);
        assertEquals(oldSize+value, inventory.getMaxSize());
    }

    /**
     * Assert empty bottle gets exchanged with water bottle if empty bottle is in inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    public void testGetFilledUpWaterBottle()
    {
        inventory.addItem(emptyBottle);
        assertEquals(waterBottle.getName(), inventory.getFilledUpWaterBottle().getName());
        assertFalse(inventory.hasItem("empty bottle"));
    }
}