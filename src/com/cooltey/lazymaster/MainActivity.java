package com.cooltey.lazymaster;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cooltey.lazymaster.lib.columnItem;
import com.cooltey.lazymaster.lib.databaseHelper;

public class MainActivity extends SherlockFragmentActivity {

	private databaseHelper db;
	private LinearLayout mainContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new databaseHelper(this);
		mainContent = (LinearLayout) findViewById(R.id.scrollContent); 
		setListView();
		
		getSupportActionBar();
	}
	
	private void setListView(){
		try{
			mainContent.removeAllViewsInLayout();
			Cursor getData = db.getAll("lazy_master", "");
			if(getData != null){
				getData.moveToFirst();
				
				for(int i = 0; i < getData.getCount(); i++){
					columnItem ci = new columnItem(this, getData);
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
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add:	

	        	String[] columns = {"title", 
	        						"swticher", 
	        						"start_time", 
	        						"end_time", 
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
