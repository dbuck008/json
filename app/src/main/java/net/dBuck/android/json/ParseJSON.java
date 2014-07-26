package net.dBuck.android.json;

import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ParseJSON extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Never use this method of connecting for a real app - only for tutorial
        // Instead, always ensure that network access is done in a background thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        setContentView(R.layout.activity_parse_json);
        String readTwitterFeed = readTwitterFeed();
        try {
            JSONArray jsonArray = new JSONArray(readTwitterFeed);
            Log.i(ParseJSON.class.getName(),
                    "Number of entries " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.i(ParseJSON.class.getName(), jsonObject.getString("text"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readTwitterFeed() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(ParseJSON.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void writeJSON() {
        JSONObject object = new JSONObject();
        try{
            object.put("name", "John Doe");
            object.put("score", new Integer(200));
            object.put("current", new Double(152.32));
            object.put("nickname", "Hacker");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parse_json, menu);
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
