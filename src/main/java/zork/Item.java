package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Items are things that can be taken, dropped, added to inventory and used
 */
public class Item extends Thing
{
    /**
     * state of the item
     */
    @SerializedName("state")
    private String state;

    /**
     * energy level of the item
     */
    @SerializedName("strength")
    private int strength;

    /**
     * location of the item
     */
    @SerializedName("where")
    private String where;

    /**
     * Constructor for an item - calls the super constructor of the parent (thing) and adds the item-specific variables
     * @param name the items name
     * @param description the items description
     * @param state the items state (fixed/removable)
     * @param strength the items strength
     * @param where the items position
     */
    public Item(String name, String description, String state, int strength, String where)
    {
        super(name, description);
        this.strength = strength;
        this.state = state;
        this.where = where;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public int getStrength()
    {
        return strength;
    }

    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    public String getWhere()
    {
       return where;
    }
}
