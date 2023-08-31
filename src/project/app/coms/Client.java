package project.app.coms;

import project.app.coms.msg.Message;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.exception.A2ANetworkException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Holds network input and output for client.
 * Server holds list of all clients.
 * @author Måns Oskarsson mnsosk-7
 */
public class Client {
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    /**
     * Creates objects which allows communication.
     *
     * @param input Network stream in.
     * @param output Network stream out.
     */
    public Client(ObjectInputStream input, ObjectOutputStream output){
        this.input = input;
        this.output = output;
    }

    public static Client connect(Socket socket) throws A2ANetworkException {
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            return new Client(input, output);
        } catch (IOException e) {
            throw new A2ANetworkException("Unable to connect to socket", e);
        }
    }

    public static Client connect(String host, int port) throws A2ANetworkException {
        Socket socket;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new A2ANetworkException("Unable to connect to ip:port", e);
        }
        return connect(socket);
    }

    public void writeObject(Object object) throws A2AIOException {
        try{
            this.output.writeObject(object);
        }catch (IOException e){
            throw new A2AIOException ("Couldn't write to object stream", e);
        }
    }

    public Message readObject() throws A2AIOException , A2AMessageException {
        try{
            return (Message) this.inputStream().readObject();
        }catch (IOException e) {
            throw new A2AIOException ("Couldn't read object from client");
        } catch (ClassNotFoundException e) {
            throw new A2AMessageException("Couldn't find object when reading from client");
        }
    }

    public ObjectInputStream inputStream(){
        return this.input;
    }

    public ObjectOutputStream outputStream(){
        return this.output;
    }
}
