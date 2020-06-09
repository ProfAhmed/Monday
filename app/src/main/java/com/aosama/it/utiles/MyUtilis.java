package com.aosama.it.utiles;

import android.app.AlertDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aosama.it.R;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class MyUtilis {
    public static AlertDialog myDialog(Context context) {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(context).setTheme(R.style.CustomDialog).build();
        dialog.setCancelable(false);
        return dialog;
    }

    public static void showKeyboard(EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public static String parseDateWithAmPm(String startTime) {
// Get date from string

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = dateFormatter.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        String displayValue = timeFormatter.format(date);
        return displayValue;
    }

    public static String parseDate(String startTime) {
// Get date from string

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = dateFormatter.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String displayValue = timeFormatter.format(date);
        return displayValue;
    }

    public static String formateDate(String name) {
        String formattedDate = "";
        //2020-03-15T21:00:00.000Z
        formattedDate = StringUtils.substringBefore(name, "T");
        return formattedDate;
//        return name;
    }

    public static String formateDateTime(String name) {
        String formattedDate = "";
        String time = name.substring(name.indexOf('T') + 1);
        String[] times = time.split(":");
        String formattedTime = times[0] + ":" + times[1];
        //2020-03-15T21:00:00.000Z
        formattedDate = StringUtils.substringBefore(name, "T");

        return formattedDate + "  " + formattedTime;
    }

    public static void parsDateYYMMDD(String input) {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = inFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        ParseDate.dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        ParseDate.day = (String) DateFormat.format("dd", date); // 20
        ParseDate.monthString = (String) DateFormat.format("MMM", date); // Jun
        ParseDate.monthNumber = (String) DateFormat.format("MM", date); // 06
        ParseDate.year = (String) DateFormat.format("yyyy", date); // 2013

    }

    public static class ParseDate {
        public static String dayOfTheWeek, day, monthString, monthNumber, year;
    }

}
