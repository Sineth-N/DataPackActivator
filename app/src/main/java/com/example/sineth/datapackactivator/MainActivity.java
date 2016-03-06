package com.example.sineth.datapackactivator;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sineth.datapackactivator.DatabaseAccess.DBHelper;
import com.example.sineth.datapackactivator.DatabaseAccess.SMSWriterSQLite;
import com.example.sineth.datapackactivator.ScratchCard.ScratchCardActivity;
import com.melnykov.fab.FloatingActionButton;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private RadioGroup packageSelector;
    private RadioButton radioButtond3;
    private RadioButton radioButtond24;
    private RadioButton radioButtond89;
    private com.melnykov.fab.FloatingActionButton buttonRecharge;
    private com.melnykov.fab.FloatingActionButton wifi;
    private com.melnykov.fab.FloatingActionButton button;
    private android.support.v7.app.AlertDialog.Builder alert;
    private Toolbar toolbar;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        RelativeLayout view = (RelativeLayout) findViewById(R.id.relativemain);
//        Snackbar
//                .make(view, R.string.snackbar_text, Snackbar.LENGTH_LONG)
//                .setAction(R.string.snackbar_action, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("TAG","success");
//                    }
//                })
//                .show();
        radioButtonSelectedClickListener();
        buttonRecharge = (FloatingActionButton) findViewById(R.id.rechargeButton);
        wifi = (FloatingActionButton) findViewById(R.id.wifi);

        com.melnykov.fab.FloatingActionButton buttonData = (FloatingActionButton) findViewById(R.id.checkBalanceButton);
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = Uri.encode("#");
                String ussd = "*100" + s;
                startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)), 1);
            }
        });

        buttonRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//                final EditText edittext = new EditText(MainActivity.this);
//                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
//                edittext.getBackground().setColorFilter(getResources().getColor(R.color.accent_material_light), PorterDuff.Mode.SRC_ATOP);
//                alert.setMessage("Enter The Card PIN");
//                alert.setTitle("Instant Recharge");
//                alert.setView(edittext);
//                alert.setPositiveButton("Recharge NOW", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        String EditTextValue = edittext.getText().toString();
//                        if (ValidatePIN(EditTextValue)) {
//                            new USSDCall().makeUSSDCall(MainActivity.this, EditTextValue);
//                        }
//                    }
//                });
//
//                alert.setNegativeButton("Later", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        String EditTextValue = edittext.getText().toString();
//                        if (ValidatePIN(EditTextValue)) {
//                            SMSWriterSQLite smsWriterSQLite = new SMSWriterSQLite(MainActivity.this);
//                            smsWriterSQLite.writeToDatabase(EditTextValue, 50);
//                            Intent intent = new Intent(MainActivity.this, ScratchCardActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//                });
//
//                alert.show();
                showInputDialog();
            }
        });
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ApManager.isApOn(MainActivity.this)) {
                    alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Hotspot Alert!");
                    alert.setMessage("hotspot is activated");
                    alert.setNegativeButton("OK", null);
                    alert.setCancelable(true);
                    alert.show();

                } else {
                    alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Hotspot Alert!");
                    alert.setMessage("hotspot is turned off !!");
                    alert.setNegativeButton("OK", null);
                    alert.setCancelable(true);
                    alert.show();
                }
                ApManager.configApState(MainActivity.this);
            }
        });
    }

    public void showInputDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.EnterPIN)
                .content(R.string.input_content_custom_invalidation)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .inputRange(14, 14)
                .positiveText(R.string.send)
                .neutralText(R.string.save)
                .input(R.string.input_hint, R.string.input_hint_empty, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        new USSDCall().makeUSSDCall(MainActivity.this, input.toString());
                    }

                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        final String input = materialDialog.getInputEditText().getText().toString();
                        if (input.length() == 14) {

                            new MaterialDialog.Builder(MainActivity.this)
                                    .title(R.string.recharge_value_title)
                                    .items(R.array.recharge_values)
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                            Log.d("TAG",""+i);
                                            if (i == 0) {
                                                value = 50;
                                            } else if (i==1) {
                                                value = 100;
                                            }
                                            final SMSWriterSQLite smsWriterSQLite = new SMSWriterSQLite(MainActivity.this);
                                            smsWriterSQLite.writeToDatabase(input, value);
                                            Intent intent = new Intent(MainActivity.this, ScratchCardActivity.class);
                                            startActivity(intent);
                                        }

                                    }).show();

                        } else {
                            new MaterialDialog.Builder(MainActivity.this)
                                    .title(R.string.title_invalid_length)
                                    .content(R.string.content_invalid_length)
                                    .positiveText(R.string.ok)
                                    .negativeText(R.string.dismiss)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                            showInputDialog();
                                        }
                                    }).show();


                        }
                    }

                })
                .show();
    }

    private boolean ValidatePIN(String textValue) {
        if (textValue.length() == 14) {
            return true;
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Invalid PIN !");
            alert.setMessage("The length of the PIN you entered is wrong please check again and retry.");
            alert.setNegativeButton("OK", null);
            alert.setCancelable(true);
            alert.show();
        }
        return false;
    }

    private void radioButtonSelectedClickListener() {
        packageSelector = (RadioGroup) findViewById(R.id.radiogrp);
        radioButtond3 = (RadioButton) findViewById(R.id.radiod3);
        radioButtond24 = (RadioButton) findViewById(R.id.radiod24);
        radioButtond89 = (RadioButton) findViewById(R.id.radiod89);
        button = (FloatingActionButton) findViewById(R.id.FAB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int selectedID = packageSelector.getCheckedRadioButtonId();
                    if (selectedID == radioButtond3.getId()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("7678", null, "D3", null, null);
                        alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Message Sent !");
                        alert.setMessage("D-3 Data Package Request Sent..");
                        alert.setNegativeButton("OK", null);
                        alert.setCancelable(true);
                        alert.show();
                    } else if (selectedID == radioButtond24.getId()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("7678", null, "D24", null, null);
                        alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Message Sent !");
                        alert.setMessage("D-24 Data Package Request Sent..");
                        alert.setNegativeButton("OK", null);
                        alert.setCancelable(true);
                        alert.show();

                    } else if (selectedID == radioButtond89.getId()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("7678", null, "D89", null, null);
                        alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Message Sent !");
                        alert.setMessage("D-89 Data Package Request Sent..");
                        alert.setNegativeButton("OK", null);
                        alert.setCancelable(true);
                        alert.show();
                    } else {
                        alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("No Package Selected !");
                        alert.setMessage("Please select one of the available packages");
                        alert.setNegativeButton("OK", null);
                        alert.setCancelable(true);
                        alert.show();
                    }
                } catch (Exception e) {
                    alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("No Package Selected !");
                    alert.setMessage("Please select one of the available packages");
                    alert.setNegativeButton("OK", null);
                    alert.setCancelable(true);
                    alert.show();

                }
                packageSelector.clearCheck();

            }
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
