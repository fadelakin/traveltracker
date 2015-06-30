package com.fisheradelakin.traveltracker;

import android.content.Context;

/**
 * Created by Fisher on 6/30/15.
 */
public class MemoriesDataSource {

    private DbHelper mDbHelper;

    public MemoriesDataSource(Context context) {
        mDbHelper = DbHelper.getInstance(context);
    }
}
