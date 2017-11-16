package bregmi1.ramapo.edu.longana_android.model;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Hand implements Serializable {
    //container to store the dominos in hand
    public Vector<Domino> hand;

    /**
     * Constructor for Hand
     *
     * @param hand vector of dominoes to be stored as hand
     */
    public Hand(Vector<Domino> hand){
        this.hand = hand;
    }

    /**
     * To see if a domino exists in hand
     *
     * @param domino domino to check if exists
     * @return true if exists false if not
     */
    public boolean hasDomino(Domino domino){
        for(Domino d: hand){
            if(d.equals(domino)) return true;
        }
        return false;
    }

    /**
     * To get the index of the given domino in hand
     * @param domino domino to get index of
     * @return index of the domino in hand, -1 if doesn't exist
     */
    public int getDominoIndex(Domino domino) {
        for (int i = 0; i < hand.size(); i++){
            if(hand.get(i).equals(domino)) return i;
        }
        return -1;
    }

    /**
     * Getter to get a copy of the hand
     * @return copy of the Hand vector
     */
    public Vector<Domino> getHand() {
        return (Vector<Domino>) hand.clone();
    }

    /**
     * To get the domino with the given index
     * @param index index of the domino to get
     * @return domino with the given index
     */
    public Domino getDomino(int index){
        return hand.elementAt(index);
    }

    /**
     * To play (remove) the given domiono from hand
     * @param domino domino to play
     */
    public void playDomino(Domino domino){
        hand.remove(domino);
    }

    /**
     * To play(remove) the domino with the given index
     * @param index index of the domino to play
     */
    public void playDomino(int index){
        hand.removeElementAt(index);
    }

    /**
     * to add a domino to hand
     * @param domino Domino to be added to hand
     */
    public void add(Domino domino){
        hand.add(domino);
    }

    /**
     * To get the number of dominos in hand
     * @return the number of dominos in hand
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * To check if the hand is empty
     * @return
     */
    public boolean isEmpty(){
        return hand.isEmpty();
    }

    /**
     * To get sum of all the dominos in hand
     * @return sum of all dominos in hand
     */
    public int getHandSum(){
        int total = 0;
        for(Domino d: hand){
            total+= d.getSum();
        }
        return  total;
    }

    /**
     * To get the stringified version of hand
     * @return string representing the hand
     */
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
