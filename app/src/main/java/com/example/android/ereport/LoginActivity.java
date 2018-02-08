package com.example.android.ereport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void sign_in(View view) {

    }

    //start the register activity
    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}
