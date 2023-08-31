package project;

import project.app.coms.Client;
import project.app.coms.Server;
import project.app.exception.A2AIOException;
import project.app.exception.A2ANetworkException;
import project.app.game.Game;
import project.app.client.Terminal;

import java.io.IOException;
import java.util.Objects;

public class Apples2Apples {
    static Server server;
    static Game game;
    static Client client;
    /**
     *
     * @param argv Not given, local instance. HOST 3 -> Hosts 3 players. CONNECT 127.0.0.1 -> connects to server.
     */
    public static void main(String[] argv) throws A2ANetworkException {
        if(argv.length == 0){
            try{
                startNewGame();
            } catch (A2ANetworkException e){
                e.printStackTrace();
            }
        }else if(Objects.equals(argv[0], "HOST")) { // HOST 3, fill rest with bots util Environment.MAX_PLAYERS.
            try{
                startNewGame(Integer.parseInt(argv[1]));
            } catch (A2ANetworkException | IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }else if(Objects.equals(argv[0], "CONNECT")){ // CONNECT localhost. Connects to local host.
            try{
                startNewGame(argv[1]);
            } catch (A2ANetworkException | IOException | InterruptedException | A2AIOException e){
                e.printStackTrace();
            }
        }
    }

    // Server with bots only.
    public static void startNewGame() throws A2ANetworkException {
        server = new Server(Environment.PORT);
        server.waitForClients(0);

        game = new Game(server);
        game.run();
    }

    // Client
    public static void startNewGame(String ipAddress) throws A2ANetworkException, IOException, InterruptedException, A2AIOException {
        client = Client.connect(ipAddress, Environment.PORT);
        Terminal terminal = new Terminal(client);
        terminal.run(true);
    }

    // Server
    public static void startNewGame(int numberOfOnlinePlayers) throws A2ANetworkException, IOException, ClassNotFoundException {
        server = new Server(Environment.PORT);
        server.waitForClients(numberOfOnlinePlayers);

        game = new Game(server);
        game.run();
    }
}
