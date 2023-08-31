package project.app.object.cards.card;

import java.io.Serializable;

/**
 * Representation of a single card.
 * @author Måns Oskarsson mnsosk-7
 */
public abstract class Card implements Serializable {
    private String text;

    /**
     * @param text Card content
     */
    public Card(String text){
        this.text = text;
    }

    /**
     * @return Card content
     */
    public String toString(){
        return this.text;
    }

    /**
     * @param text Replace content with text, wild card for example.
     */
    public void replace(String text){
        this.text = text;
    }
}
