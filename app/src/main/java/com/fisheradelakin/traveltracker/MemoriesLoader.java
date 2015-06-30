package com.fisheradelakin.traveltracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Fisher on 6/30/15.
 */
public class MemoriesLoader extends DbCursorLoader {

    private MemoriesDataSource mDataSource;

    public MemoriesLoader(Context context, MemoriesDataSource memoriesDataSource) {
        super(context);
        mDataSource = memoriesDataSource;
    }

    @Override
    protected Cursor loadCursor() {
        return mDataSource.allMemoriesCursor();
    }
}
