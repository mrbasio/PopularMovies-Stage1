package com.example.ahmad.popularmovies01.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ahmad.popularmovies01.Objects.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmad on 28/07/2015.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "movies.db";

    public static final String TABLE_NAME = "popular";
    public static final String COLUMN_MOVIE_TITLE = "movie_title";
    public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
    public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
    public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String COLUMN_MOVIE_IMAGE_POSTER = "movie_image_poster";
    public static final String COLUMN_MOVIE_STRING_ID = "movie_string_id";

    public static final String FAV_TABLE_NAME = "favList";


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
                TABLE_NAME + " (" +
                "_id" + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_STRING_ID + " TEXT, " +
                COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                COLUMN_MOVIE_IMAGE_POSTER + " TEXT, " +
                COLUMN_MOVIE_TITLE + " TEXT , " +
                COLUMN_MOVIE_VOTE_AVERAGE + " REAL " +
                " );";

        final String SQL_CREATE_FAV_TABLE = "CREATE TABLE " +
                FAV_TABLE_NAME + " (" +
                "_id" + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_STRING_ID + " TEXT, " +
                COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                COLUMN_MOVIE_IMAGE_POSTER + " TEXT, " +
                COLUMN_MOVIE_TITLE + " TEXT , " +
                COLUMN_MOVIE_VOTE_AVERAGE + " REAL " +
                " );";

        db.execSQL(SQL_CREATE_POPULAR_TABLE);
        db.execSQL(SQL_CREATE_FAV_TABLE);
    }

    /**
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAV_TABLE_NAME);
        onCreate(db);
    }

    // Adding new movie
    public void addMovie(Movie movie, int type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_STRING_ID, movie.getId());
        values.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        values.put(COLUMN_MOVIE_IMAGE_POSTER, movie.getPoster_path());
        values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVote_average());

        // Inserting Row
        if (type == 0)
            db.insert(TABLE_NAME, null, values);
        else
            db.insert(FAV_TABLE_NAME, null, values);
        db.close(); // Closing database connection

        Log.d("DB", "insertion completed for " + movie.getTitle());
    }

    public Cursor getAllMoviesCursor(String type) {
        String selectQuery = "";
        if (!type.equals("favourite"))
            selectQuery = "SELECT  * FROM " + TABLE_NAME;
        else
            selectQuery = "SELECT  * FROM " + FAV_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    // Getting All Movies
    public List<Movie> getAllMovies(String type) {
        List<Movie> contactList = new ArrayList<Movie>();
        String selectQuery = "";
        // Select All Query
        if (type.equals("favourite"))
            selectQuery = "SELECT  * FROM " + FAV_TABLE_NAME;
        else
            selectQuery = "SELECT  * FROM " + TABLE_NAME;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6));
                // Adding contact to list
                contactList.add(movie);
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        // return movie list
        return contactList;
    }

    public void deleteAll(int type) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (type == 0)
            db.delete(TABLE_NAME, null, null);
        else
            db.delete(FAV_TABLE_NAME, null, null);
        db.close();
    }

    public void deleteMovie(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAV_TABLE_NAME, COLUMN_MOVIE_STRING_ID + "=" + id, null);
    }

    public boolean ifExcite(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + FAV_TABLE_NAME + " where "
                + COLUMN_MOVIE_STRING_ID + " = " + id;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public Movie getMovie(String id, int type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_MOVIE_STRING_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        Movie movie = new Movie(cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)
                , cursor.getString(6));
        cursor.close();
        // return contact
        return movie;
    }
}