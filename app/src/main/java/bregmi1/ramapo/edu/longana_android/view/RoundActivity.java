package bregmi1.ramapo.edu.longana_android.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

import bregmi1.ramapo.edu.longana_android.R;
import bregmi1.ramapo.edu.longana_android.model.Computer;
import bregmi1.ramapo.edu.longana_android.model.Domino;
import bregmi1.ramapo.edu.longana_android.model.Human;
import bregmi1.ramapo.edu.longana_android.model.Round;
import bregmi1.ramapo.edu.longana_android.model.Side;

public class RoundActivity extends Activity {

    private Round round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        Intent intent = getIntent();
        round = (Round) intent.getSerializableExtra("round");
        TextView tournamentScore = (TextView) findViewById(R.id.tournamentScore);
        tournamentScore.setText(intent.getSerializableExtra("tournamentScore").toString());

        TextView roundCount = (TextView) findViewById(R.id.roundCount);
        roundCount.setText(intent.getSerializableExtra("roundCount").toString());


        Button passButton = (Button) findViewById(R.id.passButton);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round.humanPass()) {
                    askToSave().setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            normalizePlayTurn();
                        }
                    }).show();
                } else {
                    Toast.makeText(RoundActivity.this, "You can't pass yet! \nYou might have a valid move in your hand or you might be able to draw from stock!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button drawButton = (Button) findViewById(R.id.drawButton);
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Domino drawnDomino = round.humanDraw();
                if (drawnDomino != null) {
                    Toast.makeText(RoundActivity.this, "You drew " + drawnDomino.toString(), Toast.LENGTH_SHORT).show();
                    refreshLayout();
                } else {
                    Toast.makeText(RoundActivity.this, "You can't draw yet! \nYou might have a valid move in your hand or you already drew from stock!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button hintButton = (Button) findViewById(R.id.saveButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = round.getHint();
                if (hint == null) {
                    hint = "You do not have any valid moves in hand! You need to draw or pass!";
                }
                Toast.makeText(RoundActivity.this, hint, Toast.LENGTH_LONG).show();
            }
        });

        round.init();
        refreshLayout();
        askToSave().setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstPlayerLogic = round.determineFirstPlayer();
                if (firstPlayerLogic != null) {
                    refreshLayout();
                    AlertDialog.Builder messages = new AlertDialog.Builder(RoundActivity.this);
                    messages.setMessage(firstPlayerLogic)
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    normalizePlayTurn();
                                }
                            });
                    //save and quit, normalize the firstPlayer
                    messages.show();
                } else {
                    normalizePlayTurn();
                }
            }
        }).show();

        TextView humanScore = (TextView) findViewById(R.id.humanScore);
        humanScore.setText(String.format("%d", round.getPlayerScore(Human.class)));

        TextView computerScore = (TextView) findViewById(R.id.computerScore);
        computerScore.setText(String.format("%d", round.getPlayerScore(Computer.class)));
    }

    @Override
    public void onBackPressed() {

    }

    private void refreshLayout() {
        //check for round end here
        if (round.hasRoundEnded()) {
            AlertDialog.Builder messages = new AlertDialog.Builder(this);
            StringBuilder roundScores = new StringBuilder();
            roundScores.append("Round Ended!\n").append(round.getRoundWinnerAndScore());
            //calculate scores and show here
            messages.setMessage(roundScores.toString())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            //save and quit, normalize the firstPlayer
            messages.show();
        }
        Vector<Domino> layout = round.getLayout();
        Vector<Domino> stock = round.getStock();
        Vector<Domino> humanHand = round.getHumanHand();
        Vector<Domino> computerHand = round.getComputerHand();

        GridLayout layoutLayout = (GridLayout) findViewById(R.id.layout);
        GridLayout stockLayout = (GridLayout) findViewById(R.id.stock);
        GridLayout humanHandLayout = (GridLayout) findViewById(R.id.humanHand);
        GridLayout computerHandLayout = (GridLayout) findViewById(R.id.computerHand);

        layoutLayout.removeAllViews();
        addDominosToGameLayout(layout, layoutLayout);

        stockLayout.removeAllViews();
        addButtonsToLayout(stock, stockLayout, false);

        humanHandLayout.removeAllViews();
        addButtonsToLayout(humanHand, humanHandLayout, true);

        computerHandLayout.removeAllViews();
        addButtonsToLayout(computerHand, computerHandLayout, false);
    }

    private void addDominosToGameLayout(Vector<Domino> dominoes, GridLayout layout) {
        GridLayout.LayoutParams gridLayoutParam;

        int row = 0, col = 0;
        for (int i = 0; i < dominoes.size(); i++) {
            if (i >= 14) row = 1;
            col = i % 14;
            GridLayout.Spec gridRow = GridLayout.spec(row, 1);
            GridLayout.Spec gridCol = GridLayout.spec(col, 1);
            gridLayoutParam = new GridLayout.LayoutParams(gridRow, gridCol);
            layout.addView(getDominoButton(dominoes.get(i), false), gridLayoutParam);
        }
    }

    private void addButtonsToLayout(Vector<Domino> dominoes, GridLayout layout, boolean buttonsEnabled) {
        GridLayout.LayoutParams gridLayoutParam;
        for (int i = 0; i < dominoes.size(); i++) {
            gridLayoutParam = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(i));
            layout.addView(getDominoButton(dominoes.get(i), buttonsEnabled), gridLayoutParam);
        }
    }

    private Button getDominoButton(final Domino domino, boolean buttonsEnabled) {
        Button button = new Button(this);
        button.setEnabled(buttonsEnabled);

        if (buttonsEnabled) {
            //eventListenerForButton
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder sideSelector = new AlertDialog.Builder(RoundActivity.this);
                    sideSelector.setMessage("Select a side to play: ")
                            .setPositiveButton("RIGHT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (playRound(domino, Side.RIGHT)) {
                                        askToSave().setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                normalizePlayTurn();
                                            }
                                        }).show();

                                    }
                                }
                            })
                            .setNegativeButton("LEFT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (playRound(domino, Side.LEFT)) {
                                        askToSave().setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                normalizePlayTurn();
                                            }
                                        }).show();
                                    }
                                }
                            });
                    sideSelector.show();
                }
            });
        }

        if (domino.isDouble()) button.setText(domino.getPip1() + "\n|\n" + domino.getPip2());
        else button.setText(domino.toString());
        return button;

    }

    private boolean playRound(Domino domino, Side side) {
        String message = round.play(domino, side);
        if (message != null) {
            Toast toast = Toast.makeText(RoundActivity.this, message, Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        refreshLayout();
        return true;
    }

    private void normalizePlayTurn() {
        String normalizeResult = round.normalizeTurn();
        if (normalizeResult != null) {
            refreshLayout();
            Toast.makeText(RoundActivity.this, normalizeResult, Toast.LENGTH_LONG).show();
            askToSave().show();
        }
    }

    private AlertDialog.Builder askToSave() {
        AlertDialog.Builder saveAlert = new AlertDialog.Builder(RoundActivity.this);
        saveAlert.setMessage("Do you want to save and quit?: ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("serialize", round.serialize());
                        Intent output = new Intent();
                        setResult(RESULT_CANCELED, output);
                        finish();
                    }
                }).setNegativeButton("NO", null);
        return saveAlert;
    }
}


