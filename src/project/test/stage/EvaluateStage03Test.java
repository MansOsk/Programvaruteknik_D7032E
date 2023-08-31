package project.test.stage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.coms.Client;
import project.app.coms.Server;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.exception.A2ANetworkException;
import project.app.game.stage.EvaluateStage03;
import project.app.game.stage.InitStage01;
import project.app.game.stage.PlayStage02;
import project.app.game.stage.State;
import project.app.player.Player;
import project.app.client.Terminal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvaluateStage03Test {
    int port;
    Server server;
    State state;
    InitStage01 stage01;
    PlayStage02 stage02;
    EvaluateStage03 stage03;
    Client client;
    Terminal terminal;

    /**
     *
     */
    @Test
    public void testStage03(){
        Runnable runnable = () -> {
            // INIT
            port = Environment.PORT + 3;
            try {
                server = new Server(port);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't host on port " + port,e);
            }
            state = new State(server);
            stage01 = new InitStage01(state);
            stage02 = new PlayStage02(state);
            stage03 = new EvaluateStage03(state);
            stage01.nextStage(stage02);
            stage02.nextStage(stage03);
            //

            ExecutorService executor = userThread();

            try {
                server.waitForClients(1);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't wait for clients", e);
            }
            stage01.run();
            stage02.run();

            stage03.showResult(); // Only msg, send result to HumanPlayer. Already tested with net func.

            try {
                stage03.evaluate(); // Judge evaluates answers.
            } catch (A2AMessageException | A2AIOException e) {
                throw new RuntimeException("Couldn't evaluate", e);
            }
            executor.shutdown();

            Assertions.assertTrue(checkForPoint()); // Any points given?

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

    /**
     * Check if any increase of point after evaluate has occurred.
     *
     * @return If any player got points.
     */
    private boolean checkForPoint(){
        for(Player player : state.getPlayers()){
            if(player.getScore().size() > 0){
                return true;
            }
        }
        for (Player player : state.getPlayers()){
            System.out.println(player.getScore().size());
        }
        return false;
    }
}
