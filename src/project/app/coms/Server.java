package project.app.coms;

import project.app.coms.msg.StaticMessage;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.exception.A2ANetworkException;
import project.app.player.HumanPlayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Network server which contains all connected clients.
 * Provides communication functionality.
 * @author Måns Oskarsson mnsosk-7
 */
public class Server {
    private final ServerSocket serverSocket;
    private final List<Client> clients = new ArrayList<>();

    /**
     *
     */
    public Server(int port) throws A2ANetworkException {
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            throw new A2ANetworkException("Unable to open port at " + port, e);
        }
    }

    /**
     * Iterates through players, waits for clients that will be added and adds them to a contact list.
     *
     * @param numberOfOnlinePlayers Number of player's the server will wait for.
     * @throws A2ANetworkException Can't establish connection between a server and a client.
     */
    public void waitForClients(int numberOfOnlinePlayers) throws A2ANetworkException {
        for(int onlineClient = 0; onlineClient < numberOfOnlinePlayers; onlineClient++) {
            try{
                Socket connectionSocket = serverSocket.accept();
                Client newClient = Client.connect(connectionSocket);    // Client contains output and input stream.
                this.clients.add(newClient);
            } catch (IOException e) {
                throw new A2ANetworkException("Unable to open new client connection: ", e);
            }
        }
    }

    /**
     * Writes static message and waits for acknowledgement.
     *
     * @param player Endpoint
     * @param msg StaticMessage.
     * @throws A2AIOException Can't write to object stream.
     * @throws A2AMessageException Can't read and construct message (find type) for read object.
     */
    public void staticMessage(HumanPlayer player, StaticMessage msg) throws A2AIOException, A2AMessageException {
        try{
            player.getClient().writeObject(msg);
            player.getClient().readObject();
        }catch (A2AIOException e){
            throw new A2AIOException("Cant write to object stream", e);
        }catch (A2AMessageException e){
            throw  new A2AMessageException("", e);
        }
    }

    /**
     * @return Clients connected to server.
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     * Close server socket.
     *
     * @throws A2ANetworkException Unable to close remote connection
     */
    public void close() throws A2ANetworkException {
        try{
            this.serverSocket.close();
        }catch (IOException e){
            throw new A2ANetworkException("Unable to close remote connection: ", e);
        }

    }
}
