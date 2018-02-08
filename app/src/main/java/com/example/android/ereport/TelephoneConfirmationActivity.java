package com.example.android.ereport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TelephoneConfirmationActivity extends AppCompatActivity {
    String code;
    EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_confirmation);

        get_intent_data();

        et_code = findViewById(R.id.et_code);
    }

    private void get_intent_data() {
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
    }

    public void confirm_info(View view) {
        if (et_code.getText().toString().equals(code)) {
            Toast.makeText(this, "Valid Code", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show();
        }
    }
}
