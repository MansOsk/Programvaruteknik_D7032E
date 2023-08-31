package project.test.coms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.Environment;
import project.app.coms.Client;
import project.app.coms.Server;
import project.app.coms.msg.Instruction;
import project.app.coms.msg.Message;
import project.app.coms.msg.RequestMessage;
import project.app.coms.msg.StaticMessage;
import project.app.exception.A2ANetworkException;
import project.app.object.cards.DrawPile;
import project.app.tool.Randomizer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConnectionTest {

    @Test
    public void connection() throws A2ANetworkException, IOException, ClassNotFoundException {
        int port = Environment.PORT + Randomizer.NextInt(100); // Add to port inorder to avoid port collision.
        Server server = new Server(port);

        String expectedContent = "OK";
        String expectedType = "STATIC";

        ExecutorService executor = Executors.newSingleThreadExecutor(); // Wait for connection on other thread.
        executor.execute(() -> Assertions.assertDoesNotThrow(() -> {
            server.waitForClients(1);
            ObjectOutputStream outToClient = server.getClients().get(0).outputStream();
            outToClient.writeObject(new StaticMessage<>("OK"));
        }));

        Client client = Client.connect("localhost", port);

        Message msg = (Message) client.inputStream().readObject();

        assertEquals(expectedContent, msg.getContent()); // Handshake if correct
        assertEquals(expectedType, msg.msgType()); // Handshake if correct

        executor.shutdown();
    }

    @Test
    public void connectionMsg() throws A2ANetworkException, IOException, ClassNotFoundException{
        int port = Environment.PORT + Randomizer.NextInt(100); // Add to port inorder to avoid port collision.
        Server server = new Server(port);

        String expectedContent = "[A Bad Haircut] - The perfect start to a bad hair day. ";
        String expectedType = "STATIC";
        Instruction expectedInstruction = Instruction.DRAW;

        ExecutorService executor = Executors.newSingleThreadExecutor(); // Wait for connection on other thread.
        executor.execute(() -> Assertions.assertDoesNotThrow(() -> {
            server.waitForClients(1);
            ObjectOutputStream outToClient = server.getClients().get(0).outputStream();
            DrawPile drawPile = new DrawPile(Environment.RED_APPLES);
            outToClient.writeObject(new RequestMessage<>(drawPile, Instruction.DRAW));
        }));

        Client client = Client.connect("localhost", port);

        Message<DrawPile> msg = (Message<DrawPile>) client.inputStream().readObject();

        assertEquals(expectedContent, msg.getContent().get(0).toString()); // Handshake if correct
        assertEquals(expectedInstruction, Instruction.DRAW);

        executor.shutdown();
    }
}
