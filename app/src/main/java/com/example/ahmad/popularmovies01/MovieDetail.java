package com.example.ahmad.popularmovies01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieDetail extends ActionBarActivity {
    ArrayList<String> youtubeKeys;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.movie_title);
        title.setText(intent.getStringExtra("title"));
        TextView overview = (TextView) findViewById(R.id.movie_overview);
        overview.setText(intent.getStringExtra("overview"));
        TextView rate = (TextView) findViewById(R.id.movie_rating);
        rate.setText(intent.getStringExtra("vote_average"));
        TextView date = (TextView) findViewById(R.id.movie_release_date);
        date.setText(intent.getStringExtra("release_date"));
        ImageView poster = (ImageView) findViewById(R.id.image_poster);
        Picasso.with(getApplication()).load(intent.getStringExtra("poster_path")).into(poster);

     /*   FetchTrailersData fetchTrailersData = new FetchTrailersData();
        fetchTrailersData.execute(intent.getStringExtra("id"));*/
        //for (int i=0;i<)
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

  /*  public class FetchTrailersData extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String trailersList = null;

            String sort_by = "popularity.desc";

            try {
                final String MOVIES_URL_BASE =
                        "https://api.themoviedb.org/3/movie/"+params[0]+"/videos?";
                final String FORMAT_PARAM = "api_key";


                Uri uri = Uri.parse(MOVIES_URL_BASE).buildUpon()
                        .appendQueryParameter(FORMAT_PARAM, "fa324076f75b84a267d88a4b41ea2ed8")
                        .build();
                URL url = new URL(uri.toString());

                Log.v("TAG ALL", "Built URI " + uri.toString());

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
                trailersList = buffer.toString();

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
                return gettrailers(trailersList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            listView = (ListView) findViewById(R.id.list_id_in_detail);
            setListViewHeightBasedOnChildren(listView);
            listView.setAdapter(new TrailersAdapter(getApplicationContext(), arrayList));

            super.onPostExecute(arrayList);
        }

        public ArrayList<String> gettrailers(String trailersList) throws JSONException {
            JSONObject moviesJson = new JSONObject(trailersList);
            JSONArray jsonArray = moviesJson.getJSONArray("results");
            youtubeKeys = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                youtubeKeys.add(jsonObject.getString("key"));
            }
            return youtubeKeys;
        }
    }
    */

}
