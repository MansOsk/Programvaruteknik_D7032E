package project.app.object.cards.card;

import java.io.Serializable;

/**
 * Wildcard where user can choose content.
 * Check GenericCard for clarification on this.
 * @author Måns Oskarsson mnsosk-7
 */
public class WildCard extends Card implements Serializable {
    public WildCard(String s){
        super(s);
    }
}
