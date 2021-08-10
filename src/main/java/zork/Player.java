package zork;

import com.google.gson.annotations.SerializedName;

public class Player
{
    /**
     * The players name
     */
    @SerializedName("name")
    private String name = "Hugo";

    /**
     * Reached points in the game
     */
    @SerializedName("points")
    private int points = 0;

    /**
     * The players current energy level
     */
    @SerializedName("strength")
    private int strength = 20;

    /**
     * The players current position
     */
    @SerializedName("where")
    private String where;

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