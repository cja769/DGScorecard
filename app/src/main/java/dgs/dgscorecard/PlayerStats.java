package dgs.dgscorecard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayerStats extends Activity {

    private Map<View, Player> viewToPlayer;
    public static String EXTRA_MESSAGE = "player_stats_player";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);

        List<Player> players = (new DGSDatabaseHelper(this)).getAllPlayerItems(this);
        viewToPlayer = new HashMap<>();
        LinearLayout ll = (LinearLayout) findViewById(R.id.pstat_ll);
        for(Player p: players) {
            TextView tv = new TextView(this);
            tv.setText(p.getName());
            tv.setTextSize(30);
            tv.setPadding(0,0,0,20);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewStats(v);
                }
            });
            viewToPlayer.put(tv,p);
            ll.addView(tv);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_stats, menu);
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

    private void viewStats(View v) {
        Intent intent = new Intent(this, ViewPlayerStats.class);
        intent.putExtra(EXTRA_MESSAGE, viewToPlayer.get(v).getName());
        startActivity(intent);
    }

}
