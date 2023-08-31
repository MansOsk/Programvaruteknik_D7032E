package project.app.game.stage;

import project.Environment;
import project.app.coms.Client;
import project.app.coms.msg.Instruction;
import project.app.coms.msg.Message;
import project.app.coms.msg.RequestMessage;
import project.app.coms.msg.StaticMessage;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.object.cards.DrawPile;
import project.app.player.Bot;
import project.app.player.HumanPlayer;
import project.app.player.Player;
import project.app.tool.Randomizer;

import java.util.List;

/**
 * Sets up the game, reads deck, player init.
 */
public class InitStage01 extends Stage {
    public InitStage01(State currentState, Stage nextStage) {
        super(currentState, nextStage);
    }

    public InitStage01(State currentState) {
        super(currentState);
    }

    @Override
    public void run() {
        try {
            initDeck();
        } catch (A2AIOException e) {
            throw new RuntimeException(e);
        }
        shuffleDeck();

        initPlayers();
        initHands(Environment.MAX_HAND);
        initJudge(Randomizer.NextInt(gameState.players.size()));

        finished();
    }

    public void initDeck() throws A2AIOException {
        try {
            gameState.redApples = new DrawPile(Environment.RED_APPLES);
            gameState.greenApples = new DrawPile(Environment.GREEN_APPLES);
        } catch (A2AIOException e) {
            throw new A2AIOException("Couldn't load red apple or green apple deck",e);
        }
    }

    private void shuffleDeck(){
        gameState.redApples.shuffle();
        gameState.greenApples.shuffle();
    }

    /**
     * Initialize all players list, containing bots and human-players.
     * Ping all clients and initialize, fill remaining with bot depending on max players in environment.
     */
    public void initPlayers() {
        List<Client> clients = gameState.server.getClients();

        // Add and ping clients.
        for (int i = 0; i < clients.size(); i++) {
            RequestMessage<Integer> msg = new RequestMessage<>((i+1), Instruction.INIT); // i+1 so id starts with 1.
            try {
                clients.get(i).writeObject(msg);
                Message<HumanPlayer> msg2 = (Message<HumanPlayer>) clients.get(i).readObject();
                HumanPlayer player = msg2.getContent();
                player.setClient(clients.get(i));   // Update new player (from client) with correct output & input socket.
                gameState.players.add(player);
            } catch (A2AIOException | A2AMessageException e) {
                e.printStackTrace();
            }
        }

        // Fill with bots to match max players.
        if (gameState.players.size() <= Environment.MAX_PLAYERS) {
            for (int i = gameState.players.size(); i < Environment.MAX_PLAYERS; i++) {
                gameState.players.add(new Bot(false, i + 1));
            }
        }
    }

    /**
     *  Initialize all players hands by picking cards from draw pile, and adding to internal discard pile.
     *
     * @param max Max amount of cards being drawn.
     */
    public void initHands(int max) {
        super.drawCards(max);
    }

    /**
     * Initializes judge.
     *
     * @param i Initializes judge on index i.
     */
    public void initJudge(int i) {
        Player player = gameState.players.get(i);

        if (player instanceof HumanPlayer) {
            try {
                gameState.players.get(i).setJudge(true);
                gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(true, Instruction.JUDGE));
            } catch (A2AIOException | A2AMessageException e) {
                e.printStackTrace();
            }
        } else if (player instanceof Bot) {
            gameState.players.get(i).setJudge(true);
        }
    }
}
