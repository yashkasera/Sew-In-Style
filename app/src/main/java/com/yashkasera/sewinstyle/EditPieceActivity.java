/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditPieceActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context=this;
    Cursor cursor=null;
    String per="",person="",perName="",perId="",piece="",pieceId="";
    TextView oName, oPiece, oDescription, odateAdded, cost, oPrice,profit,profitper;
    TextInputLayout sp1,Piece1,description1;
    TextInputEditText sp,Piece,description;
    double P=0.0,CP=0.0,SP=0.0,PP=0.0;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_piece);
        toolbar();
        person = getIntent().getStringExtra("person");
        per = getIntent().getStringExtra("per");
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        piece = getIntent().getStringExtra("piece");
        pieceId = getIntent().getStringExtra("pieceId");
        oName = findViewById(R.id.oName);
        oPiece = findViewById(R.id.oPiece);
        oDescription = findViewById(R.id.oDescription);
        odateAdded = findViewById(R.id.odateAdded);
        cost = findViewById(R.id.cost);
        oPrice = findViewById(R.id.oPrice);
        sp1 = findViewById(R.id.sp1);
        sp = findViewById(R.id.sp);
        profit = findViewById(R.id.profit);
        profitper = findViewById(R.id.profitper);
        Piece = findViewById(R.id.piece);
        Piece1 = findViewById(R.id.piece1);
        description = findViewById(R.id.description);
        description1 = findViewById(R.id.description1);
        cursor = myDbAdapter.getPiece(Integer.parseInt(pieceId));
        if(cursor.getCount()==0){
            Message.message(context,"ERROR#404 - Piece not found!");
            finish();
        }
        else {
            oName.setText(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
            oPiece.setText(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
            oDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
            odateAdded.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED")));
            CP = cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
            cost.setText(CP+"");
            if(cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"))==0){
                findViewById(R.id.p).setVisibility(View.GONE);
                findViewById(R.id.p1).setVisibility(View.GONE);
            }
            else {
                oPrice.setText(cursor.getString(cursor.getColumnIndexOrThrow("PRICE")));
                sp.setText(cursor.getString(cursor.getColumnIndexOrThrow("PRICE")));
                SP = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                P = SP - CP;
                profit.setText(P + "");
                PP = P / CP * 100;
                profitper.setText(PP + "");
            }
            Piece.setText(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
            description.setText(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
            Piece.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0)
                        Piece1.setError("Piece Name cannot be empty");
                    else
                        Piece1.setError(null);
                    flag=1;
                }
            });
            sp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(sp.getText().toString())) {
                        if (sp.getText().toString().equalsIgnoreCase("0")) {
                            sp.setText("0.0");
                            sp.selectAll();
                        } else {
                            SP = Double.parseDouble(sp.getText().toString());
                            P = SP - CP;
                            PP = (P / CP) * 100;
                            PP = Math.rint(PP);
                            profit.setText(String.valueOf(P));
                            profitper.setText(PP + "%");
                        }
                    } else
                        sp.setText("0");
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(sp.getText().toString())) {
                        if (sp.getText().toString().equalsIgnoreCase("0")) {
                            sp.setText("0.0");
                            sp.selectAll();
                        } else {
                            SP = Double.parseDouble(sp.getText().toString());
                            P = SP - CP;
                            PP = (P / CP) * 100;
                            PP = Math.rint(PP);
                            profit.setText(String.valueOf(P));
                            profitper.setText(PP + "%");
                        }
                    } else
                        sp.setText("0");
                    if(!s.toString().equalsIgnoreCase(oPrice.getText().toString()))
                        flag=1;
                }
            });
            description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equalsIgnoreCase(oDescription.getText().toString()))
                        flag=1;
                }
            });
            findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final alertDialog dialog = new alertDialog(context);
                    Button yes = dialog.findViewById(R.id.positive);
                    Button no = dialog.findViewById(R.id.negative);
                    ImageView dismiss = dialog.findViewById(R.id.dismiss);
                    dialog.setTitle("Confirm Cancel", "Are you sure you want to cancel?", "Yes", "No");
                    yes.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            finish();
                            dialog.quitDialog(v);
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.quitDialog(v);
                        }
                    });
                    dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.quitDialog(v);
                        }
                    });
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                }
            });
            findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == 0) {
                        Message.message(context, "No Change Obeserved!");
                        finish();
                    } else {
                        if (cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")) == 0) {
                            if (Piece.getText().length() == 0 || TextUtils.isEmpty(Piece.getText())) {
                                Piece1.setError("Piece Name Cannot be empty");
                            } else {
                                Piece1.setError(null);
                                final alertDialog dialog = new alertDialog(context);
                                Button yes = dialog.findViewById(R.id.positive);
                                Button no = dialog.findViewById(R.id.negative);
                                ImageView dismiss = dialog.findViewById(R.id.dismiss);
                                dialog.setTitle("Confirm Update", "Updating this piece will overwrite all the previous data. Are you due you still want to continue?", "Yes", "No");
                                yes.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick(View v) {
                                        if (myDbAdapter.updatePiece(pieceId, Piece.getText().toString(), description.getText().toString(), "0")) {
                                            Message.message(context, "Piece successfully updated");
                                            finish();
                                            dialog.quitDialog(v);
                                        } else {
                                            Message.message(context, "Piece could not be updated");
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.quitDialog(v);
                                    }
                                });
                                dismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.quitDialog(v);
                                    }
                                });
                                dialog.show();
                                Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            }
                        } else {
                            if (Piece.getText().length() == 0 || TextUtils.isEmpty(Piece.getText())) {
                                Piece1.setError("Piece Name Cannot be empty");
                            } else {
                                Piece1.setError(null);
                                P = CP - Double.parseDouble(sp.getText().toString());
                                final alertDialog dialog = new alertDialog(context);
                                Button yes = dialog.findViewById(R.id.positive);
                                Button no = dialog.findViewById(R.id.negative);
                                ImageView dismiss = dialog.findViewById(R.id.dismiss);
                                dialog.setTitle("Confirm Update", "Updating this piece will overwrite all the previous data. Are you due you still want to continue?", "Yes", "No");
                                yes.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick(View v) {
                                        if (myDbAdapter.updatePiece(pieceId, Piece.getText().toString(), description.getText().toString(), sp.getText().toString()) && myDbAdapter.updateProfit(pieceId, Piece.getText().toString(), sp.getText().toString(), P + "")) {
                                            Message.message(context, "Piece Successfully updated");
                                            finish();
                                            dialog.quitDialog(v);
                                        } else {
                                            Message.message(context, "Piece could not be updated");
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.quitDialog(v);
                                    }
                                });
                                dismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.quitDialog(v);
                                    }
                                });
                                dialog.show();
                                Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            }
                        }
                    }
                }
            });
        }
    }
    void toolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Edit Piece");
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
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_home:
                finish();
                intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }
}