package com.example.android.ereport.fragments;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.webkit.MimeTypeMap;
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
import com.example.android.ereport.activities.HomeActivity;
import com.example.android.ereport.utils.NetworkUtil;
import com.example.android.ereport.utils.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by krogers on 4/10/18.
 */

public class ReportIncident extends Fragment {
    private static final String TAG = "ReportIncident";
    protected double lat, lng;
    ProgressDialog progressBar;
    Spinner sp_type_issue;
    ArrayAdapter<CharSequence> sp_adapter;
    EditText et_nature_of_issue;
    String nature_of_issue, type_issue, intent_data_return;
    Button file_complain, chooseFile;
    FusedLocationProviderClient mFusedLocationClient;

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
        intent_data_return = "";

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

        //setting button onclick listener for choose file
        chooseFile = view.findViewById(R.id.btn_choose_file);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

    }

    private void file_complain1() {
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

    public void uploadFile() {
        new MaterialFilePicker()
                .withActivity(getActivity())
                .withRequestCode(100)
                .start();
    }


    private void file_complain() {
        Log.i(TAG, "file_complain:  Lat: " + lat + " Lng: " + lng);
        if ((lat == 0.0) || (lng == 0.0)) {
            Toast.makeText(getActivity(), "Failed to file complaint", Toast.LENGTH_SHORT).show();
        } else {
            HomeActivity activity = (HomeActivity) getActivity();
            intent_data_return = activity.getData();
            Log.i(TAG, "Fragment: onActivityResult: " + intent_data_return);
            nature_of_issue = et_nature_of_issue.getText().toString();

            progressBar = new ProgressDialog(getActivity());
            progressBar.setTitle("Sending Complaint Details");
            progressBar.setMessage("Please wait...");
            progressBar.show();
            progressBar.setCancelable(false);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Util.SERVER_URL + "final_proj_api/public/file_complaint.php";
                    OkHttpClient client = new OkHttpClient();

                    //TODO:: check the form elements before posting results
                    //TODO:: change complainant_id to reflect account signed in
                    RequestBody requestBody;

                    if (intent_data_return != null) {
                        File f = new File(intent_data_return);
                        String content_type = getMimeType(f.getPath());
                        Log.i(TAG, "content_type for file to be uploaded: " + content_type);
                        String file_path = f.getAbsolutePath();
                        RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);

                        requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("nature_of_issue", nature_of_issue)
                                .addFormDataPart("type_issue", type_issue)
                                .addFormDataPart("lat", lat + "")
                                .addFormDataPart("lng", lng + "")
                                .addFormDataPart("complainant_id", "14").addFormDataPart("media_type", content_type)
                                .addFormDataPart("uploaded_file", file_path.substring(
                                        file_path.lastIndexOf("/") + 1), file_body)
                                .build();
                    } else {
                        requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("nature_of_issue", nature_of_issue)
                                .addFormDataPart("type_issue", type_issue)
                                .addFormDataPart("lat", lat + "")
                                .addFormDataPart("lng", lng + "")
                                .addFormDataPart("complainant_id", "14")
                                .build();
                    }


                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();

                    try {

                        okhttp3.Response response = client.newCall(request).execute();

                        if (!response.isSuccessful()) {
                            throw new IOException("Error : " + response);
                        }

                        try {
                            JSONObject response_object = new JSONObject(response.body().string());
                            if (response_object.getString("status").equals("1")) {
                                Log.i(TAG, "run: server response" + " Complaint filed Successful");
                            } else {
                                Log.i(TAG, "run: server response" + " Complaint filed Unsuccessful");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressBar.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
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

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
