package bregmi1.ramapo.edu.longana_android.model;

import android.util.Log;

import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

public class Round {
    private final int MAX_PIP = 6;
    private Stock stock;
    private Human human;
    private Computer computer;
    private Layout layout;
    private boolean playerPassed;
    private int passCount;
    private boolean roundEnded;

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
        roundEnded = false;
    }

    public Vector<Domino> getLayout() {
        return layout.getLayout();
    }

    public Vector<Domino> getStock() {
        return stock.getStock();
    }

    public boolean getRoundEnded(){
        return roundEnded;
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
        playerPassed = false;
        human.unsetDominoDrawn();
        computerTurn = true;
        return null;
        //return null;
    }

    public boolean playerPass(Player player) {
        if (player.hasValidMove(layout, playerPassed)) return false;
        if (!player.hasAlreadyDrawn()) return false;
        player.unsetDominoDrawn();
        playerPassed = true;
        computerTurn = true;
        return true;
    }

    public Domino playerDraw(Player player) {
        if (player.hasValidMove(layout, playerPassed)) return null;
        if (player.hasAlreadyDrawn()) return null;
        Log.v("Player drew", "" + player.hasAlreadyDrawn());
        if (stock.isEmpty()) return null;
        Domino domino = stock.drawDomino();
        player.drawDomino(domino);
        return domino;
    }

    public String getHint() {
        Move hint = human.getHint(layout, playerPassed);
        if (hint == null) return null;
        else
            return new StringBuilder().append(human.getHint(layout, playerPassed).toString()).append("\nHint Strategy: \n").append(human.getHintStrategy()).toString();

    }


    private String playComputerMove(){
        if (!computer.play(layout, playerPassed)) {
            StringBuilder compMove = new StringBuilder();
            compMove.append("Computer doesn't have any valid moves in hand! \n");

            if (stock.isEmpty()) {
                compMove.append("Stock is empty! So, computer passed!");
                playerPassed = true;
                return compMove.toString();
            }


            Domino drawnDomino = stock.drawDomino();
            compMove.append("Computer drew ").append(drawnDomino.toString()).append(" from the stock \n");
            computer.drawDomino(drawnDomino);
            if (!computer.play(layout, playerPassed)) {
                compMove.append("Computer still doesn't have a move to play.\n So, Computer passed!");
                playerPassed = true;
                return compMove.toString();
            }
            playerPassed = false;
            computer.unsetDominoDrawn();
            return compMove.append(computer.getMoveStrategy()).toString();

        }
        playerPassed = false;
        return computer.getMoveStrategy();
    }

    private boolean hasRoundEnded(){
        roundEnded = (human.isHandEmpty() || computer.isHandEmpty()) || (passCount > 2);
        return roundEnded;
    }

}
