package zork;

import com.google.gson.annotations.SerializedName;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Energy {
    /**
     * Current energy level
     */
    @SerializedName("value")
    private int value;

    /**
     * The Enemies minimum energy level
     */
    private final int minValue = 0;

    /**
     * The Enemies maximum energy level
     */
    @SerializedName("maxValue")
    private int maxValue;

    /**
     * Constructor for energy object
     * @param value Current energy level
     * @param maxValue The Enemies maximum energy level
     */
    public Energy(int value, int maxValue) {
        this.value = value;
        this.maxValue = maxValue;
    }

    /**
     * @return Current energy level
     */
    public int getValue() {
        return value;
    }

    /**
     * Set new energy level
     * @param energy New energy level
     */
    public void setValue(int energy) {
        this.value = adjustEnergyLevel(energy);
    }

    /**
     * Increase energy level by given value
     * @author Yvonne Rahnfeld
     * @param value Value to be added to energy level
     */
    public void increaseValue(int value) {
        this.value = adjustEnergyLevel(this.value+value);
    }

    /**
     * Reduce energy level by given value
     * @author Yvonne Rahnfeld
     * @param value Value to be subtract from energy level
     */
    public void reduceValue(int value) {
        this.value = adjustEnergyLevel(this.value-value);
    }

    /**
     * Get the max value
     * @return max value
     */
    public int getMaxValue(){
        return maxValue;
    }


    /**
     * Set new max value
     * @param maxValue New energy level
     */
    public void setMaxValue(int maxValue){
        this.maxValue = maxValue;
    }

    /**
     * Energy will be filled up if the two strings are equal
     * @author Yvonne Rahnfeld
     * @param string1 First of the compared strings
     * @param string2 Second of the compared strings
     * @param difficulty Difficulty level of the game
     * @return True if energy level was increased
     */
    public int fillUpIfStringsAreEqual(String string1, String string2, int difficulty) {
        int increaseValue = 0;
        if (string1.equals(string2)) {
            increaseValue = 20*difficulty;
            increaseValue(increaseValue);
        }
        return increaseValue;
    }

    /**
     * Ensure energy level will not get higher than maxValue or lower than minValue
     * @author Yvonne Rahnfeld
     * @param value Value to adjust if it's out of range
     * @return Adjusted energy level
     */
    private int adjustEnergyLevel(int value) {
        if (value > maxValue) {
            value = maxValue;
        }
        if (value < minValue) {
            value = minValue;
        }
        return value;
    }
}
