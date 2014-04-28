package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Types;
import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button searchButton;
    private ListView resultList;

    private JSONAdapter jsonAdapter;

    // google places api url stuff
    private static final String KEY = "AIzaSyAa2USMUtwlohudtlYIN1Gb7jTYvn5albk";
    private static final String QUERY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String TYPE = "restaurant";
    private static final String LOCATION = "-27.47,153.02";     // brisbane lng/lat
    private static final int RADIUS = 500;                      // within 500 meter radius

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up button
        searchButton = (Button)findViewById(R.id.button);
        searchButton.setOnClickListener(this);

        // set up list
        resultList = (ListView)findViewById(R.id.listView);
        jsonAdapter = new JSONAdapter(this, getLayoutInflater());
        resultList.setAdapter(jsonAdapter);
    }

    @Override
    public void onClick(View view) {
        makeCallToUrl();
    }

    /**
     * Makes an API call to the url and gathers the returned JSON formatted data
     */
    private void makeCallToUrl() {
        String urlString = QUERY_URL + "location=" + LOCATION + "&radius=" + RADIUS +
                "&types=" + TYPE + "&sensor=false&key=" + KEY;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(urlString, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                        jsonAdapter.updateData(jsonObject.optJSONArray("results"));
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
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
}
