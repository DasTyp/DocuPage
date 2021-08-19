package zork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Inventory can store a specific amount of items
 * @author Jonas Proell
 * @version 19.08.2021
 */
public class Inventory {

    /**
     * Max size/amount of items the inventory can store
     */
    @SerializedName("maxSize")
    private int maxSize;

    /**
     * List of items stored in the inventory
     */
    @SerializedName("storedItems")
    private ArrayList<Item> storedItems = new ArrayList<>();

    /**
     * Constructor for Inventory class
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
            if(storedItems.size() >= maxSize){
                System.out.println("your inventory is full now");
            }
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
            if (i.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Prints out all the items in the inventory
     * @author Jonas Proell
     */
    public void show(){
        if(storedItems.size() < 1){
            System.out.println("your inventory is empty");
        }
        else{
            for(Item i : storedItems){
                System.out.println(i.getName()+" ("+i.getDescription()+")");
            }
        }
    }
}
