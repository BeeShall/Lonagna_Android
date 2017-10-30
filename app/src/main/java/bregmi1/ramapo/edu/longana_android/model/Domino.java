package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Domino {
    private int pip1;
    private int pip2;
    private boolean doubleDomino;

    public Domino(){
        pip1 = -1;
        pip2 = -1;
    }

    Domino(int pip1, int pip2) {
        this.pip1 = pip1;
        this.pip2 = pip2;
        this.doubleDomino = (pip1 == pip2);
    }

    public int getPip1() {
        return pip1;
    }

    public int getPip2() {
        return pip2;
    }

    public boolean isDouble() {
        return this.doubleDomino;
    }

    Domino flip() {
        return new Domino(pip2, pip1);
    }

    int getSum() {
        return pip1+pip2;
    }

    boolean equals(Domino domino) {
        return ((pip1 == domino.pip1) &&  (pip2 == domino.pip2)) || ((pip1 == domino.pip2) &&  (pip2 == domino.pip1));
    }

    public String toString(){
        return pip1 + "-" + pip2;
    }

}
