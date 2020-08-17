package com.example.temidemoforfebruary_19;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;



import com.robotemi.sdk.Robot;

public class TransitionToUpperLeftCorner extends TransitionDemo{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upper_left_corner);
        verifyStoragePermissions(this);
        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.

        getFrames();

        faceFragmentFrame.setOnClickListener(new View.OnClickListener() {
            float scaleX = 0.5f;
            float scaleY = 0.5f;
            @Override
            public void onClick(View v) {
                if (state == States.INIT){
                    state = States.TRANS_TO;
                    Log.i("listener","working inside of listener");
                    faceFragmentFrame.animate()
                            .scaleX(scaleX)
                            .scaleY(scaleY)
                            .x(-faceFragmentFrame.getWidth()*scaleX/2f)
                            .y(-faceFragmentFrame.getHeight()*scaleY/2f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    //faceFragmentFrame.setVisibility(View.GONE);
                                    state = States.WAIT;
                                }
                            });




                }
                else if (state == States.END){
                    startActivity(new Intent(TransitionToUpperLeftCorner.this, TransitionStart.class));
                    finish();
                }

            }

        });


        content.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if (state == States.WAIT){
                    state = States.TRANS_FROM;
                    faceFragmentFrame.animate()
                            .scaleY(1)
                            .scaleX(1)
                            .y(0)
                            .x(0)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    //faceFragmentFrame.setVisibility(View.GONE);
                                    state = States.END;
                                }
                            });

                }

            }
        });




    }

}