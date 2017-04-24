package com.kerenlu.mytodolistapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ListView list;
    ArrayList<String> addArray = new ArrayList<String>();
    TaskDbHelper mDbHelper = new TaskDbHelper(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = (ListView) findViewById(R.id.taskList);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TaskContract.TaskEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()){
            addArray.add(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE)));
            while(cursor.moveToNext()){
                addArray.add(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE)));
            }
        }
        ArrayAdapter<String> adapter = new MyAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, addArray);
        list.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerFragment newFragment = new DatePickerFragment();
                LinearLayout layout = new LinearLayout(view.getContext());
                final EditText taskEditText = new EditText(view.getContext());
                taskEditText.setHint("What do you want to do next?");
                layout.addView(taskEditText);
                final ImageButton calendarButton = new ImageButton(view.getContext());
                Resources res = getResources(); // need this to fetch the drawable
                Drawable draw = res.getDrawable(android.R.drawable.ic_menu_my_calendar);
                calendarButton.setImageDrawable(draw);
                calendarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newFragment.show(getSupportFragmentManager(), "datePicker");
                    }
                });
                layout.addView(calendarButton);
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Add a new task")
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                task += " - " + newFragment.getDate();
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                // Create a new map of values, where column names are the keys
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, task);
                                // Insert the new row, returning the primary key value of the new row
                                long newRowId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
                                Log.d(TAG, "Task to add: " + task);
                                addArray.add(task);
                                ArrayAdapter<String> adapter = new MyAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, addArray);
                                list.setAdapter(adapter);
                                taskEditText.setText(" ");
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}