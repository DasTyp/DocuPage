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
    private int visited = 0;

    /**
     * Alternative description after the first visit
     */
    @SerializedName("altDescription")
    private String altDescription = "";

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
        return "you are in the " + getName() + ".";
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
     * Set the alternative description after the first visit
     * @param altDescription Alternative description after the first visit
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
     * Increments visited counter by one
     * @author Yvonne Rahnfeld
     */
    public void incrementVisited() {
        visited++;
    }
}
