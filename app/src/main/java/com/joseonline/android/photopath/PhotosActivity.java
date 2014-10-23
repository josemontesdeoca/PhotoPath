package com.joseonline.android.photopath;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends Activity {
    private static final String CLIENT_ID = "345460d55da24204af1b3a59d25e0c42";

    private ListView lvPhotos;

    private ArrayList<Photo> photos;
    private PhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        photos = new ArrayList<Photo>();
        aPhotos = new PhotosAdapter(this, photos);
        lvPhotos.setAdapter(aPhotos);
        
        fetchPopularPhotos();
    }

    /**
     * https://api.instagram.com/v1/media/popular?client_id=345460d55da24204af1b3a59d25e0c42
     *
     * Response:
     * "data" => [x] => "user" => "username"
     * "data" => [x] => "caption" => "text"
     * "data" => [x] => "images" => "standar_resolution" => "url"
     * "data" => [x] => "images" => "standar_resolution" => "height"
     * "data" => [x] => "likes" => "count"
     */
    private void fetchPopularPhotos() {
        // Setup the popular URL endpoit
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        // Trigger the network request
        client.get(popularUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i =0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        Photo photo = new Photo();
                        photo.setUsername(photoJSON.getJSONObject("user").getString("username"));
                        if (!photoJSON.isNull("caption")) {
                            photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                        }
                        photo.setImageUrl(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        photo.setLikeCounts(photoJSON.getJSONObject("likes").getInt("count"));
                        photos.add(photo);
                    }
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    // JSON parsed is invalid
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("ERROR", String.valueOf(statusCode));
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
