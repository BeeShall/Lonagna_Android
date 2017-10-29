package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Computer extends Player {
    public Computer(){
        side = Side.RIGHT;
        otherSide = Side.LEFT;
    }

    public String getMoveStrategy() {
        return this.hintStrategy;
    }

    public String play(Layout layout, boolean playerPassed) {
        Move hintMove = hint(layout, playerPassed);
        return super.play(hand.getDominoIndex(hintMove.getDomino()), layout, hintMove.getSide(), playerPassed);
    }
}
