package io.griesser.simpletodo.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.BaseModelQueriable;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Transformable;

import io.griesser.simpletodo.R;
import io.griesser.simpletodo.adapters.TodoItemCursorAdapter;
import io.griesser.simpletodo.fragments.SortByDialogFragment;
import io.griesser.simpletodo.models.TodoItem;
import io.griesser.simpletodo.models.TodoItem_Table;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity implements SortByDialogFragment.SortByDialogListener {

    public static final String EXTRA_TODOITEM = "todoitem";

    public static final String SORT_BY_NAME_ASC = "Name asc.";
    public static final String SORT_BY_NAME_DESC = "Name desc.";
    public static final String SORT_BY_DATE_ASC = "Date asc.";
    public static final String SORT_BY_DATE_DESC = "Date desc.";
    public static final String SORT_BY_PRIORITY_ASC = "Priority asc.";
    public static final String SORT_BY_PRIORITY_DESC = "Priority desc.";

    private final int REQUEST_CODE_EDIT_ITEM = 20;

    private FlowQueryList<TodoItem> items;
    private TodoItemCursorAdapter itemsAdapter;
    private ListView lvItems;
    private boolean showDone = true;
    private String sortBy = SORT_BY_NAME_ASC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        lvItems = (ListView)findViewById(R.id.lvItems);

        updateTodoItemsFromDatabase();
        itemsAdapter = new TodoItemCursorAdapter(this, items.cursor(), 0, EditItemActivity.DATE_PATTERN);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void updateTodoItemsFromDatabase() {

        Transformable<TodoItem> from = SQLite.select().from(TodoItem.class);
        if(!showDone)
            from = ((From<TodoItem>)from).where(TodoItem_Table.status.eq(0));

        switch (sortBy) {
            case SORT_BY_NAME_DESC:
                from = from.orderBy(TodoItem_Table.name, false);
                break;
            case SORT_BY_DATE_ASC:
                from = from.orderBy(TodoItem_Table.dueTo, true);
                break;
            case SORT_BY_DATE_DESC:
                from = from.orderBy(TodoItem_Table.dueTo, false);
                break;
            case SORT_BY_PRIORITY_ASC:
                from = from.orderBy(TodoItem_Table.priority, true);
                break;
            case SORT_BY_PRIORITY_DESC:
                from = from.orderBy(TodoItem_Table.priority, false);
                break;
            default:
            case SORT_BY_NAME_ASC:
                from = from.orderBy(TodoItem_Table.name, true);
                break;
        }

        items = ((BaseModelQueriable<TodoItem>)from).flowQueryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_show_completed).setChecked(showDone);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_completed:
                showDone = !item.isChecked();
                item.setChecked(showDone);
                updateTodoItemsFromDatabase();
                itemsAdapter.changeCursor(items.cursor());

                return true;
            case R.id.action_sort_by:

                FragmentManager fm = getFragmentManager();
                SortByDialogFragment sortByFragment = SortByDialogFragment.newInstance(sortBy);
                sortByFragment.show(fm, "fragment_sortby");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                items.get(i).delete();
                items.refresh();
                itemsAdapter.changeCursor(items.cursor());

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);

                TodoItem todoItem = items.get(i);
                intent.putExtra(EXTRA_TODOITEM, todoItem);

                startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
            }
        });
    }

    public void onAddItem(View v) {

        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_ITEM) {
            items.refresh();
            itemsAdapter.changeCursor(items.cursor());
        }
    }

    @Override
    public void onSortBySelected(String sortBy) {

        this.sortBy = sortBy;
        updateTodoItemsFromDatabase();
        itemsAdapter.changeCursor(items.cursor());
    }
}
