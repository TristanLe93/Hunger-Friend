package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This is the initial activity of the app.
 *
 * @author Tristan Le
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private Button searchButton;
    private Location location;
    private Spinner categories;
    private Spinner quality;
    private Spinner price;

    /**
     * Set the search button to this listener.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up button
        searchButton = (Button)findViewById(R.id.button);
        searchButton.setOnClickListener(this);

        // set up spinners
        categories = (Spinner)findViewById(R.id.categories);
        quality = (Spinner)findViewById(R.id.quality);
        price = (Spinner)findViewById(R.id.price);
    }

    /**
     * On click make a url call to Google Places API
     */
    @Override
    public void onClick(View view) {
        makeCallToUrl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Makes an API call to the url and returns JSON formatted data
     */
    private void makeCallToUrl() {
        // get user location via 3G/GPS/WiFi connection
        location = getLocationData();
        if (location == null) {
            return;
        }

        // combine url string

        String loc = "-27.47,153.02";
        //String loc = location.getLatitude() + "," + location.getLongitude();
        String urlString = Constants.URL_NEARBY + "location=" + loc + "&radius=" + Constants.RADIUS +
                "&types=" + Constants.TYPE + "&sensor=true&key=" + Constants.KEY;

        // apply user filters
        int price = this.price.getSelectedItemPosition() - 1;
        if (price >= 0) {
            urlString += "&maxprice=" + price;
        }

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(urlString, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        int results = jsonObject.optJSONArray("results").length();

                        // only transition if we have results
                        if (results == 0)  {
                            String msg = "No results were found...";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        } else {
                            startResultsActivity(jsonObject.optJSONArray("results"));
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
     * Retrieves location information from the service provider.
     * @return Location object containing latitude and longitude
     */
    private Location getLocationData() {
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();

        // get the location
        String provider = lm.getBestProvider(c, false);
        Location location = lm.getLastKnownLocation(provider);

        // display error if we cannot get location data
        if (location == null) {
            String msg = "Unable to get location data. Ensure that Location Services are enabled.";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            return null;
        }

        return location;
    }

    /**
     * Begin the transition to the results screen
     * @param jsonArray json text containing restaurant information
     */
    private void startResultsActivity(JSONArray jsonArray) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("jsonArray", jsonArray.toString());
        i.putExtra("latitude", location.getLatitude());
        i.putExtra("longitude", location.getLongitude());
        startActivity(i);
    }
}
