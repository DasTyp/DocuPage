package zork;

import com.google.gson.annotations.SerializedName;

/**
 * Enemies have all NonItems attributes, an energy level and they can also be fought
 * @author Yvonne Rahnfeld
 * @version 24.08.21
 */
public class Enemy extends NonItem {
    /**
     * The enemies current energy
     */
    @SerializedName("energy")
    private Energy energy;

    /**
     * Constructor for an Enemy - calls the super constructor of the parent (NonItem) and adds the Enemy-specific variables
     * @param name The Enemies name
     * @param description The Enemies description
     * @param state The Enemies state (fixed/removable)
     * @param where The Enemies position
     * @param necessaryItem Name of the item that is necessary to manipulate the Enemy
     * @param value The Enemies current energy level
     * @param maxEnergy The Enemies maximum energy level
     */
    public Enemy(String name, String description, String state, String where, String necessaryItem, int value, int maxEnergy) {
        super(name, description, state, where, necessaryItem);
        this.energy = new Energy(value, maxEnergy);
    }

    /**
     * @return The Enemies energy
     */
    public Energy getEnergy() {
        return energy;
    }
}
