/**
 * 
 */
package com.ucsc.mcs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ucsc.mcs.constants.CommonConstants;

/**
 * @author thisara
 *
 */
public class LoginActivity extends Activity implements OnClickListener{
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	EditText editTxtUsername, editTxtPassword;
	TextView txtViewLoginError;
	Button btnLogin, btnSignup;

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
		txtViewLoginError = (TextView)findViewById(R.id.txtViewLoingError);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnSignup = (Button)findViewById(R.id.btnSignupLogin);
		btnLogin.setOnClickListener(this);
		btnSignup.setOnClickListener(this);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String username = prefs.getString("username", "");
		String password = prefs.getString("password", "");
		
		editTxtUsername.setText(username);
		editTxtPassword.setText(password);
	}

	public void onClick(View v) {
		
		if (v.getId() == R.id.btnLogin) {
			
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String loginResponse = ServiceInvoker.login(editTxtUsername.getText().toString(), editTxtPassword.getText().toString(), telephonyManager
					.getDeviceId());

			if (loginResponse.equals("true") || loginResponse.equals("false")) {
				boolean loginSuccess = Boolean.valueOf(loginResponse);

				if (loginSuccess) {
					SharedPreferences settings = getSharedPreferences("UserDetails", MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(CommonConstants.USERNAME, editTxtUsername.getText().toString().trim());
					editor.putString(CommonConstants.PASSWORD, editTxtPassword.getText().toString().trim());
					editor.commit();

					Intent home = new Intent(v.getContext(), HomeActivity.class);
					startActivityForResult(home, CommonConstants.HOME_REQ_ID);
				} else {
					txtViewLoginError.setText("Invalid username or password!");
				}
			} else {
				Toast.makeText(LoginActivity.this, loginResponse, Toast.LENGTH_LONG).show();
			}

		}else if(v.getId() == R.id.btnSignupLogin){
			Intent signup = new Intent(v.getContext(), SignUpActivity.class);
			startActivityForResult(signup, CommonConstants.SIGNUP_REQ_ID);
		}

	}

}
