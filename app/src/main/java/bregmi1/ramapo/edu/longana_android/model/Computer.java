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

    @Override
    public boolean play(int dominoIndex, Layout layout, Side side, boolean playerPassed) {
        return super.play(dominoIndex, layout, side, playerPassed);
    }

    public boolean play(Layout layout, boolean playerPassed) {
        Move hintMove = hint(layout, playerPassed);
        if (hintMove == null) {
            return false;
        }
        hintStrategy = new StringBuilder(hintStrategy).append("\n Therefore, Computer played ").append(hintMove.getDomino().toString()).append(" to the ").append(hintMove.getSide().toString()).toString();
        return super.play(hand.getDominoIndex(hintMove.getDomino()), layout, hintMove.getSide(), playerPassed);
    }
}
