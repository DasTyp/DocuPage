package zork;

import com.google.gson.annotations.SerializedName;

public class Player
{
    /**
     * The players inventory
     */
    Inventory inventory;

    /**
     * The initial amount of items the player can carry in his inventory
     */
    int maxInventorySize = 10;

    @SerializedName("name")
    private String name = "Hugo";
    @SerializedName("points")
    private int points = 0;
    @SerializedName("strength")
    private int strength = 20;
    @SerializedName("where")
    private String where;

    Player() {
        inventory = new Inventory(maxInventorySize);
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public int getStrength()
    {
        return strength;
    }

    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    public String getRoomName()
    {
        return where;
    }

    public void setRoomName(String where)
    {
        this.where = where;
    }
}