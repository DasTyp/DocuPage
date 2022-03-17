package zork;

import controller.GameController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Game game;
    GameController gameController;
    Item healingPotion = new Item("healing potion", "", "", 0, "");

    /**
     * Set up test data
     */
    public PlayerTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
    }

    @Test
    @DisplayName("Getters and Setters for the Stats should work properly")
    public void testStats(){
        //health getters and setters working properly
        game.player.increaseStat("health", 1000);
        assert game.player.getStat("health") == 100;
        game.player.reduceStat("health", 1000);
        assert game.player.getStat("health") == 0;
        game.player.increaseHealth(1000);
        assert game.player.getHealth() == 100;
        game.player.reduceHealth(1000);
        assert game.player.getHealth() == 0;

        //thirst getters and setters working properly
        game.player.increaseStat("thirst", 1000);
        assert game.player.getStat("thirst") == 100;
        game.player.reduceStat("thirst", 1000);
        assert game.player.getStat("thirst") == 0;
        game.player.increaseThirst(1000);
        assert game.player.getThirst() == 100;
        game.player.reduceThirst(1000);
        assert game.player.getThirst() == 0;

        //hunger getters and setters working properly
        game.player.increaseStat("hunger", 1000);
        assert game.player.getStat("hunger") == 100;
        game.player.reduceStat("hunger", 1000);
        assert game.player.getStat("hunger") == 0;
        game.player.increaseHunger(1000);
        assert game.player.getHunger() == 100;
        game.player.reduceHunger(1000);
        assert game.player.getHunger() == 0;
    }

    @Test
    @DisplayName("Test the healing potion")
    public void testHealingPotion(){
        game.player.reduceHealth(1000); //make health 0
        game.player.reduceThirst(1000); //make thirst 0
        game.player.inventory.addItem(healingPotion);
        gameController.processInput("drink healing potion");
        assert game.player.getThirst() == 10; //healing potion adds +10 thirst
        assert game.player.getHealth() == 25; //healing potion adds +25 health
        assertTrue(game.player.inventory.hasItem("empty jar")); //empty jar is returned
        assertFalse(game.player.inventory.hasItem(healingPotion)); //healing potion is not in inventory anymore
    }

    @Test
    @DisplayName("Test players name")
    public void testName(){
        String testName = "testName";
        assert game.player.getName().equals("no_name_set"); // default value from test data
        game.player.setName(testName);
        assert game.player.getName().equals(testName);
    }
}