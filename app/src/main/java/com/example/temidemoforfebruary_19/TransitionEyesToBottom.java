package com.example.temidemoforfebruary_19;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.List;


public class TransitionEyesToBottom extends TransitionDemo{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eyes_to_bottom);
        //verifyStoragePermissions(this);
        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.

        getFrames();

        faceFragmentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*I have replaced some of his variables that were used for the
                *   stand in face with the variables needed for the new temi-robot
                *   face. The variables eye, and guide are now replaced with top_guide,
                *   bottom_guide, and distance. I use these variables to accurately get the
                *   measurements of the eye's size and position, and how far I should should
                *   go to transition the eyes to the bottom without cutting parts of the eyes out.
                *                                                       -Ese
                */
                //View eye = findViewById(R.id.rightEye);
                //View guide = findViewById(R.id.eyeGuide);
                View top_guide = findViewById(R.id.eyebrow_height_start_guide);
                View bottom_guide = findViewById(R.id.eye_end_height_guide);
                float distance = top_guide.getY() - bottom_guide.getY();

                if (state == States.INIT) {
                    Log.i("listener", "working inside of listener");
                    state = States.TRANS_TO;
                    faceFragmentFrame.animate()
                            //I added these new constraints due to new face layout - Ese
                            .y(faceFragmentFrame.getHeight() - top_guide.getY() - bottom_guide.getY() - distance / 2f)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    //faceFragmentFrame.setVisibility(View.GONE);
                                    state = States.WAIT;
                                }
                            });
                }
                else if (state == States.WAIT){
                    state = States.TRANS_FROM;
                    faceFragmentFrame.animate()
                            .y(0)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    //faceFragmentFrame.setVisibility(View.GONE);
                                    state = States.END;
                                }
                            });
                }
                else if (state == States.END){
                    startActivity(new Intent(TransitionEyesToBottom.this, TransitionStart.class));
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
                            .y(0)
                            .setDuration(1000)
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
