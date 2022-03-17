package zork;

import com.google.gson.annotations.SerializedName;
import controller.CommunicationController;
import java.util.List;

/**
 * This class processes user input and controls the hole game
 */
public class Game {
    /**
     * Contains the players data
     */
    Player player;

    public static int state = Constants.STATE_BEGINNING;

    /**
     * Difficulty level of the game, affects energy operations
     */
    private int difficulty = 1;

    /**
     * Processes more complex actions
     */
    ActionHandler actionHandler = new ActionHandler();

    /**
     * List of all rooms in this game
     */
    @SerializedName("rooms")
    List<Room> rooms;

    public void setPlayer(Player player) {
        this.player = player;
        CommunicationController.getInstance().setPlayerName(player.getName());
        CommunicationController.getInstance().setPlayer(player);
    }

    public Player getPlayer(){
        return this.player;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Show players inventory
     */
    public String showPlayersInventory() {
        return player.inventory.show();
    }

    /**
     * show weapons strength
     *
     * @param name weapons name
     * @return String
     */
    public String showWeaponsStrength(String name) {
        Item weapon = player.inventory.getItem(name);
        return weapon.showStrength();
    }

    /**
     * set players name
     *
     * @param name players new name
     * @return String
     */
    public String setPlayersName(String name) {
        player.setName(name);
        CommunicationController.getInstance().setPlayerName(name);
        return "Your players name is now: " + player.getName();
    }

    public void setTimeBeforeAction(long time) {
        actionHandler.setTimeBeforeAction(time);
    }

    /**
     * Block in fight
     * @param input user input
     * @return String output
     */
    public String block(String input) {
        return actionHandler.block(input, this);
    }

    /**
     * Critical strike in fight
     * @param input user input
     * @return String output
     */
    public String criticalStrike(String input) {
        return actionHandler.criticalStrike(input, this);
    }

    /**
     * Shows information about all ways, things and available items in current room when looking around or
     * shows information about the way for given direction
     *
     * @param lookingAt The direction the player is looking at
     * @return String
     */
    public String look(String lookingAt) {
        String output = "";
        boolean isEachDirection = lookingAt.equals(Constants.EACH_DIRECTION);

        if (!Constants.isProperInput(lookingAt, Constants.DIRECTIONS) && !isEachDirection) {
            output = "No valid direction. please enter look north / south / west / east / up or down.";
        } else if (!hasWays()) {
            output = "You're stucked in a room. there's no way hiding there.";
        } else if (getWayForDirection(lookingAt) == null && !isEachDirection) {
            output = "There's nothing in the direction " + lookingAt + ".";
        } else if (isEachDirection) {
            output = getCurrentRoom().showAllThingsInRoom();
        } else {
            Way resultWay = getWayForDirection(lookingAt);
            String wayDescription = resultWay.isFree() ? resultWay.getDescription() : resultWay.getAltDescription();
            output = "There is a " + resultWay.getName() + " going " + lookingAt + ". " + wayDescription;
        }
        return output;
    }

    /**
     * Moves in the given direction if it's a valid direction and the way is not blocked
     *
     * @param direction The direction the player wants to move
     * @return String
     */
    public String move(String direction) {
        String output = "";
        reducePlayersEnergy();
        if (!Constants.isProperInput(direction, Constants.DIRECTIONS)) {
            output = "No valid direction. Please enter move north / south / west / east / up or down.";
        } else if (!hasWays()) {
            output = "You're stucked in a room. There's no way hiding there.";
        } else if (getWayForDirection(direction) == null) {
            output = "You can't move in this direction.";
        } else if (!getWayForDirection(direction).isFree()) {
            output = "The " + getWayForDirection(direction).getName() + " is currently blocked, you can't move in this " +
                    "direction until you find a way to unlock it.\n" + getWayForDirection(direction).getAltDescription();
        } else {
            Way resultWay = getWayForDirection(direction);
            output = "You're taking the " + resultWay.getName() + " " + direction + ". ";
            player.setRoomName(resultWay.getTo());
            Room resultRoom = getCurrentRoom();
            String description = resultRoom.getDescription();
            if (resultRoom.getVisited() >= 1 && !resultRoom.getAltDescription().equals("")) {
                description = resultRoom.getAltDescription();
            }
            output += resultRoom + " It's " + description + ".";
            resultRoom.incrementVisited();
            if (resultRoom.getName().equals("traders hut") && resultRoom.getVisited() == 1) {
                output += "\n\nYou have unlocked the feature ITEM TRADING!\nNow you have the possibility to trade with" +
                        " items by exchanging them for other players items.\n" + Constants.ABOUT_TRADING;
            }
        }
        return output;
    }

    /**
     * Adds item to the players inventory if an item with given name exists in current room and inventory isn't full.
     * If the item is removable it will be removed from current room. Water can not be taken.
     *
     * @param itemName from item to take, from user input
     * @return String
     * @author Yvonne Rahnfeld
     */
    public String take(String itemName) {
        String output = "";
        Item item = getCurrentRoom().getItem(itemName);
        if (item != null) {
            if (!item.getName().equals("water") || player.inventory.hasItem("empty bottle")) {
                item = item.getName().equals("water") ? player.inventory.getFilledUpWaterBottle() : item;
                boolean itemAdded = player.inventory.addItem(item);
                if (itemAdded) {
                    output = "Item \"" + item.getName() + "\" was successfully added to your inventory. ";
                    if (item.getState().equals("removable")) {
                        getCurrentRoom().getRoomItemList().remove(item);
                    } else {
                        output += "But there is still plenty of it around here. ";
                    }
                }
            } else {
                output = "You can not take the water with you, you don't have a empty vessel for it. ";
            }
        } else {
            output = "The thing \"" + itemName + "\" you want to take is not an item around here. ";
        }
        return output;
    }

    /**
     * Drops specified item from user Inventory into the current room if that item is in the players inventory.
     * If an item already exists, don't add a new one.
     *
     * @param itemName from UserInput.
     * @return String
     * @author Christian Litke
     */
    public String drop(String itemName) {
        String output = "";
        Item invItem = player.inventory.getItem(itemName);
        List<Item> roomItems = getCurrentRoom().getRoomItemList();
        boolean itemFound = false;
        if (invItem != null) {
            boolean itemRemoved = player.inventory.removeItem(invItem);
            if (itemRemoved) {
                output = "Item \"" + invItem.getName() + "\" has been dropped. ";
                //check if item already exists in room. Only applies for items there are infinite of.
                for (Item roomItem : roomItems) {
                    if (roomItem.getName().equals(itemName)) {
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    getCurrentRoom().getRoomItemList().add(invItem);
                    invItem.setState("removable");
                    invItem.setWhere("is lying on the ground");
                } else {
                    output += "You can't tell it apart from all the other " + invItem.getName() + " anymore. ";
                }
            }
        } else {
            output = "The thing \"" + itemName + "\" you want to drop isn't in your inventory. ";
        }
        return output;
    }

    /**
     * Helper method: Returns the way in the given direction if available (otherwise the way is null)
     *
     * @param direction Given direction for looking for ways
     * @return Way if there's a way in the given direction, otherwise return null
     */
    public Way getWayForDirection(String direction) {
        Way resultWay = null;
        for (Way way : getCurrentRoom().getRoomWayList()) {
            if (way.getDirection().equals(direction)) {
                resultWay = way;
                break;
            }
        }
        return resultWay;
    }

    /**
     * Helper method: Returns the current room
     */
    public Room getCurrentRoom() {
        Room currentRoom = null;
        for (Room r : rooms) {
            if (r.getName().equals(player.getRoomName())) {
                currentRoom = r;
            }
        }
        return currentRoom;
    }

    /**
     * Get room by its name
     *
     * @param roomName Name of the room to look for
     * @return Room is found in game, otherwise return null
     * @author Yvonne Rahnfeld
     */
    public Room getRoom(String roomName) {
        Room room = null;
        for (Room r : rooms) {
            if (r.getName().equals(roomName)) {
                room = r;
            }
        }
        return room;
    }

    /**
     * Method to use items (command "use <item>")
     *
     * @param itemName Given user input
     * @return String
     * @author Yvonne Rahnfeld
     */
    public String use(String itemName) {
        String output = "";
        reducePlayersEnergy();
        if (itemName.equals("rucksack")) {
            Item rucksack = getCurrentRoom().getItem(itemName);
            if (null == rucksack) {
                rucksack = player.inventory.getItem(itemName);
                player.inventory.removeItem(itemName);
            }
            if (null != rucksack) {
                output = actionHandler.useRucksack(rucksack, this);
            } else {
                output = "There is no item with name \"" + itemName + "\" around here!";
            }
        } else {
            output = "You can not use this.";
        }
        return output;

    }

    /**
     * Method to implement command use with
     *
     * @param firstItem  user input for first item
     * @param secondItem user input for second item
     * @return String
     * @author Patrick Mayer
     */
    public String useWith(String firstItem, String secondItem) {
        String output = "";
        reducePlayersEnergy();
        Item item1 = player.inventory.getItem(firstItem);
        if (null != item1) {
            NonItem nonItem = getCurrentRoom().getNonItem(secondItem);
            if (null != nonItem) {
                if (item1.getName().equals("broken two-way radio") && nonItem.getName().equals("two-way radio")) {
                    output = Constants.OUTRO_TEXT + "\n\nYou have won the game!\n\nIf you want to quit the game type " +
                            "\"quit\"  or\nif you want to start a new game type \"new\".";
                    Game.state = Constants.STATE_NEW_LOAD_OR_QUIT;
                } else {
                    output = actionHandler.useWith(item1, nonItem, this);
                }
            } else {
                Item item2 = player.inventory.getItem(secondItem);
                if (item2 != null) {
                    output = actionHandler.useWith(item1, item2, this);
                } else {
                    Enemy enemy = getCurrentRoom().getEnemy(secondItem);
                    if (null != enemy) {
                        output = actionHandler.useWith(item1, enemy, this);
                    } else {
                        Animal animal = getCurrentRoom().getAnimal(secondItem);
                        if (null != animal) {
                            output = actionHandler.useWith(item1, animal, this);
                        } else {
                            output = "You do not have the second item in the inventory and there is no thing around here in the room.";
                        }
                    }
                }
            }
        } else {
            output = "You do not have the first item in your inventory.";
        }
        return output;
    }

    /**
     * Method to implement command feed with
     *
     * @param animalToFeed user input for animal
     * @param itemToFeed   user input for item
     * @return String
     * @author Patrick Mayer
     */
    public String feedWith(String animalToFeed, String itemToFeed) {
        String output = "";
        Item item = player.inventory.getItem(itemToFeed);
        if (null != item) {
            Animal animal = getCurrentRoom().getAnimal(animalToFeed);
            if (animal != null) {
                output = actionHandler.feedWith(animal, itemToFeed, this);
            } else {
                output = "This animal is not in the room";
            }
        } else {
            output = "You do not have the food in the inventory.";
        }
        return output;
    }

    /**
     * Method to implement command fight with
     *
     * @param enemyName  user input for enemy
     * @param weaponName user input for weapon
     * @return String
     * @author Patrick Mayer
     */
    public String fightWith(String enemyName, String weaponName) {
        String output = "";
        reducePlayersEnergy();
        Item item = player.inventory.getItem(weaponName);
        if (null != item) {
            Enemy enemy = getCurrentRoom().getEnemy(enemyName);
            if (enemy != null) {
                output = actionHandler.fightEnemy(item, enemy, this);
            } else {
                output = "This enemy is not in the room.";
            }
        } else {
            output = "You do not have the weapon in the inventory.";
        }
        return output;

    }

    /**
     * Method to implement command for eat
     *
     * @param itemName The user input
     * @return String
     * @author Patrick Mayer
     */
    public String eat(String itemName) {
        String output = "";
        if (player.inventory.hasItem(itemName) || null != getCurrentRoom().getItem(itemName)) {
            if (Constants.isProperInput(itemName, Constants.FOOD_LIST)) {
                output = actionHandler.eat(itemName, player, true);
            } else {
                output = "This item is not a food";
            }
        } else {
            output = "You do not have this item in the inventory.";
        }
        return output;
    }

    /**
     * Method to implement command drink
     *
     * @param itemName The user input
     * @return String
     * @author Patrick Mayer
     */
    public String drink(String itemName) {
        String output = "";
        if (player.inventory.hasItem(itemName) || null != getCurrentRoom().getItem(itemName) ||
                (player.inventory.hasItem("water bottle") && itemName.equals("water"))) {
            if (Constants.isProperInput(itemName, Constants.DRINKABLE_LIST)) {
                output = actionHandler.eat(itemName, player, false);
            } else {
                output = "This is nothing to drink.";
            }
        } else {
            output = "You do not have this item in the inventory.";
        }
        return output;
    }

    /**
     * Method to implement command track
     *
     * @param thing The user input
     * @return String
     * @author Christian Litke
     */
    public String track(String thing) {
        String output = "";
        reducePlayersEnergy();
        NonItem tracks = getCurrentRoom().getNonItem(thing);
        if (tracks == null) {
            output = "You can't track " + thing + ".";
        } else {
            output = actionHandler.trackPawprints(tracks, this);
        }
        return output;
    }

    /**
     * Helper method: Checks if the current room has ways
     */
    private boolean hasWays() {
        if (getCurrentRoom().getRoomWayList().size() == 0) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * Method to print a info for the player. It will tell what he can do with nonItems, Animals or Enemies or
     * information about how to trade items
     *
     * @param input the input of the user
     * @return String
     * @author Patrick Mayer
     */
    public String helpWithThing(String input) {
        String output = "";
        if (input.matches("trading|trade")) {
            output = Constants.ABOUT_TRADING;
        } else {
            NonItem nonItem = getCurrentRoom().getNonItem(input);
            if (null != nonItem) {
                output = "You can use the command: " + nonItem.getCommand() + " with the item: " + nonItem.getNecessaryItem();
            } else {
                Enemy enemy = getCurrentRoom().getEnemy(input);
                if (null != enemy) {
                    output = "You can use the command: " + enemy.getCommand() + " with the item: " + enemy.getNecessaryItem();
                } else {
                    Animal animal = getCurrentRoom().getAnimal(input);
                    if (null != animal) {
                        output = "You can use the command: " + animal.getCommand() + " with the item: " + animal.getNecessaryItem();
                    } else {
                        output = "This is not a thing in this room!";
                    }
                }
            }
        }
        return output;
    }

    /**
     * Check players health level. If health is 0 and he doesn't get health fill up the player will die (game over).
     *
     * @author Yvonne Rahnfeld
     */
    public String checkAndProcessPlayersEnergy() {
        String output = "";

        if (player.getHealth() <= 0 && Game.state != Constants.STATE_NEW_LOAD_OR_QUIT) {
            output = "\n\nYour health level reached zero!\nIt can be increased, but only if you are lucky...\n" +
                    "Type in \"try\" if you want to take this chance!";
            Game.state = Constants.STATE_TRY_FILLUP;
        }
        return output;
    }

    /**
     * Action when player is game over
     * @return String
     */
    public String gameOver() {
        Game.state = Constants.STATE_NEW_LOAD_OR_QUIT;
        return "\n\nGAME OVER: YOU DIED!\n\nIf you want to load a saved game type \"load\" or\nif you want " +
                "to start a new game type \"new\".";
    }

    /**
     * Health will be filled up by chance
     * @return String
     * @author Yvonne Rahnfeld
     */
    public String fillUpHealthByChance() {
        String output = "";
        Energy energy = player.getHealthObject();
        String energyName = "health";
        int randomNumber2 = (int) (Math.random() * 3 * difficulty + 1);
        String random = "" + randomNumber2;
        int increaseValue1 = energy.fillUpIfStringsAreEqual("1", random, getDifficulty());
        int increaseValue = increaseValue1 != 0 ? increaseValue1 :
                energy.fillUpIfStringsAreEqual("2", random, getDifficulty());
        if (increaseValue > 0) {
            output = "\nYou were lucky! Your " + energyName + " level was filled up by " + increaseValue + "!";
            Game.state = Constants.STATE_PLAY;
        } else {
            output += "\nYou had no luck, your " + energyName + " level was not filled up!";
            output += gameOver();
        }
        return output;
    }

    /**
     * Increase energy of all enemies in game for difficulty level medium or hard
     *
     * @author Yvonne Rahnfeld
     */
    public void setEnemiesEnergy() {
        for (Room room : rooms) {
            List<Enemy> enemyList = room.getRoomEnemyList();
            if (null != enemyList) {
                for (Enemy enemy : room.getRoomEnemyList()) {
                    int initialEnergy = enemy.getEnergy().getValue();
                    int newEnergy;
                    switch (getDifficulty()) {
                        case 2 -> {
                            newEnergy = initialEnergy + initialEnergy / 2;
                        }
                        case 3 -> {
                            newEnergy = initialEnergy * 2;
                        }
                        default -> newEnergy = initialEnergy;
                    }
                    enemy.getEnergy().setMaxValue(newEnergy);
                    enemy.getEnergy().setValue(newEnergy);
                }
            }
        }
    }

    /**
     * Reduces hunger and thirst; and health if hunger or thirst are below 0. Helth will be filled up if hunger and thirst
     * are above a certain limit. Values are depending on chosen difficulty level
     *
     * @author Yvonne Rahnfeld
     */
    private void reducePlayersEnergy() {
        int lowerLimit = 20 * difficulty;
        int hunger = player.getHunger();
        int thirst = player.getThirst();

        if (thirst == 0 || hunger == 0) {
            player.reduceHealth(5 * difficulty);
        }
        if (thirst >= lowerLimit && hunger >= lowerLimit) {
            int healthFillUp = (difficulty == 2) ? 10 : 15 / difficulty;     // get fillUpValue that is dividable by 5
            player.increaseHealth(healthFillUp);
        }
        player.reduceThirst(2 * difficulty);
        player.reduceHunger(3 * difficulty);
    }

}