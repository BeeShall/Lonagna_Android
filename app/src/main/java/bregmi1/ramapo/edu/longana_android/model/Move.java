package bregmi1.ramapo.edu.longana_android.model;

import java.io.Serializable;

/**
 * Created by beeshall on 10/29/17.
 */

class Move implements Serializable {
    //to hold the domino for the move
    private Domino domino;
    //to hold the side for the move
    private Side side;

    /**
     * Constructor for the move
     *
     * @param domino domino in the move
     * @param side   side the move was or to be played on
     */
    Move(Domino domino, Side side) {
        this.domino = domino;
        this.side = side;
    }

    /**
     * Getter for the domino in the move
     *
     * @return domino in the move
     */
    Domino getDomino() {
        return domino;
    }

    /**
     * setter for the domino in the move
     * @param domino domino to set dor the move
     */
    public void setDomino(Domino domino) {
        this.domino = domino;
    }

    /**
     * Getter for the side in the move
     * @return side in the move
     */
    Side getSide() {
        return side;
    }

    /**
     * setter for the side in the move
     * @param side side to set for the move
     */
    void setSide(Side side) {
        this.side = side;
    }

    /**
     * To get the stringified version of the move
     * @return string representing the move
     */
    @Override
    public String toString() {
        return domino.toString() + " on the " + side.toString();
    }
}
