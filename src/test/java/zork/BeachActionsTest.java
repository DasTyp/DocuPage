package zork;

import controller.GameController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test beach-specific methods to enable ways or manipulate things in other ways
 * @author Yvonne Rahnfeld
 * @version 01.09.21
 */
public class BeachActionsTest {
    Game game;
    GameController gameController;
    Item fishingRod = new Item("fishing rod", "", "", 0, "");
    Item fish = new Item("fish", "", "", 0, "");
    Item meat = new Item("meat", "", "", 0, "");
    Item machete = new Item("machete", "", "", 0, "");
    Item spear = new Item("spear", "", "", 0, "");
    Item rucksack = new Item("rucksack", "", "", 5, "");
    Item note = new Item("note", "", "", 0, "");

    /**
     * Set up test data
     */
    public BeachActionsTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
    }

    /**
     * Assert item fish is added to room if fishing rod is in players inventory and nonitem fish swarm exists in
     * current room (island), fish swarm stays in room
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseFishingRodWithFishSwarm → item fish added to room(island), fish swarm still in room, fishing rod still in inventory")
    public void testUseFishingRodWithFishSwarm_Island_ReceiveFishKeepRodFishSwarmStillInRoom()
    {
        game.player.setRoomName("island");
        game.player.inventory.addItem(fishingRod);
        assertNull(game.getCurrentRoom().getItem("fish"));
        assertNotNull(game.getCurrentRoom().getNonItem("fish swarm"));

        gameController.processInput("use fishing rod with fish swarm");

        assertTrue(game.player.inventory.hasItem(fishingRod));
        assertNotNull(game.getCurrentRoom().getItem("fish"));
        assertNotNull(game.getCurrentRoom().getNonItem("fish swarm"));
    }

    /**
     * Assert item fish is added to room if fishing rod is in players inventory and nonitem fish swarm exists in
     * current room (middle beach), fish swarm stays in room
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseFishingRodWithFishSwarm → item fish added to room(middle beach), fish swarm still in room, fishing rod still in inventory")
    public void testUseFishingRodWithFishSwarm_MiddleBeach_ReceiveFishKeepRodFishSwarmStillInRoom()
    {
        game.player.setRoomName("middle beach");
        game.player.inventory.addItem(fishingRod);
        assertNull(game.getCurrentRoom().getItem("fish"));
        assertNotNull(game.getCurrentRoom().getNonItem("fish swarm"));

        gameController.processInput("use fishing rod with fish swarm");

        assertTrue(game.player.inventory.hasItem(fishingRod));
        assertNotNull(game.getCurrentRoom().getItem("fish"));
        assertNotNull(game.getCurrentRoom().getNonItem("fish swarm"));
    }

    /**
     * Assert shark leaves room (eastern beach) after feeding with fish (necessary item) and moves to island, way to
     * island is free, way from island is blocked, fish left inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("FeedShark → shark moves from eastern beach to island, blocked way gets freed")
    public void testFeedShark_FeedWithFishEasternBeach_SharkMovesToIslandWayIsFree()
    {
        game.player.setRoomName("eastern beach");
        game.player.inventory.addItem(fish);
        assertNotNull(game.getCurrentRoom().getAnimal("shark"));
        assertFalse(game.getCurrentRoom().getWay("way through shallow water to the tiny island").isFree());

        gameController.processInput("feed shark with fish");

        assertFalse(game.player.inventory.hasItem(fish));
        assertNull(game.getCurrentRoom().getItem("fish"));
        assertNull(game.getCurrentRoom().getAnimal("shark"));
        assertTrue(game.getCurrentRoom().getWay("way through shallow water to the tiny island").isFree());
        assertFalse(game.getRoom("island").getWay("way through shallow water to the main island").isFree());
        assertNotNull(game.getRoom("island").getAnimal("shark"));
    }

    /**
     * Assert shark leaves room (island) after feeding with fish (necessary item) and moves to eastern beach, way to
     * eastern beach is free, way from eastern beach is blocked, fish left inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("FeedShark → shark moves from island to eastern beach, blocked way gets freed")
    public void testFeedShark_FeedWithFishIsland_SharkMovesToEasternBeachWayIsFree()
    {
        game.player.setRoomName("island");
        game.player.inventory.addItem(fish);
        Animal shark = game.getRoom("eastern beach").getAnimal("shark");
        game.getCurrentRoom().getRoomAnimalList().add(shark);
        game.getRoom("eastern beach").getRoomAnimalList().remove(shark);
        assertNotNull(game.getCurrentRoom().getAnimal("shark"));

        gameController.processInput("feed shark with fish");

        assertFalse(game.player.inventory.hasItem(fish));
        assertNull(game.getCurrentRoom().getItem("fish"));
        assertNull(game.getCurrentRoom().getAnimal("shark"));
        assertTrue(game.getCurrentRoom().getWay("way through shallow water to the main island").isFree());
        assertFalse(game.getRoom("eastern beach").getWay("way through shallow water to the tiny island").isFree());
        assertNotNull(game.getRoom("eastern beach").getAnimal("shark"));
    }

    /**
     * Assert shark does not leave room (eastern beach) after feeding with meat (is not the sharks necessary item) and
     * does not move to island, way to island remains blocked, way from island remains free, meat left inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("FeedShark → shark dos not move to island, way remains blocked")
    public void testFeedShark_FeedWithMeatEasternBeach_SharkDoesNotMoveToIslandWayRemainsFree()
    {
        game.player.setRoomName("eastern beach");
        game.player.inventory.addItem(meat);
        assertNotNull(game.getCurrentRoom().getAnimal("shark"));

        gameController.processInput("feed shark with meat");

        assertFalse(game.player.inventory.hasItem(fish));
        assertNotNull(game.getCurrentRoom().getAnimal("shark"));
        assertFalse(game.getCurrentRoom().getWay("way through shallow water to the tiny island").isFree());
        assertTrue(game.getRoom("island").getWay("way through shallow water to the main island").isFree());
        assertNull(game.getRoom("island").getAnimal("shark"));
    }

    /**
     * Assert the thorny bush gets cut down, this direction of both ways (beach to jungle) is free, thorny bush moves to
     * the other jungle/beach connection and blocks the way the other direction, same happens with the crocodile
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseMacheteWithThornyBush → way is free, bush + crocodile block jungle/beach connection the other direction")
    public void testUseMacheteWithThornyBush_WesternBeach_WaysAreFreeOtherDirectionBlockedByBushAndCrocodile()
    {
        game.player.setRoomName("western beach");
        game.player.inventory.addItem(machete);
        assertNotNull(game.getCurrentRoom().getNonItem("thorny bush"));
        assertNotNull(game.getRoom("eastern beach").getAnimal("crocodile"));


        gameController.processInput("use machete with thorny bush");

        assertTrue(game.getCurrentRoom().getWay("sandy trail into the palm forest").isFree());
        assertTrue(game.getRoom("eastern beach").getWay("path into the jungle").isFree());
        assertFalse(game.getRoom("palm forest").getWay("sandy trail").isFree());
        assertFalse(game.getRoom("jungle glade").getWay("a path").isFree());
        assertNull(game.getCurrentRoom().getNonItem("thorny bush"));
        assertNull(game.getRoom("eastern beach").getAnimal("crocodile"));
        assertNotNull(game.getRoom("palm forest").getAnimal("crocodile"));
        assertNotNull(game.getRoom("jungle glade").getNonItem("thorny bush"));
    }

    /**
     * Assert the thorny bush gets cut down, this direction of both ways (jungle to beach) is free, thorny bush moves to
     * the other beach/jungle connection and blocks the way the other direction, same happens with the crocodile
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseMacheteWithThornyBush → way is free, bush + crocodile block beach/jungle connection the other direction")
    public void testUseMacheteWithThornyBush_JungleGlade_WaysAreFreeOtherDirectionBlockedByBushAndCrocodile()
    {
        game.player.setRoomName("jungle glade");
        game.player.inventory.addItem(machete);
        NonItem thornyBush = game.getRoom("western beach").getNonItem("thorny bush");
        game.getCurrentRoom().getRoomNonItemList().add(thornyBush);
        Animal crocodile = game.getRoom("eastern beach").getAnimal("crocodile");
        game.getRoom("palm forest").getRoomAnimalList().add(crocodile);
        assertNotNull(game.getCurrentRoom().getNonItem("thorny bush"));

        gameController.processInput("use machete with thorny bush");

        assertTrue(game.getCurrentRoom().getWay("a path").isFree());
        assertTrue(game.getRoom("palm forest").getWay("sandy trail").isFree());
        assertFalse(game.getRoom("eastern beach").getWay("path into the jungle").isFree());
        assertFalse(game.getRoom("western beach").getWay("sandy trail into the palm forest").isFree());
        assertNull(game.getCurrentRoom().getNonItem("thorny bush"));
        assertNull(game.getRoom("palm forest").getAnimal("crocodile"));
        assertNotNull(game.getRoom("eastern beach").getAnimal("crocodile"));
        assertNotNull(game.getRoom("western beach").getNonItem("thorny bush"));
    }

    /**
     * Assert that when crocodile gets fed with meat (its necessary item), this direction of both ways (beach to jungle)
     * is free, it moves to the other jungle/beach connection and blocks the way the other direction, same happens with
     * thorny bush
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("FeedCrocodile (with meat) → way is free, bush + crocodile block beach/jungle connection the other direction")
    public void testFeedCrocodile_WithMeatPalmForest_WaysAreFreeOtherDirectionBlockedByBushAndCrocodile()
    {
        game.player.setRoomName("eastern beach");
        game.player.inventory.addItem(meat);
        assertNotNull(game.getCurrentRoom().getAnimal("crocodile"));

        gameController.processInput("feed crocodile with meat");

        assertTrue(game.getCurrentRoom().getWay("path into the jungle").isFree());
        assertTrue(game.getRoom("western beach").getWay("sandy trail into the palm forest").isFree());
        assertFalse(game.getRoom("palm forest").getWay("sandy trail").isFree());
        assertFalse(game.getRoom("jungle glade").getWay("a path").isFree());
        assertNull(game.getCurrentRoom().getAnimal("crocodile"));
        assertNull(game.getRoom("western beach").getNonItem("thorny bush"));
        assertNotNull(game.getRoom("palm forest").getAnimal("crocodile"));
        assertNotNull(game.getRoom("jungle glade").getNonItem("thorny bush"));
    }

    /**
     * Assert that when crocodile gets fed with meat (its necessary item), this direction of both ways (jungle to beach)
     * is free, it moves to the other beach/jungle connection and blocks the way the other direction, same happens with
     * thorny bush
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("FeedCrocodile (with meat) → way is free, bush + crocodile block jungle/beach connection the other direction")
    public void testFeedCrocodile_WithMeatEasternBeach_WaysAreFreeOtherDirectionBlockedByBushAndCrocodile()
    {
        game.player.setRoomName("palm forest");
        game.player.inventory.addItem(meat);
        NonItem thornyBush = game.getRoom("western beach").getNonItem("thorny bush");
        game.getRoom("jungle glade").getRoomNonItemList().add(thornyBush);
        Animal crocodile = game.getRoom("eastern beach").getAnimal("crocodile");
        game.getCurrentRoom().getRoomAnimalList().add(crocodile);
        assertNotNull(game.getCurrentRoom().getAnimal("crocodile"));

        gameController.processInput("feed crocodile with meat");

        assertTrue(game.getCurrentRoom().getWay("sandy trail").isFree());
        assertTrue(game.getRoom("jungle glade").getWay("a path").isFree());
        assertFalse(game.getRoom("western beach").getWay("sandy trail into the palm forest").isFree());
        assertFalse(game.getRoom("eastern beach").getWay("path into the jungle").isFree());
        assertNull(game.getCurrentRoom().getAnimal("crocodile"));
        assertNull(game.getRoom("jungle glade").getNonItem("thorny bush"));
        assertNotNull(game.getRoom("eastern beach").getAnimal("crocodile"));
        assertNotNull(game.getRoom("western beach").getNonItem("thorny bush"));
    }

    /**
     * Assert that when crocodile gets fed with fish (not its necessary item), this direction of both ways (beach to jungle)
     * blocked, the other direction remains free, crocodile and thorny bush do not move
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("FeedCrocodile (with fish) → way remains blocked, bush and crocodile do not move")
    public void testFeedCrocodile_WithFishEasternBeach_WaysRemainBlockedBushAndCrocodileDoNotMove()
    {
        game.player.setRoomName("eastern beach");
        game.player.inventory.addItem(fish);
        assertNotNull(game.getCurrentRoom().getAnimal("crocodile"));

        gameController.processInput("feed crocodile with fish");

        assertFalse(game.getCurrentRoom().getWay("path into the jungle").isFree());
        assertFalse(game.getRoom("western beach").getWay("sandy trail into the palm forest").isFree());
        assertTrue(game.getRoom("palm forest").getWay("sandy trail").isFree());
        assertTrue(game.getRoom("jungle glade").getWay("a path").isFree());
        assertNotNull(game.getCurrentRoom().getAnimal("crocodile"));
        assertNotNull(game.getRoom("western beach").getNonItem("thorny bush"));
        assertNull(game.getRoom("palm forest").getAnimal("crocodile"));
        assertNull(game.getRoom("jungle glade").getNonItem("thorny bush"));
    }

    /**
     * Assert that when item spear is in players inventory and is thrown at the NonItem coconut palm the item coconut
     * will be added to current room only every third try
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseSpearWithCoconutPalm, case: spear in inventory → coconut is added to room every third try")
    public void testUseSpearWithCoconutPalm_PlayerHasSpear_CoconutAddedToRoomEveryThirdTry()
    {
        game.player.setRoomName("middle beach");
        game.player.inventory.addItem(spear);
        assertNotNull(game.getCurrentRoom().getNonItem("coconut palm"));
        assertNull(game.getCurrentRoom().getItem("coconut"));
        // first try
        gameController.processInput("use spear with coconut palm");
        assertNull(game.getCurrentRoom().getItem("coconut"));
        // second try
        gameController.processInput("use spear with coconut palm");
        assertNull(game.getCurrentRoom().getItem("coconut"));
        // third try
        gameController.processInput("use spear with coconut palm");
        assertNotNull(game.getCurrentRoom().getItem("coconut"));
        assertNotNull(game.getCurrentRoom().getNonItem("coconut palm"));
    }

    /**
     * Assert that when item spear is not in players inventory it can't be used with NonItem coconut palm, item coconut
     * will not be added to current room
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseSpearWithCoconutPalm, case: spear not in inventory → coconut is not added to room")
    public void testUseSpearWithCoconutPalm_PlayerHasNoSpear_CoconutNotAddedToRoom()
    {
        game.player.setRoomName("middle beach");
        assertNull(game.player.inventory.getItem("spear"));
        assertNotNull(game.getCurrentRoom().getNonItem("coconut palm"));
        assertNull(game.getCurrentRoom().getItem("coconut"));
        // first try
        gameController.processInput("use spear with coconut palm");
        assertNull(game.getCurrentRoom().getItem("coconut"));
        // second try
        gameController.processInput("use spear with coconut palm");
        assertNull(game.getCurrentRoom().getItem("coconut"));
        // third try
        gameController.processInput("use spear with coconut palm");
        assertNull(game.getCurrentRoom().getItem("coconut"));
        assertNotNull(game.getCurrentRoom().getNonItem("coconut palm"));
    }

    /**
     * Assert that when rucksack is in current room  and gets used, the inventories max size gets increased by the
     * rucksacks strength-attribute and rucksack leaves current room
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseRucksack, case: rucksack is in room → max inventory size gets increased, rucksack removed from room")
    public void testUseRucksack_RucksackInRoom_InventorySizeIncreasedRucksackRemovedFromRoom()
    {
        game.player.setRoomName("island");
        game.getCurrentRoom().getRoomItemList().add(rucksack);
        assertNotNull(game.getCurrentRoom().getItem(rucksack.getName()));
        assertEquals(10, game.player.inventory.getMaxSize());

        gameController.processInput("use rucksack");

        assertEquals(10 + rucksack.getStrength(), game.player.inventory.getMaxSize());
        assertNull(game.getCurrentRoom().getItem(rucksack.getName()));
    }

    /**
     * Assert that when rucksack is in players inventory and gets used, the inventories max size gets increased by the
     * rucksacks strength-attribute and rucksack gets removed from inventory
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseRucksack, case: rucksack is in inventory → max inventory size gets increased, rucksack removed from inventory")
    public void testUseRucksack_RucksackInInventory_InventorySizeIncreasedRucksackRemovedFromInventory()
    {
        game.player.inventory.addItem(rucksack);
        assertNotNull(game.player.inventory.getItem(rucksack.getName()));
        assertEquals(10, game.player.inventory.getMaxSize());

        gameController.processInput("use rucksack");

        assertEquals(10 + rucksack.getStrength(), game.player.inventory.getMaxSize());
        assertNull(game.player.inventory.getItem(rucksack.getName()));
    }

    /**
     * Assert that when player tries to use rucksack when it's not in inventory or current room, the inventories max size
     * does not get increased by the rucksacks strength-attribute
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseRucksack, case: rucksack is not in inventory or room → max inventory size does not get increased")
    public void testUseRucksack_RucksackNotInInventoryOrRoom_InventorySizeNotIncreased()
    {
        assertNull(game.player.inventory.getItem(rucksack.getName()));
        assertNull(game.getCurrentRoom().getItem(rucksack.getName()));
        assertEquals(10, game.player.inventory.getMaxSize());

        gameController.processInput("use rucksack");

        assertEquals(10, game.player.inventory.getMaxSize());
    }

    /**
     * Assert that when item note is in inventory and gets used with nonItem suitcase, the suitcase gets opened and
     * releases all of its items
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseNoteWithSuitcase, case: note is in inventory → all contained items get added to room")
    public void testUseNoteWithSuitcase_NoteInInventory_AllContainedItemsGetAddedToRoom()
    {
        game.player.setRoomName("island");
        game.player.inventory.addItem(note);
        assertNotNull(game.getCurrentRoom().getNonItem("suitcase"));
        assertNull(game.getCurrentRoom().getItem("rucksack"));
        assertNull(game.getCurrentRoom().getItem("sea shell"));
        assertNull(game.getCurrentRoom().getItem("key"));
        assertNull(game.getCurrentRoom().getItem("empty bottle"));

        gameController.processInput("use note with suitcase");

        assertNull(game.getCurrentRoom().getNonItem("suitcase"));
        assertNotNull(game.getCurrentRoom().getNonItem("open suitcase"));
        assertNotNull(game.getCurrentRoom().getItem("rucksack"));
        assertNotNull(game.getCurrentRoom().getItem("sea shell"));
        assertNotNull(game.getCurrentRoom().getItem("key"));
        assertNotNull(game.getCurrentRoom().getItem("empty bottle"));
    }

    /**
     * Assert that when item note is not in inventory and player tries to use it with nonItem suitcase, the suitcase
     * does not get opened and releases no items
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("UseNoteWithSuitcase, case: note is in not in inventory → suitcase remains closed, no items added to room")
    public void testUseNoteWithSuitcase_NoteNotInInventory_SuitcaseStillClosedNoItemsAddedToRoom()
    {
        game.player.setRoomName("island");
        assertNull(game.player.inventory.getItem(note.getName()));
        assertNotNull(game.getCurrentRoom().getNonItem("suitcase"));
        assertNull(game.getCurrentRoom().getItem("rucksack"));
        assertNull(game.getCurrentRoom().getItem("sea shell"));
        assertNull(game.getCurrentRoom().getItem("key"));
        assertNull(game.getCurrentRoom().getItem("empty bottle"));

        gameController.processInput("use note with suitcase");

        assertNotNull(game.getCurrentRoom().getNonItem("suitcase"));
        assertNull(game.getCurrentRoom().getNonItem("open suitcase"));
        assertNull(game.getCurrentRoom().getItem("rucksack"));
        assertNull(game.getCurrentRoom().getItem("sea shell"));
        assertNull(game.getCurrentRoom().getItem("key"));
        assertNull(game.getCurrentRoom().getItem("empty bottle"));
    }
}
