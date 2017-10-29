package bregmi1.ramapo.edu.longana_android.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Player {
    protected int score;
    protected Hand hand;
    protected Side side;
    protected Side otherSide;
    protected String hintStrategy;

    public Player(){
        this(0,null);
    }

    public Player(int score, Hand hand){
        this.score=score;
        this.hand=hand;
        this.hintStrategy = new String("");
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public boolean isHandEmpty(){
        return hand.isEmpty();
    }

    public void drawDomino(Domino domino){
        hand.add(domino);
    }

    public int hasDominoInHand(Domino domino){
        return hand.getDominoIndex(domino);
    }

    public String play(int dominoIndex, Layout layout, Side side, boolean playerPassed ){
        Domino domino = hand.getDomino(dominoIndex);
        if(domino.equals(layout.getEngine())) layout.setEngine();
        else{
            if(side != this.side) return "Invalid Side!";
            String message = "";
            if((message = layout.placeDomino(domino,side)) != null) return message;
        }
        hand.playDomino(dominoIndex);
        return null;
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
                return o1.getDomino().getSum() - o2.getDomino().getSum();
            }
        });

        //the domino with the highest sum is the best play
        Move returnMove = possibleMoves.firstElement();
        hintStrat.append("This domino decreases the total sum on the hand by ");
        hintStrat.append(+returnMove.getDomino().getSum());
        hintStrat.append("\n");

        //However, if the domino can be placed on either side (passed or double)
        if (returnMove.getSide() == Side.ANY) {
            hintStrat.append("However, this domino can be placed on either side!");

            int leftPlacementScore = getBestScoreforNextMove(layout, new Move(returnMove.getDomino(), Side.LEFT));
            int rightPlacementScore = getBestScoreforNextMove(layout, new Move(returnMove.getDomino(), Side.RIGHT));

            if (leftPlacementScore > rightPlacementScore) {
                hintStrat.append("If placed on LEFT, the next best domino decreases the total sum on the hand by ");
                hintStrat.append(leftPlacementScore);
                hintStrat.append(" rather than placing on the RIGHT which would decrease by ");
                hintStrat.append(rightPlacementScore);
                hintStrat.append("\n");
                returnMove.setSide(Side.LEFT);
            } else if (rightPlacementScore > leftPlacementScore) {
                hintStrat.append("If placed on RIGHT, the next best domino decreases the total sum on the hand by ");
                hintStrat.append(rightPlacementScore);
                hintStrat.append(" rather than placing on the LEFT which would decrease by ");
                hintStrat.append(leftPlacementScore);
                hintStrat.append("\n");
                returnMove.setSide(Side.RIGHT);
            } else {
                hintStrat.append("This domino will decreases the total sum on the hand by ");
                hintStrat.append(leftPlacementScore);
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

        if (move.getDomino().equals(moves.firstElement()))
            bestScore = moves.get(1).getDomino().getSum();
        else bestScore = moves.firstElement().getDomino().getSum();

        layout.undo(move.getSide());
        return bestScore;
    }





}
