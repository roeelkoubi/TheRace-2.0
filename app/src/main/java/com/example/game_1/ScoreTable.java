package com.example.game_1;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import models.Utils;
import adapters.ScoreTableAda;
import database.MangerDBM;
import models.Coordinate;
import models.OnItemClickListener;
import models.OnScoreResponse;
import models.Score;

public class ScoreTable extends FragmentActivity implements OnScoreResponse, OnItemClickListener, OnMapReadyCallback {

    private RecyclerView scoreRv;
    private ScoreTableAda scoreRvAdapter;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_table);
        scoreRv = findViewById(R.id.scoreRv);
        scoreRv.setLayoutManager(new LinearLayoutManager(this));
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment!=null)
            mapFragment.getMapAsync(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MangerDBM.getInstance().fetchScores(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void consumeScores(List<Score> scoreList) {
        scoreRvAdapter = new ScoreTableAda(scoreList,this);
        scoreList.sort((o1, o2) -> Long.compare(o2.getTimeLasted(),o1.getTimeLasted()));
        scoreRv.setAdapter(scoreRvAdapter);
    }
    private void moveCamera(Score score) {
        Log.d("Method","moveCamera,coordinte: " + score.getCoordinate());
        LatLng position = new LatLng(score.getCoordinate().getLatitude(),
                score.getCoordinate().getLongtitude());
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(Utils.getTimeString(score.getTimeLasted())));
        map.moveCamera(CameraUpdateFactory.
                newLatLngZoom(position,1));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Coordinate coordination = new Coordinate(5.20,7.2);
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordination.getLatitude(),coordination.getLongtitude()),0));
    }

    @Override
    public void Clicked(Score score) {
        moveCamera(score);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

