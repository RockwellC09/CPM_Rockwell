package com.ChristopherRockwell.watchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileActivity extends Activity implements OnClickListener {

	TextView username;
	TextView email;
	TextView fav;
	TextView hours;
	Intent loginActivity;
	Intent editProfileActivity;
	Intent watchListActivity;
	Context context;
	Button logoutBtn;
	Button editProfileBtn;
	private boolean onResumeHasRun;
	List<String> titlesList;
	List<String> daysList;
	List<String> timesList;
	List<String> idsList;
	SharedPreferences sharedPreferences;
	Editor editor;
	private ProgressDialog dialog;
	public  void showMyProgress () {
	    dialog = new ProgressDialog(this);
	    dialog.setCancelable(false);
	    dialog.setMessage("Loading...");
	    dialog.show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		context = this;
		
		// initialize
		onResumeHasRun = false;
		username = (TextView) findViewById(R.id.pro_username);
		email = (TextView) findViewById(R.id.pro_email);
		fav = (TextView) findViewById(R.id.pro_fav);
		hours = (TextView) findViewById(R.id.pro_hours);
		logoutBtn = (Button) findViewById(R.id.logout);
		editProfileBtn = (Button) findViewById(R.id.editProfile);
		titlesList = new ArrayList<String>();
		daysList = new ArrayList<String>();
		timesList = new ArrayList<String>();
		idsList = new ArrayList<String>();
		
		logoutBtn.setOnClickListener(this);
		editProfileBtn.setOnClickListener(this);

		ParseUser currentUser = ParseUser.getCurrentUser();
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		if (currentUser != null) {
			if (sharedPreferences.getString("firstRun", null) == "yes" && isConnected) {
				Log.i("Getting", "Local Data");
				
				showMyProgress();
				editor.putString("firstRun", "no");
				editor.commit();
				// do stuff with the user
				username.setText(currentUser.getUsername());
				email.setText(currentUser.getEmail());
				String favStr = currentUser.get("favMovie").toString();
				if (!favStr.replace(" ", "").isEmpty()) {
					fav.setText(currentUser.get("favMovie").toString());
				} else {
					fav.setText("None");
				}

				ParseQuery<ParseObject> query = ParseQuery.getQuery("movInfo");
				query.whereEqualTo("user", currentUser);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> movObjs, ParseException arg1) {
						// TODO Auto-generated method stub
						if (arg1 == null) {
							for (ParseObject object : movObjs) {
								hours.setText(object.get("watchHrs").toString());
								editor.putString("hours", object.get("watchHrs").toString());
								editor.putString("hoursID", object.getObjectId());
								saveUserLocally();
							}
						} else {
							Log.d("Error", "Error: " + arg1.getMessage());
						}
					}
				});
			} else {
				// do stuff with the user
				username.setText(sharedPreferences.getString("username", null));
				email.setText(sharedPreferences.getString("email", null));
				String favStr = sharedPreferences.getString("fav", null);
				if (!favStr.replace(" ", "").isEmpty()) {
					fav.setText(sharedPreferences.getString("fav", null));
				} else {
					fav.setText("None");
				}
				hours.setText(sharedPreferences.getString("hours", null));
			}

		} else {
			// show the login screen
			loginActivity = new Intent(context, MainActivity.class);
			startActivity(loginActivity);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		watchListActivity = new Intent(context, WatchListActivity.class);
		startActivity(watchListActivity);
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.editProfile) {
			editProfileActivity = new Intent(context, EditProfileActivity.class);
			startActivity(editProfileActivity);
		} else {
			editor.clear();
			editor.apply();
			ParseUser.logOut();
			loginActivity = new Intent(context, MainActivity.class);
			startActivity(loginActivity);
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if (!onResumeHasRun) {
	        onResumeHasRun = true;
	        return;
	    }
	    
	    ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			String userStr = sharedPreferences.getString("username", null);
			if (userStr != null) {
				Log.i("Getting", "Local Data");
				// do stuff with the user
				username.setText(sharedPreferences.getString("username", null));
				email.setText(sharedPreferences.getString("email", null));
				String favStr = sharedPreferences.getString("fav", null);
				if (!favStr.replace(" ", "").isEmpty()) {
					fav.setText(sharedPreferences.getString("fav", null));
				} else {
					fav.setText("None");
				}
				hours.setText(sharedPreferences.getString("hours", null));
			} else {
				// do stuff with the user
				username.setText(currentUser.getUsername());
				email.setText(currentUser.getEmail());
				String favStr = currentUser.get("favMovie").toString();
				if (!favStr.replace(" ", "").isEmpty()) {
					fav.setText(currentUser.get("favMovie").toString());
				} else {
					fav.setText("None");
				}

				ParseQuery<ParseObject> query = ParseQuery.getQuery("movInfo");
				query.whereEqualTo("user", currentUser);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> movObjs, ParseException arg1) {
						// TODO Auto-generated method stub
						if (arg1 == null) {
							for (ParseObject object : movObjs) {
								hours.setText(object.get("watchHrs").toString());
								editor.putString("hours", object.get("watchHrs").toString());
								editor.putString("hoursID", object.getObjectId());
								saveUserLocally();
							}
						} else {
							Log.d("Error", "Error: " + arg1.getMessage());
						}
					}
				});
			}

		} else {
			// show the login screen
			loginActivity = new Intent(context, MainActivity.class);
			startActivity(loginActivity);
		}
	}
	
	// save user info locally
	private void saveUserLocally() {
		editor.putString("username", username.getText().toString());
		editor.putString("email", email.getText().toString());
		editor.putString("fav", fav.getText().toString());
		editor.putString("username", username.getText().toString());
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("itemInfo");
		query.whereEqualTo("user", currentUser);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> movObjs, ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg1 == null) {
					for (ParseObject object : movObjs) {
						titlesList.add(object.get("title").toString());
						daysList.add(object.get("day").toString());
						timesList.add(object.get("time").toString());
						idsList.add(object.getObjectId());
					} 
					editor.putString("title", titlesList.toString());
					editor.putString("day", daysList.toString());
					editor.putString("time", timesList.toString());
					editor.putString("ids", idsList.toString());
					editor.commit();
					Log.i("Local Data:", "Saved");
					dialog.hide();
				} else {
					Log.d("Error", "Error: " + arg1.getMessage());
				}
			}
		});
	}
}
