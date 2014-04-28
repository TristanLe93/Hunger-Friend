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
import org.json.JSONObject;


public class JSONAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    JSONArray jsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
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
            convertView = inflater.inflate(R.layout.row_book, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView)convertView.findViewById(R.id.img_thumbnail);
            holder.titleTextView = (TextView)convertView.findViewById(R.id.text_title);
            holder.authorTextView = (TextView)convertView.findViewById(R.id.text_author);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder)convertView.getTag();
        }

        // Get the current book's data in JSON form
        JSONObject restaurantData = getItem(position);

        // Grab the title and author from the JSON
        String bookTitle = "";
        String authorName = "";

        if (restaurantData.has("name")) {
           bookTitle = restaurantData.optString("name");
        }

        if (restaurantData.has("vicinity")) {
            authorName = restaurantData.optString("vicinity");
        }

        // Send these Strings to the TextViews for display
        holder.titleTextView.setText(bookTitle);
        holder.authorTextView.setText(authorName);

        return convertView;
    }

    /*
     * this is used so you only ever have to do
     *  inflation and finding by ID once ever per View
     */
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
}
