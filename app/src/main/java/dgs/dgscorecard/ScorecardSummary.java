package dgs.dgscorecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.LinkAddress;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.grantland.widget.AutofitHelper;
import me.grantland.widget.AutofitTextView;


public class ScorecardSummary extends Activity {

    private ArrayList<TextView> allTextViews;
    private Map<Player, ArrayList<Integer>> allScores;
    private Scorecard mScorecard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard_summary);

        ArrayList<String> scorecards = (new DGSDatabaseHelper(this)).dumpScorecards();
        for(String s: scorecards){
            Log.v("",s);
        }
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.ss_linear_layout);
        linLayout.removeAllViews();
        allTextViews = new ArrayList<TextView>();
        mScorecard = (new DGSDatabaseHelper(this)).getFullScorecard(getIntent().getIntExtra(ScorecardActivity.EXTRA_MESSAGE,-1),this);
        allScores = mScorecard.getScores();
        String str = "";
        for(Player p: allScores.keySet()) {
            for (int s : allScores.get(p))
                str += s + " ";
            Log.v(p.getName(),str);
            str = "";
        }


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int holes = 0;
        for(ArrayList<Integer> ai: allScores.values()){
            holes = ai.size();
            break;
        }

        int cardsNeeded = holes%9 != 0 ? holes/9 + 1 : holes/9;
        for(int i = 0; i  < cardsNeeded; i++) {
            View item = inflater.inflate(R.layout.activity_scorecard_summary, null);
            TableLayout tempTableLayout = (TableLayout) item.findViewById(R.id.ss_scorecard_table);
            TableRow nameR = (TableRow) item.findViewById(R.id.ss_name_row);
            TableRow holeRow = (TableRow) item.findViewById(R.id.ss_hole_row);
            TableRow parRow = (TableRow) item.findViewById(R.id.ss_par_row);
            tempTableLayout.removeView(nameR);
            for(int j = 1; j < holeRow.getChildCount(); j++){
                if(i*9 + j - 1 >= holes) {
                    ((TextView) holeRow.getChildAt(j)).setText("-");
                    ((TextView) parRow.getChildAt(j)).setText("-");
                }
                else {
                    ((TextView) holeRow.getChildAt(j)).setText(((i * 9) + j) + "");
                    ((TextView) parRow.getChildAt(j)).setText((mScorecard.getCourse().getPars().get(i * 9 + j - 1)) + "");
                }
            }
            for (Player p : allScores.keySet()) {
                View item2 = inflater.inflate(R.layout.activity_scorecard_summary, null);
                TableRow nameRow = (TableRow) item2.findViewById(R.id.ss_name_row);
                int count = nameRow.getChildCount();
                ((AutofitTextView) nameRow.getChildAt(0)).setText(p.getName());
                for (int j = 1; j < count; j++) {
                    if(i*9 + j - 1 >= holes)
                        ((TextView) nameRow.getChildAt(j)).setText("-");
                    else
                        ((TextView) nameRow.getChildAt(j)).setText(allScores.get(p).get((i*9) + j - 1) + "");
                }
                ((TableLayout) nameRow.getParent()).removeView(nameRow);
                tempTableLayout.addView(nameRow);

            }
            ((LinearLayout) tempTableLayout.getParent()).removeView(tempTableLayout);
            linLayout.addView(tempTableLayout);

        }

        (findViewById(R.id.ss_finish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });

//        ((Button) findViewById(R.id.ss_export)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                export();
//            }
//        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scorecard_summary, menu);
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

//    private void export(){
//        File file = new File(getFilesDir().getAbsolutePath(), "scorecard.csv");
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = openFileOutput("scorecard.csv", Context.MODE_WORLD_READABLE);
//            outputStream.write("Hello World".getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Log.v("Stuff",getFilesDir().getAbsolutePath());
//    }

    private void sendMessage(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void goToMain(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
