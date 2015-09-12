package com.example.ahmad.popularmovies01.Data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by ahmad on 07/08/2015.
 */
public class MoviesProvider extends ContentProvider {
    MoviesDbHelper moviesDbHelper;

    private static final int movie = 10;
    private static final int movies = 20;

    private static final int addMovie = 80;
    private static final int getAllMoviesCursor = 90;
    private static final int getAllMoviesList = 30;
    private static final int deleteAll = 40;
    private static final int deleteMovie = 50;
    private static final int ifExcite = 60;
    private static final int getMovie = 70;

    public static final String AUTHORITY = "com.example.ahmad.popularmovies01";

    public static final String BASE_PATH = "popular";
    public static final String BASE_PATH_FAV = "favList";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + '/'
            + BASE_PATH_FAV);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/movies";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/movie";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAV, movies);
        //sURIMatcher.addURI(AUTHORITY, BASE_PATH, getAllMoviesCursor);
        //sURIMatcher.addURI(AUTHORITY, BASE_PATH, deleteAll);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAV + "/*", movie);
        //sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/*", deleteMovie);
        //sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/*", getMovie);
        //sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/*", ifExcite);
    }


    @Override
    public boolean onCreate() {
        moviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(MoviesDbHelper.FAV_TABLE_NAME);


        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case movies:
                break;
            case movie:
                // adding the ID to the original query
                queryBuilder.appendWhere(moviesDbHelper.COLUMN_MOVIE_STRING_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        //String selectQuery = "SELECT  * FROM " + MoviesDbHelper.FAV_TABLE_NAME;
        // Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = moviesDbHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case movies:
                id = sqlDB.insert(MoviesDbHelper.FAV_TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH_FAV + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = moviesDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case movies:
                rowsDeleted = sqlDB.delete(MoviesDbHelper.TABLE_NAME, null,
                        null);
                break;
            case movie:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsDeleted = sqlDB.delete(MoviesDbHelper.TABLE_NAME,
                            MoviesDbHelper.COLUMN_MOVIE_STRING_ID + "=" + id,
                            null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void checkColumns(String[] projection) {
        String[] available = {MoviesDbHelper.COLUMN_MOVIE_STRING_ID,
                MoviesDbHelper.COLUMN_MOVIE_IMAGE_POSTER, MoviesDbHelper.COLUMN_MOVIE_OVERVIEW,
                MoviesDbHelper.COLUMN_MOVIE_RELEASE_DATE, MoviesDbHelper.COLUMN_MOVIE_TITLE,
                MoviesDbHelper.COLUMN_MOVIE_VOTE_AVERAGE};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }


}
