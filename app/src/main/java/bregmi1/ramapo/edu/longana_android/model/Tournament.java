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
    private static final int MAX_PIP = 7;

    private int tournamentScore;
    private Round currentRound;
    private int roundCount;
    private Human human;
    private Computer computer;

    public Tournament() {
        currentRound = null;
        tournamentScore = 0;
        roundCount = 1;
        human = new Human();
        computer = new Computer();
        this.tournamentScore = 0;
    }

    public int getTournamentScore() {
        return tournamentScore;
    }

    public void setTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }

    public int getCurrentRoundCount() {
        return roundCount - 1;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    private int getEnginePip() {
        return (roundCount % MAX_PIP == 0) ? 0 : (MAX_PIP - (roundCount) % MAX_PIP);
    }

    public Round generateNewRound() {
        currentRound = new Round(human, computer, getEnginePip());
        roundCount++;
        return currentRound;
    }

    public boolean checkIfTournamentEnded() {
        return (human.getScore() > tournamentScore || computer.getScore() > tournamentScore);
    }

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
