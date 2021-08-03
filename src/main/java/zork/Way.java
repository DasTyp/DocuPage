package zork;

public class Way extends Thing
{
    private String direction;
    private String from;
    private String to;

    // Constructor for a way object - calls the super constructor of the parent (thing) and adds the way-specific variables
    public Way(String name, String description, String direction, String from, String to)
    {
        super(name, description);
        this.direction = direction;
        this.from = from;
        this.to = to;
    }

    // Method simplifies the default output for a way object
    @Override
    public String toString()
    {
        return "there's a " + getName() + " going " + direction + ". " + getDescription();
    }

    // Getters and setters for a way

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
