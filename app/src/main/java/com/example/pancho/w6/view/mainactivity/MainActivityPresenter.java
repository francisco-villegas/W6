package com.example.pancho.w6.view.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.pancho.w6.model.Movies;
import com.example.pancho.w6.util.CONSTANTS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    MainActivityContract.View view;
    private static final String TAG = "MainActivityPresenter";
    private Context context;

    @Override
    public void attachView(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void makeRestCall(String query, boolean force) {
        final Date currentTime = Calendar.getInstance().getTime();

        SharedPreferences prefs = context.getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
        Long longd = prefs.getLong(CONSTANTS.MY_PREFS_TIME_REST, -1);
        Date old_time;
        if(longd == -1) {
            old_time = currentTime;
        } else {
            old_time = new Date(longd);
        }

        Log.d(TAG, "onResponse: " + currentTime + " " + old_time);

        if(force || (currentTime.compareTo(old_time)>=0)) {
            OkHttpClient client = new OkHttpClient();
            HttpUrl url = new HttpUrl.Builder()
                    .scheme(CONSTANTS.BASE_SCHEMA_MOVIES)
                    .host(CONSTANTS.BASE_URL_MOVIES)
                    .addPathSegments(CONSTANTS.PATH_SEARCH_MOVIES)
                    .addQueryParameter("api_key", CONSTANTS.KEY_MOVIES )
                    .addQueryParameter("query", query )
                    .build();
            Log.i(TAG, "url: " + url.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Gson gson = new Gson();
                    String r = response.body().string();
                    try {
                        final Movies movies = gson.fromJson(r, Movies.class);

                        //UpdateUI
                        updateUIRest(movies);

                        if (movies.getResults() != null && movies.getResults().size() > 0) {
                            //Save cache
                            SharedPreferences.Editor editor = context.getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE).edit();
                            Date current_plus10 = new Date(currentTime.getTime() + CONSTANTS.TIME_UNTIL_NEXT_CALL);
                            Log.d(TAG, "onResponse: " + current_plus10);
                            editor.putLong(CONSTANTS.MY_PREFS_TIME_REST, current_plus10.getTime());

                            //Save json
                            editor.putString(CONSTANTS.MY_PREFS_JSON, r);
                            editor.commit();
                        }
                        else{
                            Toast.makeText(context, "Movie not found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.d(TAG, "onResponse: " + r);
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(context, "Data in cache", Toast.LENGTH_SHORT).show();

            //Get Json from cache
            Movies movies = MoviesFromCache();

            //UpdateUI
            updateUIRest(movies);
        }

    }

    private void updateUIRest(final Movies movies) {
        ((MainActivity) view).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (movies.getResults() != null && movies.getResults().size() > 0) {
                    view.sendResult(movies);
                } else {
                    Toast.makeText(context, "Movie not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Movies MoviesFromCache(){
        SharedPreferences prefs = context.getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CONSTANTS.MY_PREFS_JSON, null);
        Type type = new TypeToken<Movies>() {
        }.getType();
        Movies movies = gson.fromJson(json, type);

        return movies;
    }
}
