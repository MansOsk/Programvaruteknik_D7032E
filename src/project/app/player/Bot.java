package project.app.player;

import project.app.object.cards.Hand;
import project.app.object.cards.card.Card;
import project.app.tool.Randomizer;

/**
 * Handled by server. Fill if not max players.
 */
public class Bot extends Player{

    public Bot() {
    }

    public Bot(boolean isJudge, int id) {
        super(isJudge, id);
    }

    /**
     * @return Random card which bot will play.
     */
    public Card play(){
        Hand hand = super.getHand();
        return hand.play(Randomizer.NextInt(hand.size()));
    }

    public int random(int max){
        return Randomizer.NextInt(max);
    }
}
