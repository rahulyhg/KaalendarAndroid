package com.answers.kaalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DaysAdapter extends BaseAdapter {
    private Context mContext;

    public DaysAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mWeekDays.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) mContext
    			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     
    		View gridView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	gridView = new View(mContext);
        	// get layout from date.xml
			gridView = inflater.inflate(R.layout.days, null);
 
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(mWeekDays[position]);
			gridView.setBackgroundColor(Color.parseColor("#0090FF"));
        } else {
        	gridView = (View) convertView;
        }

        return gridView;
    }

    // references to our dates
    private String[] mWeekDays = {
           "S","M","T","W","T","F","S"
    };
}
