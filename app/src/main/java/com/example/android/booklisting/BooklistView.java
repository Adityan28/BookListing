package com.example.android.booklisting;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by user on 06-07-2017.
 */

public class BooklistView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Booklists>>{
    private static final int BOOK_LOADER_ID = 1;
    private static final String BOOK_LIST_URL = "https://www.googleapis.com/books/v1/volumes";
    private BooklistAdapter mAdapter;
    private String mQuery;

    @Override
    public Loader<List<Booklists>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query_var = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key),
                                    getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(BOOK_LIST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q",mQuery);
        uriBuilder.appendQueryParameter("maxResults", "40");

        return new BooklistLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Booklists>> loader, List<Booklists> data) {
        View loadingIndicator = findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);

        TextView emptyView=(TextView)findViewById(R.id.empty_state_textview);
        emptyView.setText(R.string.no_books);
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Booklists>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        Intent listviewIntent = getIntent();
        Bundle b = listviewIntent.getExtras();
        if(b!=null){
            mQuery=(String)b.get("location");
        }
        TextView emptyView = (TextView) findViewById(R.id.empty_state_textview);
        ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = check.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet_connection);
        }
        mAdapter = new BooklistAdapter(this, new ArrayList<Booklists>());
        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setEmptyView(emptyView);

        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booklists booklists = mAdapter.getItem(position);
                Uri uri = Uri.parse(booklists.getmInfoLink());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

    }

    public static class BooklistLoader extends AsyncTaskLoader<List<Booklists>>{
        private String mUrl;
        public BooklistLoader(Activity context , String urls){
            super(context);
            mUrl=urls;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Booklists> loadInBackground() {
            if (mUrl == null) {
                return null;
            }
            List<Booklists> result = QueryUtils.fetchBookData(mUrl);
            return result;
        }
    }
}
