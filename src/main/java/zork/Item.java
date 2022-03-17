package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Items are things that can be taken, dropped, added to inventory and used (except water, can only be drunk)
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
     * maximum strength of an item
     */
    @SerializedName("maxStrength")
    private int maxStrength = strength;

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
     * @return The items maxStrength
     * @author Patrick Mayer
     */
    public int getMaxStrength()
    {
        return maxStrength;
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
     * Sets the items maxStrength
     * @param maxStrength The items new maxStrength
     * @author Patrick Mayer
     */
    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }
    /**
     * @return The items position
     */
    public String getWhere()
    {
       return where;
    }

    /**
     * Sets the items new position
     * @param where The items new position
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * reduces strength by given value or set to 0 if value is too big
     * @param reduction value by how much the strength gets reduced
     * @author Patrick Mayer
     */
    public void reduceStrength(int reduction){
        if(this.strength-reduction>=0) {
            this.strength = this.strength - reduction;
        } else{
            this.strength=0;
        }
    }

    /**
     * Method to show the current strength of an item and the maximum strength
     * @author Patrick Mayer
     */
    public String showStrength(){
        return "The "+this.getName()+" has a current strength of "+ this.strength+" and a maximum strength of "+this.maxStrength+".";
    }
}
