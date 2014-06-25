package com.cooltey.lazymaster;

import java.util.Calendar;

import com.cooltey.lazymaster.lib.ColumnData;
import com.cooltey.lazymaster.lib.DatabaseHelper;
import com.cooltey.lazymaster.lib.MainItemActions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

public class AlarmReceiver extends BroadcastReceiver {	
	
	private ColumnData setColumnData = new ColumnData();
	private DatabaseHelper db;
	private Calendar mCalendar;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		db = new DatabaseHelper(context);
		mCalendar = Calendar.getInstance();
		
		// do receiver action
		//Log.d("LazyMaster", "HELLO");
		
		setCurrentTime();
		getDatabaseData();
	}
	
	private void setCurrentTime(){

		// TODO Auto-generated method stub
		int setHour 	  = mCalendar.get(Calendar.HOUR_OF_DAY);
		int setMinute	  =	mCalendar.get(Calendar.MINUTE);
		
		String currentHour = "" + setHour;
		String currentMinute = "" + setMinute;
		if(setHour < 10){
			currentHour = "0" + setHour;
		}
		
		if(setMinute < 10){
			currentMinute = "0" + setMinute;
		}
		

		String finalCurrentTime = currentHour + ":" + currentMinute;
	}
	
	private String setDateOnWhere(){
		String returnVal = "";
		String setDate = mCalendar.get(Calendar.YEAR) + "" + mCalendar.get(Calendar.MONTH) + "" + mCalendar.get(Calendar.DATE);
		returnVal = " AND (start_date = '" + setDate + "' OR end_date = '"+ setDate +"')";
		return returnVal;
	}
	
	private int setWeekOnWhere(){
		int returnVal = 0;
		int setWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch(setWeek){
			case 0:
				returnVal = 10;
			break;
			
			case 1:
				returnVal = 11;
			break;
			
			case 2:
				returnVal = 12;
			break;
			
			case 3:
				returnVal = 13;
			break;
			
			case 4:
				returnVal = 14;
			break;
			
			case 5:
				returnVal = 15;
			break;
			
			case 6:
				returnVal = 16;
			break;
		}
		
		return returnVal;
	}
	
	private void getDatabaseData(){
		try{
			Cursor getData = db.getAll("lazy_master", " WHERE 1=1 "  + setDateOnWhere());
			if(getData != null){				
				getData.moveToFirst();
				
				for(int i = 0; i < getData.getCount(); i++){
					
					// get week check
					setColumnData.getRepeatDay = Boolean.parseBoolean(getData.getString(9));
					// need to check week
					if(setColumnData.getRepeatDay){
						if(Boolean.parseBoolean(getData.getString(setWeekOnWhere()))){
							// then going into check mode
							
						}
					}else{
						// then going into check mode
						
					}
										
					getData.moveToNext(); 
				}
			}
		}catch(Exception e){
			Log.d("setListView", e + "");
		}
	}
}