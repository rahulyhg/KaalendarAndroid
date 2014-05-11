package com.answers.kaalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static SQLiteHelper sInstance;
	
	public static final String TABLE_KAALENDAR = "kaalendar";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEXT = "reminder_notes";
	public static final String COLUMN_ISNOTE = "is_Note";
	public static final String COLUMN_DATE = "entered_date";

	public static final String DB_NAME = "kaalendar.db";
	public static final int DB_VERSION = 1;

	// create database string
	public static final String CREATE_TABLE_HOOFIT = "create table "
			+ TABLE_KAALENDAR + "( " + COLUMN_ID
			+ " integer primary key autoincrement,  " 
			+ COLUMN_TEXT + " text, " 
			+ COLUMN_ISNOTE + " integer, "
			+ COLUMN_DATE + " datetime );";

	public static SQLiteHelper getInstance(Context context) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    if (sInstance == null) {
	      sInstance = new SQLiteHelper(context.getApplicationContext());
	    }
	    return sInstance;
	  }
	
	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("SQLite", "CREATING TABLE HOOFIT");
		db.execSQL(CREATE_TABLE_HOOFIT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// do nothing now
	}

}
