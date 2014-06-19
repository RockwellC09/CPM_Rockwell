package com.ChristopherRockwell.watchlist;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewItemActivity extends Activity implements OnClickListener {

	TextView titleTV;
	TextView showingTV;
	Button deleteBtn;
	Context context;
	Intent watchListActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_item);
		context = this;
		
		// initialize 
		titleTV = (TextView) findViewById(R.id.iv_title);
		showingTV = (TextView) findViewById(R.id.iv_showing);
		deleteBtn = (Button) findViewById(R.id.iv_delete);
		deleteBtn.setOnClickListener(this);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			String titleStr = sharedPreferences.getString("title", null);
			titleTV.setText(titleStr);
			ParseQuery<ParseObject> query = ParseQuery.getQuery("itemInfo");
			query.whereEqualTo("user", currentUser);
			query.whereEqualTo("title", titleStr);
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> movObjs, ParseException arg1) {
					// TODO Auto-generated method stub
					String showingStr = "";
					if (arg1 == null) {
						for (ParseObject object : movObjs) {
							showingStr = "Showing " + object.get("day") + " at " + object.get("time");
						} 
						showingTV.setText(showingStr);
					} else {
						Log.d("Error", "Error: " + arg1.getMessage());
					}
				}
			});
		}
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder
		.setTitle("Delete Item")
		.setMessage("Are you sure?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	ParseUser currentUser = ParseUser.getCurrentUser();
        		if (currentUser != null) {
        			ParseQuery<ParseObject> query = ParseQuery.getQuery("itemInfo");
        			query.whereEqualTo("user", currentUser);
        			query.whereEqualTo("title", titleTV.getText().toString());
        			query.findInBackground(new FindCallback<ParseObject>() {

        				@Override
        				public void done(List<ParseObject> movObjs, ParseException arg1) {
        					// TODO Auto-generated method stub
        					if (arg1 == null) {
        						for (ParseObject object : movObjs) {
        							try {
										object.delete();
										watchListActivity = new Intent(context, WatchListActivity.class);
										startActivity(watchListActivity);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
        						} 
        					} else {
        						Log.d("Error", "Error: " + arg1.getMessage());
        					}
        				}
        			});
        		}
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
		alertDialogBuilder.show();
	}
}
