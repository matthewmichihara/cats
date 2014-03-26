package com.chojima;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class MainActivity extends Activity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImgurService imgur;
    private StaggeredGridView grid;
    private ImagesAdapter adapter;
    private boolean hasRequestedMore;
    private int currentPage = 0;
    private PullToRefreshLayout pullToRefreshLayout;
    private List<Image> images = new ArrayList<Image>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = (StaggeredGridView) findViewById(R.id.grid);
        grid.setOnScrollListener(this);
        grid.setOnItemClickListener(this);

        RestAdapter restAdapter = new RestAdapter.Builder().setServer("https://api.imgur.com").build();
        imgur = restAdapter.create(ImgurService.class);

        View header = LayoutInflater.from(this).inflate(R.layout.grid_header, grid, false);
        grid.addHeaderView(header);

        imgur.getImages(Config.SUBREDDIT, currentPage, new Callback<ImageSearchResponse>() {
            @Override public void success(ImageSearchResponse imageSearchResponse, Response response) {
                images.addAll(imageSearchResponse.getImages());
                adapter = new ImagesAdapter(MainActivity.this, images);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override public void failure(RetrofitError retrofitError) {
                Log.e(TAG, "Error", retrofitError);
            }
        });

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        ActionBarPullToRefresh.from(this).options(Options.create().refreshOnUp(true).minimize(5000).build()).allChildrenArePullable().listener(this).setup(pullToRefreshLayout);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            // prepare for surgery
            ScalpelDrawer scalpelDrawer = new ScalpelDrawer(this);
            scalpelDrawer.wrapInside(this);
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!hasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                hasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Image image = (Image) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(ImageActivity.EXTRA_IMAGE_LINK, image.getLink());
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_down, 0);
    }

    @Override public void onRefreshStarted(View view) {
        currentPage = 0;
        imgur.getImages(Config.SUBREDDIT, currentPage, new Callback<ImageSearchResponse>() {
            @Override public void success(ImageSearchResponse imageSearchResponse, Response response) {
                images.clear();
                images.addAll(imageSearchResponse.getImages());
                adapter.notifyDataSetChanged();
                pullToRefreshLayout.setRefreshComplete();
            }

            @Override public void failure(RetrofitError retrofitError) {
                Log.e(TAG, "Error", retrofitError);
            }
        });
    }

    private void onLoadMoreItems() {
        imgur.getImages(Config.SUBREDDIT, ++currentPage, new Callback<ImageSearchResponse>() {
            @Override public void success(ImageSearchResponse imageSearchResponse, Response response) {
                images.addAll(imageSearchResponse.getImages());
                adapter.notifyDataSetChanged();
                hasRequestedMore = false;
            }

            @Override public void failure(RetrofitError retrofitError) {
                Log.e(TAG, "Error", retrofitError);
            }
        });
    }
}
