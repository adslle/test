package com.example.adslle.test.Requests;

import android.util.Log;

import com.android.volley.Response;

/**
 * Created by ADSLLE on 27/07/2016.
 */
public class LoginRequest extends Request {


    public LoginRequest(String email, String password, Response.Listener<String> listener,Response.ErrorListener stringErrorListener){
        super(Method.POST,REQUEST_URL +"login",listener,stringErrorListener);
        params.put("email",email+"");
        params.put("password",password);
    }

}
