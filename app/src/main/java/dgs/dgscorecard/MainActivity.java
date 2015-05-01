package dgs.dgscorecard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button newCardButton = (Button) findViewById(R.id.main_new_scorecard);
        newCardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(PlayerSelect.class);
            }
        });


        final Button testSummary = (Button) findViewById(R.id.main_test_summary);
        testSummary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(ScorecardSummary.class);
            }
        });

        findViewById(R.id.main_edit_scorecard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(EditScorecard.class);
            }
        });

        findViewById(R.id.main_edit_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(EditCourse.class);
            }
        });

        findViewById(R.id.main_edit_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(EditPlayer.class);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void sendMessage(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
