package com.ChristopherRockwell.watchlist;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignupActivity extends Activity implements OnClickListener, ValidationListener{
	
	
	@TextRule(order = 1, minLength = 6, message = "Enter at least 6 characters for you username.")
	EditText username;
	@Password(order = 2)
	@TextRule(order = 3, minLength = 8, message = "Enter at least 8 characters for your password.")
	EditText pwd;
	@ConfirmPassword(order = 5)
	EditText confirmPwd;
	@Required(order = 6)
	@Email(order = 7)
	@TextRule(order = 3, message = "Enter a valid email.")
	EditText email;
	EditText fav;
	Spinner hours;
	Context context;
	Button signupBtn;
	private Validator validator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		context = this;
		
		validator = new Validator(this);
	    validator.setValidationListener(this);
		
		// initialize
		username = (EditText) findViewById(R.id.username);
		pwd = (EditText) findViewById(R.id.password);
		confirmPwd = (EditText) findViewById(R.id.confirmPwd);
		email = (EditText) findViewById(R.id.email);
		fav = (EditText) findViewById(R.id.fav);
		hours = (Spinner) findViewById(R.id.hours);
		signupBtn = (Button) findViewById(R.id.signup2);
		signupBtn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {		
		// TODO Auto-generated method stub
		validator.validate();
	}
	
	public void onValidationSucceeded() {
		ParseUser user = new ParseUser();
		user.setUsername(username.getText().toString());
		user.setPassword(pwd.getText().toString());
		user.setEmail(email.getText().toString());
		user.put("favMovie", fav.getText().toString());
		
		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					// Hooray! Let them use the app now.
					ParseObject movInfo = new ParseObject("movInfo");
					movInfo.put("user", ParseUser.getCurrentUser());
					movInfo.put("watchHrs", Integer.parseInt(hours.getSelectedItem().toString()));
					movInfo.saveInBackground();
					Toast.makeText(context, "Works", Toast.LENGTH_SHORT).show();
				} else {
					// Sign up didn't succeed. Look at the ParseException
					// to figure out what went wrong
				}
			}
		});
        Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
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
