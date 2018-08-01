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

/**
 * This activity is the second screen of the app
 * It handlers the stopwatch and takes user input from stats
 * Then it uses the StatLoggerSingleton to create a text file and write the stats to it
 */
public class Basic extends AppCompatActivity {

    //Keep a log for debugging
    private static final String TAG = "BasicActivity";

    //general elements
    LinkedList<Set> match;
    Button endSet;
    StatLoggerSingleton statLogger;
    File file;

    //stopwatch elements
    TextView textView3;
    Button startTimer;
    Handler handler;
    long milliTime, startTime, timeBuff, updateTime = 0L;
    int mins, secs, millis;

    //stat recording elements
    Button error, kill, block, ace, receiveWin, receiveLoss, ofNote, serveWin, serveLoss;
    String matchTitle, homeTeam, awayTeam;
    int receiveWins = 0, receiveLosses = 0, serveWins = 0, serveLosses = 0;


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
        ofNote = (Button) findViewById(R.id.of_note);
        receiveWin = (Button) findViewById(R.id.receive_win);
        receiveLoss = (Button) findViewById(R.id.receive_loss);
        serveWin = (Button) findViewById(R.id.serve_win);
        serveLoss = (Button) findViewById(R.id.serve_loss);


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

        ofNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = statLogger.writeToFile(file, "Of Note at " + textView3.getText().toString());
                if (success) {
                    Snackbar.make(findViewById(R.id.constraintLayout), "Something of note logged", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        receiveWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveWins++;  //win from receiving serve
                Snackbar.make(findViewById(R.id.constraintLayout), "Added a receive win", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        receiveLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveLosses++;  //loss from receiving serve
                boolean success = statLogger.writeToFile(file, "Receive loss at " + textView3.getText().toString());
                if (success) {
                    Snackbar.make(findViewById(R.id.constraintLayout), "Added a receive loss", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        serveWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serveWins++;  //loss from receiving serve
                Snackbar.make(findViewById(R.id.constraintLayout), "Added a serve win", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        serveLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serveLosses++;  //loss from receiving serve
                Snackbar.make(findViewById(R.id.constraintLayout), "Added a serve loss", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
                statLogger.writeToFile(file,"Serve Wins: " + serveWins);
                statLogger.writeToFile(file, "Serve Losses: " + serveLosses);

                //log the final scores
                getScores();

                statLogger.writeToFile(file, ""); //for nice formatting
            }
        });
    }

    /**
     * When the user closes the timer give a summary of the match
     */
    private void reviewMatch() {
        statLogger.writeToFile(file, "");
        statLogger.writeToFile(file, "Clock Stopped");
        for (Set s : match) {
            statLogger.writeToFile(file, s.toString());
        }
        statLogger.writeToFile(file, "");
    }

    /**
     * This method handles individual stats
     * @param type The stat: block, ace, kill, etc.
     */
    private void buttonHandler(final String type) {
        //record the timestamp as soon as the button is pressed
        final String time = textView3.getText().toString();

        //throw an with a list of players so the user can assign a player to the stat
        AlertDialog.Builder builder = new AlertDialog.Builder(Basic.this);
        builder.setTitle("Assign " + type + " to player");

        // add the the list
        builder.setItems(getResources().getStringArray(R.array.players), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //create the string we want to write to the text file
                String toLog = compose(type, time, getResources().getStringArray(R.array.players)[which]);
                //write it using the StatLoggerSingleton
                boolean successLog = statLogger.writeToFile(file, toLog);
                //if everything works out let the user know their entry was recorded
                if (successLog) {
                    Snackbar.make(findViewById(R.id.constraintLayout), "Logged", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * This method requests the final score of the set from the user and logs it
     */
    private void getScores() {
        //create a set object
        final Set curSet = new Set();

        //Away team score

        //Throw an alert dialog box with a textfield asking the user for the score
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
        /* this will be used second because the other score input dialog will be created and shown
        on top of this one */
        AlertDialog dialog1 = builder1.create();
        dialog1.show();


        //Home team score
        final EditText scoreHomeEntry = new EditText(this);
        scoreHomeEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Score");
        builder.setMessage("What did " + homeTeam + " score?");
        builder.setView(scoreHomeEntry); //add the EditText field to the alert dialog
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set the param in the set POJO
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

        //Handle miscellaneous set (non-individual) stats

        //add the set to the match
        curSet.setTeamOne(homeTeam);
        curSet.setTeamTwo(awayTeam);
        curSet.setReceiveWin(receiveWins);
        curSet.setReceiveLoss(receiveLosses);
        curSet.setServeWin(serveWins);
        curSet.setServeLoss(serveLosses);

        match.add(curSet);

        //reset the stat specific things
        //this just sets the counters to zero
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


    /**
     * This is the stopwatch
     */
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
        serveWins = 0;
        serveLosses = 0;
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

