package dgs.dgscorecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class PlayerSelect extends Activity {

    private String name;
    public static final String EXTRA_MESSAGE = "Players";
    private SharedPreferences mPrefs;
    private int num_players;

    // database helper
    private DGSDatabaseHelper mDatabaseHelper;

    // database items list
    private List<Player> mPlayer;

    //ArrayList<String> prevPlayers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);

        mDatabaseHelper = new DGSDatabaseHelper(this);
        mPlayer = mDatabaseHelper.getAllPlayerItems(this);

        mPrefs = getSharedPreferences("PlayerSelect_prefs", MODE_PRIVATE);

        //restore player count
        num_players = mPrefs.getInt("num_players", 0);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ps_checkbox_field);
        for(int i = 0; i < mPlayer.size(); i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(mPlayer.get(i).getName());
            cb.setChecked(false);
            ll.addView(cb);
        }
        //prevPlayers.addAll(mPrefs.getStringSet("PlayerSelect_prefs", new HashSet<String>()));

        final Button nextButton = (Button) findViewById(R.id.ps_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
            }
        });

        final Button addButton = (Button) findViewById(R.id.ps_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPlayerToList();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        super.onStop();

        //Save Player Names
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("num_players", num_players);
        //if(prevPlayers.size() > 0)
            //ed.putStringSet("mPlayerNames", new HashSet<String>(prevPlayers));
        ed.apply();
    }

    private void addPlayerToList(){
        EditText nameEditText = (EditText)findViewById(R.id.ps_enter_name);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ps_checkbox_field);
        String newName = nameEditText.getText().toString();
        if(newName.equals(""))
            return;
        //prevPlayers.add(newName);
        CheckBox cb = new CheckBox(this);
        cb.setText(newName);
        cb.setChecked(true);
        ll.addView(cb);
        System.out.println(newName);
        Player newPlayer = new Player(newName, newName, "0", "0","0", "0", num_players, this);
        ++num_players;
        ArrayList<Player> players = new ArrayList<>();
        players.add(newPlayer);
        mDatabaseHelper.addPlayerItems(players);
        //System.out.println(num_players);
        nameEditText.setText("");

    }

    private void sendMessage(View view) {
        Intent intent = new Intent(this, CourseSelect.class);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ps_checkbox_field);
        int numOfBoxes = ll.getChildCount();
        ArrayList<String> players = new ArrayList<String>();
        for(int i = 0; i < numOfBoxes; i++){
            CheckBox cb = (CheckBox) ll.getChildAt(i);
            if(cb.isChecked())
                players.add(cb.getText().toString());
        }
        if(players.size() == 0)
            return;
        intent.putStringArrayListExtra(EXTRA_MESSAGE, players);
        startActivity(intent);

    }
}
