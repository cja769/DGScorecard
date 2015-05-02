package dgs.dgscorecard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;


public class CourseSelect extends Activity {

    private String course;
    public static final String EXTRA_MESSAGE = "Course";
    private DGSDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_select);

        (findViewById(R.id.cs_start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScorecard();
            }
        });

        (findViewById(R.id.cs_add)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addCourse();
            }
        });

        LinearLayout checkboxFields = (LinearLayout) findViewById(R.id.cs_checkbox_field);
        mDatabaseHelper = new DGSDatabaseHelper(this);
        List<Course> allCourses = mDatabaseHelper.getAllCourseItems(this);
        for(Course c: allCourses){
            CheckBox cb = new CheckBox(this);
            cb.setText(c.getName());
            cb.setChecked(false);
            checkboxFields.addView(cb);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout checkBoxFields = (LinearLayout) findViewById(R.id.cs_checkbox_field);
                    for(int i = 0; i < checkBoxFields.getChildCount(); i++){
                        View child = checkBoxFields.getChildAt(i);
                        if(!child.equals(v))
                            ((CheckBox) child).setChecked(false);
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_select, menu);
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

    private void addCourse(){
        Intent intent = new Intent(this, ManualCourseAdd.class);
        EditText courseEditText = (EditText)findViewById(R.id.cs_enter_name);
        if(courseEditText.getText().toString().equals(""))
            return;
        intent.putExtra(EXTRA_MESSAGE, courseEditText.getText().toString());
        intent.putExtra(ManualCourseAdd.NEXT_ACTIVITY, "CourseSelect");
        intent.putExtra(PlayerSelect.EXTRA_MESSAGE,getIntent().getStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE));
        startActivity(intent);
    }

    public void startScorecard() {
        Intent intent = new Intent(this, ScorecardActivity.class);
        LinearLayout checkboxFields = (LinearLayout) findViewById(R.id.cs_checkbox_field);
        String c = "";
        for(int i = 0; i < checkboxFields.getChildCount(); i++){
            CheckBox course = (CheckBox) checkboxFields.getChildAt(i);
            if(course.isChecked()){
                c = course.getText().toString();
                break;
            }
        }
        if(c.equals("")) return;

        intent.putExtra(EXTRA_MESSAGE,c);

        intent.putStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE, getIntent().getStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE));
        startActivity(intent);
    }
}
