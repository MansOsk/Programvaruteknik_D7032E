package project.test.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.object.cards.Hand;
import project.app.object.cards.card.GenericCard;
import project.app.player.Bot;
import project.app.player.Player;
import project.app.player.SortByPoints;

import java.util.ArrayList;

public class SortByPointsTest {

    /**
     * Test where a random amount of players is added and random number for point (<= TEST_MAX_SIZE in env.).
     * Sort and check if descending order for every player in list is sorted by each point in descending order.
     */
    @Test
    public void sortByPointsTest(){
        for (int i = 0; i <= Environment.TEST_PER_CASE; i++) {
            ArrayList<Player> players = new ArrayList<>();
            for (int j = 0; j <= Environment.TEST_MAX_SIZE; j++) {
                players.add(new Bot());
                players.get(j).setScore(createCards(Environment.TEST_MAX_SIZE));
            }
            players.sort(new SortByPoints());
            for (int j = 0; j < players.size(); j++) {
                for (int k = j; k < players.size(); k++) { // Check if sorted by amount of green apples.
                    Assertions.assertTrue(players.get(j).getScore().size() >= players.get(k).getScore().size());
                }
            }
        }
    }

    private Hand createCards(int i){
        Hand cards = new Hand();
        for (int j = 0; j < i; j++) {
            cards.add(new GenericCard("CONTENT DOESNT MATTER"));
        }
        return cards;
    }
}
