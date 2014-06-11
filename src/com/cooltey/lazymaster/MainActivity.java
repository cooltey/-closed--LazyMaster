package com.cooltey.lazymaster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LinearLayout mainContent = (LinearLayout) findViewById(R.id.scrollContent); 
		
		
		for(int i = 0; i < 3; i++){			
			// tmp function
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogView = inflater.inflate(R.layout.lazy_column_item, null);			
			mainContent.addView(dialogView);
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add:	
	        		        	
	        	
	            return true;
	            
	        case R.id.action_about:
	        	
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
