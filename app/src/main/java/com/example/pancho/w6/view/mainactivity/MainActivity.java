package com.example.pancho.w6.view.mainactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pancho.w6.R;
import com.example.pancho.w6.injection.mainactivity.DaggerMainActivityComponent;
import com.example.pancho.w6.model.Movies;
import com.example.pancho.w6.model.Result;
import com.example.pancho.w6.util.PaginationScrollListener;
import com.example.pancho.w6.view.secondView.SecondView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View, FirstAdapter.EventListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.ettoolbar)
    EditText ettoolbar;


    @BindView(R.id.recycler)
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;

    List<Result> results;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    MainActivityPresenter presenter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = -1; // -1 remove the limit of pages with -1 this should be > 0
    private int currentPage = PAGE_START;
    private int limit = 6;
    private FirstAdapter firstAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        setupDaggerComponent();
        presenter.attachView(this);
        presenter.setContext(getApplicationContext());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray));

        initRecyclerView();
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemAnimator = new DefaultItemAnimator();
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(itemAnimator);
        recycler.setHasFixedSize(true);
        recycler.setItemViewCacheSize(20);
        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recycler.addOnScrollListener(new PaginationScrollListener( layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private ArrayList<Result> fetchResultsSub(List<Result> results, int currentPage, int limit) {
        ArrayList<Result> amazonBooksub = new ArrayList<Result> (results.subList((currentPage-1)*limit,currentPage*limit));
        return amazonBooksub;
    }

    private void loadNextPage() {
        ArrayList<Result> resultsSub = fetchResultsSub(results,currentPage,limit);

        firstAdapter.removeLoadingFooter();
        isLoading = false;
        firstAdapter.addAll(resultsSub);
        firstAdapter.addLoadingFooter();
    }

    private void setupDaggerComponent() {
        DaggerMainActivityComponent.create().insert(this);
    }

    //create action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //options for action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                String query = ettoolbar.getText().toString();

                if (query.equals("")) {
                    Toast.makeText(this, "You have to type anything before press the search button", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.makeRestCall(query, true);
                }

                break;
        }
        return true;
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void sendResult(Movies movies) {
        this.results = movies.getResults();
        firstAdapter = new FirstAdapter(movies.getResults());
        recycler.setAdapter(firstAdapter);
        firstAdapter.notifyDataSetChanged();
    }

    @Override
    public void ItemClick(Result item) {
        Log.d(TAG, "ItemClick: ");

        SecondView fragment = SecondView.newInstance(String.valueOf(item.getId()));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frame, fragment, TAG);
        ft.addToBackStack(TAG);
        ft.commit();
    }
}
