package bregmi1.ramapo.edu.longana_android.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */


public class Stock implements Serializable {
    //indicates the size of hand
    private final int HAND_SIZE = 8;
    //container for storing the dominos in boneyard
    private Vector<Domino> stock;

    /**
     * Default constructor for the stock
     */
    public Stock(){
        stock = new Vector<>();
    }

    /**
     * Construtor for the stock, Generates all the possible dominos
     *
     * @param maxPip maximum pip that can be used in a domino
     */
    public Stock(int maxPip){
        stock = new Vector<>();
        for (int i = 0; i <= maxPip; i++) {
            for (int j = i; j <= maxPip; j++) {
                stock.add(new Domino(i,j));
            }
        }
    }

    /**
     * Getter for the stock
     *
     * @return copy of the stock vector
     */
    Vector<Domino> getStock() {
        return (Vector<Domino>) stock.clone();
    }

    /**
     * setter for the stock
     * @param stock vector of stock to set
     */
    public void setStock(Vector<Domino> stock) {
        this.stock = stock;
    }

    /**
     * To check if the stock is empty
     * @return true is stock is empty, else false
     */
    boolean isEmpty() {
        return stock.isEmpty();
    }

    /**
     * To shuffle the stock randomly
     */
    void shuffleStock() {
        Random random = new Random(System.currentTimeMillis());
        Collections.shuffle(stock,random);
    }

    /**
     * To generate a hand from the stock
     * @return Hand object with the generated dominos
     */
    Hand generateHand() {
        Vector <Domino> hand = new Vector<>();
        for (int i = 0; i < HAND_SIZE; i++){
            hand.add(stock.elementAt(0));
            stock.removeElementAt(0);
        }
        return new Hand(hand);
    }

    /**
     * To draw a domino from the stock
     * @return domino from the top of the stock
     */
    Domino drawDomino() {
        if(stock.isEmpty()) return  null;
        Domino temp = stock.elementAt(0);
        stock.removeElementAt(0);
        return temp;
    }

    /**
     * To get stringified version of the stock
     * @return string representing the stock
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Boneyard:\n");
        for (Domino d : stock) {
            builder.append(d.toString()).append(" ");
        }
        return builder.toString();
    }
}
