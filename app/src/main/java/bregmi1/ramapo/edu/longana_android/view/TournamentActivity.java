package bregmi1.ramapo.edu.longana_android.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bregmi1.ramapo.edu.longana_android.R;
import bregmi1.ramapo.edu.longana_android.model.Round;
import bregmi1.ramapo.edu.longana_android.model.Tournament;

public class TournamentActivity extends Activity {

    Tournament tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        tournament = new Tournament();

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if tournament has to be loaded or freshly started
                AlertDialog.Builder messages = new AlertDialog.Builder(TournamentActivity.this);
                messages.setMessage("Would you like to load a game?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //load the tournament
                                //get the round and start the roundActivity
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //ask for the tournament score
                                AlertDialog.Builder scoreAlert = new AlertDialog.Builder(TournamentActivity.this);
                                final EditText scoreText = new EditText(TournamentActivity.this);
                                scoreAlert.setTitle("Please enter a tournament score:")
                                        .setView(scoreText)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tournament.setTournamentScore(Integer.parseInt(scoreText.getText().toString()));
                                                launchNewRound();
                                            }
                                        });
                                scoreAlert.show();
                            }
                        });
                messages.show();
            }
        });
    }

    private void launchNewRound() {
        Round round = tournament.generateNewRound();
        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra("round", round);
        intent.putExtra("tournamentScore", tournament.getTournamentScore());
        intent.putExtra("roundCount", tournament.getCurrentRoundCount());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (tournament.checkIfTournamentEnded()) {
            AlertDialog.Builder messages = new AlertDialog.Builder(this);
            messages.setMessage("Tournament Ended!\n" + tournament.getWinnerAndScores())
                    .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            //save and quit, normalize the firstPlayer
            messages.show();
        } else {
            launchNewRound();
        }
    }
}
