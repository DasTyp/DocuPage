package zork;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains all relevant data for the player
 */
public class Player
{
    /**
     * The players name
     */
    @SerializedName("name")
    private String name = "Hugo";

    /**
     * The players reached points in the game (high score)
     */
    @SerializedName("points")
    private int points = 0;

    /**
     * The players current energy level
     */
    @SerializedName("strength")
    private int strength = 20;

    /**
     * The players current position (room name)
     */
    @SerializedName("where")
    private String where;

    /**
     * @return The players reached points in the game (high score)
     */
    public int getPoints()
    {
        return points;
    }

    /**
     * Set the players reached points in the game (high score)
     * @param points The players new reached points in the game (new high score)
     */
    public void setPoints(int points)
    {
        this.points = points;
    }

    /**
     * @return The players current energy level
     */
    public int getStrength()
    {
        return strength;
    }

    /**
     * Set the players new energy level
     * @param strength The players new energy level
     */
    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    /**
     * @return The players current position (room name)
     */
    public String getRoomName()
    {
        return where;
    }

    /**
     * Set the players new position (room name)
     * @param where The players new position (room name)
     */
    public void setRoomName(String where)
    {
        this.where = where;
    }
}