package com.example.adslle.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText editTextFirstName = (EditText) findViewById(R.id.registerFirstName);
        final EditText editTextLastName = (EditText) findViewById(R.id.registerLastName);
        final EditText editTextEmail = (EditText) findViewById(R.id.registerEmail);
        final EditText editTextPassword = (EditText) findViewById(R.id.registerPassword);
        final EditText editTextPasswordConfirmation = (EditText) findViewById(R.id.registerPasswordConfirmation);
        final TextView textViewLoginHere = (TextView) findViewById(R.id.registerLogin);
        final Button buttonRegister = (Button) findViewById(R.id.register);


        textViewLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String email= editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordConfirmation = editTextPasswordConfirmation.getText().toString();

                Response.Listener<String> stringListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.has("success")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(jsonResponse.get("success").toString())
                                        .setNegativeButton("Ok",null)
                                        .create()
                                        .show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                                    if(jsonResponse.has("firstName")){
                                        editTextFirstName.setError(jsonResponse.getJSONArray("firstName").get(0).toString());
                                    }if(jsonResponse.has("lastName")){
                                        editTextLastName.setError(jsonResponse.getJSONArray("lastName").get(0).toString());
                                    }if(jsonResponse.has("email")){
                                        editTextEmail.setError(jsonResponse.getJSONArray("email").get(0).toString());
                                    }if(jsonResponse.has("password")){
                                        String passwordResult="";
                                        for (int i=0;i<jsonResponse.getJSONArray("password").length();i++){
                                            passwordResult+=jsonResponse.getJSONArray("password").get(0).toString()+"\n";
                                        }
                                        editTextPassword.setError(passwordResult);
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

                progress = ProgressDialog.show(RegisterActivity.this, "Register process",
                        "wait for response", true);

                RegisterRequest registerRequest=new RegisterRequest(firstName,lastName,email,password,passwordConfirmation,stringListener,errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }

    public void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(msg + " ")
                .setNegativeButton("Ok", null)
                .create()
                .show();
    }
}
