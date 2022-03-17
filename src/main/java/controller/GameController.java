package controller;

import view.GuiMainFrame;
import view.MapPanel;
import zork.*;

/**
 * Controller for game
 * @author Yvonne Rahnfeld
 */
public class GameController {
    /**
     * The controlled game
     */
    private Game game;

    /**
     * Constructor
     * @param game The controlled game
     */
    public GameController(Game game) {
        this.game = game;
    }

    public GameController() {
        this.game = Zork.game;
    }

    /**
     * Check players health, hunger and thirst level in game
     * @return String
     * @author Yvonne Rahnfeld
     */
    public String checkAndProcessPlayersEnergy() {
        return game.checkAndProcessPlayersEnergy();
    }

    /**
     * Process given input from GUI and call game methods
     * @param input User input
     * @return Output String for GUI
     */
    public String processInput(String input) {
        String output = "";
        if (input.matches("trade|trade (.+)")) {
            output = processTradeOffer(input);
        } else if (Game.state == Constants.STATE_PLAY) {
            output = processGameStatePlay(input);
        } else {
            output = processExceptionalGameState(input);
        }
        return output;
    }

    /**
     * Process given input from GUI and call game methods (if game is in playing state)
     * @param input User input
     * @return Output String for GUI
     */
    private String processGameStatePlay(String input) {
        String output = "";
        if (input.matches("help|info")) {
            System.out.println("Available commands: " + Constants.COMMAND_LIST);
        } else if (input.equals("?")) {
            output = game.getCurrentRoom().toString();
        } else if (input.matches("map")) {
            MapPanel.getInstance(GuiMainFrame.getInstance()).openMap();
        } else if (input.matches("inventory|show inventory")) {
            output = game.showPlayersInventory();
        } else if (input.matches("help (.+)")) {
            output = game.helpWithThing(input.replace("help ", ""));
        } else if (input.matches("show strength (.+)")) {
            input = input.replace("show strength ", "");
            if (Constants.isProperInput(input, Constants.WEAPONS_LIST)) {
                output = game.showWeaponsStrength(input);
            } else {
                output = "This is not a weapon.";
            }
        } else if (input.matches("name (.+)")) {
            output = game.setPlayersName(input.replace("name ", ""));
        } else if (input.matches("rename (.+)")) {
            output = game.setPlayersName(input.replace("rename ", ""));
        } else if (input.matches("name|rename")) {
            output = "Please choose a name for your player.";
        } else if (input.equals("save")) {
            output = Zork.saveGame(game);
        } else if (input.matches("restore|load")) {
            output = Zork.loadGame(Constants.SAVED_GAME);
        } else if (input.matches("new|new game")) {
            output = Zork.newGame();
        } else if (input.equals("quit") || input.equals("exit")) {
            Zork.saveGame(game);
            Zork.exitGame();
        } else if (input.matches("look (.+)")) {
            output = game.look(input.replace("look ", ""));
        } else if (input.matches(Constants.EACH_DIRECTION)) {
            output = game.look(input);
        } else if (input.matches("look")) {
            output = "You have to say where you want to look (around, north, south, east, west, up, down).";
        } else if (input.matches("move (.+)")) {
            output = game.move(input.replace("move ", ""));
        } else if (Constants.isProperInput(input, Constants.DIRECTIONS)) {
            output = game.move(input);
        } else if (input.matches("move")) {
            output = "You have to say where you want to move (north, south, east, west, up, down).";
        } else if (input.matches("take (.+)")) {
            output = game.take(input.replace("take ", ""));
        } else if (input.matches("take")) {
            output = "You have to say what you want to take and add to your inventory.";
        } else if (input.matches("drop (.+)")) {
            output = game.drop(input.replace("drop ", ""));
        } else if (input.matches("drop")) {
            output = "You have to specify an item from your inventory to drop.";
        } else if (input.matches("eat (.+)")) {
            output = game.eat(input.replace("eat ", ""));
        } else if (input.matches("eat")) {
            output = "You have to say what you want to eat.";
        } else if (input.matches("drink (.+)")) {
            output = game.drink(input.replace("drink ", ""));
        } else if (input.matches("drink")) {
            output = "You have to say what you want to drink.";
        } else if (input.matches("use (.+) with (.+)")) {
            String[] parameters = createInputWithTwoParameters(input, "use ", "with");
            output = game.useWith(parameters[0], parameters[1]);
        } else if (input.matches("use (.+)")) {
            output = game.use(input.replace("use ", ""));
        } else if (input.matches("use ")) {
            output = "You have to say what you want to use.";
        } else if (input.matches("feed (.+) with (.+)")) {
            String[] parameters = createInputWithTwoParameters(input, "feed ", "with");
            output = game.feedWith(parameters[0], parameters[1]);
        } else if (input.matches("feed (.+)")) {
            output = "You have to say what you want to feed the animal with.";
        } else if (input.matches("feed")) {
            output = "You have to say what you want to feed.";
        } else if (input.matches("track (.+)")) {
            output = game.track(input.replace("track ", ""));
        } else if (input.matches("track")) {
            output = "You have to say what you want to track.";
        } else if (input.matches("fight (.+) with (.+)")) {
            String[] parameters = createInputWithTwoParameters(input, "fight ", "with");
            output = game.fightWith(parameters[0], parameters[1]);
        } else if (input.matches("fight (.+)")) {
            output = "You have to say the weapon you want to fight with.";
        } else if (input.equals("fight")) {
            output = "You have to say what you want to fight.";
        } else {
            output = "Unknown command.";
        }
        return output;
    }

    /**
     * Process given input from GUI and call game methods (if game is in exceptional state)
     * @param input User input
     * @return Output String for GUI
     */
    private String processExceptionalGameState(String input) {
        String output = "";
        if (Game.state == Constants.STATE_BEGINNING) {
            output = processBeginningOfGame(input);
        } else if (Game.state == Constants.STATE_NEW_LOAD_OR_QUIT) {
            output = processBreakOfGame(input);
        } else if (Game.state == Constants.STATE_CHOOSE_DIFFICULTY) {
            output = processDifficultySetting(input);
        } else if (Game.state == Constants.STATE_SET_NAME) {
            Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
            game.setPlayersName(input);
            output = "Hello, " + input + "!\n\nA new game is started.....\n" + Constants.INTRO_TEXT +
                    "Please choose a difficulty level for this game:\n\n     easy (1)      medium (2)      hard (3)\n";
        } else if (Game.state == Constants.STATE_BLOCK) {
            output = game.block(input);
            Game.state = Constants.STATE_PLAY;
        } else if (Game.state == Constants.STATE_CRITICAL_STRIKE) {
            output = game.criticalStrike(input);
            Game.state = Constants.STATE_PLAY;
        } else if (Game.state == Constants.STATE_TRY_FILLUP) {
            if (input.equals("try")) {
                output = game.fillUpHealthByChance();
            } else {
                output = "You didn't type \"try\", so you missed the chance!\n";
                output += game.gameOver();
            }
        }
        return output;
    }

    /**
     * Process given input from GUI and call game methods (if game is in beginning state)
     * @param input User input
     * @return Output String for GUI
     */
    private String processBeginningOfGame(String input) {
        String output = "";
        if (input.matches("new|new game")) {
            Game.state = Constants.STATE_SET_NAME;
            output = "Please enter your name:";
        } else if (input.equals("quit") || input.equals("exit")) {
            Zork.exitGame();
        } else if (input.matches("restore|load")) {
            output = Zork.loadGame(Constants.SAVED_GAME);
        } else {
            output = "This was not a valid command.\nTo load a saved game type \"load\" or if you want to start a " +
                    "new game type \"new\".";
        }
        return output;
    }

    /**
     * Process given input from GUI and call game methods (if game is in interrupted state)
     * @param input User input
     * @return Output String for GUI
     */
    private String processBreakOfGame(String input) {
        String output = "";
        if (input.matches("new|new game")) {
            output = Zork.newGame();
        } else if (input.equals("quit") || input.equals("exit")) {
            Zork.exitGame();
        } else if (input.matches("restore|load")) {
            output = Zork.loadGame(Constants.SAVED_GAME);
        } else {
            output = "This was not a valid command.\nTo load a saved game type \"load\" or if you want to start a " +
                    "new game type \"new\".";
        }
        return output;
    }

    /**
     * Process given input from GUI and call game methods (if game is in state of setting difficulty)
     * @param input User input
     * @return Output String for GUI
     */
    private String processDifficultySetting(String input) {
        String output = "";
        Game.state = Constants.STATE_PLAY;
        String orientationOutput = game.getCurrentRoom() + " " + game.getCurrentRoom().getDescription();
        switch (input) {
            case "easy", "1" -> {
                game.setDifficulty(1);
                output = "Difficulty level easy was set.\n" + orientationOutput;
            }
            case "medium", "2" -> {
                game.setDifficulty(2);
                output = "Difficulty level medium was set.\n" + orientationOutput;
            }
            case "hard", "3" -> {
                game.setDifficulty(3);
                output = "Difficulty level hard was set.\n" + orientationOutput;
            }
            default -> {
                Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
                output = "This was not a valid difficulty level.\n";
            }
        }
        if (game.getDifficulty() == 2 || game.getDifficulty() == 3) {
            game.setEnemiesEnergy();
        }
        return output;
    }

    /**
     * Process given input from GUI here, if it started with "trade". If trading feature is unlocked (traders hut was
     * visited by player) and offered item is in players inventory the trade gets initialized
     * @param input user input string
     * @return confirmation or error message
     */
    private String processTradeOffer(String input) {
        String output = "";
        if (game.getRoom("traders hut").getVisited() > 0) {
            if (input.matches("trade (.+) for (.+) with (.+)")) {
                // extract Strings for offeredItem, requestedItem and tradingPartner
                String[] parameters = createInputWithThreeParameters(input, "trade", "for", "with");
                // init trade if offeredItem is in players inventory
                if (game.getPlayer().inventory.hasItem(parameters[0])) {
                    CommunicationController.getInstance().initTradeOffer(game.getPlayer().inventory.getItem(parameters[0]),
                            parameters[1], parameters[2]);
                    output = "Your trading offer was sent to player " + parameters[2] +
                            ".\nYou will be notified if the player has accepted your offer.";
                } else {
                    output = "The item " + parameters[0] + " is not in your inventory! You can only trade with items you own.";
                }
            } else {
                output = "This command is not valid. If you want to start a trading offer to someone, the correct " +
                        "command is:\n\"trade <offeredItem> for <requestedItem> with <tradingPartner>\"";
            }
        } else {
            output = "Sorry, this is not possible. You haven't unlocked the trading feature yet!";
        }
        return output;
    }

    /**
     * Helper method to get two parameters out of user input
     *
     * @param input      The user input which should be split
     * @param command    The command in the front
     * @param connection The word between the parameters
     * @return An array of Strings with the two parameters
     * @author Patrick Mayer
     */
    private String[] createInputWithTwoParameters(String input, String command, String connection) {
        String temp = input.replace(command, "");
        return temp.split(" " + connection + " ");
    }

    /**
     * Get three parameters out of user input
     * @param input user input string
     * @param firstReplacement first string to remove
     * @param secondReplacement second string to remove
     * @param thirdReplacement third string to remove
     * @return Array of extracted strings
     */
    private String[] createInputWithThreeParameters(String input, String firstReplacement, String secondReplacement, String thirdReplacement) {
        String temp = input.replace(firstReplacement + " ", "");
        String[] first = temp.split(" " + secondReplacement + " ");
        String[] second = first[1].split(" " + thirdReplacement + " ");
        return new String[]{first[0], second[0], second[1]};
    }

    public Game getGame(){
        return game;
    }
}
