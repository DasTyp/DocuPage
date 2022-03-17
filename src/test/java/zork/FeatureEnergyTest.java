package zork;

import controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test the games and players energy processes and management
 * @author Yvonne Rahnfeld
 * @version 08.09.21
 */
public class FeatureEnergyTest {
    Game game;
    int initialHealth;
    int initialHunger;
    int initialThirst;
    Enemy bear;
    Enemy apeKing;
    GameController gameController;

    /**
     * Set up test data
     */
    public FeatureEnergyTest() {
        InputStream instream = FeatureEnergyTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Game.state = Constants.STATE_PLAY;
        game.player.inventory.addItem(new Item("machete", "", "", 0, ""));
        initialHealth = game.player.getHealth();
        initialHunger = game.player.getHunger();
        initialThirst = game.player.getThirst();
        gameController = new GameController(game);
        bear = game.getRoom("boss cave").getEnemy("bear");
        apeKing = game.getRoom("ape horde").getEnemy("ape king");
    }

    /**
     * Assert that through action hunger (that is consuming energy) is reduced by 3, thirst reduced by 2 and health
     * remains unchanged when hunger and thirst are not at 0 (for easy mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players energy levels are reduced, health unchanged, difficulty easy")
    public void testReducingPlayersEnergyWhilePlaying_DifficultyEasy_HealthUnchanged()
    {
        game.setDifficulty(1);
        gameController.processInput("use machete with thing");

        assertTrue(game.player.getHunger() > 0);
        assertTrue(game.player.getThirst() > 0);
        assertEquals(initialHealth, game.player.getHealth());
        assertEquals(initialHunger-3*game.getDifficulty(), game.player.getHunger());
        assertEquals(initialThirst-2*game.getDifficulty(), game.player.getThirst());
    }

    /**
     * Assert that through action (that is consuming energy) hunger is reduced by 6, thirst reduced by 4 and health
     * remains unchanged when hunger and thirst are not at 0 (for medium mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players energy levels are reduced, health unchanged, difficulty medium")
    public void testReducingPlayersEnergyWhilePlaying_DifficultyMedium_HealthUnchanged()
    {
        game.setDifficulty(2);
        gameController.processInput("use machete with thing");

        assertTrue(game.player.getHunger() > 0);
        assertTrue(game.player.getThirst() > 0);
        assertEquals(initialHealth, game.player.getHealth());
        assertEquals(initialHunger-3*game.getDifficulty(), game.player.getHunger());
        assertEquals(initialThirst-2*game.getDifficulty(), game.player.getThirst());
    }

    /**
     * Assert that through action (that is consuming energy) hunger is reduced by 9, thirst reduced by 6 and health
     * remains unchanged when hunger and thirst are not at 0 (for hard mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players energy levels are reduced, health unchanged, difficulty hard")
    public void testReducingPlayersEnergyWhilePlaying_DifficultyHard_HealthUnchanged()
    {
        game.setDifficulty(3);
        gameController.processInput("use machete with thing");

        assertTrue(game.player.getHunger() > 0);
        assertTrue(game.player.getThirst() > 0);
        assertEquals(initialHealth, game.player.getHealth());
        assertEquals(initialHunger-3*game.getDifficulty(), game.player.getHunger());
        assertEquals(initialThirst-2*game.getDifficulty(), game.player.getThirst());
    }

    /**
     * Assert that through action (that is consuming energy) health will be reduced by 5 when hunger and thirst are
     * at 0 (for easy mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be reduced if hunger+thirst are at 0, difficulty easy")
    public void testReducingPlayersEnergyWhilePlaying_DifficultyEasy_HealthReduced()
    {
        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        assertEquals(0, game.player.getHunger());
        assertEquals(0, game.player.getThirst());

        game.setDifficulty(1);
        gameController.processInput("use machete with thing");

        assertEquals(initialHealth-5*game.getDifficulty(), game.player.getHealth());
        assertEquals(0, game.player.getHunger());
        assertEquals(0, game.player.getThirst());
    }

    /**
     * Assert that through action (that is consuming energy) health will be reduced by 10 when hunger and thirst are
     * at 0 (for medium mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be reduced if hunger+thirst are at 0, difficulty medium")
    public void testReducingPlayersEnergyWhilePlaying_DifficultyMedium_HealthReduced()
    {
        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        assertEquals(0, game.player.getHunger());
        assertEquals(0, game.player.getThirst());

        game.setDifficulty(2);
        gameController.processInput("use machete with thing");

        assertEquals(initialHealth-5*game.getDifficulty(), game.player.getHealth());
        assertEquals(0, game.player.getHunger());
        assertEquals(0, game.player.getThirst());
    }

    /**
     * Assert that through action (that is consuming energy) health will be reduced by 15 when hunger and thirst are
     * at 0 (for hard mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be reduced if hunger+thirst are at 0, difficulty hard")
    public void testReducingPlayersEnergyWhilePlaying_DifficultyHard_HealthReduced()
    {
        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        assertEquals(0, game.player.getHunger());
        assertEquals(0, game.player.getThirst());

        game.setDifficulty(3);
        gameController.processInput("use machete with thing");

        assertEquals(initialHealth-5*game.getDifficulty(), game.player.getHealth());
        assertEquals(0, game.player.getHunger());
        assertEquals(0, game.player.getThirst());
    }

    /**
     * Assert that through action (that is not consuming energy) health will be increased by 15 when hunger and thirst
     * are above lower limit for fill-up (20 for easy mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be increased if hunger+thirst are above 20, difficulty easy")
    public void testIncreasingPlayersHealthWhilePlaying_HungerAndThirstAboveLowerLimit_DifficultyEasy()
    {
        initialHealth = 10;
        int lowerLimit = 20;
        game.player.setRoomName("middle beach");
        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        game.player.reduceHealth(100);
        game.player.increaseThirst(lowerLimit);
        game.player.increaseHunger(lowerLimit);
        game.player.increaseHealth(initialHealth);
        assertEquals(lowerLimit, game.player.getHunger());
        assertEquals(lowerLimit, game.player.getThirst());
        assertEquals(initialHealth, game.player.getHealth());

        game.setDifficulty(1);
        gameController.processInput("west");

        assertEquals(initialHealth+15, game.player.getHealth());
    }

    /**
     * Assert that through action (that is not consuming energy) health will be increased by 10 when hunger and thirst
     * are above lower limit for fill-up (40 for medium mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be increased if hunger+thirst are above 40, difficulty medium")
    public void testIncreasingPlayersHealthWhilePlaying_HungerAndThirstAboveLowerLimit_DifficultyMedium()
    {
        game.player.setRoomName("middle beach");
        initialHealth = 10;
        int lowerLimit = 40;
        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        game.player.reduceHealth(100);
        game.player.increaseThirst(lowerLimit);
        game.player.increaseHunger(lowerLimit);
        game.player.increaseHealth(initialHealth);
        assertEquals(lowerLimit, game.player.getHunger());
        assertEquals(lowerLimit, game.player.getThirst());
        assertEquals(initialHealth, game.player.getHealth());

        game.setDifficulty(2);
        gameController.processInput("west");

        assertEquals(initialHealth+10, game.player.getHealth());
    }

    /**
     * Assert that through action (that is not consuming energy) health will be increased by 5 when hunger and thirst
     * are above lower limit for fill-up (60 for hard mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be increased if hunger+thirst are above 60, difficulty hard")
    public void testIncreasingPlayersHealthWhilePlaying_HungerAndThirstAboveLowerLimit_DifficultyHard()
    {
        initialHealth = 10;
        int lowerLimit = 60;
        game.player.setRoomName("middle beach");
        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        game.player.reduceHealth(100);
        game.player.increaseThirst(lowerLimit);
        game.player.increaseHunger(lowerLimit);
        game.player.increaseHealth(initialHealth);
        assertEquals(lowerLimit, game.player.getHunger());
        assertEquals(lowerLimit, game.player.getThirst());
        assertEquals(initialHealth, game.player.getHealth());

        game.setDifficulty(3);
        gameController.processInput("west");

        assertEquals(initialHealth+5, game.player.getHealth());
    }

    /**
     * Assert that through action (that is not consuming energy) health will not be increased when hunger and
     * thirst are beneath lower limit for fill-up (20 for example in easy mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be increased if hunger+thirst are beneath 20, difficulty easy")
    public void testNoHealthIncreasingWhilePlaying_HungerAndThirstBeneathLowerLimit()
    {
        initialHealth = 10;
        int lowerLimit = 20;
        game.player.setRoomName("middle beach");

        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        game.player.reduceHealth(100);
        game.player.increaseThirst(lowerLimit-1);
        game.player.increaseHunger(lowerLimit-1);
        game.player.increaseHealth(initialHealth);
        assertEquals(lowerLimit-1, game.player.getHunger());
        assertEquals(lowerLimit-1, game.player.getThirst());
        assertEquals(initialHealth, game.player.getHealth());

        game.setDifficulty(1);
        gameController.processInput("west");

        assertEquals(initialHealth, game.player.getHealth());
    }

    /**
     * Assert that through action (that is not consuming energy) health will not be increased when hunger or
     * thirst are beneath lower limit for fill-up (20 for example in easy mode)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if players health level will be increased if hunger or thirst are beneath 20, difficulty easy")
    public void testNoHealthIncreasingWhilePlaying_HungerOrThirstBeneathLowerLimit()
    {
        initialHealth = 10;
        int lowerLimit = 20;
        game.player.setRoomName("middle beach");

        game.player.reduceThirst(100);
        game.player.reduceHunger(100);
        game.player.reduceHealth(100);
        game.player.increaseThirst(lowerLimit);
        game.player.increaseHunger(lowerLimit-1);
        game.player.increaseHealth(initialHealth);
        assertEquals(lowerLimit-1, game.player.getHunger());
        assertEquals(lowerLimit, game.player.getThirst());
        assertEquals(initialHealth, game.player.getHealth());

        game.setDifficulty(1);
        gameController.processInput("west");

        assertEquals(initialHealth, game.player.getHealth());
    }

    /**
     * Assert that enemies energy will not be increased when easy mode is chosen
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if enemies level will not be increased if difficulty easy is chosen")
    public void testNoEnergyIncreasingForEnemies_DifficultyEasy()
    {
        assertNotNull(bear);
        assertNotNull(apeKing);
        int initialBearEnergy = bear.getEnergy().getValue();
        int initialBearMaxEnergy = bear.getEnergy().getMaxValue();
        int initialApeKingEnergy = apeKing.getEnergy().getValue();
        int initialApeKingMaxEnergy = apeKing.getEnergy().getMaxValue();

        Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
        gameController.processInput("1");

        assertEquals(1, game.getDifficulty());
        assertEquals(initialBearEnergy, bear.getEnergy().getValue());
        assertEquals(initialBearMaxEnergy, bear.getEnergy().getMaxValue());
        assertEquals(initialApeKingEnergy, apeKing.getEnergy().getValue());
        assertEquals(initialApeKingMaxEnergy, apeKing.getEnergy().getMaxValue());
    }

    /**
     * Assert that enemies energy will be increased by the half when medium mode is chosen
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if enemies level will be increased by the half if difficulty medium is chosen")
    public void testEnergyIncreasingForEnemies_DifficultyMedium()
    {
        assertNotNull(bear);
        assertNotNull(apeKing);
        int initialBearEnergy = bear.getEnergy().getValue();
        int initialBearMaxEnergy = bear.getEnergy().getMaxValue();
        int initialApeKingEnergy = apeKing.getEnergy().getValue();
        int initialApeKingMaxEnergy = apeKing.getEnergy().getMaxValue();

        Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
        gameController.processInput("2");

        assertEquals(2, game.getDifficulty());
        assertEquals(initialBearEnergy+initialBearEnergy/2, bear.getEnergy().getValue());
        assertEquals(initialBearMaxEnergy+initialBearMaxEnergy/2, bear.getEnergy().getMaxValue());
        assertEquals(initialApeKingEnergy+initialApeKingEnergy/2, apeKing.getEnergy().getValue());
        assertEquals(initialApeKingMaxEnergy+initialApeKingMaxEnergy/2, apeKing.getEnergy().getMaxValue());
    }

    /**
     * Assert that enemies energy will doubled when hard mode is chosen
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test if enemies level will be doubled if difficulty hard is chosen")
    public void testEnergyIncreasingForEnemies_DifficultyHard()
    {
        assertNotNull(bear);
        assertNotNull(apeKing);
        int initialBearEnergy = bear.getEnergy().getValue();
        int initialBearMaxEnergy = bear.getEnergy().getMaxValue();
        int initialApeKingEnergy = apeKing.getEnergy().getValue();
        int initialApeKingMaxEnergy = apeKing.getEnergy().getMaxValue();

        Game.state = Constants.STATE_CHOOSE_DIFFICULTY;
        gameController.processInput("3");

        assertEquals(3, game.getDifficulty());
        assertEquals(initialBearEnergy*2, bear.getEnergy().getValue());
        assertEquals(initialBearMaxEnergy*2, bear.getEnergy().getMaxValue());
        assertEquals(initialApeKingEnergy*2, apeKing.getEnergy().getValue());
        assertEquals(initialApeKingMaxEnergy*2, apeKing.getEnergy().getMaxValue());
    }
}
