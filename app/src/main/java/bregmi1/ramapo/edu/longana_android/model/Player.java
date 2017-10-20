package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Player {
    private int score;
    private Hand hand;
    protected Side side;

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

    public boolean isHandEmpty(){
        return hand.isEmpty();
    }

    public void drawDomino(Domino domino){
        hand.add(domino);
    }

    public int hasDominoInHand(Domino domino){
        return hand.getDominoIndex(domino);
    }

    public String play(int dominoIndex, Layout layout, Side side, boolean playerPassed ){
        Domino domino = hand.getDomino(dominoIndex);
        if(domino.equals(layout.getEngine())) layout.setEngine();
        else{
            if(side != this.side) return "Invalid Side!";
            String message = "";
            if((message = layout.placeDomino(domino,side)) != null) return message;
        }
        hand.playDomino(dominoIndex);
        return null;
    }


}
