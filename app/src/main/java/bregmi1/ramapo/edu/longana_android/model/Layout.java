package bregmi1.ramapo.edu.longana_android.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Layout implements Serializable {
    //container storing the dominos on the left side of the engine
    private Vector<Domino> left;
    //engine for the round
    private Domino engine;
    //indicates whether engine has been set yet
    private boolean engineSet;
    //containers storing the dominoes on the right side of the engine
    private Vector<Domino> right;

    /**
     * Default constructor for Layout
     */
    public Layout() {
        this(null);
    }

    /**
     * Constructor for Layout
     *
     * @param engine engine for the round
     */
    public Layout(Domino engine){
        this.engine = engine;
        engineSet = false;
        left = new Vector();
        right = new Vector<>();
    }

    /**
     * Getter for the engine
     *
     * @return the engine of the round
     */
    Domino getEngine() {
        return this.engine;
    }

    void setEngine() {
        engineSet = true;
    }

    /**
     * To check if engine has been set
     * @return true is engine has been set else false.
     */
    boolean isEngineSet() {
        return engineSet;
    }

    /**
     * To get a single vector representing the entire layout
     * @return vector with all the dominoes in the layout
     */
    public Vector<Domino> getLayout() {
        Vector<Domino> layout = new Vector<>();
        if (!engineSet) return layout;
        layout.addAll(left);
        Collections.reverse(layout);
        layout.add(engine);
        layout.addAll(right);
        return (Vector<Domino>) layout.clone();
    }

    /**
     * To set the layout with the given dominos
     * @param dominoes vector of dominoes to set the layout with
     */
    public void setLayout(Vector<Domino> dominoes){
        if(dominoes.isEmpty()) return;
        engineSet = true;
        left.clear();
        right.clear();

        int index =0;
        while(!dominoes.elementAt(index).equals(engine)) left.add(dominoes.elementAt(index++));
        Collections.reverse(left);
        index++; //skipping the engine
        while(index<dominoes.size()) right.add(dominoes.elementAt(index++));
    }

    /**
     * To check if the side of the engine is empty
     * @param side side to check for
     * @return true if side is empty, false if not
     */
    public boolean isSideEmpty(Side side){
        if(side == Side.LEFT) return left.isEmpty();
        else if(side == Side.RIGHT ) return right.isEmpty();
        else return true;
    }

    /**
     * To place a domino in the layout
     * @param domino domino to place
     * @param side side to place
     * @return true if placed, false if not
     */
    boolean placeDomino(Domino domino, Side side) {
        if (!engineSet) return false;
        Domino validatedDomino = checkIfDominoCanBePlaced(domino,side);
        if (validatedDomino == null) return false;
        if (side == Side.LEFT) left.add(validatedDomino);
        else if (side == Side.RIGHT) right.add(validatedDomino);
        return true;
    }

    /**
     * To check if the domino is placeable int the given side
     * @param domino domino to check for
     * @param side side to check for
     * @return true if valid false if not
     */
    boolean canDominoBePlaced(Domino domino, Side side) {
        if (!engineSet) return false;
        return checkIfDominoCanBePlaced(domino, side) != null;
    }

    /**
     *To remove the last domino from the given side
     * @param side side to remove the domino from
     */
    void undo(Side side) {
        if (side == Side.LEFT) left.removeElementAt(left.size() - 1);
        if (side == Side.RIGHT) right.removeElementAt(right.size() - 1);
    }

    /**
     * To check if a domino can be placed in the given side
     * @param domino domino to place
     * @param side side to place
     * @return domino with correct orientation (flipped if needed)
     */
    private Domino checkIfDominoCanBePlaced(Domino domino, Side side){
        if(!engineSet) return null;
        if (side == Side.LEFT) {
            //if side is left
            int leftDominoPip = (left.isEmpty()) ? engine.getPip1() : left.lastElement().getPip1();
            if(leftDominoPip == domino.getPip2()) return domino;
                //if the matching pip is in the opposite side, flip the domino
            else if(leftDominoPip == domino.getPip1()) {
                return domino.flip();
            }
        } else if (side == Side.RIGHT) {
            //if side is right
            int rightDominoPip = (right.isEmpty())? engine.getPip1() : right.lastElement().getPip2();
            if(rightDominoPip == domino.getPip1()) return domino;
                //if the matching pip is in the opposite side, flip the domino
            else if(rightDominoPip == domino.getPip2()) {
                return domino.flip();
            }
        } else return null;
        return null;
    }

    /**
     * To get the strigified versiojn of layout
     * @return string representing the layout
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Layout:\nL ");
        for (int i = left.size() - 1; i >= 0; --i) {
            builder.append(left.elementAt(i).toString()).append(" ");
        }
        builder.append(engine.toString()).append(" ");
        for (Domino d : right) {
            builder.append(d.toString()).append(" ");
        }
        builder.append("R");
        return builder.toString();

    }
}
