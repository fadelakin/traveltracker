package com.fisheradelakin.traveltracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fisher on 6/30/15.
 */
public class MemoriesDataSource {

    private DbHelper mDbHelper;
    private String[] allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_CITY,
            DbHelper.COLUMN_COUNTRY,
            DbHelper.COLUMN_LATITUDE,
            DbHelper.COLUMN_LONGITUDE,
            DbHelper.COLUMN_NOTES
    };

    public MemoriesDataSource(Context context) {
        mDbHelper = DbHelper.getInstance(context);
    }

    public void createMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NOTES, memory.getNotes());
        values.put(DbHelper.COLUMN_CITY, memory.getCity());
        values.put(DbHelper.COLUMN_COUNTRY, memory.getCountry());
        values.put(DbHelper.COLUMN_LATITUDE, memory.getLatitude());
        values.put(DbHelper.COLUMN_LONGITUDE, memory.getLongitude());
        mDbHelper.getWritableDatabase().insert(DbHelper.MEMORIES_TABLE, null, values);
    }

    public List<Memory> getAllMememories() {
        List<Memory> memories = new ArrayList<>();

        Cursor cursor = mDbHelper.getReadableDatabase().query(DbHelper.MEMORIES_TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Memory memory = cursorToMemory(cursor);
            memories.add(memory);
            cursor.moveToNext();
        }
        cursor.close();
        return memories;
    }

    private Memory cursorToMemory(Cursor cursor) {
        Memory memory = new Memory();
        memory.setCity(cursor.getString(1));
        memory.setCountry(cursor.getString(2));
        memory.setLatitude(cursor.getDouble(3));
        memory.setLongitude(cursor.getDouble(4));
        memory.setNotes(cursor.getString(5));
        return memory;
    }
}
