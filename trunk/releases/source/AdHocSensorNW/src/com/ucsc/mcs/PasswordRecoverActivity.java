/**
 * 
 */
package com.ucsc.mcs;

import com.ucsc.mcs.constants.CommonConstants;

import android.app.Activity;
import android.content.Context;
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
public class PasswordRecoverActivity extends Activity implements OnClickListener{
	
	private String imei;
	private ServiceInvoker serviceInvoker;
	
	private EditText edtTxtUsername, edtTxtEmail;
	private Button btnCancel, btnReset;
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_recover);
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		
		serviceInvoker = new ServiceInvoker();
		
		edtTxtUsername = (EditText) findViewById(R.id.editTxtUsernamePwReset);
		edtTxtEmail = (EditText) findViewById(R.id.editTxtEmailPwReset);
		
		btnCancel = (Button) findViewById(R.id.btnCancelPwReset);
		btnReset = (Button) findViewById(R.id.btnPwReset);
		btnCancel.setOnClickListener(this);
		btnReset.setOnClickListener(this);
	}


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		if (v.getId() == R.id.btnCancelPwReset) {
			finish();
		} else if (v.getId() == R.id.btnPwReset) {
			
			String username = edtTxtUsername.getText().toString().trim();
			String email = edtTxtEmail.getText().toString().trim();
			
			if (email.length() > 0 || username.length() > 0) {
				boolean isReset = serviceInvoker.passwordRecover(username, email, imei);
				
				if (isReset) {
					Toast.makeText(PasswordRecoverActivity.this, "Your password has been resetted and send.", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(PasswordRecoverActivity.this, "Cannot find your account.", Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(PasswordRecoverActivity.this, "Please enter one of Username and Email address to reset profile password.",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	
	

}
