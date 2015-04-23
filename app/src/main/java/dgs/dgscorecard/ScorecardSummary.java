package dgs.dgscorecard;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class ScorecardSummary extends Activity {

    private ArrayList<TextView> allTextViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard_summary);

        TableRow holeRow = (TableRow) findViewById(R.id.ss_hole_row);
        TableRow parRow = (TableRow) findViewById(R.id.ss_par_row);
        allTextViews = new ArrayList<TextView>();

        for(int i = 0; i < 18; i++) {
            TextView tv = new TextView(this);
            TextView tv2 = new TextView(this);
            tv.setText((i+1)+"");
            tv2.setText(3+"");
            holeRow.addView(tv);
            parRow.addView(tv2);
            allTextViews.add(tv);
            allTextViews.add(tv2);

        }

        View myView=findViewById(R.id.ss_scorecard_table);
        myView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for(TextView tv: allTextViews) {
                    TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
                    params.leftMargin = 5;
                    params.rightMargin = 5;
                    tv.setLayoutParams(params);
                }
            }
        });
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
}
