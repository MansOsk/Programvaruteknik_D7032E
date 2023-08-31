package project.app.player;

import project.app.object.cards.Hand;
import project.app.object.cards.card.Card;

import java.io.Serializable;

public abstract class Player implements Serializable {
    private Hand hand;
    private boolean isJudge;
    private int id;
    private Hand greenApples;

    public Player() {
        this.isJudge = false;
    }

    public Player(boolean isJudge, int id){
        this.hand = new Hand();
        this.isJudge = isJudge;
        this.id = id;
        this.greenApples = new Hand();
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){ this.id = id; }

    public boolean isJudge(){
        return this.isJudge;
    }

    public Hand getHand(){
        return this.hand;
    }

    public void setJudge(boolean judge) {
        this.isJudge = judge;
    }

    public void setHand(Hand hand){
        this.hand = hand;
    }

    public Hand getScore() {
        return greenApples;
    }

    public void setScore(Hand cards) {
        this.greenApples = cards;
    }

    public void addScore(Card card){
        this.greenApples.add(card);
    }
}

