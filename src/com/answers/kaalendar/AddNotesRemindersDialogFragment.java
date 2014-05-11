package com.answers.kaalendar;

import java.text.DecimalFormat;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AddNotesRemindersDialogFragment extends DialogFragment {

	SharedPreferences mPref;
	TextView mHoursValue,mMinutesValue;
	int mHourSet, mMinuteSet;
	EditText mEnterNotesReminders;
	CheckBox mReminderCheckBox;
	TextView mTextViewDone;
	SQLiteDataSource datasource;
	String mConstructedDate;
	InterfaceDialogFragmentCommunicator comm;

	public AddNotesRemindersDialogFragment() {
		// Empty constructor required dialog fragment
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notesreminders,
				container);
		mPref = getActivity().getSharedPreferences("APP_PREFERENCES", 0);
		String actualDate = mPref.getString("ACTUAL_DATE", "");
		mConstructedDate = mPref.getString("CONSTRUCTED_DATE", "");
		Log.d("DIALOG FRAGMENT", "date: " + actualDate);
		getDialog().setTitle(actualDate);
		
		datasource = new SQLiteDataSource(getActivity());
		datasource.open();

		mReminderCheckBox = (CheckBox) view.findViewById(R.id.reminderCheck);
		mTextViewDone = (TextView) view.findViewById(R.id.doneText);
		mEnterNotesReminders = (EditText) view.findViewById(R.id.enterNotesReminders);
		
		SeekBar mHoursSeekBar = (SeekBar) view.findViewById(R.id.hourSeekBar);
		mHoursValue = (TextView) view.findViewById(R.id.hoursValue);
		
		SeekBar mMinutesSeekBar = (SeekBar) view.findViewById(R.id.minuteSeekBar);
		mMinutesValue = (TextView) view.findViewById(R.id.minutesValue);

		mHoursSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mHoursValue.setText("HH : " + progress);
				mHourSet = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// do nothing
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// do nothing
			}

		});
		
		mMinutesSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mMinutesValue.setText("MM : " + progress);
				mMinuteSet = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// do nothing
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// do nothing
			}

		});
		
		mTextViewDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int isNote;
				String enteredTime;
				String enteredText = mEnterNotesReminders.getText().toString();
				DecimalFormat decimalFormatter = new DecimalFormat("00");
				Log.d("ACTIVITY NOTES", "acknowledging click");
				Log.d("ACTIVITY NOTES", "entered text: " + enteredText);
				if (enteredText.equals("")) {
					// do nothing
				} else {
					if (mReminderCheckBox.isChecked())
						isNote = 0;
					else
						isNote = 1;
					if (isNote == 1)
						enteredTime = "00" + ":" + "00";
					else
						enteredTime = decimalFormatter.format(mHourSet) + ":" + decimalFormatter.format(mMinuteSet);
					datasource.insertIntoKaalendar(enteredText, isNote,
							(mConstructedDate + " " + enteredTime));
					mEnterNotesReminders.setText("");
				}
				mReminderCheckBox.setChecked(false);
				getDialog().dismiss();
				comm.updateListView();

			}
		});


		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		comm = (InterfaceDialogFragmentCommunicator) getActivity();
	}

}
