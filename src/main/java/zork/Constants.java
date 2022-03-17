package zork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class with constant variables
 */
public class Constants {
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
            Arrays.asList("\nmove + north / east / west / south / up / down or north / east / west / south / up / down",
                    "\nlook + around / north / east / west / south / up / down or around", "\nquit or exit", "\n?",
                    "\nhelp or info", "\nshow inventory or inventory", "\ntake + \"item\"", "\ndrop + \"item\"",
                    "\nuse + item + with + nonItem/animal/enemy/item", "\nuse + item", "\nfeed + Animal + with + item",
                    "\neat + item", "\ndrink + item", "\ntrack + nonItem", "\nfight + enemy", "\nsave", "\nrestore/load",
                    "map", "\nhelp + nonItem/animal/enemy", "\nhelp trading / trade", "\nshow strength + item",
                    "\nname/rename + \"name\"", "\n")
    );

    /**
     * List of all english vowels (german: Vokale)
     */
    public static final List<Character> VOWELS = new ArrayList<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));

    /**
     * Location of the json data for a saved game
     */
    public static final String SAVED_GAME = "savedGame.json";

    /**
     * Location of the json data for a new game
     */
    public static final String NEW_GAME ="/database.json";

    /**
     * Location of the json data for unit tests
     */
    public static final String TEST_DATA = "files/testData.json";

    /**
     * Location and size of image of the map
     */
    public static final String MAP = "/map.PNG";

    /**
     * Intro text that shows up at the beginning of the game to introduce the backstory
     */
    public static final String INTRO_TEXT = "\nYou are a scientist on your way to a secluded island in the Papua New Guinea " +
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
            " adventure game?!\nAt least you already have a backpack where you can collect things as inventory...\n\n" +
            "Pay attention to your health, hunger and thirst level!\nYou have to eat food items" +
            "to fill up your health level and drink water to fill up your thirst level.\nYour health will be filled up " +
            "automatically as long as your hunger and thirst levels are filled up well.\nDon't forget to save every now and then!\n";

    /**
     * Outro txt that shows up at the end of the game telling the player he has finished the game and storyline
     */
    public static final String OUTRO_TEXT = "\nAs you found the old two way radio in the wooden house your hopes are back up again. \n" +
            "Carefully you put the broken parts, you found in the cave, together. After you connected the battery to the two way radio,\n" +
            "you can't believe your eyes. Lights are flashing, some noises are coming out the speakers...\n" +
            " Not only noises, words, sentences. You can't understand the language, but you know that there is help out there.\n" +
            "You couldn't have imagined you will make it to that point, after all the jungle trouble, cave fights and dangerous situations.\n" +
            "But you made it. Happy and relieved you take a seat near the desk and try to establish connection to the society outside, \n" +
            "imagining what you will do first, when you are back home again... \n\n" +
            "You can make an emergency call in your broken English, but you can't quite understand whether the person on the other end understood you or not.\n" +
            "Only when the next day, as a helicopter appears on the horizon and lands on the coast of the island, you know... you are saved.";

    /**
     * Gets shown if player unlocks item trading in traders hut or asks for trading help
     */
    public static final String ABOUT_TRADING = "\nTo make a trade you have to enter the command   \"trade <offeredItem>" +
            " for <requestedItem> with <tradingPartner>\"   in the game input field. The other player will be notified " +
            "about your offer and can accept or decline it. If he accepts, your items will be exchanged in your " +
            "inventories. You will also be notified when another player sends you an offer and you can accept or decline it.\n" +
            "During the whole trading process you and your trading partner have to stay connected to the broker, " +
            "otherwise the trade will be aborted. Every offer has also a time limit of 10 minutes. If the trade isn't " +
            "completed within this time, the offer expires and the trade will be aborted as well.";

    /**
     * List with eatable items for the player
     */
    public static final List<String> FOOD_LIST = new ArrayList<>(Arrays.asList("mushroom", "berries", "coconut", "banana", "fish"));
    /**
     * List with items, which are drinkable
     */
    public static final List<String> DRINKABLE_LIST = new ArrayList<>(Arrays.asList("water", "healing potion"));

    /**
     * List of items, which are weapons
     */
    public static final List<String> WEAPONS_LIST = new ArrayList<>(Arrays.asList("machete"));

    /**
     * Checks if given input is a vowel (german: Vokal)
     *
     * @param input Input to check
     * @return True if input is a vowel
     * @author Yvonne Rahnfeld
     */
    public static boolean isVowel(char input) {
        return VOWELS.contains(input);
    }

    /**
     * message limit for every topic
     */
    public static int MAX_MESSAGES = 500;

    /**
     * Helper method: Checks if a given input is contained in a given list
     *
     * @param input       Given input that has to be checked if it's in given list
     * @param properInput List of proper input
     * @return true if input exists in given list
     */
    public static <T> boolean isProperInput(T input, List<T> properInput) {
        return properInput.contains(input);
    }

    /**
     * game states, necessary for correct controller behavior
     */
    public static int STATE_PLAY = 0;
    public static int STATE_BEGINNING = 1;
    public static int STATE_NEW_LOAD_OR_QUIT = 2;
    public static int STATE_SET_NAME = 3;
    public static int STATE_CHOOSE_DIFFICULTY = 4;
    public static int STATE_BLOCK = 5;
    public static int STATE_CRITICAL_STRIKE = 6;
    public static int STATE_TRY_FILLUP = 7;

    /**
     * connection states
     */
    public static final String STATE_CONNECTED = "CONNECTED";
    public static final String STATE_NOT_CONNECTED = "NOT_CONNECTED";

    /**
     * Trade topic
     */
    public static String TRADING_TOPIC = "Zork4/trading/";

    /**
     * Trade message types
     */
    public static String TRADE_OFFER = "0000";
    public static String TRADE_ACCEPTED ="0001";
    public static String TRADE_ABORTED ="1011";
    public static String TRADE_EXECUTE ="1111";
    public static String TRADE_ITEM_TRANSFER ="1010";
    public static String NAME_CHECK = "1100";
    public static String NAME_BLOCKED = "1101";

    /**
     * HTML colors
     */
    public static String[] HTML_COLORS = {"red", "blue", "teal", "green", "orange", "purple", "lime", "aqua"};
}

