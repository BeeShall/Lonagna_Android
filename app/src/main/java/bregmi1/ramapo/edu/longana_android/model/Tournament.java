package bregmi1.ramapo.edu.longana_android.model;

/**
 * Created by beeshall on 10/17/17.
 */

public class Tournament {
    private static final int MAX_PIP = 6;

    private int tournamentScore;
    private Round currentRound;
    private int roundCount;
    private Human human;
    private Computer computer;

    public Tournament() {
        currentRound = null;
        tournamentScore = 0;
        roundCount = 0;
        human = new Human();
        computer = new Computer();
        this.tournamentScore = tournamentScore;
    }

    public int getTournamentScore() {
        return tournamentScore;
    }

    public void setTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public Round generateNewRound() {
        int enginePip = (roundCount % MAX_PIP == 0) ? 0 : (MAX_PIP - (roundCount) % MAX_PIP);
        currentRound = new Round(human, computer, enginePip);
        roundCount++;
        return currentRound;
    }

    public boolean checkIfTournamentEnded() {
        return (human.getScore() > tournamentScore || computer.getScore() > tournamentScore);
    }

    public String getWinner() {
        int humanScore = human.getScore();
        int computerScore = computer.getScore();
        //if human has greater score, human is winner
        if (humanScore > computerScore) {
            return "Human";
        }
        //else computer is winner
        else {
            return "Computer";
        }
    }


    public void load() {

    }


}
