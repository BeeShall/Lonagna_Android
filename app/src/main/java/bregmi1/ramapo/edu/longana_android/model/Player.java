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
    //hand of the player
    protected Hand hand;
    //side the player will be playing on
    Side side;
    //opposite side of the player
    Side otherSide;
    //to store the hint strategy for human or move strategy for computer
    String hintStrategy;
    //indicates if the player drew from stock
    private boolean dominoDrawn;
    //score of the player
    private int score;

    /**
     * Default constructor for player
     */
    Player() {
        this(0,null);
    }

    /**
     * Contructor for player
     *
     * @param score score for the player
     * @param hand  hand for the player
     */
    public Player(int score, Hand hand){
        this.score=score;
        this.hand=hand;
        this.dominoDrawn = false;
        this.hintStrategy = "";
    }

    /**
     * Getter for the player score
     *
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter for the player score
     * @param score score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * To get the player hand vector
     *
     * @return copy of player hand vector
     */
    public Vector<Domino> getHand() {
        return hand.getHand();
    }

    /**
     * To set the hand for the player
     *
     * @param hand hand object for the player hadn
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * to get the sum of all pips in hand
     *
     * @return sum of all pips in hand
     */
    public int getHandSum() {
        return hand.getHandSum();
    }

    /**
     * To check if player has drawn from stock
     * @return true if player has already done, else false.
     */
    boolean hasAlreadyDrawn() {
        return dominoDrawn;
    }

    /**
     * To set that the player has already drawn
     */
    public void setDominoDrawn() {
        dominoDrawn = true;
    }

    /**
     * To unset that player has already drawn
     */
    void unsetDominoDrawn() {
        dominoDrawn = false;
    }

    /**
     * To add score to the existing player score
     * @param score score to add
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * To check if the player hand is empty
     * @return true if player hand is empty, else false
     */
    boolean isHandEmpty() {
        return hand == null || hand.isEmpty();
    }

    /**
     * To get stringified value of player hand
     * @return string representing the player hand
     */
    public String getserializedHand() {
        return hand.toString();
    }

    /**
     * To check if player had a valid move that he/she can play
     * @param layout layout to play in
     * @param playerPassed indicates whether previous player passed
     * @return true if player has valid move, false if not.
     */
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

    /**
     * draw the domino from stock. adds domino to the hand
     * @param domino domino drawn from the stock
     */
    void drawDomino(Domino domino) {
        setDominoDrawn();
        hand.add(domino);
    }

    void clearHand(){
        hand=null;
    }

    /**
     * To check if the domino exists in hand
     * @param domino domino to loolk for
     * @return true if domino exists, false if not.
     */
    int hasDominoInHand(Domino domino) {
        return hand.getDominoIndex(domino);
    }

    /**
     * To play the domino i.e. place the domino in layour and remove from hand
     * @param dominoIndex domino index in hand for the domino
     * @param layout layout to play on
     * @param side side to play on
     * @return true if played, false if errors
     */
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

    /**
     * To validate and play the move
     * @param dominoIndex domino index in hand for the domino
     * @param layout layout to play on
     * @param side side to play on
     * @param playerPassed indicates whether the previous player passed
     * @return true if move was possible, false if not.
     */
    public boolean play(int dominoIndex, Layout layout, Side side, boolean playerPassed) {
        Domino domino = hand.getDomino(dominoIndex);
        if (layout.isEngineSet()) {
            Log.v("Placing domino", "" + domino.isDouble() + " " + playerPassed);
            if (side == otherSide && (!domino.isDouble() && !playerPassed)) return false;
            return playDomino(dominoIndex, layout, side);
        }
        return false;
    }

    /**
     * To get the hint for the player for the current game state
     * @param layout layout to look the hint for
     * @param playerPassed indicates whether previous player passed
     * @return Move if hint exists, null if no hints.
     */
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

    /**
     * To get all the possible moves that can be made given the current game state
     * @param layout layout of the current game state
     * @param playerPassed indicates whether previous player passed
     * @return vector of moves that can be made
     */
    private Vector<Move> getAllPossibleMoves(Layout layout, boolean playerPassed) {
        Vector<Move> possibleMoves = new Vector<>();
        for (int i = 0; i < hand.getHandSize(); i++) {
            Domino domino = hand.getDomino(i);
            Move move = null;
            //if the domino can be placed on the regular side
            if (layout.canDominoBePlaced(domino, side)) move = new Move(domino, side);
            //if previous player passed or the domino is a double, things change
            if (playerPassed || domino.isDouble()) {
                if (layout.canDominoBePlaced(domino, otherSide)) {
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

    /**
     * To get the best score for next move after placing the given domino
     * @param layout layout of the current game state
     * @param move next move to make
     * @return highest sum that can be yielded
     */
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

        if (move.getDomino().equals(moves.firstElement().getDomino())) {
            if (moves.size() > 2) {
                bestScore = moves.get(1).getDomino().getSum();
            } else return 0;
        } else bestScore = moves.firstElement().getDomino().getSum();

        layout.undo(move.getSide());
        return bestScore;
    }





}
