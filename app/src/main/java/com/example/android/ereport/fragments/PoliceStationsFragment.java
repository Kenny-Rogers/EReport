package com.example.android.ereport.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.ereport.R;
import com.example.android.ereport.models.App;
import com.example.android.ereport.models.Secretariat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by krogers on 3/24/18.
 */

public class PoliceStationsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "PoliceStationsFragment";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Box<Secretariat> secretariats;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return inflater.inflate(R.layout.fragment_police_stations, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BoxStore boxStore = ((App) getActivity().getApplication()).getBoxStore();
        secretariats = boxStore.boxFor(Secretariat.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (secretariats.getAll().size() != 0) {
            Log.i(TAG, "onMapReady: " + "Secretariat data found");
            ArrayList<Secretariat> secretariatArrayList = (ArrayList<Secretariat>) secretariats.getAll();

            for (int i = 0; i < secretariatArrayList.size(); i++) {
                Secretariat sec_object = secretariatArrayList.get(i);
                LatLng sec = new LatLng(Double.parseDouble(sec_object.getLat()), Double.parseDouble(sec_object.getLng()));
                mMap.addMarker(new MarkerOptions().position(sec).title(sec_object.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sec));
            }
        } else {
            Log.i(TAG, "onMapReady: " + "Secretariat Not data found");
        }

    }
}
