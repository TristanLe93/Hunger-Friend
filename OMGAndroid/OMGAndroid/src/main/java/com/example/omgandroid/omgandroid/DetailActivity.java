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


public class DetailActivity extends Activity implements View.OnClickListener {
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

        Button directions = (Button)findViewById(R.id.directionsBtn);
        directions.setOnClickListener(this);

        // set up reviews list view
        ReviewsAdapter adapter = new ReviewsAdapter(this, getLayoutInflater());
        ListView resultList = (ListView)findViewById(R.id.listview_reviews);
        resultList.setAdapter(adapter);

        String jsonString = getIntent().getExtras().getString("json");

        try {
            JSONObject data = new JSONObject(jsonString);
            JSONArray reviews = data.getJSONArray("reviews");
            adapter.updateData(reviews);

            // store all json data to its corresponding controls
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

    @Override
    public void onClick(View view) {
        String jsonString = getIntent().getExtras().getString("json");
        String latlng = "";
        String name = "";
        String address = "";

        try {
            JSONObject data = new JSONObject(jsonString);

            // set lat and lng
            JSONObject loc = data.optJSONObject("geometry").optJSONObject("location");
            String lat = loc.optString("lat");
            String lng = loc.optString("lng");
            latlng = lat + "," + lng;

            // set name and address
            name = data.optString("name");
            address = data.optString("vicinity");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent navigation = new Intent(this, NavigationActivity.class);
        navigation.putExtra("latlng", latlng);
        navigation.putExtra("name", name);
        navigation.putExtra("address", address);
        startActivity(navigation);
    }
}
