package bregmi1.ramapo.edu.longana_android.model;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Hand implements Serializable {
    public Vector<Domino> hand;

    public Hand(Vector<Domino> hand){
        this.hand = hand;
    }

    public boolean hasDomino(Domino domino){
        for(Domino d: hand){
            if(d.equals(domino)) return true;
        }
        return false;
    }

    public int getDominoIndex(Domino domino){
        for(int i=0; i<hand.size(); i++){
            if(hand.get(i).equals(domino)) return i;
        }
        return -1;
    }

    public Vector<Domino> getHand() {
        return (Vector<Domino>) hand.clone();
    }

    public Domino getDomino(int index){
        return hand.elementAt(index);
    }

    public void playDomino(Domino domino){
        hand.remove(domino);
    }

    public void playDomino(int index){
        hand.removeElementAt(index);
    }

    public void add(Domino domino){
        hand.add(domino);
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean isEmpty(){
        return hand.isEmpty();
    }

    public int getHandSum(){
        int total = 0;
        for(Domino d: hand){
            total+= d.getSum();
        }
        return  total;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Hand: ");
        for (Domino d : hand) {
            builder.append(d.toString()).append(" ");
        }
        return builder.toString();
    }
}
