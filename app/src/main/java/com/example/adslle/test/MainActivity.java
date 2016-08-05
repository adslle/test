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
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.adslle.test.Adapters.BookCustomAdapter;
import com.example.adslle.test.Requests.BooksRequest;
import com.example.adslle.test.Requests.LoginRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!SharedPreferencesManager.isAuthenticated()) {
            finish();
        }

        final Button buttonLogout = (Button) findViewById(R.id.logout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.setSomeStringValue(MainActivity.this, "token", null);
                Intent registerIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("books")) {
                        JSONObject jsonBooks = jsonResponse.getJSONObject("books");
                        JSONArray jsonArray = jsonBooks.getJSONArray("data");
                        ListView listView =(ListView) findViewById(R.id.listViewBooks);
                        listView.setAdapter(new BookCustomAdapter(MainActivity.this, jsonArray));
                        //dialog(jsonArray.toString());
                    }
                } catch (JSONException e) {
                    dialog("JSONException");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                JSONObject jsonResponse = null;
                if (networkResponse != null) {
                    if (networkResponse.statusCode == 400 || networkResponse.statusCode == 401) {
                        dialog("token problem");
                    }else{
                        /*try {
                            String res = new String(networkResponse.data,
                                    HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                            dialog("status code: "+res);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }*/
                        dialog("status code: "+networkResponse.statusCode);
                    }
                }else{
                    dialog(error.getMessage());
                }

            }
        };


        progress = ProgressDialog.show(MainActivity.this, "Load Books",
                "wait for response", true);

        BooksRequest booksRequest = new BooksRequest(stringListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getAppContext());
        queue.add(booksRequest);
    }

    public void dialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(msg + " ")
                .setNegativeButton("Ok", null)
                .create()
                .show();
    }
}
