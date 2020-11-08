package com.example.secondassignment2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Timer extends AppCompatActivity {
    private TextView textView;
    private Button start;
    private Button stop;
    private Button reset;

    private CountDownTimer countDownTimer;
    private static final long startedTime = 600000;
    private long timeLeft ;
    private long finalTime=startedTime;
    private int seconds ;
    private int minutes ;
    private boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        textView=findViewById(R.id.txtView);
        start = findViewById(R.id.btnStart);
        stop = findViewById(R.id.btnStop);
        reset = findViewById(R.id.btnReset);

        updateCountDown();


    }
//
//    private void checkSavedInstance(Bundle savedInstanceState) {
//        if (savedInstanceState != null){
//            seconds = savedInstanceState.getInt("seconds");
//            minutes = savedInstanceState.getInt("minutes");
////            hours = savedInstanceState.getInt("hours");
//            running = savedInstanceState.getBoolean("running");
//
//
//        }
//
//    }

    private void startTimer() {
        finalTime = System.currentTimeMillis() + timeLeft;
        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long ToFinish) {
                timeLeft = ToFinish;
                updateCountDown();

            }
            @Override
            public void onFinish() {
                running = false;
                ButtonsChange();
            }
        }.start();
        running = true;
        ButtonsChange();
    }
    private void updateCountDown() {
        int minutes =(int) (timeLeft / 1000) / 60;
        int  seconds =(int) (timeLeft / 1000) % 60;

        String timeLeftFormat = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textView.setText(timeLeftFormat);
    }

    public void onClickStart(View view) {
        running = true;
        startTimer();
    }

    public void onClickStop(View view) {
        running = false;
        countDownTimer.cancel();
        ButtonsChange();

    }

    public void resetTimer(View view) {
        timeLeft = startedTime;
        updateCountDown();
        ButtonsChange();
    }
    private void ButtonsChange() {
        if (running) {
            reset.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
        } else {
            start.setVisibility(View.VISIBLE);
            if (timeLeft < 1000) {
                stop.setVisibility(View.INVISIBLE);
            } else {
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);

            }
            if (timeLeft < startedTime) {
                reset.setVisibility(View.VISIBLE);
            } else {
                reset.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putLong("seconds", seconds);
//        bundle.putLong("finalTime", finalTime);
        bundle.putBoolean("running", running);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeLeft = savedInstanceState.getLong("timeLeft");
        running = savedInstanceState.getBoolean("running");

        updateCountDown();
        if (running) {
            finalTime = savedInstanceState.getLong("finalTime");
            timeLeft = finalTime - System.currentTimeMillis();
            startTimer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("timeLeft", timeLeft);
        editor.putBoolean("running", running);
        editor.putLong("finalTime", finalTime);
        editor.apply();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        timeLeft = prefs.getLong("timeLeft", startedTime);
        running = prefs.getBoolean("running", false);
        updateCountDown();
        if (running) {
            finalTime = prefs.getLong("finalTime", 0);
            timeLeft = finalTime - System.currentTimeMillis();
            if (timeLeft < 0) {
                timeLeft = 0;
                running = false;
                updateCountDown();
            } else {
                startTimer();
            }
        }
    }

}