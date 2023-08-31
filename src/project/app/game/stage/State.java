package project.app.game.stage;

import project.app.coms.Server;
import project.app.object.cards.Board;
import project.app.object.cards.DrawPile;
import project.app.object.cards.card.Card;
import project.app.player.Player;

import java.util.ArrayList;

/**
 * Packet visible for all stages. Accessibility for recreating stages.
 * Get/Set methods for testing, files outside the scope (stage packet).
 */
public class State {
    Server server;
    DrawPile greenApples;
    DrawPile redApples;
    ArrayList<Player> players;
    Card currentGreenApple;
    Board board;   // Card played with corresponding player id.

    public State(Server server){
        this.server = server;
        this.players = new ArrayList<>();
        this.board = new Board();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public DrawPile getGreenApples() {
        return greenApples;
    }

    public void setGreenApples(DrawPile greenApples) {
        this.greenApples = greenApples;
    }

    public DrawPile getRedApples() {
        return redApples;
    }

    public void setRedApples(DrawPile redApples) {
        this.redApples = redApples;
    }
}
