package com.answers.kaalendar;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteDataSource {

	private SQLiteDatabase database;
	private SQLiteHelper dbhelper;

	public SQLiteDataSource(Context context) {
		dbhelper = SQLiteHelper.getInstance(context);
	}
	
	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public void openDatabaseToWrite() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void closeDatabaseConnection() {
		dbhelper.close();
	}

	public boolean insertIntoKaalendar(String textNoteReminder, int isNote,
			String date) {
		Log.d("SQLite INSERT", "Reminder: " + textNoteReminder);
		Log.d("SQLite INSERT", "Is Note: " + isNote);
		Log.d("SQLite INSERT", "Date: " + date);

		ContentValues values = new ContentValues();
		
		openDatabaseToWrite();

		values.put(SQLiteHelper.COLUMN_TEXT, textNoteReminder);
		values.put(SQLiteHelper.COLUMN_ISNOTE, isNote);
		values.put(SQLiteHelper.COLUMN_DATE, date);

		long objectId = database.insert(SQLiteHelper.TABLE_KAALENDAR, null, values);
		Log.d("SQLite DATASOURCE", "Object Id: " + objectId);

		closeDatabaseConnection();

		return true;
	}
	
	public ArrayList<Integer> getAllDistinctSpecialDays(String start, String end){
		

		String selectQuery = "SELECT DISTINCT date(entered_date) FROM " + SQLiteHelper.TABLE_KAALENDAR +
				" where date(entered_date) BETWEEN\"" + start + "\" AND  \"" + end + "\"";
		ArrayList<Integer> uniqueDates = new ArrayList<Integer>();
		Cursor cursor = database.rawQuery(selectQuery, null);
		Log.d("SQLite FETCH", selectQuery);
		Log.d("SQLite FETCH", "rows returned: " + cursor.getCount());
		

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Log.d("SQLite FETCH", "entered first row");
			Log.d("SQLite FETCH", cursor.getString(0).substring(8));
			uniqueDates.add(Integer.valueOf(cursor.getString(0).substring(8)));
			cursor.moveToNext();
		}
		cursor.close();
		
		return uniqueDates;
	}
	
	public boolean deleteNoteReminder(int objectId){
		if ((database.delete(SQLiteHelper.TABLE_KAALENDAR,SQLiteHelper.
				COLUMN_ID + " = " + objectId, null))> 0){
			Log.d("SREENI", "successfully deleted: " + objectId );
			return true;
		}
		else {
			Log.d("SREENI", "could not delete the id " + objectId );
			return false;
		}
	}
	
	public List<SQLiteNotesReminders> getAllNotesReminders(String strDate){
		List<SQLiteNotesReminders> notesAndReminders = new ArrayList<SQLiteNotesReminders>();
		String selectQuery = "SELECT * FROM "  + SQLiteHelper.TABLE_KAALENDAR + 
				" where date(entered_date) = date(\""  + strDate + "\")" + " order by entered_date";
		Cursor cursor = database.rawQuery(selectQuery, null);
		Log.d("SQLite FETCH", selectQuery);
		Log.d("SQLite FETCH", "rows returned: " + cursor.getCount());
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SQLiteNotesReminders noteReminder = cursorToNotesAndReminders(cursor);
			notesAndReminders.add(noteReminder);
			cursor.moveToNext();
		}
		cursor.close();
		
		return notesAndReminders;
	}
	
	private SQLiteNotesReminders cursorToNotesAndReminders(Cursor cursor) {
		
		SQLiteNotesReminders notesReminders = new SQLiteNotesReminders();
		notesReminders.setObjectId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
		notesReminders.setNoteReminder(cursor.getString(cursor.getColumnIndex("reminder_notes")));
		notesReminders.setIsNote(Integer.parseInt(cursor.getString(cursor.getColumnIndex("is_Note"))));
		notesReminders.setDateEntered(cursor.getString(cursor.getColumnIndex("entered_date")));
		
		return notesReminders;
	}

}
