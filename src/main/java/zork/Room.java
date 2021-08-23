package zork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Room extends Thing
{
    @SerializedName("ways")
    List<Way> roomWayList = new ArrayList<>();
    @SerializedName("items")
    private List<Item> roomItemList = new ArrayList<>();

    // Constructor for a room object - calls the super constructor of the parent (thing) and adds the room-specific variables
    public Room(String name, String description)
    {
        super(name, description);
    }

    // Method simplifies the default output for a room object
    @Override
    public String toString()
    {
        return "you are in the room " + getName() + ".";
    }

    // Getters and setters for a room

    public List<Way> getRoomWayList()
    {
        return roomWayList;
    }

    public List<Item> getRoomItemList()
    {
        return roomItemList;
    }
}
