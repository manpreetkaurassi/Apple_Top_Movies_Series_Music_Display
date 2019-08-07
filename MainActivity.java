package com.example.topapps;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// TOP MOVIES
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Inside1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = (ListView) findViewById(R.id.xmlListView);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml");
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String feedUrl = null;

        switch(id){
            case R.id.menuMovie:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";
                break;

            case R.id.menuSeries:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml";
                break;
            case R.id.menuSongs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml";
                break;
            default: super.onOptionsItemSelected(item);
        }
        downloadUrl(feedUrl);
        return true;
    }
    
    private void downloadUrl(String feedUrl){
        Log.d(TAG, "downloadUrl: Inside");
        DataDownload dataDownload = new DataDownload();
        dataDownload.execute(feedUrl);

    }

    private class DataDownload extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
            ParseApplication parseApplication = new ParseApplication();
            parseApplication.parse(s);

//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
//                    MainActivity.this, R.layout.list_item,parseApplication.getApplications());
//            listApps.setAdapter(arrayAdapter);

        FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.lists_record,parseApplication.getApplications());
            listApps.setAdapter(feedAdapter);

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: The URL is " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            Log.d(TAG, "doInBackground: RSS feed is " + rssFeed);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error Downloading");
            }
            return rssFeed;
        }


        private String downloadXML(String URLConnect) {
            StringBuilder xmlResult = new StringBuilder();
            try {
                Log.d(TAG, "downloadXML: " + URLConnect);
                URL url = new URL(URLConnect);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int respone = connection.getResponseCode();
                Log.d(TAG, "downloadXML: Response code is" + respone);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.d(TAG, "doInBackground: Malformed URL" + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: IOException" + e.getMessage());
            } catch (SecurityException e) {
                Log.d(TAG, "downloadXML: Security permission missing" + e.getMessage());
            }

            return xmlResult.toString();
        }
    }

}
