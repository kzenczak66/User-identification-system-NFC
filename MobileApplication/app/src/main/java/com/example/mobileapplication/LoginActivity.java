package com.example.mobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {
    private TextView txtLogin, txtPasswd;
    private TextView txtIP;
    private CardView btnLogin;
    private ProgressBar progressBar;
    private static String ip="";
    //private static final String URL_LOGIN="http://192.168.1.4/pracainz/login.php";
    private static String URL_LOGIN="";
    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        sessionManager=new SessionManager(this);
        txtLogin = findViewById(R.id.txtLogin);
        txtPasswd = findViewById(R.id.txtPasswd);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar=findViewById(R.id.progressBar);
        txtIP=findViewById(R.id.txtIP);
        txtIP.setText("192.168.1.5");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mUsername=String.valueOf(txtLogin.getText());
                String mPassword=String.valueOf(txtPasswd.getText());
                ip=String.valueOf(txtIP.getText());
                ip=ip.trim();
                URL_LOGIN="http://"+ip+"/pracainz/login.php";

                if(!mUsername.equals("")||!mPassword.equals(""))
                {
                    Login(mUsername,mPassword);
                }
                else
                {
                    txtLogin.setError("Wprowadź login!");
                    txtPasswd.setError("Wprowadź hasło!");
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        finish();

    }


    private void Login(String username, String password)
    {
       progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                            String success=jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("login");

                            if(success.equals("1"))
                            {
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    String imie=object.getString("imie").trim();
                                    String username=object.getString("username").trim();

                                    sessionManager.createSession(imie,username);

                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this,"Logowanie pomyślne! \n Witaj "+imie,Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this,"Błąd! "+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,"Błąd! "+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}