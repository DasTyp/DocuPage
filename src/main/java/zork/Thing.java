package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Abstract class for all occurring things in the game (ways, items, ...)
 */
abstract class Thing
{
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public Thing(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}