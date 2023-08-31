package project.app.client;

import project.app.coms.Client;
import project.app.coms.msg.Message;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.player.HumanPlayer;
import project.app.player.Player;

import java.io.IOException;

/**
 *
 */
public class Terminal {
    private final Client client;
    private final HumanPlayer player;
    private final TerminalHandler handler;

    public Terminal(Client client){
        this.client = client;
        this.player = new HumanPlayer(false, hashCode());
        this.handler = new TerminalHandler(player, client);
    }

    public void run(boolean loop) throws A2AIOException {
        while(handler.isRun){
            try {
                Message<Object> msg = listen();
                this.handler.in(msg, loop);

            } catch (A2AIOException | A2AMessageException e) {
                e.printStackTrace();
            }
        }
    }

    public void run(boolean loop, boolean test) throws A2AIOException {
        while(handler.isRun){
            try {
                Message<Object> msg = listen();
                this.handler.in(msg, loop, test);

            } catch (A2AIOException | A2AMessageException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Message<Object> listen() throws A2AIOException, A2AMessageException {
        try{
            return (Message<Object>) this.client.inputStream().readObject();
        }catch (IOException e){
            throw new A2AIOException("Couldn't read input from server", e);
        }catch (ClassNotFoundException e){
            throw new A2AMessageException("Couldn't find reference for object in message", e);
        }
    }

    public Player getPlayer(){
        return this.player;
    }
}
