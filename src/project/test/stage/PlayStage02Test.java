package project.test.stage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.coms.Client;
import project.app.coms.Server;
import project.app.exception.A2ANetworkException;
import project.app.game.stage.InitStage01;
import project.app.game.stage.PlayStage02;
import project.app.game.stage.State;
import project.app.object.cards.Board;
import project.app.object.cards.card.Card;
import project.app.player.Player;
import project.app.client.Terminal;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PlayStage02Test {
    int port;
    Server server;
    State state;
    InitStage01 stage01;
    PlayStage02 stage02;
    Client client;
    Terminal terminal;

    @Test
    public void testStage02(){
        Runnable runnable = () -> {
            // INIT
            port = Environment.PORT + 2;
            try {
                server = new Server(port);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't host on port " + port,e);
            }
            state = new State(server);
            stage01 = new InitStage01(state);
            stage02 = new PlayStage02(state);
            stage01.nextStage(stage02);
            //

            ExecutorService executor = userThread();

            try {
                server.waitForClients(1);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't wait for clients", e);
            }
            stage01.run();

            Player player = terminal.getPlayer();
            int index = Integer.parseInt(Environment.TEST_RESPONSE);
            Card expectedCard = player.getHand().get(index);    // Get expected card before play, it gets discarded.

            // Testing for stage02 begins
            stage02.playGreenApple(); // If any try/catch triggers test fails. Init for play stage.
            stage02.playRedApple(); // This will be tested, check if board gets filled with correct answers.

            Board board = state.getBoard();

            int expectedId = player.getId();
            boolean ifExist = ifIdCardCorrect(board, expectedId, expectedCard);

            if(!terminal.getPlayer().isJudge()){
                Assertions.assertTrue(ifExist);  // Check if picked card from player exist on game board.
            }

            stage02.shuffleBoard();

            executor.shutdown();
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

    private ExecutorService userThread(){
        ExecutorService executor = Executors.newSingleThreadExecutor(); // Wait for connection on other thread.
        executor.execute(() -> Assertions.assertDoesNotThrow(() -> {
            client = Client.connect("localhost", port);
            terminal = new Terminal(client);

            terminal.run(true, true);
        }));
        return executor;
    }

    private boolean ifIdCardCorrect(Board board, int id, Card card){
        for (int i = 0; i < board.size(); i++) { // IF id for player and card played by that player is correct. OK!
            boolean cardEq = Objects.equals(board.get(i).toString(), card.toString());

            if((id == board.getId(i)) && cardEq){ // check index pair.
                if(Objects.equals(board.getCard(id).toString(), card.toString())){ // check if getting card by id works.
                    return true;
                }
            }
        }
        return false;
    }

}
