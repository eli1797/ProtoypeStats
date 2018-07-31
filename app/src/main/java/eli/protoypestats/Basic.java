package eli.protoypestats;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;

import eli.protoypestats.dummy.Set;

public class Basic extends AppCompatActivity {

    //Keep a log for debugging
    private static final String TAG = "BasicActivity";

    //general elements
    LinkedList<Set> match;
    Button endSet;
    StatLoggerSingleton statLogger;

    //stopwatch elements
    TextView textView3;
    Button startTimer;
    Handler handler;
    long milliTime, startTime, timeBuff, updateTime = 0L;
    int mins, secs, millis;

    //stat recording elements
    Button error, kill, block, ace, receiveWin, receiveLoss;
    String matchTitle, homeTeam, awayTeam;
    File file;
    int receiveWins = 0, receiveLosses = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);


        /* Match Info */
        homeTeam = getIntent().getStringExtra("HOME_TEAM");
        awayTeam = getIntent().getStringExtra("AWAY_TEAM");
        String matchTitle = homeTeam + "_vs_" + awayTeam;
        Log.d(TAG, matchTitle);

        //get singleton and setup logging
        statLogger = StatLoggerSingleton.getInstance();
        file = statLogger.createFile(matchTitle);


//        Log.v(TAG, matchTitle);
        endSet = (Button) findViewById(R.id.set_over);

        match = new LinkedList<>();

        /* Stopwatch */
        textView3 = (TextView) findViewById(R.id.textView3);
        startTimer = (Button) findViewById(R.id.start_button);

        handler = new Handler();

        startTimer.setOnClickListener(new View.OnClickListener() {
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
                            reviewMatch();
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
        receiveWin = (Button) findViewById(R.id.receive_win);
        receiveLoss = (Button) findViewById(R.id.receive_loss);


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

        receiveWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveWins++;  //win from receiving serve
            }
        });

        receiveLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveLosses++;  //loss from receiving serve
            }
        });

        endSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "end set clicked");
                statLogger.writeToFile(file, ""); //for nice formatting
                statLogger.writeToFile(file, "SET OVER");
                //log the set related stats
                statLogger.writeToFile(file,"Receive Wins: " + receiveWins);
                statLogger.writeToFile(file, "Receive Losses: " + receiveLosses);

                //log the final scores
                getScores();


                statLogger.writeToFile(file, ""); //for nice formatting
            }
        });




    }

    /**
     * When the user closes the timer
     */
    private void reviewMatch() {
        statLogger.writeToFile(file, "");
        statLogger.writeToFile(file, "Clock Stopped");
        for (Set s : match) {
            statLogger.writeToFile(file, s.toString());
        }
        statLogger.writeToFile(file, "");
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
                statLogger.writeToFile(file, toLog);

            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * This method requests the final score from the user and logs it
     */
    private void getScores() {
        final Set curSet = new Set();

        //Away team score
        final EditText scoreAwayEntry = new EditText(this);
        scoreAwayEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Input Score");
        builder1.setMessage("What did " + awayTeam + " score?");
        builder1.setView(scoreAwayEntry);
        builder1.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curSet.setTeamTwoScore(Integer.parseInt(scoreAwayEntry.getText().toString()));
                statLogger.writeToFile(file, awayTeam + ": " + scoreAwayEntry.getText().toString());
                statLogger.writeToFile(file,"");
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                statLogger.writeToFile(file,"Action Cancelled");
                dialog.cancel();
            }
        });

        // create and show the alert dialog
        //this will be used second because the other score input dialog will be created and shown
        // on top of this one
        AlertDialog dialog1 = builder1.create();
        dialog1.show();


        //Home team score
        final EditText scoreHomeEntry = new EditText(this);
        scoreHomeEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Score");
        builder.setMessage("What did " + homeTeam + " score?");
        builder.setView(scoreHomeEntry);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curSet.setTeamOneScore(Integer.parseInt(scoreHomeEntry.getText().toString()));

                //log the score
                statLogger.writeToFile(file,homeTeam + ": " + scoreHomeEntry.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                //@TODO: Design the cancel button to work
                statLogger.writeToFile(file,"Action Cancelled");
                dialog.cancel();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //add the set to the match
        curSet.setTeamOne(homeTeam);
        curSet.setTeamTwo(awayTeam);
        curSet.setReceiveWin(receiveWins);
        curSet.setReceiveLoss(receiveLosses);

        match.add(curSet);

        //reset the stat specific things
        resetSetStats();

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
     * Resets set specific stats the renew each set
     */
    private void resetSetStats () {
        receiveLosses = 0;
        receiveWins = 0;
    }

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

