package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This activity produces a route path from the users location
 * and to the restaurants location.
 */
public class NavigationActivity extends Activity {
    private GoogleMap map;
    private ArrayList<Step> steps = new ArrayList<Step>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        createMap();
    }

    private void setInformation(JSONObject leg) {
        List<String> headers = new ArrayList<String>();
        HashMap<String, List<String>> child = new HashMap<String, List<String>>();

        // add list headers
        headers.add("Directions");
        headers.add("Details");

        // get details
        String startAddress = "From: " + leg.optString("start_address");
        String endAddress = "To: " + leg.optString("end_address");
        String distance = "Distance: " + leg.optJSONObject("distance").optString("text");
        String time = "Duration: " + leg.optJSONObject("duration").optString("text");

        List<String> details = new ArrayList<String>();
        details.add(startAddress);
        details.add(endAddress);
        details.add(distance);
        details.add(time);

        // get directions
        List<String> directions = new ArrayList<String>();
        for (Step s : steps) {
            directions.add(s.getInstruction());
        }

        // set child data to hashmap
        child.put(headers.get(0), directions);
        child.put(headers.get(1), details);

        // create the list view
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.expLv);
        ExpandableListAdapter adapter = new ExpandableListAdapter(this, headers, child);
        listView.setAdapter(adapter);
    }

    /**
     * Creates an instance of the map
     */
    private void createMap() {
        Toast.makeText(getApplicationContext(), "Fetching routing details...", Toast.LENGTH_SHORT).show();
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

        // get user location
        double lat = Constants.userLocation.getLatitude();
        double lng = Constants.userLocation.getLongitude();
        LatLng userLoc = new LatLng(lat, lng);

        // create a marker for the users location
        map.addMarker(new MarkerOptions()
                .title("You are here!")
                .position(userLoc)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // get restaurant location
        String[] latlng = getIntent().getExtras().getString("latlng").split(",");
        lat = Double.parseDouble(latlng[0]);
        lng = Double.parseDouble(latlng[1]);
        LatLng restaurantLoc = new LatLng(lat, lng);

        // set restaurant info
        String name = getIntent().getExtras().getString("name");
        String address = getIntent().getExtras().getString("address");

        // create marker for restaurant location
        map.addMarker(new MarkerOptions()
                .title(name)
                .snippet(address)
                .position(restaurantLoc));

        // find location between destination and location
        double x = ((restaurantLoc.latitude - userLoc.latitude) / 2) + userLoc.latitude;
        double y = ((restaurantLoc.longitude - userLoc.longitude) / 2) + userLoc.longitude;
        LatLng destination = new LatLng(x, y);

        // zoom to user location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 16));

        // fetch directions
        String start =  userLoc.latitude + "," + userLoc.longitude;
        String end = getIntent().getExtras().getString("latlng");
        fetchDirections(start, end);
    }

    /**
     * Makes a URL call to the API and attempts to get directions between two points.
     * @param start - starting location
     * @param end - ending location
     */
    private void fetchDirections(String start, String end) {
        // create url string
        String url = Constants.URL_DIRECTIONS + "&origin=" + start + "&destination=" + end;

        ArrayList route = new ArrayList<Step>();

        // establish connection to the Directions API
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject directions) {
                JSONArray routeJson = directions.optJSONArray("routes");

                if (routeJson.length() > 0) {
                    // parse the json response
                    JSONArray legs = routeJson.optJSONObject(0).optJSONArray("legs");

                    parseJsonResponse(legs);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no routing possible for this location.", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                // display error message
                String msg = "Error: " + statusCode + " " + throwable.getMessage();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Parses the JSON response and creates the routing on the map to the location.
     * @param jsonLegs
     */
    private void parseJsonResponse(JSONArray jsonLegs) {
        JSONObject leg = jsonLegs.optJSONObject(0);
        JSONArray jsonSteps = leg.optJSONArray("steps");
        steps = new ArrayList<Step>();

        // store each step information
        for (int i = 0; i < jsonSteps.length(); i++) {
            JSONObject step = jsonSteps.optJSONObject(i);
            Step s = new Step(step, i);
            steps.add(s);
        }

        PolylineOptions polyline = new PolylineOptions()
                .geodesic(true)
                .width(5)
                .color(Color.BLUE);

        // create the routing
        for (Step s : steps) {
            List<LatLng> points = s.getPoints();

            for (int i = 0; i < points.size(); i++) {
                LatLng src = points.get(i);
                polyline.add(src);
            }
        }

        map.addPolyline(polyline);
        setInformation(leg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnBack(View v) {
        finish();
    }
}