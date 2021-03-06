package com.example.android.ereport.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereport.R;
import com.example.android.ereport.fragments.AnnouncementListFragment;
import com.example.android.ereport.fragments.PoliceStationsFragment;
import com.example.android.ereport.fragments.ReportIncident;
import com.example.android.ereport.models.Announcement;
import com.example.android.ereport.models.App;
import com.example.android.ereport.models.Secretariat;
import com.example.android.ereport.utils.LocationTracker;
import com.example.android.ereport.utils.NetworkUtil;
import com.example.android.ereport.utils.Util;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";
    String intent_data_return;

    Box<Announcement> announcements;
    Box<Secretariat> secretariats;
    Intent trackingIntent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        local_storage_setup();
        sync_data();

        trackingIntent = new Intent(this, LocationTracker.class);
        startService(trackingIntent);
    }

    private void local_storage_setup() {
        //ObjectBox set up
        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        announcements = boxStore.boxFor(Announcement.class);
        secretariats = boxStore.boxFor(Secretariat.class);
    }

    //gets latest details
    private void sync_data() {
        final String date;
        String url;

        //announcements
        if (announcements.getAll().isEmpty()) {
            date = "";
        } else {
            ArrayList<Announcement> list_announcements = (ArrayList<Announcement>) announcements.getAll();
            int count = list_announcements.size();
            Announcement announcement = list_announcements.get(count - 1);
            date = announcement.getDate();
        }

        url = Util.SERVER_URL + "final_proj_api/public/get_users_list.php?user_type=announcement&date=" + date;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                notification();
                try {
                    JSONArray responsearray = new JSONArray(response);
                    for (int i = 0; i < responsearray.length(); i++) {
                        JSONObject object = responsearray.getJSONObject(i);
                        Announcement announcement = new Announcement();
                        announcement.setImage(object.getString("image"));
                        announcement.setDate(object.getString("date_published"));
                        announcement.setMessage(object.getString("message"));
                        announcement.setTitle(object.getString("title"));
                        announcements.put(announcement);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(HomeActivity.this, "Data returned", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "No new announcements found", Toast.LENGTH_SHORT).show();
            }
        });

        NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(request);


        //secretariats
        String sec_date;
        if (secretariats.getAll().isEmpty()) {
            sec_date = "";
        } else {
            ArrayList<Secretariat> list_secretariats = (ArrayList<Secretariat>) secretariats.getAll();
            int count = list_secretariats.size();
            Secretariat secretariat = list_secretariats.get(count - 1);
            sec_date = secretariat.getDate_published();
        }

        url = Util.SERVER_URL + "final_proj_api/public/get_users_list.php?user_type=secretariat&date=" + sec_date;

        StringRequest sec_request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray responsearray = new JSONArray(response);
                    for (int i = 0; i < responsearray.length(); i++) {
                        JSONObject object = responsearray.getJSONObject(i);
                        Secretariat secretariat = new Secretariat();
                        secretariat.setType(object.getString("type"));
                        secretariat.setRegion(object.getString("region"));
                        secretariat.setName(object.getString("name"));
                        secretariat.setRep_id(object.getString("rep_id"));
                        secretariat.setLat(object.getString("lat"));
                        secretariat.setLng(object.getString("lng"));
                        secretariat.setDate_published(object.getString("date_published"));
                        secretariats.put(secretariat);

                        Toast.makeText(HomeActivity.this, "Data inserted " + secretariats.getAll().size(), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(HomeActivity.this, "Secretariat Data returned", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "No new Secretariat found", Toast.LENGTH_SHORT).show();
            }
        });

        NetworkUtil.getInstance(getApplicationContext()).addToRequestQueue(sec_request);

    }

    private void notification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ghana_police)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ghana_police))
                .setContentTitle("Notification from Ghana Police")
                .setContentText("Hello nad welcome to  Ghana Police");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            intent_data_return = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.i(TAG, "onActivityResult: " + intent_data_return);
        }
    }

    public String getData() {
        return this.intent_data_return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_announcement) {
            // Handle the announcements action
            fragment = new AnnouncementListFragment();
        } else if (id == R.id.nav_gallery) {
            //handle the police stations action
            fragment = new PoliceStationsFragment();
        } else if (id == R.id.nav_slideshow) {
            //handle the report incident
            fragment = new ReportIncident();
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        } else {
            fragment = new AnnouncementListFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.screen_area, fragment);

            fragmentTransaction.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(trackingIntent);
    }


}
