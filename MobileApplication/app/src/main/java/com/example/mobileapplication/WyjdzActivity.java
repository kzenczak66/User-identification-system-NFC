package com.example.mobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.*;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;


import java.util.UUID;


public class WyjdzActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{


    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    SessionManager sessionManager;
    TextView txtTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wejdz);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sessionManager=new SessionManager(this);
        txtTest = findViewById(R.id.txtIdUrzadzenia);
        String id = id(this);
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        id=id.replace("-","");
        id=id.substring(0,30);
        txtTest.setText("e"+id);
        if (mNfcAdapter == null) {

            txtTest.setText("Nie znaleziono NFC!");


        } else if (mNfcAdapter.isEnabled()) {

            mNfcAdapter.setNdefPushMessageCallback(this,this);


        } else {
            showAlertDialog();
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = txtTest.getText().toString();
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }


    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private void showAlertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        alertDialog.setTitle("Włączenie nfc");
        alertDialog.setMessage("Musisz włączyć NFC aby kontynuować!");
        alertDialog.setPositiveButton("włącz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
                    Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        alertDialog.setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

}