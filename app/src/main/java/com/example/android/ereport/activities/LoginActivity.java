package com.example.android.ereport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereport.R;
import com.example.android.ereport.utils.NetworkUtil;
import com.example.android.ereport.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        et_email = findViewById(R.id.et_login_email);
        et_password = findViewById(R.id.et_login_password);
    }

    public void sign_in(View view) {
        String sign_in_url = Util.SERVER_URL + "final_proj_api/public/log_user_in.php";
        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();
        final String user_type = "complainant";

        StringRequest request = new StringRequest(Request.Method.POST, sign_in_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 4) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Login Details", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                Log.e("VolleyError", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_type", user_type);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        //adding the request to the networkutil
        NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //start the register activity
    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}
