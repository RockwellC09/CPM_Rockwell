package com.ChristopherRockwell.watchlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class WatchListActivity extends Activity {
	ListView myListV;
	List<String> movieList;
	Context context;
	TextView noItemsTV;
	private boolean onResumeHasRun;
	Intent viewItemActivity;
	Intent addItemActivity;
	Intent profileActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_list);
		context = this;
		
		onResumeHasRun = false;
		myListV = (ListView) findViewById(R.id.watchListView);
		noItemsTV = (TextView) findViewById(R.id.noItemTV);
		noItemsTV.setVisibility(View.GONE);
		
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userStr = sharedPreferences.getString("username", null);
		if (userStr != null) {
			Log.i("Getting", "Local Data");
			String titleStr = sharedPreferences.getString("title", null);
			movieList = new ArrayList<String>(Arrays.asList(titleStr.substring(1, titleStr.length() - 1).split(", ")));
			setAdapter();
		} else {
			movieList = new ArrayList<String>();
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("itemInfo");
				query.whereEqualTo("user", currentUser);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> movObjs, ParseException arg1) {
						// TODO Auto-generated method stub
						if (arg1 == null) {
							for (ParseObject object : movObjs) {
								movieList.add(object.get("title").toString());
							} 
							setAdapter();
						} else {
							Log.d("Error", "Error: " + arg1.getMessage());
						}
					}
				});
			}
		}
		
		myListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String selectedTitle = (myListV.getItemAtPosition(position).toString());
				saveTitle(selectedTitle);
				
				viewItemActivity = new Intent(context, ViewItemActivity.class);
				startActivity(viewItemActivity);
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.watch_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		if (item.getItemId() == R.id.add) {
			addItemActivity = new Intent(context, AddItemActivity.class);
			startActivity(addItemActivity);
		} else {
			profileActivity = new Intent(context, ProfileActivity.class);
			startActivity(profileActivity);
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void setAdapter() {
		if (!movieList.isEmpty()) {
			ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(context, R.layout.list_row, R.id.movTitle, movieList);
			myListV.setAdapter(myArrayAdapter);
		} else {
			noItemsTV.setVisibility(View.VISIBLE);
		}
	}
	
	// save title to be used when view an item
		private void saveTitle(String title) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			Editor editor = sharedPreferences.edit();
			editor.putString("movTitle", title);
			editor.commit();
		}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if (!onResumeHasRun) {
	        onResumeHasRun = true;
	        return;
	    }
	    
	    SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userStr = sharedPreferences.getString("username", null);
		if (userStr != null) {
			Log.i("Getting", "Local Data");
			String titleStr = sharedPreferences.getString("title", null);
			movieList = new ArrayList<String>(Arrays.asList(titleStr.substring(1, titleStr.length() - 1).split(", ")));
			setAdapter();
		} else {
			movieList = new ArrayList<String>();
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("itemInfo");
				query.whereEqualTo("user", currentUser);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> movObjs, ParseException arg1) {
						// TODO Auto-generated method stub
						if (arg1 == null) {
							for (ParseObject object : movObjs) {
								movieList.add(object.get("title").toString());
							} 
							setAdapter();
						} else {
							Log.d("Error", "Error: " + arg1.getMessage());
						}
					}
				});
			}
		}
	}
}
