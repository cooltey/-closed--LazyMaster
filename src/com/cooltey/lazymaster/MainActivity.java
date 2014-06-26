package com.cooltey.lazymaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cooltey.lazymaster.lib.AudioController;
import com.cooltey.lazymaster.lib.BrightnessController;
import com.cooltey.lazymaster.lib.DatabaseHelper;
import com.cooltey.lazymaster.lib.MainItemActions;

public class MainActivity extends SherlockFragmentActivity {

	private DatabaseHelper db;
	private LinearLayout mainContent;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		db = new DatabaseHelper(this);
		mainContent = (LinearLayout) findViewById(R.id.scrollContent); 
		
		setInitialSetting();
		setListView();
		
		setReceiver();
		getSupportActionBar();
		
	}
	
	private void setReceiver(){
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        // set receiver action
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        // set timer
        alarmManager.setRepeating(AlarmManager.RTC, 0, 10 * 1000, pendingIntent);
	}
	
	private void setListView(){
		try{
			mainContent.removeAllViewsInLayout();
			Cursor getData = db.getAll("lazy_master", "");
			if(getData != null){
				getData.moveToFirst();
				
				for(int i = 0; i < getData.getCount(); i++){
					MainItemActions ci = new MainItemActions(this, getData);
					View getView = ci.getItemView();
					
					mainContent.addView(getView);
					
					getData.moveToNext(); 
				}
			}
		}catch(Exception e){
			Log.d("setListView", e + "");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setInitialSetting(){
	
		AudioController ac = new AudioController(mContext);
		String soundMode = ac.getOriginalSoundMode();
		
		BrightnessController bc = new BrightnessController(mContext);
		String brightness = bc.getOriginalBrightness() + "";
		
		// check system has activate the lazy mode
		SharedPreferences settings = mContext.getSharedPreferences("system_info", 0);
		
		boolean checkActivated = settings.getBoolean("activated", false);
		if(!checkActivated){
		    SharedPreferences.Editor getData = settings.edit();
		    getData.putString("sounds", soundMode);
		    getData.putString("brightness", brightness);
		    getData.commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add:	

	        	String[] columns = {"title", 
	        						"swticher", 
	        						"start_time", 
	        						"end_time", 
	        						"start_date", 
	        						"end_date", 
	        						"brightness", 
	        						"sounds", 
	        						"repeat_day",
	        						"week_sun", 
	        						"week_mon", 
	        						"week_tue", 
	        						"week_wed", 
	        						"week_thu", 
	        						"week_fri", 
	        						"week_sat"};
	        	
	        	String[] values = {"", 
								   "false", 
								   "00:00", 
								   "00:00",
								   "", 
								   "", 
								   "", 
								   "", 
								   "false",
								   "false", 
								   "false", 
								   "false", 
								   "false", 
								   "false", 
								   "false", 
								   "false"};
	        	
	        	db.insert("lazy_master", columns, values);     
	        	
	        	// reset view
	        	setListView();
	            return true;
	            
	        case R.id.action_about:
	        	
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
