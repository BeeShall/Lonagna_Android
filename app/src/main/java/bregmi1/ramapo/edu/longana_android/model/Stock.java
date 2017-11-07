package bregmi1.ramapo.edu.longana_android.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */


public class Stock implements Serializable {
    private final int HAND_SIZE = 8;
    private Vector<Domino> stock;

    public Stock(){
        stock = new Vector<>();
    }

    public Stock(int maxPip){
        stock = new Vector<>();
        for(int i=0; i<=maxPip; i++){
            for(int j=i; j<=maxPip; j++){
                stock.add(new Domino(i,j));
            }
        }
    }

    Vector<Domino> getStock() {
        return (Vector<Domino>) stock.clone();
    }

    public void setStock(Vector<Domino> stock) {
        this.stock = stock;
    }

    boolean isEmpty() {
        return stock.isEmpty();
    }

    void shuffleStock() {
        Random random = new Random(System.currentTimeMillis());
        Collections.shuffle(stock,random);
    }

    Hand generateHand() {
        Vector <Domino> hand = new Vector<>();
        for(int i=0; i<HAND_SIZE; i++){
            hand.add(stock.elementAt(0));
            stock.removeElementAt(0);
        }
        return new Hand(hand);
    }

    Domino drawDomino() {
        if(stock.isEmpty()) return  null;
        Domino temp = stock.elementAt(0);
        stock.removeElementAt(0);
        return temp;
    }

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
