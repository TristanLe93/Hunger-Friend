package com.example.omgandroid.omgandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends Activity implements View.OnClickListener {
    private Button directionsBtn;

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

        directionsBtn = (Button)findViewById(R.id.directionsBtn);
        directionsBtn.setOnClickListener(this);

        // set up reviews list view
        ReviewsAdapter adapter = new ReviewsAdapter(this, getLayoutInflater());
        ListView resultList = (ListView)findViewById(R.id.listview_reviews);
        resultList.setAdapter(adapter);

        String jsonString = getIntent().getExtras().getString("json");

        try {
            JSONObject data = new JSONObject(jsonString);
            JSONArray reviews = data.optJSONArray("reviews");

            if (reviews != null) {
                adapter.updateData(reviews);
            }

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
        if (view == directionsBtn) {
            directionsBtnClick();
        }
    }

    /**
     * Opens the navigation map display a route path to the restaurant from
     * the user's location
     */
    private void directionsBtnClick() {
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
