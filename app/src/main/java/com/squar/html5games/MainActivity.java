package com.squar.html5games;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://html5-games-experiment.appspot.com/";
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Make an object from the recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        // Attach a layout manager to the recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Instantiate the adapter with the data -list of users- from getData method
        gameRecyclerViewAdapter = new GameRecyclerViewAdapter(this,recyclerView);

        // Attach the recyclerView with the adapter
        recyclerView.setAdapter(gameRecyclerViewAdapter);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(() -> fetchGameList());

        fetchGameList();
    }


    public void fetchGameList(){

        Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build();

        MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);

        Observable<Items> games = apiService.gameList();

        games
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(g -> {
                gameRecyclerViewAdapter.addItems(g.getItems());
                mWaveSwipeRefreshLayout.setRefreshing(false);
                }, e -> Log.e(MainActivity.class.getSimpleName(), e.getMessage()));
    }
}
