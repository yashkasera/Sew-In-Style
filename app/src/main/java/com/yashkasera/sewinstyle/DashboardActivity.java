/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_CANCELED;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_HW_NOT_PRESENT;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";
    Cursor cursor;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    String person;
    Context context = this;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        ImageView imageView=findViewById(R.id.imageView);
        sharedPreferences = context.getSharedPreferences("auth", MODE_PRIVATE);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        imageView.startAnimation(animation);
        findViewById(R.id.l1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                person = "Customer";
                cursor = myDbAdapter.getPersons(person);
                if(cursor.getCount()==0){
                    Intent intent = new Intent(context, ErrorActivity.class);
                    intent.putExtra("person", person);
                    intent.putExtra("per","1");
                    intent.putExtra("error", "noPerson");
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(DashboardActivity.this, CusTaiEmbActivity.class);
                    intent.putExtra("per", "1");
                    intent.putExtra("person", person);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.l2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                person = "Embroider";
                cursor = myDbAdapter.getPersons(person);
                if(cursor.getCount()==0){
                    Intent intent = new Intent(context, ErrorActivity.class);
                    intent.putExtra("person", person);
                    intent.putExtra("per","2");
                    intent.putExtra("error", "noPerson");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(DashboardActivity.this, CusTaiEmbActivity.class);
                    intent.putExtra("per", "2");
                    intent.putExtra("person", person);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.l3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                person = "Tailor";
                cursor = myDbAdapter.getPersons(person);
                if(cursor.getCount()==0){
                    Intent intent = new Intent(context, ErrorActivity.class);
                    intent.putExtra("person", person);
                    intent.putExtra("per","3");
                    intent.putExtra("error", "noPerson");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(DashboardActivity.this, CusTaiEmbActivity.class);
                    intent.putExtra("per", "3");
                    intent.putExtra("person", person);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.l4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = myDbAdapter.getPayments("", "all", "");
                if(cursor.getCount()!=0) {
                    Intent intent = new Intent(DashboardActivity.this, ViewPaymentsActivity.class);
                    intent.putExtra("function", "all");
                    startActivity(intent);
                }
                else{
                    Message.message(context,"No Payments Found!");
                }
            }
        });
        findViewById(R.id.l5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor=myDbAdapter.getPieces();
                if (cursor.getCount()!=0) {
                    Intent intent=new Intent(DashboardActivity.this,ViewPiecesActivity.class);
                    intent.putExtra("function","all");
                    startActivity(intent);
                } else {
                    Message.message(context,"No Pieces Found!");
                }
            }
        });

        findViewById(R.id.l6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDbAdapter.getAllExpense().getCount()!=0) {
                    Intent intent=new Intent(DashboardActivity.this,ViewExpensesActivity.class);
                    intent.putExtra("function", "all");
                    startActivity(intent);
                } else {
                    Message.message(context,"No Expenses Found");
                }
            }
        });

        findViewById(R.id.l7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,InventoryActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.l8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,ReportsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("auth", MODE_PRIVATE);
                if(sharedPreferences.getString("mode","").equalsIgnoreCase(""))
                    startActivity(new Intent(context, SettingsActivity.class));
                else if (sharedPreferences.getString("mode", "").equalsIgnoreCase("pin")) {
                    AuthDialog authDialog = new AuthDialog(context, (new Intent(context, SettingsActivity.class)));
                    authDialog.show();
                    Window window = authDialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                } else if (sharedPreferences.getString("mode", "").equalsIgnoreCase("biometrics")) {
                    biometric();
                }
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
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
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
    public void onBackPressed() {
        final alertDialog dialog = new alertDialog(context);
        dialog.setTitle("Confirm Exit","Are you sure you want to leave this app?","Yes","No");
        dialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    protected void onDestroy() {
        super.onDestroy();
        if(myDbAdapter!=null)
            myDbAdapter.close();
        if(cursor!=null)
            cursor.close();
    }

    public void Details(View view) {
        Intent intent = new Intent(context, DetailsActivity.class);
        startActivity(intent);
    }
}
