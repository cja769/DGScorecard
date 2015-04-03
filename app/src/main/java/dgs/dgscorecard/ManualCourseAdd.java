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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ManualCourseAdd extends ActionBarActivity {

    private int holes;
    private Map<NumberPicker,TextView> parFields;
    private Map<Integer,Integer> pars;
    private Map<NumberPicker, Integer> numPickerHoles;
    public static final String EXTRA_MESSAGE = "Pars";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_course_add);

        parFields = new HashMap<NumberPicker, TextView>();
        pars = new HashMap<Integer, Integer>();
        numPickerHoles = new HashMap<NumberPicker, Integer>();
        TextView title = (TextView) findViewById(R.id.mca_title);
        title.setText(getIntent().getStringExtra(CourseSelect.EXTRA_MESSAGE));
        NumberPicker np = (NumberPicker) findViewById(R.id.mca_number_picker);
        np.setMinValue(1);
        np.setMaxValue(50);
        np.setValue(18);
        holes = 18;
        LinearLayout ll = drawHoles(18,1);
        LinearLayout holeContainer = (LinearLayout) findViewById(R.id.mca_hole_container);
        holeContainer.removeAllViews();
        int childCount = ll.getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = ll.getChildAt(0);
            ll.removeView(child);
            holeContainer.addView(child);
        }
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                addHoles(newVal-oldVal,(LinearLayout) findViewById(R.id.mca_hole_container));
                holes = newVal;
            }
        });

        final Button startButton = (Button) findViewById(R.id.mca_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual_course_add, menu);
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

    private void addHoles(int numHoles, LinearLayout ll){
        if(numHoles < 0){
            numHoles = Math.abs(numHoles);
            for(int i = 0; i < numHoles; i++){
                int max = ll.getChildCount();
                ll.removeView(ll.getChildAt(max-1));
                pars.remove(holes-i);
            }

        }
        else{
            LinearLayout temp = drawHoles(numHoles,holes+1);
            for(int i = 0; i < numHoles; i++){
                View child = temp.getChildAt(0);
                temp.removeView(child);
                ll.addView(child);
            }

        }

    }

    private LinearLayout drawHoles(int numHoles, int startHoleNumber){
        LinearLayout ll = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i < numHoles; i++){
            View item = inflater.inflate(R.layout.activity_manual_course_add, null);
            LinearLayout singleHole = (LinearLayout) item.findViewById(R.id.mca_single_hole);
            LinearLayout parent = (LinearLayout) item.findViewById(R.id.mca_hole_container);
            TextView hole = (TextView) item.findViewById(R.id.mca_hole);
            hole.setText("Hole " + startHoleNumber + "");
            TextView par = (TextView) item.findViewById(R.id.mca_par);
            par.setText("Par 3");
            pars.put(startHoleNumber,3);
            NumberPicker np = (NumberPicker) item.findViewById(R.id.mca_hole_picker);
            parFields.put(np,par);
            np.setMinValue(1);
            np.setMaxValue(20);
            np.setValue(3);
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    parFields.get(picker).setText("Par " + newVal);
                    Integer i = numPickerHoles.get(picker);
                    pars.put(numPickerHoles.get(picker), newVal);
                }
            });
            numPickerHoles.put(np,startHoleNumber);
            parent.removeView(singleHole);
            ll.addView(singleHole);
            startHoleNumber++;
        }
        return ll;
    }

    private void sendMessage(View v){
        Intent intent = new Intent(this, ScorecardActivity.class);
        Set<Integer> keys = pars.keySet();
        int[] k = new int[keys.size()];
        int i = 0;
        for(Integer key: keys){

            if(key != null) {
                k[i] = key.intValue();
                i++;
            }
        }
        Arrays.sort(k);
        ArrayList<Integer> allPars = new ArrayList<Integer>();
        for(i = 0; i < k.length; i++)
            allPars.add(pars.get(k[i]));

        intent.putIntegerArrayListExtra(EXTRA_MESSAGE,allPars);
        intent.putExtra(CourseSelect.EXTRA_MESSAGE, getIntent().getStringExtra(CourseSelect.EXTRA_MESSAGE));
        intent.putStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE, getIntent().getStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE));
        startActivity(intent);

    }
}
