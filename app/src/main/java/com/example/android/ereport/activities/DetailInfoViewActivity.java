package com.example.android.ereport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.ereport.R;
import com.example.android.ereport.models.Announcement;
import com.example.android.ereport.models.App;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class DetailInfoViewActivity extends AppCompatActivity {
    Box<Announcement> announcements;
    TextView tv_title, tv_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info_view_activity);

        //getting the id of the announcement from the intent extras
        Intent intent = getIntent();
        String extras = intent.getStringExtra("announcement_id");
        // Toast.makeText(this, extras, Toast.LENGTH_SHORT).show();
        long announcement_id = Long.parseLong(extras);

        //setting up to fetch the info from the local db
        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        announcements = boxStore.boxFor(Announcement.class);

        //fetching the particular announcement
        Announcement announcement = announcements.get(announcement_id);

        //initializing the UI elements
        tv_title = findViewById(R.id.tv_title);
        tv_message = findViewById(R.id.tv_description);

        //setting the details to the screen
        tv_title.setText(announcement.getTitle());
        tv_message.setText(announcement.getMessage());

    }
}
