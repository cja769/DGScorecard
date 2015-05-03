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
import java.util.List;
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
    public static final String NEXT_ACTIVITY = "Next";
    private SharedPreferences mPrefs;
    private int num_courses;
    private Class nextClass;
    private boolean newCourse;

    private DGSDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_course_add);

        mDatabaseHelper = new DGSDatabaseHelper(this);
        mPrefs = getSharedPreferences("CourseSelect_prefs", MODE_PRIVATE);
        num_courses = mPrefs.getInt("num_courses",0);
        parFields = new HashMap<Button, TextView>();
        pars = new HashMap<Integer, Integer>();
        buttonPars = new HashMap<Button, Integer>();
        AutofitTextView title = (AutofitTextView) findViewById(R.id.mca_title);
        Button moreHoles = (Button) findViewById(R.id.mca_more_holes);
        Button lessHoles = (Button) findViewById(R.id.mca_less_holes);

        ArrayList<Integer> pars = new ArrayList<>();
        for(int i = 0; i < 18; i++) pars.add(3);

        final Button startButton = (Button) findViewById(R.id.mca_start);
        nextClass = ScorecardActivity.class;
        String c = getIntent().getStringExtra(ManualCourseAdd.NEXT_ACTIVITY);
        if(c.equals("CourseSelect") || c.equals("EditCourse")){
            startButton.setText("Finish");
            if(c.equals("CourseSelect")) {
                nextClass = CourseSelect.class;
                title.setText(getIntent().getStringExtra(CourseSelect.EXTRA_MESSAGE));
            }
            else {
                nextClass = EditCourse.class;
                String courseName = getIntent().getStringExtra(EditCourse.EXTRA_MESSAGE);
                title.setText(courseName);
                newCourse = getIntent().getBooleanExtra(EditCourse.NEW_COURSE, true);
                if(!newCourse)
                    pars = getCoursePars(courseName);
            }
        }
        holes = 18;
        LinearLayout ll = drawHoles(18,1, pars);
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
                ArrayList<Integer> pars = new ArrayList<Integer>();
                pars.add(3);
                addHoles(1, (LinearLayout) findViewById(R.id.mca_hole_container), pars);
                TextView holeText = (TextView) findViewById(R.id.mca_total_holes);
                holes++;
                holeText.setText(holes + "");
            }
        });

        lessHoles.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(holes-1 <= 0)
                    return;
                addHoles(-1,(LinearLayout) findViewById(R.id.mca_hole_container), new ArrayList<Integer>());
                TextView holeText = (TextView) findViewById(R.id.mca_total_holes);
                holes--;
                holeText.setText(holes+"");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(nextClass);
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

    private void addHoles(int numHoles, LinearLayout ll, ArrayList<Integer> pars){
        if(numHoles < 0){
            numHoles = Math.abs(numHoles);
            for(int i = 0; i < numHoles; i++){
                int max = ll.getChildCount();
                ll.removeView(ll.getChildAt(max-1));
                pars.remove(holes-i);
            }

        }
        else{
            LinearLayout temp = drawHoles(numHoles,holes+1, pars);
            for(int i = 0; i < numHoles; i++){
                View child = temp.getChildAt(0);
                temp.removeView(child);
                ll.addView(child);
            }

        }

    }

    private LinearLayout drawHoles(int numHoles, int startHoleNumber, ArrayList<Integer> coursePars){
        LinearLayout ll = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i < numHoles; i++){
            View item = inflater.inflate(R.layout.activity_manual_course_add, null);
            LinearLayout singleHole = (LinearLayout) item.findViewById(R.id.mca_single_hole);
            LinearLayout parent = (LinearLayout) item.findViewById(R.id.mca_hole_container);
            TextView hole = (TextView) item.findViewById(R.id.mca_hole);
            TextView par = (TextView) item.findViewById(R.id.mca_hole_par);
            hole.setText("Hole " + startHoleNumber + "");
            par.setText(coursePars.get(i)+"");
            pars.put(startHoleNumber, coursePars.get(i));
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

    private void sendMessage(Class c){
        Intent intent = new Intent(this, c);
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


        me.grantland.widget.AutofitTextView courseEditText = (me.grantland.widget.AutofitTextView)findViewById(R.id.mca_title);
        String name = courseEditText.getText().toString();

        Course course = new Course(holes, allPars, name, this, num_courses);

        ++num_courses;

        List<Course> courseList = new ArrayList<>();
        courseList.add(course);

        if(newCourse)
            mDatabaseHelper.addCourseItems(courseList);
        else
            mDatabaseHelper.updateCourse(course);
        ArrayList<String> courses = mDatabaseHelper.dumpCourses();
        for(String s: courses){
            Log.v(s.substring(0,s.indexOf(":")), s.substring(s.indexOf(":")+1));
        }

        if(c == CourseSelect.class)
            intent.putStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE,getIntent().getStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE));
        startActivity(intent);

    }

    private ArrayList<Integer> getCoursePars(String course) {
        Course c = mDatabaseHelper.getCourseByName(course);
        return c.getPars();

    }
}
