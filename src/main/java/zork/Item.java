package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Items are things that can be taken, dropped, added to inventory and used
 */
public class Item extends Thing
{
    /**
     * State of the item (fixed items are always there, removable items leave their room after taking them)
     */
    @SerializedName("state")
    private String state;

    /**
     * Energy level of the item
     */
    @SerializedName("strength")
    private int strength;

    /**
     * Position of the item
     */
    @SerializedName("where")
    private String where;

    /**
     * Constructor for an item - calls the super constructor of the parent (thing) and adds the item-specific variables
     * @param name The items name
     * @param description The items description
     * @param state The items state (fixed/removable)
     * @param strength The items strength
     * @param where The items position
     */
    public Item(String name, String description, String state, int strength, String where)
    {
        super(name, description);
        this.strength = strength;
        this.state = state;
        this.where = where;
    }

    /**
     * @return The items state (fixed/removable)
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the items state
     * @param state The items new state (fixed/removable)
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * @return The items strength
     */
    public int getStrength()
    {
        return strength;
    }

    /**
     * Sets the items strength
     * @param strength The items new strength
     */
    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    /**
     * @return The items position
     */
    public String getWhere()
    {
       return where;
    }
}
