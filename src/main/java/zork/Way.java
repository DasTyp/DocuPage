package zork;

/**
 * Ways connect rooms
 */
public class Way extends Thing
{
    /**
     * direction like north, south, ...
     */
    private String direction;

    /**
     * From room (name)
     */
    private String from;

    /**
     * To room (name)
     */
    private String to;

    /**
     * Constructor for a way object - calls the super constructor of the parent (thing) and adds the way-specific variables
     * @param name
     * @param description
     * @param direction
     * @param from
     * @param to
     */
    public Way(String name, String description, String direction, String from, String to)
    {
        super(name, description);
        this.direction = direction;
        this.from = from;
        this.to = to;
    }

    /**
     * Method simplifies the default output for a way object
     */
    @Override
    public String toString()
    {
        return "there's a " + getName() + " going " + direction + ". " + getDescription();
    }

    public String getDirection()
    {
        return direction;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }
}
