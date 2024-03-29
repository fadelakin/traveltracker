package com.fisheradelakin.traveltracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by Fisher on 6/29/15.
 */
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG  = "MarkerAdapter";

    private LayoutInflater mLayoutInflater;
    private View mView;
    private HashMap<String, Memory> mMemories;

    MarkerAdapter(LayoutInflater layoutInflater, HashMap<String, Memory> memories) {
        mLayoutInflater = layoutInflater;
        mMemories = memories;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        if(mView == null) {
            mView = mLayoutInflater.inflate(R.layout.marker, null);
        }

        Memory memory = mMemories.get(marker.getId());

        TextView titleView = (TextView) mView.findViewById(R.id.title);
        titleView.setText(memory.getCity());

        TextView snippetView = (TextView) mView.findViewById(R.id.snippet);
        snippetView.setText(memory.getCountry());

        TextView notesView = (TextView) mView.findViewById(R.id.notes);
        notesView.setText(memory.getNotes());

        return mView;
    }
}
