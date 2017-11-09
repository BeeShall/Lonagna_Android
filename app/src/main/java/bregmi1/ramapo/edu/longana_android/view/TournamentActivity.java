package bregmi1.ramapo.edu.longana_android.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

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
                                AlertDialog.Builder alert = new AlertDialog.Builder(TournamentActivity.this);
                                alert.setTitle("Select a game");

                                final File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles(new FilenameFilter() {
                                    @Override
                                    public boolean accept(File dir, String name) {
                                        return true;
                                    }
                                });


                                String[] items = new String[files.length];
                                for (int i = 0; i < files.length; ++i) {
                                    items[i] = files[i].getName();
                                }


                                alert.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ListView lw = ((AlertDialog) dialog).getListView();
                                        String fileName = (String) lw.getAdapter().getItem(which);

                                        //de-serialize and create round;
                                        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + fileName;
                                        try {
                                            InputStream is = new FileInputStream(filePath);
                                            startRoundActivity(tournament.load(is));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                alert.show();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //ask for the tournament score
                                AlertDialog.Builder scoreAlert = new AlertDialog.Builder(TournamentActivity.this);
                                final EditText scoreText = new EditText(TournamentActivity.this);
                                scoreText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                scoreAlert.setTitle("Please enter a tournament score:")
                                        .setView(scoreText)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tournament.setTournamentScore(Integer.parseInt(scoreText.getText().toString()));
                                                startRoundActivity(tournament.generateNewRound());
                                            }
                                        });
                                scoreAlert.show();
                            }
                        });
                messages.show();
            }
        });
    }


    private void startRoundActivity(Round round) {
        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra("round", round);
        intent.putExtra("tournamentScore", tournament.getTournamentScore());
        intent.putExtra("roundCount", tournament.getCurrentRoundCount());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if save and quit
        Log.v("resultCode", "" + resultCode + "  " + RESULT_CANCELED);
        if (resultCode == RESULT_CANCELED) {
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/longana.txt";
            if (tournament.serialize(new File(filePath), data.getStringExtra("round"))) {
                Toast.makeText(TournamentActivity.this, "The game has been saved. GoodBye!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
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
            startRoundActivity(tournament.generateNewRound());
        }
    }
}
