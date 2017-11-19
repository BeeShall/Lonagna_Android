package bregmi1.ramapo.edu.longana_android.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

/**
 * Created by beeshall on 10/17/17.
 */

//implementing as serializable so that it can be passed from TournamentActivity to RoundActivity
public class Round implements Serializable {
    //maximum number of pip that can appear
    private final int MAX_PIP = 6;
    //boneyard for the round
    private Stock stock;
    //human player for the round
    private Human human;
    //computer player for the round
    private Computer computer;
    //layout for the round
    private Layout layout;
    //indicates if previous player passed
    private boolean playerPassed;
    //to count the number of consecutive passes
    private int passCount;

    //to track if its computer's turn
    private boolean computerTurn;

    /**
     * Default constructor for Round class
     */
    public Round(){
        this(null,null, -1);
    }

    /**
     * Constructor for Round class
     *
     * @param human     human player for the round
     * @param computer  computer player for the round
     * @param enginePip engine for the round (indicating the pip)
     */
    public Round(Human human, Computer computer, int enginePip){
        stock = new Stock(MAX_PIP);
        this.human = human;
        this.computer = computer;
        human.clearHand();
        computer.clearHand();
        this.layout = new Layout(new Domino(enginePip,enginePip));
        passCount = 0;
        playerPassed = false;
    }

    /**
     * Getter for Layout
     *
     * @return copy of the layout vector
     */
    public Vector<Domino> getLayout() {
        return layout.getLayout();
    }

    /**
     * Getter for Stock
     * @return copy of the stock vector
     */
    public Vector<Domino> getStock() {
        return stock.getStock();
    }

    /**
     * Getter for the human hand
     * @return copy of the human hand vector
     */
    public Vector<Domino> getHumanHand() {
        return human.getHand();
    }

    /**
     * Getter for the computer hand
     * @return copy of the computer hand vector
     */
    public Vector<Domino> getComputerHand() {
        return computer.getHand();
    }

    /**
     * To get the score for the given player
     * @param player Class for the player to get the score for
     * @return score for the player
     */
    public int getPlayerScore(Class player) {
        if (player == Computer.class) return computer.getScore();
        else if (player == Human.class) return human.getScore();
        return -1;
    }

    /**
     * Initializes the round
     */
    public void init(){
        if (human.isHandEmpty()) {
            //brand new game
            stock.shuffleStock();
            human.setHand(stock.generateHand());
            computer.setHand(stock.generateHand());
        }
    }

    /**
     * To determine the first player
     *
     * @return string representing the steps involved in determining the first player
     */
    public String determineFirstPlayer() {
        //if the engine has already been set, the first player has already been determined
        if (layout.isEngineSet()) return null;
        StringBuilder displayLog = new StringBuilder();
        Domino engine = layout.getEngine();

        //both player draw the domino until one of them has the engine
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
        //reset the drawn for both the players because, this doesn't involve with the passing rules
        human.unsetDominoDrawn();
        computer.unsetDominoDrawn();
        layout.setEngine();
        return displayLog.toString();
    }

    /**
     * To check any of the players have the engine
     * @param engine the engine of the game to look for
     * @param displayLog to keep tract of the steps involved
     * @return true if any of the players have it, false if not
     */
    private boolean playerHasEngine(Domino engine, StringBuilder displayLog){
        int dominoIndex;
        //if human has the engine
        if((dominoIndex = human.hasDominoInHand(engine)) >= 0){
            Log.v("Round: ", "Human has the engine!");
            displayLog.append("Human has the Engine! \n");

            //human plays the domino and computer's turn is next
            human.playDomino(dominoIndex, layout, Side.ANY);
            displayLog.append("Human placed the engine! \n");
            computerTurn = true;
            return true;
        }
        //if computer has the engine
        else if((dominoIndex =computer.hasDominoInHand(engine)) >= 0){
            Log.v("Round: ", "Computer has the engine!");
            displayLog.append("Computer has the Engine! \n");

            //computer plays the domino and human's turn is next
            computer.playDomino(dominoIndex, layout, Side.ANY);
            displayLog.append("It's human's turn!");
            computerTurn = false;
            return true;
        }
        return false;
    }

    /**\
     * To normalize the turn such that next player is human
     * @return string representing strategy involve in normalizing turn
     */
    public String normalizeTurn() {
        if (computerTurn) {
            computerTurn = false;
            return playComputerMove();
        }
        return null;
    }

    /**
     * To play the non-computer i.e. human move
     *
     * @param domino domino to play
     * @param side   side to play
     * @return string representing steps involved in making the move
     */
    public String play(Domino domino, Side side){
        //humanMove
        if (!human.play(human.hasDominoInHand(domino), layout, side, playerPassed))
            return "Invalid move!";
        resetPass();
        human.unsetDominoDrawn();
        computerTurn = true;
        return null;
        //return null;
    }

    /**
     * Passes the turn for human player
     * @return true if pass was possible, false if not
     */
    public boolean humanPass() {
        if (human.hasValidMove(layout, playerPassed)) return false;
        if (!human.hasAlreadyDrawn()) return false;
        human.unsetDominoDrawn();
        passPlayer();
        computerTurn = true;
        return true;
    }

    /**
     * Draws a domino for the human
     * @return Domino that was drawn , null if not
     */
    public Domino humanDraw() {
        if (human.hasValidMove(layout, playerPassed)) return null;
        if (human.hasAlreadyDrawn()) return null;
        Log.v("Player drew", "" + human.hasAlreadyDrawn());
        if (stock.isEmpty()) {
            human.setDominoDrawn();
            return null;
        }
        Domino domino = stock.drawDomino();
        human.drawDomino(domino);
        return domino;
    }

    /**
     * To get hint for the current game state
     * @return string representing the hint move with the strategy
     */
    public String getHint() {
        Move hint = human.getHint(layout, playerPassed);
        if (hint == null) return null;
        else
            return human.getHint(layout, playerPassed).toString() + "\nHint Strategy: \n" + human.getHintStrategy();

    }

    /**
     * To make the computer move
     * @return String representing the steps involved in playing the computer move
     */
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

    /**
     * To pass the player turn
     */
    private void passPlayer() {
        playerPassed = true;
        passCount++;
    }

    /**
     * To reset the player pass
     */
    private void resetPass() {
        playerPassed = false;
        passCount = 0;
    }

    /**
     * To check if the round has ended
     * @return true is round ended, else false
     */
    public boolean hasRoundEnded() {
        return (human.isHandEmpty() || computer.isHandEmpty()) || (passCount > 2);
    }

    /**
     * To find the round winner and update the scores
     * @return String including the round winner and respective player scores
     */
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

    /**
     * To load round from a saved state
     * @param reader inputstream to read the game from
     */
    public void load(BufferedReader reader) {

        try {
            reader.readLine();
            reader.readLine();
            //computer hand

            String line = reader.readLine();
            String[] lineData = line.split(":");

            computer.setHand(new Hand(getDominosFromString(lineData[1])));

            //computer score
            line = reader.readLine();
            lineData = line.split(":");
            computer.setScore(Integer.parseInt(lineData[1].trim()));

            reader.readLine();
            reader.readLine();

            //human hand
            line = reader.readLine();
            lineData = line.split(":");
            human.setHand(new Hand(getDominosFromString(lineData[1])));

            //human score
            line = reader.readLine();
            lineData = line.split(":");
            human.setScore(Integer.parseInt(lineData[1].trim()));

            reader.readLine();
            reader.readLine();
            line = reader.readLine().trim();
            Log.v("Line:", line);
            line = line.substring(1, line.length() - 2);
            Log.v("Line:", line);
            layout.setLayout(getDominosFromString(line));

            reader.readLine();
            reader.readLine();
            line = reader.readLine().trim();
            stock.setStock(getDominosFromString(line));

            reader.readLine();
            line = reader.readLine();
            lineData = line.split(":");
            lineData[1] = lineData[1].trim().toUpperCase();
            if (lineData[1].equals("YES")) playerPassed = true;
            else if (lineData[1].equals("NO")) playerPassed = false;

            reader.readLine();
            line = reader.readLine();
            lineData = line.split(":");
            lineData[1] = lineData[1].trim().toUpperCase();
            if (lineData[1].equals("HUMAN")) computerTurn = false;
            else if (lineData[1].equals("COMPUTER")) computerTurn = true;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * To translate the string representation of dominos to a vector of Domino Objects
     * @param dominoString string representing the dominos
     * @return vector of parsed dominoes
     */
    private Vector<Domino> getDominosFromString(String dominoString) {
        Log.v("dominostring", dominoString);
        if (dominoString.trim().equals("")) return new Vector<>();
        String[] dominoStrings = dominoString.trim().split(" ");
        Vector<Domino> dominos = new Vector<>();
        for (String s : dominoStrings) {
            s = s.trim();
            dominos.add(new Domino(Character.getNumericValue(s.charAt(0)), Character.getNumericValue(s.charAt(2))));
        }
        return dominos;
    }

    /**
     * To serialize the round state to a string
     * @return String representing the serialized round
     */
    public String serialize() {
        StringBuilder serializedRound = new StringBuilder();
        serializedRound.append("Computer: \n");
        serializedRound.append("\t").append(computer.getserializedHand()).append("\n");
        serializedRound.append("\tScore: ").append(computer.getScore()).append("\n\n");

        serializedRound.append("Human: \n");
        serializedRound.append("\t").append(human.getserializedHand()).append("\n");
        serializedRound.append("\tScore: ").append(human.getScore()).append("\n\n");

        serializedRound.append(layout.toString()).append("\n\n");

        serializedRound.append(stock.toString()).append("\n\n");

        if (layout.isEngineSet()) {

            serializedRound.append("Previous Player Passed: ").append((playerPassed) ? "Yes" : "No").append("\n\n");

            serializedRound.append("Next Player: ").append((computerTurn) ? "Computer" : "Human").append("\n");
        } else {
            serializedRound.append("Previous Player Passed: ").append("\n\n");

            serializedRound.append("Next Player: ").append("\n");
        }


        return serializedRound.toString();
    }



}
