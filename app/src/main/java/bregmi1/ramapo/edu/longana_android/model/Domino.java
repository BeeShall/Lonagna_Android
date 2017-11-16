package bregmi1.ramapo.edu.longana_android.model;

import java.io.Serializable;

/**
 * Created by beeshall on 10/17/17.
 */

public class Domino implements Serializable {
    //left side pip of the domino
    private int pip1;
    //right side pip of the domino
    private int pip2;
    //indicates if the domino is a double
    private boolean doubleDomino;

    /**
     * Default constructor for the domino
     */
    public Domino(){
        pip1 = -1;
        pip2 = -1;
    }

    /**
     * Contructor for the domino
     *
     * @param pip1
     * @param pip2
     */
    Domino(int pip1, int pip2) {
        this.pip1 = pip1;
        this.pip2 = pip2;
        this.doubleDomino = (pip1 == pip2);
    }

    /**
     * Getter for pip1 in domino
     *
     * @return left pip of the domino
     */
    public int getPip1() {
        return pip1;
    }

    /**
     * Getter for pip2
     * @return right pip of the domino
     */
    public int getPip2() {
        return pip2;
    }

    /**
     * to check if the domino is a double
     * @return whether the domino is a double
     */
    public boolean isDouble() {
        return this.doubleDomino;
    }

    /**
     * switches the left and right pip in the domino
     * @return a new domino with the pips flipped
     */
    Domino flip() {
        return new Domino(pip2, pip1);
    }

    /**
     * gets sum of pips in the domino
     * @return the sum of pip in the domino
     */
    int getSum() {
        return pip1+pip2;
    }

    /**
     * checks if the passed domino is equal to this
     * @param domino domino to be checked against
     * @return true is same false if not
     */
    boolean equals(Domino domino) {
        return ((pip1 == domino.pip1) &&  (pip2 == domino.pip2)) || ((pip1 == domino.pip2) &&  (pip2 == domino.pip1));
    }

    /**
     * return the stringified version if the domino
     * @return string representing the domino
     */
    public String toString(){
        return pip1 + "-" + pip2;
    }

}
