/**
 * 
 */
package com.ucsc.mcs;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author thisara
 *
 */
public class SignUpActivity extends Activity implements OnClickListener {
	
	private Button btnSignup, btnCancel;
	private EditText editTxtUsername, editTxtPassword, editTxtConfirmPassword, editTxtFullname, editTxtEmail;
	private ServiceInvoker serviceInvoker;
	private String imei;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		serviceInvoker = new ServiceInvoker();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		
		editTxtUsername = (EditText)findViewById(R.id.editTxtUsername);
		editTxtPassword = (EditText)findViewById(R.id.editTxtPassword);
		editTxtConfirmPassword = (EditText)findViewById(R.id.editTxtConfirmPassword);
		editTxtFullname = (EditText)findViewById(R.id.editTxtFullname);
		editTxtEmail = (EditText)findViewById(R.id.editTxtEmail);
		
		btnSignup = (Button)findViewById(R.id.btnSignup);
		btnCancel = (Button)findViewById(R.id.btnCancelSignup);
		btnSignup.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		if (v.getId() == R.id.btnSignup) {
			if (editTxtUsername.getText().toString().length() > 0 && editTxtPassword.getText().toString().length() > 0
					&& editTxtConfirmPassword.getText().toString().length() > 0 && editTxtFullname.getText().toString().length() > 0
					&& editTxtEmail.getText().toString().length() > 0) {
				if (editTxtPassword.getText().toString().equals(editTxtConfirmPassword.getText().toString())) {
					boolean isSuccess = serviceInvoker.signup(editTxtUsername.getText().toString(), editTxtPassword.getText().toString(),
							editTxtFullname.getText().toString(), imei, editTxtEmail.getText().toString());
					if (isSuccess) {
						Toast.makeText(SignUpActivity.this, "User added successfully. Login with user information", Toast.LENGTH_LONG).show();
						Intent login = new Intent(v.getContext(), LoginActivity.class);
						startActivityForResult(login, CommonConstants.LOGIN_REQ_ID);
					} else {
						Toast.makeText(SignUpActivity.this, "Username exists! Try other username.", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(SignUpActivity.this, "Password does not match each other!", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(SignUpActivity.this, "Input validation error! All the fields should be not null", Toast.LENGTH_LONG).show();
			}

		} else if (v.getId() == R.id.btnCancelSignup) {
			Intent login = new Intent(v.getContext(), LoginActivity.class);
			startActivityForResult(login, CommonConstants.LOGIN_REQ_ID);
		}

	}

}
