package com.ChristopherRockwell.watchlist;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
		
		logoutBtn.setOnClickListener(this);
		editProfileBtn.setOnClickListener(this);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
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
						}
					} else {
						Log.d("Error", "Error: " + arg1.getMessage());
					}
				}
			});

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
			// update user info just in case they've edited their profile
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
						}
					} else {
						Log.d("Error", "Error: " + arg1.getMessage());
					}
				}
			});

		} else {
			// show the login screen
			loginActivity = new Intent(context, MainActivity.class);
			startActivity(loginActivity);
		}
	}
}
