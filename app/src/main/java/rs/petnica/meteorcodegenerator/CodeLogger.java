package rs.petnica.meteorcodegenerator;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vladi on 07.08.2017.
 */

public class CodeLogger {

    public CodeLogger(int group) {
        this.group = group;
    }

    public void log(String code) {
        File log = getLog(group);

        try {
            FileWriter fileWriter = new FileWriter(log, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);
            int second = now.get(Calendar.SECOND);
            String time =
                    ((hour < 10 ? "0" : "") + hour) + ":" +
                    ((minute < 10 ? "0" : "") + minute) + ":" +
                    ((second < 10 ? "0" : "") + second);

            bufferedWriter.append(code + " " + time + '\n');

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CodeMark> load(int group) {
        File log = getLog(group);

        if (log.exists()) {
            try {
                FileReader fileReader = new FileReader(log);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                ArrayList<CodeMark> codeMarks = new ArrayList<>();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split(" ");
                    try {
                        codeMarks.add(new CodeMark(split[0], split[1]));
                    } catch (ArrayIndexOutOfBoundsException e) {}
                }

                bufferedReader.close();

                return codeMarks;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }

        return null;
    }

    private static File getLog(int group) {
        Calendar now = Calendar.getInstance();

        Calendar other = (Calendar) now.clone(), first, second;
        if (now.get(Calendar.HOUR_OF_DAY) > 15) {
            other.add(Calendar.DAY_OF_MONTH, 1);
            first = now;
            second = other;
        } else {
            other.add(Calendar.DAY_OF_MONTH, -1);
            first = other;
            second = now;
        }

        int year1, year2, month1, month2, day1, day2;

        year1 = first.get(Calendar.YEAR);
        year2 = second.get(Calendar.YEAR);

        month1 = first.get(Calendar.MONTH) + 1;
        month2 = second.get(Calendar.MONTH) + 1;

        day1 = first.get(Calendar.DAY_OF_MONTH);
        day2 = second.get(Calendar.DAY_OF_MONTH);

        String filename = "" + year1 + "_" + month1 + "_" + day1 + "-";
        filename += "" + year2 + "_" + month2 + "_" + day2 + "-";
        filename += "G" + (group + 1) + ".txt";

        return new File(Environment.getExternalStorageDirectory(), filename);
    }

    private int group;

}
