package com.example.game_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Thread thread= new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent mainIntent= new Intent(Activity_Splash.this,MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}


