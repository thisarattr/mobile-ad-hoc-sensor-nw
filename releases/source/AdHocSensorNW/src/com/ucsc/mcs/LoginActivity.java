/**
 * 
 */
package com.ucsc.mcs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ucsc.mcs.constants.CommonConstants;

/**
 * @author thisara
 *
 */
public class LoginActivity extends Activity implements OnClickListener{
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	EditText editTxtUsername, editTxtPassword;
	Button btnLogin, btnSignup, btnforgot;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//Bind UI elements
		editTxtUsername = (EditText)findViewById(R.id.editTxtUsernameLogin);
		editTxtPassword = (EditText)findViewById(R.id.editTxtPasswordLogin);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnSignup = (Button)findViewById(R.id.btnSignupLogin);
		btnforgot = (Button)findViewById(R.id.btnForgotPw);
		btnLogin.setOnClickListener(this);
		btnSignup.setOnClickListener(this);
		btnforgot.setOnClickListener(this);
		
		//using user details from save file not application session data
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String username = prefs.getString(CommonConstants.USERNAME, "");
		String password = prefs.getString(CommonConstants.PASSWORD, "");
		
		editTxtUsername.setText(username);
		editTxtPassword.setText(password);
	}

	public void onClick(final View v) {
		
		if (v.getId() == R.id.btnLogin) {
			
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String loginResponse = ServiceInvoker.login(editTxtUsername.getText().toString().trim(), editTxtPassword.getText().toString().trim(),
					telephonyManager.getDeviceId());

			if (loginResponse.equals("true") || loginResponse.equals("false")) {
				boolean loginSuccess = Boolean.valueOf(loginResponse);

				if (loginSuccess) {
					//save user details to access when application is alive.
					SharedPreferences settings = getSharedPreferences(CommonConstants.PREF_USER_DETAILS, MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(CommonConstants.USERNAME, editTxtUsername.getText().toString().trim());
					editor.putString(CommonConstants.PASSWORD, editTxtPassword.getText().toString().trim());
					editor.commit();
					
					
					AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Do you want to save password?");
					alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//This will save user details on file and even application restarts it will be there.
							SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
							SharedPreferences.Editor edit = pref.edit();
							edit.putString(CommonConstants.USERNAME, editTxtUsername.getText().toString().trim());
							edit.putString(CommonConstants.PASSWORD, editTxtPassword.getText().toString().trim());
							edit.commit();
							
							Intent home = new Intent(v.getContext(), HomeActivity.class);
							startActivityForResult(home, CommonConstants.HOME_REQ_ID);
							return;
						}

					});
					alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							Intent home = new Intent(v.getContext(), HomeActivity.class);
							startActivityForResult(home, CommonConstants.HOME_REQ_ID);
							return;
						}
					});
					alertDialog.show();
					

				} else {
					Toast.makeText(LoginActivity.this, "Incorrect  Username or Password. Please try again!", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(LoginActivity.this, loginResponse, Toast.LENGTH_LONG).show();
			}

		}else if(v.getId() == R.id.btnSignupLogin){
			Intent signup = new Intent(v.getContext(), SignUpActivity.class);
			startActivityForResult(signup, CommonConstants.SIGNUP_REQ_ID);
			
		}else if(v.getId() == R.id.btnForgotPw){
			Intent pwReset = new Intent(v.getContext(), PasswordRecoverActivity.class);
			startActivityForResult(pwReset, CommonConstants.PW_RESET_REQ_ID);
		}

	}

}
