package com.example.omgandroid.omgandroid;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;


public class ResultsActivity extends Activity {
    private ListView resultList;
    private JSONAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_results);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        resultList = (ListView)findViewById(R.id.listView);
        adapter = new JSONAdapter(this, getLayoutInflater());
        resultList.setAdapter(adapter);

        String jsonString = getIntent().getExtras().getString("jsonArray");

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            adapter.updateData(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
