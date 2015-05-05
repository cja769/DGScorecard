package dgs.dgscorecard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditScorecard extends Activity {

    private DGSDatabaseHelper dbHelp;
    private LinearLayout checkBoxLayout;
    private Map<CheckBox, Scorecard> ids;
    public static String EXTRA_MESSAGE = "edit_scorecard_id";
    public static String EDIT_SCORECARD = "edit_scorecard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scorecard);

        checkBoxLayout = (LinearLayout) findViewById(R.id.ec_checkbox);
        dbHelp = new DGSDatabaseHelper(this);
        ids = new HashMap<>();
        List<Scorecard> allCards = dbHelp.getAllFullScorecards();
        CheckBox cb = new CheckBox(this);
        cb.setText("Select All");
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = ((CheckBox) v).isChecked();
                for(int i = 0; i < checkBoxLayout.getChildCount(); i++) {
                    ((CheckBox) checkBoxLayout.getChildAt(i)).setChecked(check);
                }
            }
        });
        checkBoxLayout.addView(cb);
        for(Scorecard c: allCards) {
            if(c.getPlayers() == null || c.getPlayers().size() <= 0) {
                dbHelp.deleteScorecard(c.getID());
            }
            else {
                cb = new CheckBox(this);
                cb.setMaxLines(2);
                Format formatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
                String s = formatter.format(c.getDate());
                cb.setText(c.getCourse().getName() + "\n" + s);
                checkBoxLayout.addView(cb);
                ids.put(cb, c);
           }
        }

        findViewById(R.id.es_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CheckBox> cbs = new ArrayList<CheckBox>();
                for(int i = 1; i < checkBoxLayout.getChildCount(); i++){
                    CheckBox cb = (CheckBox) checkBoxLayout.getChildAt(i);
                    if(cb.isChecked()){
                        boolean deleted = dbHelp.deleteScorecard(ids.get(cb).getID());
                        if(deleted)
                            cbs.add(cb);
                    }
                }
                for(CheckBox cb: cbs){
                    checkBoxLayout.removeView(cb);
                    ids.remove(cb);
                }
                ((CheckBox) checkBoxLayout.getChildAt(0)).setChecked(false);
            }
        });

        findViewById(R.id.es_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cont = true;
                for(int i = 1; i < checkBoxLayout.getChildCount() && cont; i++){
                    CheckBox cb = (CheckBox) checkBoxLayout.getChildAt(i);
                    if(cb.isChecked()){
                        cont = false;
                        resumeScorecard(ids.get(cb).getID());
                    }
                }
            }
        });

        findViewById(R.id.es_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cont = true;
                for(int i = 1; i < checkBoxLayout.getChildCount() && cont; i++){
                    CheckBox cb = (CheckBox) checkBoxLayout.getChildAt(i);
                    if(cb.isChecked()){
                        cont = false;
                        viewScorecard(ids.get(cb).getID());
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_scorecard, menu);
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

//    private void unclickBoxes(View v){
//        for(int i = 0; i < checkBoxLayout.getChildCount(); i++){
//            CheckBox cb = (CheckBox) checkBoxLayout.getChildAt(i);
//            if(!cb.equals(v))
//                cb.setChecked(false);
//        }
//    }

    private void resumeScorecard(int id){
        Intent intent = new Intent(this, ScorecardActivity.class);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
        this.finish();
    }

    private void viewScorecard(int id) {
        Intent intent = new Intent(this, ScorecardSummary.class);
        intent.putExtra(ScorecardActivity.EXTRA_MESSAGE,id);
        intent.putExtra(EDIT_SCORECARD, true);
        startActivity(intent);
    }
}
