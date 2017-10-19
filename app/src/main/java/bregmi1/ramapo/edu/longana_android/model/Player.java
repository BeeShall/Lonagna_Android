package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Player {
    private int score;
    private Hand hand;

    public Player(){
        this(0,null);
    }

    public Player(int score, Hand hand){
        this.score=score;
        this.hand=hand;
    }

    public int getScore() {
        return score;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }


}
