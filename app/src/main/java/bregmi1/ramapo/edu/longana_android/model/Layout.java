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

    public Domino getEngine(){
        return this.engine;
    }

    public void setEngine(){
        engineSet = true;
    }

    public Vector<Domino> getLayout() {
        Vector<Domino> layout = new Vector<>();
        if (!engineSet) return layout;
        layout.addAll(left);
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

    public boolean isMoveValid(Domino domino, Side side) {
        if (!engineSet) return false;
        return checkIfDominoCanBePlaced(domino, side) != null;
    }

    public String placeDomino(Domino domino, Side side){

        if(!engineSet) return "Engine has not been set yet!";
        Domino validatedDomino = checkIfDominoCanBePlaced(domino,side);
        if(validatedDomino == null) return "Invalid move!";
        if(side == Side.LEFT) left.add(domino);
        else if(side == Side.RIGHT) right.add(domino);
        return null;
    }

    public void undo(Side side) {
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
