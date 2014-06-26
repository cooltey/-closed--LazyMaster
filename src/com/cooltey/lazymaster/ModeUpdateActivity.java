package com.cooltey.lazymaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cooltey.lazymaster.lib.AudioController;
import com.cooltey.lazymaster.lib.BrightnessController;

public class ModeUpdateActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brightness_main);
		
		Intent intent = this.getIntent();
		int getSounds = intent.getExtras().getInt("sounds");
		int getBrightness = intent.getExtras().getInt("brightness");
		
		try{			
			// set volumn
			AudioController ac = new AudioController(this);
			ac.setTmpSounds(getSounds);
			
			BrightnessController bc = new BrightnessController(this);
			bc.setTmpBrightness(getBrightness);
			
			finish();
			
			//android.os.Process.killProcess(android.os.Process.myPid());
		}catch(Exception e){
			Log.d("getSounds", getSounds + "");
			Log.d("getBrightness", getBrightness + "");
			Log.d("exception", e + "");
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume(); 
		Log.d("On Resume", "yap");
	}
	
	
}
