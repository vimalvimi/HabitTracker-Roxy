package com.vimalroxy.habittracker_roxy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vimalroxy.habittracker_roxy.data.HabitContract.HabitEntry;
import com.vimalroxy.habittracker_roxy.data.HabitDbHelper;

import static com.vimalroxy.habittracker_roxy.data.HabitContract.HabitEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private HabitDbHelper mHabitDbHelper;
    private EditText mSearchField;
    private Button mEnterButton;
    private Button mDeleteAllButton;
    private Spinner mTimeOfTheDay;
    private int mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Search Field and Buttons
        mSearchField = (EditText) findViewById(R.id.enterHabitTextField);
        mEnterButton = (Button) findViewById(R.id.enterButton);
        mDeleteAllButton = (Button) findViewById(R.id.deleteAllButton);
        mTimeOfTheDay = (Spinner) findViewById(R.id.spinnerTimeOfTheDay);

        mHabitDbHelper = new HabitDbHelper(this);

        SetupSpinner();

        displayDatabaseInfo();

        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterNewHabit();
                displayDatabaseInfo();
            }
        });

        mDeleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRows();
                displayDatabaseInfo();
            }
        });
    }

    private void SetupSpinner() {

        ArrayAdapter timeOfTheDaySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_time_options, android.R.layout.simple_spinner_item);
        timeOfTheDaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mTimeOfTheDay.setAdapter(timeOfTheDaySpinnerAdapter);

        mTimeOfTheDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.time_morning))) {
                        mTime = HabitEntry.TIME_MORNING;
                    } else if (selection.equals(getString(R.string.time_evening))) {
                        mTime = HabitEntry.TIME_EVENING;
                    } else if (selection.equals(getString(R.string.time_night))) {
                        mTime = HabitEntry.TIME_NIGHT;
                    } else {
                        mTime = HabitEntry.UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTime = 0; // Unknown
            }
        });
    }

    private void EnterNewHabit() {

        String newHabit = mSearchField.getText().toString().trim();

        int timeInt = mTimeOfTheDay.getSelectedItemPosition();

        //Gets the data repository in write mode
        SQLiteDatabase database = mHabitDbHelper.getWritableDatabase();

        //Create New Map of Values, KEYS
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT, newHabit);
        values.put(HabitEntry.COLUMN_TIME, timeInt);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = database.insert(TABLE_NAME, null, values);
        Log.v("MAIN", "NEW ROW ID = " + newRowId);
    }

    private void displayDatabaseInfo() {

        SQLiteDatabase database = mHabitDbHelper.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT,
                HabitEntry.COLUMN_TIME,
                HabitEntry.COLUMN_COUNT
        };

        Cursor cursor = database.query(
                TABLE_NAME,                // The table to query
                projection,                          // The columns to return
                null,                               // The columns for the WHERE clause
                null,                              // The values for the WHERE clause
                null,                             // don't group the rows
                null,                            // don't filter by row groups
                null                            // The sort order
        );

        TextView displayView = (TextView) findViewById(R.id.textView);

        try {
            displayView.setText("Numbers of Habits : " + cursor.getCount());

            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int idCount = cursor.getColumnIndex(HabitEntry.COLUMN_COUNT);
            int habitColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT);
            int timeColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_TIME);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                int currentTime = cursor.getInt(timeColumnIndex);
                String currentHabit = cursor.getString(habitColumnIndex);
                int currentCount = cursor.getInt(idCount);

                displayView.append("\n" + currentHabit + " --- " + currentTime + " --- " + currentCount);
            }
        } catch (Exception c) {
            Log.e(TAG, "displayDatabaseInfo: ", c);
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    public void deleteRows() {
        mHabitDbHelper = new HabitDbHelper(this);
        SQLiteDatabase database = mHabitDbHelper.getWritableDatabase();
        database.execSQL("delete from " + TABLE_NAME);
    }
}
