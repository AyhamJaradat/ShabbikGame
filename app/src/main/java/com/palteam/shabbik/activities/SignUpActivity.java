package com.palteam.shabbik.activities;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.palteam.shabbik.R;

import com.palteam.shabbik.beans.User;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.RegistrationManager;
import com.palteam.shabbik.webservice.WebService;

public class SignUpActivity extends Activity implements
        OnClickListener, IConstants, RegistrationManager.OnRegistrationDoneListener {
    private String TAG = this.getClass().getSimpleName();

    private Context context;

    private EditText firstNameEditText, lastNameEditText, emailEditText;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        AppUtils.logHeap(TAG);

        setContentView(R.layout.activity_sign_up);

        initializations();
    }
    /**
     * Initialize activity components.
     */
    private void initializations() {
        context = getApplicationContext();

        ImageView signUpButton = (ImageView) findViewById(R.id.signUpImageView);
        signUpButton.setOnClickListener(this);

        firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);

        // Set font on title text-view.
        Typeface editTextTypeface = Typeface.createFromAsset(
                context.getAssets(), IConstants.Fonts.ROBOTO_CONDENSED_REGULAR);

        firstNameEditText.setTypeface(editTextTypeface);
        lastNameEditText.setTypeface(editTextTypeface);
        emailEditText.setTypeface(editTextTypeface);

        user = new User();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.signUpImageView:
                validateInformation();
                break;
            default:
                break;

        }
    }

    private void validateInformation() {

        // check if there is Internet connection
        boolean isConnected = WebService.isConnectedToWeb(context);

        if (!isConnected) {

            // no Internet connection
            Toast.makeText(context,
                    getResources().getString(R.string.internet_failer),
                    Toast.LENGTH_LONG).show();

        } else {

            // get String values
            String fName = firstNameEditText.getText().toString().trim();
            String lName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            // validate
            if (fName.length() == 0 || lName.length() == 0) {

                Toast.makeText(this, getString(R.string.warning_data_missing),
                        Toast.LENGTH_SHORT).show();

            } else {

                // Fill user data.
                user.setUserFirstName(fName);
                user.setUserLastName(lName);

                if (email.length() != 0) {
                    user.setUserEmail(email);
                }

                new RegistrationManager(this, this).register(user);

            }
        }

    }

    @Override
    public void onRegistrationSucceed() {

        Toast.makeText(this,
                getString(R.string.register_successfully),
                Toast.LENGTH_SHORT).show();

        finish();

    }

    @Override
    public void onRegistrationFailed() {

        Toast.makeText(this,
                getString(R.string.failer_message),
                Toast.LENGTH_SHORT).show();

        finish();

    }

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) SignUpActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(SignUpActivity.this
                        .getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
