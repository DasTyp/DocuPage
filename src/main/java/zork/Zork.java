package zork;

import com.google.gson.Gson;
import communication.BrokerConnection;
import view.ChatPanel;
import view.GamePanel;
import view.GuiMainFrame;
import view.StatsPanel;

import view.MapPanel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class for the project, it parses, executes, saves and loads the game
 */
public class Zork {
    /**
     * Class variable game
     */
    public static Game game;

    /**
     * Class variable gson for reading and saving the jsons (library in folder libs)
     */
    private static Gson gson = new Gson();

    /**
     * Main frame of complete GUI
     */
    public static GuiMainFrame gui = GuiMainFrame.getInstance();

    /**
     * Game part of GUI
     */
    public static GamePanel gamePanel;

    /**
     * Chat part of GUI
     */
    public static ChatPanel chat =ChatPanel.getInstance();

    /**
     * The game is started from here
     *
     * @param args arguments for executing the main function
     */
    public static void main(String[] args) {
        initGame();
        initGui();
    }

    /**
     * Method for saving the current game state into file savedGame.json (player, rooms with items and ways)
     *
     * @param g Game that has to be saved
     */
    public static String saveGame(Game g)
    {
        String output = "";
        // Serialization
        String savedGame = gson.toJson(g);
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.SAVED_GAME));
            writer.write(savedGame);
            writer.close();
            output = "\nGame saved.";
        }
        catch (IOException e)
        {
            output = "\nFehler beim Speichern in Datei.";
        }
        return output;
    }

    /**
     * Method for loading the game state from the given file
     * For the start of the game this function is called with database.json
     * If this function is called during the game it will try to load savedGame.json if it exists
     *
     * @param jsonFile Json file that has to be loaded
     */
    public static String loadGame(String jsonFile)
    {
        String output = "";
        Path path = Paths.get(jsonFile);
        if(!Files.exists(path)) {
            output = "There's no saved game to load.";
        }
        else {
            game = Zork.parseData(jsonFile);
            if (game != null) {
                game.setPlayer(game.player);
                output = "\nGame loaded.\n\n" + game.getCurrentRoom().toString();
                Game.state = Constants.STATE_PLAY;
            }
            gamePanel = gamePanel.reset();
        }
        return output;
    }

    /**
     * Method to initially load game (required because database.json is encapsulated in .jar)
     *
     * @author Jonas Pröll
     */
    public static void initGame() {
        InputStream instream = Zork.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        if (game != null) {
            game.setPlayer(game.player);
        }
    }

    /**
     * Start new game and ask for difficulty
     * @return String output
     * @author Yvonne Rahnfeld
     */
    public static String newGame() {
        String output = "";
        InputStream instream = Zork.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        if (game != null) {
            game.setPlayer(game.player);
            output = "\nWELCOME TO THE ZORK GAME!\n" +
                    "\nA new game is started.....\n" + Constants.INTRO_TEXT +
                    "\nPlease choose a difficulty level for this game:\n\n     easy (1)      medium (2)      hard (3)\n";
            Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
            gamePanel = gamePanel.reset();
        }
        return output;
    }

    /**
     * Method to quit game and disconnect from broker
     *
     * @author Yvonne Rahnfeld
     */
    public static void exitGame() {
        BrokerConnection brokerConnection = BrokerConnection.getInstance();
        brokerConnection.disconnect();
        System.exit(0);
    }

    /**
     * Method is parsing the content of the given json (player, rooms with items and ways) and
     * returns a game with the corresponding settings
     *
     * @param file File that has to be parsed
     * @return Parsed game data
     */
    public static Game parseData(String file) {
        Game game;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Deserialization
            game = gson.fromJson(sb.toString(), Game.class);

            reader.close();

            return game;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method is parsing the content of the given json (player, rooms with items and ways) and
     * returns a game with the corresponding settings
     *
     * @param instream file as stream
     * @return Parsed game data
     */
    public static Game parseData(InputStream instream) {
        Game game;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Deserialization
            game = gson.fromJson(sb.toString(), Game.class);

            reader.close();

            return game;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to initially load gui
     *
     * @author Yvonne Rahnfeld
     */
    private static void initGui() {
        gamePanel = GamePanel.getInstance();
        gui.addRight(StatsPanel.getInstance());
        gui.addRight(chat);
        gui.addLeft(gamePanel);
        MapPanel mapPanel = MapPanel.getInstance(gui);
        gui.addMain(mapPanel);
        gui.setVisible(true);
        gui.pack();
        gui.pack();
        gamePanel.addToOutput("WELCOME TO THE ZORK GAME!\n\nIf you want to start a new game type \"new\", if you want " +
                "to load a saved game type \"load\".");
    }
}