package project.test.object.cards;

import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.exception.A2AIOException;
import project.app.object.cards.DrawPile;
import project.app.object.cards.Hand;
import project.app.object.cards.card.Card;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for playing hand functionality in game.
 */
class HandTest {
    /**
     * Test when playing card if it removes from player hand.
     *
     * @throws A2AIOException File can't load.
     */
    @Test
    void play() throws A2AIOException {
        DrawPile drawPile = new DrawPile(Environment.RED_APPLES);
        Hand hand = new Hand(drawPile.pickMany(3));

        for (int i = 0; i < hand.size(); i++){
            assertTrue(hand.contains(hand.get(i)));
            Card card = hand.play(i);
            assertFalse(hand.contains(card));
        }
    }
}