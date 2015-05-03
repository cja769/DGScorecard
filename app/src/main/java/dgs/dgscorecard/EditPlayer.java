package dgs.dgscorecard;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class EditPlayer extends Activity {

    // database helper
    private DGSDatabaseHelper mDatabaseHelper;

    // database items list
    private List<Player> mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        mDatabaseHelper = new DGSDatabaseHelper(this);
        mPlayer = mDatabaseHelper.getAllPlayerItems(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_player_checkbox);
        for(int i = 0; i < mPlayer.size(); i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(mPlayer.get(i).getName());
            cb.setChecked(false);
            ll.addView(cb);
        }

        final Button addButton = (Button) findViewById(R.id.edit_player_delete);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removePlayer();
            }
        });

        findViewById(R.id.ep_add_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.ep_edit_text_add);
                addPlayer(et.getText().toString());
                et.setText("");
            }
        });
    }

    private void removePlayer() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_player_checkbox);
        int numOfBoxes = ll.getChildCount();
        ArrayList<CheckBox> players = new ArrayList<>();
        for(int i = 0; i < numOfBoxes; i++){
            CheckBox cb = (CheckBox) ll.getChildAt(i);
            if(cb.isChecked()) {
                players.add(cb);
            }
        }
        for(CheckBox s: players) {
            mDatabaseHelper.deletePlayer(s.getText().toString());
            ll.removeView(s);
        }
    }

    private void addPlayer(String name){
        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_player_checkbox);
        Player p = new Player(name);
        ArrayList<Player> list = new ArrayList<>();
        list.add(p);
        mDatabaseHelper.addPlayerItems(list);
        CheckBox cb = new CheckBox(this);
        cb.setText(name);
        ll.addView(cb);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_player, menu);
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
}
