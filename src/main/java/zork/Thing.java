package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Abstract class for all occurring things in the game (ways, items, ...)
 */
abstract class Thing
{
    /**
     * The things name
     */
    @SerializedName("name")
    private String name;

    /**
     * The things description
     */
    @SerializedName("description")
    private String description;

    /**
     * The super constructor for all derived classes, adds the class-specific variables
     * @param name The things name
     * @param description The things description
     */
    public Thing(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * @return The things name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the things new name
     * @param name The things new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return The things description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the things new description
     * @param description The things new description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

}