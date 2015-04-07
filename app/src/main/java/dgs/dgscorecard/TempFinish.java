package dgs.dgscorecard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class TempFinish extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_finish);

        final Button finishButton = (Button) findViewById(R.id.tf_new_scorecard);
        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(PlayerSelect.class);
            }
        });

        final Button mainButton = (Button) findViewById(R.id.tf_main);
        mainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(MainActivity.class);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temp_finish, menu);
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
