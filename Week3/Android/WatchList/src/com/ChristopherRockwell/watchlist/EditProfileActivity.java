package com.ChristopherRockwell.watchlist;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EditProfileActivity extends Activity implements OnClickListener, ValidationListener {
	EditText oldPwd;
	@Password(order = 1)
	@TextRule(order = 2, minLength = 8, message = "Enter at least 8 characters for your password.")
	EditText newPwd;
	@ConfirmPassword(order = 3)
	EditText confirmPwd;
	EditText fav;
	Spinner hours;
	Button updatePwdBtn;
	Button editProfileBtn;
	Context context;
	private Validator validator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		context = this;
		
		validator = new Validator(this);
	    validator.setValidationListener(this);

		// initialize
		oldPwd = (EditText) findViewById(R.id.ep_old_pwd);
		newPwd = (EditText) findViewById(R.id.ep_new_pwd);
		confirmPwd = (EditText) findViewById(R.id.ep_confirm_pwd);
		fav = (EditText) findViewById(R.id.ep_fav);
		hours = (Spinner) findViewById(R.id.ep_hours);
		updatePwdBtn = (Button) findViewById(R.id.ep_update_pwd);
		editProfileBtn = (Button) findViewById(R.id.ep_edit_profile);

		updatePwdBtn.setOnClickListener(this);
		editProfileBtn.setOnClickListener(this);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// do stuff with the user
			String favStr = currentUser.get("favMovie").toString();
			if (!favStr.replace(" ", "").isEmpty()) {
				fav.setText(currentUser.get("favMovie").toString());
			} else {
				fav.setText("");
			}

			ParseQuery<ParseObject> query = ParseQuery.getQuery("movInfo");
			query.whereEqualTo("user", currentUser);
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> movObjs, ParseException arg1) {
					// TODO Auto-generated method stub
					if (arg1 == null) {
						for (ParseObject object : movObjs) {
							hours.setSelection(Integer.parseInt(object.get("watchHrs").toString()));
						}
					} else {
						Log.d("score", "Error: " + arg1.getMessage());
					}
				}
			});

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ep_update_pwd) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			String pwdStr = sharedPreferences.getString("pwd", null);
			if (oldPwd.getText().toString().equals(pwdStr)) {
				validator.validate();
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				
				alertDialogBuilder
				.setTitle("Invalid Password")
				.setMessage("Please enter your current password correctly. (Check Caps Lock)")
				.setPositiveButton("Ok", null);
				alertDialogBuilder.show();
			}
		} else {
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				currentUser.put("favMovie", fav.getText().toString());
				currentUser.saveInBackground();
			}
			ParseQuery<ParseObject> query = ParseQuery.getQuery("movInfo");
			query.whereEqualTo("user", currentUser);
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> movObjs, ParseException arg1) {
					// TODO Auto-generated method stub
					if (arg1 == null) {
						for (ParseObject object : movObjs) {
							object.put("watchHrs", Integer.parseInt(hours.getSelectedItem().toString()));
							object.saveInBackground();
						}
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						
						alertDialogBuilder
						.setTitle("Success!")
						.setMessage("Profile Updated!")
						.setPositiveButton("Ok", null);
						alertDialogBuilder.show();
						
					} else {
AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						
						alertDialogBuilder
						.setTitle("Error")
						.setMessage("There was an error when updating your profile. Please check your connection and try again.")
						.setPositiveButton("Ok", null);
						alertDialogBuilder.show();
						Log.d("Error", "Error: " + arg1.getMessage());
					}
				}
			});
		}
	}
	
	public void onValidationSucceeded() {
		// save the password to parse
        ParseUser currentUser = ParseUser.getCurrentUser();
		currentUser.setPassword(newPwd.getText().toString());
		currentUser.saveInBackground();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		alertDialogBuilder
		.setTitle("Success!")
		.setMessage("Password Updated!")
		.setPositiveButton("Ok", null);
		alertDialogBuilder.show();
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
