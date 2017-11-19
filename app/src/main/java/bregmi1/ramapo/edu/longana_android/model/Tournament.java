package bregmi1.ramapo.edu.longana_android.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by beeshall on 10/17/17.
 */

public class Tournament {
    //maximum number of pips
    private static final int MAX_PIP = 7;
    //goal score for the tournament
    private int tournamentScore;
    //holds the round running currently
    private Round currentRound;
    //holds the counts of round
    private int roundCount;
    //human player for the tournament
    private Human human;
    //computer player for the tournamnet
    private Computer computer;

    /**
     * Default constructor for the Tournament
     */
    public Tournament() {
        currentRound = null;
        tournamentScore = 0;
        roundCount = 1;
        human = new Human();
        computer = new Computer();
        this.tournamentScore = 0;
    }

    /**
     * Getter for tournament score
     *
     * @return torunament score
     */
    public int getTournamentScore() {
        return tournamentScore;
    }

    /**
     * Setter for tournament score
     *
     * @param tournamentScore
     */
    public void setTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }

    /**
     * Getter for round count
     * @return return the current round count
     */
    public int getCurrentRoundCount() {
        return roundCount - 1;
    }

    /**
     * Getter for current round
     * @return current round
     */
    public Round getCurrentRound() {
        return currentRound;
    }

    /**
     * To get the pip for the engine for the respective round
     * @return pip for the engine
     */
    private int getEnginePip() {
        return (roundCount % MAX_PIP == 0) ? 0 : (MAX_PIP - (roundCount) % MAX_PIP);
    }

    /**
     * To generate a new Round
     * @return a new round
     */
    public Round generateNewRound() {
        currentRound = new Round(human, computer, getEnginePip());
        roundCount++;
        return currentRound;
    }

    /**
     * To check if tournament has ended
     * @return true if tournament has ened, false if not
     */
    public boolean checkIfTournamentEnded() {
        return (human.getScore() > tournamentScore || computer.getScore() > tournamentScore);
    }

    /**
     * To calculate the tournament scores and the winner
     * @return String representing tournament scores and the winner
     */
    public String getWinnerAndScores() {
        StringBuilder scores = new StringBuilder();
        int humanScore = human.getScore();
        int computerScore = computer.getScore();
        scores.append("Human Score: ").append(humanScore)
                .append("\nComputer Score: ").append(computerScore);
        //if human has greater score, human is winner
        if (humanScore > computerScore) {
            return scores.append("\nHuman won the tournament!").toString();
        }
        //else computer is winner
        else {
            return scores.append("\nComputer won the tournament!").toString();
        }
    }

    public void setPlayerScore(Class player, int score){
        if(player == Human.class) human.setScore(score);
        else computer.setScore(score);
    }

    /**
     * To load a round from inputstream
     * @param is InputStream to load the round from
     * @return Round with the loaded data
     */
    public Round load(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line = reader.readLine();
            String[] lineData = line.split(":");
            this.tournamentScore = Integer.parseInt(lineData[1].trim());

            line = reader.readLine();
            lineData = line.split(":");
            this.roundCount = Integer.parseInt(lineData[1].trim());
            currentRound = new Round(human, computer, getEnginePip());
            currentRound.load(reader);
            roundCount++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentRound;
    }

    /**
     * To serialize the game to the give file
     * @param file file to serialize to
     * @param data additional data to write to
     * @return true if successful, false if not.
     */
    public boolean serialize(File file, String data) {
        StringBuilder builder = new StringBuilder();
        builder.append("Tournament Score: ").append(tournamentScore).append("\n")
                .append("Round Count: ").append(roundCount - 1).append("\n\n")
                .append(data);

        Log.v("serialize", builder.toString());

        try {
            FileOutputStream os = new FileOutputStream(file);
            os.write(builder.toString().getBytes());
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
