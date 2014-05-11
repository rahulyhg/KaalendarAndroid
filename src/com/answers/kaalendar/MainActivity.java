package com.answers.kaalendar;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Calendar mCalendar;
	private int mMonth, mYear, mNumberOfWeeks;
	private final String[] mArrayMonths = { "JAN", "FEB", "MAR", "APR", "MAY",
			"JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	private TextView mTextViewMonthYear;
	private CalendarAdapter mCalAdapter;
	private GridView mDatesGridview;
	private String mActualDateSelected;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.calendar_view);
		
		mPref = getApplicationContext().getSharedPreferences(
				"APP_PREFERENCES", MODE_PRIVATE);
		
		mCalendar = new GregorianCalendar();
		mMonth = mCalendar.get(Calendar.MONTH) + 1;
		mYear = mCalendar.get(Calendar.YEAR);
		mNumberOfWeeks = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
		Log.d("MAIN ACTIVITY", "Month: " + mMonth + " " + " Year: " + mYear);

		mTextViewMonthYear = (TextView) findViewById(R.id.tMonthYear);
		String monthAndYear = mArrayMonths[mMonth-1] + " " + mYear;
		mTextViewMonthYear.setText(monthAndYear);
		Log.d("MAIN ACTIVITY", "TextView Month: " + monthAndYear);

		ImageView nextMonth = (ImageView) findViewById(R.id.nextImage);
		ImageView prevMonth = (ImageView) findViewById(R.id.prevImage);

		prevMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMonth <= 1) {
					mMonth = 12;
					mYear--;
				} else {
					mMonth--;
				}
				Log.d("MAIN ACTIVITY", "Month: " + mMonth + " " + " Year: " + mYear);
				String monthAndYear = mArrayMonths[mMonth - 1] + " " + mYear;
				mCalendar.set(mYear, mMonth-1, 1);
				mTextViewMonthYear.setText(monthAndYear);
				Log.d("MAIN ACTIVITY", "TextView Month: " + monthAndYear);
				updateGridView(mMonth, mYear,mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH));
			}

		});
		
		nextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMonth >= 12) {
					mMonth = 1;
					mYear++;
				} else {
					mMonth++;
				}
				Log.d("MAIN ACTIVITY", "Month: " + mMonth + " " + " Year: " + mYear);
				String monthAndYear = mArrayMonths[mMonth - 1] + " " + mYear;
				mCalendar.set(mYear, mMonth-1, 1);
				mTextViewMonthYear.setText(monthAndYear);
				Log.d("MAIN ACTIVITY", "TextView Month: " + monthAndYear);
				updateGridView(mMonth, mYear,mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH));
			}

		});

		GridView daysGridView = (GridView) findViewById(R.id.daysGridview);
		daysGridView.setAdapter(new DaysAdapter(this));

		mDatesGridview = (GridView) findViewById(R.id.datesGridview);

		mDatesGridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Log.d("MAIN ACTIVITY", "Position: " + position + " ID: " + id);
				getDateSelected(position);

			}
		});
		
		updateGridView(mMonth, mYear,mNumberOfWeeks);
		
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		updateGridView(mMonth, mYear, mNumberOfWeeks);
	}
	
	private void updateGridView(int month, int year, int numberOfWeeks){
		mCalAdapter = new CalendarAdapter(this, month, year, numberOfWeeks);
		mCalAdapter.notifyDataSetChanged();
		mDatesGridview.setAdapter(mCalAdapter);
	}
	
	private void getDateSelected(int position){
		int dateSelected, currentMonth, currentYear;
		boolean booleanValueOfTheCell;
		String constructedDate;
		DecimalFormat formatter = new DecimalFormat("00");
		
		dateSelected = mCalAdapter.getDateInCurrentCell(position);
		currentMonth = mCalAdapter.getCurrentMonthInTheGrid();
		currentYear = mCalAdapter.getCurrentYearInTheGrid();
		booleanValueOfTheCell = mCalAdapter.getBooleanValueOfTheCell(position);
		
		
		if(booleanValueOfTheCell){
			constructedDate = currentYear + "-" + formatter.format(currentMonth+1) + "-" + formatter.format(dateSelected);
			mActualDateSelected = formatter.format(dateSelected) + "-" + mArrayMonths[currentMonth] + "-" +
					currentYear;
		}
		else{
			if (position < 7){
				constructedDate = getPrevMonthDate(currentMonth+1, currentYear, dateSelected);
				Log.d("MAIN ACTIVITY", "previous month date selected");
			}
			else{
				Log.d("MAIN ACTIVITY", "next month date selected");
				constructedDate = getNextMonthDate(currentMonth+1, currentYear, dateSelected);
			}
		}
		
		Log.d("MAIN ACTIVITY", "Current System Date: " + mCalendar.getTime());
		Log.d("MAIN ACTIVITY", "Date constructed: " + constructedDate);
		Log.d("MAIN ACTIVITY", "Date selected: " + dateSelected);
		Log.d("MAIN ACTIVITY", "Month selected: " + currentMonth);
		Log.d("MAIN ACTIVITY", "Year selected: " + currentYear);
		Log.d("MAIN ACTIVITY", "Boolean Value of the cell: " + booleanValueOfTheCell);
		
		Intent notesRemindersIntent = new Intent(this, ActivityNotesReminder.class);
		
		Editor editor = mPref.edit();
		editor.putString("CONSTRUCTED_DATE" ,constructedDate );
		editor.putString("ACTUAL_DATE", mActualDateSelected);
		editor.commit();
		
		startActivity(notesRemindersIntent);
	}
	
	private String getPrevMonthDate(int month, int year, int date){
		DecimalFormat formatter = new DecimalFormat("00");
		if (month == 1){
			mActualDateSelected = formatter.format(date) + "-" + mArrayMonths[11] + "-" +
					(year-1);
			return (--year) + "-" + formatter.format(12) + "-" + formatter.format(date);
		}
		else{
			mActualDateSelected = formatter.format(date) + "-" + mArrayMonths[month-2] + "-" +
					year;
			return year + "-" + formatter.format(--month) + "-" + formatter.format(date);
		}
	}
	
	private String getNextMonthDate(int month, int year, int date){
		DecimalFormat formatter = new DecimalFormat("00");
		if (month == 12){
			mActualDateSelected = formatter.format(date) + "-" + mArrayMonths[0] + "-" +
					(year+1);
			return (++year) + "-" + formatter.format(1) + "-" + formatter.format(date);
		}
		else{
			mActualDateSelected = formatter.format(date) + "-" + mArrayMonths[month] + "-" +
					year;
			return year + "-" + formatter.format(++month) + "/" + formatter.format(date);
		}
	}


}
