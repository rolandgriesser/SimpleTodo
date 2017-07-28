package io.griesser.simpletodo.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by rolan on 26/07/2017.
 */
@Database(name = TodoItemDatabase.NAME, version = TodoItemDatabase.VERSION)
public class TodoItemDatabase {

    public static final String NAME = "TodoItemDatabase";
    public static final int VERSION = 1;
}