package com.vimalroxy.habittracker_roxy.data;

import android.provider.BaseColumns;

public final class HabitContract {

    private HabitContract() {
    }

    /* Inner class that defines the table contents of the location table */
    public static final class HabitEntry implements BaseColumns {

        //TABLE name
        public static final String TABLE_NAME = "habit";

        //STRING constants for each heading.
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HABIT = "habit";
        public static final String COLUMN_COUNT = "count";
        public static final String COLUMN_TIME = "time";

        //Posssible values for the time
        public final static int UNKNOWN = 0;
        public final static int TIME_MORNING = 1;
        public final static int TIME_EVENING = 2;
        public final static int TIME_NIGHT = 3;
    }
}