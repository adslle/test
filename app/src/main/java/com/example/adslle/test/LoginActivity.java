package com.example.adslle.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.adslle.test.Requests.LoginRequest;
import com.example.adslle.test.Requests.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedPreferencesManager.isAuthenticated()){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            LoginActivity.this.startActivity(intent);
            finish();
        }
        final EditText editTextEmail = (EditText) findViewById(R.id.loginEmail);
        final EditText editTextPassword = (EditText) findViewById(R.id.loginPassword);
        final TextView textViewRegisterHere = (TextView) findViewById(R.id.loginRegister);
        final Button buttonLogin = (Button) findViewById(R.id.login);

        textViewRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                Response.Listener<String> stringListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        Log.e("Error response", response + " ");
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("token")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("success")
                                        .setNegativeButton("Ok", null)
                                        .create()
                                        .show();

                                SharedPreferencesManager.setSomeStringValue(LoginActivity.this,"token",jsonResponse.get("token").toString());
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            dialog("something wrong try again");
                        }

                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        JSONObject jsonResponse = null;
                        try {
                            String res = new String(networkResponse.data,
                                    HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                            jsonResponse = new JSONObject(res);
                            if (networkResponse != null) {
                                if (networkResponse.statusCode == 422) {
                                    if (jsonResponse.has("password")) {
                                        editTextPassword.setError(jsonResponse.getJSONArray("password").get(0).toString());
                                        editTextPassword.requestFocus();
                                    }
                                    if (jsonResponse.has("email")) {
                                        editTextEmail.setError(jsonResponse.getJSONArray("email").get(0).toString());
                                        editTextEmail.requestFocus();
                                    }
                                }else if(networkResponse.statusCode == 401){
                                    dialog(jsonResponse.getString("error"));
                                }else{
                                    dialog("something wrong try again");
                                }
                            }
                        } catch (JSONException e) {
                            dialog("something wrong try again");
                        } catch (UnsupportedEncodingException e) {
                            dialog("something wrong try again");
                        }

                    }
                };


                progress = ProgressDialog.show(LoginActivity.this, "Login process",
                        "wait for response", true);

                LoginRequest loginRequest = new LoginRequest(email, password, stringListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }


    public void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(msg + " ")
                .setNegativeButton("Ok", null)
                .create()
                .show();
    }
}
