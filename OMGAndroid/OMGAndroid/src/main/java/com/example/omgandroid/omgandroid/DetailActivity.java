package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends Activity {
    private ListView resultList;
    private ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_detail);

        // get all the controls on this activity
        TextView title = (TextView)findViewById(R.id.title_textview);
        TextView address = (TextView)findViewById(R.id.address_textview);
        TextView phone = (TextView)findViewById(R.id.phone_textview);
        TextView rating = (TextView)findViewById(R.id.rating_textview);
        TextView website = (TextView)findViewById(R.id.url_textview);

        // set up reviews list view
        adapter = new ReviewsAdapter(this, getLayoutInflater());
        resultList = (ListView)findViewById(R.id.listview_reviews);
        resultList.setAdapter(adapter);

        String jsonString = getIntent().getExtras().getString("json");

        try {
            // store all json data to its corresponding controls
            JSONObject data = new JSONObject(jsonString);
            JSONArray reviews = data.getJSONArray("reviews");
            adapter.updateData(reviews);

            title.setText(data.optString("name"));
            address.setText(data.optString("vicinity"));
            phone.setText(data.optString("formatted_phone_number"));
            rating.setText("Rating: " + data.optString("rating"));
            website.setText(data.optString("website"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void btnBack(View v) {
        finish();
    }
}
