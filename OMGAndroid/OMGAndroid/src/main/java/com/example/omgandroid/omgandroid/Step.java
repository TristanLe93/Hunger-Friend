package com.example.omgandroid.omgandroid;

import android.text.Html;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores data of a single point of the routing path.
 * The route path is divided up into individual steps.
 *
 * @author Tristan Le, N8320055
 */
public class Step {
    private LatLng start;
    private LatLng end;
    private String instruction;
    private String distance;
    private String duration;
    private List<LatLng> points;

    /**
     * Construct the step by passing the JSON text
     * containing the data
     * @param step - json string of the step
     * @param index - the number of which the step occurs in the route path
     */
    public Step(JSONObject step, int index) {
        distance = step.optJSONObject("distance").optString("text");
        duration = step.optJSONObject("duration").optString("text");
        instruction = index + ". " + Html.fromHtml(step.optString("html_instructions")).toString();

        // parse start and end location
        JSONObject startLoc = step.optJSONObject("start_location");
        JSONObject endLoc = step.optJSONObject("end_location");
        start = new LatLng(startLoc.optDouble("lat"), startLoc.optDouble("lng"));
        end = new LatLng(endLoc.optDouble("lat"), endLoc.optDouble("lng"));

        // get the points of the polyLine
        JSONObject polyline = step.optJSONObject("polyline");
        String points = polyline.optString("points");
        this.points = decodePoly(points);
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

    public List<LatLng> getPoints() {
        return points;
    }

    /**
     * Helper method that decodes the Polyline text from the json string
     * and creates LatLng points.
     * @param encoded - polyline encoded text
     * @return list of LatLng points
     */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
