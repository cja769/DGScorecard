package dgs.dgscorecard;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.List;


public class EditCourse extends Activity {

    // database helper
    private DGSDatabaseHelper mDatabaseHelper;

    // database items list
    private List<Course> mCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        mDatabaseHelper = new DGSDatabaseHelper(this);
        mCourse = mDatabaseHelper.getAllCourseItems(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_courses_checkbox);
        for(int i = 0; i < mCourse.size(); i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(mCourse.get(i).getName());
            cb.setChecked(false);
            ll.addView(cb);
        }
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
}
