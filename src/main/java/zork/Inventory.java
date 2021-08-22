package zork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    @SerializedName("maxSize")
    private int maxSize;
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
     * @author Jonas Proell
     * adds an item to the inventory
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
     * @author Jonas Proell
     * removes an item from the inventory
     * @param item the item which should be removed from the inventory
     * @return true if the items was successfully removed
     */
    public boolean removeItem(Item item){
        return storedItems.remove(item);
    }

    /**
     * @author Jonas Proell
     * removes an item from the inventory by name
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
     * @author Jonas Proell
     * checks if the inventory holds a specific item
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
     * @author Jonas Proell
     * checks if the inventory holds a specific item by name
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
     * @author Jonas Proell
     * prints out all the items in the inventory
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
     * mirrors itemlist for rooms.
     * @author Christian Litke
     * @return list object of stored items
     */
    public List<Item> getInventoryList()
    {
        return storedItems;
    }
}
