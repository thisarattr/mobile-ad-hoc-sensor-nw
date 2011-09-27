/**
 * 
 */
package com.ucsc.mcs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
	}

	public void onClick(View v) {
		
		if (v.getId() == R.id.btnLogin) {
			
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String loginOut = ServiceInvoker.login(editTxtUsername.getText().toString(), editTxtPassword.getText().toString(), telephonyManager
					.getDeviceId());

			if (loginOut.equals("true") || loginOut.equals("false")) {
				boolean isSuccess = Boolean.valueOf(loginOut);

				// TODO isLogin must be there...
				if (true) {
					CommonConstants.USERNAME = editTxtUsername.getText().toString();
					Intent home = new Intent(v.getContext(), HomeActivity.class);
					startActivityForResult(home, CommonConstants.HOME_REQ_ID);
				} else {
					txtViewLoginError.setText("Invalid username or password!");
				}
			} else {
				Toast.makeText(LoginActivity.this, loginOut, Toast.LENGTH_LONG).show();
			}

		}else if(v.getId() == R.id.btnSignupLogin){
			Intent signup = new Intent(v.getContext(), SignUpActivity.class);
			startActivityForResult(signup, CommonConstants.SIGNUP_REQ_ID);
		}

	}

}
