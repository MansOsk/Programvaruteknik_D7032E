package project.test.stage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.coms.Client;
import project.app.coms.Server;
import project.app.exception.A2ANetworkException;
import project.app.game.Game;
import project.app.client.Terminal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameLoopTest {

    int port;
    Server server;
    Client client;
    Terminal terminal;

    @Test
    public void loopTest(){
        for (int i = 0; i <= Environment.TEST_PER_CASE; i++) {  //
            // INIT
            port = Environment.PORT + 5;

            try {
                server = new Server(port);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't host on port " + port, e);
            }

            Game game = new Game(server);

            ExecutorService executor = userThread();

            try {
                server.waitForClients(1);
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't wait for clients", e);
            }

            game.run();

            executor.shutdown();

            try {
                server.close();
            } catch (A2ANetworkException e) {
                throw new RuntimeException("Couldn't close connection", e);
            }
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
