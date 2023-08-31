package project.app.game.stage;

import project.Environment;
import project.app.coms.msg.Instruction;
import project.app.coms.msg.StaticMessage;
import project.app.exception.A2AIOException;
import project.app.exception.A2AMessageException;
import project.app.player.HumanPlayer;
import project.app.player.Player;
import project.app.player.SortByPoints;

import java.util.ArrayList;

/**
 * Publish leaderboard, draw cards and select new judge. Check win condition.
 */
public class ReplenishStage04 extends Stage{
    public ReplenishStage04(State currentState, Stage nextStage) {
        super(currentState, nextStage);
    }
    public ReplenishStage04(State currentState) {
        super(currentState);
    }

    @Override
    public void run() {
        if(Environment.DEBUG){ // IF want to see some result after each game loop in server window.
            debugPrintResult();
        }

        try{
            publishLeaderboard(gameState.players);
        }catch (A2AIOException e){
            e.printStackTrace();
        }

        replenishCards();

        try {
            newJudge();
        } catch (A2AIOException e) {
            e.printStackTrace();
        }

        if(winCondition()){ // Stop game loop if win condition is fulfilled.
            stop();
        }else{              // Continue to next stage (in this game loop design back to play stage).
            gameState.board.clear();
            finished();
        }
    }

    /**
     * Sort players by points, then publish to players. Redo to unsorted when done (because of next judge in list).
     *
     * @param players Player list.
     * @throws A2AIOException If publish fails
     */
    public void publishLeaderboard(ArrayList<Player> players) throws A2AIOException {
        ArrayList<Player> playersUnsorted = (ArrayList<Player>) players.clone();
        players.sort(new SortByPoints()); // Tested.

        StringBuilder string = new StringBuilder();

        string.append("--------LEADERBOARD--------\n");
        int i = 1;
        for(Player player : players){ // Add player + id and points.
            string.append(i);
            string.append(": Player ");
            string.append(player.getId());
            string.append(" = ");
            string.append(player.getScore().size());
            string.append(" point\n");
            i++;
        }
        string.append("---------------------------\n");

        for(Player player : players){
            if(player instanceof HumanPlayer){
                try{
                    gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(string.toString()));
                }catch (A2AIOException | A2AMessageException e){
                    throw new A2AIOException("Could not publish leaderboard to players", e);
                }
            }
        }
        gameState.players = playersUnsorted;

        if(Environment.DEBUG){ // Prints leaderboard in server window.
            System.out.println(string);
        }
    }

    public void replenishCards() {
        drawCards(1); // In parent class.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Select the new judge (next in list) and remove old judge.
     *
     * @throws A2AIOException IF network error when updating human player judge.
     */
    public void newJudge() throws A2AIOException {
        for (int i = 0; i < gameState.players.size(); i++) {
            if(gameState.players.get(i).isJudge()){
                Player oldJudge = gameState.players.get(i);
                Player newJudge = gameState.players.get((i+1) % (Environment.MAX_PLAYERS));

                oldJudge.setJudge(false);
                newJudge.setJudge(true);

                gameState.players.set(i, oldJudge);
                gameState.players.set((i+1) % Environment.MAX_PLAYERS, newJudge);
                if(oldJudge instanceof HumanPlayer){
                    try{
                        gameState.server.staticMessage((HumanPlayer) gameState.players.get(i), new StaticMessage<>(false, Instruction.JUDGE));
                    }catch (A2AMessageException | A2AIOException e){
                        throw new A2AIOException("Couldn't write to player, updating to judge = false",e);
                    }
                }
                if(newJudge instanceof HumanPlayer){
                    try{
                        gameState.server.staticMessage((HumanPlayer) gameState.players.get((i+1) % (Environment.MAX_PLAYERS)), new StaticMessage<>(true, Instruction.JUDGE));
                    }catch (A2AMessageException | A2AIOException e){
                        throw new A2AIOException("Couldn't write to player, updating to judge = true",e);
                    }
                }
                break;
            }
        }
    }

    /**
     * Check any player have requirement to flag win.
     *
     * @return If score greater or equal than win threshold, return true.
     */
    public boolean winCondition(){
        boolean win = false;
        for(Player player : gameState.players){
            if(player.getScore().size() >= Environment.WIN){
                try {
                    winMessage(player);
                } catch (A2AMessageException | A2AIOException e){
                    e.printStackTrace();
                }
                return true;
            }
        }
        return win;
    }

    private void winMessage(Player playerWin) throws A2AMessageException, A2AIOException {
        StringBuilder string = new StringBuilder();
        string.append("\n\n!!! Player (ID ");
        string.append(playerWin.getId());
        string.append(")");
        string.append(" HAS WON THE GAME !!!");
        for(Player player : gameState.players){
            if( player instanceof HumanPlayer){
                gameState.server.staticMessage((HumanPlayer) player, new StaticMessage<>(string.toString()));
                ((HumanPlayer) player).getClient().writeObject(new StaticMessage<>("",Instruction.EXIT));
            }
        }
    }

    /**
     * Prints some game summaries in server window, for debug only...
     */
    private void debugPrintResult(){
        System.out.println("################################################\n");
        System.out.println("Green apple: " + gameState.currentGreenApple.toString() + "\n");
        System.out.println(gameState.board.toString());
        for (Player player : gameState.players){
            if(player.isJudge()){
                System.out.println("Player " + (player.getId()) + " was judge");
            }
        }
    }
}
