package project.test.tool;

import project.Environment;
import project.app.exception.A2AIOException;
import project.app.exception.A2ABoundsException;
import project.app.object.cards.card.Card;
import project.app.tool.File;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for resources. File loading etc.
 * @author Måns Oskarsson mnsosk-7
 */
class FileTest {
    /**
     * Test for checking if first and last element in file read is equivalent to given text.
     *
     * @throws A2AIOException File can't load.
     */
    @Test
    void readApples() throws A2AIOException {
        String first = "[A Bad Haircut] - The perfect start to a bad hair day. ";
        String last = "[Zucchini] - A squashed vegetable. ";

        ArrayList<Card> cards = File.readApples(Environment.RED_APPLES);
        String testFirst = cards.get(0).toString();  // Takes content of first card.
        String testLast = cards.get(cards.size() - 1).toString();  // Takes content of last card.

        assertEquals(first, testFirst);
        assertEquals(last, testLast);

    }

    /**
     * Test for reading only one line and if the first expected element equals the one line.
     *
     * @throws A2AIOException File can't load.
     * @throws A2ABoundsException If index for specific lines is incorrect
     */
    @Test
    void readApplesMax() throws A2AIOException, A2ABoundsException {
        String first = "[A Bad Haircut] - The perfect start to a bad hair day. ";
        ArrayList<Card> cards = File.readApples(Environment.RED_APPLES,1);
        assertEquals(first, cards.get(0).toString());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Card card = cards.get(1); // Will not work since only 1 card is loaded into index 0.
        });
        String expectedMessage = "Index 1 out of bounds for length 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test for reading lines given index start and end value for lines in file.
     *
     * @throws A2AIOException File can't load.
     * @throws A2ABoundsException If index for specific lines is incorrect
     */
    @Test
    void readApplesBounds() throws A2AIOException, A2ABoundsException {
        String first = "[A Parade] - I love a parade. ";   // Index 41 (41 - 1 array index).
        String second = "[A Piano] - A piano is a percussion instrument, like a drum or cymbal. "; // Index 42 (42 - 1 array index).

        ArrayList<Card> cards = File.readApples(Environment.RED_APPLES,41,42);
        assertEquals(first, cards.get(0).toString());
        assertEquals(second, cards.get(1).toString());
    }
}