/**
 * 
 */
package com.ucsc.mcs;

import java.util.Map;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author thisara
 *
 */
public class UpdateProfileActivity extends Activity implements OnCheckedChangeListener, OnClickListener{
	
	private static final String TAG = UpdateProfileActivity.class.getSimpleName();
	
	private EditText editTxtUsename, editTxtOldPassword, editTxtNewPassword, editTxtConfirmPw, editTxtEmail, editTxtFullname, editTxtRank, editTxtDate; 
	private Button btnUpdate, btnCancel;
	private CheckBox ChkBxChangePw;
	private ServiceInvoker serviceInvoker;
	private String username, password, imei;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_profile);
		
		serviceInvoker = new ServiceInvoker();
		//Access application session preferences
		SharedPreferences settings = getSharedPreferences(CommonConstants.PREF_USER_DETAILS, MODE_PRIVATE);
		username = settings.getString(CommonConstants.USERNAME, "");
		password = settings.getString(CommonConstants.PASSWORD, "");
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();

		editTxtUsename = (EditText) findViewById(R.id.editTxtUsernameUpdate);
		editTxtOldPassword = (EditText) findViewById(R.id.editTxtOldPasswordUpdate);
		editTxtNewPassword = (EditText) findViewById(R.id.editTxtNewPasswordUpdate);
		editTxtConfirmPw = (EditText) findViewById(R.id.editTxtConfirmNewPasswordUpdate);
		editTxtEmail = (EditText) findViewById(R.id.editTxtEmailUpdate);
		editTxtFullname = (EditText) findViewById(R.id.editTxtFullnameUpdate);
		editTxtRank = (EditText) findViewById(R.id.editTxtRankUpdate);
		editTxtDate = (EditText) findViewById(R.id.editTxtDatelUpdate); 
		
		ChkBxChangePw = (CheckBox) findViewById(R.id.chkBxChangePw);
		ChkBxChangePw.setOnCheckedChangeListener(this);
		
		btnUpdate = (Button) findViewById(R.id.btnUpdateUser);
		btnCancel = (Button) findViewById(R.id.btnCancelUpdateUser);
		btnUpdate.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		try {
			Map<String, String> userInfo = serviceInvoker.getUser(username);
			
			if(!userInfo.isEmpty()){
				editTxtUsename.setText(userInfo.get(CommonConstants.GETUSER_USERNAME));
				editTxtEmail.setText(userInfo.get(CommonConstants.GETUSER_EMAIL));
				editTxtFullname.setText(userInfo.get(CommonConstants.GETUSER_FULLNAME));
				editTxtRank.setText(userInfo.get(CommonConstants.GETUSER_RANK));
				editTxtDate.setText(userInfo.get(CommonConstants.GETUSER_TIMESTAMP));
			}
		} catch (RuntimeException e) {
			Toast.makeText(UpdateProfileActivity.this, "Error Retrieving User information. " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	

	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (buttonView.getId() == R.id.chkBxChangePw) {
			if (isChecked) {
				editTxtNewPassword.setEnabled(true);
				editTxtConfirmPw.setEnabled(true);
			} else {
				editTxtNewPassword.setEnabled(false);
				editTxtConfirmPw.setEnabled(false);
			}
		}

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		if (v.getId() == R.id.btnUpdateUser) {

			String oldPassword = editTxtOldPassword.getText().toString().trim();
			String newPassword = editTxtNewPassword.getText().toString().trim();
			String newConfirmPassword = editTxtConfirmPw.getText().toString().trim();

			if (oldPassword.length() > 0) {

				// No need to change the existing password.
				if (!ChkBxChangePw.isChecked()) {
					newPassword = "";
				} else if (!(newPassword.length()>0 && newConfirmPassword.length()>0 && newPassword.equals(newConfirmPassword))) {
					Toast.makeText(UpdateProfileActivity.this, "New Passwoed does not match!", Toast.LENGTH_LONG).show();
					return;
				}

				Boolean isUpdated = serviceInvoker.editUser(username, oldPassword, newPassword, imei, editTxtEmail.getText().toString(),
						editTxtFullname.getText().toString());

				if (isUpdated) {
					Toast.makeText(UpdateProfileActivity.this, "User Profile Successfully Updated.", Toast.LENGTH_LONG).show();
					Intent home = new Intent(v.getContext(), HomeActivity.class);
					startActivityForResult(home, CommonConstants.HOME_REQ_ID);
				} else {
					Toast.makeText(UpdateProfileActivity.this, "Incorrect Password, Update failed! ", Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(UpdateProfileActivity.this, "Please enter your existing password.", Toast.LENGTH_LONG).show();
			}

		} else if (v.getId() == R.id.btnCancelUpdateUser) {
			Intent home = new Intent(v.getContext(), HomeActivity.class);
			startActivityForResult(home, CommonConstants.HOME_REQ_ID);
		}

	}

	
}
