package dgs.dgscorecard;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
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
        Integer handicap = dbHelp.getPlayerHandicap(player);
        Double averagePutts = dbHelp.getPlayerAveragePutts(player);
        Integer totalPutts = dbHelp.getPlayerTotalPutts(player);
        Integer rounds = dbHelp.getPlayerRoundsPlayed(player);
        Integer totalScore = dbHelp.getPlayerTotalScore(player);
        Double averageScore = dbHelp.getPlayerAverageScore(player);
        String mostPlayed = dbHelp.getPlayedMostPlayed(player);
        Integer totalHoles = dbHelp.getHolesPlayed(player);
        if(handicap != null)
            ((TextView) findViewById(R.id.vps_handicap)).setText(handicap+"");
        if(averagePutts != null)
            ((TextView) findViewById(R.id.vps_avg_putts)).setText(averagePutts+"");
        if(totalPutts != null)
            ((TextView) findViewById(R.id.vps_total_putts)).setText(totalPutts+"");
        if(rounds != null)
            ((TextView) findViewById(R.id.vps_rounds_played)).setText(rounds+"");
        if(totalScore != null)
            ((TextView) findViewById(R.id.vps_total_score)).setText(totalScore+"");
        if(averageScore != null)
            ((TextView) findViewById(R.id.vps_average_score)).setText(averageScore+"");
        if(mostPlayed != null)
            ((TextView) findViewById(R.id.vps_most_played)).setText(mostPlayed);
        if(totalHoles != null)
            ((TextView) findViewById(R.id.vps_total_holes)).setText(totalHoles+"");

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
