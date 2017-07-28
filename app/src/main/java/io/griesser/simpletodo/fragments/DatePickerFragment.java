package io.griesser.simpletodo.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rolan on 27/07/2017.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    public DatePickerFragment() {

    }

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date = (Date)getArguments().getSerializable(ARG_DATE);

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);

    }
}
