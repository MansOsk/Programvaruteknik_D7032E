package project.test.stage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.coms.Client;
import project.app.coms.Server;
import project.app.exception.A2AIOException;
import project.app.exception.A2ANetworkException;
import project.app.game.stage.*;
import project.app.object.cards.Hand;
import project.app.object.cards.card.Card;
import project.app.object.cards.card.GenericCard;
import project.app.player.Player;
import project.app.client.Terminal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReplenishStage04Test {
    int port;
    Server server;
    State state;
    InitStage01 stage01;
    PlayStage02 stage02;
    EvaluateStage03 stage03;
    ReplenishStage04 lastStage;
    Client client;
    Terminal terminal;

    @Test
    public void testStage04(){
        Runnable runnable = () -> {
            // INIT
            port = Environment.PORT + 4;

            try {
                server = new Server(port);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't host on port " + port, e);
            }

            state = new State(server);
            stage01 = new InitStage01(state);
            stage02 = new PlayStage02(state);
            stage03 = new EvaluateStage03(state);
            lastStage = new ReplenishStage04(state);
            stage01.nextStage(stage02);
            stage02.nextStage(stage03);
            stage03.nextStage(lastStage);
            //

            ExecutorService executor = userThread();

            try {
                server.waitForClients(1);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't wait for clients", e);
            }

            stage01.run();
            stage02.run();
            stage03.run();

            try {
                lastStage.publishLeaderboard(state.getPlayers());
            } catch (A2AIOException e) {
                throw new RuntimeException("Couldn't publish leaderboard", e);
            }

            Player player = terminal.getPlayer();

            Card card = state.getBoard().getCard(player.getId());

            Assertions.assertFalse(player.getHand().contains(card)); // Card should be discarded after played.

            lastStage.replenishCards();

            int indexOfNewJudge = -1;
            for (int i = 0; i < state.getPlayers().size(); i++) {
                if(state.getPlayers().get(i).isJudge()){
                    indexOfNewJudge = (i + 1) % (Environment.MAX_PLAYERS); // Expected, next player in list is judge.
                }
            }

            try {
                lastStage.newJudge(); // Select next judge (next player in list).
            } catch (A2AIOException e) {
                throw new RuntimeException(e);
            }

            Assertions.assertTrue(state.getPlayers().get(indexOfNewJudge).isJudge()); // Is expected judge actually judge?

            player = terminal.getPlayer();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Assertions.assertEquals(Environment.MAX_HAND, player.getHand().size()); // Check so correct cards is given.

            state.getPlayers().get(0).setScore(new Hand());

            for (int i = 0; i < Environment.WIN; i++) { // Add cards so player will win.
                state.getPlayers().get(0).addScore(new GenericCard("CONTENT DOENST MATTER"));
            }

            if(! lastStage.winCondition()){ // Stop game loop if win condition is fulfilled. Should be since added.
                Assertions.fail();
                System.exit(1);
            }

            executor.shutdown();

            try {
                server.close();
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't close connection", e);
            }
        };
        for (int i = 0; i <= Environment.TEST_PER_CASE; i++) {  //
            runnable.run();
        }
    }

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
