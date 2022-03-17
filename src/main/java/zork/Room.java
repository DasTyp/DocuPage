package zork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Rooms can be entered and left via ways and they can contain things and items
 */
public class Room extends Thing {
    /**
     * Counter for room visits
     */
    @SerializedName("visited")
    private int visited;

    /**
     * Alternative description after the first visit
     */
    @SerializedName("altDescription")
    private String altDescription;

    /**
     * List of ways out of the room
     */
    @SerializedName("ways")
    List<Way> roomWayList = new ArrayList<>();

    /**
     * List of items in the room
     */
    @SerializedName("items")
    private List<Item> roomItemList = new ArrayList<>();

    /**
     * List of NonItems in the room
     */
    @SerializedName("nonItems")
    private List<NonItem> roomNonItemList = new ArrayList<>();

    /**
     * List of Animals in the room
     */
    @SerializedName("animals")
    private List<Animal> roomAnimalList = new ArrayList<>();

    /**
     * List of Enemies in the room
     */
    @SerializedName("enemies")
    private List<Enemy> roomEnemyList = new ArrayList<>();

    /**
     * Constructor for a room object - calls the super constructor of the parent (thing) and adds the room-specific variables
     * @param name        The rooms name
     * @param description The rooms description
     */
    public Room(String name, String description) {
        super(name, description);
    }

    /**
     * Method simplifies the default output for a room object
     * @return String that describes the players position
     */
    @Override
    public String toString() {
        return "You are in the " + getName() + ".";
    }

    /**
     * @return How often the room was visited yet
     */
    public int getVisited() {
        return visited;
    }

    /**
     * @return Alternative description after the first visit
     */
    public String getAltDescription() {
        return altDescription;
    }

    /**
     * Set new altDescription after the first visit
     * @param altDescription New altDescription
     */
    public void setAltDescription(String altDescription) {
        this.altDescription = altDescription;
    }

    /**
     * @return List of ways out of the room
     */
    public List<Way> getRoomWayList() {
        return roomWayList;
    }

    /**
     * @return List of items in the room
     */
    public List<Item> getRoomItemList() {
        return roomItemList;
    }

    /**
     * @return List of NonItems in the room
     */
    public List<NonItem> getRoomNonItemList() {
        return roomNonItemList;
    }

    /**
     * @param NonItems to set for the room
     * @author Christian Litke
     */
    public void setRoomNonItemList(List<NonItem> NonItems) {this.roomNonItemList = NonItems;}

    /**
     * @return List of Animals in the room
     */
    public List<Animal> getRoomAnimalList() {
        return roomAnimalList;
    }

    /**
     * @param Animals to set for the room
     * @author Christian Litke
     */
    public void setRoomAnimalList(List<Animal> Animals) {this.roomAnimalList = Animals;}

    /**
     * @return List of Enemies in the room
     */
    public List<Enemy> getRoomEnemyList() {
        return roomEnemyList;
    }
    /**
     * @param Enemies to set for the room
     * @author Christian Litke
     */
    public void setRoomEnemyList(List<Enemy> Enemies){this.roomEnemyList = Enemies;}

    /**
     * Increments visited counter by one
     * @author Yvonne Rahnfeld
     */
    public void incrementVisited() {
        visited++;
    }

    /**
     * Show all things in this room (ways, items, nonItems,animals, enemies, ...)
     * @author Yvonne Rahnfeld
     */
    public String showAllThingsInRoom() {
        StringBuilder output = new StringBuilder();
        output.append(showWayList());
        output.append(showNonItems(getRoomNonItemList()));
        output.append(showNonItems(getRoomAnimalList()));
        output.append(showNonItems(getRoomEnemyList()));
        output.append(showItemList());
        return output.toString();
    }

    /**
     * Return item if it exists in  room
     * @author Yvonne Rahnfeld
     * @param itemName Name of item to look for in room
     * @return searched item if it is in room
     */
    public Item getItem(String itemName) {
        Item searchedItem = null;
        for (Item item : getRoomItemList()) {
            if (item.getName().equals(itemName)) {
                searchedItem = item;
            }
        }
        return searchedItem;
    }

    /**
     * Return way if it exists in  room
     * @author Yvonne Rahnfeld
     * @param wayName Name of way to look for in room
     * @return searched way if it is in room
     */
    public Way getWay(String wayName) {
        Way searchedWay = null;
        for (Way way : getRoomWayList()) {
            if (way.getName().equals(wayName)) {
                searchedWay = way;
            }
        }
        return searchedWay;
    }

    /**
     * Return NonItem if it exists in  room
     * @author Yvonne Rahnfeld
     * @param nonItemName Name of NonItem to look for in room
     * @return searched NonItem if it is in room
     */
    public NonItem getNonItem(String nonItemName) {
        return getIfExistsInRoom(nonItemName, getRoomNonItemList());
    }

    /**
     * Return Animal if it exists in  room
     * @author Yvonne Rahnfeld
     * @param animalName Name of Animal to look for in room
     * @return searched Animal if it is in room
     */
    public Animal getAnimal(String animalName) {
        return getIfExistsInRoom(animalName, getRoomAnimalList());
    }

    /**
     * Return Enemy if it exists in  room
     * @author Yvonne Rahnfeld
     * @param enemyName Name of Enemy to look for in room
     * @return searched Enemy if it is in room
     */
    public Enemy getEnemy(String enemyName) {
        return getIfExistsInRoom(enemyName, getRoomEnemyList());
    }

    /**
     * Template method: Returns thing if it's in room (NonItems or type of a class extended from class NonItem)
     * @param thingName Name of thing to look for in room
     * @param list List to look for in (sets type of returned searchedThing)
     * @return Searched thing if found in given list
     */
    private <T extends NonItem> T getIfExistsInRoom(String thingName, List<T> list) {
        T searchedThing = null;
        for (T thing : list) {
            if (thing.getName().equals(thingName)) {
                searchedThing = thing;
            }
        }
        return searchedThing;
    }

    /**
     * Show all Ways in this room
     * @author Yvonne Rahnfeld
     */
    private String showWayList() {
        List<Way> list = getRoomWayList();
        StringBuilder output = new StringBuilder();
        if (list != null) {
            for (Way way : list) {
                String wayDescription = way.isFree() ? way.getDescription() : way.getAltDescription();
                String indefArticle = Constants.isVowel(way.getName().toLowerCase().charAt(0)) ? "an " : "a ";
                String s = "There is " + indefArticle + way.getName() + " going " + way.getDirection() + ". " + wayDescription + "\n";
                output.append(s);
            }
        }
        return output.toString();
    }

    /**
     * Show all Items in this room
     * @author Yvonne Rahnfeld
     */
    private String showItemList() {
        List<Item> list = getRoomItemList();
        StringBuilder output = new StringBuilder("");
        if (list != null) {
            for (Item item : list) {
                String sentenceIntro = Constants.isVowel(item.getName().toLowerCase().charAt(0)) ? "An " : "A ";
                String itemName = item.getName();
                if (item.getName().equals("berries") || item.getName().equals("water")) {
                    sentenceIntro = "";
                    itemName = item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1);
                }
                String s = sentenceIntro + itemName + " " + item.getWhere() + ".\n";
                output.append(s);
            }
        }
        return output.toString();
    }

    /**
     * Template method: Shows all things in room which are NonItems or type of a class extended from class NonItem
     * @param list List of NonItems to be displayed
     */
    private <T extends NonItem> String showNonItems(List<T> list) {
        StringBuilder output = new StringBuilder("");
        if (list != null) {
            for (T nonItem : list) {
                String sentenceIntro = Constants.isVowel(nonItem.getName().toLowerCase().charAt(0)) ? "An " : "A ";
                String nonItemName = nonItem.getName();
                if (nonItem.getName().equals("apes")) {
                    sentenceIntro = "";
                    nonItemName = nonItem.getName().substring(0, 1).toUpperCase() + nonItem.getName().substring(1);
                }
                String s = sentenceIntro + nonItemName + " " + nonItem.getWhere() + ". " + nonItem.getDescription() + "\n";
                output.append(s);
            }
        }
        return output.toString();
    }
}
