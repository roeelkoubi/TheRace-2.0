package com.example.game_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private Button btn_leaderBoard;
    private Button btn_Sensors_Mode;
    public static final String GAME_MODE = "GM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},0);
        }
        btn_leaderBoard = findViewById(R.id.tableScore);
        btn_Sensors_Mode=findViewById(R.id.SensorMode);
        btn_leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ScoreTable.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.playButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,Game_activity.class);
            intent.putExtra(GAME_MODE,"ordinary");
            startActivity(intent);
        });
        findViewById(R.id.SensorMode).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,Game_activity.class);
            intent.putExtra(GAME_MODE,"Sensors");
            startActivity(intent);
        });
    }
}