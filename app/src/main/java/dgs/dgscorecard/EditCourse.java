package dgs.dgscorecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class EditCourse extends Activity {

    // database helper
    private DGSDatabaseHelper mDatabaseHelper;
    public static String EXTRA_MESSAGE = "edit_course_name";
    public static String NEW_COURSE = "edit_course_new";

    // database items list
    private List<Course> mCourse;
    private Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        mDatabaseHelper = new DGSDatabaseHelper(this);
        thisContext = this;
        mCourse = mDatabaseHelper.getAllCourseItems(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_courses_checkbox);
        CheckBox cb = new CheckBox(this);
        cb.setText("Select All");
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.edit_courses_checkbox);
                boolean check = ((CheckBox) v).isChecked();
                for(int i = 0; i < ll.getChildCount(); i++)
                    ((CheckBox) ll.getChildAt(i)).setChecked(check);
            }
        });
        ll.addView(cb);
        for(int i = 0; i < mCourse.size(); i++) {
            cb = new CheckBox(this);
            cb.setText(mCourse.get(i).getName());
            cb.setChecked(false);
            ll.addView(cb);
        }

        findViewById(R.id.ec_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.ec_edit_text_add);
                String name = et.getText().toString();
                if(name.equals(""))
                    return;
                Intent intent = new Intent(thisContext, ManualCourseAdd.class);
                intent.putExtra(EXTRA_MESSAGE, name);
                intent.putExtra(ManualCourseAdd.NEXT_ACTIVITY,"EditCourse");
                intent.putExtra(NEW_COURSE,true);
                startActivity(intent);
            }
        });

        findViewById(R.id.ec_modify_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.edit_courses_checkbox);
                boolean cont = true;
                String name = "";
                for(int i = 1; i < ll.getChildCount() && cont; i++){
                    CheckBox cb = (CheckBox) ll.getChildAt(i);
                    if(cb.isChecked()){
                        name = cb.getText()+"";
                        cont = false;
                    }
                }

                if(!cont) {
                    Intent intent = new Intent(thisContext, ManualCourseAdd.class);
                    intent.putExtra(EXTRA_MESSAGE, name);
                    intent.putExtra(ManualCourseAdd.NEXT_ACTIVITY, "EditCourse");
                    intent.putExtra(NEW_COURSE, false);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.ec_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.edit_courses_checkbox);
                ArrayList<CheckBox> cbs = new ArrayList<CheckBox>();
                for(int i = 1; i < ll.getChildCount(); i++){
                    CheckBox cb = (CheckBox) ll.getChildAt(i);
                    if(cb.isChecked()){
                        boolean deleted = mDatabaseHelper.deleteCourse(cb.getText().toString());
                        if(deleted)
                            cbs.add(cb);
                    }
                }
                for(CheckBox cb: cbs)
                    ll.removeView(cb);
                ((CheckBox) ll.getChildAt(0)).setChecked(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_course, menu);
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
    public void onBackPressed() {
        Intent setIntent = new Intent(this,MainActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(setIntent);
    }

}
