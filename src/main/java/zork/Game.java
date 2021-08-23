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
 * This class processes user input and controls the hole game
 */
public class Game
{
    /**
     * Contains the players data
     */
    Player player;

    /**
     * List of all rooms in this game
     */
    @SerializedName("rooms")
    List<Room> rooms;

    /**
     * Main playing method that processes user input, filters possible commands and calls the related methods
     * @param player The current player in the game
     */
    public void play(Player player)
    {
        Scanner userInput = new Scanner(System.in);
        this.player = player;

        System.out.println(Constants.INTRO_TEXT);
        Map map; //Declaration of the Map

        System.out.println("welcome to the zork game.");
        Path path = Paths.get(Constants.SAVED_GAME);
        if(!Files.exists(path)) {
            System.out.println("if you want to load a saved game type 'load'.");
        }
        System.out.println(getCurrentRoom());
        System.out.println(getCurrentRoom().getDescription());

        // While-loop for listening to the input commands after each action
        while (true)
        {
            String input = userInput.nextLine();

            if (input.matches("help|info")) {
                System.out.println("Available commands: " + Constants.COMMAND_LIST);
            }
            // hidden warp command
            else if (input.matches("warp|warp (.+)"))
            {
                if (input.matches("warp (.+)")) {
                    String roomName = input.replace("warp ", "");
                    player.setRoomName(roomName);
                    System.out.println(getCurrentRoom());
                    System.out.println(getCurrentRoom().getDescription());
                }
                else {
                    System.out.println("You have to say which room you want to warp to.");
                }
            }
            else if (input.matches("inventory|show inventory")) {
                player.inventory.show();
            }
            else if (input.equals("quit") || input.equals("exit")) {
                System.exit(0);
            }
            else if (input.matches("look|look (.+)")) {
                Pattern p = Pattern.compile("look (.+)");
                Matcher m = p.matcher(input);
                if (m.find()) {
                    look(m.group(1));
                }
                else {
                    System.out.println("You have to say where you want to look (around, north, south, east, west, up, down).");
                }
            }
            else if (input.matches("move|move (.+)")) {
                Pattern p = Pattern.compile("move (.+)");
                Matcher m = p.matcher(input);
                if (m.find()) {
                    move(m.group(1));
                }
                else {
                    System.out.println("You have to say where you want to move (north, south, east, west, up, down).");
                }
            }
            else  if(isProperInput(input,Constants.DIRECTIONS)){
                move(input);
            }
            else if(input.matches(Constants.EACH_DIRECTION)){
                look(input);
            }
            else if (input.matches("take|take (.+)"))
            {
                if (input.matches("take (.+)")) {
                    String itemName = input.replace("take ", "");
                    take(itemName);
                }
                else {
                    System.out.println("You have to say what you want to take and add to your inventory.");
                }
            }
            else if (input.matches("drop|drop (.+)"))
            {
                if(input.matches("drop (.+)")) {
                    String itemName = input.replace("drop ","");
                    drop(itemName);
                }
                else {
                    System.out.println("You have to specify an item from your inventory to drop.");
                }
            }
            else if (input.equals("?")) {
                System.out.println(getCurrentRoom());
            }
            else if (input.equals("save")) {
                Zork.saveGame(this);
            }
            else if (input.matches("restore|load")) {
                Zork.loadGame(Constants.SAVED_GAME);
            }
            else if (input.matches("map")){
                map=new Map("MAP", Constants.MAP);
            }
            else {
                System.out.println("Unknown command.");
            }
        }
    }

    /**
     * Shows information about all ways, things and available items in current room when looking around or
     * shows information about the way for given direction
     * @param lookingAt The direction the player is looking at
     */
    public void look(String lookingAt)
    {
        // Boolean that indicates if the command "look around" has been written
        boolean isEachDirection = lookingAt.equals(Constants.EACH_DIRECTION);

        // Entered phrase is not "look around" and is not "look + valid direction"
        if (!isProperInput(lookingAt, Constants.DIRECTIONS) && !isEachDirection) {
            System.out.println("no valid direction. please enter look north / south / west / east / up or down.");
        }
        // The current room has no ways (actually this shouldn't happen as you have to enter the room somehow)
        else if (!hasWays()) {
            System.out.println("you're stucked in a room. there's no way hiding there.");
        }
        // Entered phrase is "look + valid direction" but there is no way in the chosen direction
        else if (getWayForDirection(lookingAt) == null && !isEachDirection) {
            System.out.println("there's nothing in the direction " + lookingAt + ".");
        }
        // Entered phrase is "look around": show everything in the current room (ways, items)
        else if (isEachDirection) {
            //Show available ways in the current room
            for (Way way : getCurrentRoom().getRoomWayList())
            {
                String wayDescription = way.isFree() ? way.getDescription() : way.getAltDescription();
                System.out.println("there is a " + way.getName() + " going " + way.getDirection() + ". " + wayDescription);
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
        else {
            Way resultWay = getWayForDirection(lookingAt);
            String wayDescription = resultWay.isFree() ? resultWay.getDescription() : resultWay.getAltDescription();
            System.out.println("there is a " + resultWay.getName() + " going " + lookingAt + ". " + wayDescription);
        }
    }

    /**
     * Moves in the given direction if it's a valid direction and the way is not blocked
     * @param direction The direction the player wants to move
     */
    public void move(String direction)
    {
        if (!isProperInput(direction, Constants.DIRECTIONS)) {
            System.out.println("no valid direction. please enter move north / south / west / east / up or down.");
        }
        else if (!hasWays()) {
            System.out.println("you're stucked in a room. there's no way hiding there.");
        }
        else if (getWayForDirection(direction) == null) {
            System.out.println("you can't move in this direction.");
        }
        else {
            Way resultWay = getWayForDirection(direction);
            System.out.println("you're taking the " + resultWay.getName() + " " + direction + ". ");
            player.setRoomName(resultWay.getTo());
            Room resultRoom = getCurrentRoom();
            String description = resultRoom.getDescription();
            if (resultRoom.getVisited() >= 1) {
                description = resultRoom.getAltDescription();
            }
            System.out.println(resultRoom + " It's " + description + ".");
            resultRoom.incrementVisited();
        }
    }

    /**
     * adds item to the players inventory if an item with given name exists in current room and inventory isn't full,
     * if the item is removable it will be removed from current room
     * @author Yvonne Rahnfeld
     * @param itemName from item to take, from user input
     */
    public void take(String itemName) {
        Item item = getItemFromCurrentRoom(itemName);
        if (item != null) {
            boolean itemAdded = player.inventory.addItem(item);
            if (itemAdded) {
                System.out.println("item \"" + item.getName() + "\" was successfully added to your inventory. ");
                if (item.getState().equals("removable")) {
                    getCurrentRoom().getRoomItemList().remove(item);
                } else {
                    System.out.println("But there is still plenty of it around here. ");
                }
            }
        }
        else {
            System.out.println("the thing \"" + itemName + "\" you want to take is not an item around here. ");
        }
    }

    /**
     * @author Yvonne Rahnfeld
     * @param itemName from item to look for in current room, from user input
     * @return searched item if it is in current room
     */
    private Item getItemFromCurrentRoom(String itemName) {
        Item foundItem = null;
        List<Item> roomItems = getCurrentRoom().getRoomItemList();
        for (Item item: roomItems) {
            if (item.getName().equals(itemName)) {
                foundItem =  item;
            }
        }
        return foundItem;
    }

    /**
     * Drops specified item from user Inventory into the current room if that item is in the players inventory.
     * If an item already exists, don't add a new one.
     * @author Christian Litke
     * @param itemName from UserInput.
     */
    public void drop(String itemName) {
        Item invItem = getItemFromInventory(itemName);
        List<Item> roomItems =getCurrentRoom().getRoomItemList();
        boolean itemFound = false;
        if (invItem != null) {
            boolean itemRemoved = player.inventory.removeItem(invItem);
            if (itemRemoved) {
                System.out.println("item \"" + invItem.getName() + "\" has been dropped. ");
                //check if item already exists in room. Only applies for items there are infinite of.
                for(Item roomItem: roomItems){
                    if (roomItem.getName().equals(itemName)) {
                        itemFound = true;
                        break;
                    }
                }
                if(itemFound == false){
                    getCurrentRoom().getRoomItemList().add(invItem);
                    invItem.setState("removable");
                }
                else {
                    System.out.println("You can't tell it apart from all the other " + invItem.getName() + " anymore. ");
                }
            }
        }
        else {
            System.out.println("the thing \"" + itemName + "\" you want to drop isn't in your inventory. ");
        }
    }

    /**
     * @author Christian Litke
     * @param itemName from item to look for in player inventory, from user input
     * @return searched item if it is in inventory. Null if it isn't
     * helper function, fetches item object from players inventory if it exists.
     */
    private Item getItemFromInventory(String itemName) {
        Item foundItem = null;
        List<Item> invItems = player.inventory.getInventoryList();
        for (Item item: invItems) {
            if (item.getName().equals(itemName)) {
                foundItem =  item;
            }
        }
        return foundItem;
    }

    /**
     * Helper method: Checks if a given string is contained in a given list
     * @param input Given input that has to be checked if it's proper
     * @param properInput List of proper input
     */
    private boolean isProperInput(String input, List<String> properInput)
    {
        if (properInput.contains(input)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Helper method: Checks if the current room has ways
     */
    private boolean hasWays()
    {
        if (getCurrentRoom().getRoomWayList().size() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Helper method: Returns the way in the given direction if available (otherwise the way is null)
     * @param direction Given direction for looking for ways
     * @return Way if there's a way in the given direction, otherwise return null
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
     * Helper method: Returns the current room
     */
    private Room getCurrentRoom()
    {
        Room currentRoom = null;
        for (Room r : rooms)
        {
            if (r.getName().equals(player.getRoomName())) {
                currentRoom = r;
            }
        }
        return currentRoom;
    }
}