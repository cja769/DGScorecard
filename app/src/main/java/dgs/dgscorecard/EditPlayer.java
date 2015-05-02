package dgs.dgscorecard;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    }

    private void removePlayer() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_player_checkbox);
        int numOfBoxes = ll.getChildCount();
        ArrayList<String> players = new ArrayList<String>();
        for(int i = 0; i < numOfBoxes; i++){
            CheckBox cb = (CheckBox) ll.getChildAt(i);
            if(cb.isChecked()) {
                //delete
            }
        }
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
