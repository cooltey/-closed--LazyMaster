package com.cooltey.lazymaster.lib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.cooltey.lazymaster.R;


public class MainItemActions{

	private Context      mContext;
	private Cursor       mCursor;
	
	private EditText     titleView;
	private ToggleButton switchView;
	private TextView     startTimeView;
	private TextView     endTimeView;
	private SeekBar      brightnessView;
	private Spinner		 soundModeView;
	private CheckBox     repeatDayView;
	private ToggleButton weekSunView;
	private ToggleButton weekMonView;
	private ToggleButton weekTueView;
	private ToggleButton weekWedView;
	private ToggleButton weekThuView;
	private ToggleButton weekFriView;
	private ToggleButton weekSatView;
	
	private ColumnData setColumnData = new ColumnData();
	
	private ArrayList<ToggleButton> weekViewArray = new ArrayList<ToggleButton>();
	
	private ArrayList<Boolean> weekArray = new ArrayList<Boolean>();

	private BrightnessController brightnessController;
	
	
	public MainItemActions(Context context, Cursor cursor){
		mContext = context;
		mCursor	 = cursor;
		brightnessController = new BrightnessController(mContext);
	}
	
	public View getItemView(){
		View getView = null;
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		getView = inflater.inflate(R.layout.lazy_column_item, null);	
		
		titleView          = (EditText) getView.findViewById(R.id.title);
		switchView         = (ToggleButton) getView.findViewById(R.id.switcher);
		startTimeView      = (TextView) getView.findViewById(R.id.start_time);
		endTimeView        = (TextView) getView.findViewById(R.id.end_time);
		brightnessView     = (SeekBar) getView.findViewById(R.id.brightness);
		soundModeView      = (Spinner) getView.findViewById(R.id.sound_mode);
		repeatDayView      = (CheckBox) getView.findViewById(R.id.repeat_day);
		weekSunView        = (ToggleButton) getView.findViewById(R.id.week_sun);
		weekMonView        = (ToggleButton) getView.findViewById(R.id.week_mon);
		weekTueView        = (ToggleButton) getView.findViewById(R.id.week_tue);
		weekWedView        = (ToggleButton) getView.findViewById(R.id.week_wed);
		weekThuView        = (ToggleButton) getView.findViewById(R.id.week_thu);
		weekFriView        = (ToggleButton) getView.findViewById(R.id.week_fri);
		weekSatView        = (ToggleButton) getView.findViewById(R.id.week_sat);
		
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
			setColumnData.getStartDate 		= mCursor.getString(5);
			setColumnData.getEndDate 		= mCursor.getString(6);
			setColumnData.getBrightness 	= mCursor.getInt(7);
			setColumnData.getSounds 		= mCursor.getInt(8);
			setColumnData.getRepeatDay		= Boolean.parseBoolean(mCursor.getString(9));
			setColumnData.getWeekSun 		= Boolean.parseBoolean(mCursor.getString(10));
			setColumnData.getWeekMon 		= Boolean.parseBoolean(mCursor.getString(11));
			setColumnData.getWeekTue 		= Boolean.parseBoolean(mCursor.getString(12));
			setColumnData.getWeekWed 		= Boolean.parseBoolean(mCursor.getString(13));
			setColumnData.getWeekThu 		= Boolean.parseBoolean(mCursor.getString(14));
			setColumnData.getWeekFri 		= Boolean.parseBoolean(mCursor.getString(15));
			setColumnData.getWeekSat 		= Boolean.parseBoolean(mCursor.getString(16));
			
			// set spinner
			ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.sound_mode, android.R.layout.simple_spinner_item);
			spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        soundModeView.setAdapter(spinnerAdapter);
			
			titleView.setText(setColumnData.getTitle);
			switchView.setChecked(setColumnData.getSwitcher);
			startTimeView.setText(setColumnData.getStartTime);
			endTimeView.setText(setColumnData.getEndTime);
			brightnessView.setProgress(setColumnData.getBrightness);
			soundModeView.setSelection(setColumnData.getSounds);
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
						 String newHours = "" + hourOfDay;
						 if(hourOfDay < 10){
							 newHours = "0" + hourOfDay;
						 }
						 
						 
						 String newMins = "" + minute;
						 if(minute < 10){
							 newMins = "0" + minute;
						 }
						 tv.setText(newHours + ":" + newMins);
						 // set data
						 setColumnData.getStartTime = newHours + ":" + newMins;
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
						 String newHours = "" + hourOfDay;
						 if(hourOfDay < 10){
							 newHours = "0" + hourOfDay;
						 }
						 
						 String newMins = "" + minute;
						 if(minute < 10){
							 newMins = "0" + minute;
						 }
						 tv.setText(newHours + ":" + newMins);
						 // set data
						 setColumnData.getEndTime = newHours + ":" + newMins;
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
				brightnessController.setTmpBrightness(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				brightnessController.setTmpBrightness(brightnessController.getOriginalBrightness());
				
				int getFinalProgress = seekBar.getProgress();
				setColumnData.getBrightness = getFinalProgress;
				saveIntoDB();
			}
			
		});
		
		
		soundModeView.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				setColumnData.getSounds = position;
				saveIntoDB();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
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

		 SharedPreferences settings = mContext.getSharedPreferences("system_info", 0);	
		 SharedPreferences.Editor getData = settings.edit();
	     getData.putBoolean("activated", false);
	     getData.putInt("task_id", Integer.parseInt(setColumnData.getId));
	     getData.commit();
	    
		 setDateForAlarm();
		
		 DatabaseHelper db = new DatabaseHelper(mContext);
		 
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

		String[] values = {setColumnData.getTitle, 
						   setColumnData.getSwitcher + "", 
						   setColumnData.getStartTime, 
						   setColumnData.getEndTime, 
						   setColumnData.getStartDate, 
						   setColumnData.getEndDate, 
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
	
	private void setDateForAlarm(){
		String[] splitStartTime = setColumnData.getStartTime.split(":");
		String[] splitEndTime = setColumnData.getEndTime.split(":");
		
		int startTime = Integer.parseInt(splitStartTime[0] + "" + splitStartTime[1]);
		int endTime = Integer.parseInt(splitEndTime[0] + "" + splitEndTime[1]);
		
		if(endTime < startTime){
			Calendar nc  = Calendar.getInstance(); 
			
			String getStartDate = nc.get(Calendar.YEAR) + "" + nc.get(Calendar.MONTH) + "" + nc.get(Calendar.DATE);
			
			nc.add(Calendar.DATE, +1);
			String getEndDate = nc.get(Calendar.YEAR) + "" + nc.get(Calendar.MONTH) + "" + nc.get(Calendar.DATE);

			setColumnData.getStartDate = getStartDate;
			setColumnData.getEndDate   = getEndDate;
		}else{
			Calendar nc  = Calendar.getInstance(); 
			String getCurrentDate = nc.get(Calendar.YEAR) + "" + nc.get(Calendar.MONTH) + "" + nc.get(Calendar.DATE);
		
			setColumnData.getStartDate = getCurrentDate;
			setColumnData.getEndDate   = getCurrentDate;
		}
	}
}
