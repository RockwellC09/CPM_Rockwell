package com.ChristopherRockwell.watchlist;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

public class WatchListActivity extends Activity {
	ListView myListV;
	List<String> movieList;
	Context context;
	TextView noItemsTV;
	private boolean onResumeHasRun;
	Intent viewItemActivity;
	Intent addItemActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_list);
		context = this;
		
		onResumeHasRun = false;
		movieList = new ArrayList<String>();
		myListV = (ListView) findViewById(R.id.watchListView);
		noItemsTV = (TextView) findViewById(R.id.noItemTV);
		noItemsTV.setVisibility(View.GONE);
		
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
		
		addItemActivity = new Intent(context, AddItemActivity.class);
		startActivity(addItemActivity);
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
			editor.putString("title", title);
			editor.commit();
		}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if (!onResumeHasRun) {
	        onResumeHasRun = true;
	        return;
	    }
	    
	    movieList = new ArrayList<String>();
	    noItemsTV.setVisibility(View.GONE);
		
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
