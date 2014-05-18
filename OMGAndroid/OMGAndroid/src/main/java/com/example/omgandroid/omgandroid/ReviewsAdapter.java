package com.example.omgandroid.omgandroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReviewsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    JSONArray jsonArray;

    public ReviewsAdapter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
        jsonArray = new JSONArray();
    }

    public void updateData(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int i) {
        return jsonArray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = inflater.inflate(R.layout.reviews_row, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.authorTextView = (TextView)convertView.findViewById(R.id.textview_authorName);
            holder.ratingTextView = (TextView)convertView.findViewById(R.id.textview_rating);
            holder.messageTextView = (TextView)convertView.findViewById(R.id.textview_message);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {
            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder)convertView.getTag();
        }

        // Get the current book's data in JSON form
        JSONObject reviews = getItem(position);

        //set review data
        try {
            holder.authorTextView.setText(reviews.getString("author_name"));
            holder.ratingTextView.setText(reviews.getString("rating"));
            holder.messageTextView.setText(reviews.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    /*
     * this is used so you only ever have to do
     *  inflation and finding by ID once ever per View
     */
    private static class ViewHolder {
        public TextView authorTextView;
        public TextView ratingTextView;
        public TextView messageTextView;
    }
}
