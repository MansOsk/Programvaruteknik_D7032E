package project.test.object.cards;

import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.object.cards.Board;
import project.app.object.cards.card.GenericCard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BoardTest {
    /**
     * Test if board with corresponding player id is shuffled, and id for card is correct after shuffle.
     * @author Måns Oskarsson mnsosk-7
     */
    @Test
    void shuffle() {
        for(int i = 0; i <= Environment.TEST_PER_CASE; i++){
            Board board = new Board();
            Board oldBoard = new Board();
            for (int id = 0; id < Environment.TEST_MAX_SIZE; id++) {
                board.put(id, new GenericCard(""+id));
                oldBoard.put(id, new GenericCard(""+id));
            }

            board.shuffle(); // In shuffle(), if equivalent with not shuffled board. Reshuffle.

            assertNotEquals(oldBoard.toString(), board.toString()); // Should not equal since shuffle is done.

            for (int j = 0; j < board.size(); j++) {
                assertEquals(board.getCard(j).toString(), Integer.toString(j)); // Check so id is correct after shuffle.
            }

        }
    }
}
