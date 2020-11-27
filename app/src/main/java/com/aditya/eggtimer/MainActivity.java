package com.aditya.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView3;
    CountDownTimer countDownTimer;
    SeekBar seekBar;
   final int startingPosition = 5; //30 is in seconds seekBar starting position
    MediaPlayer mediaPlayer;

    //code to handle images of a button
    Boolean imageView1isShown = true; //this will allow to change the image over and over again back and forth
    Boolean TimerIsActive = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekBar);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView3 = (TextView) findViewById(R.id.textView3);
        int max = 600; //600 is in seconds
        seekBar.setMax(max);
        seekBar.setProgress(startingPosition);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("seekbar", Integer.toString(progress));
                int min =5;
                if(progress<min)
                {
                   seekBar.setProgress(startingPosition);
                }
                else
                {
                    //calling Update Timer function
                    UpdateTimer(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /*
                keeping  mediaPlayer.stop(); in try catch will prevent the application from crashing when the seekbar is adjusted when the
                alaram is not playing when the app is first booted
                 */
                try {
                    mediaPlayer.stop(); //this will stop the music that is being played by the alarm when the seekBar is slided
                }
                catch(Exception e)
                {
                    Log.i("Error slider", "seekbar error");
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //setting up on click listener on the textView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity2.class);
                startActivity(intent);
            }
        });
    }

    public void resetCountDownTimer() //function for resetting the countDownTimer
    {
        textView3.setText("0:5");
        seekBar.setProgress(startingPosition);
        seekBar.setEnabled(true);
        countDownTimer.cancel(); //this will stop the timer
        TimerIsActive =false;
    }

    //function that is created to handle the click of the button
    public void buttonClicked(View view) {
        if(TimerIsActive){
            resetCountDownTimer();
        }
        else{
            TimerIsActive = true; //this indicates that the timer is currently active
            seekBar.setEnabled(false); //after pressing the start timer button this method will Disable seekbar
            Log.i("Button", "Button pressed");

            //Timer related Logic
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000 + 100, 1000) {
                public void onTick(long MillisecondsUntilDone) {
                    Log.i("left", String.valueOf(MillisecondsUntilDone / 1000));
                    //calling UpdateTimer() method that we just created
                    UpdateTimer((int) MillisecondsUntilDone / 1000);
                }

                public void onFinish() {
                    Log.i("we are done", "No more countdown");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alaram_ii);
                    mediaPlayer.start();
                    resetCountDownTimer();
                }
            }.start();
        }
            //code to handle animations of the button
            ImageView start = (ImageView) findViewById(R.id.start_button);
            ImageView stop = (ImageView) findViewById(R.id.stop_button);
            if (imageView1isShown) {
                imageView1isShown = false;
                start.animate().rotation(1800).alpha(0).setDuration(2000);
                stop.animate().rotation(3600).alpha(1).setDuration(2000);
            } else {
                imageView1isShown = true;
                start.animate().rotation(3600).alpha(1).setDuration(2000);
                stop.animate().rotation(1800).alpha(0).setDuration(2000);
            }
    }

    public void UpdateTimer(int SecondsLeft) //function created to handle time
    {
        //Logic for converting seconds into minutes and seconds so that it can be displayed in the clock format
        int minutes = SecondsLeft/60 ;
        int seconds = SecondsLeft % 60;
        textView3.setText(minutes + " : " + seconds);
    }

}