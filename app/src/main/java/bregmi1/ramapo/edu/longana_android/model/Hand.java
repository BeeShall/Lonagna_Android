package bregmi1.ramapo.edu.longana_android.model;

import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Hand {
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

    public void playDomino(Domino domino){
        hand.remove(domino);
    }

    public int getHandSum(){
        int total = 0;
        for(Domino d: hand){
            total+= d.getSum();
        }
        return  total;
    }
}
