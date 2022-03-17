package zork;

import controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class CaveActionsTest {
    Game game;
    GameController gameController;
    Item flute = new Item("flute", "", "", 0, "");
    Item pickaxe = new Item("pickaxe", "", "", 0, "");
    Item machete = new Item("machete","","",20,"");
    Scanner test;

    /**
     * Set up test data
     */
    public CaveActionsTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Game.state = Constants.STATE_PLAY;
        gameController = new GameController(game);
    }

    /**
     * Assert The snake goes away and the way north is set to free after the input was use flute with snake.
     */
    @Test
    @DisplayName("useFluteWithSnake -> Snake goes away & way north is free")
    public void testUseFluteWithSnake(){
        game.player.setRoomName("cave entrance");
        game.player.inventory.addItem(flute);
        assertFalse(game.getWayForDirection("north").isFree());
        assertNotNull(game.getCurrentRoom().getAnimal("snake"));
        gameController.processInput("use flute with snake");
        assertTrue(game.getWayForDirection("north").isFree());
        assertNull(game.getCurrentRoom().getAnimal("snake"));
        assertTrue(game.player.inventory.hasItem(flute));
    }

    /**
     * Assert The cairn goes away and there appears a stone and the way east is set to free after the input was use pickaxe with cairn.
     */
    @Test
    @DisplayName("UsePickaxeWithCairn -> Cairn goes away & way east is free")
    public void testUsePickaxeWithCairn(){
        game.player.setRoomName("cave fork");
        game.player.inventory.addItem(pickaxe);
        assertFalse(game.getWayForDirection("east").isFree());
        assertNotNull(game.getCurrentRoom().getNonItem("cairn"));
        gameController.processInput("use pickaxe with cairn");
        assertTrue(game.getWayForDirection("east").isFree());
        assertNull(game.getCurrentRoom().getNonItem("cairn"));
        assertTrue(game.player.inventory.hasItem(pickaxe));
        assertNotNull(game.getCurrentRoom().getItem("stone"));
    }

    /**
     * Assert if attack enemy gets 20 damage (Strenght of machete 20)
     */
    @Test
    @DisplayName("Attack -> Enemy gets 20 damage")
    public void testAttack(){
        game.player.setRoomName("boss cave");
        Enemy enemy = game.getCurrentRoom().getEnemy("bear");
        game.player.inventory.addItem(machete);
        assertNotNull(enemy);
        int initialEnergy = enemy.getEnergy().getValue();
        assertEquals(20, machete.getStrength());
        game.actionHandler.setCurrentWeapon(machete);
        game.actionHandler.setCurrentEnemy(enemy);
        game.actionHandler.attack(game);
        assertEquals(initialEnergy-20, enemy.getEnergy().getValue());
        game.player.setRoomName("boss cave");
    }

    /**
     * Assert if successful block no damage to player
     */
    @Test
    @DisplayName("Sucessful Block-> No damage to player")
    public void testSucessfulBlock() throws InterruptedException {
        game.player.setRoomName("boss cave");
        Enemy enemy = game.getCurrentRoom().getEnemy("bear");
        game.player.inventory.addItem(machete);
        Game.state = Constants.STATE_BLOCK;
        assertNotNull(enemy);
        game.actionHandler.setCurrentWeapon(machete);
        game.actionHandler.setCurrentEnemy(enemy);
        game.actionHandler.setTimeBeforeAction(System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(1);
        game.actionHandler.setTimeAfterAction(System.currentTimeMillis());
        int playerEnergy =game.player.getHealth();
        gameController.processInput("block");
        assertEquals(playerEnergy,game.player.getHealth());
    }

    /**
     * Assert not successful block 20 damage to player
     */
    @Test
    @DisplayName("Wrong input, not Sucessful Block-> 20 damage to player")
    public void testNotSucessfulBlockWrongInput() throws InterruptedException {
        game.player.setRoomName("boss cave");
        Enemy enemy = game.getCurrentRoom().getEnemy("bear");
        game.player.inventory.addItem(machete);
        Game.state = Constants.STATE_BLOCK;
        assertNotNull(enemy);
        game.actionHandler.setCurrentWeapon(machete);
        game.actionHandler.setCurrentEnemy(enemy);
        game.actionHandler.setTimeBeforeAction(System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(1);
        game.actionHandler.setTimeAfterAction(System.currentTimeMillis());
        int playerEnergy =game.player.getHealth();
        gameController.processInput("wrong input");
        assertEquals(playerEnergy-20,game.player.getHealth());
    }

    /**
     * Assert not successful block 20 damage to player
     */
    @Test
    @DisplayName("Took too long, not Sucessful Block-> 20 damage to player")
    public void testNotSucessfulBlockTookTooLong() throws InterruptedException {
        game.player.setRoomName("boss cave");
        Enemy enemy = game.getCurrentRoom().getEnemy("bear");
        game.player.inventory.addItem(machete);
        Game.state = Constants.STATE_BLOCK;
        assertNotNull(enemy);
        game.actionHandler.setCurrentWeapon(machete);
        game.actionHandler.setCurrentEnemy(enemy);
        game.actionHandler.setTimeBeforeAction(System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(3);
        game.actionHandler.setTimeAfterAction(System.currentTimeMillis());
        int playerEnergy =game.player.getHealth();
        gameController.processInput("block");
        assertEquals(playerEnergy-20,game.player.getHealth());
    }

    /**
     * if successful critical strike 40 damage to bear
     */
    @Test
    @DisplayName("sucessful critical strike -> 40 damage to bear")
    public void testsucessfulCriticalStrike() throws InterruptedException {
        game.player.setRoomName("boss cave");
        Enemy enemy = game.getCurrentRoom().getEnemy("bear");
        game.player.inventory.addItem(machete);
        Game.state = Constants.STATE_CRITICAL_STRIKE;
        assertNotNull(enemy);
        game.actionHandler.setCurrentWeapon(machete);
        game.actionHandler.setCurrentEnemy(enemy);
        game.actionHandler.setTimeBeforeAction(System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(1);
        game.actionHandler.setTimeAfterAction(System.currentTimeMillis());
        int initialEnergy = enemy.getEnergy().getValue();
        assertEquals(20, game.player.inventory.getItem("machete").getStrength());
        gameController.processInput("          ");
        assertEquals(initialEnergy-40,enemy.getEnergy().getValue());
    }

    /**
     * if not successful critical strike no damage to bear
     */
    @Test
    @DisplayName("Less spaces, not successful critical strike -> no damage to bear")
    public void testNotSuccessfulCriticalStrikeLessSpaces() throws InterruptedException {
        game.player.setRoomName("boss cave");
        Enemy enemy = game.getCurrentRoom().getEnemy("bear");
        game.player.inventory.addItem(machete);
        Game.state = Constants.STATE_CRITICAL_STRIKE;
        assertNotNull(enemy);
        game.actionHandler.setCurrentWeapon(machete);
        game.actionHandler.setCurrentEnemy(enemy);
        game.actionHandler.setTimeBeforeAction(System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(1);
        game.actionHandler.setTimeAfterAction(System.currentTimeMillis());
        int initialEnergy = enemy.getEnergy().getValue();
        assertEquals(20, game.player.inventory.getItem("machete").getStrength());
        gameController.processInput("      ");
        assertEquals(initialEnergy,enemy.getEnergy().getValue());
    }

    /**
     * if not successful critical strike no damage to bear
     */
    @Test
    @DisplayName("Took too long, not successful critical strike -> no damage to bear")
    public void testNotSuccessfulCriticalStrikeTookTooLong() throws InterruptedException {
        game.player.setRoomName("boss cave");
        Enemy enemy = game.getCurrentRoom().getEnemy("bear");
        game.player.inventory.addItem(machete);
        Game.state = Constants.STATE_CRITICAL_STRIKE;
        assertNotNull(enemy);
        game.actionHandler.setCurrentWeapon(machete);
        game.actionHandler.setCurrentEnemy(enemy);
        game.actionHandler.setTimeBeforeAction(System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(5);
        game.actionHandler.setTimeAfterAction(System.currentTimeMillis());
        int initialEnergy = enemy.getEnergy().getValue();
        assertEquals(20, game.player.inventory.getItem("machete").getStrength());
        gameController.processInput("          ");
        assertEquals(initialEnergy,enemy.getEnergy().getValue());
    }

    /**
     * Test if strength-reduction works
     */
    @Test
    @DisplayName("after fight -> strength of weapon -1")
    public void testReductionOfStrength(){
        game.player.setRoomName("boss cave");
        game.player.inventory.addItem(machete);
        assertNotNull(game.getCurrentRoom().getEnemy("bear"));
        int initialStrength = game.player.inventory.getItem("machete").getStrength();
        game.player.inventory.getItem("machete").reduceStrength(1);
        assertEquals(initialStrength-1,game.player.inventory.getItem("machete").getStrength());
    }
}