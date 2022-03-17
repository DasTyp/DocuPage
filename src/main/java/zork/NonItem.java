package zork;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * NonItems are things that exist in rooms and the player can interact with them, but they can not be used like items (take etc.)
 * @author Yvonne Rahnfeld
 * @version 24.08.21
 */
public class NonItem extends Thing
{
    /**
     * State of the NonItem (fixed NonItems are always there, removable NonItems leave their room after manipulating them)
     */
    @SerializedName("state")
    protected String state;

    /**
     * The NonItems position
     */
    @SerializedName("where")
    protected String where;

    /**
     * Name of the item that is necessary to manipulate the NonItem
     */
    @SerializedName("necessaryItem")
    protected String necessaryItem;

    /**
     * List of items that can be obtained from NonItem after manipulating it
     */
    @SerializedName("containedItems")
    private List<Item> containedItemsList = new ArrayList<>();

    @SerializedName("command")
    protected String command;
    /**
     * Constructor for a NonItem - calls the super constructor of the parent (thing) and adds the NonItem-specific variables
     * @param name The NonItems name
     * @param description The NonItems description
     * @param state The NonItems state (fixed/removable)
     * @param where The NonItems position
     * @param necessaryItem Name of the item that is necessary to manipulate the NonItem
     */
    public NonItem(String name, String description, String state, String where, String necessaryItem)
    {
        super(name, description);
        this.state = state;
        this.where = where;
        this.necessaryItem = necessaryItem;
    }

    /**
     * Constructor for a NonItem - calls the super constructor of the parent (thing) (to use for child classes)
     * @param name The NonItems name
     * @param description The NonItems description
     */
    public NonItem(String name, String description)
    {
        super(name, description);
    }

    /**
     * @return The NonItems state (fixed/removable)
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the NonItems new state
     * @param state The NonItems new state (fixed/removable)
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * @return The NonItems position
     */
    public String getWhere()
    {
        return where;
    }

    /**
     * Sets the NonItems new position
     * @param where The NonItems new position
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * @return Name of the item that is necessary to manipulate the NonItem
     */
    public String getNecessaryItem() {
        return necessaryItem;
    }

    /**
     * Set new name of the item that is necessary to manipulate the NonItem
     * @param necessaryItem New name of the item that is necessary to manipulate the NonItem
     */
    public void setNecessaryItem(String necessaryItem) {
        this.necessaryItem = necessaryItem;
    }

    /**
     * @return Item that can be obtained from NonItem after manipulating it
     */
    public List<Item> getContainedItemsList() {
        return containedItemsList;
    }
    public void setContainedItemsList(List<Item> containedItemsList){
        this.containedItemsList = containedItemsList;
    }

    /**
     *
     * @return command of the NonItem
     */
    public String getCommand(){return command;}
    /**
     * @param command of the NonItem
     */
    public void setCommand(String command){this.command = command;}
}
