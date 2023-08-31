package project.app.object.cards;

import project.Environment;
import project.app.object.cards.card.Card;
import project.app.tool.Randomizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Board have list containing playerid, parent class (cards) for what card is played.
 * IMPORTANT! -> an id is connected to a card. This is heavily tested!
 */
public class Board extends Cards implements Serializable {

    ArrayList<Integer> playerId;
    public Board(){
        super();
        this.playerId = new ArrayList<>();
    }

    public Board(ArrayList<Integer> playerId){
        super();
        this.playerId = playerId;
    }

    public Board(ArrayList<Integer> playerId, ArrayList<Card> cards){
        super(cards);
        this.playerId = playerId;
    }

    public void put(int id, Card card){
        super.add(card);
        playerId.add(id);
    }

    public int getId(int index){
        return playerId.get(index);
    }

    public Card getCard(int id){
        int i = 0;
        for(int player : playerId){
            if(String.valueOf(id).equals(String.valueOf(player))){
                return super.get(i);
            }else{
                i++;
            }
        }
        return null;
    }

    public void clear(){
        super.clear();
        this.playerId.clear();
    }

    /**
     * Randomize board and indexes for player id.
     * Shuffle the board and check where shuffled cards are.
     * Place id on corresponding index for card after shuffle.
     */
    @SuppressWarnings("unchecked")
    public void shuffle(){
        int size = super.size();

        ArrayList<Card> oldCards = super.pickMany(size);
        ArrayList<Card> newCards = Randomizer.shuffle(super.pickMany(size));
        super.replaceAll(newCards);

        if(super.pickMany(super.size()).equals(oldCards)){ // IF identical with original, retry. Good for test.
            shuffle();
        }else{
            ArrayList<Integer> newPlayerId = (ArrayList<Integer>) playerId.clone();
                for (int i = 0; i < oldCards.size(); i++) {
                    Card card = oldCards.get(i);
                    int id = playerId.get(i);
                    for (int j = 0; j < super.size(); j++) {
                        if(Objects.equals(card.toString(), super.get(j).toString()) && i != j){
                            newPlayerId.set(j, id);
                            break;
                        }
                    }
                }
                playerId = newPlayerId;
        }
    }
    
    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append("Cards played: \n");
        for (int i = 0; i < playerId.size(); i++) {
            if(Environment.DEBUG){ // IF Debug then add id to board. Showing what player played card.
                string.append(String.format("ID=(%d)\t", playerId.get(i)));
            }else{
                string.append(String.format("[%d]\t", i));
            }
            string.append(super.get(i).toString());
            string.append("\n");
        }
        return string.toString();
    }
}
