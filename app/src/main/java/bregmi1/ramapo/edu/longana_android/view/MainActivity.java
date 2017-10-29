package bregmi1.ramapo.edu.longana_android.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.Vector;

import bregmi1.ramapo.edu.longana_android.R;
import bregmi1.ramapo.edu.longana_android.model.Computer;
import bregmi1.ramapo.edu.longana_android.model.Domino;
import bregmi1.ramapo.edu.longana_android.model.Human;
import bregmi1.ramapo.edu.longana_android.model.Round;
import bregmi1.ramapo.edu.longana_android.model.Side;

public class MainActivity extends AppCompatActivity {

    private Human human;
    private Computer computer;
    private Round round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        human = new Human();
        computer = new Computer();

        round = new Round(human, computer, 6);
        round.init();
        AlertDialog.Builder messages = new AlertDialog.Builder(this);
        messages.setMessage(round.determineFirstPlayer());
        messages.show();
        refreshLayout();

    }

    private void refreshLayout() {
        Vector<Domino> layout = round.getLayout();
        Vector<Domino> stock = round.getStock();
        Vector<Domino> humanHand = human.getHand();
        Vector<Domino> computerHand = computer.getHand();

        GridLayout layoutLayout = (GridLayout) findViewById(R.id.layout);
        GridLayout stockLayout = (GridLayout) findViewById(R.id.stock);
        GridLayout humanHandLayout = (GridLayout) findViewById(R.id.humanHand);
        GridLayout computerHandLayout = (GridLayout) findViewById(R.id.computerHand);

        layoutLayout.removeAllViews();
        addButtonsToLayout(layout, layoutLayout, false);

        stockLayout.removeAllViews();
        addButtonsToLayout(stock, stockLayout, false);

        humanHandLayout.removeAllViews();
        addButtonsToLayout(humanHand, humanHandLayout, true);

        computerHandLayout.removeAllViews();
        addButtonsToLayout(computerHand, computerHandLayout, false);


    }

    private void addButtonsToLayout(Vector<Domino> dominoes, GridLayout layout, boolean buttonsEnabled) {
        for (int i = 0; i < dominoes.size(); i++) {
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(i));
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
                    AlertDialog.Builder sideSelector = new AlertDialog.Builder(MainActivity.this);
                    sideSelector.setMessage("Setect a side to player: ")
                            .setPositiveButton("RIGHT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    round.play(domino, Side.RIGHT);
                                    refreshLayout();
                                }
                            })
                            .setNegativeButton("LEFT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    round.play(domino, Side.LEFT);
                                    refreshLayout();
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
}
