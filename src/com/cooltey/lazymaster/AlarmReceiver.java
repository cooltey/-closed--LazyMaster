package com.cooltey.lazymaster;

import java.util.Calendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.cooltey.lazymaster.lib.AudioController;
import com.cooltey.lazymaster.lib.BrightnessController;
import com.cooltey.lazymaster.lib.ColumnData;
import com.cooltey.lazymaster.lib.DatabaseHelper;

public class AlarmReceiver extends BroadcastReceiver {	
	
	private ColumnData setColumnData = new ColumnData();
	private DatabaseHelper db;
	private Calendar mCalendar;
	private Context mContext;
	private NotificationManager notificationManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		mContext = context;
		db = new DatabaseHelper(context);
		mCalendar = Calendar.getInstance();
		notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// do receiver action
		//Log.d("LazyMaster", "HELLO");
		
		getCurrentTime();
		setCursorData();
		
	}
	
	private int getCurrentTime(){

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
		

		String finalCurrentTime = currentHour + "" + currentMinute;
		
		return Integer.parseInt(finalCurrentTime);
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
	
	private void setCursorData(){
		try{
			Cursor getData = db.getAll("lazy_master", " WHERE 1=1 AND swticher = 'true' "  + setDateOnWhere());
			boolean isActivated = false;
			if(getData != null){				
				getData.moveToFirst();
				
				for(int i = 0; i < getData.getCount(); i++){
					
					// get week check
					setColumnData.getRepeatDay = Boolean.parseBoolean(getData.getString(9));
					// need to check week
					if(setColumnData.getRepeatDay){
						if(Boolean.parseBoolean(getData.getString(setWeekOnWhere()))){
							// then going into check mode
							isActivated = checkMode(isActivated, getData);
						}
					}else{
						isActivated = checkMode(isActivated, getData);
						
					}
										
					getData.moveToNext(); 
				}
			}else{
				Log.d("backToDefault", "true");
				backToDefault();
			}
			
			if(!isActivated){
				Log.d("backToDefault", "true");
				backToDefault();				
			}
		}catch(Exception e){
			Log.d("getDatabaseData", e + "");
		}
	}
	
	private boolean checkMode(boolean isActivated, Cursor getData){
		// then going into check mode
		// check time range
		setColumnData.getStartTime 		= getData.getString(3);
		setColumnData.getEndTime 		= getData.getString(4);
		String[] splitStartTime = setColumnData.getStartTime.split(":");
		String[] splitEndTime = setColumnData.getEndTime.split(":");
		
		int startTime = Integer.parseInt(splitStartTime[0] + "" + splitStartTime[1]);
		int endTime = Integer.parseInt(splitEndTime[0] + "" + splitEndTime[1]);
		if(startTime > endTime){
			// cross the day
			//Log.d("Cross day", "yap");
			// start check range
			// e.g. 2200 ~ 0600
			// current  2300
			// 2300 >= 2200 && 2300 >= 0600
			// current 0100
			// 0100 <= 2200 && 0100 <= 0600
			if((getCurrentTime() >= startTime && getCurrentTime() >= endTime)
				|| (getCurrentTime() <= startTime && getCurrentTime() <= endTime)){

				Log.d("On the range of time", "yap");
				startSetUp(getData);
				isActivated = true;
			}else{
				//backToDefault();
				Log.d("On the range of time", "nope");
			}
		}else{
			// normal
			// e.g. 0600 ~ 2200
			// current 1000
			// 0600 <= 1000 && 2200 >= 1000
			if(startTime <= getCurrentTime() && endTime >= getCurrentTime()){

				Log.d("On the range of time", "yap");
				startSetUp(getData);
				isActivated = true;
			}else{
				//backToDefault();
				Log.d("On the range of time", "nope");
			}
		}
		
		return isActivated;
	}
	
	private void startSetUp(Cursor cursor){

		SharedPreferences settings = mContext.getSharedPreferences("system_info", 0);	
		boolean isActivated = settings.getBoolean("activated", false);
		int activateId = settings.getInt("task_id", 0);
		if(!isActivated || activateId !=  cursor.getInt(0)){
			setColumnData.getTitle 			= cursor.getString(1);
			setColumnData.getBrightness 	= cursor.getInt(7);
			setColumnData.getSounds 		= cursor.getInt(8);
			
			// use intent to activate brightness change
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("sounds", setColumnData.getSounds);
			bundle.putInt("brightness", setColumnData.getBrightness);
			intent.putExtras(bundle);
			intent.setClassName("com.cooltey.lazymaster", "com.cooltey.lazymaster.ModeUpdateActivity");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    mContext.startActivity(intent);
		    	
			
		    SharedPreferences.Editor getData = settings.edit();
		    getData.putBoolean("activated", true);
		    getData.commit();
		    
		    generateNotification(mContext, 
		    		setColumnData.getTitle, 
		    		mContext.getString(R.string.notification_msg) + 
		    		setColumnData.getStartTime +
		    		mContext.getString(R.string.notification_msg_2) + 
		    		setColumnData.getEndTime);
		}
	}
	
	private void backToDefault(){
		SharedPreferences settings = mContext.getSharedPreferences("system_info", 0);				
		if(settings.getBoolean("activated", false)){
			setColumnData.getBrightness 	= Integer.parseInt(settings.getString("brightness", "0"));
			setColumnData.getSounds 		= Integer.parseInt(settings.getString("sounds", "0"));

			Log.d("setColumnData.getBrightness - backToDefault ", setColumnData.getBrightness  + "");
			// use intent to activate brightness change
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("sounds", setColumnData.getSounds);
			bundle.putInt("brightness", setColumnData.getBrightness);
			intent.putExtras(bundle);
			intent.setClassName("com.cooltey.lazymaster", "com.cooltey.lazymaster.ModeUpdateActivity");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    mContext.startActivity(intent);
		    
		    SharedPreferences.Editor getData = settings.edit();
		    getData.putBoolean("activated", false);
		    getData.commit();
		    
		    // cancel notification
		    notificationManager.cancel(0);
		    
		    Log.d("Backtodefault", "true");
		}
	}
	
	private void generateNotification(Context context, String title, String message) {
		
	   //Creating Notification Builder
	   NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
       //Title for Notification
       notification.setContentTitle(title);
       //Message in the Notification
       notification.setContentText(message);
       //Alert shown when Notification is received
       notification.setTicker(title);
       //Icon to be set on Notification
       notification.setSmallIcon(R.drawable.ic_launcher);
       //Creating new Stack Builder
       TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
       stackBuilder.addParentStack(MainActivity.class);
       //Intent which is opened when notification is clicked
       Intent resultIntent = new Intent(context, MainActivity.class);
       stackBuilder.addNextIntent(resultIntent);
       PendingIntent pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
       notification.setContentIntent(pIntent);
       notificationManager.notify(0, notification.build());
	}
}