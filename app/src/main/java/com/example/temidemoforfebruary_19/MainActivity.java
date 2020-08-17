package com.example.temidemoforfebruary_19;

import android.Manifest;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.robotemi.sdk.Robot;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.MediaObject;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamObject;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener,
        OnConstraintBeWithStatusChangedListener,
        OnDetectionStateChangedListener,
        Robot.AsrListener
        {

    // Storage Permissions
    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.
        setButtons();
     }

    void setButtons(){
        Button but0 = findViewById(R.id.menuButton0);
        Button but1 = findViewById(R.id.menuButton1);
        Button but2 = findViewById(R.id.menuButton2);
        Button but3 = findViewById(R.id.menuButton3);
        Button but4 = findViewById(R.id.menuButton4);
        Button but5 = findViewById(R.id.menuButton5);

        but0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TransitionToUpperLeftCorner.class);
                    startActivity(i);
            }
        });
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TransitionEyesToBottom.class);
                startActivity(i);
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TransitionSwipe.class);
                startActivity(i);
            }
        });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TransitionFade.class);
                startActivity(i);
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,TransitionShrink.class);
                startActivity(i);
            }
        });
        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        Robot.getInstance().addOnRobotReadyListener(this);
        Robot.getInstance().addNlpListener(this);
        Robot.getInstance().addOnBeWithMeStatusChangedListener(this);
        Robot.getInstance().addOnGoToLocationStatusChangedListener(this);
        Robot.getInstance().addConversationViewAttachesListenerListener(this);
        Robot.getInstance().addWakeupWordListener(this);
        Robot.getInstance().addTtsListener(this);
        Robot.getInstance().addOnLocationsUpdatedListener(this);


    }
    protected void onStop() {
        super.onStop();
        Robot.getInstance().removeOnRobotReadyListener(this);
        Robot.getInstance().removeNlpListener(this);
        Robot.getInstance().removeOnBeWithMeStatusChangedListener(this);
        Robot.getInstance().removeOnGoToLocationStatusChangedListener(this);
        Robot.getInstance().removeConversationViewAttachesListenerListener(this);
        Robot.getInstance().removeWakeupWordListener(this);
        Robot.getInstance().removeTtsListener(this);
        Robot.getInstance().removeOnLocationsUpdateListener(this);
    }


    //Implementations of required interfaces, regardless of whether applied:
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    public static void verifyStoragePermissions(Activity activity) {

    }
    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void initViews() {

    }
    public void speak(View view) {}

    /**
     * tiltAngle controls temi's head by specifying which angle you want
     * to tilt to and at which speed.
     */
    public void tiltAngle(View view) {
        robot.tiltAngle(23, 5.3F);
    }
    public void turnBy(View view) {
        robot.turnBy(180, 6.2F);
    }
    public void tiltBy(View view) {
        robot.tiltBy(70, 1.2F);
    }
    public void getBatteryData(View view) {}
    public void savedLocationsDialog(View view) {}
    @Override
    public void onNlpCompleted(NlpResult nlpResult) {}
    @Override
    public void onWakeupWord(String wakeupWord, int direction) {
        // Do anything on wakeup. Follow, go to location, or even try creating dance moves.
    }
    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        // Do whatever you like upon the status changing. after the robot finishes speaking
    }
    @Override
    public void onBeWithMeStatusChanged(String status) {}
    @Override
    public void onGoToLocationStatusChanged(String location, String status, int descriptionId, String description) {}
    @Override
    public void onConversationAttaches(boolean isAttached) {}
    @Override
    public void onPublish(ActivityStreamPublishMessage message) {}
    @Override
    public void onLocationsUpdated(List<String> locations) {}
    @Override
    public void onConstraintBeWithStatusChanged(boolean isConstraint) {
        Log.d("onConstraintBeWith", "status = " + isConstraint);
    }
    @Override
    public void onDetectionStateChanged(int state) {}
    @Override
    public void onAsrResult(@NonNull String asrResult){
        Log.d("onAsrResult", "asrResult = " + asrResult);
    }


}
