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
        layout.setEngine();
        return displayLog.toString();
    }

    private boolean playerHasEngine(Domino engine, StringBuilder displayLog){
        int dominoIndex;
        if((dominoIndex = human.hasDominoInHand(engine)) >= 0){
            Log.v("Round: ", "Human has the engine!");
            displayLog.append("Human has the Engine! \n");
            human.play(dominoIndex,layout,Side.ANY, playerPassed);
            return true;
        }
        else if((dominoIndex =computer.hasDominoInHand(engine)) >= 0){
            Log.v("Round: ", "Computer has the engine!");
            displayLog.append("Computer has the Engine! \n");
            computer.play(dominoIndex,layout,Side.ANY, playerPassed);
            return true;
        }
        return false;
    }


    public String play( Domino domino, Side side){
        //humanMove
        String message = human.play(human.hasDominoInHand(domino),layout,side, playerPassed);
        if(message != null) return message;
        if(hasRoundEnded()) return null;
        message = playComputerMove();

        if(hasRoundEnded()) return null;
        return "";
    }

    private String playComputerMove(){
        return computer.play(layout, playerPassed);
    }

    private boolean hasRoundEnded(){
        roundEnded = (human.isHandEmpty() || computer.isHandEmpty()) || (passCount > 2);
        return roundEnded;
    }

}
