package bregmi1.ramapo.edu.longana_android.model;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Layout {
    private Vector<Domino> left;
    private Domino engine;
    private Vector<Domino> right;

    public Layout() {
        left = new Vector();
        right = new Vector<>();
        engine = null;
    }

    public Layout(Domino engine){
        this.engine = engine;
        left = new Vector();
        right = new Vector<>();
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

    public boolean placeDomino(Domino domino, Side side){
        if(engine==null) return false;
        Domino validatedDomino = checkIfDominoCanBePlaced(domino,side);
        if(validatedDomino == null) return false;
        if(side == Side.LEFT) left.add(domino);
        else if(side == Side.RIGHT) right.add(domino);
        return true;
    }

    private Domino checkIfDominoCanBePlaced(Domino domino, Side side){
        if(engine == null) return null;
        if(side == Side.LEFT){
            int leftDominoPip = (left.isEmpty()) ? engine.getPip1() : left.lastElement().getPip1();
            if(leftDominoPip == domino.getPip2()) return domino;
            else if(leftDominoPip == domino.getPip1()) {
                domino.flip();
                return domino;
            }
        }
        else if(side == Side.RIGHT){
            int rightDominoPip = (right.isEmpty())? engine.getPip1() : right.lastElement().getPip2();
            if(rightDominoPip == domino.getPip1()) return domino;
            else if(rightDominoPip == domino.getPip2()) {
                domino.flip();
                return domino;
            }
        }
        else return null;
        return null;
    }

}
