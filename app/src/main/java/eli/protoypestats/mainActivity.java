package eli.protoypestats;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * This is the first screen of the application
 * The user inputs two team names and some other info
 * It also checks permissions as this app needs to write to text files
 * The team names are is passed onto the next activity for logging
 */
public class mainActivity extends AppCompatActivity {

    //Keep a log for debugging
    private static final String TAG = "MainActivity";

    //for requesting write to external storage permissions
    private static final int PERMISSION_REQUEST_CODE = 1;

    public int outOf;

    // UI references
    private EditText homeTeam, awayTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeTeam = (EditText) findViewById(R.id.homeTeam);
        homeTeam.setGravity(Gravity.CENTER);

        awayTeam = (EditText) findViewById(R.id.awayTeam);
        awayTeam.setGravity(Gravity.CENTER);

        //Button for signing in
        Button goToStatsButton = (Button) findViewById(R.id.goToStatsButton);
        goToStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToStats(view);
            }
        });

        permissionCheck();
    }

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(mainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mainActivity.this, "Write External Storage permission allows us to do store stats. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(mainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e(TAG, "Permission Denied, You cannot use local drive .");
                }
                break;
        }
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

        //@TODO: make it an option to add or drop players from the team here
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Currently no settings", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb3Sets:
                if (checked)
                    // The match will be best of three sets
                    outOf = 3;
                    break;
            case R.id.rb5Sets:
                if (checked)
                    // The match will be best of five sets
                    outOf = 5;
                    break;
        }
    }

    /**
     * This method validates the user entry then moves to the next activity
     * @param view The current view
     */
    void goToStats(View view) {

        homeTeam.setError(null);
        awayTeam.setError(null);

        //ensure there are valid entries in the team name textfields
        if (!validateForm()) {
            return;
        }

        //at this point the team names are valid
        //go to the next screen
        Intent in = new Intent(mainActivity.this, Basic.class);
        in.putExtra("HOME_TEAM", homeTeam.getText().toString());
        in.putExtra("AWAY_TEAM", awayTeam.getText().toString());
        startActivity(in);

    }

    /**
     * This method ensures the users text entry conforms to certain rules
     * @return true if it does
     */
    private boolean validateForm() {
        boolean valid = true;

        //check that team names aren't empty
        String homeTeamName = homeTeam.getText().toString();
        if (TextUtils.isEmpty(homeTeamName)) {
            homeTeam.setError("Home Team Name Required");
            valid = false;
        } else {
            homeTeam.setError(null);
        }

        String awayTeamName = awayTeam.getText().toString();
        if (TextUtils.isEmpty(awayTeamName)) {
            awayTeam.setError("Away Team Name Required");
            valid = false;
        } else if (homeTeamName.equals(awayTeamName)) {
            // if these are the same how can teams be differentiated?
            awayTeam.setError("Cannot have same name as home team");
            valid = false;
        } else {
            awayTeam.setError(null);
        }


        return valid;
    }
}
