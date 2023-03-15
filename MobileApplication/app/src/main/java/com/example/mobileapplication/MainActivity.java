package com.example.mobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView txtWelcome;
    CardView btnWejdz, btnWyjdz, btnWyloguj;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sessionManager=new SessionManager(this);
        sessionManager.checkLogin();

        txtWelcome = findViewById(R.id.txtWelcome);

        btnWejdz = findViewById(R.id.btnWejdz);
        btnWyjdz = findViewById(R.id.btnWyjdz);
        btnWyloguj = findViewById(R.id.btnWyloguj);

        HashMap<String,String> user=sessionManager.getUserDetail();
        String mImie=user.get(sessionManager.IMIE);
        String mUsername=user.get(sessionManager.USERNAME);

        txtWelcome.setText("Witaj "+mImie+"!");
        btnWejdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WejdzActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnWyjdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WyjdzActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnWyloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog();
            }
        });
    }
    public void alertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        alertDialog.setTitle("Wyloguj");
        alertDialog.setMessage("Czy na pewno chcesz się wylogować?");
        alertDialog.setPositiveButton("Wyloguj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.logout();
            }
        });
        alertDialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {

            alertDialog();

    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        sessionManager.logout();
//        finish();
//    }
}