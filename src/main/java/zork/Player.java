package zork;

import com.google.gson.annotations.SerializedName;
import controller.CommunicationController;
import view.StatsPanel;

import javax.swing.*;

/**
 * This class contains all relevant data for the player
 */
public class Player
{
    /**
     * The players inventory
     */
    public Inventory inventory;

    /**
     * The players health stat
     */
    private Energy health;

    /**
     * The players hunger stat
     */
    private Energy hunger;

    /**
     * The players thirst stat
     */
    private Energy thirst;

    /**
     * The initial amount of items the player can carry in his inventory
     */
    int maxInventorySize = 10;

    /**
     * The players name
     */
    @SerializedName("name")
    private String name = "Hugo";

    /**
     * The players reached points in the game (high score)
     */
    @SerializedName("points")
    private int points = 0;

    /**
     * The players current energy level
     */
    @SerializedName("strength")
    private int strength = 20;

    /**
     * The players current position (room name)
     */
    @SerializedName("where")
    private String where;

    /**
     * Helper Class
     */
    private final Helper HELPER = new Helper();

    /**
     * Constructor
     */
    Player() {
        inventory = new Inventory(maxInventorySize);
    }

    /**
     * @return players name
     */
    public String getName() {
        return name;
    }

    /**
     * set players name
     * @param name players name
     */
    public void setName(String name) {
        this.name = name;
        CommunicationController.getInstance().setPlayerName(name);
    }

    /**
     * @return The players reached points in the game (high score)
     */
    public int getPoints()
    {
        return points;
    }

    /**
     * Set the players reached points in the game (high score)
     * @param points The players new reached points in the game (new high score)
     */
    public void setPoints(int points)
    {
        this.points = points;
    }

    /**
     * @return The players current energy level
     */
    public int getStrength()
    {
        return strength;
    }

    /**
     * Set the players new energy level
     * @param strength The players new energy level
     */
    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    /**
     * @return The players current position (room name)
     */
    public String getRoomName()
    {
        return where;
    }

    /**
     * Set the players new position (room name)
     * @param where The players new position (room name)
     */
    public void setRoomName(String where)
    {
        this.where = where;
    }

    /**
     * Increases health stat by value
     * @author Jonas Proell
     * @param value value to be added to health stat
     */
    public void increaseHealth(int value){
        health.increaseValue(value);
    }

    /**
     * Reduces health stat by value
     * @author Jonas Proell
     * @param value value to be subtracted to health stat
     */
    public void reduceHealth(int value){
        health.reduceValue(value);
    }

    /**
     * Increases hunger stat by value
     * @author Jonas Proell
     * @param value value to be added to hunger stat
     */
    public void increaseHunger(int value){
        hunger.increaseValue(value);
    }

    /**
     * Reduces hunger stat by value
     * @author Jonas Proell
     * @param value value to be subtracted to hunger stat
     */
    public void reduceHunger(int value){
        hunger.reduceValue(value);
    }

    /**
     * Increases thirst stat by value
     * @author Jonas Proell
     * @param value value to be added to thirst stat
     */
    public void increaseThirst(int value){
        thirst.increaseValue(value);
    }

    /**
     * Reduces thirst stat by value
     * @author Jonas Proell
     * @param value value to be subtracted to thirst stat
     */
    public void reduceThirst(int value){
        thirst.reduceValue(value);
    }

    /**
     * Gets current health value
     * @author Jonas Proell
     * @return current health value
     */
    public int getHealth(){
        return health.getValue();
    }

    /**
     * Gets current hunger value
     * @author Jonas Proell
     * @return current hunger value
     */
    public int getHunger(){
        return hunger.getValue();
    }

    /**
     * Gets current thirst value
     * @author Jonas Proell
     * @return current thirst value
     */
    public int getThirst(){
        return thirst.getValue();
    }

    /**
     * Get health as object to manipulate it
     * @return Players health as object
     */
    public Energy getHealthObject() {
        return health;
    }

    /**
     * Increases a stat by its name
     * @author Jonas Proell
     * @param statName the name of the stat to be increased
     * @param value value to increase by
     */
    public void increaseStat(String statName, int value){
        switch (statName) {
            case "health" -> health.increaseValue(value);
            case "hunger" -> hunger.increaseValue(value);
            case "thirst" -> thirst.increaseValue(value);
        }
    }

    /**
     * Reduce a stat by its name
     * @author Jonas Proell
     * @param statName the name of the stat to be reduced
     * @param value value to reduce by
     */
    public void reduceStat(String statName, int value){
        switch (statName) {
            case "health" -> health.reduceValue(value);
            case "hunger" -> hunger.reduceValue(value);
            case "thirst" -> thirst.reduceValue(value);
        }
    }

    /**
     * Get a stats value by its name
     * @author Jonas Proell
     * @param statName the name of the stat to get the value of
     * @return value of the given stat
     */
    public int getStat(String statName){
        return switch (statName) {
            case "health" -> health.getValue();
            case "hunger" -> hunger.getValue();
            case "thirst" -> thirst.getValue();
            default -> -1;
        };
    }

    /**
     * Shows constantly updating stats in extern window
     * @author Yvonne Rahnfeld
     */
    public void updateStatsPanel() {

        StatsPanel.getInstance().setHealth(this.health.getValue(), this.health.getMaxValue());
        StatsPanel.getInstance().setHunger(this.hunger.getValue(), this.hunger.getMaxValue());
        StatsPanel.getInstance().setThirst(this.thirst.getValue(), this.thirst.getMaxValue());
        StatsPanel.getInstance().setSpace(this.inventory.getSize(), this.inventory.getMaxSize());
    }
}