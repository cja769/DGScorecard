package dgs.dgscorecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.grantland.widget.AutofitLayout;
import me.grantland.widget.AutofitTextView;


public class ManualCourseAdd extends Activity {

    private int holes;
    private Map<Button,TextView> parFields;
    private Map<Integer,Integer> pars;
    private Map<Button, Integer> buttonPars;
    public static final String EXTRA_MESSAGE = "Pars";
    private SharedPreferences mPrefs;
    private int num_courses;

    private DGSDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_course_add);

        parFields = new HashMap<Button, TextView>();
        pars = new HashMap<Integer, Integer>();
        buttonPars = new HashMap<Button, Integer>();
        AutofitTextView title = (AutofitTextView) findViewById(R.id.mca_title);
        title.setText(getIntent().getStringExtra(CourseSelect.EXTRA_MESSAGE));
        Button moreHoles = (Button) findViewById(R.id.mca_more_holes);
        Button lessHoles = (Button) findViewById(R.id.mca_less_holes);
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

        moreHoles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addHoles(1, (LinearLayout) findViewById(R.id.mca_hole_container));
                TextView holeText = (TextView) findViewById(R.id.mca_total_holes);
                holes++;
                holeText.setText(holes + "");
            }
        });

        lessHoles.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(holes-1 <= 0)
                    return;
                addHoles(-1,(LinearLayout) findViewById(R.id.mca_hole_container));
                TextView holeText = (TextView) findViewById(R.id.mca_total_holes);
                holes--;
                holeText.setText(holes+"");
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

    @Override
    protected void onStop(){
        super.onStop();

        //Save Player Names
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("num_courses", num_courses);
        //if(prevPlayers.size() > 0)
        //ed.putStringSet("mPlayerNames", new HashSet<String>(prevPlayers));
        ed.apply();
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
            TextView par = (TextView) item.findViewById(R.id.mca_hole_par);
            hole.setText("Hole " + startHoleNumber + "");
            pars.put(startHoleNumber,3);
            Button less = (Button) item.findViewById(R.id.mca_less_par);
            Button more = (Button) item.findViewById(R.id.mca_more_par);
            parFields.put(more, par);
            parFields.put(less, par);
            buttonPars.put(more, startHoleNumber);
            buttonPars.put(less, startHoleNumber);
            less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView hole = parFields.get(v);
                    Integer holeNumber = buttonPars.get(v);
                    Integer par = pars.get(holeNumber);
                    pars.put(holeNumber, --par);
                    hole.setText(par+"");
                }
            });
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView hole = parFields.get(v);
                    Integer holeNumber = buttonPars.get(v);
                    Integer par = pars.get(holeNumber);
                    pars.put(holeNumber, ++par);
                    hole.setText(par+"");
                }
            });

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


        EditText courseEditText = (EditText)findViewById(R.id.cs_course_name);
        String name = courseEditText.getText().toString();

        Course newCourse = new Course(holes, allPars, name, this, num_courses);

        ++num_courses;

        ArrayList<Course> courseList = new ArrayList<>();
        courseList.add(newCourse);

        mDatabaseHelper.addCourseItems(courseList);

        intent.putIntegerArrayListExtra(EXTRA_MESSAGE,allPars);
        intent.putExtra(CourseSelect.EXTRA_MESSAGE, getIntent().getStringExtra(CourseSelect.EXTRA_MESSAGE));
        intent.putStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE, getIntent().getStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE));
        startActivity(intent);

    }
}
