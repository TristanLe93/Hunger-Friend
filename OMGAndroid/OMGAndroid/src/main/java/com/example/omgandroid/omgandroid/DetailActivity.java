package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

        // get jsonString from getExtras
        String jsonString = getIntent().getExtras().getString("restaurant details");

        // stores all json data to its corresponding controls
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

    }
}
