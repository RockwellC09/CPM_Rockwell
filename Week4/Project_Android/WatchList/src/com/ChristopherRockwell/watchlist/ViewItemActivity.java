package com.ChristopherRockwell.watchlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ViewItemActivity extends Activity implements OnClickListener {

	TextView titleTV;
	TextView showingTV;
	Button deleteBtn;
	Context context;
	Intent watchListActivity;
	List<String> titlesList;
	List<String> daysList;
	List<String> timesList;
	List<String> idsList;
	int count;
	String showingStr;
	String objId;
	
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
		
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userStr = sharedPreferences.getString("username", null);
		if (userStr != null) {
			Log.i("Getting", "Local Data");
			
			String titleStr = sharedPreferences.getString("movTitle", null);
			titleTV.setText(titleStr);
			String myTitleStr = sharedPreferences.getString("title", null);
			titlesList = new ArrayList<String>(Arrays.asList(myTitleStr.substring(1, myTitleStr.length() - 1).split(", ")));
			String dayStr = sharedPreferences.getString("day", null);
			daysList = new ArrayList<String>(Arrays.asList(dayStr.substring(1, dayStr.length() - 1).split(", ")));
			String timeStr = sharedPreferences.getString("time", null);
			timesList = new ArrayList<String>(Arrays.asList(timeStr.substring(1, timeStr.length() - 1).split(", ")));
			String idsStr = sharedPreferences.getString("ids", null);
			idsList = new ArrayList<String>(Arrays.asList(idsStr.substring(1, idsStr.length() - 1).split(", ")));
			count = 0;

			for (int i = 0; !titlesList.get(i).equals(sharedPreferences.getString("movTitle", null)); i++) {
				count++;
			}
			showingStr = "Showing " + daysList.get(count) + " at " + timesList.get(count);
			showingTV.setText(showingStr);
		} else {
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				String titleStr = sharedPreferences.getString("title", null);
				titleTV.setText(titleStr);
				ParseQuery<ParseObject> query = ParseQuery.getQuery("itemInfo");
				query.whereEqualTo("user", currentUser);
				query.whereEqualTo("title", titleStr);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> movObjs, ParseException arg1) {
						// TODO Auto-generated method stub
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
    				
    				for (int i = 0; i < titlesList.size(); i++) {
    					if (titlesList.get(i).equals(sharedPreferences.getString("movTitle", null))) {
    						titlesList.remove(i);
    						daysList.remove(i);
    						timesList.remove(i);
    						idsList.remove(i);
    					}
    				}
					editor.putString("title", titlesList.toString());
					editor.putString("day", daysList.toString());
					editor.putString("time", timesList.toString());
					editor.putString("ids", idsList.toString());
					editor.commit();
    				
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
    			} else {
    				// no network connection
    				
    				SharedPreferences sharedPreferences = PreferenceManager
    						.getDefaultSharedPreferences(context);
    				final Editor editor = sharedPreferences.edit();
 
    				for (int i = 0; i < titlesList.size(); i++) {
    					if (titlesList.get(i).equals(sharedPreferences.getString("movTitle", null))) {
    						titlesList.remove(i);
    						daysList.remove(i);
    						timesList.remove(i);
    						objId = idsList.get(i);
    						idsList.remove(i);
    						Log.i("OBJ Id:", objId);
    					}
    				}
    				
    				ParseObject movInfo = new ParseObject("itemInfo");
    				if (objId.equals("")) {
    					movInfo.deleteEventually(new DeleteCallback() {

							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub
								ParseUser currentUser = ParseUser.getCurrentUser();
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

            				
            			});
    				} else {
    					movInfo.setObjectId(objId);
    					movInfo.deleteEventually();
    				}
    				
    				editor.putString("title", titlesList.toString());
					editor.putString("day", daysList.toString());
					editor.putString("time", timesList.toString());
					editor.putString("ids", idsList.toString());
					editor.commit();

					watchListActivity = new Intent(context, WatchListActivity.class);
					startActivity(watchListActivity);
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
