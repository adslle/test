package com.example.adslle.test.Requests;

import com.android.volley.Response;
import com.example.adslle.test.MyApplication;
import com.example.adslle.test.SharedPreferencesManager;

/**
 * Created by ADSLLE on 27/07/2016.
 */
public class BooksRequest extends Request {
    public BooksRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET,REQUEST_URL +"books/all",listener,errorListener);
        headers.put("Authorization","Bearer "+ SharedPreferencesManager.getSomeStringValue(MyApplication.getAppContext(),"token"));
    }
}
