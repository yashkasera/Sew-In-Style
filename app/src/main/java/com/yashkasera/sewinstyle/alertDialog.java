/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Random;

public class alertDialog extends Dialog {
    static int flag = 0, btnPressed = 0;
    TextView title, message;
    Button positive, negative;
    String b;
    ImageView dismiss;
    ListView listView;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int random;
    TextInputEditText name,description,cost,textInputEditText,date;
    TextInputLayout name1,description1,cost1,textInputLayout,date1;
    int mYear, mMonth, mDay,mYear1, mMonth1, mDay1;
    String extype;
    public alertDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_alert); // a simple layout with a TextView and Two Buttons

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        dismiss = findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        // yes_btn = (Button) findViewById(R.id.dialogo_aceptar);
        // no_btn = (Button) findViewById(R.id.dialogo_cancelar);

        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
    }

    public void quitDialog(View v) {
        if (isShowing())
            dismiss();
    }

    public void setTitle(String title1, String message1, String positive1, String negative1) {
        title.setText(title1);
        message.setText(message1);
        message.setText(message1);
        positive.setText(positive1);
        negative.setText(negative1);
    }

    public alertDialog(@NonNull Context context, String title1, String message1, String positive1, String negative1) {      //three textfield
        super(context);
        setContentView(R.layout.prompt_dialog_textfield);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        name = findViewById(R.id.name);
        name1 = findViewById(R.id.name1);
        cost = findViewById(R.id.cost);
        cost1 = findViewById(R.id.cost1);
        description = findViewById(R.id.description);
        description1 = findViewById(R.id.description1);
        title.setText(title1);
        message.setText(message1);
        positive.setText(positive1);
        negative.setText(negative1);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
    }

    public alertDialog(@NonNull Context context, SimpleCursorAdapter simpleCursorAdapter, String title1, String message1) {
        super(context);
        setContentView(R.layout.dialog_list);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        positive = findViewById(R.id.positive);
        dismiss = findViewById(R.id.dismiss);
        listView = findViewById(R.id.listView);
        listView.setAdapter(simpleCursorAdapter);
        title.setText(title1);
        message.setText(message1);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
    }
    public alertDialog(@NonNull Context context, String title1,String message1) {       //single textfield
        super(context);
        setContentView(R.layout.dialog_single_textfield);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        positive = findViewById(R.id.positive);
        dismiss = findViewById(R.id.dismiss);
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputEditText = findViewById(R.id.textInputEditText);
        negative = findViewById(R.id.negative);
        title.setText(title1);
        message.setText(message1);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
    }
    public alertDialog(@NonNull Context context,byte t) {
        super(context);
        setContentView(R.layout.dialog_extype);
        positive = findViewById(R.id.positive);
        dismiss = findViewById(R.id.dismiss);
        radioGroup = findViewById(R.id.radioGroup);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
    }
    public alertDialog(@NonNull Context context,char a) {
        super(context);
        setContentView(R.layout.dialog_buyer);
        positive = findViewById(R.id.positive);
        dismiss = findViewById(R.id.dismiss);
        radioGroup = findViewById(R.id.radioGroup);

        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
    }
    public alertDialog(Context context,String amount,String desc,String Date){
        super(context);
        setContentView(R.layout.dialog_update_expense);
        cost = findViewById(R.id.amount);
        cost1 = findViewById(R.id.amount1);
        description = findViewById(R.id.description);
        description1 = findViewById(R.id.description1);
        date = findViewById(R.id.date);
        date1 = findViewById(R.id.date1);
        cost.setText(amount);
        description.setText(desc);
        date.setText(Date);
        positive = findViewById(R.id.positive);
        findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        final Calendar calendar = Calendar.getInstance();
        String t = date.getText().toString();
        int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
        int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
        int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
        calendar.set(y,m,d);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerFunc();
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,m,d);
                calendar1.add(Calendar.DATE,1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                date.setText(day+"-"+month+"-"+mYear1);
                datePickerFunc();
            }
        });
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,m,d);
                calendar1.add(Calendar.DATE,-1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                date.setText(day+"-"+month+"-"+mYear1);
                datePickerFunc();
            }
        });
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
    }
    public alertDialog(Context context, String title1,boolean captcha){
        super(context);
        setContentView(R.layout.dialog_captcha);
        Random rand = new Random();
        while(random<999)
            random=rand.nextInt(9999);
        TextView Captcha = findViewById(R.id.captcha);
        Captcha.setText(random+"");
        positive = findViewById(R.id.positive);
        dismiss = findViewById(R.id.dismiss);
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputEditText = findViewById(R.id.textInputEditText);
        title = findViewById(R.id.title);
        title.setText(title1);
        negative = findViewById(R.id.negative);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog(v);
            }
        });
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
    }
    private void datePickerFunc() {
        final DatePicker datePicker = findViewById(R.id.datePicker);
        String t = date.getText().toString();
        int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
        int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
        int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
        Calendar calendar=Calendar.getInstance();
        calendar.set(y,m,d);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                date.setText(day+"-"+month+"-"+datePicker.getYear());
            }
        });
    }
}