package com.example.ahmad.popularmovies01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;


public class AllMovies extends ActionBarActivity {
    ListView listView;
    ArrayList<Hashtable<String, String>> hashtableArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_movies, menu);
        return true;
    }

    @Override
    protected void onResume() {
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovies extends AsyncTask<Void, Void, ArrayList<Hashtable<String, String>>> {

        @Override
        protected ArrayList<Hashtable<String, String>> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesList = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String type = prefs.getString("sort", getString(R.string.popularity));
            String sort_by = type + ".desc";

            try {
                final String MOVIES_URL_BASE =
                        "https://api.themoviedb.org/3/discover/movie?";
                final String Sort_PARAM = "sort_by";
                final String FORMAT_PARAM = "api_key";

                Uri uri = Uri.parse(MOVIES_URL_BASE).buildUpon()
                        .appendQueryParameter(Sort_PARAM, sort_by)
                        .appendQueryParameter(FORMAT_PARAM, "fa324076f75b84a267d88a4b41ea2ed8")
                        .build();
                URL url = new URL(uri.toString());

                Log.v("TAG All Movies", " Built URI " + uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesList = buffer.toString();

                // Log.v("TAG ALL", "Forecast string: " + moviesList);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (ProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("TAG ALL", "Error closing stream", e);
                    }
                }
            }

            try {
                return getmovies(moviesList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Hashtable<String, String>> arrayList) {
            ArrayList<String> imagesList = new ArrayList<>();

            for (int i = 0; i < arrayList.size(); i++) {
                imagesList.add(arrayList.get(i).get("poster_path"));
            }


            GridView gridview = (GridView) findViewById(R.id.gridview);
            gridview.setAdapter(new ImageAdapter(getApplication(), imagesList));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(AllMovies.this, MovieDetail.class);
                    intent.putExtra("title", hashtableArrayList.get(position).get("title"));
                    intent.putExtra("overview", hashtableArrayList.get(position).get("overview"));
                    intent.putExtra("release_date", hashtableArrayList.get(position).get("release_date"));
                    intent.putExtra("vote_average", hashtableArrayList.get(position).get("vote_average"));
                    intent.putExtra("poster_path", hashtableArrayList.get(position).get("poster_path"));
                    intent.putExtra("id", hashtableArrayList.get(position).get("id"));
                    startActivity(intent);
                }
            });


            super.onPostExecute(arrayList);
        }

        public ArrayList getmovies(String moviesString) throws JSONException {
            JSONObject moviesJson = new JSONObject(moviesString);
            JSONArray jsonArray = moviesJson.getJSONArray("results");
            hashtableArrayList = new ArrayList<>();
            String s = "http://image.tmdb.org/t/p/w500/";
            for (int i = 0; i < jsonArray.length(); i++) {
                Hashtable<String, String> movieData = new Hashtable<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                movieData.put("poster_path", s.concat(jsonObject.getString("poster_path")));
                movieData.put("title", jsonObject.getString("title"));
                movieData.put("overview", jsonObject.getString("overview"));
                String s1 = jsonObject.getString("release_date");
                String s1array[] = s1.split("-");
                movieData.put("release_date", s1array[0]);
                movieData.put("vote_average", jsonObject.getString("vote_average"));
                movieData.put("id", jsonObject.getInt("id") + "");
                hashtableArrayList.add(movieData);
            }
            return hashtableArrayList;
        }
    }
}
