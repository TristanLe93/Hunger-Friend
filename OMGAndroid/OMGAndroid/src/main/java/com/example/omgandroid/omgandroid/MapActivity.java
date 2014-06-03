package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MapActivity displays a map of markers that show each restaurants location.
 * Each marker on the map is interactive and displays the restaurant's title and
 * address location. Users can click on the details section of the marker to grab more
 * details of the restaurant.
 *
 * @author Tristan Le (N8320055)
 */
public class MapActivity extends Activity implements GoogleMap.OnInfoWindowClickListener {
    HashMap<String, String> restaurantId;
    ArrayList<MarkerOptions> markers;

    /**
     * Create and set up the Google Map.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        restaurantId = new HashMap(20);
        markers = new ArrayList<MarkerOptions>();

        parseJsonString();
        setupMap();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String key = marker.getTitle();

        if (restaurantId.containsKey(key)) {
            String id = restaurantId.get(key);
            getRestaurantDetails(id);
        }
    }

    /**
     * Parses the json string and stores all restaurant id's to a hashmap.
     * A Google Marker is created for each restaurant in the Json.
     */
    private void parseJsonString() {
        String restaurantJson = getIntent().getExtras().getString("jsonArray");

        try {
            JSONArray restaurants = new JSONArray(restaurantJson);

            for (int i = 0; i < restaurants.length(); i++) {
                JSONObject r = restaurants.getJSONObject(i);

                // get restaurant details
                String name = r.optString("name");
                String address = r.optString("vicinity");
                String reference = r.optString("reference");

                // get lat and lng
                JSONObject location = r.optJSONObject("geometry").optJSONObject("location");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");
                LatLng loc = new LatLng(lat, lng);

                // create a google marker for this restaurant
                MarkerOptions m = new MarkerOptions()
                        .title(name)
                        .snippet(address)
                        .position(loc);

                restaurantId.put(name, reference);
                markers.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the Google Map by adding map markers from the markers collection.
     */
    private void setupMap() {
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnInfoWindowClickListener(this);

        for (MarkerOptions m : markers) {
            map.addMarker(m);
        }

        // set users location
        double lat = getIntent().getExtras().getDouble("latitude");
        double lng = getIntent().getExtras().getDouble("longitude");
        LatLng userLocation = new LatLng(lat, lng);

        // create a marker for the users location
        map.addMarker(new MarkerOptions()
                .title("You are here!")
                .position(userLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    /**
     * Makes an API call to get detailed Json of restaurant selected on map
     */
    private void getRestaurantDetails(String reference) {
        Toast.makeText(getApplicationContext(), "Fetching restaurant details...", Toast.LENGTH_SHORT).show();
        String url = Constants.URL_DETAILS + "reference=" + reference +
                "&sensor=false&key=" + Constants.KEY;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                startDetailsActivity(jsonObject.optJSONObject("result"));
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " +
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Begins the transition to DetailsActivity
     *
     * @param json containing information of the chosen restaurant
     */
    private void startDetailsActivity(JSONObject json) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("json", json.toString());
        startActivity(i);
    }

    public void btnBack(View v) {
        finish();
    }
}