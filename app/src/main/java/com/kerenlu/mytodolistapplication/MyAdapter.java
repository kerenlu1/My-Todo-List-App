package com.kerenlu.mytodolistapplication;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by Keren on 18/03/2017.
 */

class MyAdapter<String> extends ArrayAdapter<String> {


    public MyAdapter(MainActivity mainActivity, int simple_list_item_1, ArrayList<String> addArray) {
        super(mainActivity, simple_list_item_1, addArray);
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
                final String task = (String) MyAdapter.this.getItem(position).toString();
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle((CharSequence) task)
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyAdapter.this.remove(task);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
        });

        return view;
    }
}
