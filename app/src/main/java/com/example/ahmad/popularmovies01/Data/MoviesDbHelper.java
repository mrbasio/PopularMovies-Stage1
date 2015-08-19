package com.example.ahmad.popularmovies01.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ahmad.popularmovies01.Objects.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmad on 28/07/2015.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "popular";

    public static final String COLUMN_MOVIE_TITLE = "movie_title";
    public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
    public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
    public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String COLUMN_MOVIE_IMAGE_POSTER = "movie_image_poster";
    public static final String COLUMN_MOVIE_STRING_ID = "movie_string_id";
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
                TABLE_NAME + " (" +
                "id" + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_STRING_ID + " TEXT UNIQUE NOT NULL, " +
                COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                COLUMN_MOVIE_RELEASE_DATE + " TEXT " +
                COLUMN_MOVIE_IMAGE_POSTER + " TEXT NOT NULL, " +
                COLUMN_MOVIE_TITLE + " TEXT UNIQUE NOT NULL, " +
                COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                " );";

        db.execSQL(SQL_CREATE_POPULAR_TABLE);
    }

    /**
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

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6));
                // Adding contact to list
                movies.add(movie);
            } while (cursor.moveToNext());
        }

        // return contact list
        return movies;
    }

    public Movie getMovie(String name) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{COLUMN_MOVIE_TITLE},
                COLUMN_MOVIE_TITLE + "=?", new String[]{name}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Movie movie = new Movie(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6));
        return movie;
    }

    public void addMovie(Movie movie) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(COLUMN_MOVIE_IMAGE_POSTER, movie.getPoster_path());
        contentValues.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVote_average());
        contentValues.put(COLUMN_MOVIE_STRING_ID, movie.getDb_id());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteMovie(Movie movie) {

    }
}
