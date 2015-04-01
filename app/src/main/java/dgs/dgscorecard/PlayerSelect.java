package dgs.dgscorecard;

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


public class PlayerSelect extends ActionBarActivity {

    private String name;
    public static final String EXTRA_MESSAGE = "Players";
    private SharedPreferences mPrefs;

    ArrayList<String> prevPlayers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);

        mPrefs = getSharedPreferences("PlayerSelect_prefs", MODE_PRIVATE);

        //restore names
        prevPlayers.addAll(mPrefs.getStringSet("PlayerSelect_prefs", new HashSet<String>()));

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
        if(prevPlayers.size() > 0)
            ed.putStringSet("mPlayerNames", new HashSet<String>(prevPlayers));
        ed.apply();
    }

    private void addPlayerToList(){
        EditText nameEditText = (EditText)findViewById(R.id.ps_enter_name);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ps_checkbox_field);
        String newName = nameEditText.getText().toString();
        prevPlayers.add(newName);
        CheckBox cb = new CheckBox(this);
        cb.setText(newName);
        cb.setChecked(true);
        ll.addView(cb);
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
        intent.putStringArrayListExtra(EXTRA_MESSAGE, players);
        startActivity(intent);

    }
}
