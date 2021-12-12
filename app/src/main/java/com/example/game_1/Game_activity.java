package com.example.game_1;

import static com.example.game_1.MainActivity.GAME_MODE;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import models.Utils;
import database.MangerDBM;
import models.ActionD;
import models.Coordinate;
import models.Score;

public class Game_activity extends AppCompatActivity implements View.OnClickListener, LocationListener, SensorEventListener {


    private ImageButton left_arrow;
    private ImageButton right_arrow;
    private ImageView heart1, heart2, heart3;
    private ImageView spongebob;
    private ImageView[] jellyfish = new ImageView[6];
    private ImageView[] krabbyPatty = new ImageView[6];
    private int krabbyPattyNumbers = 0;
    private int theCurrentLine = 3;
    private int hearts = 3;
    private int length;
    private TextView timerTv;
    private Coordinate locationCoordinate = new Coordinate(0, 0);
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener SensorEventListener;
    private int[] jellyfishLines = {0, 0, 0, 0, 0, 0};
    private int[] krabbyPattyLines = {0, 0, 0, 0, 0, 0};
    private Timer jellyfishTimer, timerGame = new Timer(), stopWatch = new Timer();
    private boolean checkIfCanMove = true;
    private Random rando1 = new Random();
    private Random rando2 = new Random();
    private Random rando3= new Random();
    private float xOffset, yOffset;
    private LocationManager locationManager;
    private Long startTime = null;
    private MediaPlayer sound1,sound2,sound3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        findViews();
        hideSystemUI();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        xOffset = (displayMetrics.widthPixels) / 6f ;
        yOffset = displayMetrics.heightPixels + spongebob.getHeight();
        sound1 = sound1.create(this, R.raw.themesong);
        sound1.setLooping(true);
        sound1.start();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (getIntent() != null) {
            Intent intent = getIntent();
            String mode = intent.getStringExtra(GAME_MODE);
            if (mode.equals("Sensors")) {
                SensorsInit();
                SensorEventListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        float x = event.values[0];
                        if (x <= -6) {
                            ActionD action = ActionD.RIGHT;
                            move(action);
                        } else if (x >= 6) {
                           ActionD action = ActionD.LEFT;
                            move(action);
                        }
                    }
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    }
                };
                right_arrow.setVisibility(View.INVISIBLE);
                left_arrow.setVisibility(View.INVISIBLE);
            } else {
               right_arrow.setOnClickListener(this);
               left_arrow.setOnClickListener(this);
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    private void SensorsInit() {
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    public boolean CheckSensor(int sensorType){
        return (sensorManager.getDefaultSensor(sensorType)!=null);
    }
    private void JellyFishTimerStart() {
        jellyfishTimer = new Timer();
        showMessage("They are coming!");
       jellyfishTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                rando2=new Random();
                int random_coins_lane= rando2.nextInt(krabbyPatty.length);
                int random_lane =rando1.nextInt(jellyfish.length);
                if (startTime == null) {
                    startTime = System.currentTimeMillis();
                    stopWatch.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            String timeString = Utils.getTimeString(getTimeElapsed());
                            runOnUiThread(() -> {
                                timerTv.setText(timeString);
                            });
                        }
                    }, 1, 1);
                }
                if (krabbyPattyLines[random_coins_lane]==1) {
                    return;
                }
                if (jellyfishLines[random_lane] == 1) {
                    return;
                }
               krabbyPattyLines[random_coins_lane]=1;
               jellyfishLines[random_lane] = 1;
                ImageView random_coin = krabbyPatty[random_coins_lane];
                ImageView random_dynamite = jellyfish[random_lane];
                runOnUiThread(() -> {
                    random_coin.setY(-200);
                    random_coin.setVisibility(View.VISIBLE);
                    random_coin.animate().y(yOffset).setUpdateListener((ValueAnimator animation) -> {
                        checkKabbyPattyHit(random_coins_lane, random_coin);
                    }).setDuration(2200).start();
                    random_dynamite.setY(-200);
                    random_dynamite.setVisibility(View.VISIBLE);
                    random_dynamite.animate()
                            .y(yOffset)
                            .setUpdateListener(animation -> checkHit(random_lane, random_dynamite))
                            .setDuration(2200)
                            .start();
                });

            }
        }, 2000, 1000);
    }
    private void checkKabbyPattyHit(int lane, ImageView coin) {
        int[] spongebob_loc = new int[2];
        int[] kabbyPatty_loc = new int[2];
        spongebob.getLocationOnScreen(spongebob_loc);
        coin.getLocationOnScreen(kabbyPatty_loc);

        if (kabbyPatty_loc[1] >= yOffset) {
            coin.setVisibility(View.INVISIBLE);
            krabbyPattyLines[lane] = 0;
        } else if (lane == theCurrentLine) {
            if (Math.abs(spongebob_loc[1] - kabbyPatty_loc[1]) < 20) {
               krabbyPattyNumbers++;
                sound2 = MediaPlayer.create(this, R.raw.collecteffect);
                sound2.start();
                for (int i = 0; i < krabbyPatty.length; i++) {
                    krabbyPattyLines[theCurrentLine] = 0;
                    krabbyPatty[theCurrentLine].setVisibility(View.INVISIBLE);
                    krabbyPatty[theCurrentLine].setY(-200f);
                }
            }
        }
    }
    private Long getTimeElapsed() {
        long timeElapsed = System.currentTimeMillis() - startTime;
        return timeElapsed;
    }
    private void checkHit(int lane, ImageView jellyFish) {
        int[] spongebob_location = new int[2];
        int[] jellyfish_location = new int[2];
        spongebob.getLocationOnScreen(spongebob_location);
       jellyFish.getLocationOnScreen(jellyfish_location);
        if (jellyfish_location[1] >= yOffset) {
          jellyFish.setVisibility(View.INVISIBLE);
            jellyFish.setY(-200f);
           jellyfishLines[lane] = 0;
        } else if (lane == theCurrentLine) {
            if (Math.abs(spongebob_location[1] - jellyfish_location[1]) < 20) {
               spongebob.setImageResource(R.drawable.explosion_fire);
               sound1.pause();
                length = sound1.getCurrentPosition();
                sound3 = MediaPlayer.create(this, R.raw.spongebobcrashsound);
                sound3.start();
                for (int i = 0; i < jellyfish.length; i++) {
                   jellyfishLines[i] = 0;
                   jellyfish[i].setVisibility(View.INVISIBLE);
                   jellyfish[i].setY(-200f);
                }
                checkIfCanMove = false;
               jellyfishTimer.cancel();
                if (hearts == 1) {
                    endGame();
                    return;
                } else if (hearts == 3) {
                   heart3.setImageResource(R.drawable.heartnotfill);
                } else if (hearts == 2) {
                   heart2.setImageResource(R.drawable.heartnotfill);
                } else if (hearts == 1) {
                   heart1.setImageResource(R.drawable.heartnotfill);
                }
                showMessage("oh no! Lives left: " + --hearts);
               timerGame.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> spongebob.setImageResource(R.drawable.spongebob));
                        checkIfCanMove = true;
                        sound1.seekTo(length);
                        sound1.start();
                        JellyFishTimerStart();
                    }
                }, 3000);
            }
        }
    }
    private void endGame() {
        jellyfishTimer.cancel();
        timerGame.cancel();
       sound1.stop();
        showMessage("All lives ran out,you lasted: ...");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
        Score score = new Score(getTimeElapsed()+(krabbyPattyNumbers * 10000L),
                locationCoordinate);
        MangerDBM.getInstance().addNewScore(score, unused -> Log.d("addNew Score", "Successfuly added new score")
                , e -> Log.d("addNew Score", e.getMessage()));
    }
    private void showMessage(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }
    private void findViews() {
       spongebob = findViewById(R.id.spongebob);
        for (int d_index = 0; d_index < 6; d_index++) {
            switch (d_index) {
                case 0:
                   krabbyPatty[d_index] = findViewById(R.id.patty1);
                   jellyfish[d_index] = findViewById(R.id.jellyfish1);
                    break;
                case 1:
                   jellyfish[d_index] = findViewById(R.id.jellyfish2);
                    krabbyPatty[d_index] = findViewById(R.id.patty2);
                    break;
                case 2:
                    jellyfish[d_index] = findViewById(R.id.jellyfish3);
                   krabbyPatty[d_index] = findViewById(R.id.patty3);
                    break;
                case 3:
                    krabbyPatty[d_index] = findViewById(R.id.patty4);
                  jellyfish[d_index] = findViewById(R.id.jellyfish4);
                    break;
                case 4:
                   krabbyPatty[d_index] = findViewById(R.id.patty5);
                    jellyfish[d_index] = findViewById(R.id.jellyfish5);
                    break;
                case 5:
                    krabbyPatty[d_index] = findViewById(R.id.patty6);
                   jellyfish[d_index] = findViewById(R.id.jellyfish6);
                    break;
            }
            jellyfish[d_index].setVisibility(View.INVISIBLE);
            krabbyPatty[d_index].setVisibility(View.INVISIBLE);
        }
        left_arrow = findViewById(R.id.leftArrow);
       right_arrow = findViewById(R.id.rightArrow);
       heart1 = findViewById(R.id.heart1);
       heart2 = findViewById(R.id.heart2);
       heart3 = findViewById(R.id.heart3);
       timerTv = findViewById(R.id.TimerScore);
    }
    public void hideSystemUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        JellyFishTimerStart();
    }
    @Override
    protected void onPause() {
        super.onPause();
        endGame();
        finish();
        jellyfishTimer.cancel();
        sensorManager.unregisterListener(SensorEventListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(SensorEventListener,sensor,400);
        JellyFishTimerStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        jellyfishTimer.cancel();
        endGame();
        finish();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rightArrow) {
           ActionD action = ActionD.RIGHT;
            move(action);
        } else if (v.getId() == R.id.leftArrow) {
            ActionD action = ActionD.LEFT;
            move(action);
        }
    }
    private void move(ActionD directionAction) {
        if (directionAction == ActionD.RIGHT) {
            if (theCurrentLine == 5 || !checkIfCanMove) return;
            spongebob.animate().xBy(xOffset)
                    .setListener(spongebobListener())
                    .setDuration(100).start();
            theCurrentLine++;
        } else {
            if (theCurrentLine == 0 || !checkIfCanMove) return;
            spongebob.animate().xBy(-xOffset)
                    .setListener(spongebobListener())
                    .setDuration(100).start();
           theCurrentLine--;
        }
    }
    private Animator.AnimatorListener spongebobListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                checkIfCanMove = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
               checkIfCanMove = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                checkIfCanMove = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                checkIfCanMove = false;
            }
        };
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longtitue = location.getLongitude();
        this.locationCoordinate = new Coordinate(longtitue, latitude);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}