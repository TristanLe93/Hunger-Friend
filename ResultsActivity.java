package com.example.omgandroid.omgandroid;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultsActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView resultList;
    private JSONAdapter adapter;
    private Context con;

    private static final String urlString = "https://maps.googleapis.com/maps/api/place/details/json?";
    private static final String KEY = "AIzaSyAa2USMUtwlohudtlYIN1Gb7jTYvn5albk";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_results);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // set up result list view
        resultList = (ListView)findViewById(R.id.listView);
        adapter = new JSONAdapter(this, getLayoutInflater());
        resultList.setAdapter(adapter);
        resultList.setOnItemClickListener(this);

        // store adapter with json array
        String jsonString = getIntent().getExtras().getString("jsonArray");

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            adapter.updateData(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // get Google Place reference ID
        JSONObject restaurant = adapter.getItem(position);
        makeCallToUrl(restaurant.optString("reference"));
    }

    /**
     * Makes an API call to the url and gathers the returned JSON formatted data
     */
    private void makeCallToUrl(String reference) {
        String url = urlString + "reference=" + reference + "&sensor=false&key=" + KEY;

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

    private void startDetailsActivity(JSONObject jsonObject) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra("restaurant details", jsonObject.toString());
        detailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(detailIntent);
    }

    public void btnBack(View v) {
        finish();

    }
}
