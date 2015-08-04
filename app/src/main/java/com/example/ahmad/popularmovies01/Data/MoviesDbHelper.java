package com.example.ahmad.popularmovies01.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmad on 28/07/2015.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " +
                MoviesContract.PopularEntry.TABLE_NAME + " (" +
                MoviesContract.PopularEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.PopularEntry.COLUMN_MOVIE_TITLE + " TEXT UNIQUE NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_MOVIE_IMAGE_POSTER + " TEXT NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                MoviesContract.PopularEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT " +
                " );";
        final String SQL_CREATE_LATEST_TABLE = "CREATE TABLE " +
                MoviesContract.LatestEntry.TABLE_NAME + " (" +
                MoviesContract.LatestEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.LatestEntry.COLUMN_MOVIE_TITLE + " TEXT UNIQUE NOT NULL, " +
                MoviesContract.LatestEntry.COLUMN_MOVIE_IMAGE_POSTER + " TEXT NOT NULL, " +
                MoviesContract.LatestEntry.COLUMN_MOVIE_OVERVIEW + " TEXT , " +
                MoviesContract.LatestEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                MoviesContract.LatestEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT " +
                " );";

        db.execSQL(SQL_CREATE_LATEST_TABLE);
        db.execSQL(SQL_CREATE_POPULAR_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.PopularEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.LatestEntry.TABLE_NAME);
        onCreate(db);
    }
}
