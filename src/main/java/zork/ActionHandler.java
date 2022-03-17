package zork;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle more complex commands like use item with item or feed animal
 * @author Patrick Mayer
 * @version 25.08.2021
 */
public class ActionHandler {

    private int spearTries = 0;
    private long timeBeforeAction;
    private long timeAfterAction;
    private Enemy currentEnemy;
    private Item currentWeapon;

    public void setTimeBeforeAction(long time) {
        timeBeforeAction = time;
    }

    public void setTimeAfterAction(long time) {
        timeAfterAction = time;
    }

    public void setCurrentEnemy(Enemy enemy) {
        currentEnemy = enemy;
    }

    public void setCurrentWeapon(Item weapon) {
        currentWeapon = weapon;
    }

    /**
     * Method to implement specific constellations of items and Non-Item (and extended classes) to do specific things
     * @param item the item which should be used
     * @param nonItem the Non-Item which should be used
     * @param game to change data of player and rooms
     * @return String
     * @author Patrick Mayer
     */
    public <T extends NonItem> String useWith(Item item, T nonItem, Game game){
        String output = "";
        if (Constants.WEAPONS_LIST.contains(item.getName()) && nonItem.getName().equals("grindstone")){
            output = useWeaponWithGrindstone(item);
        } else if (item.getName().equals("machete")) {
            output = useMachete(item, nonItem, game);
        } else if (item.getName().equals("spear")) {
            output = useSpear(item, nonItem, game);
        } else if (item.getName().equals("fishing rod") && nonItem.getName().equals("fish swarm")) {
            output = useFishingRodWithFishSwarm(item, nonItem, game.getCurrentRoom());
        } else if (item.getName().equals("note") && nonItem.getName().equals("suitcase")) {
            output = useNoteWithSuitcase(item, nonItem, game);
        } else if(item.getName().equals("flute") && nonItem.getName().equals("snake")){
            output = useFluteWithSnake(nonItem,game);
        } else if(item.getName().equals("pickaxe") && nonItem.getName().equals("cairn")){
            output = usePickaxeWithCairn(item,nonItem,game);
        } else if (item.getName().equals("healing potion") && nonItem.getName().equals("survivor")){
            output = healSurvivor(item, game);
        } else if(nonItem.getName().equals("cauldron")){
            output = useCauldron(nonItem, item, game);
        } else if(item.getName().equals("banana")){
            output = useBananaWithApes(nonItem, game);
        } else if(item.getName().equals("key") && nonItem.getName().equals("door")){
            output = useKeyWithDoor(item,nonItem,game);
        } else if(item.getName().equals("sea shell") && nonItem.getName().equals("ritual")){
            output = useSeaShellWithRitual(item, nonItem, game);
        } else if(item.getName().equals("rope") && nonItem.getName().equals("banana tree")){
            output = useRopeWithTree(item, nonItem, game);
        } else {
            output = "This is not possible!";
        }
        return output;
    }

    /**
     * Method to implement specific constellations of items and item of the inventory to do specific things
     * @param item the item which should be used
     * @param secondItem the second item which should be used
     * @param game to change data of player and rooms
     * @return String
     * @author Patrick Mayer
     */
    public String useWith(Item item, Item secondItem, Game game){
        String output = "";
        if (item.getName().equals("")&&secondItem.getName().equals("")) {
            // placeholder for code
        } else {
            output = "This is not possible!";
        }
        return output;
    }

    /**
     * Method feed to feed animals with necessary item and then call method action after fed
     * @param animal the animal, which should be fed
     * @param food Name of the item, which is the food
     * @param game to change data of player and rooms
     * @return String
     * @author Patrick Mayer
     */
    public String feedWith(Animal animal, String food, Game game){
        String output = "";
        String animalFeedability = isFeedableAnimal(animal,food);
        if(animalFeedability.equals("1")){
            game.player.inventory.removeItem(food);
            animal.setToNotHungry();
            output = "You fed the " + animal.getName() + " with " + food + ".";
            output += actionAfterFed(animal,food,game);
        } else {
            output = animalFeedability;
        }
        return output;
    }

    /**
     * Method to eat food as a player
     * @param foodName name of the item, which is the food
     * @param player the current player to get the inventory and other information
     * @return String
     * @author Patrick Mayer
     */
    public String eat(String foodName, Player player, boolean isFood){
        String output = "";
        if(isFood) {
            player.increaseHunger(20);
            player.inventory.removeItem(foodName);
            output = "You ate the " + foodName;
        } else {
            if (foodName.equals("water") && player.inventory.hasItem("water bottle")) {
                player.increaseThirst(50);
                player.inventory.removeItem("water bottle");
                player.inventory.addItem(new Item("empty bottle",
                        "can be used to carry water with you, try to \"take water\"!", "removable", 0, ""));
                output = "You drank the " + foodName + ", the bottle is empty now.";
            } else {
                if (foodName.equals("healing potion")) {
                    player.increaseThirst(10);
                    player.increaseHealth(25);
                    //give empty jar back
                    Item emptyJar = new Item("empty jar", "an empty jar made of scratched glass", "", 0, "");
                    player.inventory.addItem(emptyJar);
                } else {
                    player.increaseThirst(30);
                }
                player.inventory.removeItem(foodName);
                output = "You have drank the " + foodName;
            }
        }
        return output;
    }

    /**
     * When item rucksack is used the players inventory size will be increased by the rucksacks strength, rucksack gets
     * removed from current room
     * @author Yvonne Rahnfeld
     * @param rucksack Given item the player wants to use
     * @param game Is given to manipulate game data
     * @return String
     */
    public String useRucksack(Item rucksack, Game game) {
        game.getCurrentRoom().getRoomItemList().remove(rucksack);
        game.player.inventory.increaseMaxInventorySize(rucksack.getStrength());
        return "Your inventory size was increased by " + rucksack.getStrength() +
                "! You can now carry more things with you.";
    }

    /**
     * Adds all contained items of the NonItem (or extended classes) to the room and removes NonItem from room if state is removable
     * @author Yvonne Rahnfeld
     * @param item The item that is necessary to manipulate the NonItem
     * @param nonItem The nonItem that will be manipulated
     * @param room The room where this is happening
     */
    private <T extends NonItem> void releaseContainedItems(String item, T nonItem, Room room) {
        if (nonItem.getNecessaryItem().equals(item)) {
            for (Item containedItem : nonItem.getContainedItemsList()) {
                containedItem.setWhere("is lying on the ground");
                room.getRoomItemList().add(containedItem);
            }
        }
        if (nonItem.getState().equals("removable")) {
            removeNonItemFromRoom(nonItem, room);
        }
    }

    /**
     * Remove NonItem from its list depending on which class it belongs to
     * @author Yvonne Rahnfeld
     * @param nonItem The NonItem that should be removed from room
     * @param room The room where the nonItem should be removed
     */
    private <T extends NonItem> void removeNonItemFromRoom(T nonItem, Room room) {
        if (nonItem.getClass() == NonItem.class) {
            room.getRoomNonItemList().remove(nonItem);
        }
        if (nonItem.getClass() == Animal.class) {
            room.getRoomAnimalList().remove(nonItem);
        }
        if (nonItem.getClass() == Enemy.class) {
            room.getRoomEnemyList().remove(nonItem);
        }
    }

    /**
     * help-Method to find out if the player is able to feed an animal with the food
     * @param animal the animal, which should be fed
     * @param food the food, which the player wants to give
     * @return String
     * @author Patrick Mayer
     */
    private String isFeedableAnimal(Animal animal, String food){
        String output = "1";
        if (!animal.getNecessaryItem().contains(food)){
            output = "This is the wrong food for the animal.";
        }
        else if (animal.getNecessaryItem().isEmpty()){
            output = "You can not feed this animal";
        }
        else if(!animal.isHungry()){
            output = "Animal is not hungry";
        }
        return output;
    }

    /**
     * Method with the actions, which will happen after an animal is fed
     * @param animal the animal, which is fed
     * @param food the food the player gave the animal
     * @param game to change data of player and rooms
     * @return String
     * @author Patrick Mayer
     */
    private String actionAfterFed(Animal animal, String food, Game game){
        String output = "";
        if (animal.getName().equals("shark")) {
            output = feedShark(animal, food, game);
        } else if (animal.getName().equals("crocodile")) {
            output = feedCrocodile(animal, food, game);
        }
        return output;
    }

    /**
     * When shark gets fed, it moves from eastern beach to island (or island to eastern beach) and the way between
     * eastern beach and island is free until player moves to other room
     * @author Yvonne Rahnfeld
     * @param shark The animal, which is fed
     * @param fish The food the player gave the animal
     * @param game To change data of player and rooms
     * @return String
     */
    private String feedShark(Animal shark, String fish, Game game) {
        Room currentRoom = game.getCurrentRoom();
        releaseContainedItems(fish, shark, currentRoom);
        shark.setToHungry();
        if (currentRoom.getName().equals("eastern beach")) {
            Room otherRoom = game.getRoom("island");
            otherRoom.getWay("way through shallow water to the main island").setToBlocked();
            otherRoom.getRoomAnimalList().add(shark);
            currentRoom.getWay("way through shallow water to the tiny island").setToFree();
        } else if (currentRoom.getName().equals("island")){
            Room otherRoom = game.getRoom("eastern beach");
            otherRoom.getWay("way through shallow water to the tiny island").setToBlocked();
            otherRoom.getRoomAnimalList().add(shark);
            currentRoom.getWay("way through shallow water to the main island").setToFree();
        }
        String destination = currentRoom.getName().equals("eastern beach") ? "tiny island" : "main island";
        return "It seems to be replete and swims away. The way through shallow water to the " + destination +
                " is now clear!";
    }

    /**
     * When crocodile gets fed, this direction of both ways (beach/jungle or jungle/beach) is free, it moves to the other
     * beach/jungle connection and blocks the way the other direction, same happens with thorny bush
     * @author Yvonne Rahnfeld
     * @param crocodile The animal, which is fed
     * @param meat The food the player gave the animal
     * @param game To change data of player and rooms
     * @return String
     */
    private String feedCrocodile(Animal crocodile, String meat, Game game) {
        Room currentRoom = game.getCurrentRoom();
        releaseContainedItems(meat, crocodile, currentRoom);
        crocodile.setToHungry();
        Room jungleGlade = game.getRoom("jungle glade");
        Room westernBeach = game.getRoom("western beach");
        // change position of crocodile and thorny bush
        if (currentRoom.getName().equals("eastern beach")) {
            Room palmForest = game.getRoom("palm forest");
            currentRoom.getWay("path into the jungle").setToFree();
            westernBeach.getWay("sandy trail into the palm forest").setToFree();
            jungleGlade.getWay("a path").setToBlocked();
            palmForest.getWay("sandy trail").setToBlocked();
            palmForest.getRoomAnimalList().add(crocodile);
            NonItem thornyBush = westernBeach.getNonItem("thorny bush");
            westernBeach.getRoomNonItemList().remove(thornyBush);
            jungleGlade.getRoomNonItemList().add(thornyBush);
        }
        if (currentRoom.getName().equals("palm forest")) {
            Room easternBeach = game.getRoom("eastern beach");
            currentRoom.getWay("sandy trail").setToFree();
            jungleGlade.getWay("a path").setToFree();
            easternBeach.getWay("path into the jungle").setToBlocked();
            westernBeach.getWay("sandy trail into the palm forest").setToBlocked();
            easternBeach.getRoomAnimalList().add(crocodile);
            NonItem thornyBush = jungleGlade.getNonItem("thorny bush");
            jungleGlade.getRoomNonItemList().remove(thornyBush);
            westernBeach.getRoomNonItemList().add(thornyBush);
        }
        String way = currentRoom.getName().equals("eastern beach") ? "path into the jungle glade" :
                "sandy trail to the western beach";
        return "It seems to be replete and walks away into the jungle.\nThe " + way + " is now clear!";
    }

    /**
     * Uses Machete with given NonItem
     * @author Yvonne Rahnfeld
     * @param machete Given item
     * @param nonItem The NonItem with which the machete is to be used
     * @param game Is given to manipulate game data
     * @return String
     */
    private <T extends NonItem> String useMachete(Item machete, T nonItem, Game game) {
        String output = "You can't use the machete with this!";
        if (nonItem.getName().equals("thorny bush")) {
            output = useMacheteWithThornyBush(machete, nonItem, game);
        }
        return output;
    }

    /**
     * Uses spear with given NonItem
     * @author Yvonne Rahnfeld
     * @param spear Given item
     * @param nonItem The NonItem with which the spear is to be used
     * @param game Is given to manipulate game data
     * @return String
     */
    private <T extends NonItem> String useSpear(Item spear, T nonItem, Game game) {
        String output = "You can't use the spear with this!";
        if (nonItem.getName().equals("coconut palm")) {
            output = useSpearWithCoconutPalm(spear, nonItem, game.getCurrentRoom());
        }
        return output;
    }

    /**
     * Throw spear at coconut palm to get item coconut, will only work every third time the player tries this
     * @author Yvonne Rahnfeld
     * @param spear The item the player is using
     * @param coconutPalm The nonItem the spear is used with
     * @param room Is given to manipulate the current room
     * @return String
     */
    private String useSpearWithCoconutPalm(Item spear, NonItem coconutPalm, Room room) {
        String output = "You throw the spear at the coconut, but unfortunately just miss it. Keep trying!";
        spearTries++;
        if (0 == spearTries%3) {
            spearTries = 0;
            releaseContainedItems(spear.getName(), coconutPalm, room);
            output = "You hit it! The coconut falls down into the sand.";
        }
        return output;
    }

    /**
     * When the thorny bush gets cut down, this direction of both ways (beach/jungle or jungle/beach) is free, it moves
     * to the other beach/jungle connection and blocks the way the other direction, same happens with the crocodile
     * @author Yvonne Rahnfeld
     * @param machete The item the player is using
     * @param thornyBush The nonItem the machete is used with
     * @param game Is given to manipulate game data
     * @return String
     */
    private String useMacheteWithThornyBush(Item machete, NonItem thornyBush, Game game) {
        Room currentRoom = game.getCurrentRoom();
        releaseContainedItems(machete.getName(), thornyBush, currentRoom);
        Room easternBeach = game.getRoom("eastern beach");
        Room palmForest = game.getRoom("palm forest");
        // change position of crocodile and thorny bush
        if (currentRoom.getName().equals("western beach")) {
            Room jungleGlade = game.getRoom("jungle glade");
            currentRoom.getWay("sandy trail into the palm forest").setToFree();
            easternBeach.getWay("path into the jungle").setToFree();
            jungleGlade.getWay("a path").setToBlocked();
            palmForest.getWay("sandy trail").setToBlocked();
            jungleGlade.getRoomNonItemList().add(thornyBush);
            Animal crocodile = easternBeach.getAnimal("crocodile");
            easternBeach.getRoomAnimalList().remove(crocodile);
            palmForest.getRoomAnimalList().add(crocodile);
        }
        if (currentRoom.getName().equals("jungle glade")) {
            Room westernBeach = game.getRoom("western beach");
            currentRoom.getWay("a path").setToFree();
            palmForest.getWay("sandy trail").setToFree();
            easternBeach.getWay("path into the jungle").setToBlocked();
            westernBeach.getWay("sandy trail into the palm forest").setToBlocked();
            westernBeach.getRoomNonItemList().add(thornyBush);
            Animal crocodile = palmForest.getAnimal("crocodile");
            palmForest.getRoomAnimalList().remove(crocodile);
            easternBeach.getRoomAnimalList().add(crocodile);
        }
        String way = currentRoom.getName().equals("western beach") ? "sandy trail into the palm forest" :
                "path to the eastern beach";
        return "You have cut down the thorny bush, the " + way +  " is now clear!";
    }

    /**
     * The item fish will appear in room and can be taken
     * @author Yvonne Rahnfeld
     * @param fishingRod The item the player is using
     * @param fishSwarm The nonItem the item is used with
     * @param room Is given to manipulate game data
     * @return String
     */
    private String useFishingRodWithFishSwarm(Item fishingRod, NonItem fishSwarm, Room room) {
        String output = "The fish seem to be not hungry, try again later...";
        if (room.getName().equals("island") || 0 == room.getVisited()%2) {
            releaseContainedItems(fishingRod.getName(), fishSwarm, room);
            room.getItem("fish").setWhere("is hanging on the hook of your fishing rod");
            output = "You have caught a fish! You can take it off the hook now.";
        }
        return output;
    }

    /**
     * When item note gets used with nonItem suitcase, the suitcase gets opened and releases its items, also the rucksack
     * to increase the size of players inventory
     * @author Yvonne Rahnfeld
     * @param note The item the player is using
     * @param closedSuitcase The nonItem the item is used with
     * @param game Is given to manipulate game data
     * @return String
     */
    private String useNoteWithSuitcase(Item note, NonItem closedSuitcase, Game game) {
        // release items without changing the where-attribute
        for (Item item : closedSuitcase.getContainedItemsList()) {
            game.getCurrentRoom().getRoomItemList().add(item);
        }
        // remove old closed suitcase and add a new open suitcase
        removeNonItemFromRoom(closedSuitcase, game.getCurrentRoom());
        NonItem openSuitcase = new NonItem("open suitcase","", "fixed",
                "is lying in the sand and some things are inside","");
        game.getCurrentRoom().getRoomNonItemList().add(openSuitcase);
        game.player.inventory.removeItem(note);
        return "The code on the note was correct, you opened the suitcase! There's also a small rucksack inside. " +
                "Use it to expand the space in your inventory!";
    }

    /**
     * removes snake and  set way to north free
     * @param snake the snake which will be removed
     * @param game the game to manipulate the ways
     * @return String
     * @author Patrick Mayer
     */
    private <T extends NonItem> String useFluteWithSnake(T snake, Game game) {
        removeNonItemFromRoom(snake,game.getCurrentRoom());
        game.getWayForDirection("north").setToFree();
        return "The Snake is now distracted.";
    }

    /**
     * removes cairn and releases stone in the room + set way to west free
     * @param item the pickaxe, which has to be used
     * @param cairn the NonItem cairn
     * @param game the game to manipulate the ways
     * @return String
     * @author Patrick Mayer
     */
    private <T extends NonItem> String usePickaxeWithCairn(Item item,T cairn, Game game){
        releaseContainedItems(item.getName(),cairn,game.getCurrentRoom());
        game.getWayForDirection("east").setToFree();
        return "You destroyed the cairn. The way is free!";
    }

    /**
     * heal the survivor to get a note with the code for the bag
     * @author Jonas Proell
     * @param item the healing potion
     * @param game the game to manipulate the inventory
     * @return String
     */
    private String healSurvivor(Item item, Game game){
        //remove healing potion
        game.player.inventory.removeItem(item);
        //add letter with code
        Item letter = new Item("note", "a handwritten note with 4 digits on it: 4242", "", 0, "");
        Item emptyJar = new Item("empty jar", "an empty jar made of scratched glass", "", 0, "");
        game.player.inventory.addItem(letter);
        game.player.inventory.addItem(emptyJar);
        return "The survivor drinks the potion with a distorted face.\nHe tries to speak but this does not seem to " +
                "work properly.\nThen he rummages in his pocket and pulls out a small note. He gives it to you. " +
                "After that he immediately falls asleep again. ";
    }

    /**
     * use a given item to interact with the cauldron
     * @author Jonas Proell
     * @param cauldron the cauldron-nonItem
     * @param item the item you want to use with the cauldron
     * @param game the game to manipulate the players inventory
     * @return String
     */
    private String useCauldron(NonItem cauldron, Item item, Game game){
        String output = "";
        //one of the ingredients is used with the cauldron
        if(item.getName().equals("berries") || item.getName().equals("mushroom") || item.getName().equals("coconut")){
            //check if item is already in the cauldron
            List<Item> list = cauldron.getContainedItemsList();
            if(list.contains(item)){
                output = "This item is already in the cauldron!";
            } else {
                //add item to the cauldron
                cauldron.getContainedItemsList().add(item);
                //remove item from inventory
                game.player.inventory.removeItem(item);
                //inform the player
                output = "You dropped the " + item.getName() + " into the cauldron!";
            }
        }
        //the empty jar is used with the cauldron
        else if(item.getName().equals("empty jar")){
            //check if all ingredients are in the cauldron
            List<Item> list = cauldron.getContainedItemsList();
            boolean hasBerries = false, hasMushroom = false, hasCoconut = false;
            for (Item i : list){
                if(i.getName().equals("berries"))  hasBerries  = true;
                if(i.getName().equals("mushroom")) hasMushroom = true;
                if(i.getName().equals("coconut"))  hasCoconut  = true;
            }
            //an ingredient is missing
            if(!hasBerries || !hasCoconut || !hasMushroom){
                output = "There is still something missing...";
            } else {
                //clear the cauldron
                cauldron.getContainedItemsList().clear();
                //add healing potion to inventory
                Item potion = new Item("healing potion", "a glass jar filled with a red substance", "removable", 0, "");
                game.player.inventory.addItem(potion);
                //remove empty jar
                game.player.inventory.removeItem(item);
                //inform the player
                output = "You successfully brewed a healing potion!";
            }
        }
        //a random item is used with the cauldron
        else{
            output = "You shouldn't put that into the cauldron!";
        }
        return output;
    }

    /**
     * handler for tracking footprints & spawning animals
     * @param tracks track nonitem for the player to follow
     * @param game the game to manipulate & check free ways
     * @return String
     * @author Christian Litke
     */
    public String trackPawprints(NonItem tracks,Game game){
        String output = "";
        //required for processing
        List <Item> containedItemsTemp = tracks.getContainedItemsList();
        Item containedItem = containedItemsTemp.get(0);
        if(!containedItem.getState().equals("trackable")){
            return "You can't track " + tracks.getName() + ".";
        }
        int contained = Integer.parseInt(containedItem.getName());
        //spawn animal if tracked for long enough
        if(contained > 2){
            //spawn bunny
            Enemy bunny = new Enemy("injured bunny","a bunny with a broken leg. It can't flee you anymore. An easy harvest and perfect bait!","removable","struggling on the ground","",1, 1);
            List<Enemy> enemyList = game.getCurrentRoom().getRoomEnemyList();
            Item meat = new Item("meat","Fresh red meat. Still pretty bloody, like you just got it off the butcher","removable",0,"that was part of a bunny's corpse");
            List<Item> bunnyLoot = new ArrayList<Item>();
            bunnyLoot.add(meat);
            bunny.setContainedItemsList(bunnyLoot);
            bunny.setCommand("fight with");
            bunny.setNecessaryItem("machete");
            enemyList.add(bunny);
            game.getCurrentRoom().setRoomEnemyList(enemyList);
            //delete Track object from current room
            List <NonItem> roomNonItems = game.getCurrentRoom().getRoomNonItemList();
            roomNonItems.remove(tracks);
            game.getCurrentRoom().setRoomNonItemList(roomNonItems);
            //set Track object in a new room
            Room targetRoom = game.getRoom("sparse jungle");
            roomNonItems = targetRoom.getRoomNonItemList();
            tracks.setName("0");
            roomNonItems.add(tracks);
            targetRoom.setRoomNonItemList(roomNonItems);
            output = "You've caught up with the bunny! It seems to have worn itself out and can't flee any further.";
        }
        //when not tracked enough, spawn new tracks
        else{
            //tracks appear in a random direction that is traversable
            Way trackway = null;
            Random direction = new Random();
            //change contained item name to use as data storage
            containedItem.setName(Integer.toString(contained+1));
            containedItemsTemp.remove(0);
            containedItemsTemp.add(containedItem);
            tracks.setContainedItemsList(containedItemsTemp);
            while (trackway==null){
                switch (direction.nextInt(4)){
                    case 0: trackway = game.getWayForDirection("north");
                        break;
                    case 1: trackway = game.getWayForDirection("south");
                        break;
                    case 2: trackway = game.getWayForDirection("west");
                        break;
                    case 3: trackway = game.getWayForDirection("east");
                        break;
                }
                if(trackway!=null){
                    if(!trackway.isFree()){
                        trackway = null;
                    }
                }
            }
            //delete Track object from current room
            List <NonItem> roomNonItems = game.getCurrentRoom().getRoomNonItemList();
            roomNonItems.remove(tracks);
            game.getCurrentRoom().setRoomNonItemList(roomNonItems);
            //set Track object in a new room
            Room targetRoom = game.getRoom(trackway.getTo());
            roomNonItems = targetRoom.getRoomNonItemList();
            if(roomNonItems==null){
                roomNonItems = new ArrayList<>();
            }
            roomNonItems.add(tracks);
            targetRoom.setRoomNonItemList(roomNonItems);
            output = "The tracks lead "+ trackway.getDirection() + ". You might catch up to the bunny if you follow them.";
        }
        return output;
    }
    /**
     * distract apes and  set way to north free
     * @param apes the apes will be removed
     * @param game the game to manipulate the ways
     * @return String
     * @author Marius Richter
     */
    private <T extends NonItem> String useBananaWithApes(T apes, Game game) {
        removeNonItemFromRoom(apes,game.getCurrentRoom());
        game.getWayForDirection("north").setToFree();
        return "The Apes are now distracted.";
    }
    /**
     * Opens door, way to north is free now
     * @param item the key, which has to be used
     * @param door the NonItem door
     * @param game the game to manipulate the ways
     * @return String
     * @author Marius Richter
     */
    private <T extends NonItem> String useKeyWithDoor(Item item,T door, Game game){
        releaseContainedItems(item.getName(),door,game.getCurrentRoom());
        game.getWayForDirection("north").setToFree();
        return "You opened the door. The way is free!";
    }

    /**
     * removes seashell and adds pickaxe to the players inventory
     * @param seaShell the seaShell item
     * @param ritual the ritual nonItem
     * @param game the game to manipulate the inventory
     * @return String
     * @author Jonas Pröll
     */
    private <T extends NonItem> String useSeaShellWithRitual(Item seaShell, T ritual, Game game) {
        //remove seashell from inventory
        game.player.inventory.removeItem(seaShell);
        //add all contained items
        List<Item> unlockedItems = ritual.getContainedItemsList();
        for (Item i : unlockedItems){
            game.player.inventory.addItem(i);
        }
        //update room description
        game.getCurrentRoom().setDescription("a dark hut, lit only by the flickering of a greenish campfire. Bones " +
                "hang from the ceiling.\nA resident sings and dances around the fireplace. Perhaps it is a ritual?");
        //print story
        return "You place the sea shell at the empty space.\nThe villager sings even louder and starts to dance " +
                "around the fire.\nHe swings a pickaxe around until he gets to you and thankfully puts it in your hand.";
    }

    /**
     * Method to fight enemies
     * @param weapon the required weapon
     * @param enemy the enemy you want to fight
     * @param game the game to manipulate things
     * @return String
     * @author Patrick Mayer
     */
    public String fightEnemy(Item weapon, Enemy enemy, Game game){
        String output = "";
        currentEnemy = enemy;
        currentWeapon = weapon;
        if(weapon.getStrength()>0){
            if ((enemy.getName().equals("bear") || enemy.getName().equals("ape king")) && weapon.getName().equals("machete")) {
                try {
                    output = fightEnemyWithMachete(game);
                } catch (InterruptedException interruptedException) {
                    output = "There was an error with the Sleep function";
                }
            } else if (enemy.getName().equals("injured bunny") && weapon.getName().equals("machete")) {
                output = killBunny(enemy, game);
            } else {
                output = "You can not fight this enemy.";
            }
            weapon.reduceStrength(1);
        } else{
            output = "Your weapon is blunt. You need to sharpen it.";
        }
        return output;
    }

    /**
     * Checks energy of player and enemy and returns state as output
     * @param game the game to manipulate things
     * @author Yvonne Rahnfeld
     * @return String  output
     */
    private String checkEnergyAfterAttacking(Game game) {
        String output = "";
        String enemyName = currentEnemy.getName();
        if (game.player.getHealth() <= 0) {
            output = "\nThe " + enemyName + " killed you with that attack!";
        } else if (game.getCurrentRoom().getEnemy(enemyName).getEnergy().getValue() <= 0) {
            output = "\nThe " + enemyName + " is dead. You defeated him. ";
            if (enemyName.equals("bear")) {
                output += "\nIn the fight the two-way radio broke. But maybe you can use it anyways.";
            } else if (enemyName.equals("ape king")) {
                output += "The bridge is now free.";
            }
            releaseContainedItems(currentWeapon.getName(), currentEnemy, game.getCurrentRoom());
        }
        return output;
    }

    /**
     * Method generates random number between 1 and 3 and selects if the player has to block, do a critical strike or just attack.
     * @param game the game to manipulate player
     * @throws InterruptedException of the sleep function
     * @return String
     * @author Patrick Mayer
     */
    private String fightEnemyWithMachete(Game game) throws InterruptedException {
        String output = "";
        String enemyName = currentEnemy.getName();
        int random = (int)(Math.random()*3+1);
        if (random == 1) {
            output = "You have to block the attack of the " + enemyName +
                    ". \nType block and press enter as fast as you can!";
            Game.state = Constants.STATE_BLOCK;
        } else if (random == 2) {
            output = "You have the chance to land a critical strike on the " + enemyName + " and do double damage!" +
                    " \nPress the space key as fast as possible in 4 Seconds.\nYou need to press Enter in the 4 Seconds too.";
            Game.state = Constants.STATE_CRITICAL_STRIKE;
        } else if (random ==3) {
            output = attack(game);
        }
        return output;
    }

    /**
     * Method to hit a critical strike (You have to type space as fast as possible)
     * Damage 2*strength of weapon
     * @param input the user input
     * @param game the game to manipulate
     * @return String
     * @author Patrick Mayer
     */
    public String criticalStrike(String input, Game game) {
        timeAfterAction = System.currentTimeMillis();
        String output = "";
        if (timeAfterAction - timeBeforeAction <= 4000 && input.isBlank() && input.length() >= 10) {
            currentEnemy.getEnergy().reduceValue(currentWeapon.getStrength()*2);
            output = "You have struck a critical hit on the " + currentEnemy.getName() + "!\n" + "The " +
                    currentEnemy.getName() + " now has " + currentEnemy.getEnergy().getValue() + " health left. ";
        } else {
            output = "You failed the critical strike! ";
        }
        return output + checkEnergyAfterAttacking(game);
    }

    /**
     * Method to block an attack (Player has to type block as fast as possible)
     * Damage to player 20
     * @param input the user input
     * @param game the game to manipulate
     * @return String
     * @author Patrick Mayer
     */
    public String block(String input, Game game) {
        timeAfterAction = System.currentTimeMillis();
        String output = "";
        if (timeAfterAction - timeBeforeAction <= 2000 && input.equals("block")) {
            output = "You have successfully blocked the attack of the "+ currentEnemy.getName() + "!";
        } else {
            game.player.reduceHealth(20);
            output = "You failed the block and took damage!";
        }
        if (game.player.getHealth()<=0) {
            output = "\nThe " + currentEnemy.getName() + " killed you with that attack!";
        }
        return output;
    }

    /**
     * Method to attack the enemy
     * Damage strength of item
     * @param game the game to manipulate
     * @return String
     * @author Patrick Mayer
     */
    public String attack(Game game){
        currentEnemy.getEnergy().reduceValue(currentWeapon.getStrength());
        return checkEnergyAfterAttacking(game) + "You hit the " + currentEnemy.getName() + " with the machete! He now has " + currentEnemy.getEnergy().getValue() +
                " health left.";
    }

    /**
     * attach rope to banana tree and make bananas reachable
     * @param item the item rope
     * @param tree the bananat ree to manipulate
     * @param game the game to get the current room to add bananas
     * @return String
     * @author Christian Litke
     */
    private String useRopeWithTree(Item item, NonItem tree, Game game) {
        Room currentRoom = game.getCurrentRoom();
        List<Item> roomItems = currentRoom.getRoomItemList();
        if (roomItems == null) {
            roomItems = new ArrayList<Item>();
        }
        Item banana = tree.getContainedItemsList().get(0);
        roomItems.add(banana);
        game.player.inventory.removeItem(item);
        tree.setWhere("has a bountiful harvest of bananas growing under it's crown high above.\nThe low hanging branches" +
                " have been worn away by animals or the weather.\nA rope hangs from it's upper branches, easily climbable");
        return "With a skilled throw you've now got yourself something to climb. Those bananas should be harvestable from now on.";
    }

    /**
     * Method to kill and remove bunny and get its meat
     * @param bunny The Enemy that will be fought
     * @param game To manipulate game data
     * @return String
     * @author Christian Litke
     */
    private String killBunny(Enemy bunny, Game game) {
        String output = "There's no bunny here for you to kill.";
        Room currentRoom = game.getCurrentRoom();
        List<Enemy> roomEnemies = currentRoom.getRoomEnemyList();
        if (roomEnemies == null) {
            roomEnemies = new ArrayList<Enemy>();
        } else {
            Item meat = bunny.getContainedItemsList().get(0);
            List<Item> roomItems = currentRoom.getRoomItemList();
            if (roomItems == null) {
                roomItems = new ArrayList<Item>();
            }
            roomItems.add(meat);
            roomEnemies.remove(bunny);
            output = "You've slain the bunny! Not that it could resist your superior might. Go ahead and harvest its meat.";
        }
        return output;
    }

    /**
     * Method to sharpen weapons with the grindstone in the traders hut
     * @param item the weapon, which has to be sharpened
     * @return String
     * @author Patrick Mayer
     */
    private String useWeaponWithGrindstone(Item item) {
        String output = "The weapon is already sharp.";
        if(item.getStrength() < item.getMaxStrength()){
            item.setStrength(item.getMaxStrength());
            output = "You have sharpened your "+item.getName();
        }
        return output;
    }
}
