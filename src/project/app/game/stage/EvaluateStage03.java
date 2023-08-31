package project.app.game.stage;

import project.app.coms.msg.Instruction;
import project.app.coms.msg.Message;
import project.app.coms.msg.RequestMessage;
import project.app.coms.msg.StaticMessage;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.player.Bot;
import project.app.player.HumanPlayer;
import project.app.player.Player;

/**
 * Print information and send msg from play stage.
 * Handle judge functionality and publish result from judge.
 */
public class EvaluateStage03 extends Stage{
    public EvaluateStage03(State currentState, Stage nextStage) {
        super(currentState, nextStage);
    }
    public EvaluateStage03(State currentState) {
        super(currentState);
    }

    @Override
    public void run() {
        showResult();
        try{
            evaluate();
        }catch (A2AMessageException | A2AIOException e){
            e.printStackTrace();
        }
        finished();
    }

    public void showResult(){
        for (Player player : gameState.players) {
            if (player instanceof HumanPlayer){ // Ask client for card, then add to board (HashMap).
                try {
                    StringBuilder string = new StringBuilder();
                    string.append("\n\n");
                    string.append("Question: ");
                    string.append(gameState.currentGreenApple.toString());
                    string.append("\n");
                    string.append(gameState.board.toString());
                    string.append("\n");
                    string.append("Player (ID ");
                    string.append(player.getId());
                    string.append(")");

                    if(!player.isJudge()){
                        string.append(" you played ");
                        string.append(gameState.board.getCard(player.getId()).toString());
                    }

                    gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(string.toString()));
                } catch (A2AIOException | A2AMessageException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Ask what red apple judge chooses as best answer.
     *
     * @throws A2AMessageException Could'nt construct message from readObject().
     * @throws A2AIOException I/O error.
     */
    public void evaluate() throws A2AMessageException, A2AIOException {
        for (Player player : gameState.players) {
            if(player.isJudge()){
                int index = judgeRequest(player);
                int id = gameState.board.getId(index);
                publishResult(id);
            }
        }
    }

    private int judgeRequest(Player player) throws A2AIOException, A2AMessageException {
        if(player instanceof HumanPlayer && player.isJudge()){
            ((HumanPlayer) player).getClient().writeObject(new RequestMessage<>(gameState.board, Instruction.EVALUATE));
            Message<Integer> msg = (Message<Integer>) ((HumanPlayer) player).getClient().readObject();
            return msg.getContent();
        }else{
            assert player instanceof Bot;
            return ((Bot) player).random(gameState.board.size());
        }

    }

    private void publishResult(int id) throws A2AIOException, A2AMessageException {
        Player roundWinner = new HumanPlayer();
        for(Player player : gameState.players){
            if(player.getId() == id){
                player.addScore(gameState.board.getCard(id));
                roundWinner = player;
                if(player instanceof HumanPlayer){
                    gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(player.getScore(), Instruction.POINT));
                }
            }
        }
        for(Player player : gameState.players){
            if(player instanceof HumanPlayer){
                StringBuilder string = new StringBuilder();

                string.append("\n:::: ");
                string.append("Player (ID ");
                string.append(roundWinner.getId());
                string.append(") won by playing: ");
                string.append(gameState.board.getCard(id).toString());
                string.append(" ::::\n");

                gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(string.toString()));
            }
        }
    }
}
