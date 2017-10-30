package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/29/17.
 */

class Move {
    private Domino domino;
    private Side side;

    Move(Domino domino, Side side) {
        this.domino = domino;
        this.side = side;
    }

    Domino getDomino() {
        return domino;
    }

    public void setDomino(Domino domino) {
        this.domino = domino;
    }

    Side getSide() {
        return side;
    }

    void setSide(Side side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return domino.toString() + " on the " + side.toString();
    }
}
