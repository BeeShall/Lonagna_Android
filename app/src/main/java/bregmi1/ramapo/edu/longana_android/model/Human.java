package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Human extends Player {
    public Human(){
        side = Side.LEFT;
        otherSide = Side.RIGHT;
    }

    public Move getHint(Layout layout, boolean playerPassed) {
        return hint(layout, playerPassed);
    }

    public String getHintStrategy() {
        return this.hintStrategy;
    }

    @Override
    public boolean play(int dominoIndex, Layout layout, Side side, boolean playerPassed) {
        return super.play(dominoIndex, layout, side, playerPassed);
    }
}
