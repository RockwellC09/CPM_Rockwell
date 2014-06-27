package com.ChristopherRockwell.watchlist;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddItemActivity extends Activity implements ValidationListener {

	TimePicker myTimePicker;
	private boolean ignoreEvent = false;
	Intent watchListActivity;
	Context context;
	@Required(order = 1)
	EditText addTitle;
	private Validator validator;
	Spinner daySpinner;
	List<String> titlesList;
	List<String> daysList;
	List<String> timesList;
	List<String> idsList;
	
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
	    
	    SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		
	    String myTitleStr = sharedPreferences.getString("title", null);
		titlesList = new ArrayList<String>(Arrays.asList(myTitleStr.substring(1, myTitleStr.length() - 1).split(", ")));
		String dayStr = sharedPreferences.getString("day", null);
		daysList = new ArrayList<String>(Arrays.asList(dayStr.substring(1, dayStr.length() - 1).split(", ")));
		String timeStr = sharedPreferences.getString("time", null);
		timesList = new ArrayList<String>(Arrays.asList(timeStr.substring(1, timeStr.length() - 1).split(", ")));
		String idsStr = sharedPreferences.getString("ids", null);
		idsList = new ArrayList<String>(Arrays.asList(idsStr.substring(1, idsStr.length() - 1).split(", ")));
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
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		if (isConnected) {
			// has network connection
			
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			final Editor editor = sharedPreferences.edit();
			
			final ParseObject movInfo = new ParseObject("itemInfo");
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
			titlesList.add(addTitle.getText().toString());
			timesList.add(timeStr);
			daysList.add(daySpinner.getSelectedItem().toString());
			movInfo.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					// TODO Auto-generated method stub
					if (e == null) {
		                // Saved successfully.
						Log.i("ID:", movInfo.getObjectId().toString());
						idsList.add(movInfo.getObjectId());
						editor.putString("title", titlesList.toString());
						editor.putString("day", daysList.toString());
						editor.putString("time", timesList.toString());
						editor.putString("ids", idsList.toString());
						editor.commit();
						watchListActivity = new Intent(context, WatchListActivity.class);
						startActivity(watchListActivity);
		            }
		            else {
		                // The save failed.
		            	// show error alert
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder
						.setTitle("Error")
						.setMessage("Data not saved. Please logout and log back in and try again.")
						.setPositiveButton("Ok", null);
						alertDialogBuilder.show();
		            }
				}
		    });
		} else {
			// no network connection
			
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			final Editor editor = sharedPreferences.edit();
			
			final ParseObject movInfo = new ParseObject("itemInfo");
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
			titlesList.add(addTitle.getText().toString());
			timesList.add(timeStr);
			daysList.add(daySpinner.getSelectedItem().toString());
			editor.putString("title", titlesList.toString());
			editor.putString("day", daysList.toString());
			editor.putString("time", timesList.toString());
			editor.putString("ids", "");
			editor.commit();
			movInfo.saveEventually();//(new SaveCallback() {
//
//				@Override
//				public void done(ParseException e) {
//					// TODO Auto-generated method stub
//					if (e == null) {
//		                // Saved successfully.
//						Log.i("ID:", movInfo.getObjectId().toString());
//		            }
//		            else {
//		                // The save failed.
//		                
//		            }
//				}
//		    });
			watchListActivity = new Intent(context, WatchListActivity.class);
			startActivity(watchListActivity);
		}
		
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
