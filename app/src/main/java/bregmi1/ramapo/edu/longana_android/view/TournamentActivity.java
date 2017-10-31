package bregmi1.ramapo.edu.longana_android.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bregmi1.ramapo.edu.longana_android.R;
import bregmi1.ramapo.edu.longana_android.model.Round;
import bregmi1.ramapo.edu.longana_android.model.Tournament;

public class TournamentActivity extends AppCompatActivity {

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
                                                Round round = tournament.generateNewRound();
                                                //start the round Activity by sending this round as putExtra
                                            }
                                        });
                            }
                        });
                messages.show();
            }
        });
    }
}
