package zork;

import com.google.gson.annotations.SerializedName;

public class Item extends Thing
{
    @SerializedName("state")
    private String state;
    @SerializedName("strength")
    private int strength;
    @SerializedName("where")
    private String where;

    // Constructor for an item - calls the super constructor of the parent (thing) and adds the item-specific variables
    public Item(String name, String description, String state, int strength, String where)
    {
        super(name, description);
        this.strength = strength;
        this.state = state;
        this.where = where;
    }

    // Getters and setters for an item

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
