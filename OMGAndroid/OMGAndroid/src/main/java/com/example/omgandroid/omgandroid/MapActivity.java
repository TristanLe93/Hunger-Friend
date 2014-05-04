package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // get handle of the Map
        GoogleMap map = ((MapFragment)getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        String name = getIntent().getExtras().getString("name");
        String address = getIntent().getExtras().getString("address");

        double lat = getIntent().getExtras().getDouble("latitude");
        double lng = getIntent().getExtras().getDouble("longitude");
        LatLng restaurantLocation = new LatLng(lat, lng);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 13));

        map.addMarker(new MarkerOptions()
                .title(name)
                .snippet(address)
                .position(restaurantLocation));
    }
}
