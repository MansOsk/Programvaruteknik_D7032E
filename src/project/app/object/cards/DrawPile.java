package project.app.object.cards;

import project.app.exception.A2ABoundsException;
import project.app.exception.A2AIOException;
import project.app.object.cards.card.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * DrawPile with a discard pile.
 * Inheritance where any picked card gets added to an added discard pile.
 * @author Måns Oskarsson mnsosk-7
 */
public class DrawPile extends Cards implements Serializable {
    private static class DiscardPile extends Cards{
        public DiscardPile() {
            super();
        }
    }

    private DiscardPile discarded;

    /**
     * @param fileName Creates a deck based on filename.
     * @throws A2AIOException Can't load file.
     */
    public DrawPile(String fileName) throws A2AIOException {
        super(fileName);
        this.discarded = new DiscardPile();
    }

    public DrawPile(String fileName, int max) throws A2AIOException {
        super(fileName, max);
        this.discarded = new DiscardPile();
    }

    public DrawPile(String fileName, int start, int end) throws A2AIOException, A2ABoundsException {
        super(fileName, start, end);
        this.discarded = new DiscardPile();
    }

    /**
     * One card (x) from deck is picked and gets removed.
     * @return Card being picked.
     */
    @Override
    public Card pickOne() {
        if (super.size() == 0){ // Empty.
            restore();
        }
        Card card = super.pickOne();
        super.remove(card);
        this.discarded.add(card);
        return card;
    }

    /**
     * IF many cards (xs) from deck is picked and remove xs.
     *
     * @param x Amount of cards being picked.
     * @return Return cards.
     */
    @Override
    public ArrayList<Card> pickMany(int x) {
        if(x > super.size()){   // IF not enough of cards to pick, restore and shuffle.
            restore();
        }
        ArrayList<Card> cards = super.pickMany(x);
        super.remove(cards);
        for(Card y : cards) {
            discarded.add(y);
        }
        return cards;
    }

    /**
     * Restore draw pile by adding discard pile again. Reshuffle.
     */
    public void restore(){
        Iterator<Card> iter = discarded.iterator();
        while(iter.hasNext()){
            super.add(iter.next());    // Add to deck again.
        }
        this.discarded = new DiscardPile();
        shuffle();
    }

}
