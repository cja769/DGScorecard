package dgs.dgscorecard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CourseSelect extends Activity {

    private String course;
    public static final String EXTRA_MESSAGE = "Course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_select);

        final Button button = (Button) findViewById(R.id.cs_start);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
            }
        });
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

    public void sendMessage(View view) {
        Intent intent = new Intent(this, ManualCourseAdd.class);
        EditText courseEditText = (EditText)findViewById(R.id.cs_course_name);
        if(courseEditText.getText().toString().equals(""))
            return;
        intent.putExtra(EXTRA_MESSAGE, courseEditText.getText().toString());
        intent.putStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE, getIntent().getStringArrayListExtra(PlayerSelect.EXTRA_MESSAGE));
        startActivity(intent);


    }
}
