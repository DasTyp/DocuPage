package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Animals have all NonItems attributes and they can also be hungry and be fed to make them move away or killed to get meat
 * @author Yvonne Rahnfeld
 * @version 24.08.21
 */
public class Animal extends NonItem {
    /**
     * Marks animal as hungry (true/false), only hungry animals will eat
     */
    @SerializedName("isHungry")
    private boolean isHungry;

    /**
     * Constructor for an Animal - calls the super constructor of the parent (NonItem) and adds the Animal-specific variables
     * @param name The Animals name
     * @param description The Animals description
     * @param state The Animals state (fixed/removable)
     * @param where The Animals position
     * @param necessaryItem Name of the item that is necessary to manipulate the Animal
     * @param isHungry Marks animal as hungry (true/false), only hungry animals will eat
     */
    public Animal(String name, String description, String state, String where, String necessaryItem, boolean isHungry) {
        super(name, description, state, where, necessaryItem);
        this.isHungry = isHungry;
    }

    /**
     * Check if animal is hungry
     * @author Yvonne Rahnfeld
     * @return True if the animal is hungry
     */
    public boolean isHungry() {
        return isHungry;
    }

    /**
     * Sets attribute to true to make the Animal hungry
     * @author Yvonne Rahnfeld
     */
    public void setToHungry() {
        this.isHungry = true;
    }

    /**
     * Sets attribute to true to make the Animal not hungry
     * @author Yvonne Rahnfeld
     */
    public void setToNotHungry() {
        this.isHungry = false;
    }
}
