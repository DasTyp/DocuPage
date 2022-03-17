package zork;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Energy
 * @author Yvonne Rahnfeld
 * @version 25.08.21
 */
public class EnergyTest {

    Energy energy;
    int initialValue;
    Game game;

    public EnergyTest() {
        energy = new Energy(20, 100);
        initialValue = energy.getValue();

        InputStream instream = EnergyTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
    }

    /**
     * Assert value will be increased without exceeding a limit if after increasing (the new value would be in range)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("increaseValue, case: new value will be in range -> value will be increased without exceeding a limit")
    public void testIncreaseValue_AddValueInRange_ValueWillBeIncreasedWithoutExceedingLimit()
    {
        energy.increaseValue(energy.getMaxValue()-energy.getValue());
        Assertions.assertSame(energy.getMaxValue(), energy.getValue());
    }

    /**
     * Assert value will be increased without exceeding a limit if after increasing (the new value would be too high)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("increaseValue, case: new value will be too high -> value will be increased without exceeding a limit")
    public void testIncreaseValue_AddTooHighValue_ValueWillBeIncreasedWithoutExceedingLimit()
    {
        energy.increaseValue(energy.getMaxValue());
        Assertions.assertSame(energy.getMaxValue(), energy.getValue());
    }

    /**
     * Assert value will be increased without exceeding a limit if after increasing (the new value would be too low)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("increaseValue, case: new value would be too low -> value will be increased without exceeding a limit")
    public void testIncreaseValue_AddLowValue_ValueWillBeIncreasedWithoutExceedingLimit()
    {
        energy.increaseValue(-energy.getMaxValue());
        Assertions.assertSame(0, energy.getValue());
    }

    /**
     * Assert value will be reduced without exceeding a limit if after reducing (the new value would be in range)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("reduceValue, case: new value will be in range -> value will be reduced without exceeding a limit")
    public void testReduceValue_SubtractValueInRange_ValueWillBeReducedWithoutExceedingLimit()
    {
        energy.reduceValue(energy.getMaxValue()-energy.getValue()-1);
        Assertions.assertSame(0, energy.getValue());
    }

    /**
     * Assert value will be reduced without exceeding a limit if after reducing (the new value would be too low)
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("reduceValue, case: new value would be too low -> value will be reduced without exceeding a limit")
    public void testReduceValue_SubtractTooHighValue_ValueWillBeReducedWithoutExceedingLimit()
    {
        energy.reduceValue(energy.getMaxValue());
        Assertions.assertSame(0, energy.getValue());
    }

    /**
     * Assert energy will be increased by 20*1 if given values are equal for difficulty easy
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test fillUpIfStringsAreEqual, case: the given values are equal, difficulty easy is set")
    public void testFillUpIfStringsAreEqual_ValuesAreEqualDifficultyEasy_EnergyIncreased()
    {
        game.setDifficulty(1);
        String value1 = "1";
        String value2 = "1";

        energy.fillUpIfStringsAreEqual(value1, value2, game.getDifficulty());

        assertEquals(energy.getValue(), initialValue+20*game.getDifficulty());
    }

    /**
     * Assert energy will be increased by 20*2 if given values are equal for difficulty medium
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test fillUpIfStringsAreEqual, case: the given values are equal, difficulty medium is set")
    public void testFillUpIfStringsAreEqual_ValuesAreEqualDifficultyMedium_EnergyIncreased()
    {
        game.setDifficulty(2);
        String value1 = "1";
        String value2 = "1";

        energy.fillUpIfStringsAreEqual(value1, value2, game.getDifficulty());

        assertEquals(energy.getValue(), initialValue+20*game.getDifficulty());
    }

    /**
     * Assert energy will be increased by 20*3 if given values are equal for difficulty hard
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test fillUpIfStringsAreEqual, case: the given values are equal, difficulty hard is set")
    public void testFillUpIfStringsAreEqual_ValuesAreEqualDifficultyHard_EnergyIncreased()
    {
        game.setDifficulty(3);
        String value1 = "1";
        String value2 = "1";

        energy.fillUpIfStringsAreEqual(value1, value2, game.getDifficulty());

        assertEquals(energy.getValue(), initialValue+20*game.getDifficulty());
    }

    /**
     * Assert energy will not be increased if given values are not equal
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("Test fillUpIfStringsAreEqual, case: the given values are not equal")
    public void testFillUpIfStringsAreEqual_ValuesAreNotEqual_EnergyNotIncreased()
    {
        String value1 = "1";
        String value2 = "2";

        energy.fillUpIfStringsAreEqual(value1, value2, game.getDifficulty());

        assertEquals(energy.getValue(), initialValue);
    }
}
