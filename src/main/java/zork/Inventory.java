package zork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * The player can put things in his inventoryand  remove or use them
 * @author Jonas Proell
 * @version 12.08.2021
 */
public class Inventory {

    /**
     * Maximum amount of items in the inventory
     */
    @SerializedName("maxSize")
    private int maxSize;

    /**
     * List of items in the inventory
     */
    @SerializedName("storedItems")
    private ArrayList<Item> storedItems = new ArrayList<>();

    /**
     * @author Jonas Proell
     * @param maxSize max amount of items the inventory can hold
     */
    Inventory(int maxSize){
        if(maxSize < 1) {
            throw new Error("inventory size must be greater than 0");
        }
        this.maxSize = maxSize;
    }

    /**
     * Adds an item to the inventory
     * @author Jonas Proell
     * @param item the item which should be added to the inventory
     * @return true if the item was successfully added
     */
    public boolean addItem(Item item){
        if(storedItems.size() >= maxSize){
            System.out.println("not enough space in inventory");
            return false;
        }else{
            storedItems.add(item);
            return true;
        }
    }

    /**
     * Removes an item from the inventory
     * @author Jonas Proell
     * @param item the item which should be removed from the inventory
     * @return true if the items was successfully removed
     */
    public boolean removeItem(Item item){
        return storedItems.remove(item);
    }

    /**
     * Removes an item from the inventory by name
     * @author Jonas Proell
     * @param name the name of the item which should be removed from the inventory
     */
    public boolean removeItem(String name){
        for (Item i : storedItems){
            if(i.getName().equals(name)) {
                return storedItems.remove(i);
            }
        }
        return false;
    }

    /**
     * Checks if the inventory holds a specific item
     * @author Jonas Proell
     * @param item the item to be checked
     * @return true if the item is in the inventory
     */
    public boolean hasItem(Item item){
        for (Item i : storedItems){
            if(i == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the inventory holds a specific item by name
     * @author Jonas Proell
     * @param name the name of the item to be checked
     * @return true if the item is in the inventory
     */
    public boolean hasItem(String name){
        for (Item i : storedItems) {
            if (i.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Prints out all the items in the inventory
     * @author Jonas Proell
     */
    public void show(){
        int itemCount = storedItems.size();
        if(itemCount < 1) System.out.println("your inventory is empty");
        else{
            for(Item i : storedItems){
                System.out.print(i.getName()+" ("+i.getDescription()+")");
                System.out.println();
            }

        }
    }


    /**
     * Returns item list for rooms.
     * @author Christian Litke
     * @return list object of stored items
     */
    public List<Item> getInventoryList()
    {
        return storedItems;
    }
}
