package com.example.adslle.test.Requests;

import com.android.volley.Response;

import java.util.Map;

/**
 * Created by ADSLLE on 27/07/2016.
 */
public class RegisterRequest extends Request {


    public RegisterRequest(String firstName, String lastName, String email, String password, String passwordConfirmation, Response.Listener<String> listener,Response.ErrorListener errorListener){
        super(Method.POST,REQUEST_URL +"register",listener,errorListener);
        params.put("firstName",firstName);
        params.put("lastName",lastName);
        params.put("email",email);
        params.put("password",password);
        params.put("password_confirmation",passwordConfirmation);
    }

}
