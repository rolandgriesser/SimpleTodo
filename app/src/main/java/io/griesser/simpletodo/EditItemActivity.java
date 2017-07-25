package io.griesser.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static io.griesser.simpletodo.R.id.etNewItem;

public class EditItemActivity extends AppCompatActivity {

    private EditText etNewItem;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etNewItem = (EditText)findViewById(R.id.etNewItem);

        itemPosition = getIntent().getExtras().getInt(MainActivity.EXTRA_POSITION);
        etNewItem.setText(getIntent().getExtras().getString(MainActivity.EXTRA_TEXT));
    }

    public void onSave(View v) {
        String itemText = etNewItem.getText().toString();

        Intent data = new Intent();

        data.putExtra(MainActivity.EXTRA_POSITION, itemPosition);
        data.putExtra(MainActivity.EXTRA_TEXT, itemText);

        setResult(RESULT_OK, data);
        finish();
    }
}
