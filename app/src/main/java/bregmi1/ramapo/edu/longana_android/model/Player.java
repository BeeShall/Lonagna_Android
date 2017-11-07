package bregmi1.ramapo.edu.longana_android.model;

import android.util.Log;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

class Player implements Serializable {
    protected Hand hand;
    Side side;
    Side otherSide;
    String hintStrategy;
    private boolean dominoDrawn;
    private int score;

    Player() {
        this(0,null);
    }

    public Player(int score, Hand hand){
        this.score=score;
        this.hand=hand;
        this.dominoDrawn = false;
        this.hintStrategy = "";
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    boolean hasAlreadyDrawn() {
        return dominoDrawn;
    }

    public void setDominoDrawn() {
        dominoDrawn = true;
    }

    void unsetDominoDrawn() {
        dominoDrawn = false;
    }

    public Vector<Domino> getHand() {
        return hand.getHand();
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void addScore(int score) {
        this.score += score;
    }

    boolean isHandEmpty() {
        return hand == null || hand.isEmpty();
    }

    public int getHandSum() {
        return hand.getHandSum();
    }

    public String getserializedHand() {
        return hand.toString();
    }

    boolean hasValidMove(Layout layout, boolean playerPassed) {
        for (int i = 0; i < hand.getHandSize(); i++) {
            if (layout.canDominoBePlaced(hand.getDomino(i), side)) {
                Log.v("Domino ", "" + side);
                return true;
            }
            if (playerPassed || hand.getDomino(i).isDouble()) {
                if (layout.canDominoBePlaced(hand.getDomino(i), otherSide)) {
                    Log.v("Domino", "" + otherSide);
                    return true;
                }
            }
        }
        Log.v("Player", "doesn't have any valid move`");
        return false;
    }

    void drawDomino(Domino domino) {
        setDominoDrawn();
        hand.add(domino);
    }

    int hasDominoInHand(Domino domino) {
        return hand.getDominoIndex(domino);
    }

    boolean playDomino(int dominoIndex, Layout layout, Side side) {
        Domino domino = hand.getDomino(dominoIndex);
        if (domino.equals(layout.getEngine())) {
            layout.setEngine();
            hand.playDomino(dominoIndex);
            return true;
        }
        if (layout.placeDomino(domino, side)) {
            hand.playDomino(dominoIndex);
            return true;
        }
        return false;
    }

    public boolean play(int dominoIndex, Layout layout, Side side, boolean playerPassed) {
        Domino domino = hand.getDomino(dominoIndex);
        if (layout.isEngineSet()) {
            Log.v("Placing domino", "" + domino.isDouble() + " " + playerPassed);
            if (side == otherSide && (!domino.isDouble() && !playerPassed)) return false;
            return playDomino(dominoIndex, layout, side);
        }
        return false;
    }

    protected Move hint(Layout layout, boolean playerPassed) {
        StringBuilder hintStrat = new StringBuilder();
        //getiing the list of all the possible moves given this state of the game
        Vector<Move> possibleMoves = getAllPossibleMoves(layout, playerPassed);

        //if there areno possible moves, then we don't have a hint
        if (possibleMoves.isEmpty()) return null;

        //sorting the moves the domino with the highest sum comes to the front
        Collections.sort(possibleMoves, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                return o2.getDomino().getSum() - o1.getDomino().getSum();
            }
        });

        for (Move v : possibleMoves) {
            Log.v("possibleDomino", v.getDomino().toString());
        }

        //the domino with the highest sum is the best play
        Move returnMove = possibleMoves.firstElement();
        hintStrat.append(returnMove.getDomino().toString()).append(" decreases the total sum on the hand by ").append(returnMove.getDomino().getSum()).append("\n");

        //However, if the domino can be placed on either side (passed or double)
        if (returnMove.getSide() == Side.ANY) {
            hintStrat.append("However, this domino can be placed on either side!");

            int leftPlacementScore = getBestScoreforNextMove(layout, new Move(returnMove.getDomino(), Side.LEFT));
            int rightPlacementScore = getBestScoreforNextMove(layout, new Move(returnMove.getDomino(), Side.RIGHT));

            if (leftPlacementScore > rightPlacementScore) {
                hintStrat.append("If placed on LEFT, the next best domino decreases the total sum on the hand by ").append(leftPlacementScore);
                hintStrat.append(" rather than placing on the RIGHT which would decrease by ").append(rightPlacementScore).append("\n");
                returnMove.setSide(Side.LEFT);
            } else if (rightPlacementScore > leftPlacementScore) {
                hintStrat.append("If placed on RIGHT, the next best domino decreases the total sum on the hand by ").append(rightPlacementScore);
                hintStrat.append(" rather than placing on the LEFT which would decrease by ").append(leftPlacementScore).append("\n");
                returnMove.setSide(Side.RIGHT);
            } else {
                hintStrat.append("This domino will decreases the total sum on the hand by ").append(leftPlacementScore);
                hintStrat.append(" either way, So placing it on the otherSide would create more chances of screwing the opponent over!\n");
                returnMove.setSide(otherSide);
            }
        }

        hintStrategy = hintStrat.toString();
        return returnMove;
    }

    private Vector<Move> getAllPossibleMoves(Layout layout, boolean playerPassed) {
        Vector<Move> possibleMoves = new Vector<>();
        for (int i = 0; i < hand.getHandSize(); i++) {
            Domino domino = hand.getDomino(i);
            Move move = null;
            //if the domino can be placed on the regular side
            if (layout.isMoveValid(domino, side)) move = new Move(domino, side);
            //if previous player passed or the domino is a double, things change
            if (playerPassed || domino.isDouble()) {
                if (layout.isMoveValid(domino, otherSide)) {
                    //if the move can be placed in the other side
                    //if it can be placed in the regukar side, then it can be placed on any side
                    if (move != null) move.setSide(Side.ANY);
                        //else it can only be placed on the other side
                    else move = new Move(domino, otherSide);
                }
            }

            if (move != null) possibleMoves.add(move);
        }
        return possibleMoves;
    }

    private int getBestScoreforNextMove(Layout layout, Move move) {
        layout.placeDomino(move.getDomino(), move.getSide());

        Vector<Move> moves = getAllPossibleMoves(layout, false);
        if (moves.isEmpty()) return 0;

        int bestScore = 0;
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                return o1.getDomino().getSum() - o2.getDomino().getSum();
            }
        });

        if (move.getDomino().equals(moves.firstElement().getDomino()))
            bestScore = moves.get(1).getDomino().getSum();
        else bestScore = moves.firstElement().getDomino().getSum();

        layout.undo(move.getSide());
        return bestScore;
    }





}
