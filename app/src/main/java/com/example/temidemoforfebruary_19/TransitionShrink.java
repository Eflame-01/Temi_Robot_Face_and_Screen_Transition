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


public class TransitionShrink extends TransitionDemo{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shrink);
        verifyStoragePermissions(this);
        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.

        getFrames();

        faceFragmentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == States.INIT){
                    state = States.TRANS_TO;
                    Log.i("listener","working inside of listener");
                    faceFragmentFrame.animate()
                            .scaleX(0)
                            .scaleY(0)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    state=States.WAIT;
                                    faceFragmentFrame.setVisibility(View.GONE);
                                }
                            });




                }
                else if (state == States.END){
                    startActivity(new Intent(TransitionShrink.this, TransitionStart.class));
                    finish();
                }

            }
        });

        content.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if (state == States.WAIT){
                    state = States.TRANS_FROM;
                    faceFragmentFrame.setVisibility(View.VISIBLE);
                    faceFragmentFrame.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(1000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    state=States.END;
                                }
                            });
                }

            }
        });

    }
}
