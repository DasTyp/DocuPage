package zork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class with some constant variables
public class Constants {
    // Constans for the possible directions in the game
    public static final List<String> DIRECTIONS = new ArrayList<>(Arrays.asList("north", "east", "west", "south", "up", "down"));
    public static final String EACH_DIRECTION = "around";

    // Possible commands for the game - has to be extended if there are new commands added to the game
    public static final List<String> COMMAND_LIST = new ArrayList<>(
            Arrays.asList("move + north / east / west / south / up / down",
                    "look + around / north / east / west / south / up / down",
                    "quit or exit",
                    "?",
                    "help or info",
                    "inventory",
                    "take")
    );

    // Location of the jsons for a new or a saved game
    public static final String SAVED_GAME = "files/savedGame.json";
    public static final String NEW_GAME = "files/database.json";
}