package project.app.game.stage;

import project.app.coms.msg.Instruction;
import project.app.coms.msg.Message;
import project.app.coms.msg.RequestMessage;
import project.app.coms.msg.StaticMessage;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.object.cards.card.Card;
import project.app.player.Bot;
import project.app.player.HumanPlayer;
import project.app.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Play stage where all main game functionality is done.
 */
public class PlayStage02 extends Stage{
    public PlayStage02(State currentState, Stage nextStage) {
        super(currentState, nextStage);
    }
    public PlayStage02(State currentState) {
        super(currentState);
    }

    @Override
    public void run() {
        playGreenApple();
        playRedApple();
        shuffleBoard();

        finished();
    }

    public void playGreenApple(){
        gameState.currentGreenApple = gameState.greenApples.pickOne();

        for (Player player : gameState.players) {
            greenStatic(player, gameState.currentGreenApple);
        }
    }

    /**
     *  Send red apple request if player is not judge.
     */
    public void playRedApple(){
        String string = "You are the judge, wait for red cards.";

        for (Player player : gameState.players) {

            if(! player.isJudge()){
                appleRequest(player);

            }else if(player.isJudge() && player instanceof HumanPlayer){
                try {
                    gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(string));
                } catch (A2AIOException | A2AMessageException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates and send static message to all players, new round and what green apple was drawn.
     * Only when player is HumanPlayer.
     *
     * @param player Player (human) to send message to.
     * @param greenApple Apple to publish.
     */
    private void greenStatic(Player player, Card greenApple){
        StringBuilder msg = new StringBuilder();

        msg.append("*****************************************************\n");
        msg.append("*****************************************************\n");
        if(player.isJudge()) {
            msg.append("**                 NEW ROUND - JUDGE               **\n");
        } else {
            msg.append("**                    NEW ROUND                    **\n");
        }
        msg.append("*****************************************************\n\n");
        msg.append("Question: ");
        msg.append(greenApple.toString());
        msg.append("\n");

        if (player instanceof HumanPlayer){
            try {
                gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(msg.toString()));
            } catch (A2AIOException  | A2AMessageException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ask for red apple from player.
     *
     * @param player Human, send request. Bot chooses random card.
     */
    private void appleRequest(Player player){
        ExecutorService threadpool = Executors.newFixedThreadPool(gameState.players.size() - 1);
        if (player instanceof HumanPlayer){ // Ask client for card, then add to board (HashMap).
            try {
                ((HumanPlayer) player).getClient().writeObject(new RequestMessage<>("", Instruction.PLAY));

                //Make sure every com.player can answer at the same time
                Runnable task = () -> {
                    Card card = null;
                    try {
                        Message<Card> msg = (Message<Card>) ((HumanPlayer) player).getClient().readObject();
                        card = msg.getContent();
                    } catch (A2AIOException  | A2AMessageException e) {
                        e.printStackTrace();
                    }

                    gameState.board.put(player.getId(), card); // Add played card to board, with player id as key.
                };
                threadpool.execute(task);

            } catch (A2AIOException e) {
                e.printStackTrace();
            }
        }else if (player instanceof Bot){   // Bot plays random card.
            Card card = ((Bot) player).play();
            gameState.board.put(player.getId(), card);
        }

        threadpool.shutdown();

        while(!threadpool.isTerminated()) { // Check ea
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Func. already tested in BoardTest.java.
     * Shuffles board with cards, corresponding index for player id.
     */
    public void shuffleBoard(){
        gameState.board.shuffle();
    }
}
