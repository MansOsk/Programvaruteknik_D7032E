package project.app.game.stage;

import project.app.coms.msg.Instruction;
import project.app.coms.msg.StaticMessage;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.object.cards.DrawPile;
import project.app.object.cards.Hand;
import project.app.player.Bot;
import project.app.player.HumanPlayer;
import project.app.player.Player;

/**
 * Base template for what a game stage contains.
 */
public abstract class Stage {
    State gameState;
    private Stage nextStage;
    private boolean isFinished; // When stage is finished. IMPORTANT!
    private boolean stop; // When to stop gameloop.

    public Stage(State gameState, Stage nextStage){
        this.gameState = gameState;
        this.nextStage = nextStage;
        this.isFinished = false;
        this.stop = false;
    }

    public Stage(State gameState){
        this.gameState = gameState;
        this.isFinished = false;
        this.stop = false;
    }

    public Stage nextStage(){
        this.isFinished = false; // Reset flag before going to next stage.
        return this.nextStage;
    }

    public void nextStage(Stage stage){
        this.nextStage = stage;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void finished() { this.isFinished = !this.isFinished; }

    public boolean isStopped(){ return stop; }

    public void stop() { this.stop = !stop; }

    public void drawCards(int amountOfCards) {
        DrawPile drawpile = gameState.redApples;

        for (Player player : gameState.getPlayers()) {

            if (player instanceof HumanPlayer && !player.isJudge()) {
                Hand hand = new Hand(drawpile.pickMany(amountOfCards));
                try {
                    ((HumanPlayer) player).getClient().writeObject(new StaticMessage<>(hand, Instruction.DRAW));
                    wait((HumanPlayer) player);
                } catch (A2AIOException | A2AMessageException e) {
                    e.printStackTrace();
                }
            } else if (player instanceof Bot && !player.isJudge()) {
                Hand hand = new Hand(drawpile.pickMany(amountOfCards));
                player.setHand(hand);
            }
        }
    }

    /**
     * Always add func. finished() at end of run();
     */
    public abstract void run();

    /**
     * Wait's for response, ACK from client then continue. Result from read object not interesting.
     *
     * @param player Client who is written to.
     * @throws A2AMessageException If message content can't be translated to object.
     * @throws A2AIOException Can't be read from object stream.
     */
    void wait(HumanPlayer player) throws A2AMessageException, A2AIOException {
        player.getClient().readObject();
    }
}
