package com.kerenlu.mytodolistapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.id.list;

/**
 * Created by Keren on 18/03/2017.
 */

class MyAdapter<String> extends ArrayAdapter<String> {

    private TaskDbHelper mDbHelper;

    public MyAdapter(MainActivity mainActivity, int simple_list_item_1, ArrayList<String> addArray) {
        super(mainActivity, simple_list_item_1, addArray);
        mDbHelper = new TaskDbHelper(getContext());

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        if (position % 2 == 1) {
            view.setBackgroundColor(Color.BLUE);
        } else {
            view.setBackgroundColor(Color.RED);
        }

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final java.lang.String  task = MyAdapter.this.getItem(position).toString();
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(task)
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyAdapter.this.remove((String)task);
                                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                                // Define 'where' part of query.
                                java.lang.String selection = TaskContract.TaskEntry.COLUMN_NAME_TITLE + " LIKE ?";
                                // Specify arguments in placeholder order.
                                java.lang.String[] selectionArgs = { task };
                                // Issue SQL statement.
                                db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final java.lang.String task = MyAdapter.this.getItem(position).toString();
                Button callButton = null;
                java.lang.String phoneNum = null;
                if (task.contains("call")) {
                    Pattern pattern = Pattern.compile("(\\d.*?\\s)");
                    Matcher matcher = pattern.matcher(task);
                    if (matcher.find()) {
                        phoneNum = matcher.group(0).trim();
                    }
                    callButton = new Button(view.getContext());
                    callButton.setHint(phoneNum);
                    final java.lang.String parsePhoneNum = "tel:" + phoneNum;
                    callButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(parsePhoneNum));
                            view.getContext().startActivity(dial);
                        }
                    });
                }
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(task)
                        .setView(callButton)
                        .create();
                dialog.show();
            }
        });

        return view;
    }
}
