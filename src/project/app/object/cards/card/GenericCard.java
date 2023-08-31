package project.app.object.cards.card;

import java.io.Serializable;

/**
 * Generic card where content can't be changed.
 * @author Måns Oskarsson mnsosk-7
 */
public class GenericCard extends Card implements Serializable {
    public GenericCard(String s){
        super(s);
    }

    /**
     * Only for clarification of card-types and quality attributes.
     * Such as modifiability and expandability since it's easy to create new card types.
     *
     * @param s Overwritten with no func. Generic cards can't be changed therefore overwrite with empty func.
     */
    @Override
    public void replace(String s) {
    }
}
