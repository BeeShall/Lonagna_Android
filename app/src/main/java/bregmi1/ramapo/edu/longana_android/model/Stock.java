package bregmi1.ramapo.edu.longana_android.model;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */



public class Stock {
    private Vector<Domino> stock;
    final int HAND_SIZE = 8;

    public Stock(){
        stock = new Vector<>();
    }

    public Stock(int MAX_PIP){
        for(int i=0; i<=MAX_PIP; i++){
            for(int j=i; j<=MAX_PIP; j++){
                stock.add(new Domino(i,j));
            }
        }
    }

    public void shuffleStock(){
        Random random = new Random(System.currentTimeMillis());
        Collections.shuffle(stock,random);
    }

    public Hand generateHand(){
        Vector <Domino> hand = new Vector<>();
        for(int i=0; i<HAND_SIZE; i++){
            hand.add(stock.elementAt(0));
            stock.removeElementAt(0);
        }
        return new Hand(hand);
    }

    public Domino drawDomino(){
        if(stock.isEmpty()) return  null;
        Domino temp = stock.elementAt(0);
        stock.removeElementAt(0);
        return temp;
    }

}
