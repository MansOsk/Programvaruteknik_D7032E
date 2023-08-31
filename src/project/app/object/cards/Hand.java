package project.app.object.cards;

import project.app.object.cards.card.Card;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  Cards that will contain in the players hand.
 *  @author Måns Oskarsson mnsosk-7
 */
public class Hand extends Cards implements Serializable {
    /**
     * Constructor for creating hand based on cards.
     *
     * @param cards Cards added to hand.
     */
    public Hand(ArrayList<Card> cards){
        super(cards);
    }

    /**
     * Constructor for creating hand without any cards.
     */
    public Hand(){
        super();
    }

    /**
     * Play card based on index and remove card from hand.
     *
     * @param i Played cared based on index.
     * @return  Returns card.
     */
    public Card play(int i){
        Card card = super.get(i);
        super.remove(card);
        return card;
    }

    public String toString(){
        StringBuilder string = new StringBuilder();
        for (Card card : super.pickMany(super.size())) {
            string.append(card.toString());
            string.append("\n");
        }

        return string.toString();
    }
}


