package project.app.game;

import project.Environment;
import project.app.coms.Server;
import project.app.exception.A2ABoundsException;
import project.app.game.stage.*;

/**
 * Creates a new Apples2Apples game.
 */
public class Game{
    private Stage currentStage;

    public Game(Server server){
        State state = new State(server);
        create(state);

        try {
            Environment.WIN = winCondition();
        } catch (A2ABoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the game cycle.
     * Currently, contains normal game stages.
     * Highly modifiable for changing order. However, game logic must make sense.
     */
    private void create(State state){
        this.currentStage = new InitStage01(state);
        Stage stage2 = new PlayStage02(state);
        Stage stage3 = new EvaluateStage03(state);
        Stage lastStage = new ReplenishStage04(state);

        this.currentStage.nextStage(stage2);
        stage2.nextStage(stage3);
        stage3.nextStage(lastStage);
        lastStage.nextStage(stage2);
    }

    private int winCondition() throws A2ABoundsException {
        if(Environment.MAX_PLAYERS == 4){
            return 8;
        }else if(Environment.MAX_PLAYERS == 5){
            return 7;
        }else if(Environment.MAX_PLAYERS == 6){
            return 6;
        }else if(Environment.MAX_PLAYERS == 7){
            return 5;
        }else if(Environment.MAX_PLAYERS >= 8){
            return 4;
        }else{
            throw new A2ABoundsException("Amount of players defined is incorrect for requirement");
        }
    }

    public void run(){
        while(! currentStage.isFinished()){
            currentStage.run();

            if(currentStage.isStopped()){  // If stopped, then exit and all done.
                break;
            }
            else if(currentStage.isFinished()){ // If finished, then continue cycle.
                this.currentStage = currentStage.nextStage();
            }
        }
    }
}
