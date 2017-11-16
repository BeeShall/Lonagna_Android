package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Computer extends Player {

    /**
     * Constructor for the Computer class
     */
    public Computer(){
        side = Side.RIGHT;
        otherSide = Side.LEFT;
    }

    /**
     * To get the move strategy used by the computer
     *
     * @return stringified move strategy
     */
    public String getMoveStrategy() {
        return this.hintStrategy;
    }


    /**
     * Make the given move
     *
     * @param dominoIndex  index for the domino in hand
     * @param layout       layout of the game
     * @param side         side to play the domino in
     * @param playerPassed indicates whether the previous player passed
     * @return whether the move was successfully played
     */
    @Override
    public boolean play(int dominoIndex, Layout layout, Side side, boolean playerPassed) {
        return super.play(dominoIndex, layout, side, playerPassed);
    }

    /**
     * To play and make move for the computer using the hint logic
     * @param layout layout for the game
     * @param playerPassed indicates whether the previous player passed
     * @return whether the move was successfully played
     */
    public boolean play(Layout layout, boolean playerPassed) {
        Move hintMove = hint(layout, playerPassed);
        if (hintMove == null) {
            return false;
        }
        hintStrategy = new StringBuilder(hintStrategy).append("\n Therefore, Computer played ").append(hintMove.getDomino().toString()).append(" to the ").append(hintMove.getSide().toString()).toString();
        return super.play(hand.getDominoIndex(hintMove.getDomino()), layout, hintMove.getSide(), playerPassed);
    }
}
