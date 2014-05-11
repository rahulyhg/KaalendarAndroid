package com.answers.kaalendar;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	private Context mContext;
	private int[] mArrayOfDatesToBePrinted = new int[42];
	private boolean[] mArrayOfTrailingDates = new boolean[42];
	private int mNumberOfWeeks, mNumberOfCells;
	private int mCurrentMonth, mCurrentYear;
	private int mPrevMonth, mPrevYear;
	private int mNextMonth, mNextYear;
	private SQLiteDataSource mDataSource;
	boolean doWeHaveSpecialDays;
	ArrayList<Integer> specialDays;
	private int mToday;

	public CalendarAdapter(Context c, int month, int year, int numberOfWeeks) {
		mContext = c;
		mNumberOfWeeks = numberOfWeeks;
		mNumberOfCells = numberOfWeeks * 7;
		updatePrintingList(month - 1, year);
	}

	private void updatePrintingList(int month, int year) {
		int currentMonthMaxDays, prevMonthMaxDays, firstDayOfCurrentMonth, offset;
		Calendar mCal = Calendar.getInstance();
		
		mToday = -1;
		if (mCal.get(Calendar.YEAR) == year && mCal.get(Calendar.MONTH) == month)
			mToday = mCal.get(Calendar.DATE);

		Log.d("CALENDAR ADAPTER", "today: " + mToday);
		
		specialDays = new ArrayList<Integer>();
		doWeHaveSpecialDays = true;
		DecimalFormat formatter = new DecimalFormat("00");

		mCal.set(year, month, 1);
		firstDayOfCurrentMonth = mCal.get(Calendar.DAY_OF_WEEK);
		currentMonthMaxDays = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		mCurrentMonth = month;
		mCurrentYear = year;

		String startDate = mCurrentYear + "-"
				+ formatter.format(mCurrentMonth + 1) + "-" + "01";
		String endDate = mCurrentYear + "-"
				+ formatter.format(mCurrentMonth + 1) + "-"
				+ currentMonthMaxDays;
		mDataSource = new SQLiteDataSource(mContext);
		mDataSource.open();
		specialDays = mDataSource.getAllDistinctSpecialDays(startDate, endDate);
		mDataSource.closeDatabaseConnection();

		if (specialDays.isEmpty())
			doWeHaveSpecialDays = false;

		Log.d("CALENDAR ADAPTER", "current month: " + month);
		Log.d("CALENDAR ADAPTER", "current year: " + year);
		Log.d("CALENDAR ADAPTER", "maximum days: " + currentMonthMaxDays);
		Log.d("CALENDAR ADAPTER", "first day of month: "
				+ firstDayOfCurrentMonth);
		Log.d("CALENDAR ADAPTER",
				"number of weeks: "
						+ mCal.getActualMaximum(Calendar.WEEK_OF_MONTH));
		offset = firstDayOfCurrentMonth - 1;

		Log.d("CALENDAR ADAPTER", "offset: " + offset);

		int i, k;
		StringBuilder output1 = new StringBuilder();
		for (i = 1; i <= currentMonthMaxDays; i++) {
			mArrayOfDatesToBePrinted[offset] = i;
			mArrayOfTrailingDates[offset++] = true;
			output1.append(i + " , ");
		}


		k = 1;
		while (offset <= mNumberOfCells - 1) {
			mArrayOfDatesToBePrinted[offset++] = k++;
		}

		if (month >= 12) {
			mNextMonth = 1;
			mNextYear = mCurrentYear + 1;
		} else {
			mNextMonth = month + 2;
			mNextYear = mCurrentYear;
		}

		if (month <= 1) {
			mPrevMonth = month = 12;
			mPrevYear = year--;
		} else {
			mPrevMonth = month--;
			mNextYear = year;
		}

		mCal.set(year, month, 1);
		prevMonthMaxDays = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Log.d("CALENDAR ADAPTER", "previous month: " + month);
		Log.d("CALENDAR ADAPTER", "previous year: " + year);
		Log.d("CALENDAR ADAPTER", "maximum days: " + prevMonthMaxDays);

		offset = firstDayOfCurrentMonth - 2;
		while (offset >= 0) {
			mArrayOfDatesToBePrinted[offset--] = prevMonthMaxDays--;
		}

		StringBuilder output2 = new StringBuilder();
		for (i = 0; i < mNumberOfCells; i++) {
			output2.append(mArrayOfDatesToBePrinted[i] + " , ");
		}


	}

	public int getCount() {
		return mNumberOfWeeks * 7;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public int getDateInCurrentCell(int position) {
		return mArrayOfDatesToBePrinted[position];
	}

	public int getCurrentYearInTheGrid() {
		return mCurrentYear;
	}

	public int getCurrentMonthInTheGrid() {
		return mCurrentMonth;
	}

	public int getPrevMonth() {
		return mPrevMonth;
	}

	public int getPrevYear() {
		return mPrevYear;
	}

	public int getNextMonth() {
		return mNextMonth;
	}

	public int getNextYear() {
		return mNextYear;
	}

	public boolean getBooleanValueOfTheCell(int position) {
		return mArrayOfTrailingDates[position];
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			gridView = new View(mContext);
			// get layout from date.xml
			gridView = inflater.inflate(R.layout.dates, null);
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			if (mArrayOfTrailingDates[position]) {
				textView.setText(String
						.valueOf(mArrayOfDatesToBePrinted[position]));

				if (doWeHaveSpecialDays
						&& specialDays
								.contains(mArrayOfDatesToBePrinted[position])) {
					gridView.setBackgroundColor(Color.parseColor("#0090FF"));
				} else {
					gridView.setBackgroundColor(Color.parseColor("#434242"));
				}
				
				if (mToday != -1 && mArrayOfDatesToBePrinted[position] == mToday){
					gridView.setBackgroundColor(Color.WHITE);
					textView.setTextColor(Color.BLACK);
					
				}

			} else {
				textView.setText(String
						.valueOf(mArrayOfDatesToBePrinted[position]));
				gridView.setBackgroundColor(Color.parseColor("#b2b2b0"));
			}

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

}
