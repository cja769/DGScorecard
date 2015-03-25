package dgs.dgscorecard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;


public class ScorecardActivity extends ActionBarActivity {

    private Scorecard mScorecard;
    private int mCurrentHole;
    private TextView hole;
    private TextView par;
    private TextView totalScore;
    private TextView underOver;
    private NumberPicker score;
    private NumberPicker putts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);

        Intent intent = getIntent();
        String courseName = intent.getStringExtra(CourseSelect.EXTRA_MESSAGE);
        String playerName = intent.getStringExtra(PlayerSelect.EXTRA_MESSAGE);
        Log.v("INFO", courseName);
        Log.v("INFO", playerName);
        TextView course = (TextView) findViewById(R.id.sc_course_name);
        TextView player = (TextView) findViewById(R.id.sc_name);

        putts = (NumberPicker) findViewById(R.id.sc_putt_picker);
        hole = (TextView) findViewById(R.id.sc_hole_number);
        par = (TextView) findViewById(R.id.sc_par);
        totalScore = (TextView) findViewById(R.id.sc_total_score);
        underOver = (TextView) findViewById(R.id.sc_plus_minus);
        score = (NumberPicker) findViewById(R.id.sc_score_picker);

        mScorecard = new Scorecard();
        mScorecard.getCourse().setName(courseName);
        Player[] players = {new Player(playerName)};
        mScorecard.setPlayers(players);
        course.setText(courseName);
        player.setText(playerName);
        mCurrentHole = 0;
        putts.setMinValue(0);
        putts.setMaxValue(20);
        putts.setValue(0);
        setViewElements();

        final Button forwardButton = (Button) findViewById(R.id.sc_forward);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mCurrentHole+1 < mScorecard.getCourse().getNumHoles()) {
                    setScores();
                    mCurrentHole++;
                    setViewElements();
                }
                else {
                    // showSummary();
                }
            }
        });

        final Button backwardButton = (Button) findViewById(R.id.sc_back);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mCurrentHole-1 > -1) {
                    setScores();
                    mCurrentHole--;
                    setViewElements();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scorecard, menu);
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

    private void setScores(){
        int scoreVal = score.getValue();
        int puttVal = putts.getValue();
        Map<Player, int[]> cardScores = mScorecard.getScores();
        Map<Player, int[]> cardPutts = mScorecard.getPutts();
        for(Player player: mScorecard.getPlayers()){
            int[] playerScores = cardScores.get(player);
            playerScores[mCurrentHole] = scoreVal;
            cardScores.put(player, playerScores);
            mScorecard.setScores(cardScores);
            int[] playerPutts = cardPutts.get(player);
            playerPutts[mCurrentHole] = puttVal;
            cardPutts.put(player, playerPutts);
            mScorecard.setPutts(cardPutts);
        }
    }

    private void setViewElements(){
        hole.setText("Hole " + (mCurrentHole+1));
        par.setText("Par " + mScorecard.getCourse().getPars()[mCurrentHole]);
        score.setMinValue(1);
        score.setMaxValue(20);

        // right now we only have one player so this'll need to be changed later
        int scoreVal = mScorecard.getScores().get(mScorecard.getPlayers()[0])[mCurrentHole];
        score.setValue(scoreVal);
        int puttVal = mScorecard.getPutts().get(mScorecard.getPlayers()[0])[mCurrentHole];
        putts.setValue(puttVal);
        int total = mScorecard.calculateTotal(mScorecard.getPlayers()[0]);
        totalScore.setText(total+"");
        int diff = total - mScorecard.getCourse().getPar();
        Log.v("Total",total+"");
        Log.v("Par", mScorecard.getCourse().getPar()+"");
        String underOverStr = "";
        if(diff < 0)
            underOverStr += "(" + diff + ")";
        else
            underOverStr += "(+" + diff + ")";

        underOver.setText(underOverStr);


    }
}
