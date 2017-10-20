package bregmi1.ramapo.edu.longana_android.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import bregmi1.ramapo.edu.longana_android.R;
import bregmi1.ramapo.edu.longana_android.model.Computer;
import bregmi1.ramapo.edu.longana_android.model.Human;
import bregmi1.ramapo.edu.longana_android.model.Round;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Human human = new Human();
        Computer computer = new Computer();

        Round round = new Round(human, computer, 6);
        round.init();
        AlertDialog.Builder messages = new AlertDialog.Builder(this);
        messages.setMessage(round.determineFirstPlayer());
        messages.show();



    }
}
