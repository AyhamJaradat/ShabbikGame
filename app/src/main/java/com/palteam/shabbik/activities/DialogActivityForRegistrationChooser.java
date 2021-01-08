package com.palteam.shabbik.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.IConstants.RegistrationType;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.palteam.shabbik.R;

public class DialogActivityForRegistrationChooser extends Activity implements
        OnClickListener {

    private final String TAG = DialogActivityForRegistrationChooser.class
            .getSimpleName();

    private ImageView loginWithFacebookImageView, createNewAccountImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_dialog_for_registration_chooser);
//        setContentView(R.layout.activity_dialog_activity_for_registration_chooser);

        removeActivityBackground();

        init();

        configureDialogLayout();
    }
    private void removeActivityBackground() {

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    private void configureDialogLayout() {

        int registrationTypeIndex = getIntent().getExtras().getInt(
                IConstants.REGISTRATION_TYPE_EXTRA);
        RegistrationType registrationType = IConstants.RegistrationType
                .values()[registrationTypeIndex];

        switch (registrationType) {

            case FACEBOOK:
                createNewAccountImageView.setVisibility(View.GONE);
                break;

            case MANUAL:
                break;

            default:
                break;

        }

    }

    private void init() {

        loginWithFacebookImageView = (ImageView) findViewById(R.id.imageViewForDialogFacebookLogin);
        loginWithFacebookImageView.setOnClickListener(this);

        createNewAccountImageView = (ImageView) findViewById(R.id.imageViewForDialogCreateNewAccount);
        createNewAccountImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageViewForDialogFacebookLogin:

//                Intent toFacebookLoginActivity = new Intent(
//                        getApplicationContext(), FacebookLoginActivity.class);
//                startActivity(toFacebookLoginActivity);
                finish();

                break;

            case R.id.imageViewForDialogCreateNewAccount:

                Intent toSingUpActivity = new Intent(getApplicationContext(),
                        SignUpActivity.class);
                startActivity(toSingUpActivity);
                finish();

                break;

            default:
                break;
        }
    }


}
