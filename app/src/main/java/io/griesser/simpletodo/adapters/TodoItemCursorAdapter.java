package io.griesser.simpletodo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.griesser.simpletodo.R;
import io.griesser.simpletodo.models.TodoItem;

import static android.R.id.list;
import static io.griesser.simpletodo.R.id.editTextDueTo;
import static io.griesser.simpletodo.R.id.textViewName;

/**
 * Created by rolan on 26/07/2017.
 */

public class TodoItemCursorAdapter extends CursorAdapter {

    private SimpleDateFormat dateFormat;

    public TodoItemCursorAdapter(Context context, Cursor c, int flags, String dateFormat) {
        super(context, c, flags);
        this.dateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private LayoutInflater mInflater;


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        FlowCursor flowCursor = (FlowCursor)cursor;

        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewDueDate = (TextView) view.findViewById(R.id.textViewDueDate);
        TextView textViewPriority = (TextView) view.findViewById(R.id.textViewPriority);
        CheckBox checkBoxDone = (CheckBox) view.findViewById(R.id.checkBoxDone);

        //textViewDueDate.setText(sdf.format(cursor.get));

        textViewName.setText(flowCursor.getStringOrDefault("name"));
        long dueTo = flowCursor.getLongOrDefault("dueTo");
        textViewDueDate.setText(dateFormat.format(new Date(dueTo)));
        int priority = flowCursor.getIntOrDefault("priority");
        textViewPriority.setText(TodoItem.getPriorityText(priority));
        checkBoxDone.setChecked(flowCursor.getIntOrDefault("status") == 1);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_todo, parent, false);
    }
}
