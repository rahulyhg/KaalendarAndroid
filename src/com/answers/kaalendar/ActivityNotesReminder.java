package com.answers.kaalendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityNotesReminder extends Activity implements InterfaceDialogFragmentCommunicator {

	SharedPreferences mPref;
	TextView mSetTime;
	ListView mListView;
	CustomListViewNotesReminder mAdapter;
	List<SQLiteNotesReminders> mRowItems;
	SQLiteDataSource datasource;
	Button mAddButton;
	EditText mEnterNotesReminders;
	CheckBox mReminderCheckBox;
	String mConstructedDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notereminder);

		SharedPreferences pref = getSharedPreferences("APP_PREFERENCES", 0);
		mConstructedDate = pref.getString("CONSTRUCTED_DATE", "");
		String actualDate = pref.getString("ACTUAL_DATE", "");

		datasource = new SQLiteDataSource(this);
		datasource.open();

		TextView selectedDate = (TextView) findViewById(R.id.selectedDate);
		selectedDate.setText(actualDate);

		TextView textEnterNotesReminders = (TextView) findViewById(R.id.enterNoteReminder);

		mListView = (ListView) findViewById(R.id.listNoteReminder);

		List<SQLiteNotesReminders> allNotesReminders = datasource
				.getAllNotesReminders(mConstructedDate);
		mRowItems = new ArrayList<SQLiteNotesReminders>();

		for (SQLiteNotesReminders temp : allNotesReminders) {
			mRowItems.add(temp);
		}

		mAdapter = new CustomListViewNotesReminder(this,
				R.layout.listview_notesreminders, mRowItems);
		mListView.setAdapter(mAdapter);

		textEnterNotesReminders.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNotesReminderDialog();

			}

		});

		Log.d("ACTIVITY NOTES", "entered here");
		datasource.closeDatabaseConnection();
	}
	

	private void showNotesReminderDialog() {
		AddNotesRemindersDialogFragment editNameDialog = new AddNotesRemindersDialogFragment();
		editNameDialog.show(getFragmentManager(), "Notes Reminders");
	}

	public void updateListView() {
		datasource.open();
		Log.d("ACTIVITY REMINDER", "updating list view");
		List<SQLiteNotesReminders> allNotesReminders = datasource
				.getAllNotesReminders(mConstructedDate);
		mRowItems.clear();
		for (SQLiteNotesReminders temp : allNotesReminders) {
			mRowItems.add(temp);
		}
		mAdapter.notifyDataSetChanged();
		datasource.closeDatabaseConnection();

	}

}
