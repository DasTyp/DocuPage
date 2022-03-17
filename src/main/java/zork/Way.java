package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Ways connect rooms and can be blocked
 */
public class Way extends Thing {
    /**
     * Direction in their room like north, south, ...
     */
    @SerializedName("direction")
    private String direction;

    /**
     * Room name from where the way starts
     */
    @SerializedName("from")
    private String from;

    /**
     * Room name where the way leads to
     */
    @SerializedName("to")
    private String to;

    /**
     * Marks room as free or blocked (true/false)
     */
    @SerializedName("isFree")
    private boolean isFree;

    /**
     * Alternative description to be shown when a way is blocked
     */
    @SerializedName("altDescription")
    private String altDescription;

    /**
     * Constructor for a way object - calls the super constructor of the parent (thing) and adds the way-specific variables
     * @param name The ways name
     * @param description The ways description
     * @param direction The ways direction
     * @param from Room name from where the way starts
     * @param to Room name where the way leads to
     * @param isFree Marks room as free or blocked (true/false)
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
     * @return String that describes the way
     */
    @Override
    public String toString() {
        return "there's a " + getName() + " going " + direction + ". " + getDescription();
    }

    /**
     * Check if way is free or blocked
     * @author Yvonne Rahnfeld
     * @return True if way is free, false if way is blocked
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

    /**
     * @return The ways direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Set the ways new direction
     * @param direction The ways new direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return Room name from where the way starts
     */
    public String getFrom() {
        return from;
    }

    /**
     * Set new room name from where the way starts
     * @param from New room name from where the way starts
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return Room name where the way leads to
     */
    public String getTo() {
        return to;
    }

    /**
     * Set new room name where the way leads to
     * @param to New room name where the way leads to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Returns the alternative Description for blocked ways
     * @return Alternative description
     */
    public String getAltDescription(){
        return altDescription;
    }
}
