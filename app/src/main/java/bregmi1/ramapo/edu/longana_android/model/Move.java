package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/29/17.
 */

public class Move {
    private Domino domino;
    private Side side;

    public Move(Domino domino, Side side) {
        this.domino = domino;
        this.side = side;
    }

    public Domino getDomino() {
        return domino;
    }

    public void setDomino(Domino domino) {
        this.domino = domino;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
}
