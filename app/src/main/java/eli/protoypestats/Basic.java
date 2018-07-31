package eli.protoypestats;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Basic extends AppCompatActivity {

    //Keep a log for debugging
    private static final String TAG = "BasicActivity";

    //stopwatch elements
    TextView textView3;
    Button start_button;
    Handler handler;
    long milliTime, startTime, timeBuff, updateTime = 0L;
    int mins, secs, millis;

    //stat recording elements
    Button error, kill, block, ace, receive_win, receive_loss;
    String matchTitle;
    File file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        /* Match Info */
        matchTitle = getIntent().getStringExtra("MATCH_TITLE");
//        Log.v(TAG, matchTitle);

        /* Stopwatch */
        textView3 = (TextView) findViewById(R.id.textView3);
        start_button = (Button) findViewById(R.id.start_button);

        handler = new Handler();

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTime == 0) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                } else {
                    // Don't want the user to accidentally reset the stopwatch
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(Basic.this);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("The clock has already been started. Are you sure you want to reset?");

                    builder.setPositiveButton(R.string.yes_time, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startTime = SystemClock.uptimeMillis();
                            handler.postDelayed(runnable, 0);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.no_time, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        /* Stat recording */
        error = (Button) findViewById(R.id.error);
        kill = (Button) findViewById(R.id.kill);
        block = (Button) findViewById(R.id.block);
        ace = (Button) findViewById(R.id.ace);
        receive_loss = (Button) findViewById(R.id.receive_loss);
        receive_win = (Button) findViewById(R.id.receive_win);


        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonHandler("Error");
            }
        });

        kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonHandler("Kill");
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonHandler("Block");
            }
        });

        ace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonHandler("Ace");
            }
        });

        if (isExternalStorageWritable() && isExternalStorageReadable()) {
//            Log.v(TAG, "Ready to write to file");

            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/download");
//            Log.d(TAG, dir.toString());
            file = new File(dir, matchTitle + ".txt");
        }


    }

    private void buttonHandler(final String type) {
        final String time = textView3.getText().toString();
//        Log.v(TAG, time);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Basic.this);
        builder.setTitle("Choose a player");

        // add a list
        builder.setItems(getResources().getStringArray(R.array.players), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.v(TAG, getResources().getStringArray(R.array.players)[which]);
                String toLog = compose(type, time, getResources().getStringArray(R.array.players)[which]);
                logStat(toLog);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method writes the text in the file
     * @param entry The text to write in the file
     */
    private void logStat(String entry) {

        if (isExternalStorageWritable() && isExternalStorageReadable()) {
//            Log.v(TAG, "Ready to write to file");

            try {
                //use filewriter so it moves down lines
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.println(entry);
                pw.close();

                //let the user know they successfully logged a stat
                Snackbar.make(findViewById(R.id.constraintLayout), "Logged", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "IOE exception" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "ERROR, not prepared to write to file. Issue with external storage.");
        }

    }

    /**
     * Method writes the stat that should be logged in the text file
     * @param type The stat
     * @param timeStamp When the stat occured
     * @param name Who did it
     */
    private String compose(String type, String timeStamp, String name) {
        return type + " by " + name + " at " + timeStamp;
    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.v(TAG, "Ext. storage available to write");
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.v(TAG, "Ext. storage available to read");
            return true;
        }
        return false;
    }


    public Runnable runnable = new Runnable() {

        public void run() {

            milliTime = SystemClock.uptimeMillis() - startTime;

            updateTime = timeBuff + milliTime;

            //time math
            secs = (int) (updateTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            millis = (int) (updateTime % 1000);

            textView3.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", millis));

            handler.postDelayed(this, 0);
        }

    };

    /**
     * Requires confirmation for back button
     * Don't want users accidentally resetting the stopwatch
     */
    @Override
    public void onBackPressed() {

        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.constraintLayout),
                "Are you sure you want to go back? The stopwatch will reset.", Snackbar.LENGTH_LONG).setAction("Go Back", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mySnackbar.show();
    }
}

