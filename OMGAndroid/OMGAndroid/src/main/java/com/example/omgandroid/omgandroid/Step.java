package com.example.omgandroid.omgandroid;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tristan on 03-Jun-14.
 */
public class Step {
    private LatLng start;
    private LatLng end;
    private String instruction;
    private String distance;
    private String duration;

    public Step(JSONObject step) {
        distance = step.optJSONObject("distance").optString("text");
        duration = step.optJSONObject("duration").optString("text");

        // TODO: format instruction to string
        instruction = step.optString("html_instructions");

        // parse start and end location
        try {
            JSONObject startLoc = step.optJSONObject("start_location");
            JSONObject endLoc = step.optJSONObject("end_location");
            start = new LatLng(startLoc.getDouble("lat"), startLoc.getDouble("lng"));
            end = new LatLng(endLoc.getDouble("lat"), endLoc.getDouble("lng"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getInstruction() {
        return instruction;
    }

    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    public LatLng getEnd() {
        return end;
    }

    public LatLng getStart() {
        return start;
    }
}
