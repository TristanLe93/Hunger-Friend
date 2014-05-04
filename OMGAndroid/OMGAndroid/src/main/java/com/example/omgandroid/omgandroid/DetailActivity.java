package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // get all the controls on this activity
        TextView title = (TextView)findViewById(R.id.title_textview);
        TextView address = (TextView)findViewById(R.id.address_textview);
        TextView phone = (TextView)findViewById(R.id.phone_textview);
        TextView rating = (TextView)findViewById(R.id.rating_textview);
        TextView website = (TextView)findViewById(R.id.url_textview);

        String jsonString = getIntent().getExtras().getString("restaurant details");

        // store all json data to its corresponding controls
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            title.setText(jsonObject.optString("name"));
            address.setText(jsonObject.optString("vicinity"));
            phone.setText(jsonObject.optString("formatted_phone_number"));
            rating.setText("Rating: " + jsonObject.optString("rating"));
            website.setText(jsonObject.optString("website"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button button = (Button)findViewById(R.id.map_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMapScreen();
            }
        });
    }

    private void goToMapScreen() {
        double lat = Double.NaN;
        double lng = Double.NaN;
        String name = "";
        String address = "";

        //get location info of restaurant for google maps
        try {
            String jsonString = getIntent().getExtras().getString("restaurant details");
            JSONObject jsonObject = new JSONObject(jsonString);

            // get address and name
            name = jsonObject.optString("name");
            address = jsonObject.optString("vicinity");

            // get lat and lng
            JSONObject location = jsonObject.optJSONObject("geometry").optJSONObject("location");
            lat = location.getDouble("lat");
            lng = location.getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent mapIntent = new Intent(this, MapActivity.class);

        // pass them to next view
        mapIntent.putExtra("name", name);
        mapIntent.putExtra("address", address);
        mapIntent.putExtra("latitude", lat);
        mapIntent.putExtra("longitude", lng);

        mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mapIntent);
    }
}
