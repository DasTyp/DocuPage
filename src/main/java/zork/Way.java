package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Ways connect rooms
 */
public class Way extends Thing {
    /**
     * direction like north, south, ...
     */
    @SerializedName("direction")
    private String direction;

    /**
     * From room (name)
     */
    @SerializedName("from")
    private String from;

    /**
     * To room (name)
     */
    @SerializedName("to")
    private String to;

    /**
     * Marks room as blocked or free
     */
    @SerializedName("isFree")
    private boolean isFree;

    /**
     * Constructor for a way object - calls the super constructor of the parent (thing) and adds the way-specific variables
     *
     * @param name
     * @param description
     * @param direction
     * @param from
     * @param to
     * @param isFree
     */
    public Way(String name, String description, String direction, String from, String to, boolean isFree) {
        super(name, description);
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.isFree = isFree;
    }

    /**
     * Method simplifies the default output for a way object
     */
    @Override
    public String toString() {
        return "there's a " + getName() + " going " + direction + ". " + getDescription();
    }

    /**
     * Check if way is free or blocked
     * @author Yvonne Rahnfeld
     * @return true if way is free, false if way is blocked
     */
    public boolean isFree() {
        return isFree;
    }

    /**
     * Sets attribute isFree to true to free the way
     * @author Yvonne Rahnfeld
     */
    public void setToFree() {
        this.isFree = true;
    }

    /**
     * Sets attribute isFree to false to block the way
     * @author Yvonne Rahnfeld
     */
    public void setToBlocked() {
        this.isFree = false;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
