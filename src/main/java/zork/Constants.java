package zork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class with constant variables
 */
public class Constants
{
    /**
     * Constants for the possible directions in the game
     */
    public static final List<String> DIRECTIONS = new ArrayList<>(Arrays.asList("north", "east", "west", "south", "up", "down"));

    /**
     * Constant for all directions
     */
    public static final String EACH_DIRECTION = "around";

    /**
     * Possible commands for the game - has to be extended if there are new commands implemented in the game
     */
    public static final List<String> COMMAND_LIST = new ArrayList<>(
            Arrays.asList("move + north / east / west / south / up / down or north / east / west / south / up / down", "look + around / north / east / west / south / up / down or around", "quit or exit", "?", "help or info", "show inventory or inventory", "take + \"item\"", "drop + \"item\"")
    );

    /**
     * Location of the json data for a saved game
     */
    public static final String SAVED_GAME = "files/savedGame.json";

    /**
     * Location of the json data for a new game
     */
    public static final String NEW_GAME ="files/database.json";

    /**
     * Location of the json data for unit tests
     */
    public static final String TEST_DATA ="files/testData.json";

    /**
     * Location and size of image of the map
     */
    public static final String MAP ="files/map.PNG";

    /**
     * Intro text that shows up at the beginning of the game to introduce the backstory
     */
    public static final String INTRO_TEXT ="You are a scientist on your way to a secluded island in the Papua New Guinea " +
            "archipelago to explore recently discovered indigenous people.\nThe chosen means of transportation, a light" +
            " aircraft that was already shaking uncomfortably upon boarding, was perhaps not the best\nchoice for the journey." +
            " Just like the accompanying pilot, who argues more with his wife over his mobile radio than concentrating\non" +
            " the dense wall of clouds in front of him. Hey, what the heck, the trip can only get better....\nBut suddenly" +
            " something rams the plane in the air with a big BAAAAAAANG!\nA bird? No matter what it was, the pilot gives the order to" +
            " evacuate the now crashing plane, which means jumping out over the open ocean...!\nYou grab your backpack with the" +
            " rescue parachute and jump out the emergency exit. Fortunately, the plane was already low enough over the water\n" +
            "that you don't hurt yourself when you dive in. When you resurface, you just see the plane crash into a high" +
            " cliff wall on an island not far away.\nFortunately an island! Now things can really only get better, can't they?" +
            "\nYou detach the bothersome parachute from your backpack and swim towards it. The last few meters you wade" +
            " through the shallow water and\nlie down on the beach, exhausted. What now? Did you end up in a bloody survival" +
            " adventure game?!\nAt least you already have a backpack where you can collect things as inventory...\n";
}