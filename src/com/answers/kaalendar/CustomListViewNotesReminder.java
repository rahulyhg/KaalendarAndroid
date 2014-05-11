package com.answers.kaalendar;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomListViewNotesReminder extends
		ArrayAdapter<SQLiteNotesReminders> {
	Context context;
	List<SQLiteNotesReminders> mNotesReminders;
	private SQLiteDataSource datasource;

	private SparseBooleanArray mSelectedItemsIds;

	public CustomListViewNotesReminder(Context context, int resourceId,
			List<SQLiteNotesReminders> items) {
		super(context, resourceId, items);
		this.context = context;
		this.mNotesReminders = items;
		mSelectedItemsIds = new SparseBooleanArray();
	}

	/* private view holder class */
	private class ViewHolder {
		TextView objectId;
		TextView isNoteReminder;
		TextView textNoteReminder;
		TextView dateEntered;
		ImageView deleteButton;
		int itemPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		SQLiteNotesReminders noteReminder = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_notesreminders,
					null);
			holder = new ViewHolder();
			holder.textNoteReminder = (TextView) convertView
					.findViewById(R.id.notesRemindersText);
			holder.deleteButton = (ImageView) convertView
					.findViewById(R.id.removeNotesReminders);
			holder.isNoteReminder = (TextView) convertView
					.findViewById(R.id.textType);
			holder.dateEntered = (TextView) convertView
					.findViewById(R.id.timeNoteReminder);
			holder.objectId = (TextView) convertView
					.findViewById(R.id.objectId);
			holder.itemPosition = position;

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				RelativeLayout rowItem = (RelativeLayout) view.getParent().getParent();
				ViewHolder delHolder = (ViewHolder) ((View) view.getParent()
						.getParent()).getTag();
				Log.d("REMOVE REMINDER", "current position: "
						+ delHolder.itemPosition + "");
				TextView id = (TextView) rowItem.findViewById(R.id.objectId);
				removeNoteReminder(id.getText().toString(),
						delHolder.itemPosition);
				Log.d("REMOVE REMINDER", id.getText().toString() + " ");

			}
		});

		convertView
				.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9962B1F6
						: Color.TRANSPARENT);

		if (noteReminder.getIsNote() == 1) {
			holder.isNoteReminder.setText("  N  ");
			holder.dateEntered.setText("");
		} else {
			holder.isNoteReminder.setText("  R  ");
			holder.dateEntered
					.setText("" + noteReminder.getDateEntered().substring(11));
		}

		holder.textNoteReminder.setText(noteReminder.getNoteReminder());
		holder.objectId.setText(String.valueOf(noteReminder.getObjectId()));
		return convertView;
	}

	public void toggleSelection(int position) {
		Log.d("SREENI", "the position being passed in toggle selection: "
				+ position);
		selectView(position, !mSelectedItemsIds.get(position));
	}

	public void selectView(int position, boolean value) {
		Log.d("SREENI", "the position being passed in selectview: " + position);
		Log.d("SREENI", "the value being passed in selectview: " + value);
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);

		notifyDataSetChanged();
	}

	public int getSelectedCount() {
		Log.d("SREENI", "get selected count " + mSelectedItemsIds.size());
		return mSelectedItemsIds.size();// mSelectedCount;
	}

	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}

	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void removeNoteReminder(String objectId, int itemPosition) {
		final int removeId = Integer.valueOf(objectId);
		final int removeitemPosition = itemPosition;
		datasource = new SQLiteDataSource(context);
		new AlertDialog.Builder(context)
				.setTitle("Delete")
				.setMessage("Are you sure you want to delete?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete

								 datasource.open();
								 if (datasource.deleteNoteReminder(removeId)){
								 Log.d("REMOVE NOTES / REMINDER",
								 "successfully deleted the pollinator");
								 mNotesReminders.remove(removeitemPosition);
								 notifyDataSetChanged();
								
								 }
								 else
								 Log.d("REMOVE NOTES / REMINDER",
								 " unsucessful in removing the pollinator");
								
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).show();
	}

}
