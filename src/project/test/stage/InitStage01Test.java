package project.test.stage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.coms.Client;
import project.app.coms.Server;
import project.app.exception.A2AIOException;
import project.app.exception.A2ANetworkException;
import project.app.game.stage.InitStage01;
import project.app.game.stage.State;
import project.app.object.cards.Hand;
import project.app.player.Bot;
import project.app.client.Terminal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * One server on main thread, one client on separate thread.
 * Go through stage01 and check if expected values are correct.
 */
public class InitStage01Test {
    int port;
    Server server;
    State state;
    InitStage01 stage01;
    Client client;
    Terminal terminal;

    @Test
    public void testStage01(){
        Runnable runnable = () -> {
            port = Environment.PORT + 1;
            try {
                server = new Server(port);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't host on port " + port,e);
            }
            state = new State(server);
            stage01 = new InitStage01(state);

            ExecutorService executor = userThread();

            try {
                server.waitForClients(1);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't wait for clients", e);
            }

            stage01.initPlayers();
            try {
                stage01.initDeck();
            } catch (A2AIOException e) {
                throw new RuntimeException(e);
            }
            stage01.initHands(Environment.MAX_HAND);
            stage01.initJudge(0);

            executor.shutdown();

            // Check if data provided for client is correct.
            int expectedId = 1;

            assertEquals(expectedId, terminal.getPlayer().getId()); // If id sent from server is assigned to client.
            assertEquals(Environment.MAX_PLAYERS, state.getPlayers().size()); // If correct number of bots are added.
            assertTrue(state.getPlayers().get(1) instanceof Bot); // Check if bot is added after only client (on thread).

            // Test if correct at client side.
            String expectedFirst = "[A Bad Haircut] - The perfect start to a bad hair day. ";
            String expectedLast = "[A Car Crash] - \"Hey, it was an accident!\" ";

            Hand hand = terminal.getPlayer().getHand();

            assertEquals(expectedFirst, hand.get(0).toString()); // Unshuffled, check if first card drawn from server equals.
            assertEquals(expectedLast, hand.get(hand.size() - 1).toString()); // Unshuffled, check last.

            assertTrue(terminal.getPlayer().isJudge()); // First player judge (not random), check if player is updated.
            try {
                server.close();
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't close connection", e);
            }
        };

        for (int i = 0; i <= Environment.TEST_PER_CASE; i++) {
            runnable.run();
        }
    }

    /**
     * Terminal or HumanPlayer thread.
     *
     * @return Executor to run on separate thread.
     */
    private ExecutorService userThread(){
        ExecutorService executor = Executors.newSingleThreadExecutor(); // Wait for connection on other thread.
        executor.execute(() -> Assertions.assertDoesNotThrow(() -> {
            client = Client.connect("localhost", port);
            terminal = new Terminal(client);

            terminal.run(true, true);
        }));
        return executor;
    }

}
