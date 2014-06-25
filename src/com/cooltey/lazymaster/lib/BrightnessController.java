package com.cooltey.lazymaster.lib;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager.LayoutParams;



public class BrightnessController{
	
	private Context mContext;
	private int mBrightness;
	private Window mWindow;
	private ContentResolver mResolver;
	
	
	public BrightnessController(Context context){
		mContext = context;		
		inital();
	}
	
	private void inital(){
		mResolver = mContext.getContentResolver();
		mWindow   = ((Activity)mContext).getWindow();
		
		// To handle the auto
		Settings.System.putInt(mResolver,
		Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		//Get the current system brightness
		mBrightness = android.provider.Settings.System.getInt(mResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS,-1);
	}
	
	public void setTmpBrightness(int birghtness){
		android.provider.Settings.System.putInt(mResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, birghtness);
		//Get the current window attributes
        LayoutParams layoutpars = mWindow.getAttributes();
        //Set the brightness of this window
        layoutpars.screenBrightness = birghtness / (float)255;
        //Apply attribute changes to this window
        mWindow.setAttributes(layoutpars);
	}
	
	public int getOriginalBrightness(){
		return mBrightness;
	}
}