package com.example.tictactoe;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FullscreenActivity extends AppCompatActivity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 0;
    private static final int UI_ANIMATION_DELAY = 0;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };



     int currPlayer = 1;
     int[][] gameState = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};

    public void playerTap(View view){
        ImageView img = (ImageView) view;
        int row = Integer.parseInt(String.valueOf(img.getTag().toString().charAt(1)));
        int col = Integer.parseInt(String.valueOf(img.getTag().toString().charAt(2)));
        if(gameState[row-1][col-1]==-1){
            gameState[row-1][col-1] = currPlayer;
            img.setTranslationY(-1000f);
            if(currPlayer==0) {
                currPlayer = 1;
                img.setImageResource(R.drawable.o);
            }
            else{
                currPlayer = 0;
                img.setImageResource(R.drawable.x);
            }
            img.animate().translationYBy(1000f).setDuration(300);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        Button reset_btn = (Button) findViewById(R.id.button);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i=0; i<3; i++){
                    for(int j=0; j<3; j++){
                        gameState[i][j] = -1;
                    }
                }
                ImageView img = findViewById(R.id.x11);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x12);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x13);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x21);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x22);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x23);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x31);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x32);
                img.setImageResource(R.drawable.b);
                img = findViewById(R.id.x33);
                img.setImageResource(R.drawable.b);
                currPlayer = 1;
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}