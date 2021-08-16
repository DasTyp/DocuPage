package zork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Rooms can be entered and left via ways and they can contain things and items
 */
public class Room extends Thing
{
    /**
     * Ways into or out of the room
     */
    @SerializedName("ways")
    List<Way> roomWayList = new ArrayList<>();

    /**
     * Items in the room
     */
    @SerializedName("items")
    private List<Item> roomItemList = new ArrayList<>();

    /**
     * Constructor for a room object - calls the super constructor of the parent (thing) and adds the room-specific variables
     * @param name the rooms name
     * @param description thee rooms description
     */
    public Room(String name, String description)
    {
        super(name, description);
    }

    /**
     * Method simplifies the default output for a room object
     */
    @Override
    public String toString()
    {
        return "you are in the " + getName() + ".";
    }

    public List<Way> getRoomWayList()
    {
        return roomWayList;
    }

    public List<Item> getRoomItemList()
    {
        return roomItemList;
    }
}
