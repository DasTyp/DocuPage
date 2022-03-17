package zork;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for class Animal
 * @author Yvonne Rahnfeld
 * @version 25.08.21
 */
public class AnimalTest {

    Animal hungryAnimal = new Animal("", "", "", "", "", true);
    Animal notHungryAnimal = new Animal("", "", "", "", "", false);

    /**
     * Assert right state (hungry or not) will be returned
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("isHungry, will return if animal is hungry or not")
    public void testIsHungry_WillReturnIfAnimalIsHungryOrNot()
    {
        assertFalse(notHungryAnimal.isHungry());
        assertTrue(hungryAnimal.isHungry());
    }

    /**
     * Assert animal will get hungry, no matter if it was hungry beforeRR
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("SetToHungry, will make the animal hungry, hungry animals stay hungry")
    public void testSetToHungry_WillMakeTheAnimalHungry()
    {
        notHungryAnimal.setToHungry();
        assertTrue(notHungryAnimal.isHungry());
        hungryAnimal.setToHungry();
        assertTrue(hungryAnimal.isHungry());
    }

    /**
     * Assert animal will get not hungry, no matter if it was hungry before
     * @author Yvonne Rahnfeld
     */
    @Test
    @DisplayName("SetToNotHungry, will make the animal not hungry, not hungry animals stay not hungry")
    public void testSetToNotHungry_WillMakeTheAnimalNotHungry()
    {
        notHungryAnimal.setToNotHungry();
        assertFalse(notHungryAnimal.isHungry());
        hungryAnimal.setToNotHungry();
        assertFalse(hungryAnimal.isHungry());
    }
}
