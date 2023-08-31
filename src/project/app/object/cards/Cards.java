package project.app.object.cards;

import project.app.exception.A2ABoundsException;
import project.app.exception.A2AIOException;
import project.app.object.cards.card.Card;
import project.app.tool.File;
import project.app.tool.Randomizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representation of n amount of cards.
 * @author Måns Oskarsson mnsosk-7
 */
public abstract class Cards implements Serializable {

    private ArrayList<Card> cards;  // Polymorphism of Card, accepts variety of Card like generic or wild.

    /**
     * Initialize cards from fileName.
     *
     * @param fileName Initialize cards from fileName.
     * @throws A2AIOException File can't load.
     */
    public Cards(String fileName) throws A2AIOException {
        this();
        try{
            this.cards = File.readApples(fileName);
        } catch (A2AIOException e){
            throw new A2AIOException("Error when creating cards", e);
        }
    }

    /**
     * Initialize cards from 0 to max from lines in file.
     *
     * @param fileName Initialize cards from fileName.
     * @param max Initialize max amount cards from fileName.
     * @throws A2AIOException File can't load.
     */
    public Cards(String fileName, int max) throws A2AIOException {
        this();
        try{
            this.cards = File.readApples(fileName, max);
        } catch (A2AIOException e){
            throw new A2AIOException("Error when creating cards", e);
        } catch (A2ABoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize cards between a bound from lines in file.
     *
     * @param fileName Initialize cards from fileName.
     * @param start Which line the iterator starts at.
     * @param end Which line the iterator ends at.
     * @throws A2AIOException File can't load.
     */
    public Cards(String fileName, int start, int end) throws A2AIOException, A2ABoundsException {
        this();
        try{
            this.cards = File.readApples(fileName, start, end);
        } catch (A2AIOException e){
            throw new A2AIOException("Error when creating cards", e);
        } catch (A2ABoundsException e) {
            throw new A2ABoundsException("Out of bounds when reading apple");
        }
    }

    /**
     * Initialize cards from an existing subset of cards.
     *
     * @param cards Initialize cards from an existing subset of cards.
     */
    public Cards(ArrayList<Card> cards){
        this();
        this.cards = cards;
    }

    public Cards(){
        this.cards = new ArrayList<>();
    }

    /**
     *
     * @param i Get card
     * @return Card in list.
     */
    public Card get(int i){
        try{
            return this.cards.get(i);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     *
     * @param index Index where overwritten
     * @param card Card for overwrite.
     */
    public void set(int index, Card card){
        this.cards.set(index, card);
    }

    /**
     *  Deletes and replaces all cards with new cards.
     *
     * @param cards Cards replacing whole list.
     */
    public void replaceAll(ArrayList<Card> cards){
        this.cards = cards;
    }

    /**
     * @param card Card for finding index.
     * @return Index for card.
     */
    public int indexOf(Card card){
        return this.cards.indexOf(card);
    }

    /**
     * @param card Check if contains card.
     * @return If contains.
     */
    public boolean contains(Card card){
        return this.cards.contains(card);
    }

    /**
     * @return An iterator for iterating through cards.
     */
    public Iterator<Card> iterator(){
        return this.cards.iterator();
    }

    public void add(Card card){
        this.cards.add(card);
    }

    /**
     * @param cards Subset added to card's.
     */
    public void add(ArrayList<Card> cards){
        this.cards.addAll(cards);
    }

    public void remove(int i){
        this.cards.remove(i);
    }

    /**
     * Removes a card based on card given.
     *
     * @param card Card being removed.
     */
    public void remove(Card card){
        this.cards.remove(card);
    }

    /**
     * Removes a subset of cards.
     *
     * @param cards removed from cards.
     */
    public void remove(ArrayList<Card> cards){
        for (Card card : cards) {
            this.cards.remove(card);
        }
    }

    public void clear(){
        this.cards = new ArrayList<>();
    }

    public boolean isEmpty(){
        return this.cards.isEmpty();
    }

    public int size(){
        return this.cards.size();
    }

    /**
     * Picks a random card from cards.
     *
     * @return random card from cards.
     */
    public Card pickOne(){
        return this.cards.get(0);
    }

    /**
     * @param x Amount of cards being picked
     * @return A sublist of x cards.
     */
    public ArrayList<Card> pickMany(int x){
        ArrayList<Card> tempCards = new ArrayList<>();
        for(int i = 0; i < x; i++){
            tempCards.add(cards.get(i));
        }
        return tempCards;
    }

    /**
     * Shuffles all cards randomly.
     */
    public void shuffle(){
        ArrayList<Card> unshuffledCards = this.cards;
        this.cards = Randomizer.shuffle(unshuffledCards);
    }

    /**
     * Prints all cards. Removed in the future.
     *
     * @return Returns content of each card, seperated by space.
     */
    public String toString(){
        StringBuilder string = new StringBuilder();
        for (Card x : this.cards){
            string.append(x.toString()).append(' ');
        }
        return string.toString();
    }
}
