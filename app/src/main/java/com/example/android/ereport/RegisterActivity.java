package com.example.android.ereport;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText et_first_name, et_last_name, et_other_name;
    EditText et_telephone, et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        et_email = findViewById(R.id.email);
        et_first_name = findViewById(R.id.first_name);
        et_last_name = findViewById(R.id.last_name);
        et_other_name = findViewById(R.id.other_names);
        et_telephone = findViewById(R.id.telephone);
        et_password = findViewById(R.id.password);
    }

    public void sign_up(View view) {
        final String first_name, last_name, other_names, telephone, email, password, url;

        //server API to confirm mobile number
        url = Util.SERVER_URL + "final_proj_api/public/send_message.php";

        //getting the info typed in the fields
        first_name = et_first_name.getText().toString();
        last_name = et_last_name.getText().toString();
        other_names = et_other_name.getText().toString();
        telephone = et_telephone.getText().toString();
        password = et_password.getText().toString();
        email = et_email.getText().toString();

        //checking if the fields are not empty
        if (first_name.isEmpty() || last_name.isEmpty() || other_names.isEmpty() || telephone.isEmpty()
                || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
        } else {

            //if not empty send telephone number to API for confirmation
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String ans) {
                    Log.e("VerifyCode", "onResponse: " + ans);
                    //checking if the status code from the API is 1
                    try {
                        JSONObject response = new JSONObject(ans);
                        if (response.getString("status").equals("1")) {
                            // Sending the data to confirmation page
                            Intent intent = new Intent(getApplicationContext(), TelephoneConfirmationActivity.class);
                            intent.putExtra("code", response.getInt("code") + "");
                            //intent.putExtra("first");

                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Invalid telephone number provided", Toast.LENGTH_SHORT).show();
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
                    params.put("mobile_number", telephone);
                    return params;
                }
            };

            //adding the request to the networkutil
            NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }
}
