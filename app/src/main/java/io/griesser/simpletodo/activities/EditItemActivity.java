package io.griesser.simpletodo.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.griesser.simpletodo.R;
import io.griesser.simpletodo.fragments.DatePickerFragment;
import io.griesser.simpletodo.models.TodoItem;
import io.griesser.simpletodo.models.TodoItem_Table;

import static android.R.attr.id;
import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String DATE_PATTERN = "MM/dd/yyyy";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, Locale.US);

    private TextInputEditText editTextDueTo;
    private TextInputEditText editTextName;
    private TextInputEditText editTextDescription;
    private Spinner spinnerPriority;
    private Spinner spinnerStatus;

    private Calendar calendar;
    private TodoItem todoItem;

    private ArrayList<String> states;
    private ArrayAdapter<String> statesAdapter;

    private ArrayList<String> priorities;
    private ArrayAdapter<String> prioritiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editTextName = (TextInputEditText)findViewById(R.id.editTextName);
        editTextDueTo = (TextInputEditText)findViewById(R.id.editTextDueTo);
        editTextDescription = (TextInputEditText)findViewById(R.id.editTextDescrition);
        spinnerPriority = (Spinner)findViewById(R.id.spinnerPriority);
        spinnerStatus = (Spinner)findViewById(R.id.spinnerStatus);

        calendar = Calendar.getInstance();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        priorities = new ArrayList<>();
        priorities.add("High");
        priorities.add("Normal");
        priorities.add("Low");
        prioritiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        prioritiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(prioritiesAdapter);


        states = new ArrayList<>();
        states.add("To-Do");
        states.add("Done");
        statesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statesAdapter);

        if(getIntent().hasExtra(MainActivity.EXTRA_TODOITEM)) {
            todoItem = (TodoItem)getIntent().getExtras().getSerializable(MainActivity.EXTRA_TODOITEM);
            editTextName.setText(todoItem.getName());

            calendar.setTime(todoItem.getDueTo());
            updateLabel();

            editTextDescription.setText(todoItem.getDescription());
            spinnerStatus.setSelection(todoItem.getStatus());
            spinnerPriority.setSelection(priorities.indexOf(TodoItem.getPriorityText(todoItem.getPriority())));

        }
        else {
            spinnerPriority.setSelection(1);
            spinnerStatus.setSelection(0);
        }

        setTitle(todoItem == null ? "Add to-do" : "Edit to-do");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if(todoItem == null) {
            MenuItem menuItemDelete = menu.findItem(R.id.action_delete);
            if(menuItemDelete != null) {
                menuItemDelete.setVisible(false);
            }
        }
        return true;
    }

    private boolean isNullOrEmpty(Editable text) {
        return text == null || text.toString() == null || text.toString().isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if(todoItem != null) {
                    todoItem.delete();
                }
                setResult(RESULT_OK);
                finish();

                return true;
            case R.id.action_save:

                boolean isValid = true;
                if(isNullOrEmpty(editTextName.getText())) {
                    isValid = false;
                    editTextName.setError("This field is required.");
                }
                if(isNullOrEmpty(editTextDueTo.getText())) {
                    isValid = false;
                    editTextDueTo.setError("This field is required.");
                }

                if(!isValid)
                    return false;

                if(todoItem == null) {
                    todoItem = new TodoItem();
                }

                todoItem.setName(editTextName.getText().toString());
                try {
                    todoItem.setDueTo(DATE_FORMAT.parse(editTextDueTo.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                todoItem.setDescription(editTextDescription.getText().toString());
                Object selectedPriority = spinnerPriority.getSelectedItem();
                if(selectedPriority != null)
                    todoItem.setPriority(TodoItem.getPriority(selectedPriority.toString()));
                else
                    todoItem.setPriority(0);

                todoItem.setStatus(spinnerStatus.getSelectedItemPosition());

                try {

                    long countBefore = SQLite.select().from(TodoItem.class).count();
                    todoItem.save();


                } catch(Exception e) {

                }

                long countAfter = SQLite.select().from(TodoItem.class).count();
                setResult(RESULT_OK);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

    public void onDueToClick(View view) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(calendar.getTime());
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void updateLabel() {

        editTextDueTo.setText(DATE_FORMAT.format(calendar.getTime()));
    }
}
