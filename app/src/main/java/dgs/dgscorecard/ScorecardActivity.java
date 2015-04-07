package dgs.dgscorecard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;


public class ScorecardActivity extends ActionBarActivity {

    private Scorecard mScorecard;
    private int mCurrentHole;
    private TextView hole;
    private TextView par;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);

        Intent intent = getIntent();
        String courseName = intent.getStringExtra(CourseSelect.EXTRA_MESSAGE);
        ArrayList<String> playerName = intent.getStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE);
        ArrayList<Integer> pars = intent.getIntegerArrayListExtra(ManualCourseAdd.EXTRA_MESSAGE);
        Log.v("Course", courseName);
        String str = "[";
        for(Integer s: pars)
            str += s + " ";
        str += "]";
        Log.v("Pars", str);
        TextView course = (TextView) findViewById(R.id.sc_course_name);

        hole = (TextView) findViewById(R.id.sc_hole_number);
        par = (TextView) findViewById(R.id.sc_par);

        mScorecard = new Scorecard();
        mScorecard.getCourse().setName(courseName);
        mScorecard.getCourse().setPars(pars);
        ArrayList<Player> pArraylist = new ArrayList<Player>();
        for(String s: playerName){
            Player p = new Player(s);
            pArraylist.add(p);

        }
        course.setText(courseName);

        LinearLayout ll = (LinearLayout) findViewById(R.id.sc_all_scores);
        ll.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i < pArraylist.size(); i++){
            Player p = pArraylist.get(i);
            View item = inflater.inflate(R.layout.activity_scorecard, null);
            LinearLayout playerStuff = (LinearLayout) item.findViewById(R.id.sc_score_holder);
            TextView tv = (TextView) item.findViewById(R.id.sc_name);
            tv.setText(p.getName());
            p.setNameField(tv);
            p.setPuttField((NumberPicker) item.findViewById(R.id.sc_putt_picker));
            p.setScoreField((NumberPicker) item.findViewById(R.id.sc_score_picker));
            p.setTotalScore((TextView) item.findViewById(R.id.sc_total_score));
            p.setUnderOver((TextView) item.findViewById(R.id.sc_plus_minus));
            pArraylist.set(i,p);
            LinearLayout parent = (LinearLayout) item.findViewById(R.id.sc_all_scores);
            parent.removeView(playerStuff);
            ll.addView(playerStuff);
        }

        mScorecard.setPlayers(pArraylist);
        mCurrentHole = 0;
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
                    sendMessage(TempFinish.class);
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

        final Button finishButton = (Button) findViewById(R.id.sc_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(TempFinish.class);
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
        Map<Player, ArrayList<Integer>> cardScores = mScorecard.getScores();
        Map<Player, ArrayList<Integer>> cardPutts = mScorecard.getPutts();
        for(Player player: mScorecard.getPlayers()){
            ArrayList<Integer> playerScores = cardScores.get(player);
            int scoreVal = player.getScoreField().getValue();
            playerScores.set(mCurrentHole, scoreVal);
            cardScores.put(player, playerScores);
            mScorecard.setScores(cardScores);
            ArrayList<Integer> playerPutts = cardPutts.get(player);
            int puttVal = player.getPuttField().getValue();
            playerPutts.set(mCurrentHole, puttVal);
            cardPutts.put(player, playerPutts);
            mScorecard.setPutts(cardPutts);
        }
    }

    private void setViewElements(){
        hole.setText("Hole " + (mCurrentHole+1));
        par.setText("Par " + mScorecard.getCourse().getPars().get(mCurrentHole));

       for(Player p: mScorecard.getPlayers()){
           int scoreVal = mScorecard.getScores().get(p).get(mCurrentHole);
           NumberPicker scoreField = p.getScoreField();
           scoreField.setMinValue(1);
           scoreField.setMaxValue(20);
           scoreField.setValue(scoreVal);
           int puttVal = mScorecard.getPutts().get(p).get(mCurrentHole);
           NumberPicker puttField = p.getPuttField();
           puttField.setMinValue(0);
           puttField.setMaxValue(20);
           puttField.setValue(puttVal);
           int total = mScorecard.calculateTotal(p);
           p.getTotalScore().setText(total+"");
           int diff = total - mScorecard.getCourse().getPar();
           String underOverStr = "";
           if(diff < 0)
               underOverStr += "(" + diff + ")";
           else
               underOverStr += "(+" + diff + ")";

           p.getUnderOver().setText(underOverStr);

       }

    }

    public void sendMessage(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
