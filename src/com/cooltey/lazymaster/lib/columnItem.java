package com.cooltey.lazymaster.lib;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.cooltey.lazymaster.R;


public class columnItem{

	private Context      mContext;
	private Cursor       mCursor;
	
	private EditText     titleView;
	private ToggleButton switchView;
	private TextView     startTimeView;
	private TextView     endTimeView;
	private SeekBar      brightnessView;
	private SeekBar      soundView;
	private CheckBox     repeatDayView;
	private ToggleButton weekSunView;
	private ToggleButton weekMonView;
	private ToggleButton weekTueView;
	private ToggleButton weekWedView;
	private ToggleButton weekThuView;
	private ToggleButton weekFriView;
	private ToggleButton weekSatView;
	
	private columnData setColumnData = new columnData();
	
	private ArrayList<ToggleButton> weekViewArray = new ArrayList<ToggleButton>();
	
	private ArrayList<Boolean> weekArray = new ArrayList<Boolean>();
	
	public columnItem(Context context, Cursor cursor){
		mContext = context;
		mCursor	 = cursor;
		
	}
	
	public View getItemView(){
		View getView = null;
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		getView = inflater.inflate(R.layout.lazy_column_item, null);	
		
		titleView      = (EditText) getView.findViewById(R.id.title);
		switchView     = (ToggleButton) getView.findViewById(R.id.switcher);
		startTimeView  = (TextView) getView.findViewById(R.id.start_time);
		endTimeView    = (TextView) getView.findViewById(R.id.end_time);
		brightnessView = (SeekBar) getView.findViewById(R.id.brightness);
		soundView      = (SeekBar) getView.findViewById(R.id.sounds);
		repeatDayView  = (CheckBox) getView.findViewById(R.id.repeat_day);
		weekSunView    = (ToggleButton) getView.findViewById(R.id.week_sun);
		weekMonView    = (ToggleButton) getView.findViewById(R.id.week_mon);
		weekTueView    = (ToggleButton) getView.findViewById(R.id.week_tue);
		weekWedView    = (ToggleButton) getView.findViewById(R.id.week_wed);
		weekThuView    = (ToggleButton) getView.findViewById(R.id.week_thu);
		weekFriView    = (ToggleButton) getView.findViewById(R.id.week_fri);
		weekSatView    = (ToggleButton) getView.findViewById(R.id.week_sat);
		
		// set View data
		setItemData();
		
		// set Action
		setViewAction();
		
		return getView;
	}
	
	public void setItemData(){
		if(mCursor != null){
			setColumnData.getId 			= mCursor.getString(0);
			setColumnData.getTitle 			= mCursor.getString(1);
			setColumnData.getSwitcher 		= Boolean.parseBoolean(mCursor.getString(2));
			setColumnData.getStartTime 		= mCursor.getString(3);
			setColumnData.getEndTime 		= mCursor.getString(4);
			setColumnData.getBrightness 	= mCursor.getInt(5);
			setColumnData.getSounds 		= mCursor.getInt(6);
			setColumnData.getRepeatDay		= Boolean.parseBoolean(mCursor.getString(7));
			setColumnData.getWeekSun 		= Boolean.parseBoolean(mCursor.getString(8));
			setColumnData.getWeekMon 		= Boolean.parseBoolean(mCursor.getString(9));
			setColumnData.getWeekTue 		= Boolean.parseBoolean(mCursor.getString(10));
			setColumnData.getWeekWed 		= Boolean.parseBoolean(mCursor.getString(11));
			setColumnData.getWeekThu 		= Boolean.parseBoolean(mCursor.getString(12));
			setColumnData.getWeekFri 		= Boolean.parseBoolean(mCursor.getString(13));
			setColumnData.getWeekSat 		= Boolean.parseBoolean(mCursor.getString(14));
			
			titleView.setText(setColumnData.getTitle);
			switchView.setChecked(setColumnData.getSwitcher);
			startTimeView.setText(setColumnData.getStartTime);
			endTimeView.setText(setColumnData.getEndTime);
			brightnessView.setProgress(setColumnData.getBrightness);
			soundView.setProgress(setColumnData.getSounds);
			repeatDayView.setChecked(setColumnData.getRepeatDay);
			
			weekViewArray.add(weekSunView);
			weekViewArray.add(weekMonView);
			weekViewArray.add(weekTueView);
			weekViewArray.add(weekWedView);
			weekViewArray.add(weekThuView);
			weekViewArray.add(weekFriView);
			weekViewArray.add(weekSatView);

			weekArray.add(setColumnData.getWeekSun);
			weekArray.add(setColumnData.getWeekMon);
			weekArray.add(setColumnData.getWeekTue);
			weekArray.add(setColumnData.getWeekWed);
			weekArray.add(setColumnData.getWeekThu);
			weekArray.add(setColumnData.getWeekFri);
			weekArray.add(setColumnData.getWeekSat);
			
			for(int i = 0; i < weekArray.size(); i++){
				int position = i;

				if(setColumnData.getRepeatDay == true){
					weekViewArray.get(position).setEnabled(true);
				}
				weekViewArray.get(position).setChecked(weekArray.get(position));
			}
			
			
		}
	}
	
	private void setViewAction(){
		
		titleView.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				setColumnData.getTitle = titleView.getEditableText().toString();
				saveIntoDB();
			}
			
		});
		
		switchView.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				// save into database
				setColumnData.getSwitcher = switchView.isChecked();
				saveIntoDB();
			}
			
		});
		
		startTimeView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c        = Calendar.getInstance(); 
				final TextView tv = (TextView) v;
				int setHour 	  = c.get(Calendar.HOUR_OF_DAY);
				int setMinute	  =	c.get(Calendar.MINUTE);
				
				String getCurrentPick = tv.getText().toString();
				if(getCurrentPick.length() > 0){
					String[] currentTime = getCurrentPick.split(":");
					setHour 	= Integer.parseInt(currentTime[0]);
					setMinute 	= Integer.parseInt(currentTime[1]);
				}
				
		        // TODO Auto-generated method stub  
		        new TimePickerDialog(mContext,  
		        		new OnTimeSetListener() {  
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						 // TODO Auto-generated method stub
						 tv.setText(hourOfDay + ":" + minute);
						 // set data
						 setColumnData.getStartTime = hourOfDay + ":" + minute;
						 // save into db
						saveIntoDB();
					}  
		        }, setHour, setMinute, true).show();  
			}
			
		});
		
		endTimeView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c        = Calendar.getInstance(); 
				final TextView tv = (TextView) v;
				int setHour 	  = c.get(Calendar.HOUR_OF_DAY);
				int setMinute	  =	c.get(Calendar.MINUTE);
				
				String getCurrentPick = tv.getText().toString();
				if(getCurrentPick.length() > 0){
					String[] currentTime = getCurrentPick.split(":");
					setHour 	= Integer.parseInt(currentTime[0]);
					setMinute 	= Integer.parseInt(currentTime[1]);
				}
				
		        // TODO Auto-generated method stub  
		        new TimePickerDialog(mContext,  
		        		new OnTimeSetListener() {  
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						 // TODO Auto-generated method stub
						 tv.setText(hourOfDay + ":" + minute);
						 // set data
						 setColumnData.getEndTime = hourOfDay + ":" + minute;
						 // save into db
						saveIntoDB();
					}  
		        }, setHour, setMinute, true).show();  
			}
			
		});
		
		brightnessView.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
				int getFinalProgress = seekBar.getProgress();
				setColumnData.getBrightness = getFinalProgress;
				saveIntoDB();
			}
			
		});
		
		soundView.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
				int getFinalProgress = seekBar.getProgress();
				setColumnData.getSounds = getFinalProgress;
				saveIntoDB();
			}
			
		});
		
		
		
		repeatDayView.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				setColumnData.getRepeatDay = isChecked;
							
				for(int i = 0; i < weekArray.size(); i++){
					final int setPosition = i;

					if(isChecked){		
						((View) weekViewArray.get(setPosition)).setEnabled(true);
					}else{
						((View) weekViewArray.get(setPosition)).setEnabled(false);
					}
				}			
				
				saveIntoDB();
			}
			
		});
		
		
		// set list
		for(int i = 0; i < weekViewArray.size(); i++){
			final int position = i;
			weekViewArray.get(position).setOnCheckedChangeListener(new OnCheckedChangeListener(){
	
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					
					switch(position){
						case 0:
							setColumnData.getWeekSun = isChecked;
							break;
						case 1:
							setColumnData.getWeekMon = isChecked;
							break;
						case 2:
							setColumnData.getWeekTue = isChecked;
							break;
						case 3:
							setColumnData.getWeekWed = isChecked;
							break;
						case 4:
							setColumnData.getWeekThu = isChecked;
							break;
						case 5:
							setColumnData.getWeekFri = isChecked;
							break;
						case 6:
							setColumnData.getWeekSat = isChecked;
							break;
					}
					saveIntoDB();
				}			
			});
		}
	}
	
	private void saveIntoDB(){

		 databaseHelper db = new databaseHelper(mContext);
		 
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

		String[] values = {setColumnData.getTitle, 
						   setColumnData.getSwitcher + "", 
						   setColumnData.getStartTime, 
						   setColumnData.getEndTime, 
						   setColumnData.getBrightness + "", 
						   setColumnData.getSounds + "", 
						   setColumnData.getRepeatDay + "", 
						   setColumnData.getWeekSun + "",
						   setColumnData.getWeekMon + "", 
						   setColumnData.getWeekTue + "", 
						   setColumnData.getWeekWed + "", 
						   setColumnData.getWeekThu + "", 
						   setColumnData.getWeekFri + "", 
						   setColumnData.getWeekSat + ""};
		
		db.update("lazy_master", Integer.parseInt(setColumnData.getId), columns, values);   
		db.close();
	}
}
