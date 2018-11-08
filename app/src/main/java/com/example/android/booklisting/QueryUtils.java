package com.example.android.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    static Bitmap bmp;
    public static final String TAG = "";
    private QueryUtils() {
    }

    public static List<Booklists> fetchBookData(String requestUrl) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse=makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Booklists> booklists = extractFeatureFromJson(jsonResponse);

        return booklists;
    }

    public static URL createUrl(String requestUrl){
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<Booklists> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Booklists> booklists = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            if(baseJsonResponse.has("items")){
                JSONArray booklistArray = baseJsonResponse.getJSONArray("items");

                for (int i = 0; i < booklistArray.length(); i++) {

                    JSONObject currentbook = booklistArray.getJSONObject(i);

                    JSONObject volumeInfo = currentbook.getJSONObject("volumeInfo");

                    String infoLink = volumeInfo.getString("infoLink");

                    String title = volumeInfo.getString("title");

                    String[] authors;
                    if(volumeInfo.has("authors")){
                        JSONArray authorArray = volumeInfo.getJSONArray("authors");

                        String author[] = new String[authorArray.length()];
                        for(int j=0;j<authorArray.length();j++){
                            author[j] = authorArray.getString(j);
                        }
                        authors=author;
                    }
                    else {
                        authors=new String[]{"Authors_N/A"};
                    }


                    JSONObject imagelinks = volumeInfo.getJSONObject("imageLinks");
                    String thumbnail = imagelinks.getString("thumbnail");
                    try {
                        URL url = new URL(thumbnail);
                        try {
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    Booklists booklists1 = new Booklists(bmp,title,authors,infoLink);

                    booklists.add(booklists1);
                }
            }
            else {
                String[] author_not_available =new String[]{"Author_N/A"};
                Booklists booklists1 = new Booklists(null,"Book Name_N/A",author_not_available,"Infolink_N/A");
                booklists.add(booklists1);
            }



        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the Booklist JSON results", e);
        }

        return booklists;
    }
}
