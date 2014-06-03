package com.example.omgandroid.omgandroid;

import android.location.Location;

/**
 * Class designed to keep all of its constant variables here.
 */
public class Constants {
    public static final String URL_NEARBY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static final String URL_DETAILS = "https://maps.googleapis.com/maps/api/place/details/json?";
    public static final String URL_DIRECTIONS = "http://maps.googleapis.com/maps/api/directions/json?sensor=true&mode=walking";

    public static final String KEY = "AIzaSyAa2USMUtwlohudtlYIN1Gb7jTYvn5albk";
    public static final String TYPE = "restaurant";
    public static final int RADIUS = 500;

    // stored here to transfer user location to navigation screen
    public static Location userLocation;
}
