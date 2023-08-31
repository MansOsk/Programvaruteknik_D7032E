package project.app.client;

import project.Environment;
import project.app.coms.Client;
import project.app.coms.msg.Instruction;
import project.app.coms.msg.Message;
import project.app.coms.msg.ResponseMessage;
import project.app.coms.msg.StaticMessage;
import project.app.exception.A2AIOException;
import project.app.object.cards.Board;
import project.app.object.cards.Hand;
import project.app.object.cards.card.Card;
import project.app.player.HumanPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Functionality for incoming and outgoing traffic for HumanPlayer.
 * @author Måns Oskarsson mnsosk-7
 */
public class TerminalHandler {
    public boolean isRun = true;
    private final HumanPlayer player;
    private final Client client;
    private final BufferedReader keyboard;
    private boolean test = false;
    public TerminalHandler(HumanPlayer player, Client client){
        this.player = player;
        this.client = client;

        this.keyboard = new BufferedReader(new InputStreamReader(System.in));
    }

    public void in(Message<Object> msg, boolean loop) throws A2AIOException {
        if(msg.getInstruction() == null){
            System.out.println(msg.getContent());
            acknowledgment();
        }
        else if(msg.msgType().equals("STATIC")){
            switch (msg.getInstruction()) {
                case DRAW -> {
                    Hand playerHand = player.getHand();
                    Hand hand = (Hand) msg.getContent();
                    for (int i = 0; i < hand.size(); i++) {
                        playerHand.add(hand.get(i));
                    }
                    player.setHand(playerHand);
                }
                case EXIT -> this.isRun = !this.isRun;
                case JUDGE -> player.setJudge((boolean) msg.getContent());
            }

            acknowledgment();
        }else if(msg.msgType().equals("REQUEST")){
            switch (msg.getInstruction()) {
                case INIT -> {
                    player.setId((Integer) msg.getContent());
                    client.writeObject(new ResponseMessage<>(player));
                }
                case PLAY -> {
                    for (int i = 0; i < player.getHand().size(); i++) {
                        System.out.format("[%d]\t", i);

                        Card card = player.getHand().get(i);
                        System.out.print(card.toString() + "\n");
                    }

                    // Make player pick card until it's in the boundary of its own card hand.
                    System.out.print("\n\nPick a card: ");
                    int cardNr = keyboard();
                    while (cardNr < 0 || cardNr >= player.getHand().size()) { // Try again if out of scope.
                        System.out.println("Wrong input\n");
                        cardNr = keyboard();
                    }
                    Card card = player.getHand().play(cardNr);
                    ResponseMessage<Card> response = new ResponseMessage<>(card);
                    client.writeObject(response);
                }
                case EVALUATE -> {
                    Board board = (Board) msg.getContent();
                    System.out.print("Pick a card: ");
                    int cardNr = keyboard();
                    while (cardNr < 0 || cardNr >= board.size()) { // Try again if out of scope.
                        cardNr = keyboard();
                    }
                    ResponseMessage<Integer> responseJudge = new ResponseMessage<>(cardNr);
                    client.writeObject(responseJudge);
                }
                case INFO -> {
                    ResponseMessage<HumanPlayer> playerInfo = new ResponseMessage<>(this.player);
                    client.writeObject(playerInfo);
                }
            }
        }

        // IF debug is enabled in environment.java, print every message detail. Type, instr. etc.
        if(Environment.DEBUG){
            System.out.println("[" + msg.msgType() + "] " + msg.getInstruction() + ": " + msg.getContent());
        }

        // Useful when testing, stops game loop.
        if(! loop){
            this.isRun = false;
        }
    }

    public void in(Message<Object> msg, boolean loop, boolean test) throws A2AIOException {
        this.in(msg, loop);
        this.test = test;
    }

    public int keyboard() throws A2AIOException  {
        try{
            if(!test){
                try{
                    return Integer.parseInt(keyboard.readLine());
                }catch (NumberFormatException e){
                    return keyboard();
                }
            }else{
                return Integer.parseInt(Environment.TEST_RESPONSE); // Default test response.
            }
        }catch (IOException e){
            throw new A2AIOException ("Couldn't get correct input from keyboard", e);
        }
    }

    /**
     * Server continues with next operation when ACK from client.
     *
     * @throws A2AIOException If message is not transmitted.
     */
    private void acknowledgment() throws A2AIOException {
        client.writeObject(new StaticMessage<>(Instruction.ACK));
    }
}
