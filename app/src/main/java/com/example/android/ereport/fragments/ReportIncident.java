package com.example.android.ereport.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.ereport.R;
import com.example.android.ereport.utils.NetworkUtil;
import com.example.android.ereport.utils.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krogers on 4/10/18.
 */

public class ReportIncident extends Fragment {
    private static final String TAG = "ReportIncident";
    Spinner sp_type_issue;
    ArrayAdapter<CharSequence> sp_adapter;
    EditText et_nature_of_issue;
    String nature_of_issue, type_issue;
    Button file_complain;
    FusedLocationProviderClient mFusedLocationClient;
    private double lat, lng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_incident, null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize the objects
        et_nature_of_issue = view.findViewById(R.id.et_nature_of_issue);
        sp_type_issue = view.findViewById(R.id.sp_type_issue);
        sp_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.nature_of_issue_array, android.R.layout.simple_spinner_item);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type_issue.setAdapter(sp_adapter);

        //setting the onclick of the application
        sp_type_issue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_issue = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type_issue = "";
            }
        });

        //location services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        //setting button onclick listener
        file_complain = view.findViewById(R.id.file_complain);
        file_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                file_complain();
            }
        });

    }

    private void file_complain() {
        Log.i(TAG, "file_complain:  Lat: " + lat + " Lng: " + lng);
        if ((lat == 0.0) || (lng == 0.0)) {
            Toast.makeText(getActivity(), "Failed to file complaint", Toast.LENGTH_SHORT).show();
        } else {
            String url = Util.SERVER_URL + "final_proj_api/public/register_user.php?user_type=complaint";
            nature_of_issue = et_nature_of_issue.getText().toString();

            //TODO:: check the form elements before posting results
            Map<String, String> params = new HashMap<>();
            params.put("nature_of_issue", nature_of_issue);
            params.put("type_issue", type_issue);
            params.put("lat", lat + "");
            params.put("lng", lat + "");
            //TODO:: change complainant_id to reflect account signed in
            params.put("complainant_id", "14");

            final JSONObject params_object = new JSONObject(params);

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject response_object = new JSONObject(response);
                        if (response_object.getString("status").equals("1")) {
                            Toast.makeText(getActivity(), "Complaint filed Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Complaint filed Unsuccessful", Toast.LENGTH_SHORT).show();
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

            NetworkUtil.getInstance(getActivity()).addToRequestQueue(request);
        }

    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getActivity().getApplicationContext(), "Please enable gps", Toast.LENGTH_SHORT).show();
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                });


    }
}
