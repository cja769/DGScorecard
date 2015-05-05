package dgs.dgscorecard;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewPlayerStats extends Activity {

    private DGSDatabaseHelper dbHelp;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_player_stats);

        dbHelp = new DGSDatabaseHelper(this);
        String name = getIntent().getStringExtra(PlayerStats.EXTRA_MESSAGE);
        player = dbHelp.getPlayerByName(name);
        int handicap = dbHelp.getPlayerHandicap(player);
        double averagePutts = dbHelp.getPlayerAveragePutts(player);
        int totalPutts = dbHelp.getPlayerTotalPutts(player);
        int rounds = dbHelp.getPlayerRoundsPlayed(player);
        int totalScore = dbHelp.getPlayerTotalScore(player);
        double averageScore = dbHelp.getPlayerAverageScore(player);
        ((TextView) findViewById(R.id.vps_handicap)).setText(handicap+"");
        ((TextView) findViewById(R.id.vps_avg_putts)).setText(averagePutts+"");
        ((TextView) findViewById(R.id.vps_total_putts)).setText(totalPutts+"");
        ((TextView) findViewById(R.id.vps_rounds_played)).setText(rounds+"");
        ((TextView) findViewById(R.id.vps_total_score)).setText(totalScore+"");
        ((TextView) findViewById(R.id.vps_average_score)).setText(averageScore+"");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_player_stats, menu);
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
