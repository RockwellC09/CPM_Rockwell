package com.ChristopherRockwell.watchlist;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class AddItemActivity extends Activity implements ValidationListener {

	TimePicker myTimePicker;
	private boolean ignoreEvent = false;
	Intent watchListActivity;
	Context context;
	@Required(order = 1)
	EditText addTitle;
	private Validator validator;
	Spinner daySpinner;
	
	// credit Mark Horgan
	private static final int TIME_PICKER_INTERVAL = 5;
	private TimePicker.OnTimeChangedListener myTimePickerListener=new TimePicker.OnTimeChangedListener(){
	    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute){
	    	
	        if (ignoreEvent)
	            return;
	        
	        if (minute % TIME_PICKER_INTERVAL != 0){
	        	
	            int minuteFlr = minute - (minute % TIME_PICKER_INTERVAL);
	            minute = minuteFlr + (minute == minuteFlr + 1 ? TIME_PICKER_INTERVAL : 0);
	            
	            if (minute == 60)
	                minute = 0;
	            ignoreEvent=true;
	            timePicker.setCurrentMinute(minute);
	            ignoreEvent=false;
	        }

	    }
	};
	// End
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		context = this;
		
		myTimePicker = (TimePicker) findViewById(R.id.timePicker1);
		myTimePicker.setOnTimeChangedListener(myTimePickerListener);
		addTitle = (EditText) findViewById(R.id.add_title);
		daySpinner = (Spinner) findViewById(R.id.daySpinner);
		
		validator = new Validator(this);
	    validator.setValidationListener(this);
	    myTimePicker.setCurrentMinute(0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.add_item_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		if (item.getItemId() == R.id.m_save) {
			validator.validate();
		} else {
			watchListActivity = new Intent(context, WatchListActivity.class);
			startActivity(watchListActivity);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	public void onValidationSucceeded() {
		ParseObject movInfo = new ParseObject("itemInfo");
		movInfo.put("user", ParseUser.getCurrentUser());
		movInfo.put("title", addTitle.getText().toString());
		int hour;
		String apStr, timeStr;
		DecimalFormat minFormatter = new DecimalFormat("00");
		String formattedMin = minFormatter.format(myTimePicker.getCurrentMinute());
		
		if (myTimePicker.getCurrentHour() > 12) {
			hour = myTimePicker.getCurrentHour() - 12;
			apStr = "PM";
			timeStr = hour + ":" + formattedMin + " " + apStr;
		} else {
			apStr = "AM";
			timeStr = myTimePicker.getCurrentHour() + ":" + formattedMin + " " + apStr;
		}
		
		movInfo.put("time", timeStr);
		movInfo.put("day", daySpinner.getSelectedItem().toString());
		movInfo.saveInBackground();
		watchListActivity = new Intent(context, WatchListActivity.class);
		startActivity(watchListActivity);
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
    
}
