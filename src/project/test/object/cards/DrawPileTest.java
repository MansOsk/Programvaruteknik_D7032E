package project.test.object.cards;

import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.exception.A2AIOException;
import project.app.object.cards.DrawPile;
import project.app.object.cards.card.Card;
import project.app.tool.Randomizer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for drawing cards from a pile functionality in game.
 * @author Måns Oskarsson mnsosk-7
 */
class DrawPileTest {
    /**
     * Test if card gets removed from deck and returned correctly upon pick.
     *
     * @throws A2AIOException File can't load.
     */
    @Test
    void pickOne() throws A2AIOException {
        DrawPile drawPile = new DrawPile(Environment.RED_APPLES);
        Card card = drawPile.pickOne();
        assertFalse(drawPile.contains(card)); // If picked, card not in deck. Then project.test passed.
    }

    /**
     * Test if picking many cards, that they get added to discard pile.
     *
     * @throws A2AIOException File can't load.
     */
    @Test
    void pickMany() throws A2AIOException {
        DrawPile drawPile = new DrawPile(Environment.RED_APPLES);
        ArrayList<Card> cards = drawPile.pickMany(3);

        for (Card card : cards) {
            assertFalse(drawPile.contains(card)); // OK, if not contains.
        }
    }

    /**
     * Test for if draw-pile is restored correctly. Test 100 times. Random lines from red apple. And random amount.
     *
     * @throws A2AIOException File can't load.
     */
    @Test
    void restore() throws A2AIOException {
        for(int i = 1; i <= Environment.TEST_PER_CASE; i++){
            int randomFileLines = Randomizer.NextInt(1825) + 1; // Max lines for red apples. Random lines
            int randomPickLines = Randomizer.NextInt(randomFileLines); // Random amount being picked from drawpile.

            DrawPile drawPile = new DrawPile(Environment.RED_APPLES,randomFileLines);
            ArrayList<Card> cards = drawPile.pickMany(randomPickLines);
            for (Card card : cards){
                assertFalse(drawPile.contains(card));
            }
            drawPile.restore();
            for(Card card : cards){
                assertTrue(drawPile.contains(card));
            }
        }
    }

    /**
     * Test for if draw-pile is restored correctly. Pick whole deck so draw pile is empty.
     *
     * @throws A2AIOException File can't load.
     */
    @Test
    void restoreFullPick() throws A2AIOException {
        int randomFileLines = Randomizer.NextInt(1826); // Max lines for red apples.

        DrawPile drawPile = new DrawPile(Environment.RED_APPLES, randomFileLines);
        ArrayList<Card> cards = drawPile.pickMany(randomFileLines);
        for (Card card : cards) {
            assertFalse(drawPile.contains(card));
        }
        assertTrue(drawPile.isEmpty());    // drawPile is empty, expected -> return true since it should be empty.
        drawPile.restore();
        for (Card card : cards) {
            assertTrue(drawPile.contains(card));
        }
    }
}