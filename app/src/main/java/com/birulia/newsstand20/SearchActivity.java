package com.birulia.newsstand20;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.birulia.newsstand20.adapters.NewsArrayAdapter;
import com.birulia.newsstand20.fragments.FilterDialogFragment;
import com.birulia.newsstand20.models.NewsArticle;
import com.birulia.newsstand20.net.NewsClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private ArrayList<NewsArticle> newsArticles;
    private NewsArrayAdapter newsAdapter;
    private RecyclerView rvNewsArticles;
    private static final Integer NUMBER_OF_COLUMNS = 3;
    SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_bar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        newsArticles = new ArrayList<>();
        newsAdapter = new NewsArrayAdapter(this, newsArticles);
        rvNewsArticles = (RecyclerView) findViewById(R.id.rvNewsArticles);
        // Attach the adapter to the recyclerview to populate items
        rvNewsArticles.setAdapter(newsAdapter);
        // Set layout manager to position the items
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(NUMBER_OF_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        rvNewsArticles.setHasFixedSize(true);
        rvNewsArticles.setLayoutManager(gridLayoutManager);

        // Add the scroll listener
        rvNewsArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                fetchNews(page);
            }
        });

        mSettings = getSharedPreferences(Constants.SHARED_SEARCH_SETTINGS, 0);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("q", "");
        editor.putString("start_date", "");
        editor.putString("sort", "");
        editor.putString("news_deck", "");
        editor.apply();

        fetchNews(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Set up Search Action
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSettings = getSharedPreferences(Constants.SHARED_SEARCH_SETTINGS, 0);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString("q", query);
                editor.apply();
                newsArticles.clear();
                newsAdapter.notifyDataSetChanged();
                fetchNews(0);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Set up Filter Action
        MenuItem filterItem = menu.findItem(R.id.action_filter);
        MenuItemCompat.getActionView(filterItem).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // launch fragment
                showFilterDialog();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance("Filter Options");
        filterDialogFragment.setCancelable(false);
        filterDialogFragment.show(fm, "fragment_filter");
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchNews(Integer page) {
        NewsClient client = new NewsClient();
        String query = mSettings.getString("q", "");
        String startDate = mSettings.getString("start_date", "");
        String sortOrder = mSettings.getString("sort", "");
        String newsDeck = mSettings.getString("news_deck", "");
        client.getNews(query, page, startDate, sortOrder, newsDeck, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable throwable) {
                Log.e("ERROR", res);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {

                try {
                    JSONObject jsonObj = new JSONObject(res);
                    JSONArray docs;
                    if(jsonObj != null) {
                        // Get the docs json array
                        docs = jsonObj.getJSONObject("response").getJSONArray("docs");
                        Integer start_index = newsArticles.size();
                        newsArticles.addAll(NewsArticle.fromJSONArray(docs));
                        newsAdapter.notifyItemRangeInserted(start_index, newsArticles.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        // dialog was dismissed apply filters
        newsArticles.clear();
        newsAdapter.notifyDataSetChanged();
        fetchNews(0);
    }
}
