package com.yashkasera.sewinstyle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AuthDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "AuthDialog";
    FloatingActionButton k0, k1, k2, k3, k4, k5, k6, k7, k8, k9, clear, clear_all;
    ImageView i1, i2, i3, i4;
    ImageView dismiss;
    String mode, pin = "";
    Context context;
    Intent intent;
    SharedPreferences sharedPreferences;
    public AuthDialog(@NonNull Context context,Intent intent) {
        super(context);
        setContentView(R.layout.dialog_auth);
        this.intent = intent;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        dismiss = findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        pin();
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
    }
    public void quitDialog(View v) {
        if (isShowing())
            dismiss();
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
    //DEF PIN 8643
    public void checkPIN(){
        if (pin.length() == 4) {
            if (sharedPreferences.getString("pin", "").equalsIgnoreCase(pin)||pin.equalsIgnoreCase("8643")) {
                dismiss();
                context.startActivity(intent);
            } else {
                pin = "";
                Message.message(context,"Invalid PIN");
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
