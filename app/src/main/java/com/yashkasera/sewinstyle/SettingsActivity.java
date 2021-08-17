package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SettingsActivity";
    Switch biometrics, passcode, enable_system_pin;
    RelativeLayout pass, sys_pin;
    Toolbar toolbar;
    Context context = this;
    ImageView i1, i2, i3, i4;
    TextView pass_text;
    FloatingActionButton k0, k1, k2, k3, k4, k5, k6, k7, k8, k9, clear, clear_all;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int flag = 0;
    String mode = "", pin1 = "", pin2 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        sharedPreferences = context.getSharedPreferences("auth", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Settings");
        biometrics = findViewById(R.id.biometrics);
        passcode = findViewById(R.id.passcode);
        pass = findViewById(R.id.pass);
        pass_text = findViewById(R.id.pass_text);
        sys_pin = findViewById(R.id.sys_pin);
        sys_pin.setVisibility(View.GONE);
        enable_system_pin = findViewById(R.id.enable_system_pin);
        if (sharedPreferences.getString("mode", "").equalsIgnoreCase("biometrics")) {
            biometrics.setChecked(true);
            passcode.setChecked(false);
            pass.setVisibility(View.GONE);
            sys_pin.setVisibility(View.VISIBLE);
            enable_system_pin.setChecked(sharedPreferences.getBoolean("sys_pin", false));
        }
        else if (sharedPreferences.getString("mode", "").equalsIgnoreCase("pin")) {
            biometrics.setChecked(false);
            passcode.setChecked(true);
            pass.setVisibility(View.VISIBLE);
            sys_pin.setVisibility(View.GONE);
            pin();
        }
        else{
            biometrics.setChecked(false);
            passcode.setChecked(false);
            pass.setVisibility(View.GONE);
            sys_pin.setVisibility(View.GONE);
        }
        passcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pin_update = 0;
                if (isChecked) {
                    pass.setVisibility(View.VISIBLE);
                    flag = 1;
                    pin();
                }
                else {
                    flag = 0;
                    pass.setVisibility(View.GONE);
                }
            }
        });
        biometrics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sys_pin.setVisibility(View.VISIBLE);
                    enable_system_pin.setChecked(sharedPreferences.getBoolean("sys_pin", true));
                    passcode.setChecked(false);
                    flag = 0;
                    pin1 = "";
                    pin2 = "";
                }
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog = new alertDialog(context,"Discard Changes","Any changes you made will be lost. Are you sure you want to continue?","Yes","No");
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
                dialog.dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (biometrics.isChecked()) {
                    editor.putString("mode", "biometrics");
                    editor.putBoolean("sys_pin", enable_system_pin.isChecked());
                    if (editor.commit()) {
                        Message.message(context, "Biometrics enables successfully");
                        finish();
                    } else {
                        Message.message(context, "Biometrics could not be enabled! Please Try Again.");
                    }
                }
                if (pin_update == 1) {
                    editor.putString("mode", "pin");
                    editor.putString("pin", pin1);
                    if (editor.commit()) {
                        Message.message(context, "Pin updated successfully");
                        finish();
                    } else {
                        Message.message(context, "Pin cannot be updated! Please Try Again.");
                    }
                    pin_update = 0;
                }
                if (!biometrics.isChecked() && !passcode.isChecked() && pin_update==0) {
                    editor.putString("mode", "");
                    if (editor.commit()) {
                        Message.message(context, "Changes saved successfully");
                        finish();
                    } else {
                        Message.message(context, "Changes could not be saved! Please Try Again.");
                    }
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_any);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_home:
                finish();
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (pin1.length() <= 4) {
            switch (v.getId()) {
                case R.id.k1:
                    pin1 += "1";
                    break;
                case R.id.k2:
                    pin1 += "2";
                    break;
                case R.id.k3:
                    pin1 += "3";
                    break;
                case R.id.k4:
                    pin1 += "4";
                    break;
                case R.id.k5:
                    pin1 += "5";
                    break;
                case R.id.k6:
                    pin1 += "6";
                    break;
                case R.id.k7:
                    pin1 += "7";
                    break;
                case R.id.k8:
                    pin1 += "8";
                    break;
                case R.id.k9:
                    pin1 += "9";
                    break;
                case R.id.k0:
                    pin1 += "0";
                    break;
            }
        }
        checkPIN();
        Log.d(TAG, "PIN :" + pin1);
        setBubble();
    }

    int pin_update = 0;
    void checkPIN(){
        if (pin1.length() == 4) {
            if (flag == 1) {
                pin2 = pin1;
                pin1 = "";
                flag = 2;
                pass_text.setText("Confirm Pin");
            }
            else if (flag == 2) {
                if (pin1.equalsIgnoreCase(pin2)) {
                    pin_update = 1;
                } else {
                    Message.message(context, "Pins do not match! Please try again");
                    pin1 = "";
                    pin2 = "";
                    flag = 1;
                    pass_text.setText("Enter a 4-digit Pincode");
                }
            }
        }
    }
    void setBubble(){
        if(pin1.length()==0){
            i1.setImageResource(R.drawable.ic_circle_outline);
            i2.setImageResource(R.drawable.ic_circle_outline);
            i3.setImageResource(R.drawable.ic_circle_outline);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin1.length()==1){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_outline);
            i3.setImageResource(R.drawable.ic_circle_outline);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin1.length()==2){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_fill);
            i3.setImageResource(R.drawable.ic_circle_outline);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin1.length()==3){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_fill);
            i3.setImageResource(R.drawable.ic_circle_fill);
            i4.setImageResource(R.drawable.ic_circle_outline);
        }
        else if(pin1.length()==4){
            i1.setImageResource(R.drawable.ic_circle_fill);
            i2.setImageResource(R.drawable.ic_circle_fill);
            i3.setImageResource(R.drawable.ic_circle_fill);
            i4.setImageResource(R.drawable.ic_circle_fill);
        }
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
                if (pin1.length() > 0) {
                    pin1 = pin1.substring(0, (pin1.length() - 1));
                }
                setBubble();
            }
        });
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin1 = "";
                setBubble();
            }
        });
    }
}