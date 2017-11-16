package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Human extends Player {

    /**
     * Constructor for Human class
     */
    public Human(){
        side = Side.LEFT;
        otherSide = Side.RIGHT;
    }

    /**
     * Get hint for the human player using the hint strategy
     *
     * @param layout       layout to get the hint for
     * @param playerPassed indicates whether previous player passed
     * @return return a Move with the domino to play and side
     */
    Move getHint(Layout layout, boolean playerPassed) {
        return hint(layout, playerPassed);
    }

    /**
     * To get the explained strategy for hint logic for the suggested move
     *
     * @return string representing the hint strategy
     */
    String getHintStrategy() {
        return this.hintStrategy;
    }

}
