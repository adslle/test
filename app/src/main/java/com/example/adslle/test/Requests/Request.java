package com.example.adslle.test.Requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADSLLE on 27/07/2016.
 */
public class Request extends StringRequest{
    public static final String REQUEST_URL = "http://192.168.101.1/ajax/";
    public static final String BASE_URL = "http://192.168.101.1/";
    protected Map<String,String> params;
    protected Map<String,String> headers;

    public Request(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        params = new HashMap<>();
        headers = new HashMap<>();
        headers.put("Accept","application/json");
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
}
