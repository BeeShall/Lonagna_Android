package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Human extends Player {
    public Human(){
        side = Side.LEFT;
        otherSide = Side.RIGHT;
    }

    Move getHint(Layout layout, boolean playerPassed) {
        return hint(layout, playerPassed);
    }

    String getHintStrategy() {
        return this.hintStrategy;
    }

}
