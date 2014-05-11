package com.answers.kaalendar;

public class SQLiteNotesReminders {

	private int mId;
	private String mNoteReminder;
	private int mIsNote;
	private String mDateEntered;
	
	public void setObjectId(int id){
		mId = id;
	}
	
	public void setNoteReminder(String str){
		mNoteReminder = str;
	}
	
	public void setIsNote(int flag){
		mIsNote = flag;
	}
	
	public void setDateEntered(String date){
		mDateEntered = date;
	}
	
	public int getObjectId(){
		return mId;
	}
	
	public String getNoteReminder(){
		return mNoteReminder;
	}
	
	public int getIsNote(){
		return mIsNote;
	}
	
	public String getDateEntered(){
		return mDateEntered;
	}
}

