/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter.caching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CachingDatabase extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "CachingDatabase";

  private static final String TABLE_CACHE = "local_cache";

  private static final String KEY_NAME = "url";
  private static final String KEY_BODY = "body";
  private static final String KEY_CREATED = "createdAt";

  public CachingDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    String CREATE_CACHE_TABLE = "CREATE TABLE " + TABLE_CACHE + "("
        + KEY_NAME + " STRING PRIMARY KEY," + KEY_BODY + " TEXT,"
        + KEY_CREATED + " LONG" + ")";
    db.execSQL(CREATE_CACHE_TABLE);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (newVersion == 1) {
      // Wipe older tables if existed
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE);
      // Create tables again
      onCreate(db);
    }
  }

  public void addToCache(String name, String body) {
    // Open database connection
    SQLiteDatabase db = this.getWritableDatabase();
    // Define values for each field
    ContentValues values = new ContentValues();
    values.put(KEY_NAME, name);
    values.put(KEY_BODY, body);
    values.put(KEY_CREATED, System.currentTimeMillis());
    if(null == getCache(name)) {
      // Insert Row
      db.insertOrThrow(TABLE_CACHE, null, values);
    } else {
      int result = db.update(TABLE_CACHE, values, KEY_NAME + " = ?",
          new String[] { name });
    }
    db.close(); // Closing database connection
  }

  public String getCache(String name) {
    String body = null;
    // Open database for reading
    SQLiteDatabase db = this.getReadableDatabase();
    // Construct and execute query
    Cursor cursor = db.query(TABLE_CACHE,  // TABLE
        new String[] { KEY_NAME, KEY_BODY, KEY_CREATED }, // SELECT
        KEY_NAME + "= ?", new String[] { String.valueOf(name) },  // WHERE, ARGS
        null, null, null, "1"); // GROUP BY, HAVING, ORDER BY, LIMIT
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      if(cursor.getColumnCount() > 0) {
        body = cursor.getString(1);
      }

      cursor.close();
    }
    return body;
  }
}
