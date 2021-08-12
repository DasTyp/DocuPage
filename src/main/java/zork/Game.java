package zork;

import com.google.gson.annotations.SerializedName;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class controls the hole game
 */
public class Game
{
    Player player;
    @SerializedName("rooms")
    List<Room> rooms;

    /**
     * Main playing method with the possible commands and their method call
     * @param player
     */
    public void play(Player player)
    {
        Scanner userInput = new Scanner(System.in);
        this.player = player;

        System.out.println("welcome to the zork game.");
        Path path = Paths.get(Constants.SAVED_GAME);
        if(!Files.exists(path))
            System.out.println("if you want to load a saved game type 'load'.");
        System.out.println(getCurrentRoom());
        System.out.println(getCurrentRoom().getDescription());

        // While-loop for listening to the input commands after each action
        while (true)
        {
            String input = userInput.nextLine();

            if (input.matches("help|info"))
                System.out.println("Available commands: " + Constants.COMMAND_LIST);
            else if (input.equals("quit") || input.equals("exit"))
                System.exit(0);
            else if (input.matches("look|look (.+)"))
            {
                Pattern p = Pattern.compile("look (.+)");
                Matcher m = p.matcher(input);
                if (m.find())
                {
                    look(m.group(1));
                }
                else
                    System.out.println("You have to say where you want to look (around, north, south, east, west, up, down).");
            }
            else if (input.matches("move|move (.+)"))
            {
                Pattern p = Pattern.compile("move (.+)");
                Matcher m = p.matcher(input);
                if (m.find())
                    move(m.group(1));
                else
                    System.out.println("You have to say where you want to move (north, south, east, west, up, down).");
            }
            else if (input.equals("?"))
                System.out.println(getCurrentRoom());
            else if (input.equals("save"))
                Zork.saveGame(this);
            else if (input.matches("restore|load"))
                Zork.loadGame(Constants.SAVED_GAME);
            else
                System.out.println("Unknown command.");
        }
    }

    /**
     * Look method: shows for the current room: all available items and all available ways
     * @param lookingAt
     */
    public void look(String lookingAt)
    {
        // Boolean that indicates if the command "look around" has been written
        boolean isEachDirection = lookingAt.equals(Constants.EACH_DIRECTION);

        // Entered phrase is not "look around" and is not "look + valid direction"
        if (!isProperInput(lookingAt, Constants.DIRECTIONS) && !isEachDirection)
            System.out.println("no valid direction. please enter look north / south / west / east / up or down.");

        // The current room has no ways (actually this shouldn't happen as you have to enter the room somehow)
        else if (!hasWays())
            System.out.println("you're stuck in a room. there's no way hiding there.");

        // Entered phrase is "look + valid direction" but there is no way in the chosen direction
        else if (getWayForDirection(lookingAt) == null && !isEachDirection)
            System.out.println("there's nothing in the direction " + lookingAt + ".");

        // Entered phrase is "look around": show everything in the current room (ways, items)
        else if (isEachDirection)
        {
            //Show available ways in the current room
            for (Way way : getCurrentRoom().getRoomWayList())
            {
                System.out.println("there is a " + way.getName() + " going " + way.getDirection() + ". ");
            }
            //Show available items in the current room
            if (getCurrentRoom().getRoomItemList() != null)
            {
                for (Item item : getCurrentRoom().getRoomItemList())
                {
                    System.out.println(item.getName() + " " + item.getWhere() + ".");
                }
            }
        }

        // Entered phrase is "look + valid direction": show way for the selected direction
        else
        {
            Way resultWay = getWayForDirection(lookingAt);
            System.out.println("there is a " + resultWay.getName() + " going " + lookingAt + ".");
        }
    }

    /**
     * Move method: moves in the chosen direction if it's a valid direction and if there's a way in this direction
     * @param direction
     */
    public void move(String direction)
    {
        if (!isProperInput(direction, Constants.DIRECTIONS))
            System.out.println("no valid direction. please enter move north / south / west / east / up or down.");
        else if (!hasWays())
            System.out.println("you're stuck in a room. there's no way hiding there.");
        else if (getWayForDirection(direction) == null)
            System.out.println("you can't move in this direction.");
        else
        {
            Way resultWay = getWayForDirection(direction);
            System.out.println("you're taking the " + resultWay.getName() + " " + direction + ". ");
            player.setRoomName(resultWay.getTo());
            System.out.println(getCurrentRoom() + " It's " + getCurrentRoom().getDescription() + ".");
        }
    }

    /**
     * Helper method: Checks if a given string is contained in a given list
     * @param input
     * @param properInput
     */
    private boolean isProperInput(String input, List<String> properInput)
    {
        if (properInput.contains(input))
            return true;
        else
            return false;
    }

    /**
     * Helper method: Checks if the current room has ways
     */
    private boolean hasWays()
    {
        if (getCurrentRoom().getRoomWayList().size() == 0)
            return false;
        else
            return true;
    }

    /**
     * Helper method: Returns the way in the given direction if available (otherwise the way is null)
     * @param direction
     */
    private Way getWayForDirection(String direction)
    {
        Way resultWay = null;
        for (Way way : getCurrentRoom().getRoomWayList())
        {
            if (way.getDirection().equals(direction))
            {
                resultWay = way;
                break;
            }
        }
        return resultWay;
    }

    /**
     * Helper method: Returns the current room object
     */
    private Room getCurrentRoom()
    {
        Room currentRoom = null;
        for (Room r : rooms)
        {
            if (r.getName().equals(player.getRoomName()))
                currentRoom = r;
        }
        return currentRoom;
    }
}