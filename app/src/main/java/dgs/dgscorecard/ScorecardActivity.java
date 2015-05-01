package dgs.dgscorecard;

import android.app.Activity;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ScorecardActivity extends Activity {

    private Scorecard mScorecard;
    private int mCurrentHole;
    private TextView hole;
    private TextView par;
    private Map<Button,Player> buttonToPlayer;
    public static String EXTRA_MESSAGE = "SCORECARD";


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
        buttonToPlayer = new HashMap<Button, Player>();
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
            p.setPuttField((TextView) item.findViewById(R.id.sc_hole_putt));
            p.setScoreField((TextView) item.findViewById(R.id.sc_hole_score));
            p.setTotalScore((TextView) item.findViewById(R.id.sc_total_score));
            p.setUnderOver((TextView) item.findViewById(R.id.sc_plus_minus));
            Button moreScore = (Button) item.findViewById(R.id.sc_more_score);
            Button lessScore = (Button) item.findViewById(R.id.sc_less_score);
            Button morePutt = (Button) item.findViewById(R.id.sc_more_putt);
            Button lessPutt = (Button) item.findViewById(R.id.sc_less_putt);

            buttonToPlayer.put(moreScore, p);
            buttonToPlayer.put(lessScore, p);
            buttonToPlayer.put(morePutt, p);
            buttonToPlayer.put(lessPutt, p);
            // Setting button onclicks
            moreScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player p = buttonToPlayer.get(v);
                    changeTextView(true, null, p.getScoreField());
                    changeTextView(true, null, p.getTotalScore());
                    setUnderOver(p);

                }
            });
            lessScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Player p = buttonToPlayer.get(v);
                    if(changeTextView(false, 1, p.getScoreField())) {
                        changeTextView(false, null, p.getTotalScore());
                        setUnderOver(p);
                    }

                }
            });
            morePutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextView(true, null, buttonToPlayer.get(v).getPuttField());
                }
            });
            lessPutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextView(false, 0, buttonToPlayer.get(v).getPuttField());
                }
            });
            pArraylist.set(i,p);
            p.setScoreHolder(playerStuff);
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
                setScores();
                if(mCurrentHole+1 < mScorecard.getCourse().getNumHoles()) {
                    mCurrentHole++;
                    setViewElements();
                }
                else {
                    showSummary();
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

    private void showSummary() {

        Intent intent = new Intent(this, ScorecardSummary.class);
        Map<Player,ArrayList<Integer>> allScores = mScorecard.getScores();
        HashMap<String, ArrayList<Integer>> serializableScores = new HashMap<String, ArrayList<Integer>>();
        for(Player p: allScores.keySet())
            serializableScores.put(p.getName(),allScores.get(p));
        intent.putExtra(EXTRA_MESSAGE,serializableScores);
        startActivity(intent);

    }

    private boolean changeTextView(boolean add, Integer lowerLimit, TextView tvToChange){
        int num = 0;
        try{
            num = Integer.parseInt(tvToChange.getText()+"");
        }
        catch(NumberFormatException e) {}
        num = add == true ? num+1 : num-1;
        if(lowerLimit != null && lowerLimit.intValue() > num)
            return false;
        tvToChange.setText(num+"");
        return true;
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
            int scoreVal = 3;
            try {
                scoreVal = Integer.parseInt(player.getScoreField().getText() + "");
            }
            catch(NumberFormatException e){}
            playerScores.set(mCurrentHole, scoreVal);
            cardScores.put(player, playerScores);
            mScorecard.setScores(cardScores);
            ArrayList<Integer> playerPutts = cardPutts.get(player);
            int puttVal = 1;
            try{
                puttVal = Integer.parseInt(player.getPuttField().getText() + "");
            }
            catch(NumberFormatException e){}
            playerPutts.set(mCurrentHole, puttVal);
            cardPutts.put(player, playerPutts);
            mScorecard.setPutts(cardPutts);
            player.setScore(mScorecard.calculateTotal(player));
        }
    }

    private void setViewElements(){
        hole.setText("Hole " + (mCurrentHole+1));
        par.setText("Par " + mScorecard.getCourse().getPars().get(mCurrentHole));
        LinearLayout parent = (LinearLayout) findViewById(R.id.sc_all_scores);
        parent.removeAllViews();
       for(Player p: mScorecard.sortPlayers(mCurrentHole)){
           parent.addView(p.getScoreHolder());
           int scoreVal = mScorecard.getScores().get(p).get(mCurrentHole);
           TextView scoreField = p.getScoreField();
           scoreField.setText(scoreVal+"");
           int puttVal = mScorecard.getPutts().get(p).get(mCurrentHole);
           TextView puttField = p.getPuttField();
           puttField.setText(puttVal+"");
           int total = mScorecard.calculateTotal(p);
           p.getTotalScore().setText(total+"");
           setUnderOver(p);
       }

    }

    private void setUnderOver(Player p){
        int total = 0;
        try{
            total = Integer.parseInt(p.getTotalScore().getText()+"");
        }
        catch(NumberFormatException e){return;}
        int diff = total - mScorecard.getCourse().getPar();
        String underOverStr = "";
        if(diff < 0)
            underOverStr += "(" + diff + ")";
        else
            underOverStr += "(+" + diff + ")";
        p.getUnderOver().setText(underOverStr);
    }
}
