package zork;

import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class for the project, it parses, executes, saves and loads the game
 */
public class Zork
{
    /**
     * Class variable game
     */
    private static Game game;

    /**
     * Class variable gson for reading and saving the jsons (library in folder libs)
     */
    private static Gson gson = new Gson();

    /**
     * The game is started from here
     * @param args arguments for executing the main function
     */
    public static void main(String[] args)
    {
        Splash.print();
        loadGame(Constants.NEW_GAME);
    }

    /**
     * Method for saving the current game state into file savedGame.json (player, rooms with items and ways)
     * @param g Game that has to be saved
     */
    public static void saveGame(Game g)
    {
        // Serialization
        String savedGame = gson.toJson(g);
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.SAVED_GAME));
            writer.write(savedGame);
            writer.close();
            System.out.println("Saved.");
        }
        catch (IOException e)
        {
            System.out.println("Fehler beim Speichern in Datei.");
        }
    }

    /**
     * Method for loading the game state from the given file
     * For the start of the game this function is called with database.json
     * If this function is called during the game it will try to load savedGame.json if it exists
     * @param jsonFile Json file that has to be loaded
     */
    public static void loadGame(String jsonFile)
    {
        Path path = Paths.get(jsonFile);
        if(!Files.exists(path))
            System.out.println("There's no saved game to load.");
        else
        {
            game = Zork.parseData(jsonFile);
            game.play(game.player);
            System.out.println("Game loaded.");
        }
    }

    /**
     * Method is parsing the content of the given json (player, rooms with items and ways) and
     * returns a game with the corresponding settings
     * @param file File that has to be parsed
     * @return Parsed game data
     */
    public static Game parseData(String file)
    {
        Game game;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }

            // Deserialization
            game = gson.fromJson(sb.toString(), Game.class);

            reader.close();

            return game;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}