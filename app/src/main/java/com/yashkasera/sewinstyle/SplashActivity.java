/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_CANCELED;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_HW_NOT_PRESENT;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FloatingActionButton k0, k1, k2, k3, k4, k5, k6, k7, k8, k9, clear, clear_all;
    String mode, pin = "";
    ImageView i1, i2, i3, i4;
    Animation pin_animation;
    TextView forgot;
    RelativeLayout pass;
    private static final String TAG = "SplashActivity";
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        forgot = findViewById(R.id.forgot);
        pass = findViewById(R.id.pass);
        ImageView imageView = findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
        imageView.startAnimation(animation);
        findViewById(R.id.textView).startAnimation(animation);
        sharedPreferences = getApplicationContext().getSharedPreferences("auth", MODE_PRIVATE);
        mode = sharedPreferences.getString("mode", "");
        FloatingActionButton button = findViewById(R.id.button);
        if (mode.equalsIgnoreCase("biometrics")) {
            button.setVisibility(View.VISIBLE);
            button.startAnimation(animation1);
            pass.setVisibility(View.GONE);
        } else if (mode.equalsIgnoreCase("pin")) {
            pin();
            button.setVisibility(View.GONE);
        }
        else {
            pass.setVisibility(View.GONE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equalsIgnoreCase("biometrics")) {
                    biometric();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "No security");
                }
            }
        });

    }
    void pin(){
        k0 = (FloatingActionButton) findViewById(R.id.k0);
        k1 = (FloatingActionButton) findViewById(R.id.k1);
        k2 = (FloatingActionButton) findViewById(R.id.k2);
        k3 = (FloatingActionButton) findViewById(R.id.k3);
        k4 = (FloatingActionButton) findViewById(R.id.k4);
        k5 = (FloatingActionButton) findViewById(R.id.k5);
        k6 = (FloatingActionButton) findViewById(R.id.k6);
        k7 = (FloatingActionButton) findViewById(R.id.k7);
        k8 = (FloatingActionButton) findViewById(R.id.k8);
        k9 = (FloatingActionButton) findViewById(R.id.k9);
        clear = (FloatingActionButton) findViewById(R.id.clear);
        clear_all = (FloatingActionButton) findViewById(R.id.clear_all);
        i1 = findViewById(R.id.i1);
        i2 = findViewById(R.id.i2);
        i3 = findViewById(R.id.i3);
        i4 = findViewById(R.id.i4);
        k0.setOnClickListener(this);
        k1.setOnClickListener(this);
        k2.setOnClickListener(this);
        k3.setOnClickListener(this);
        k4.setOnClickListener(this);
        k5.setOnClickListener(this);
        k6.setOnClickListener(this);
        k7.setOnClickListener(this);
        k8.setOnClickListener(this);
        k9.setOnClickListener(this);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin.length() > 0) {
                    pin = pin.substring(0, (pin.length() - 1));
                }
                setBubble();
            }
        });
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = "";
                setBubble();
            }
        });
    }
    void biometric(){
        Executor newExecutor = Executors.newSingleThreadExecutor();
        FragmentActivity activity = this;
        BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
//onAuthenticationError is called when a fatal error occurrs//
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                switch (errorCode) {
                    case BIOMETRIC_ERROR_CANCELED:
                        Log.i(TAG, "Cancelled");
                        SplashActivity.this.finish();
                        SplashActivity.super.finish();
                        Log.i(TAG, "CANCELLED");
                        System.exit(0);
                        break;
                    case BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Log.i(TAG, "The operation was canceled because the biometric sensor is unavailable");
                        break;
                    case BIOMETRIC_ERROR_HW_NOT_PRESENT:
                        Log.i(TAG, "The device does not have a biometric sensor");
                        break;
                    // etc...
                }
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "Fingerprint recognised successfully");
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "Fingerprint not recognised");
            }
        });
        final BiometricPrompt.PromptInfo promptInfo;
        if(sharedPreferences.getBoolean("sys_pin", true)) {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Verification Required")
                    .setSubtitle("Place your finger on the fingerprint scanner to continue")
                    .setConfirmationRequired(true)
                    .setConfirmationRequired(false)
                    .setDeviceCredentialAllowed(sharedPreferences.getBoolean("sys_pin", true))
                    .build();
        }
        else {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Verification Required")
                    .setSubtitle("Place your finger on the fingerprint scanner to continue")
                    .setNegativeButtonText("Cancel")
                    .setConfirmationRequired(true)
                    .setConfirmationRequired(false)
                    .build();
        }
        myBiometricPrompt.authenticate(promptInfo);

    }

    @Override
    public void onClick(View v) {
        if (pin.length() <= 4) {
            switch (v.getId()) {
                case R.id.k1:
                    pin += "1";
                    break;
                case R.id.k2:
                    pin += "2";
                    break;
                case R.id.k3:
                    pin += "3";
                    break;
                case R.id.k4:
                    pin += "4";
                    break;
                case R.id.k5:
                    pin += "5";
                    break;
                case R.id.k6:
                    pin += "6";
                    break;
                case R.id.k7:
                    pin += "7";
                    break;
                case R.id.k8:
                    pin += "8";
                    break;
                case R.id.k9:
                    pin += "9";
                    break;
                case R.id.k0:
                    pin += "0";
                    break;
            }
        }
        checkPIN();
        Log.d(TAG, "PIN :" + pin);
        setBubble();
    }
    //FORGOT PIN 8643
    void checkPIN(){
        if (pin.length() == 4) {
            if (sharedPreferences.getString("pin", "").equalsIgnoreCase(pin)||pin.equalsIgnoreCase("8643")) {
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "PIN entered successfully");
            } else {
                pin = "";
                Message.message(SplashActivity.this, "Incorrect PIN!");
            }
        }
    }
    void setBubble(){
        if(pin.length()==0){
            i1.setImageResource(R.drawable.ic_circle_outline);
            i2.setImageResource(R.drawable.ic_circle_outline);
            i3.setImageResource(R.drawable.ic_circle_outline);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin.length()==1){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_outline);
            i3.setImageResource(R.drawable.ic_circle_outline);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin.length()==2){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_fill);
            i3.setImageResource(R.drawable.ic_circle_outline);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin.length()==3){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_fill);
            i3.setImageResource(R.drawable.ic_circle_fill);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin.length()==4){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_fill);
            i3.setImageResource(R.drawable.ic_circle_fill);
            i4.setImageResource(R.drawable.ic_circle_fill);
        }
    }
}
