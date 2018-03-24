package com.example.android.ereport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class TelephoneConfirmationActivity extends AppCompatActivity {
    String code, first_name, last_name, other_names, telephone, email, password, url, address;
    EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_confirmation);

        //getting the data from the intent
        get_intent_data();

        et_code = findViewById(R.id.et_code);
    }

    private void get_intent_data() {
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        first_name = intent.getStringExtra("first_name");
        last_name = intent.getStringExtra("last_name");
        other_names = intent.getStringExtra("other_names");
        telephone = intent.getStringExtra("telephone");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        address = intent.getStringExtra("address");
    }

    public void confirm_info(View view) {
        final Map<String, String> params = new HashMap<>();
        url = Util.SERVER_URL + "final_proj_api/public/register_user.php?user_type=complainant";
        if (et_code.getText().toString().equals(code)) {
            //Toast.makeText(this, "Valid Code", Toast.LENGTH_SHORT).show();
            params.put("first_name", first_name);
            params.put("last_name", last_name);
            params.put("other_names", other_names);
            params.put("telephone", telephone);
            params.put("email", email);
            params.put("password", password);
            params.put("address", address);

            final JSONObject params_object = new JSONObject(params);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject response_object = new JSONObject(response);
                        if (response_object.getString("status").equals("1")) {
                            Toast.makeText(TelephoneConfirmationActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TelephoneConfirmationActivity.this, "Registration UnSuccessfull", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return params_object.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);
        } else {
            Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show();
        }

    }
}
