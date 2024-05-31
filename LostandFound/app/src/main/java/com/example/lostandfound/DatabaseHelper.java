package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "LOST_FOUND_ITEMS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_POST_TYPE = "POST_TYPE";
    public static final String COLUMN_ITEM_NAME = "ITEM_NAME";
    public static final String COLUMN_ITEM_DESCRIPTION = "ITEM_DESCRIPTION";
    public static final String COLUMN_ITEM_FOUND_DATE = "ITEM_FOUND_DATE";
    public static final String COLUMN_ITEM_FOUND_LOCATION = "ITEM_FOUND_LOCATION";
    public static final String COLUMN_FINDER_NAME = "FINDER_NAME";
    public static final String COLUMN_FINDER_PHONE = "FINDER_PHONE";

    public DatabaseHelper(@Nullable Context context) { super(context, "items.db", null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_POST_TYPE + " TEXT, " + COLUMN_ITEM_NAME + " TEXT, " + COLUMN_ITEM_DESCRIPTION + " TEXT, " + COLUMN_ITEM_FOUND_DATE + " TEXT, " + COLUMN_ITEM_FOUND_LOCATION + " TEXT, " + COLUMN_FINDER_NAME + " TEXT, " + COLUMN_FINDER_PHONE + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addPost(LostAndFoundModel post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_POST_TYPE, post.getPostType());
        cv.put(COLUMN_ITEM_NAME, post.getItemName());
        cv.put(COLUMN_ITEM_DESCRIPTION, post.getItemDescription());
        cv.put(COLUMN_ITEM_FOUND_DATE, post.getItemFoundDate());
        cv.put(COLUMN_ITEM_FOUND_LOCATION, post.getItemFoundLocation());
        cv.put(COLUMN_FINDER_NAME, post.getFinderName());
        cv.put(COLUMN_FINDER_PHONE, post.getFinderPhone());

        long insert = db.insert(TABLE_NAME, null, cv);
        return insert != -1;
    }

    public List<LostAndFoundModel> getAll() {
        List<LostAndFoundModel> returnList = new ArrayList<>();

        // Get all records from DB
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        LostAndFoundModel LostAndFoundModel;

        // If there are any records, add them to our list
        if (cursor.moveToFirst()) {
            do {
                int postId = cursor.getInt(0);
                String postType = cursor.getString(1);
                String itemName = cursor.getString(2);
                String itemDescription = cursor.getString(3);
                String itemFoundDate = cursor.getString(4);
                String itemFoundLocation = cursor.getString(5);
                String finderName = cursor.getString(6);
                String finderPhone = cursor.getString(7);

                LostAndFoundModel = new LostAndFoundModel(postId, postType, itemName, itemDescription, itemFoundDate, itemFoundLocation, finderName, finderPhone);
                returnList.add(LostAndFoundModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public LostAndFoundModel getPostById(int id) {
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        LostAndFoundModel LostAndFoundModel;

        // If a record was returned, create a post object out of it
        if (cursor.moveToFirst()) {
            int postId = cursor.getInt(0);
            String postType = cursor.getString(1);
            String itemName = cursor.getString(2);
            String itemDescription = cursor.getString(3);
            String itemFoundDate = cursor.getString(4);
            String itemFoundLocation = cursor.getString(5);
            String finderName = cursor.getString(6);
            String finderPhone = cursor.getString(7);

            LostAndFoundModel = new LostAndFoundModel(postId, postType, itemName, itemDescription, itemFoundDate, itemFoundLocation, finderName, finderPhone);
        } else {
            LostAndFoundModel = new LostAndFoundModel(-1, "Error", "", "", "", "", "", "");
        }

        cursor.close();
        db.close();
        return LostAndFoundModel;
    }

    public boolean deletePostById(int id) {
        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        return cursor.moveToFirst(); // returns true if a record was deleted and returned from db.rawQuery; false if nothing was returned.
    }
}
