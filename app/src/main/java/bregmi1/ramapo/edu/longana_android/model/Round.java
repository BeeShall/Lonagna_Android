package bregmi1.ramapo.edu.longana_android.model;

import android.util.Log;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

//implementing as serializable so that it can be passed from TournamentActivity to RoundActivity
public class Round implements Serializable {
    private final int MAX_PIP = 6;
    private Stock stock;
    private Human human;
    private Computer computer;
    private Layout layout;
    private boolean playerPassed;
    private int passCount;

    private boolean computerTurn;

    public Round(){
        this(null,null, -1);
    }

    public Round(Human human, Computer computer, int enginePip){
        stock = new Stock(MAX_PIP);
        this.human = human;
        this.computer = computer;
        this.layout = new Layout(new Domino(enginePip,enginePip));
        passCount = 0;
        playerPassed = false;
    }

    public Vector<Domino> getLayout() {
        return layout.getLayout();
    }

    public Vector<Domino> getStock() {
        return stock.getStock();
    }

    public Vector<Domino> getHumanHand() {
        return human.getHand();
    }

    public Vector<Domino> getComputerHand() {
        return computer.getHand();
    }

    public void init(){
        stock.shuffleStock();
        human.setHand(stock.generateHand());
        computer.setHand(stock.generateHand());

    }

    public String determineFirstPlayer(){
        StringBuilder displayLog = new StringBuilder();
        Domino engine = layout.getEngine();
        while(!playerHasEngine(engine, displayLog)){
            Domino domino = stock.drawDomino();
            human.drawDomino(domino);
            Log.v("Round: ", "Human drew "+domino.toString());
            displayLog.append("Human drew ").append(domino.toString()).append("\n");

            domino = stock.drawDomino();
            computer.drawDomino(domino);
            Log.v("Round: ","Computer drew"+domino.toString());
            displayLog.append("Computer drew ").append(domino.toString()).append("\n");
        }
        human.unsetDominoDrawn();
        computer.unsetDominoDrawn();
        layout.setEngine();
        return displayLog.toString();
    }

    private boolean playerHasEngine(Domino engine, StringBuilder displayLog){
        int dominoIndex;
        if((dominoIndex = human.hasDominoInHand(engine)) >= 0){
            Log.v("Round: ", "Human has the engine!");
            displayLog.append("Human has the Engine! \n");
            human.playDomino(dominoIndex, layout, Side.ANY);
            displayLog.append("Human placed the engine! \n");
            computerTurn = true;
            return true;
        }
        else if((dominoIndex =computer.hasDominoInHand(engine)) >= 0){
            Log.v("Round: ", "Computer has the engine!");
            displayLog.append("Computer has the Engine! \n");
            computer.playDomino(dominoIndex, layout, Side.ANY);
            displayLog.append("It's human's turn!");
            computerTurn = false;
            return true;
        }
        return false;
    }

    public String normalizeTurn() {
        if (computerTurn) {
            computerTurn = false;
            return playComputerMove();
        }
        return null;
    }


    public String play( Domino domino, Side side){
        //humanMove
        if (!human.play(human.hasDominoInHand(domino), layout, side, playerPassed))
            return "Invalid move!";
        resetPass();
        human.unsetDominoDrawn();
        computerTurn = true;
        return null;
        //return null;
    }

    public boolean humanPass() {
        if (human.hasValidMove(layout, playerPassed)) return false;
        if (!human.hasAlreadyDrawn()) return false;
        human.unsetDominoDrawn();
        passPlayer();
        computerTurn = true;
        return true;
    }

    public Domino humanDraw() {
        if (human.hasValidMove(layout, playerPassed)) return null;
        if (human.hasAlreadyDrawn()) return null;
        Log.v("Player drew", "" + human.hasAlreadyDrawn());
        if (stock.isEmpty()) return null;
        Domino domino = stock.drawDomino();
        human.drawDomino(domino);
        return domino;
    }

    public String getHint() {
        Move hint = human.getHint(layout, playerPassed);
        if (hint == null) return null;
        else
            return human.getHint(layout, playerPassed).toString() + "\nHint Strategy: \n" + human.getHintStrategy();

    }


    private String playComputerMove(){
        if (!computer.play(layout, playerPassed)) {
            StringBuilder compMove = new StringBuilder();
            compMove.append("Computer doesn't have any valid moves in hand! \n");

            if (stock.isEmpty()) {
                compMove.append("Stock is empty! So, computer passed!");
                passPlayer();
                return compMove.toString();
            }


            Domino drawnDomino = stock.drawDomino();
            compMove.append("Computer drew ").append(drawnDomino.toString()).append(" from the stock \n");
            computer.drawDomino(drawnDomino);
            if (!computer.play(layout, playerPassed)) {
                compMove.append("Computer still doesn't have a move to play.\n So, Computer passed!");
                passPlayer();
                return compMove.toString();
            }
            resetPass();
            computer.unsetDominoDrawn();
            return compMove.append(computer.getMoveStrategy()).toString();

        }
        resetPass();
        return computer.getMoveStrategy();
    }

    private void passPlayer() {
        playerPassed = true;
        passCount++;
    }

    private void resetPass() {
        playerPassed = false;
        passCount = 0;
    }

    public boolean hasRoundEnded() {
        return (human.isHandEmpty() || computer.isHandEmpty()) || (passCount > 2);
    }

    public String getRoundWinnerAndScore() {
        StringBuilder scores = new StringBuilder();
        //holds sum of all tiles for human
        int humanTotal = human.getHandSum();
        //holds sum of all tiles for computer
        int computerTotal = computer.getHandSum();
        scores.append("Computer Score: ").append(humanTotal)
                .append("\nHuman Score: ").append(computerTotal).append("\n");

        int score = 0;
        //if human emptied the hand , he wins
        if (human.isHandEmpty()) {
            scores.append("Human");
            human.addScore(computerTotal);
            score = computerTotal;
        }
        //if computer emptied the hand, computer wins
        else if (computer.isHandEmpty()) {
            scores.append("Computer");
            computer.addScore(humanTotal);
            score = humanTotal;
        }
        //if human has less sum than computer, human wins
        else if (humanTotal < computerTotal) {
            scores.append("Human");
            human.addScore(computerTotal);
            score = computerTotal;
        }
        //if computer has less sum, computer wins
        else if (humanTotal > computerTotal) {
            scores.append("Computer");
            computer.addScore(humanTotal);
            score = humanTotal;
        }
        //if equal, its a draw
        else {
            scores.append("The round ended with a draw!");
            return scores.toString();
        }
        return scores.append(" won this round with a score of ").append(score).toString();
    }


}
