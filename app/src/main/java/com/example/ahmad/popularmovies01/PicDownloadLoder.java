package com.example.ahmad.popularmovies01;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;

import java.io.File;

/**
 * Created by ahmad on 10/09/2015.
 */
public class PicDownloadLoder extends AsyncTaskLoader<Void> {
    Context context;

    public PicDownloadLoder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public Void loadInBackground() {


        File direct = new File(Environment.getExternalStorageDirectory()
                + "/popularMovies");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        SharedPreferences sharedPreferences =
                context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
        Uri downloadUri = Uri.parse(sharedPreferences.getString("poster_path", "h" +
                "ttp://img2.wikia.nocookie.net/__cb20130511180903/legendmarielu/ima" +
                "ges/b/b4/No_image_available.jpg"));
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir(direct.getAbsolutePath(),
                        sharedPreferences.getString("id", "Unknown") + ".jpg");

        mgr.enqueue(request);
        return null;
    }
}
