package com.example.temidemoforfebruary_19;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.robotemi.sdk.Robot;
import androidx.annotation.NonNull;

import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;


import java.util.List;


public abstract class TransitionDemo extends AppCompatActivity implements
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
        Robot.AsrListener,
        FaceFragment.OnFragmentInteractionListener,
        SampleQuiz.OnQuizInteractionListener
{

    //I added these in order to implement my Face and Gaze code into the Temi-SDK and the TransitionDemo -Ese
    @SuppressLint("StaticFieldLeak")
    protected static Face face;
    protected static Handler handler = new Handler();


    public enum States {INIT, TRANS_TO, WAIT, TRANS_FROM,END}

    // Storage Permissions
    protected static Robot robot;


    //fields for modifying stuff
    protected View frame; //overall frame for the layout - get this in order to control touch listeners
    protected View faceFragmentFrame;
    protected States state;
    protected View content; //placeholder UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.
        state = States.INIT;
    }

    //function for getting certain view components with the same tags within each layout -- not considered good practice
    protected void getFrames(){
        frame = findViewById(R.id.main_activity_frame);
        faceFragmentFrame = findViewById(R.id.face_fragment);
        content = findViewById(R.id.content);
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

        /*Since the imageViews on the fragment aren't created in the Activity Lifecycle
        * until you get to onStart(), it is better to instantiate the face in the onStart()
        * method.
        *                                                                   - Ese
        */
        face = new Face(this);
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


    //TODO: Keep these functions because they cause errors in the other subclasses when deleted
    //Implements fragment method for face
    public void onFragmentInteraction(Uri u){

    }
    //Implements fragment method for quiz
    public void onQuizInteraction(Uri u){

    }


    //Implementations of required interfaces, regardless of whether applied:
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        /*int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }*/
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
        //etSpeak = findViewById(R.id.etSpeak);
        //etSaveLocation = findViewById(R.id.etSaveLocation);
        //etGoTo = findViewById(R.id.etGoTo);
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
        /*I used the onWakeupWord() method as a test for the gaze aversion and other features.
         * You can delete this if need be.
         *                                                  - Ese
         */
        face.changeGazeTo(Gaze.PERFORM_ALL);
        //END
    }
    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        // Do whatever you like upon the status changing. after the robot finishes speaking
    }
    @Override
    public void onBeWithMeStatusChanged(String status) {
        //  When status changes to "lock" the robot recognizes the user and begin to follow.
        switch (status) {
            case "abort":
                // do something i.e. speak
                robot.speak(TtsRequest.create("Abort", false));
                face.changeColorTo(getResources().getColor(R.color.white));
                face.changeFaceTo(Mood.SADNESS);
                break;

            case "calculating":
                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case "lock":
                robot.speak(TtsRequest.create("Lock", false));
                break;

            case "search":
                robot.speak(TtsRequest.create("Search", false));
                face.changeColorTo(getResources().getColor(R.color.yellow));
                face.changeFaceTo(Mood.NEUTRAL);
                break;

            case "start":
                robot.speak(TtsRequest.create("Start", false));
                face.changeColorTo(getResources().getColor(R.color.lightGreen));
                face.changeFaceTo(Mood.NEUTRAL);
                break;

            case "track":
                robot.speak(TtsRequest.create("Track", false));
                face.changeColorTo(getResources().getColor(R.color.forestGreen));
                face.changeFaceTo(Mood.HAPPY);
                break;
        }
    }
    @Override
    public void onGoToLocationStatusChanged(String location, String status, int descriptionId, String description) {}
    @Override
    public void onConversationAttaches(boolean isAttached) {}
    @Override
    public void onPublish(ActivityStreamPublishMessage message) {}
    @Override
    public void onLocationsUpdated(List<String> locations) {
    }
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
