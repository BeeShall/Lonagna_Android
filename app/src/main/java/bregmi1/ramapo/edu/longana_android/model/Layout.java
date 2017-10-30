package bregmi1.ramapo.edu.longana_android.model;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Layout {
    private Vector<Domino> left;
    private Domino engine;
    private boolean engineSet;
    private Vector<Domino> right;

    public Layout() {
        this(null);
    }

    public Layout(Domino engine){
        this.engine = engine;
        engineSet = false;
        left = new Vector();
        right = new Vector<>();
    }

    Domino getEngine() {
        return this.engine;
    }

    void setEngine() {
        engineSet = true;
    }

    boolean isEngineSet() {
        return engineSet;
    }

    public Vector<Domino> getLayout() {
        Vector<Domino> layout = new Vector<>();
        if (!engineSet) return layout;
        layout.addAll(left);
        Collections.reverse(layout);
        layout.add(engine);
        layout.addAll(right);
        return (Vector<Domino>) layout.clone();
    }


    public void setLayout(Vector<Domino> dominoes){
        if(dominoes.isEmpty()) return;
        left.clear();
        right.clear();

        int index =0;
        while(!dominoes.elementAt(index).equals(engine)) left.add(dominoes.elementAt(index++));
        Collections.reverse(left);
        index++; //skipping the engine
        while(index<dominoes.size()) right.add(dominoes.elementAt(index++));
    }

    public boolean isSideEmpty(Side side){
        if(side == Side.LEFT) return left.isEmpty();
        else if(side == Side.RIGHT ) return right.isEmpty();
        else return true;
    }

    boolean isMoveValid(Domino domino, Side side) {
        if (!engineSet) return false;
        return checkIfDominoCanBePlaced(domino, side) != null;
    }

    boolean placeDomino(Domino domino, Side side) {
        if (!engineSet) return false;
        Domino validatedDomino = checkIfDominoCanBePlaced(domino,side);
        if (validatedDomino == null) return false;
        if (side == Side.LEFT) left.add(validatedDomino);
        else if (side == Side.RIGHT) right.add(validatedDomino);
        return true;
    }

    boolean canDominoBePlaced(Domino domino, Side side) {
        if (!engineSet) return false;
        return checkIfDominoCanBePlaced(domino, side) != null;
    }

    void undo(Side side) {
        if (side == Side.LEFT) left.removeElementAt(left.size() - 1);
        if (side == Side.RIGHT) right.removeElementAt(right.size() - 1);
    }

    private Domino checkIfDominoCanBePlaced(Domino domino, Side side){
        if(!engineSet) return null;
        if(side == Side.LEFT){
            int leftDominoPip = (left.isEmpty()) ? engine.getPip1() : left.lastElement().getPip1();
            if(leftDominoPip == domino.getPip2()) return domino;
            else if(leftDominoPip == domino.getPip1()) {
                return domino.flip();
            }
        }
        else if(side == Side.RIGHT){
            int rightDominoPip = (right.isEmpty())? engine.getPip1() : right.lastElement().getPip2();
            if(rightDominoPip == domino.getPip1()) return domino;
            else if(rightDominoPip == domino.getPip2()) {
                return domino.flip();
            }
        }
        else return null;
        return null;
    }

}
