package project.app.player;

import java.util.Comparator;

/**
 * Helper function to player, comparing and sorting by score.
 */
public class SortByPoints implements Comparator<Player> {

    /**
     * Sorting by descending order, when publishing leaderboard.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return What order it should be sorted by.
     */
    public int compare(Player a, Player b) {

        return b.getScore().size() - a.getScore().size();
    }
}
