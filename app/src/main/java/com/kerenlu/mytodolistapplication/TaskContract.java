package com.kerenlu.mytodolistapplication;

import android.provider.BaseColumns;

/**
 * Created by Keren on 22/04/2017.
 */

public final class TaskContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TaskContract() {}

    /* Inner class that defines the table contents */
    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "todolist";
        public static final String COLUMN_NAME_TITLE = "task";
    }
}
