package zork;

import com.google.gson.annotations.SerializedName;

public class Player
{
    @SerializedName("name")
    private String name = "Hugo";
    @SerializedName("points")
    private int points = 0;
    @SerializedName("strength")
    private int strength = 20;
    @SerializedName("where")
    private String where;

    // Getters and setters for a player

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