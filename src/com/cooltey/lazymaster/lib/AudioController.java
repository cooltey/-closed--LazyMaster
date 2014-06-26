package com.cooltey.lazymaster.lib;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager.LayoutParams;



public class AudioController{
	
	private Context mContext;
	private int mSounds;
	private AudioManager mAudioManager;
	
	
	public AudioController(Context context){
		mContext = context;		
		inital();
	}
	
	private void inital(){
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void setTmpSounds(int sounds){
		switch (sounds) {
	    	case 0:
	    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	    		break;
	    	case 1:
	    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	    		break;
	    	case 2:
	    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	    		break;
		}
	}
	
	public String getOriginalSoundMode(){
		String soundMode = "0";
		
		switch (mAudioManager.getRingerMode()) {
		    case AudioManager.RINGER_MODE_SILENT:
		    	soundMode = "1";
		        break;
		    case AudioManager.RINGER_MODE_VIBRATE:
		    	soundMode = "2";
		        break;
		    case AudioManager.RINGER_MODE_NORMAL:
		    	soundMode = "0";
		        break;
		}
		
		return soundMode;
	}
}