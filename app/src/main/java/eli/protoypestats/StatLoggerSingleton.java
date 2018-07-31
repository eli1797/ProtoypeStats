package eli.protoypestats;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Eager singleton charged with writing to the text file
 * Created by Elijah Bailey on 7/31/2018.
 */

public class StatLoggerSingleton {

    //Keep a log for debugging
    private static final String TAG = "StatLoggerSingleton";

    //one static instance
    private static final StatLoggerSingleton ourInstance = new StatLoggerSingleton();

    public static StatLoggerSingleton getInstance() {
        return ourInstance;
    }

    //private constructor to do prevent other classes from creating
    private StatLoggerSingleton() {
    }

    /**
     * Method writes the text in the file
     * @param entry The text to write in the file
     */
    boolean writeToFile(File file, String entry) {

        if (isExternalStorageWritable() && isExternalStorageReadable()) {
//            Log.v(TAG, "Ready to write to file");

            try {
                //use filewriter so it moves down lines
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.println(entry);
                pw.close();

                Log.v(TAG, "Wrote \"" + entry + "\" to file: " + file.toString());

                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                Log.d(TAG, "IOE exception" + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            Log.d(TAG, "ERROR, not prepared to write to file. Issue with external storage.");
            return false;
        }
    }

    File createFile (String fileName) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/download");
//            Log.d(TAG, dir.toString());
        return new File(dir, fileName + ".txt");
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.v(TAG, "Ext. storage available to write");
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.v(TAG, "Ext. storage available to read");
            return true;
        }
        return false;
    }
}
