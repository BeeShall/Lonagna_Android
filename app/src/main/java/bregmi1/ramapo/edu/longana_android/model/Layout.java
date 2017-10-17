package bregmi1.ramapo.edu.longana_android.model;

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

}
