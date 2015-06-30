package com.fisheradelakin.traveltracker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Fisher on 6/30/15.
 */
public abstract class DbCursorLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;

    public DbCursorLoader(Context context) {
        super(context);
    }

    protected abstract Cursor loadCursor();

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursor();
        if(cursor != null) {
            cursor.getCount();
        }
        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        Cursor oldCursor = mCursor;
        mCursor = data;

        if(isStarted()) {
            super.deliverResult(data);
        }

        if(oldCursor != null && oldCursor != data) {
            onReleaseResources(oldCursor);
        }
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null) {
            deliverResult(mCursor);
        }

        if(takeContentChanged() || mCursor == null) {
            forceLoad(); // reload data if no cursor
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);

        if(data != null) {
            onReleaseResources(data);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if(mCursor != null) {
            onReleaseResources(mCursor);
        }

        mCursor = null;
    }

    private void onReleaseResources(Cursor cursor) {
        if(!cursor.isClosed()) {
            cursor.close();
        }
    }
}
