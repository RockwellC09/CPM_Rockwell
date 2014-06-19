package com.ChristopherRockwell.watchlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity implements OnClickListener {
	Button loginBtn;
	Button signupBtn;
	Intent signupActivity;
	Context context;
	Intent profileActivity;
	EditText username;
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;

		// initialize parse
		Parse.initialize(context, "roh7D1LeQ56iBto6N5KQMnkhKOaiy2mnZ8YlpQS8", "QX4mWwY3D2uwGmpTxyQkj66uJnABUsbNfAM7dJCe");

		// initialize
		loginBtn = (Button) findViewById(R.id.login);
		signupBtn = (Button) findViewById(R.id.signup1);
		loginBtn.setOnClickListener(this);
		signupBtn.setOnClickListener(this);
		username = (EditText) findViewById(R.id.userlog);
		password = (EditText) findViewById(R.id.pwdlog);

		// check to see if a user is signed in
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// do stuff with the user
			profileActivity = new Intent(context, ProfileActivity.class);
			startActivity(profileActivity);
		} else {
			// show the signup login screen
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.signup1) {
			signupActivity = new Intent(context,SignupActivity.class);
			startActivity(signupActivity);
		} else {
			ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {

				@Override
				public void done(ParseUser user, ParseException e) {
					// TODO Auto-generated method stub
					if (user != null) {
						// Hooray! The user is logged in.
						
						// save pasword for later use
						savePwd();
						
						profileActivity = new Intent(context, ProfileActivity.class);
						startActivity(profileActivity);
					} else {
						// Signup failed. Look at the ParseException to see what happened.
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder
						.setTitle("Invalid Login")
						.setMessage("Please check your login credentials and try again.")
						.setPositiveButton("Ok", null);
						alertDialogBuilder.show();

					}
				}
			});
		}
	}

	// save password to be check against later when updating it
	private void savePwd() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString("pwd", password.getText().toString());
		editor.commit();
	}

}
